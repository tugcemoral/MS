package tr.edu.metu.ceng.ms.thesis.modstarlite.data;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import tr.edu.metu.ceng.ms.thesis.modstarlite.util.InconsistentObjectiveBehaviourException;
import tr.edu.metu.ceng.ms.thesis.modstarlite.util.InconsistentObjectiveSizeException;

/**
 * This class is used to hold an objective array for a state.
 * 
 * @author tugcem
 * 
 */
public class ObjectiveArray {

	public static List<ObjectiveArray> ZERO = new Vector<ObjectiveArray>();
	
	public static ObjectiveArray SINGLE_ZERO;
	
	public static ObjectiveArray SINGLE_DOMINATIBLE_MIN;

	// TODO: should be parameterized by obj behaviour.
	private static List<ObjectiveArray> POSITIVE_INF_FOR_MIN = new Vector<ObjectiveArray>();

	static {
		// construct static objective arrays...
		// List<ObjectiveArray> zeroList = new Vector<ObjectiveArray>();
		// zeroList.add(new ObjectiveArray(Objective.ZERO, Objective.ZERO));
		// ZERO = Collections.unmodifiableList(zeroList);
		ZERO.add(new ObjectiveArray(Objective.ZERO, Objective.ZERO));

		// List<ObjectiveArray> posInfForMinList = new Vector<ObjectiveArray>();
		// posInfForMinList.add(new ObjectiveArray(
		// Objective.dominatableMinimizedObj,
		// Objective.dominatableMinimizedObj));
		// POSITIVE_INF_FOR_MIN =
		// Collections.unmodifiableList(posInfForMinList);
		POSITIVE_INF_FOR_MIN.add(new ObjectiveArray(
				Objective.INFINITY,
				Objective.INFINITY));
		
		SINGLE_ZERO = new ObjectiveArray(Objective.ZERO, Objective.ZERO);
		SINGLE_DOMINATIBLE_MIN = new ObjectiveArray(Objective.INFINITY, Objective.INFINITY);
	}

	private Objective[] objectives;

	public ObjectiveArray(Objective... objectives) {
		this.objectives = objectives;
	}

	public ObjectiveArray(int capacity) {
		this.objectives = new Objective[capacity];
	}

	public Objective[] getObjectives() {
		return objectives;
	}

	public void setObjectives(Objective[] objectives) {
		this.objectives = objectives;
	}

	@Override
	public String toString() {
		String output = "[";

		for (int i = 0; i < this.getObjectives().length; i++) {
			if (i < this.getObjectives().length - 1) {
				output += this.getObjectives()[i].toString() + ", ";
			} else {
				output += this.getObjectives()[i].toString();
			}
		}
		output += "]";
		return output;
	}

	public String toShortenString() {
		String output = "[";
		for (int i = 0; i < this.getObjectives().length; i++) {
			if (i < this.getObjectives().length - 1) {
				output += this.getObjectives()[i].toShortenString() + ",";
			} else {
				output += this.getObjectives()[i].toShortenString();
			}
		}
		output += "]";
		return output;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(objectives);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjectiveArray other = (ObjectiveArray) obj;
		if (!Arrays.equals(objectives, other.objectives))
			return false;
		return true;
	}

	public static List<ObjectiveArray> positiveInfinityForMinimizedBehaviour() {

		List<ObjectiveArray> POSITIVE_INF_FOR_MIN = new Vector<ObjectiveArray>();
		POSITIVE_INF_FOR_MIN.add(new ObjectiveArray(
				Objective.INFINITY,
				Objective.INFINITY));

		return POSITIVE_INF_FOR_MIN;
	}

	public int size() {
		return this.getObjectives().length;
	}

	public Objective get(int index) throws IndexOutOfBoundsException {
		return this.getObjectives()[index];
	}

	public void put(int index, Objective objective) {
		this.getObjectives()[index] = objective;
	}

	public boolean dominates(ObjectiveArray that) {
		// get objectives from states.
		Objective[] objectivesA = this.getObjectives();
		Objective[] objectivesB = that.getObjectives();

		// check that sizes are equal.
		if (objectivesA.length != objectivesB.length) {
			throw new InconsistentObjectiveSizeException();
		}

		int dominationCountForA = 0;
		int dominationCountForB = 0;
		int equalityCount = 0;

		for (int i = 0; i < objectivesA.length; i++) {
			// get corresponding objectives to determine domination.
			Objective objectiveA = objectivesA[i];
			Objective objectiveB = objectivesB[i];

			if (!objectiveA.getBehaviour().equals(objectiveB.getBehaviour())) {
				throw new InconsistentObjectiveBehaviourException();
			}

			// if this objective is designed to minimize.
			if (objectiveA.getBehaviour().equals(ObjectiveBehaviour.MINIMIZED)) {
				if (objectiveA.getValue() < objectiveB.getValue()) {
					dominationCountForA++;
				} else if (objectiveB.getValue() < objectiveA.getValue()) {
					dominationCountForB++;
				} else {
					// the equality condition for two objectives.
					equalityCount++;
				}
			} else {
				if (objectiveA.getValue() > objectiveB.getValue()) {
					dominationCountForA++;
				} else if (objectiveB.getValue() > objectiveA.getValue()) {
					dominationCountForB++;
				} else {
					// the equality condition for two objectives.
					equalityCount++;
				}
			}
		}

		if (equalityCount != 0) {
			if (equalityCount < objectivesA.length) {
				return (dominationCountForA == (objectivesA.length - equalityCount)) ? true
						: false;
			} else {
				// means that all objectives are same.
				return false;
			}
		} else {
			return (dominationCountForA == objectivesA.length) ? true : false;
		}

	}

}
