package tr.edu.metu.ceng.ms.thesis.mogpp.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.MOState;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Objective;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveArray;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveBehaviour;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path;
import tr.edu.metu.ceng.ms.thesis.modstarlite.ui.components.MOStaticMap;
import tr.edu.metu.ceng.ms.thesis.modstarlite.ui.components.OperationsPanel;
import tr.edu.metu.ceng.ms.thesis.modstarlite.util.GridMapGenerator;
import tr.edu.metu.ceng.ms.thesis.modstarlite.util.GridMapUtils;
import tr.edu.metu.ceng.ms.thesis.modstarlite.util.MultiObjectiveUtils;
import tr.edu.metu.ceng.ms.thesis.modstarlite.util.PathComparator;
import tr.edu.metu.ceng.ms.thesis.modstarlite.util.PropertiesWriter;
import tr.edu.metu.ceng.ms.thesis.mogpp.core.MOGeneticPathPlanner;
import tr.edu.metu.ceng.ms.thesis.mogpp.core.env.MOGeneticMap;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;
import tr.edu.metu.ceng.ms.thesis.robotutils.ui.MapPanel;

public class GridMOGPPlanning {

	// private static Random rnd = new Random();

	private static final Color MISTY_GRAY = new Color(153, 153, 153, 100);

	private static Shape dot = new RoundRectangle2D.Double(-0.25, -0.25, 0.5,
			0.5, 0.25, 0.25);

	private static Shape rect = new Rectangle2D.Double(-0.25, -0.25, 0.5, 0.5);

	private static Stroke dotStroke = new BasicStroke(0.5f);

	private static AtomicBoolean needToReplan = new AtomicBoolean(true);

	private static int oldPathSize = 0;

	private static IntCoord prevTmpGoal;

	private MOGeneticMap gmap;

	private static PathComparator pathComparator = new PathComparator();

	private static ObjectiveArray actualPathCost = ObjectiveArray.SINGLE_ZERO;

	// private static final String EXECUTION_FILE =
	// "/experimental/vfrustum/125x125/125_25(32)";

	// private static final String EXECUTION_FILE =
	// "/experimental/journal-tests/fullyobservable/randomized/80x80/80x80";
	// private static final String EXECUTION_FILE =
	// "/experimental/journal-tests/fullyobservable/handcrafted/160x160/160x160";
	// private static final String EXECUTION_FILE =
	// "/experimental/journal-tests/multiobjectivity/80x80/80x80_45";
	private static final String EXECUTION_FILE = "/experimental/journal-tests/partiallyobservable/80x80/80x80_50(28)";

	private static final String EXECUTION_FILE_PROPERTIES = EXECUTION_FILE
			+ ".properties";

	public static void main(String[] args) throws InterruptedException {
		// create the mogp planning executor and run it (like hell).
		GridMOGPPlanning mogpPlanning = new GridMOGPPlanning();
		mogpPlanning.execute();
	}

