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

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import tr.edu.metu.ceng.ms.thesis.modstarlite.core.MODStarLiteImpl;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.MOState;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path;
import tr.edu.metu.ceng.ms.thesis.modstarlite.ui.components.MOStaticMap;
import tr.edu.metu.ceng.ms.thesis.modstarlite.ui.components.OperationsPanel;
import tr.edu.metu.ceng.ms.thesis.modstarlite.util.GridMapGenerator;
import tr.edu.metu.ceng.ms.thesis.modstarlite.util.GridMapUtils;
import tr.edu.metu.ceng.ms.thesis.modstarlite.util.PropertiesWriter;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;
import tr.edu.metu.ceng.ms.thesis.robotutils.ui.MapPanel;

public class EmptyMapPlanner {

	// private static Random rnd = new Random();

//	private static final String EXECUTION_FILE = "/experimental/journal-tests/partiallyobservable/100x100/100x100_10(16)";
//	private static final String EXECUTION_FILE = "/experimental/journal-tests/multiobjectivity/80x80/80x80_45";
	private static final String EXECUTION_FILE = "/experimental/journal-tests/fullyobservable/randomized/default";
//	private static final String EXECUTION_FILE = "/experimental/journal-tests/fullyobservable/randomized/160x160/160x160";
	
	private static final String EXECUTION_FILE_PROPERTIES = EXECUTION_FILE + ".properties";

	private static final Color MISTY_GRAY = new Color(153, 153, 153, 100);

	private static Shape dot = new RoundRectangle2D.Double(-0.25, -0.25, 0.5,
			0.5, 0.25, 0.25);

	private static Shape rect = new Rectangle2D.Double(-0.25, -0.25, 0.5, 0.5);

	private static Stroke dotStroke = new BasicStroke(0.5f);

	private MOStaticMap sm;

	public static void main(String[] args) throws InterruptedException {
		// create the mod* planning executor and run it (like hell).
		EmptyMapPlanner modStarPlanning = new EmptyMapPlanner();
		modStarPlanning.execute();
	}

	public void execute() throws InterruptedException {
		// generate the multi-objective map.
		GridMapGenerator mapGen = new GridMapGenerator(
				EXECUTION_FILE_PROPERTIES);

		double tzRatio;
		double obstacleRatio;
		do {
			sm = mapGen.generateMap();
			tzRatio = calculateTZRatioManually(sm);
			obstacleRatio = calculateObstacleRatioManually(sm);
			System.out.println(tzRatio + " : " + obstacleRatio);
		}
		while(false  /* || tzRatio <= 40d  /* || tzRatio > 40d  || obstacleRatio <= 13d */);

		Rectangle2D mapBounds = new Rectangle2D.Double(0.0, 0.0, sm.size(0),
				sm.size(1));
		// initialize non-optimized MODStar planning.
		MODStarLiteImpl modStar = new MODStarLiteImpl(sm, new IntCoord(
				sm.getStart()), new IntCoord(sm.getTmpGoal()), EXECUTION_FILE);

		// create the components (display map, operations panel and log viewer)
		// to visualize algorithm execution.
		MapPanel mp = new VisualizedMapPanel(modStar, sm, mapBounds);
		OperationsPanel op = new VisualizedOperationsPanel(mp, sm);
		// LogViewerPanel lv = new LogViewerPanel(mp, sm);

		JFrame jf = new JFrame("MOD*Lite");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// jf.setResizable(false);
		jf.setMinimumSize(new Dimension(900, 600));
		jf.getContentPane().setLayout(new BorderLayout());

		JPanel tmpPanel = new JPanel(new BorderLayout());
		tmpPanel.add(mp, BorderLayout.CENTER);
		tmpPanel.add(op, BorderLayout.EAST);

		jf.getContentPane().add(tmpPanel, BorderLayout.CENTER);
		jf.pack();
		jf.setVisible(true);

		mp.setView(mapBounds);

		// Print and display start, goal, threat zone and initial viewing
		// frustum areas.
		mp.drawThreatZonesAndVFrustum();
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

		System.out.println("Threat zone Ratio : " + mp.calculateTZRatio());
		System.out.println("Obstacle Ratio : " + mp.calculateObstacleRatio()  + "%");
	}
	
