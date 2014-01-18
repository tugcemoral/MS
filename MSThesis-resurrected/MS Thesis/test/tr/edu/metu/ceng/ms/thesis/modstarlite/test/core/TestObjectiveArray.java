package tr.edu.metu.ceng.ms.thesis.modstarlite.test.core;

import static junit.framework.Assert.assertFalse;
import org.junit.Test;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Objective;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveArray;
import tr.edu.metu.ceng.ms.thesis.modstarlite.test.util.ObjectiveUtil;

public class TestObjectiveArray {

	@Test
	public void testNotDominationEachOther() {

		Objective oa = ObjectiveUtil.createMinimizedObjective(24.0);
		Objective ob = ObjectiveUtil.createMinimizedObjective(1.0);
		Objective oc = ObjectiveUtil.createMinimizedObjective(22.0);
		Objective od = ObjectiveUtil.createMinimizedObjective(1.2);

		ObjectiveArray oa1 = new ObjectiveArray(oa, ob);
		ObjectiveArray oa2 = new ObjectiveArray(oc, od);

		assertFalse(oa1.dominates(oa2));
		assertFalse(oa2.dominates(oa1));

	}

}
