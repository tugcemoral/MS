package tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds;

import java.util.List;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveArray;
import tr.edu.metu.ceng.ms.thesis.modstarlite.util.MultiObjectiveUtils;

/**
 * A tuple with two components used to assign priorities to states in the D*
 * search. Keys are compared according to a lexical ordering, e.g. k < k' iff
 * either k1 < k'1 or (k1 = k'1 and k2 < k'2).
 */
public class Key implements Comparable<Key> {

	final List<ObjectiveArray> objectivesListA;

	final List<ObjectiveArray> objectivesListB;

	public Key(List<ObjectiveArray> objectivesA,
			List<ObjectiveArray> objectivesB) {
		this.objectivesListA = objectivesA;
		this.objectivesListB = objectivesB;
	}

	public int compareTo(Key that) {
		if (MultiObjectiveUtils.completelyDominates(this.objectivesListA,
				that.objectivesListA)) {
			return -1;
		} else if (MultiObjectiveUtils.completelyDominates(
				that.objectivesListA, this.objectivesListA)) {
			return 1;
		} else if (MultiObjectiveUtils.equals(this.objectivesListA,
				that.objectivesListA)) {
			if (MultiObjectiveUtils.completelyDominates(this.objectivesListB,
					that.objectivesListB)) {
				return -1;
			} else if (MultiObjectiveUtils.completelyDominates(
					that.objectivesListB, this.objectivesListB)) {
				return 1;
			} else {
				// the situation that k2's are non-dominated or equal.
				return 0;
			}
		} else {
			// the situation that k1's are non-dominated.
			return 0;
		}
	}

	@Override
	public String toString() {
		String output = "<";
		for (ObjectiveArray objArray : this.objectivesListA) {
			output += " [" + objArray.toString() + "] ";
		}
		output += "; ";
		for (ObjectiveArray objArray : this.objectivesListB) {
			output += "[" + objArray.toString() + "] ";
		}
		output += ">";
		return output;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((objectivesListA == null) ? 0 : objectivesListA.hashCode());
		result = prime * result
				+ ((objectivesListB == null) ? 0 : objectivesListB.hashCode());
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
		Key other = (Key) obj;
		if (objectivesListA == null) {
			if (other.objectivesListA != null)
				return false;
		} else if (!objectivesListA.equals(other.objectivesListA))
			return false;
		if (objectivesListB == null) {
			if (other.objectivesListB != null)
				return false;
		} else if (!objectivesListB.equals(other.objectivesListB))
			return false;
		return true;
	}
}
