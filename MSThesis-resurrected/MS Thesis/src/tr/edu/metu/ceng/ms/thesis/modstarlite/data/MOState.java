package tr.edu.metu.ceng.ms.thesis.modstarlite.data;

import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;

/**
 * @author tugcem
 * 
 */
public class MOState {

	/**
	 * The objective array for this multi-objective state.
	 */
	private ObjectiveArray objectives;

	/**
	 * Determines whether this constaint cell is an obstacle (true) or not
	 * (false).
	 * 
	 */
	private boolean hasObstacle;

	private boolean isInThreat = false;

	private double totalRisk;

	private boolean isInViewingFrustum = false;

	private boolean isPossibleTmpGoal = false;
	
	private boolean isTraversable;

	private IntCoord coords;

	public MOState(ObjectiveArray objectives, boolean isObstacle) {
		this.objectives = objectives;
		this.hasObstacle = isObstacle;
		this.coords = new IntCoord(-1, -1);
	}

	/**
	 * Default constructor for MOState.
	 */
	public MOState(int x, int y) {
		coords = new IntCoord(x, y);
	}

	public boolean hasObstacle() {
		return hasObstacle;
	}

	public void setHasObstacle(boolean isObstacle) {
		this.hasObstacle = isObstacle;
	}

	@Override
	public String toString() {
		return "{" + this.getObjectives() + ", " + this.hasObstacle() + "}";
	}

	public String toShortenString() {
		return this.getObjectives().toShortenString();
	}

	public boolean dominates(MOState that) {
		// get objectives from states.
		Objective[] objectivesA = this.getObjectives().getObjectives();
		Objective[] objectivesB = that.getObjectives().getObjectives();

		// check that sizes are equal.
		if (objectivesA.length != objectivesB.length) {
			throw new IllegalStateException(
					"Sizes of objectives of neighbor states should be same");
		}

		int dominationCountForA = 0;
		int dominationCountForB = 0;
		int equalityCount = 0;

		for (int i = 0; i < objectivesA.length; i++) {
			// get corresponding objectives to determine domination.
			Objective objectiveA = objectivesA[i];
			Objective objectiveB = objectivesB[i];

			if (!objectiveA.getBehaviour().equals(objectiveB.getBehaviour())) {
				throw new IllegalStateException(
						"Equivalent objectives must have same behaviour!");
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

	public static MOState getInfiniteState(ObjectiveBehaviour[] objBehaviours) {
		// create infinite objectives according to given behaviours.
		Objective[] objectives = new Objective[objBehaviours.length];
		// construct the dominatable (by any objective vector) objectives.
		for (int i = 0; i < objBehaviours.length; i++) {
			if (objBehaviours[i].equals(ObjectiveBehaviour.MINIMIZED)) {
				objectives[i] = new Objective(Integer.MAX_VALUE,
						ObjectiveBehaviour.MINIMIZED);
			} else {
				objectives[i] = new Objective(Integer.MIN_VALUE,
						ObjectiveBehaviour.MAXIMIZED);
			}
		}
		return new MOState(new ObjectiveArray(objectives), true);
	}

	public ObjectiveArray getObjectives() {
		return objectives;
	}

	public void setObjectives(ObjectiveArray objectives) {
		this.objectives = objectives;
	}

	public boolean isInThreat() {
		return isInThreat;
	}

	public void setInThreat(boolean isInThreat) {
		this.isInThreat = isInThreat;
	}

	public void addRisk(double threatZoneRisk) {
		// set corresponding risk for this state.
		setTotalRisk(getTotalRisk() + threatZoneRisk);
	}

	public double getTotalRisk() {
		return totalRisk;
	}

	private void setTotalRisk(double totalRisk) {
		this.totalRisk = totalRisk;
	}

	public boolean isInViewingFrustum() {
		return isInViewingFrustum;
	}

	public void setInViewingFrustum(boolean isInViewingFrustum) {
		this.isInViewingFrustum = isInViewingFrustum;
	}

	public boolean isTraversable() {
		return isTraversable;
	}

	public void setTraversable(boolean isTraversable) {
		this.isTraversable = isTraversable;
	}

	public IntCoord getCoords() {
		return coords;
	}

	public void setCoords(IntCoord coords) {
		this.coords = coords;
	}

	public boolean isPossibleTmpGoal() {
		return isPossibleTmpGoal;
	}

	public void setPossibleTmpGoal(boolean isPossibleTmpGoal) {
		this.isPossibleTmpGoal = isPossibleTmpGoal;
	}
}
