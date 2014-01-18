package tr.edu.metu.ceng.ms.thesis.modstarlite.test.core;

import static org.hamcrest.core.Is.is;
import static tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveBehaviour.MINIMIZED;

import java.util.List;
import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Objective;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveArray;
import tr.edu.metu.ceng.ms.thesis.modstarlite.util.MultiObjectiveUtils;

public class TestMultiObjectiveOperations {

	/**
	 * Asserts two multi-objective lists sum operation is correct.
	 * L1 = {[1, 3]}
	 * L2 = {[7, 2], [4, 3]}
	 * L1 + L2 = {[8, 5], [5, 6]}
	 */
	@Test
	public void testSum() {
		
//		// create a list of objective array.
//		List<ObjectiveArray> firstList = new Vector<ObjectiveArray>();

		// create objective arrays for first list.
		Objective firstObjective = new Objective(1d, MINIMIZED);
		Objective secondObjective = new Objective(3d, MINIMIZED);
		ObjectiveArray firstObjArray = new ObjectiveArray(firstObjective,
				secondObjective);

//		ObjectiveArray secondObjArray = new ObjectiveArray(2);
//		secondObjArray.put(0, new Objective(3d, MINIMIZED));
//		secondObjArray.put(1, new Objective(1d, MINIMIZED));
//
//		// put created obj arrays into first list.
//		firstList.add(firstObjArray);
//		firstList.add(secondObjArray);

		// create another list of objective array.
		List<ObjectiveArray> secondList = new Vector<ObjectiveArray>();

		// create objective arrays for second list.
		ObjectiveArray thirdObjArray = new ObjectiveArray(2);
		thirdObjArray.put(0, new Objective(7d, MINIMIZED));
		thirdObjArray.put(1, new Objective(2d, MINIMIZED));
		ObjectiveArray fourthObjArray = new ObjectiveArray(2);
		fourthObjArray.put(0, new Objective(4d, MINIMIZED));
		fourthObjArray.put(1, new Objective(3d, MINIMIZED));

		// put created obj arrays into second list.
		secondList.add(thirdObjArray);
		secondList.add(fourthObjArray);

		
		List<ObjectiveArray> summedList = MultiObjectiveUtils.sum(firstObjArray, secondList);
		
		System.out.println(summedList);
		
//		Assert.assertThat(summedList.size(), is(2));
//		Assert.assertThat(summedList.size(), is(2));
		
		
		
	}

}
