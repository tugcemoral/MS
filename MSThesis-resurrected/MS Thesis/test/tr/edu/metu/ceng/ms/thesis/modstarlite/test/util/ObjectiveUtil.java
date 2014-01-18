package tr.edu.metu.ceng.ms.thesis.modstarlite.test.util;

import java.util.List;
import java.util.Vector;

import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.Key;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Objective;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveArray;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveBehaviour;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;

public class ObjectiveUtil {

	public static Objective createMinimizedObjective(int value) {
		return new Objective(value, ObjectiveBehaviour.MINIMIZED);
	}

	public static Objective createMinimizedObjective(double value) {
		return new Objective(value, ObjectiveBehaviour.MINIMIZED);
	}

	public static Objective createMaximizedObjective(int value) {
		return new Objective(value, ObjectiveBehaviour.MAXIMIZED);
	}

	public static IntCoord createIntCoord(int x, int y) {
		return new IntCoord(x, y);
	}

	public static Key generateKey(IntCoord state, int k1Value, int k2Value) {
		// create minimized k1 and k2 wrt given state's coordinates.
		List<ObjectiveArray> k1 = createMinimizedObjectiveList(
				state.getInts()[0], k1Value);
		List<ObjectiveArray> k2 = createMinimizedObjectiveList(
				state.getInts()[1], k2Value);

		return new Key(k1, k2);
	}
	
	public static Key generateKey(int k11, int k12, int k21,
			int k22) {
		// create minimized k1 and k2 wrt given parameters.
		List<ObjectiveArray> k1 = createMinimizedObjectiveList(
				k11, k12);
		List<ObjectiveArray> k2 = createMinimizedObjectiveList(
				k21, k22);
		
		return new Key(k1, k2);
	}

	private static List<ObjectiveArray> createMinimizedObjectiveList(
			int value1, int value2) {
		List<ObjectiveArray> oal = new Vector<ObjectiveArray>();
		oal.add(createObjectiveArray(value1, value2));
		return oal;
	}

	private static ObjectiveArray createObjectiveArray(int value1, int value2) {
		ObjectiveArray oa = new ObjectiveArray(
				createMinimizedObjective(value1),
				createMinimizedObjective(value2));
		return oa;
	}

}
