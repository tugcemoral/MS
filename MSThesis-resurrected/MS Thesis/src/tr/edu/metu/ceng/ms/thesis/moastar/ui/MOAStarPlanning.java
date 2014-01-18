package tr.edu.metu.ceng.ms.thesis.moastar.ui;

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
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import tr.edu.metu.ceng.ms.thesis.moastar.MOAStar;
import tr.edu.metu.ceng.ms.thesis.modstarlite.core.MODStarLiteImpl;
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
import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;
import tr.edu.metu.ceng.ms.thesis.robotutils.ui.MapPanel;

public class MOAStarPlanning {

	private static final Color MISTY_GRAY = new Color(153, 153, 153, 100);

	private static Shape dot = new RoundRectangle2D.Double(-0.25, -0.25, 0.5,
			0.5, 0.25, 0.25);

	private static Shape rect = new Rectangle2D.Double(-0.25, -0.25, 0.5, 0.5);

	private static Stroke dotStroke = new BasicStroke(0.5f);

	private static AtomicBoolean needToReplan = new AtomicBoolean(true);

	MOAStar map;

	private static IntCoord prevTmpGoal;

	private MOStaticMap sm;

	private static int oldPathSize = 0;

	private static PathComparator pathComparator = new PathComparator();

	private static ObjectiveArray actualPathCost = ObjectiveArray.SINGLE_ZERO;
	
	private static final String EXECUTION_FILE = "/experimental/handcrafted/125x125/2012-06-05_01:36:13";
	
	private static final String EXECUTION_FILE_PROPERTIES = EXECUTION_FILE + ".properties";

	public static void main(String[] args) throws Exception {
		MOAStarPlanning moaStarPlanning = new MOAStarPlanning();
		moaStarPlanning.execute();
	}

