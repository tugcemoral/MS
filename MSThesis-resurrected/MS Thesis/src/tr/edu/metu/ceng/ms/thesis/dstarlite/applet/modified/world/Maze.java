/*
 * Copyright 2009 Luis Henrique O. Rios
 *
 * This file is part of D* Lite Demonstration Applet.
 *
 * D* Lite Demonstration Applet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * D* Lite Demonstration Applet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with D* Lite Demonstration Applet.  If not, see <http://www.gnu.org/licenses/>.
 */
package tr.edu.metu.ceng.ms.thesis.dstarlite.applet.modified.world;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

public class Maze {
	
	private int size_x, size_y;
	private Cell cells[][];
	private Cell original_start, start, goal, robot_cell;

	private boolean diagonal;
	private int n_directions;

	private int iteration;

	private Key.KeyComparator cde;
	private TreeSet<Key> u;

	private static final int N_DIRECTIONS_WITHOUT_DIAGONALS = 4;
	private static final int N_DIRECTIONS_WITH_DIAGONALS = 8;

	private static final int d_x[] = { 0, 1, -1, 0, 1, 1, -1, -1 };
	private static final int d_y[] = { 1, 0, 0, -1, -1, 1, 1, -1 };

	private static final String infinity = Character.toString((char) 0x221E);

	private static final Color path_color = new Color(100, 100, 255, 255);
	private static final Color wall_color = new Color(255, 100, 100, 255);
	private static final Color visited_cell_color = new Color(150, 200, 150,
			255);

	public Maze(double seed, int size_x, int size_y) {
		int x, y;
		Random rand = new Random((long)seed);

		iteration = 0;
		diagonal = false;
		n_directions = N_DIRECTIONS_WITHOUT_DIAGONALS;

		cde = new Key.KeyComparator();
		u = new TreeSet<Key>(cde);

		this.size_x = size_x;
		this.size_y = size_y;

		cells = new Cell[size_x][size_y];
		for (x = 0; x < size_x; x++) {
			for (y = 0; y < size_y; y++) {
				cells[x][y] = new Cell(x, y);
			}
		}

		/* Choose the start cell. */
		x = ((int) (rand.nextDouble() * Integer.MAX_VALUE)) % size_x;
		y = ((int) (rand.nextDouble() * Integer.MAX_VALUE)) % size_y;
		original_start = robot_cell = start = cells[x][y];

		/* Choose the goal cell. */
		do {
			x = ((int) (rand.nextDouble() * Integer.MAX_VALUE)) % size_x;
			y = ((int) (rand.nextDouble() * Integer.MAX_VALUE)) % size_y;
			goal = cells[x][y];
		} while (start.x == goal.x && start.y == goal.y);

		for (x = 0; x < size_x; x++) {
			for (y = 0; y < size_y; y++) {
				// Manhattan.
				cells[x][y].h = Math.abs(x - goal.x) + Math.abs(y - goal.y);
				cells[x][y].iteration = iteration;

				if (cells[x][y] != start && cells[x][y] != goal) {
					if (rand.nextDouble() <= 0.25) {
						cells[x][y].real_type = Cell.WALL;
					}
				}
			}
		}
	}

	private void copyMaze() {
		int x, y;
		for (x = 0; x < size_x; x++) {
			for (y = 0; y < size_y; y++) {
				cells[x][y].type_robot_vision = cells[x][y].real_type;
			}
		}
	}

	public void setDiagonal(boolean diagonal) {
		this.diagonal = diagonal;

		if (diagonal) {
			n_directions = N_DIRECTIONS_WITH_DIAGONALS;
		} else {
			n_directions = N_DIRECTIONS_WITHOUT_DIAGONALS;
		}

		initialize();
	}

	public void setGoal(int x, int y) {
		Point indexes;

		indexes = convertCoordinatesToIndexes(x, y);
		if (indexes == null)
			return;
		x = indexes.x;
		y = indexes.y;

		if (cells[x][y] == start) {
			return;
		}
		goal = cells[x][y];
		goal.real_type = goal.type_robot_vision = Cell.FREE;

		initialize();
	}

	public void setStart(int x, int y) {
		Point indexes;

		indexes = convertCoordinatesToIndexes(x, y);
		if (indexes == null)
			return;
		x = indexes.x;
		y = indexes.y;

		if (cells[x][y] == goal) {
			return;
		}
		robot_cell = original_start = start = cells[x][y];
		start.real_type = start.type_robot_vision = Cell.FREE;

		initialize();
	}