	public void execute() throws InterruptedException {
		// generate the multi-objective map.
		GridMapGenerator mapGen = new GridMapGenerator(
				EXECUTION_FILE_PROPERTIES);
		gmap = mapGen.generateGeneticMap();

		Rectangle2D mapBounds = new Rectangle2D.Double(0.0, 0.0, gmap.size(0),
				gmap.size(1));
		// initialize Multi-Objective Genetic Path Planner.
		MOGeneticPathPlanner mogpp = new MOGeneticPathPlanner(gmap,
				new IntCoord(gmap.getStart()), new IntCoord(gmap.getTmpGoal()));

		// MODStarLiteImpl modStar = new MODStarLiteImpl(sm, new IntCoord(
		// sm.getStart()), new IntCoord(sm.getTmpGoal()), EXECUTION_FILE);

		// create the components (display map, operations panel and log viewer)
		// to visualize algorithm execution.
		MapPanel mp = new VisualizedMapPanel(mogpp, gmap, mapBounds);
		OperationsPanel op = new VisualizedOperationsPanel(mp, gmap);
		// LogViewerPanel lv = new LogViewerPanel(mp, sm);

		JFrame jf = new JFrame("MOGPP");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// jf.setResizable(false);
		jf.setMinimumSize(new Dimension(900, 600));
		jf.getContentPane().setLayout(new BorderLayout());

		JPanel tmpPanel = new JPanel(new BorderLayout());
		tmpPanel.add(mp, BorderLayout.CENTER);
		tmpPanel.add(op, BorderLayout.EAST);

		// jf.getContentPane().add(mp, BorderLayout.CENTER);
		// jf.getContentPane().add(op, BorderLayout.EAST);
		jf.getContentPane().add(tmpPanel, BorderLayout.CENTER);
		// jf.getContentPane().add(lv, BorderLayout.SOUTH);
		jf.pack();
		jf.setVisible(true);

		mp.setView(mapBounds);

		// System.out.println("Picked endpoints: " + Arrays.toString(start) +
		// "->"
		// + Arrays.toString(goal));

		// Print and display start, goal, threat zone and initial viewing
		// frustum areas.
		mp.drawThreatZonesAndVFrustum();
		mp.setShape("Start", dot, AffineTransform.getTranslateInstance(
				(double) gmap.getStart()[0] + 0.5,
				(double) gmap.getStart()[1] + 0.5), Color.GREEN, dotStroke);
		mp.setShape("TmpGoal", dot, AffineTransform.getTranslateInstance(
				(double) gmap.getTmpGoal()[0] + 0.5,
				(double) gmap.getTmpGoal()[1] + 0.5), Color.BLUE, dotStroke);
		mp.setShape("Goal", dot, AffineTransform.getTranslateInstance(
				(double) gmap.getGoal()[0] + 0.5,
				(double) gmap.getGoal()[1] + 0.5), Color.RED, dotStroke);

		// Execute MOGPP search FOREVER
		while (true) {
			if (needToReplan.getAndSet(false)) {
				synchronized (mogpp) {
					List<Path<Coordinate>> paths = mogpp.plan();
					op.updatePanel(paths);
//					System.out.println("# of solutions found by MOGPP: "
//							+ paths.size());

					if (paths.size() == 0
							&& !Arrays.equals(gmap.getTmpGoal(), gmap.getGoal())) {

						IntCoord tmpGoal = gmap.updateTmpGoal();
						mogpp.updateGoal(tmpGoal);
						prevTmpGoal = tmpGoal;
						mp.setShape("TmpGoal", dot, AffineTransform
								.getTranslateInstance(
										(double) gmap.getTmpGoal()[0] + 0.5,
										(double) gmap.getTmpGoal()[1] + 0.5),
								Color.BLUE, dotStroke);

						needToReplan.set(true);
//						System.out.println("Replanning throught: "
//								+ Arrays.toString(tmpGoal.getInts()));
					}

					if (paths.size() > 0) {
						if (!Arrays.equals(gmap.getCurrentAgentLocation()
								.getInts(), gmap.getGoal())) {

							int index = findMiddlePathIndex(paths);
//							drawPath(mp, paths.get(index));
							// drawPath(mp, paths.get(paths.size()-1));

							// if (paths.size() > 1) {
							// op.displaySelectPathInfo();
							// synchronized (op.concurrentObj) {
							// op.concurrentObj.wait();
							// }
							// operateOnPath(
							// OperationsPanel.getCurrentDrawnPath(),
							// paths, modStar, mp,1);
							// } else {
							// operateOnPath(0, paths, modStar, mp,1);
							// }
							 operateOnPath(index, paths, mogpp, mp, 1);
						} else {
							System.out.println("Reached Goal: "
									+ gmap.getTmpGoal().toString());
							break;
						}
					} else {
						drawPath(mp, null);
					}

					if (paths.size() > 0) {
						drawPath(mp, paths.get(0));
					} else {
						drawPath(mp, null);
					}
				}
			}
			Thread.yield();
		}
	}

