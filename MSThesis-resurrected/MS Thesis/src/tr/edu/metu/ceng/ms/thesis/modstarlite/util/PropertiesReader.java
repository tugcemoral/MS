package tr.edu.metu.ceng.ms.thesis.modstarlite.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JOptionPane;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ExecutionType;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveBehaviour;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Obstacle;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.TemporaryGoalStrategy;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Threat;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.VFrustumStrategy;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;

public class PropertiesReader {

	private static final String RESOURCE = "/resource/";

	private static int width;

	private static int height;

	// used in MOA*
	private static double hp;
	
	private static double allowedDamagePercent;
	
	// used in MOGPP
	private static int maxIteration;
	
	private static int elitists;
	
	private static int populationSize;
	
	private static double crossoverRate;
	
	private static double mutationRate;
	///
	
	private static ObjectiveBehaviour[] objBehaviours;

	private static int[] start;

	private static int[] goal;

	private static int[] viewingFrustumBorders;

	private static boolean randomGeneration;

	private static int numberOfThreats;

	private static List<Threat> threats;

	private static int numberOfObstacles;

	private static int numberOfObstacleGroups;

	private static List<Obstacle> obstacles;

	private static ExecutionType executionType;

	private static VFrustumStrategy vFrustumStrategy;

	private static TemporaryGoalStrategy tmpGoalStrategy;