	private Point convertCoordinatesToIndexes(int x, int y) {
		Point indexes;
		int i, j;

		if (x < ORIGIN_X || y < ORIGIN_Y)
			return null;

		x -= ORIGIN_X;
		y -= ORIGIN_Y;

		i = x / SIDE_SIZE;
		j = y / SIDE_SIZE;

		if (!(0 <= i && i < size_x && 0 <= j && j < size_y))
			return null;

		indexes = new Point(i, j);

		return indexes;
	}

	public void printCell(int x, int y) {
		Point indexes;
		int i, j;
		Cell cell;

		indexes = convertCoordinatesToIndexes(x, y);

		if (indexes == null)
			return;

		i = indexes.x;
		j = indexes.y;
		cell = cells[i][j];

		System.out.println("I am " + cell + " and my parent is " + cell.parent);
	}

	public void transformCell(int x, int y) {
		Point indexes;
		int i, j;
		Cell cell;

		indexes = convertCoordinatesToIndexes(x, y);

		if (indexes == null)
			return;
		i = indexes.x;
		j = indexes.y;

		cell = cells[i][j];

		if (cell != start && cell != goal && cell != robot_cell) {
			if (cell.real_type == Cell.WALL) {
				cell.real_type = Cell.FREE;
			} else {
				cell.real_type = Cell.WALL;
			}
		}
	}

	private static final int SIDE_SIZE = 50;
	private static final int SELECTION_EDGE = 3;
	private static final int COMMON_EDGE = 1;
	private static final int ORIGIN_X = SIDE_SIZE, ORIGIN_Y = SIDE_SIZE;

	public Dimension getPrintedMazeSize() {
		return new Dimension(SIDE_SIZE * (size_x + 1), SIDE_SIZE * (size_y + 1));
	}

	private void printCellInfo(Graphics2D g2, Cell cell) {
		String g, rhs;
		Rectangle2D r;
		int x = ORIGIN_X + cell.x * SIDE_SIZE;
		int y = ORIGIN_Y + cell.y * SIDE_SIZE;

		g2.setColor(Color.black);

		if (cell.g < Integer.MAX_VALUE) {
			g = Integer.toString(cell.g);
		} else {
			g = infinity;
		}

		if (cell.rhs < Integer.MAX_VALUE) {
			rhs = Integer.toString(cell.rhs);
		} else {
			rhs = infinity;
		}

		r = g2.getFontMetrics().getStringBounds(g + "/" + rhs + "/" + cell.h,
				g2);
		y = y + (SIDE_SIZE + (int) r.getHeight()) / 2;
		x = x + (SIDE_SIZE - (int) r.getWidth()) / 2;
		g2.drawString(g + "/" + rhs + "/" + cell.h, x, y);
	}

	private void drawRobot(Graphics2D g2, Cell cell) {
		Rectangle2D r;
		int x = ORIGIN_X + cell.x * SIDE_SIZE;
		int y = ORIGIN_Y + cell.y * SIDE_SIZE;

		g2.setColor(Color.black);
		r = g2.getFontMetrics().getStringBounds("R", g2);
		y = y + (SIDE_SIZE + (int) r.getHeight()) / 2;
		x = x + (SIDE_SIZE - (int) r.getWidth()) / 2;
		g2.drawString("R", x, y);
	}

