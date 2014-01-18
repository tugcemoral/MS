package tr.edu.metu.ceng.ms.thesis.modstarlite.data;

public enum ExecutionType {

	/**
	 * Displays the MOD*Lite execution step by step.
	 */
	STEP_BY_STEP,

	/**
	 * Do not display each step, just moves through the nearest border of
	 * current viewing frustum area.
	 */
	VIEWING_FRUSTUM_BORDER,

	/**
	 * Just shows the final result(s) it available.
	 */
	GOAL_DIRECTED;

}
