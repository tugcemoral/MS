package tr.edu.metu.ceng.ms.thesis.robotutils.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JLabel;

import tr.edu.metu.ceng.ms.thesis.modstarlite.util.PropertiesReader;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;
import tr.edu.metu.ceng.ms.thesis.robotutils.planning.GridDStarLite;
import tr.edu.metu.ceng.ms.thesis.robotutils.util.GridMapUtils;

/**
 * Creates a randomized 2D map and solves a path between two random locations
 * using non-optimized D* search. Since D* is incremental, the GUI is configured
 * to toggle obstacles on left clicks and change the start location on a right
 * click.
 * 
 * @author Tugcem Oral
 * 
 */
public class Grid_NO_DStarPlanning {

	private static Random rnd = new Random();

	private static Shape dot = new RoundRectangle2D.Double(-0.25, -0.25, 0.5,
			0.5, 0.25, 0.25);
	private static Stroke dotStroke = new BasicStroke(0.5f);
	private static AtomicBoolean needToReplan = new AtomicBoolean(true);

	private static int oldPathSize = 0;

	public static void main(String[] args) {
		Grid_NO_DStarPlanning dStarPlanning = new Grid_NO_DStarPlanning();
		dStarPlanning.execute();
	}

	public void execute() {

		// Generate a random blocky map (using cellular automata rules)
		// final StaticMap sm = GridMapGenerator.createRandomMazeMap2D(10, 10);
		final StaticMap sm = GridMapGenerator
				.createPredefinedStaticMap("250x250with22threatsNice.properties");
		final Rectangle2D mapBounds = new Rectangle2D.Double(0.0, 0.0,
				sm.size(0), sm.size(1));

		// Determine start and goal "random" locations...

		// Find an unoccupied start location
		// int[] start = new int[sm.dims()];
		// while (sm.get(start) < 0) {
		// for (int i = 0; i < sm.dims(); i++) {
		// start[i] = rnd.nextInt(sm.size(i));
		// }
		// }
		// int[] start = new int[]{1,1};
		int[] start = PropertiesReader.getStart();

		// Find an unoccupied goal location (that isn't the same as the start)
		// int[] goal = new int[sm.dims()];
		// while (sm.get(goal) < 0 || Arrays.equals(start, goal)) {
		// for (int i = 0; i < sm.dims(); i++) {
		// goal[i] = rnd.nextInt(sm.size(i));
		// }
		// }
		// int[] goal = new int[]{10-2, 10-2};
		int[] goal = PropertiesReader.getGoal();

		// initialize non-optimizede dStar planning.
		GridDStarLite dStar = new GridDStarLite(sm, new IntCoord(start),
				new IntCoord(goal));

		// create a display map to visualize algorithm execution.
		MapPanel mp = new VisualizedMapPanel(dStar, sm, mapBounds);

		mp.setIcon("map", GridMapUtils.toImage(sm), mapBounds);
		mp.setPreferredSize(new Dimension(600, 600));

		JFrame jf = new JFrame("Map");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.getContentPane().setLayout(new BorderLayout());
		jf.getContentPane().add(mp, BorderLayout.CENTER);
		jf.getContentPane()
				.add(new JLabel(
						"<html>"
								+ "<font color=red>(LEFT CLICK)</font> Toggle Obstacle / "
								+ "<font color=red>(RIGHT-CLICK)</font> Change start location <p>"
								+ "<font color=blue>(MOUSE DRAG)</font> Pan around map / "
								+ "<font color=blue>(MOUSE WHEEL)</font> Zoom in/out of map"
								+ "</html>"), BorderLayout.NORTH);
		jf.pack();
		jf.setVisible(true);

		mp.setView(mapBounds);

		// Print and display start and goal locations
		System.out.println("Picked endpoints: " + Arrays.toString(start) + "->"
				+ Arrays.toString(goal));
		mp.setShape("Start", dot, AffineTransform.getTranslateInstance(
				(double) start[0] + 0.5, (double) start[1] + 0.5), Color.GREEN,
				dotStroke);
		mp.setShape("Goal", dot, AffineTransform.getTranslateInstance(
				(double) goal[0] + 0.5, (double) goal[1] + 0.5), Color.RED,
				dotStroke);

		// Execute D* search FOREVER
		while (true) {
			if (needToReplan.getAndSet(false)) {
				synchronized (dStar) {
					List<? extends Coordinate> path = dStar.plan();
					drawPath(mp, path);
				}
			}
			Thread.yield();
		}
	}

	private void drawPath(MapPanel mp, List<? extends Coordinate> path) {
		// Print and display resulting lowest cost path
		if (path.isEmpty()) {
			System.out.println("No path found!");

			for (int i = 1; i < oldPathSize - 1; i++)
				mp.removeShape("p" + i);

			oldPathSize = 0;
		} else {
			System.out.println("Solution path: " + path);

			for (int i = 1; i < path.size() - 1; i++) {
				Coordinate c = path.get(i);
				mp.setShape(
						"p" + i,
						dot,
						AffineTransform.getTranslateInstance(c.get(0) + 0.5,
								c.get(1) + 0.5), Color.CYAN, dotStroke);
			}

			for (int i = path.size() - 1; i < oldPathSize - 1; i++)
				mp.removeShape("p" + i);

			oldPathSize = path.size();
		}
	}

	private class VisualizedMapPanel extends MapPanel {

		private static final long serialVersionUID = -7367043588510526405L;

		private GridDStarLite dStar;

		private Rectangle2D mapBounds;

		private StaticMap sm;

		public VisualizedMapPanel(GridDStarLite dStar, StaticMap sm,
				Rectangle2D mapBounds) {
			this.sm = sm;
			this.dStar = dStar;
			this.mapBounds = mapBounds;
		}

		@Override
		public void onClick(double x, double y, int button, int numClicks) {
			// Find the map cell that was clicked
			int row = (int) x;
			int col = (int) y;

			// Ignore clicks outside the map
			if (row < 0 || row >= mapBounds.getWidth() || col < 0
					|| col >= mapBounds.getHeight())
				return;

			// Determine if click was left (BUTTON1) or right (BUTTON3)
			if (button == MouseEvent.BUTTON1) {
				// When clicked, toggle a map obstacle
				synchronized (dStar) {
					if (sm.get(row, col) == 0) {
						dStar.setCost(new IntCoord(row, col), (byte) 255);
					} else {
						dStar.setCost(new IntCoord(row, col), (byte) 0);
					}
				}

				setIcon("map", GridMapUtils.toImage(sm), mapBounds);
			} else if (button == MouseEvent.BUTTON3) {
				// When clicked, change the start location
				synchronized (dStar) {
					dStar.setStart(new IntCoord(row, col));
				}

				setShape("Start", dot, AffineTransform.getTranslateInstance(
						(double) row + 0.5, (double) col + 0.5), Color.GREEN,
						dotStroke);
			}

			needToReplan.set(true);
		}

	}

}
