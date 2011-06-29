package tr.edu.metu.ec.pp.world;

/**
 * The agent who tries to reach to the goal cell. Note that there is only one
 * agent in our environment, so this class has only one instance.
 * 
 * @author tugcem
 * 
 */
public class Agent {

	private static Agent agent;

	private Location currentLocation;

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

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}

}