	private double calculateTZRatioManually(MOStaticMap sm) {
		int numberOfThreatCells = 0;
		for (int i = 0; i < sm.sizes()[0]; i++) {
			for (int j = 0; j < sm.sizes()[1]; j++) {
				if (sm.get(i, j).isInThreat()) {
					numberOfThreatCells++;
				}
			}
		}
		
		return (double)((numberOfThreatCells * 100) / (sm.sizes()[0] * sm.sizes()[1]));
	}
	
	private double calculateObstacleRatioManually(MOStaticMap sm) {
		int numberOfObstacles = 0;
		for (int i = 0; i < sm.sizes()[0]; i++) {
			for (int j = 0; j < sm.sizes()[1]; j++) {
				if (sm.get(i, j).hasObstacle()) {
					numberOfObstacles++;
				}
			}
		}
		
		return (double)((numberOfObstacles * 100) / (sm.sizes()[0] * sm.sizes()[1]));
	}

	public class VisualizedOperationsPanel extends OperationsPanel {

		private static final long serialVersionUID = -6312189043800858489L;

		public VisualizedOperationsPanel(MapPanel mp, MOStaticMap sm) {
			super(mp, sm);
		}

		protected void draw(Path<Coordinate> path) {
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

		private MODStarLiteImpl modStar;

		private Rectangle2D mapBounds;

		private MOStaticMap sm;

		public VisualizedMapPanel(MODStarLiteImpl modStar, MOStaticMap sm,
				Rectangle2D mapBounds) {
			super.setIcon("map", GridMapUtils.toImage(sm), mapBounds);
			this.sm = sm;
			this.modStar = modStar;
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
						setShape("Threat" + i + j, dot,
								AffineTransform.getTranslateInstance(
										(double) i + 0.5, (double) j + 0.5),
								threatColor, dotStroke);
					}
//					if (sm.isInViewingFrustumArea(i, j)) {
//						setShape("VFrustum" + i + "#" + j, rect,
//								AffineTransform.getTranslateInstance(
//										(double) i + 0.5, (double) j + 0.5),
//								MISTY_GRAY, dotStroke);
//					}
				}
			}
		}
		
		@Override
		public double calculateTZRatio() {
			int numberOfThreatCells = 0;
			for (int i = 0; i < sm.sizes()[0]; i++) {
				for (int j = 0; j < sm.sizes()[1]; j++) {
					if (sm.get(i, j).isInThreat()) {
						numberOfThreatCells++;
					}
				}
			}
			
			return (double)((numberOfThreatCells * 100) / (sm.sizes()[0] * sm.sizes()[1]));
		}
		
		@Override
		public double calculateObstacleRatio() {
			int numberOfObstacles = 0;
			for (int i = 0; i < sm.sizes()[0]; i++) {
				for (int j = 0; j < sm.sizes()[1]; j++) {
					if (sm.get(i, j).hasObstacle()) {
						numberOfObstacles++;
					}
				}
			}
			
			return (double)((numberOfObstacles * 100) / (sm.sizes()[0] * sm.sizes()[1]));
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
				synchronized (modStar) {
					sm.clearBackpointers();
					if (!sm.hasObstacle(row, col)) {
						// put an obstacle on that location...
						modStar.setObstacleOnCoordinate(new IntCoord(row, col),
								true);
					} else {
						// unput the obstacle on that location...
						modStar.setObstacleOnCoordinate(new IntCoord(row, col),
								false);
					}
				}

				setIcon("map", GridMapUtils.toImage(sm), mapBounds);

				// replan the optimal paths...
				// needToReplan.set(true);
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

				setShape("Start", dot, AffineTransform.getTranslateInstance(
						(double) row + 0.5, (double) col + 0.5), Color.GREEN,
						dotStroke);
				// update new start...
				sm.setStart(newStart.getInts());
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
