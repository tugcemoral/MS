package tr.edu.metu.ceng.ms.thesis.modstarlite.ui;

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
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JLabel;

import tr.edu.metu.ceng.ms.thesis.modstarlite.core.GridDStarLiteWithMOMap;
import tr.edu.metu.ceng.ms.thesis.modstarlite.ui.components.MOStaticMap;
import tr.edu.metu.ceng.ms.thesis.modstarlite.util.GridMapUtils;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;
import tr.edu.metu.ceng.ms.thesis.robotutils.ui.MapPanel;

public class GridDStarPlanningWithSpecifiedMap {

	public static Shape dot = new RoundRectangle2D.Double(-0.25, -0.25, 0.5,
			0.5, 0.25, 0.25);
	public static Stroke dotStroke = new BasicStroke(0.5f);
	public static AtomicBoolean needToReplan = new AtomicBoolean(true);
	private static int oldPathSize = 0;

	private MOStaticMap sm;

	private int[] start;

	private int[] goal;

	public GridDStarPlanningWithSpecifiedMap(MOStaticMap sm, int[] start,
			int[] goal) {
		this.sm = sm;
		this.start = start;
		this.goal = goal;
	}

	public void execute() {
		final Rectangle2D mapBounds = new Rectangle2D.Double(0.0, 0.0,
				sm.size(0), sm.size(1));

		// initialize non-optimizede dStar planning.
		GridDStarLiteWithMOMap dStar = new GridDStarLiteWithMOMap(sm,
				new IntCoord(start), new IntCoord(goal));

		// create a display map to visualize algorithm execution.
		MapPanel mp = new VisualizedMapPanel(dStar, sm, mapBounds);

		mp.setIcon("map", GridMapUtils.toImage(sm), mapBounds);
		mp.setPreferredSize(new Dimension(600, 600));

		JFrame jf = new JFrame("D*Lite with Specified MO Map");
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

		private GridDStarLiteWithMOMap dStar;

		private Rectangle2D mapBounds;

		private MOStaticMap sm;

		public VisualizedMapPanel(GridDStarLiteWithMOMap dStarLiteWithMap,
				MOStaticMap sm, Rectangle2D mapBounds) {
			this.sm = sm;
			this.dStar = dStarLiteWithMap;
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
					if (!sm.hasObstacle(row, col)) {
						// put an obstacle on that location...
						dStar.setObstacleOnCoordinate(new IntCoord(row, col),
								true);
					} else {
						// unput the obstacle on that location...
						dStar.setObstacleOnCoordinate(new IntCoord(row, col),
								false);
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