	private int findMiddlePathIndex(List<Path<Coordinate>> paths) {
		if (paths.size() == 1) {
			return 0;
		} else {
			List<Path<Coordinate>> newPaths = new Vector<Path<Coordinate>>();
			newPaths.addAll(paths);
			Collections.sort(newPaths, pathComparator);
			paths = newPaths;
			return (int) Math.ceil(paths.size() / 2);
		}
	}

	private void operateOnPath(int pathNumber, List<Path<Coordinate>> paths,
			MOGeneticPathPlanner mogpp, MapPanel mp, int index)
			throws InterruptedException {

		IntCoord newAgentLoc;
		try {
			newAgentLoc = (IntCoord) paths.get(pathNumber).get(index);
			actualPathCost = MultiObjectiveUtils.sum(actualPathCost,
					new ObjectiveArray(new Objective(1,
							ObjectiveBehaviour.MINIMIZED), new Objective(gmap
							.get(newAgentLoc.getInts()).getTotalRisk(),
							ObjectiveBehaviour.MINIMIZED)));
		} catch (IndexOutOfBoundsException ioobe) {
			System.out.println("###Total Execution Time: "
					+ mogpp.getTotalExecTime());
			System.out.println("###Actual Path Cost: " + actualPathCost);
			return;
		}

		mp.setShape(
				"Agent",
				dot,
				AffineTransform.getTranslateInstance(
						(double) newAgentLoc.get(0) + 0.5,
						(double) newAgentLoc.get(1) + 0.5), Color.CYAN,
				dotStroke);

		cleanViewingFrustum(mp, gmap.getvFrustumArea());
		gmap.clearPossibleTmpGoals();

		// mogpp.updateAgentLocation(newAgentLoc);
		gmap.setCurrentAgentLocation(newAgentLoc);

		reDrawViewingFrustum(mp, gmap.getvFrustumArea());

		IntCoord tmpGoal = gmap.updateTmpGoal();

		if (tmpGoal.equals(prevTmpGoal)) {
			// Thread.sleep(500);
			operateOnPath(pathNumber, paths, mogpp, mp, index + 1);
		} else {
			gmap.clearBackpointers();
			mogpp.updateGoal(tmpGoal);
			mogpp.updateStart(newAgentLoc);
			prevTmpGoal = tmpGoal;
			mp.setShape(
					"TmpGoal",
					dot,
					AffineTransform.getTranslateInstance(
							(double) gmap.getTmpGoal()[0] + 0.5,
							(double) gmap.getTmpGoal()[1] + 0.5), Color.BLUE,
					dotStroke);

			needToReplan.set(true);
//			System.out.println("Replanning throught: "
//					+ Arrays.toString(tmpGoal.getInts()));
			// TODO: this sleep should be parameterized.
			// Thread.sleep(500);
		}
	}

	private void cleanViewingFrustum(MapPanel mp, List<MOState> vFrustumArea) {
		for (MOState moState : vFrustumArea) {
			int i = moState.getCoords().getInts()[0];
			int j = moState.getCoords().getInts()[1];

			if (i >= 0 && j >= 0) {
				mp.removeShape("VFrustum" + i + "#" + j);
				// mp.setShape("VFrustum" + i + j, rect, AffineTransform
				// .getTranslateInstance((double) i + 0.5,
				// (double) j + 0.5), MISTY_GRAY, dotStroke);
			}
		}
	}

	// /**
	// * Find an unoccupied start location
	// *
	// * @return
	// */
	// private int[] findStart() {
	// int[] start = new int[sm.dims()];
	// while (sm.hasObstacle(start)) {
	// for (int i = 0; i < sm.dims(); i++) {
	// start[i] = rnd.nextInt(sm.size(i));
	// }
	// }
	// return start;
	// }
	//
	// /**
	// * Find an unoccupied goal location (that isn't the same as the start)
	// *
	// * @param start
	// * @return
	// */
	// private int[] findGoal(int[] start) {
	// int[] goal = new int[sm.dims()];
	// while (sm.hasObstacle(goal) || Arrays.equals(start, goal)) {
	// for (int i = 0; i < sm.dims(); i++) {
	// goal[i] = rnd.nextInt(sm.size(i));
	// }
	// }
	// return goal;
	// }

