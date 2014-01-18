package tr.edu.metu.ceng.ms.thesis.mogpp.core.ga;

import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;

/**
 * The agent who tries to reach to the goal cell. Note that there is only one
 * agent in our environment, so this class has only one instance.
 * 
 * @author tugcem
 * 
 */
public class Agent {

	private static Agent agent;

	private Coordinate currentLocation;

	public static Agent getAgent() {
		if (agent == null) {
			agent = new Agent();
		}
		return agent;
	}

	private Agent() {
	}

	private boolean isReachedToGoal;

	public boolean isReachedToGoal() {
		return isReachedToGoal;
	}

	public void setReachedToGoal(boolean isReachedToGoal) {
		this.isReachedToGoal = isReachedToGoal;
	}

	public Coordinate getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Coordinate currentLocation) {
		this.currentLocation = currentLocation;
	}

}
