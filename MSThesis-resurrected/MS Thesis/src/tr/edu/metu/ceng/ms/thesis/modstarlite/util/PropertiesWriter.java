package tr.edu.metu.ceng.ms.thesis.modstarlite.util;

import static tr.edu.metu.ceng.ms.thesis.modstarlite.util.FileWriterUtils.RESOURCE;
import static tr.edu.metu.ceng.ms.thesis.modstarlite.util.FileWriterUtils.now;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveBehaviour;
import tr.edu.metu.ceng.ms.thesis.modstarlite.ui.components.MOStaticMap;

public class PropertiesWriter {

	public static String finalizeProperties(MOStaticMap sm) {
		// generate the properties file name and finalize properties.
		return finalizeProperties(now() + ".properties", sm);
	}

	public static String finalizeProperties(String fileName, MOStaticMap sm) {
		// create the properties to store.
		Properties prop = new Properties();

		prop.put("width", String.valueOf(sm.sizes()[0]));
		prop.put("height", String.valueOf(sm.sizes()[1]));

		prop.put("objBehaviours", getString(sm.getObjectiveBehaviours()));

		prop.put("start", getString(sm.getStart()));
		prop.put("goal", getString(sm.getGoal()));

		prop.put("viewingFrustumBorders",
				getString(sm.getViewingFrustumBorders()));
		prop.put("executionType", sm.getExecutionType().toString());

		prop.put("randomGeneration", "false");

		prop.put("numberOfThreats", String.valueOf(sm.getThreats().size()));
		for (int i = 1; i <= sm.getThreats().size(); i++) {
			prop.put("threat" + String.valueOf(i), sm.getThreats().get(i - 1)
					.toPropString());
		}

		prop.put("numberOfObstacles", String.valueOf(sm.getObstacles().size()));
		for (int i = 1; i <= sm.getObstacles().size(); i++) {
			prop.put("obstacle" + String.valueOf(i), getString(sm
					.getObstacles().get(i - 1).getCoords().getInts()));
		}

		try {
			prop.store(new FileOutputStream(System.getProperty("user.dir")
					+ RESOURCE + fileName),
					"System settings are stored under: " + RESOURCE + fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace(); // WTF??
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "Properties are saved under : " + RESOURCE + fileName;
	}

	private static String getString(int... intArray) {
		String propString = "";
		for (int i = 0; i < intArray.length; i++) {
			int actualInt = intArray[i];
			if (i == intArray.length - 1) {
				propString += actualInt;
			} else {
				propString += actualInt + ", ";
			}
		}

		return propString;
	}

	private static String getString(ObjectiveBehaviour[] objectiveBehaviours) {
		String propString = "";
		for (int i = 0; i < objectiveBehaviours.length; i++) {
			ObjectiveBehaviour objectiveBehaviour = objectiveBehaviours[i];
			if (i == objectiveBehaviours.length - 1) {
				propString += objectiveBehaviour.toString();
			} else {
				propString += objectiveBehaviour.toString() + ", ";
			}
		}

		return propString;
	}

}
