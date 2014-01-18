package tr.edu.metu.ceng.ms.thesis.modstarlite.data;

/**
 * This strategies are used to find temporary goals inside viewing frustum area.
 * 
 * @author tugcem
 * 
 */
public enum TemporaryGoalStrategy {

	/**
	 * Used to get nearest state inside viewing frustum to actual goal by
	 * shortest path.
	 */
	NEAREST_BY_SP,

	/**
	 * Used to get nearest state inside viewing frustum to actual goal by
	 * minimum risk.
	 */
	NEAREST_BY_MR;

}
