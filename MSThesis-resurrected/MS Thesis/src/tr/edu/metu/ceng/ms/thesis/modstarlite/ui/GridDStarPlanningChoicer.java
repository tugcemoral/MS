package tr.edu.metu.ceng.ms.thesis.modstarlite.ui;

import tr.edu.metu.ceng.ms.thesis.modstarlite.ui.components.MOStaticMap;
import tr.edu.metu.ceng.ms.thesis.robotutils.ui.GridDStarPlanning;

public class GridDStarPlanningChoicer {

	public GridDStarPlanningChoicer(MOStaticMap sm, int[] start, int[] goal) {
		new GridDStarPlanningWithSpecifiedMap(sm, start, goal).execute();
	}

	public GridDStarPlanningChoicer() {
		new GridDStarPlanning().execute();
	}

}