	public void drawMaze(Graphics2D g2) {
		int x, y, i, j;
		int edge, side;
		Rectangle2D r;
		GeneralPath rhombus;
		Font f;

		f = g2.getFont();
		g2.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 25));

		side = SIDE_SIZE - (COMMON_EDGE * 2);

		for (i = 0; i < size_x; i++) {
			x = ORIGIN_X + i * SIDE_SIZE;
			y = 0;
			g2.setColor(Color.white);
			g2.fillRect(x + COMMON_EDGE, y + COMMON_EDGE, side, side);
			r = g2.getFontMetrics()
					.getStringBounds(Integer.toString(i + 1), g2);
			y = y + (SIDE_SIZE + (int) r.getHeight()) / 2;
			x = x + (SIDE_SIZE - (int) r.getWidth()) / 2;
			g2.setColor(Color.black);
			g2.drawString(Integer.toString(i + 1), x, y);
		}

		for (i = 0; i < size_y; i++) {
			x = 0;
			y = ORIGIN_X + i * SIDE_SIZE;
			g2.setColor(Color.white);
			g2.fillRect(x + COMMON_EDGE, y + COMMON_EDGE, side, side);
			r = g2.getFontMetrics().getStringBounds(Integer.toString(i), g2);
			y = y + (SIDE_SIZE + (int) r.getHeight()) / 2;
			x = x + (SIDE_SIZE - (int) r.getWidth()) / 2;
			g2.setColor(Color.black);
			g2.drawString(Character.toString((char) (i + 'A')), x, y);
		}

		g2.setFont(f);

		for (i = 0; i < size_x; i++) {
			for (j = 0; j < size_y; j++) {
				x = ORIGIN_X + i * SIDE_SIZE;
				y = ORIGIN_Y + j * SIDE_SIZE;
				g2.setColor(Color.black);
				g2.fillRect(x, y, SIDE_SIZE, SIDE_SIZE);
				switch (cells[i][j].type_robot_vision) {
				case Cell.WALL:
					g2.setColor(wall_color);
					g2.fillRect(x + COMMON_EDGE, y + COMMON_EDGE, side, side);
					break;
				case Cell.PATH:
					g2.setColor(path_color);
					g2.fillRect(x + COMMON_EDGE, y + COMMON_EDGE, side, side);
					break;
				case Cell.FREE:
					if (cells[i][j].iteration == iteration) {
						g2.setColor(visited_cell_color);
					} else if (cells[i][j].iteration != 0) {
						g2.setColor(Color.lightGray);
					} else {
						g2.setColor(Color.white);
					}
					if (u.contains(new Key(cells[i][j]))) {
						edge = SELECTION_EDGE;
					} else {
						edge = COMMON_EDGE;
					}

					g2.fillRect(x + edge, y + edge, SIDE_SIZE - (edge * 2),
							SIDE_SIZE - (edge * 2));
					break;
				}

				if (cells[i][j] != start && cells[i][j] != goal
						&& cells[i][j].type_robot_vision != Cell.WALL) {
					printCellInfo(g2, cells[i][j]);
				}
			}
		}

		/* Start. */
		x = ORIGIN_X + start.x * SIDE_SIZE;
		y = ORIGIN_Y + start.y * SIDE_SIZE;

		g2.setColor(Color.black);
		g2.fillRect(x, y, SIDE_SIZE, SIDE_SIZE);

		g2.setColor(Color.white);
		rhombus = new GeneralPath();
		if (u.contains(new Key(start))) {
			edge = SELECTION_EDGE;
		} else {
			edge = COMMON_EDGE;
		}

		side = SIDE_SIZE - (edge) * 2;

		g2.fillRect(x + edge, y + edge, side, side);

		rhombus.moveTo(side / 2, 0);
		rhombus.lineTo(side, side / 2);
		rhombus.lineTo(side / 2, side);
		rhombus.lineTo(0, side / 2);
		rhombus.lineTo(side / 2, 0);

		g2.setColor(Color.orange);
		g2.translate(x + edge, y + edge);
		g2.fill(rhombus);
		g2.translate(-(x + edge), -(y + edge));

		printCellInfo(g2, start);

		/* Goal. */
		x = ORIGIN_X + goal.x * SIDE_SIZE;
		y = ORIGIN_Y + goal.y * SIDE_SIZE;

		g2.setColor(Color.black);
		g2.fillRect(x, y, SIDE_SIZE, SIDE_SIZE);

		g2.setColor(Color.white);
		rhombus = new GeneralPath();
		if (u.contains(new Key(goal))) {
			edge = SELECTION_EDGE;
		} else {
			edge = COMMON_EDGE;
		}

		side = SIDE_SIZE - (edge) * 2;

		g2.fillRect(x + edge, y + edge, side, side);

		rhombus.moveTo(side / 2, 0);
		rhombus.lineTo(side, side / 2);
		rhombus.lineTo(side / 2, side);
		rhombus.lineTo(0, side / 2);
		rhombus.lineTo(side / 2, 0);

		g2.setColor(Color.orange);
		g2.translate(x + edge, y + edge);
		g2.fill(rhombus);
		g2.translate(-(x + edge), -(y + edge));

		g2.setColor(Color.black);
		r = g2.getFontMetrics().getStringBounds("G", g2);
		y = y + (SIDE_SIZE + (int) r.getHeight()) / 2;
		x = x + (SIDE_SIZE - (int) r.getWidth()) / 2;

		g2.drawString("G", x, y);
	}

	public void drawRealMaze(Graphics2D g2) {
		int x, y, i, j;
		int side;
		Rectangle2D r;
		GeneralPath rhombus;
		Font f;

		f = g2.getFont();
		g2.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 25));

		side = SIDE_SIZE - (COMMON_EDGE * 2);

		for (i = 0; i < size_x; i++) {
			x = ORIGIN_X + i * SIDE_SIZE;
			y = 0;
			g2.setColor(Color.white);
			g2.fillRect(x + COMMON_EDGE, y + COMMON_EDGE, side, side);
			r = g2.getFontMetrics()
					.getStringBounds(Integer.toString(i + 1), g2);
			y = y + (SIDE_SIZE + (int) r.getHeight()) / 2;
			x = x + (SIDE_SIZE - (int) r.getWidth()) / 2;
			g2.setColor(Color.black);
			g2.drawString(Integer.toString(i + 1), x, y);
		}

		for (i = 0; i < size_y; i++) {
			x = 0;
			y = ORIGIN_X + i * SIDE_SIZE;
			g2.setColor(Color.white);
			g2.fillRect(x + COMMON_EDGE, y + COMMON_EDGE, side, side);
			r = g2.getFontMetrics().getStringBounds(Integer.toString(i), g2);
			y = y + (SIDE_SIZE + (int) r.getHeight()) / 2;
			x = x + (SIDE_SIZE - (int) r.getWidth()) / 2;
			g2.setColor(Color.black);
			g2.drawString(Character.toString((char) (i + 'A')), x, y);
		}

		g2.setFont(f);

		for (i = 0; i < size_x; i++) {
			for (j = 0; j < size_y; j++) {
				x = ORIGIN_X + i * SIDE_SIZE;
				y = ORIGIN_Y + j * SIDE_SIZE;
				g2.setColor(Color.black);
				g2.fillRect(x, y, SIDE_SIZE, SIDE_SIZE);
				switch (cells[i][j].real_type) {
				case Cell.WALL:
					g2.setColor(wall_color);
					g2.fillRect(x + COMMON_EDGE, y + COMMON_EDGE, side, side);
					break;
				case Cell.PATH:
					g2.setColor(path_color);
					g2.fillRect(x + COMMON_EDGE, y + COMMON_EDGE, side, side);
					break;
				case Cell.FREE:
					g2.setColor(Color.white);
					g2.fillRect(x + COMMON_EDGE, y + COMMON_EDGE, SIDE_SIZE
							- (COMMON_EDGE * 2), SIDE_SIZE - (COMMON_EDGE * 2));
					break;
				}
			}
		}

		/* Start. */
		x = ORIGIN_X + original_start.x * SIDE_SIZE;
		y = ORIGIN_Y + original_start.y * SIDE_SIZE;

		g2.setColor(Color.black);
		g2.fillRect(x, y, SIDE_SIZE, SIDE_SIZE);

		g2.setColor(Color.white);
		rhombus = new GeneralPath();

		side = SIDE_SIZE - (COMMON_EDGE) * 2;

		g2.fillRect(x + COMMON_EDGE, y + COMMON_EDGE, side, side);

		rhombus.moveTo(side / 2, 0);
		rhombus.lineTo(side, side / 2);
		rhombus.lineTo(side / 2, side);
		rhombus.lineTo(0, side / 2);
		rhombus.lineTo(side / 2, 0);

		g2.setColor(Color.orange);
		g2.translate(x + COMMON_EDGE, y + COMMON_EDGE);
		g2.fill(rhombus);
		g2.translate(-(x + COMMON_EDGE), -(y + COMMON_EDGE));

		g2.setColor(Color.black);
		r = g2.getFontMetrics().getStringBounds("S", g2);
		y = y + (SIDE_SIZE + (int) r.getHeight()) / 2;
		x = x + (SIDE_SIZE - (int) r.getWidth()) / 2;

		g2.drawString("S", x, y);

		/* Goal. */
		x = ORIGIN_X + goal.x * SIDE_SIZE;
		y = ORIGIN_Y + goal.y * SIDE_SIZE;

		g2.setColor(Color.black);
		g2.fillRect(x, y, SIDE_SIZE, SIDE_SIZE);

		g2.setColor(Color.white);
		rhombus = new GeneralPath();

		side = SIDE_SIZE - (COMMON_EDGE) * 2;

		g2.fillRect(x + COMMON_EDGE, y + COMMON_EDGE, side, side);

		rhombus.moveTo(side / 2, 0);
		rhombus.lineTo(side, side / 2);
		rhombus.lineTo(side / 2, side);
		rhombus.lineTo(0, side / 2);
		rhombus.lineTo(side / 2, 0);

		g2.setColor(Color.orange);
		g2.translate(x + COMMON_EDGE, y + COMMON_EDGE);
		g2.fill(rhombus);
		g2.translate(-(x + COMMON_EDGE), -(y + COMMON_EDGE));

		g2.setColor(Color.black);
		r = g2.getFontMetrics().getStringBounds("G", g2);
		y = y + (SIDE_SIZE + (int) r.getHeight()) / 2;
		x = x + (SIDE_SIZE - (int) r.getWidth()) / 2;

		g2.drawString("G", x, y);

		drawRobot(g2, robot_cell);
	}

	public String getStack() {
		String s = new String();
		Iterator<Key> i = u.iterator();

		while (i.hasNext()) {
			s += i.next().toString() + "\n";
		}

		return s;
	}

	private int H(Cell cell) {
		if (!diagonal) {
			return Math.abs(cell.x - start.x) + Math.abs(cell.y - start.y); /*
																			 * Manhattan
																			 * .
																			 */
		} else {
			return Math.max(Math.abs(cell.x - start.x),
					Math.abs(cell.y - start.y));
		}
	}

	/* D* Lite. */
	private void clearPath() {
		int x, y;

		for (x = 0; x < size_x; x++) {
			for (y = 0; y < size_y; y++) {
				if (cells[x][y].type_robot_vision == Cell.PATH) {
					cells[x][y].type_robot_vision = Cell.FREE;
				}
			}
		}
	}

	private boolean markPath() {
		int smallest_g;
		Cell next_cell, cell;

		if (start.g == Integer.MAX_VALUE)
			return false;

		cell = start;
		while (cell != goal) {

			smallest_g = cell.g;
			next_cell = null;

			for (int i = 0; i < n_directions; i++) {
				int x, y;
				x = cell.x + d_x[i];
				y = cell.y + d_y[i];

				if (0 <= x && x < size_x && 0 <= y && y < size_y
						&& cells[x][y].type_robot_vision != Cell.WALL) {

					if (cells[x][y].g < smallest_g) {
						smallest_g = cells[x][y].g;
						next_cell = cells[x][y];
					}
				}
			}

			cell.type_robot_vision = Cell.PATH;
			cell = next_cell;
		}
		return true;
	}

	public void initialize() {
		int x, y;
		iteration = 0;

		clearPath();
		copyMaze();

		robot_cell = start = original_start;

		for (x = 0; x < size_x; x++) {
			for (y = 0; y < size_y; y++) {
				cells[x][y].g = cells[x][y].rhs = Integer.MAX_VALUE;
				cells[x][y].h = H(cells[x][y]);
				cells[x][y].iteration = iteration;
				cells[x][y].parent = null;
			}
		}

		goal.rhs = 0;
		iteration++;
		u.clear();
		u.add(new Key(goal));
	}

	private void updateCell(Cell cell) {
		Key old_key;
		Cell new_parent = null;

		System.out.println("Update Cell: " + cell);

		old_key = new Key(cell);
		cell.iteration = iteration;

		if (cell != goal) {
			cell.rhs = Integer.MAX_VALUE;

			for (int d = 0; d < n_directions; d++) {
				int x, y;
				x = cell.x + d_x[d];
				y = cell.y + d_y[d];

				if (0 <= x && x < size_x && 0 <= y && y < size_y
						&& cells[x][y].type_robot_vision != Cell.WALL) {

					int t;

					if (cells[x][y].g == Integer.MAX_VALUE)
						t = Integer.MAX_VALUE;
					else
						t = cells[x][y].g + 1;

					if (cell.rhs > t /* && cell != cells[x][y].parent */) {
						cell.rhs = t;
						new_parent = cells[x][y];
					}
				}
			}
		}
		u.remove(old_key);
		cell.parent = new_parent;

		System.out.println("New Parent: " + new_parent);

		if (cell.g != cell.rhs) {
			u.add(new Key(cell));
		}
	}

	public boolean executionEnd() {
		return !(!u.isEmpty() && (cde.compare(new Key(start), u.first()) > 0 || start.g != start.rhs));
	}

	public boolean reachedGoal() {
		return robot_cell == goal;
	}

	public void calculatePath(boolean singleStage) {

		while (!executionEnd()) {
			Key key;
			Cell cell;

			key = u.first();
			u.remove(key);
			cell = key.cell;

			cell.iteration = iteration;

			if (cell.g > cell.rhs) {
				cell.g = cell.rhs;

				/* The start cell cannot generate children. */
				if (cell != start) {
					for (int i = 0; i < n_directions; i++) {
						int x, y;
						x = cell.x + d_x[i];
						y = cell.y + d_y[i];

						if (0 <= x && x < size_x && 0 <= y && y < size_y
								&& cells[x][y].type_robot_vision != Cell.WALL
								&& cell.g + 1 < cells[x][y].rhs) {

							updateCell(cells[x][y]);
						}
					}
				}

			} else {
				cell.g = Integer.MAX_VALUE;

				updateCell(cell);

				// However, the start can have children because it changes
				// during the execution.
				for (int i = 0; i < n_directions; i++) {
					int x, y;
					x = cell.x + d_x[i];
					y = cell.y + d_y[i];

					if (0 <= x && x < size_x && 0 <= y && y < size_y
							&& cells[x][y].type_robot_vision != Cell.WALL
					// && cells[x][y].parent == cell
					) {

						updateCell(cells[x][y]);
					}
				}
			}

			if (singleStage)
				break;
		}

		if (executionEnd())
			markPath();

	}

	public void move(boolean singleStep) {
		int smallest_g = Integer.MAX_VALUE;
		Cell next_cell = null, cell;
		boolean changed = false;

		/* Before move the robot check for changes in the maze. */
		for (int d = 0; d < N_DIRECTIONS_WITH_DIAGONALS; d++) {
			int x, y, i, j;
			x = robot_cell.x + d_x[d];
			y = robot_cell.y + d_y[d];

			if (0 <= x && x < size_x && 0 <= y && y < size_y) {
				if (cells[x][y].type_robot_vision != cells[x][y].real_type
						&& !(cells[x][y].type_robot_vision == Cell.PATH && cells[x][y].real_type == Cell.FREE)) {

					cell = cells[x][y];
					changed = true;
					if (cell.type_robot_vision == Cell.WALL) {

						cell.type_robot_vision = Cell.FREE;
						cell.g = cell.rhs = Integer.MAX_VALUE;
						cell.parent = null;

						updateCell(cell);
						for (int k = 0; k < n_directions; k++) {
							i = cell.x + d_x[k];
							j = cell.y + d_y[k];

							if (0 <= i
									&& i < size_x
									&& 0 <= j
									&& j < size_y
									&& cells[i][j].type_robot_vision != Cell.WALL
									&& cell.g + 1 < cells[i][j].rhs) {

								updateCell(cells[i][j]);
							}
						}

					} else {
						Key old_key;
						old_key = new Key(cell);

						u.remove(old_key);

						cell.g = cell.rhs = Integer.MAX_VALUE;
						cell.parent = null;
						cell.type_robot_vision = Cell.WALL;

						for (int k = 0; k < n_directions; k++) {
							i = cell.x + d_x[k];
							j = cell.y + d_y[k];

							if (0 <= i
									&& i < size_x
									&& 0 <= j
									&& j < size_y
									&& cells[i][j].type_robot_vision != Cell.WALL
									&& cells[i][j].parent == cell) {

								updateCell(cells[i][j]);
							}
						}
					}
				}
			}
		}

		if (changed) {
			clearPath();
			start = robot_cell;
			iteration++;
			TreeSet<Key> new_u = new TreeSet<Key>(cde);
			Key key;

			while (u.size() > 0) {
				key = u.first();
				u.remove(key);
				cell = key.cell;
				new_u.add(new Key(cell));
			}
			u = new_u;
			calculatePath(singleStep);
			return;
		}

		/* If there was no change move the robot. */
		for (int d = 0; d < n_directions; d++) {
			int x, y;
			x = robot_cell.x + d_x[d];
			y = robot_cell.y + d_y[d];

			if (0 <= x && x < size_x && 0 <= y && y < size_y
					&& cells[x][y].type_robot_vision != Cell.WALL
					&& cells[x][y].g < smallest_g) {
				smallest_g = cells[x][y].g;
				next_cell = cells[x][y];
			}
		}

		if (smallest_g != Integer.MAX_VALUE) {
			clearPath();
			start = robot_cell = next_cell;
			markPath();
		}
	}
}
