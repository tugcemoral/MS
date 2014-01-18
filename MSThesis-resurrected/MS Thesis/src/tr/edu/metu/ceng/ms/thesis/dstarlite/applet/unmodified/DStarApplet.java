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
package tr.edu.metu.ceng.ms.thesis.dstarlite.applet.unmodified;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import tr.edu.metu.ceng.ms.thesis.dstarlite.applet.unmodified.world.Maze;
import tr.edu.metu.ceng.ms.thesis.dstarlite.applet.unmodified.world.MazeCanvas;

public class DStarApplet extends Applet implements ActionListener,
		ItemListener, KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private static final int APPLET_WIDTH = 1000;
	private static final int APPLET_HEIGHT = 600;

	Maze maze;
	MazeCanvas realMazeCanvas, dStarCanvas;
	boolean place_start, place_goal, single_step, executing, first_change;
	Button restart, move, nextStep, changeStart, changeGoal;
	Checkbox single_step_execution, diagonals;
	TextArea ta;

	public void init() {

		this.setLayout(new GridBagLayout());
		this.setSize(APPLET_WIDTH, APPLET_HEIGHT);

		GridBagConstraints rootGBC = new GridBagConstraints();

		maze = new Maze(Math.random() * Long.MAX_VALUE, 7, 9);
		realMazeCanvas = new MazeCanvas(maze.getPrintedMazeSize());
		dStarCanvas = new MazeCanvas(maze.getPrintedMazeSize());

		int width = (int) maze.getPrintedMazeSize().getWidth();

		//initialize title canvases.
		MazeCanvas realMazeTitleCanvas = new MazeCanvas(
				new Dimension(width, 70));
		MazeCanvas dStarMazeTitleCanvas = new MazeCanvas(new Dimension(width,
				70));
		
		realMazeTitleCanvas.drawTitle("Real Map", width);
		dStarMazeTitleCanvas.drawTitle("D* Lite", width);

		// set the titles of canvases.
//		drawTitles(realMazeTitleCanvas, dStarMazeTitleCanvas, width);

		rootGBC.insets = new Insets(0, 3, 0, 0);

		rootGBC.fill = GridBagConstraints.NONE;
		rootGBC.gridx = 0;
		rootGBC.gridy = 0;
		rootGBC.gridheight = 1;
		rootGBC.gridwidth = 1;
		this.add(realMazeTitleCanvas, rootGBC);

		rootGBC.fill = GridBagConstraints.NONE;
		rootGBC.gridx = 1;
		rootGBC.gridy = 0;
		rootGBC.gridheight = 1;
		rootGBC.gridwidth = 1;
		this.add(dStarMazeTitleCanvas, rootGBC);

		rootGBC.fill = GridBagConstraints.NONE;
		rootGBC.gridx = 0;
		rootGBC.gridy = 1;
		rootGBC.gridheight = 1;
		rootGBC.gridwidth = 1;
		this.add(realMazeCanvas, rootGBC);

		rootGBC.fill = GridBagConstraints.NONE;
		rootGBC.gridx = 1;
		rootGBC.gridy = 1;
		rootGBC.gridheight = 1;
		rootGBC.gridwidth = 1;
		this.add(dStarCanvas, rootGBC);

		rootGBC.fill = GridBagConstraints.VERTICAL;
		rootGBC.gridx = 2;
		rootGBC.gridy = 1;
		rootGBC.gridheight = 1;
		rootGBC.gridwidth = 1;
		ta = new TextArea("", 1, 12, TextArea.SCROLLBARS_NONE);
		ta.setFocusable(false);
		this.add(ta, rootGBC);

		//construct the buttons container and buttons' functionalities. 
		Container buttons = constructButtonsContainer();
		
		rootGBC.fill = GridBagConstraints.BOTH;
		rootGBC.gridx = 0;
		rootGBC.gridy = 2;
		rootGBC.gridheight = 1;
		rootGBC.gridwidth = 3;
		this.add(buttons, rootGBC);

		realMazeCanvas.addMouseListener(this);
		realMazeCanvas.addKeyListener(this);
		dStarCanvas.addMouseListener(this);
		dStarCanvas.addKeyListener(this);

		executing = single_step = place_start = place_goal = false;
		first_change = true;

		maze.initialize();
		maze.calculatePath(single_step);
		maze.drawRealMaze(realMazeCanvas.getGraphics2D());
		maze.drawMaze(dStarCanvas.getGraphics2D());

		realMazeCanvas.repaint();
		dStarCanvas.repaint();
		ta.setText(maze.getStack());

	}

	private Container constructButtonsContainer() {
		// create a container for buttons...
		Container buttons = new Container();
		// set layout of this container...
		buttons.setLayout(new FlowLayout());

		// initialize buttons.
		restart = createButton("Restart", true);
		move = createButton("Move", true);
		changeStart = createButton("Place Start", true);
		changeGoal = createButton("Place Goal", true);
		nextStep = createButton("NextStep", true);
		//add initialized buttons on to buttons component...
		buttons.add(restart);
		buttons.add(move);
		buttons.add(changeStart);
		buttons.add(changeGoal);
		buttons.add(nextStep);
		// create and add checkboxes into it.
		buttons.add(createCheckbox(single_step_execution, "Step by Step"));
		buttons.add(createCheckbox(diagonals, "Allow Diagonal Movement"));

		return buttons;
	}

	private Component createCheckbox(Checkbox cb, String text) {
		cb = new Checkbox(text);
		cb.addItemListener(this);
		return cb;
	}

	private Button createButton(String text, boolean isEnable) {
		Button button = new Button(text);
		button.setEnabled(isEnable);
		button.addActionListener(this);
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		
		return button;
	}