	private void reDrawViewingFrustum(MapPanel mp, List<MOState> vFrustumArea) {
		for (MOState moState : vFrustumArea) {
			int i = moState.getCoords().getInts()[0];
			int j = moState.getCoords().getInts()[1];

			if (i >= 0 && j >= 0) {
				// mp.removeShape("VFrustum" + i);
				mp.setShape("VFrustum" + i + "#" + j, rect, AffineTransform
						.getTranslateInstance((double) i + 0.5,
								(double) j + 0.5), MISTY_GRAY, dotStroke);
			}
		}
	}

	private void drawPath(MapPanel mp, Path<? extends Coordinate> path) {
		// Print and display resulting lowest cost path
		if (path == null) {
			// JOptionPane.showMessageDialog(mp, "No Path Found!", "Warning",
			// JOptionPane.WARNING_MESSAGE);
//			System.out.println("No path found!");

			for (int i = 1; i < oldPathSize - 1; i++)
				mp.removeShape("p" + i);

			oldPathSize = 0;
		} else {
//			System.out.println("Solution path: " + path);

			for (int i = 1; i < path.size() - 1; i++) {
				Coordinate c = path.get(i);
				mp.setShape(
						"p" + i,
						dot,
						AffineTransform.getTranslateInstance(c.get(0) + 0.5,
								c.get(1) + 0.5), mp.getPathColor(), dotStroke);
			}

			for (int i = path.size() - 1; i < oldPathSize - 1; i++)
				mp.removeShape("p" + i);

			oldPathSize = path.size();
		}
	}

	public class VisualizedOperationsPanel extends OperationsPanel {

		private static final long serialVersionUID = -6312189043800858489L;

		public VisualizedOperationsPanel(MapPanel mp, MOStaticMap sm) {
			super(mp, sm);
		}

		protected void draw(Path<Coordinate> path) {
			drawPath(mp, path);
		}

		protected void saveSettings() {
			// save properties and get the info to display.
			String info = PropertiesWriter.finalizeProperties(sm);
			JOptionPane.showMessageDialog(null, info, "Successful",
					JOptionPane.INFORMATION_MESSAGE);
		}

	}

	public class VisualizedMapPanel extends MapPanel {

		private static final long serialVersionUID = -9065775798293900573L;

		private MOGeneticPathPlanner mogpp;

		private Rectangle2D mapBounds;

		private MOStaticMap sm;

		public VisualizedMapPanel(MOGeneticPathPlanner mogpp, MOStaticMap sm,
				Rectangle2D mapBounds) {
			super.setIcon("map", GridMapUtils.toImage(sm), mapBounds);
			this.sm = sm;
			this.mogpp = mogpp;
			this.mapBounds = mapBounds;
		}

		public void drawThreatZonesAndVFrustum() {

			for (int i = 0; i < sm.sizes()[0]; i++) {
				for (int j = 0; j < sm.sizes()[1]; j++) {
					if (sm.get(i, j).isInThreat()) {
						// generate threat zone
						Color threatColor = generateThreatZoneColor(sm
								.get(i, j));
						// draw the threat zone.
						setShape("Threat" + i + "#" + j, dot,
								AffineTransform.getTranslateInstance(
										(double) i + 0.5, (double) j + 0.5),
								threatColor, dotStroke);
					}
					if (sm.isInViewingFrustumArea(i, j)) {
						setShape("VFrustum" + i + "#" + j, rect,
								AffineTransform.getTranslateInstance(
										(double) i + 0.5, (double) j + 0.5),
								MISTY_GRAY, dotStroke);
					}
				}
			}
		}

