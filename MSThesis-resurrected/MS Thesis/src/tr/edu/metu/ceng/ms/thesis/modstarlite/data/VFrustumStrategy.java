package tr.edu.metu.ceng.ms.thesis.modstarlite.data;

public enum VFrustumStrategy {

	/**
	 * This strategy keeps visited states in viewing frustum borders and re-plan
	 * according to.
	 */
	KEEP_VISITED_STATES,

	/**
	 * This strategy refreshes viewing frustum area on each movement; thus,
	 * removes states which out of boundaries.
	 */
	REFRESH;

}