//	private void drawTitles(MazeCanvas a, MazeCanvas d_lite, int width) {
//		// int width,
//		int x, y;
//		Rectangle2D r;
//		Graphics2D g2;
//
//		// width = (int) maze.getPrintedMazeSize().getWidth();
//
//		g2 = a.getGraphics2D();
//		g2.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 30));
//		g2.setColor(Color.WHITE);
//		g2.fillRect(0, 0, width, 70);
//		g2.setColor(Color.BLACK);
//		r = g2.getFontMetrics().getStringBounds("Real Map", g2);
//		y = (70 + (int) r.getHeight()) / 2;
//		x = (width - (int) r.getWidth()) / 2;
//
//		g2.drawString("Real Map", x, y);
//
//		// width = (int) maze.getPrintedMazeSize().getWidth();
//
//		g2 = d_lite.getGraphics2D();
//		g2.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 30));
//		g2.setColor(Color.WHITE);
//		g2.fillRect(0, 0, width, 70);
//		g2.setColor(Color.BLACK);
//		r = g2.getFontMetrics().getStringBounds("D* Lite (Robot Vision)", g2);
//		y = (70 + (int) r.getHeight()) / 2;
//		x = (width - (int) r.getWidth()) / 2;
//
//		g2.drawString("D* Lite (Robot Vision)", x, y);
//	}

	public void stop() {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			maze.printCell(e.getX(), e.getY());
			return;
		}

		if (place_start) {
			maze.setStart(e.getX(), e.getY());
			place_start = false;
			enableButtons(true);

		} else if (place_goal) {
			maze.setGoal(e.getX(), e.getY());
			place_goal = false;
			enableButtons(true);

		} else {
			if (executing)
				return;

			maze.transformCell(e.getX(), e.getY());
			maze.drawRealMaze(realMazeCanvas.getGraphics2D());
			realMazeCanvas.repaint();

			return;
		}

		first_change = true;

		maze.calculatePath(single_step);
		maze.drawRealMaze(realMazeCanvas.getGraphics2D());
		maze.drawMaze(dStarCanvas.getGraphics2D());

		if (single_step) {
			executing = true;
			nextStep.setEnabled(true);
		}

		ta.setText(maze.getStack());

		if (maze.executionEnd()) {
			nextStep.setEnabled(false);
			executing = false;
		}

		realMazeCanvas.repaint();
		dStarCanvas.repaint();
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Next Step")) {

			maze.calculatePath(single_step);
			maze.drawMaze(dStarCanvas.getGraphics2D());
			dStarCanvas.repaint();

			if (maze.executionEnd()) {
				nextStep.setEnabled(false);
				move.setEnabled(true);
				executing = false;
			}
			ta.setText(maze.getStack());
		} else if (e.getActionCommand().equals("Restart")) {
			first_change = true;

			maze.initialize();
			maze.calculatePath(single_step);
			maze.drawMaze(dStarCanvas.getGraphics2D());
			maze.drawRealMaze(realMazeCanvas.getGraphics2D());

			realMazeCanvas.repaint();
			dStarCanvas.repaint();

			move.setEnabled(true);
			nextStep.setEnabled(false);
			executing = false;

			if (!maze.executionEnd()) {
				nextStep.setEnabled(true);
				move.setEnabled(false);
				executing = true;
			}
			ta.setText(maze.getStack());
		} else if (e.getActionCommand().equals("Place Start")) {
			enableButtons(false);
			place_start = true;
		} else if (e.getActionCommand().equals("Place Goal")) {
			enableButtons(false);
			place_goal = true;
		} else if (e.getActionCommand().equals("Move")) {
			first_change = true;

			maze.move(single_step);
			maze.drawRealMaze(realMazeCanvas.getGraphics2D());
			maze.drawMaze(dStarCanvas.getGraphics2D());

			realMazeCanvas.repaint();
			dStarCanvas.repaint();
			if (!maze.executionEnd() || maze.reachedGoal()) {
				move.setEnabled(false);
				if (single_step) {
					nextStep.setEnabled(true);
					executing = true;
				}
			}
			ta.setText(maze.getStack());
		}
	}

	public void itemStateChanged(ItemEvent e) {
		System.out.println();
		String string = (String) e.getItem();
		if (string.equals("Allow Diagonal Movement")) {
			first_change = true;

			maze.setDiagonal(diagonals.getState());
			maze.initialize();
			maze.calculatePath(single_step);
			maze.drawMaze(dStarCanvas.getGraphics2D());
			maze.drawRealMaze(realMazeCanvas.getGraphics2D());

			if (single_step) {
				executing = true;
				nextStep.setEnabled(true);
			}

			ta.setText(maze.getStack());

			if (maze.executionEnd()) {
				nextStep.setEnabled(false);
				executing = false;
			}

			realMazeCanvas.repaint();
			dStarCanvas.repaint();

		} else {
			single_step = !single_step;
		}
	}

	private void enableButtons(boolean e) {
		changeStart.setEnabled(e);
		changeGoal.setEnabled(e);
		move.setEnabled(e);
		nextStep.setEnabled(e);
		single_step_execution.setEnabled(e);
		diagonals.setEnabled(e);
		restart.setEnabled(e);
	}

}