		private Color generateThreatZoneColor(MOState moState) {
			double totalRisk = moState.getTotalRisk();
			boolean inViewingFrustum = moState.isInViewingFrustum();

			// // create the default color.
			// Color tZoneColor = new Color(255, 200, 200);
			//
			// // get the decrement factor.
			// int decrementFact = inViewingFrustum ? (int) (totalRisk * 0.3)
			// : (int) (totalRisk * 0.6);
			// create the default color.
			Color tZoneColor = new Color(255, 128, 128);

			// get the decrement factor.
			int decrementFact = (int) (totalRisk * 0.5);

			int red = tZoneColor.getRed();
			int green = tZoneColor.getGreen();
			int blue = tZoneColor.getBlue();
			int alpha = tZoneColor.getAlpha();

			if (green >= decrementFact) {
				green = green - decrementFact;
			} else {
				green = 0;
				red = red - (int) ((decrementFact - green) / 2);
				// if (red < 0)
				// red = 0;
			}
			if (blue >= decrementFact) {
				blue = blue - decrementFact;
			} else {
				blue = 0;
				red = red - (int) ((decrementFact - blue) / 2);
				// if (red < 0)
				// red = 0;
			}

			Color color;
			try {
				color = new Color(red, green, blue, alpha);
			} catch (Exception e) {
				color = Color.MAGENTA;
			}
			return color;
		}

		// @Override
		// public void onClick(double x, double y, int button, int numClicks) {
		// // Find the map cell that was clicked
		// int row = (int) x;
		// int col = (int) y;
		//
		// // Ignore clicks outside the map
		// if (row < 0 || row >= mapBounds.getWidth() || col < 0
		// || col >= mapBounds.getHeight())
		// return;
		//
		// // Determine if click was left (BUTTON1) or right (BUTTON3)
		// if (button == MouseEvent.BUTTON1 && !isCtrlIsMasked()) {
		// // When clicked, toggle a map obstacle
		// synchronized (modStar) {
		// sm.clearBackpointers();
		// if (!sm.hasObstacle(row, col)) {
		// // put an obstacle on that location...
		// modStar.setObstacleOnCoordinate(new IntCoord(row, col),
		// true);
		// } else {
		// // unput the obstacle on that location...
		// modStar.setObstacleOnCoordinate(new IntCoord(row, col),
		// false);
		// }
		// }
		//
		// setIcon("map", GridMapUtils.toImage(sm), mapBounds);
		//
		// // replan the optimal paths...
		// needToReplan.set(true);
		// } else if (button == MouseEvent.BUTTON1 && isCtrlIsMasked()) {
		// // get clicked state...
		// MOState moState = sm.get(row, col);
		// // show objective on an option pane.
		// JOptionPane.showMessageDialog(this, "[" + row + ", " + col
		// + "]: " + moState.getObjectives(), "Objectives of "
		// + "[" + row + ", " + col + "]",
		// JOptionPane.INFORMATION_MESSAGE);
		// // also display this message on console.
		// System.out.println("[" + row + ", " + col + "]: "
		// + moState.getObjectives());
		//
		// } else if (button == MouseEvent.BUTTON3) {
		// // When clicked, change the start location
		// IntCoord newStart = new IntCoord(row, col);
		// synchronized (modStar) {
		// modStar.setStart(newStart);
		// }
		//
		// setShape("Start", dot, AffineTransform.getTranslateInstance(
		// (double) row + 0.5, (double) col + 0.5), Color.GREEN,
		// dotStroke);
		// // update new start...
		// sm.setStart(newStart.getInts());
		// // replan the optimal paths...
		// needToReplan.set(true);
		// }
		// }

		@Override
		protected void onKeyPressed() {
			// System.out.println(sm.get(1,1).getObjectives());
		}

		@Override
		protected void onKeyReleased() {
		}
	}

}