	private void execute() throws Exception {

		// boolean randomGeneration = PropertiesReader.isRandomGeneration();

		// if (randomGeneration) {
		// // Generate a random multi-objective blocky map with objectives'
		// // behaviour.
		// sm = (new GridMapGenerator())
		// .createRandom2DMOMazeMapWithThreatsAndObstacles(width,
		// height, objBehaviours);
		// } else {
		// sm = (new GridMapGenerator())
		// .create2DMOMazeMapWithThreatAndObstaclesUsingPredefinedParams(
		// width, height, objBehaviours);
		// }
		// "moaStarInputFile6.properties"

		GridMapGenerator mapgen = new GridMapGenerator(
				EXECUTION_FILE_PROPERTIES);
		sm = mapgen
		// .create2DMOMazeMapWithThreatAndObstaclesUsingPredefinedParams();
				.generateMap();

		Rectangle2D mapBounds = new Rectangle2D.Double(0.0, 0.0, sm.size(0),
				sm.size(1));

		MOAStar moaStar = new MOAStar(sm, EXECUTION_FILE);

		// create the components (display map, operations panel and log viewer)
		// to visualize algorithm execution.

		MapPanel mp = new VisualizedMapPanel(moaStar, sm, mapBounds);
		OperationsPanel op = new MOAStarOperationsPanel(mp, sm);
		// LogViewerPanel lv = new LogViewerPanel(mp, sm);

		JFrame jf = new JFrame("MOA*");
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

		// Print and display start, goal and threat zone locations.
		mp.drawThreatZonesAndVFrustum();
		// System.out.println("Picked endpoints: " + Arrays.toString(start) +
		// "->"
		// + Arrays.toString(goal));

		mp.setShape("Start", dot, AffineTransform.getTranslateInstance(
				(double) sm.getStart()[0] + 0.5,
				(double) sm.getStart()[1] + 0.5), Color.GREEN, dotStroke);
		mp.setShape("TmpGoal", dot, AffineTransform.getTranslateInstance(
				(double) sm.getTmpGoal()[0] + 0.5,
				(double) sm.getTmpGoal()[1] + 0.5), Color.BLUE, dotStroke);
		mp.setShape("Goal", dot,
				AffineTransform.getTranslateInstance(
						(double) sm.getGoal()[0] + 0.5,
						(double) sm.getGoal()[1] + 0.5), Color.RED, dotStroke);

		// Execute MOD* search FOREVER
		while (true) {
			if (needToReplan.getAndSet(false)) {
				synchronized (moaStar) {
					List<Path<Coordinate>> foundPaths = moaStar
							.findPath_MOAStar();
					op.updatePanel(foundPaths);

//					if (foundPaths != null) {
//						if (!Arrays.equals(sm.getCurrentAgentLocation()
//								.getInts(), sm.getGoal())) {
//
//							int index = findMiddlePathIndex(foundPaths);
//							if (foundPaths.get(index) == null) {
//								System.out.println("###Total Execution Time: "
//										+ moaStar.getTotalExecTime());
//							}
//							drawPath(mp, foundPaths.get(foundPaths.size()-1));
//
//							// if (foundPaths.size() > 1) {
//							// op.displaySelectPathInfo();
//							// synchronized (op.concurrentObj) {
//							// op.concurrentObj.wait();
//							// }
//							// operateOnPath(
//							// OperationsPanel.getCurrentDrawnPath(),
//							// foundPaths, moaStar, mp,1);
//							// } else {
//							// operateOnPath(0, foundPaths, moaStar, mp,1);
//							// }
//							operateOnPath(foundPaths.size()-1, foundPaths, moaStar, mp, 1);
//						} else {
//							System.out.println("Reached Goal: "
//									+ sm.getTmpGoal().toString());
//							break;
//						}
//					} else {
//						drawPath(mp, null);
//						System.out.println("###Total Execution Time: "
//								+ moaStar.getTotalExecTime());
//					}

					 if (foundPaths != null) {
					 drawPath(mp, foundPaths.get(0));
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
			Collections.sort(paths, pathComparator);
			return (int) Math.ceil(paths.size() / 2);
		}
	}

	private void operateOnPath(int pathNumber, List<Path<Coordinate>> paths,
			MOAStar moaStar, MapPanel mp, int index) throws Exception {

		IntCoord newAgentLoc;
		try {
			newAgentLoc = (IntCoord) paths.get(pathNumber).get(index);
			actualPathCost = MultiObjectiveUtils.sum(actualPathCost,
					new ObjectiveArray(new Objective(1,
							ObjectiveBehaviour.MINIMIZED), new Objective(sm
							.get(newAgentLoc.getInts()).getTotalRisk(),
							ObjectiveBehaviour.MINIMIZED)));
		} catch (IndexOutOfBoundsException ioobe) {
			System.out.println("###Total Execution Time: "
					+ moaStar.getTotalExecTime());
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

		cleanViewingFrustum(mp, sm.getvFrustumArea());

		// moaStar.updateAgentLocation(newAgentLoc);
		sm.setStart(newAgentLoc.getInts());
		sm.setCurrentAgentLocation(newAgentLoc);

		reDrawViewingFrustum(mp, sm.getvFrustumArea());

		IntCoord tmpGoal = sm.updateTmpGoal();
		// moaStar = new MOAStar(sm);
		if (tmpGoal.equals(prevTmpGoal)) {
			// Thread.sleep(500);
			operateOnPath(pathNumber, paths, moaStar, mp, index + 1);
		} else {
			// sm.clearBackpointers();
			moaStar.updateGoal(tmpGoal);
			prevTmpGoal = tmpGoal;
			mp.setShape(
					"TmpGoal",
					dot,
					AffineTransform.getTranslateInstance(
							(double) sm.getTmpGoal()[0] + 0.5,
							(double) sm.getTmpGoal()[1] + 0.5), Color.BLUE,
					dotStroke);

			needToReplan.set(true);
			System.out.println("Replanning throught: "
					+ Arrays.toString(tmpGoal.getInts()));
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
			System.out.println("No path found!");

			for (int i = 0; i < oldPathSize; i++)
				mp.removeShape("p" + i);

			oldPathSize = 0;
		} else {
			System.out.println("Solution path: " + path);
			// XXX: Notice that MOA* paths does not contain neither start nor
			// goal.
			for (int i = 0; i < path.size(); i++) {
				Coordinate c = path.get(i);
				mp.setShape(
						"p" + i,
						dot,
						AffineTransform.getTranslateInstance(c.get(0) + 0.5,
								c.get(1) + 0.5), mp.getPathColor(), dotStroke);
			}
			for (int i = path.size(); i < oldPathSize; i++)
				mp.removeShape("p" + i);

			oldPathSize = path.size();
		}
	}

	public class MOAStarOperationsPanel extends OperationsPanel {

		private static final long serialVersionUID = 2911002589334755676L;

		public MOAStarOperationsPanel(MapPanel mp, MOStaticMap sm) {
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

		private static final long serialVersionUID = -7367043588510526405L;

		private MOAStar moaStar;

		private Rectangle2D mapBounds;

		private MOStaticMap sm;

		public VisualizedMapPanel(MOAStar moaStar, MOStaticMap sm,
				Rectangle2D mapBounds) {
			super.setIcon("map", GridMapUtils.toImage(sm), mapBounds);
			this.sm = sm;
			this.moaStar = moaStar;
			this.mapBounds = mapBounds;
		}

		public void drawThreatZonesAndVFrustum() {

			for (int i = 0; i < sm.sizes()[0]; i++) {
				for (int j = 0; j < sm.sizes()[1]; j++) {
					if (sm.get(i, j).isInThreat()) {
						// generate threat zone
						Color threatColor = generateThreatZoneColor(sm
								.get(i, j).getTotalRisk());
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

		private Color generateThreatZoneColor(double totalRisk) {
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
			}
			if (blue >= decrementFact) {
				blue = blue - decrementFact;
			} else {
				blue = 0;
				red = red - (int) ((decrementFact - blue) / 2);
			}

			Color color;
			try {
				color = new Color(red, green, blue, alpha);
			} catch (Exception e) {
				color = Color.MAGENTA;
			}
			return color;
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
			if (button == MouseEvent.BUTTON1 && !isCtrlIsMasked()) {
				// When clicked, toggle a map obstacle
				synchronized (moaStar) {
					if (!sm.hasObstacle(row, col)) {
						// put an obstacle on that location...
						// moaStar.setObstacleOnCoordinate(new IntCoord(row,
						// col),
						// true);
					} else {
						// unput the obstacle on that location...
						// moaStar.setObstacleOnCoordinate(new IntCoord(row,
						// col),
						// false);
					}
				}

				setIcon("map", GridMapUtils.toImage(sm), mapBounds);

				// replan the optimal paths...
				needToReplan.set(true);
			} else if (button == MouseEvent.BUTTON1 && isCtrlIsMasked()) {
				// get clicked state...
				MOState moState = sm.get(row, col);
				// show objective on an option pane.
				JOptionPane.showMessageDialog(this, "[" + row + ", " + col
						+ "]: " + moState.getObjectives(), "Objectives of "
						+ "[" + row + ", " + col + "]",
						JOptionPane.INFORMATION_MESSAGE);
				// also display this message on console.
				System.out.println("[" + row + ", " + col + "]: "
						+ moState.getObjectives());

			} else if (button == MouseEvent.BUTTON3) {
				// When clicked, change the start location
				IntCoord newStart = new IntCoord(row, col);
				// synchronized (moaStar) {
				// moaStar.setStart(newStart);
				// }

				setShape("Start", dot, AffineTransform.getTranslateInstance(
						(double) row + 0.5, (double) col + 0.5), Color.GREEN,
						dotStroke);
				// update new start...
				sm.setStart(newStart.getInts());
				// replan the optimal paths...
				needToReplan.set(true);
			}
		}

		@Override
		protected void onKeyPressed() {
			// System.out.println(sm.get(1,1).getObjectives());
		}

		@Override
		protected void onKeyReleased() {
		}
	}

}