	public static void initializeProperties(String propertiesFileName) {
		// create the properties instance.
		Properties prop = new Properties();
		try {
			// read the given properties file.
			prop.load(new FileInputStream(System.getProperty("user.dir")
					+ RESOURCE + propertiesFileName));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, propertiesFileName
					+ " is not found under " + System.getProperty("user.dir")
					+ RESOURCE, "File Not Found", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// set all corresponding properties.
		width = Integer.parseInt(prop.getProperty("width"));
		height = Integer.parseInt(prop.getProperty("height"));
		objBehaviours = getObjBehaviours(prop.getProperty("objBehaviours"));
		start = getIntArray(prop.getProperty("start", "-1, -1"));
		goal = getIntArray(prop.getProperty("goal", "-1, -1"));
		randomGeneration = Boolean.parseBoolean(prop.getProperty(
				"randomGeneration", "true"));

		numberOfThreats = Integer.parseInt(prop.getProperty("numberOfThreats", "0"));
		for (int i = 1; i <= numberOfThreats; i++) {
			getThreats().add(generateThreat(prop.getProperty("threat" + i)));
		}
		numberOfObstacles = Integer.parseInt(prop
				.getProperty("numberOfObstacles","0"));
		for (int i = 1; i <= numberOfObstacles; i++) {
			getObstacles().add(
					generateObstacle(prop.getProperty("obstacle" + i)));
		}

		// Viewing frustum area - related properties.
		try {
			viewingFrustumBorders = getIntArray(prop
					.getProperty("viewingFrustumBorders"));
		} catch (Exception e) {
			viewingFrustumBorders = new int[] { width, height };
		}

		executionType = ExecutionType.valueOf(prop.getProperty("executionType",
				"GOAL_DIRECTED"));
		vFrustumStrategy = VFrustumStrategy.valueOf(prop.getProperty(
				"vFrustumStrategy", "REFRESH"));
		tmpGoalStrategy = TemporaryGoalStrategy.valueOf(prop.getProperty(
				"tmpGoalStrategy", "NEAREST_BY_SP"));

		/////////////////// Properties for MOA* integration. ///////////////////////

		//agent properties.
		double[] agentProperties = getDoubleArray(prop.getProperty("agent",
				"0,0,0"));
		hp = agentProperties[0];
		allowedDamagePercent = agentProperties[1];

		//obstacle groups.
		numberOfObstacleGroups = Integer.parseInt(prop
				.getProperty("numberOfObstacleGroups", "0"));
		for (int i = 1; i <= numberOfObstacleGroups; i++) {
			getObstacles()
					.addAll(generateObstacleGroup(prop
							.getProperty("obstacleGroup" + i)));
		}

		/////////////////// Properties for MOGPP integration //////////////////////
		maxIteration = Integer.parseInt(prop.getProperty("maxIteration", "20"));
		elitists = Integer.parseInt(prop.getProperty("elitists", "5"));
		populationSize = Integer.parseInt(prop.getProperty("populationSize", "50"));
		crossoverRate = Double.parseDouble(prop.getProperty("crossoverRate", "0.8"));
		mutationRate = Double.parseDouble(prop.getProperty("mutationRate", "0.05"));
		
	}

	private static List<Obstacle> generateObstacleGroup(
			String property) {

		List<Obstacle> obstacleGroup = new Vector<Obstacle>();

		int[] obstacleGroupProperties = getSpacedIntArray(property);

		int startCol = obstacleGroupProperties[0];
		int startRow = obstacleGroupProperties[1];
		int endCol = obstacleGroupProperties[2];
		int endRow = obstacleGroupProperties[3];

		for (int i = startRow; i <= endRow; i++) {
			for (int j = startCol; j <= endCol; j++) {
				obstacleGroup.add(new Obstacle(i, j));
			}
		}

		return obstacleGroup;
	}

	private static Obstacle generateObstacle(String property) {
		// create a new Obstacle with given properties.
		return new Obstacle(getIntArray(property));
	}

	private static Threat generateThreat(String property) {
		// get corresponding threat properties.
		double[] threatProperties = getDoubleArray(property);
		
		//differences between threat zones are default: 2
		if(threatProperties.length == 6){
			// create a new threat and return it...
			return new Threat(
					new IntCoord((int) threatProperties[0],
							(int) threatProperties[1]),
							(int) threatProperties[2], 2,
							Arrays.copyOfRange(threatProperties, 3, threatProperties.length));
		}else {
			return new Threat(
					new IntCoord((int) threatProperties[0],
							(int) threatProperties[1]),
							(int) threatProperties[2], (int)threatProperties[3],
							Arrays.copyOfRange(threatProperties, 4, threatProperties.length));
		}
		
	}

	private static double[] getDoubleArray(String property) {
		String[] split = property.split(",");
		double[] result = new double[split.length];
		for (int i = 0; i < split.length; i++) {
			String str = split[i];
			result[i] = Double.parseDouble(str.trim());
		}
		return result;

	}

	private static ObjectiveBehaviour[] getObjBehaviours(String property) {
		String[] splitted = property.split(",");
		// create an objective behaviours array.
		ObjectiveBehaviour[] objBehaviours = new ObjectiveBehaviour[splitted.length];
		// dump this temp array and return it
		for (int i = 0; i < splitted.length; i++) {
			objBehaviours[i] = ObjectiveBehaviour.valueOf(splitted[i].trim());
		}
		return objBehaviours;
	}

	private static int[] getIntArray(String property) {
		String[] split = property.split(",");
		int[] result = new int[split.length];
		for (int i = 0; i < split.length; i++) {
			String str = split[i];
			result[i] = (int) Double.parseDouble(str.trim());
		}
		return result;
	}

	private static int[] getSpacedIntArray(String property) {
		String[] split = property.split(" ");
		int[] result = new int[split.length];
		for (int i = 0; i < split.length; i++) {
			String str = split[i];
			result[i] = (int) Double.parseDouble(str);
		}
		return result;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	public static int[] getStart() {
		return start;
	}

	public static int[] getGoal() {
		return goal;
	}

	public static boolean isRandomGeneration() {
		return randomGeneration;
	}

	public static int getNumberOfThreats() {
		return numberOfThreats;
	}

	public static List<Threat> getThreats() {
		if (threats == null) {
			threats = new Vector<Threat>();
		}
		return threats;
	}

	public static int getNumberOfObstacles() {
		return numberOfObstacles;
	}

	public static List<Obstacle> getObstacles() {
		if (obstacles == null) {
			obstacles = new Vector<Obstacle>();
		}
		return obstacles;
	}

	public static ObjectiveBehaviour[] getObjBehaviours() {
		return objBehaviours;
	}

	public static int[] getViewingFrustumBorders() {
		return viewingFrustumBorders;
	}

	public static ExecutionType getExecutionType() {
		return executionType;
	}

	public static VFrustumStrategy getvFrustumStrategy() {
		return vFrustumStrategy;
	}

	public static TemporaryGoalStrategy getTmpGoalStrategy() {
		return tmpGoalStrategy;
	}

	public static double getHp() {
		return hp;
	}

	public static double getAllowedDamagePercent() {
		return allowedDamagePercent;
	}

	public static int getMaxIteration() {
		return maxIteration;
	}

	public static int getElitists() {
		return elitists;
	}

	public static int getPopulationSize() {
		return populationSize;
	}

	public static double getCrossoverRate() {
		return crossoverRate;
	}

	public static double getMutationRate() {
		return mutationRate;
	}

}
