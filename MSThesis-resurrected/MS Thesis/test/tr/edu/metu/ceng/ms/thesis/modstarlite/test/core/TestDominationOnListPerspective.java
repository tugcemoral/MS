package tr.edu.metu.ceng.ms.thesis.modstarlite.test.core;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveBehaviour.MINIMIZED;

import java.util.List;
import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Objective;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveArray;
import tr.edu.metu.ceng.ms.thesis.modstarlite.util.MultiObjectiveUtils;

public class TestDominationOnListPerspective {

	@Test
	public void testConstructionNonDomList() throws Exception {

		// create a list of objective array.
		List<ObjectiveArray> firstList = new Vector<ObjectiveArray>();

		// create objective arrays for first list.
		Objective firstObjective = new Objective(10d, MINIMIZED);
		Objective secondObjective = new Objective(6d, MINIMIZED);
		ObjectiveArray firstObjArray = new ObjectiveArray(firstObjective,
				secondObjective);

		ObjectiveArray secondObjArray = new ObjectiveArray(2);
		secondObjArray.put(0, new Objective(8d, MINIMIZED));
		secondObjArray.put(1, new Objective(9d, MINIMIZED));

		// put created obj arrays into first list.
		firstList.add(firstObjArray);
		firstList.add(secondObjArray);

		// create another list of objective array.
		List<ObjectiveArray> secondList = new Vector<ObjectiveArray>();

		// create objective arrays for second list.
		ObjectiveArray thirdObjArray = new ObjectiveArray(2);
		thirdObjArray.put(0, new Objective(7d, MINIMIZED));
		thirdObjArray.put(1, new Objective(8d, MINIMIZED));
		ObjectiveArray fourthObjArray = new ObjectiveArray(2);
		fourthObjArray.put(0, new Objective(15d, MINIMIZED));
		fourthObjArray.put(1, new Objective(4d, MINIMIZED));

		// put created obj arrays into second list.
		secondList.add(thirdObjArray);
		secondList.add(fourthObjArray);

		// get non-dominated list of these lists.
		List<ObjectiveArray> nonDominatedList = MultiObjectiveUtils
				.nonDominatedList(firstList, secondList);

		Assert.assertThat(nonDominatedList.size(), is(3));

		Assert.assertThat(nonDominatedList.contains(firstObjArray), is(true));
		Assert.assertThat(nonDominatedList.contains(secondObjArray), is(false));
		Assert.assertThat(nonDominatedList.contains(thirdObjArray), is(true));
		Assert.assertThat(nonDominatedList.contains(fourthObjArray), is(true));

	}

	@Test
	public void testNonDomination() throws Exception {

		// create a list of objective array.
		List<ObjectiveArray> firstList = new Vector<ObjectiveArray>();

		// create objective arrays for first list.
		Objective firstObjective = new Objective(10d, MINIMIZED);
		Objective secondObjective = new Objective(6d, MINIMIZED);
		ObjectiveArray firstObjArray = new ObjectiveArray(firstObjective,
				secondObjective);

		ObjectiveArray secondObjArray = new ObjectiveArray(2);
		secondObjArray.put(0, new Objective(8d, MINIMIZED));
		secondObjArray.put(1, new Objective(9d, MINIMIZED));

		// put created obj arrays into first list.
		firstList.add(firstObjArray);
		firstList.add(secondObjArray);

		// create another list of objective array.
		List<ObjectiveArray> secondList = new Vector<ObjectiveArray>();

		// create objective arrays for second list.
		ObjectiveArray thirdObjArray = new ObjectiveArray(2);
		thirdObjArray.put(0, new Objective(11d, MINIMIZED));
		thirdObjArray.put(1, new Objective(9d, MINIMIZED));
		ObjectiveArray fourthObjArray = new ObjectiveArray(2);
		fourthObjArray.put(0, new Objective(10d, MINIMIZED));
		fourthObjArray.put(1, new Objective(10d, MINIMIZED));

		// put created obj arrays into second list.
		secondList.add(thirdObjArray);
		secondList.add(fourthObjArray);

		// get non-dominated list of these lists.
		List<ObjectiveArray> nonDominatedList = MultiObjectiveUtils
				.nonDominatedList(firstList, secondList);

		Assert.assertThat(nonDominatedList.size(), is(2));

		Assert.assertThat(nonDominatedList.contains(firstObjArray), is(true));
		Assert.assertThat(nonDominatedList.contains(secondObjArray), is(true));
		Assert.assertThat(nonDominatedList.contains(thirdObjArray), is(false));
		Assert.assertThat(nonDominatedList.contains(fourthObjArray), is(false));

		Assert.assertThat(nonDominatedList, equalTo(firstList));

	}

	@Test
	public void testCompletelyDomination() throws Exception {

		// create objective arrays for first list.
		Objective firstObjective = new Objective(15d, MINIMIZED);
		Objective secondObjective = new Objective(71d, MINIMIZED);
		ObjectiveArray firstObjArray = new ObjectiveArray(firstObjective,
				secondObjective);

		ObjectiveArray secondObjArray = new ObjectiveArray(2);
		secondObjArray.put(0, new Objective(23d, MINIMIZED));
		secondObjArray.put(1, new Objective(0d, MINIMIZED));

		ObjectiveArray thirdObjArray = new ObjectiveArray(2);
		thirdObjArray.put(0, new Objective(25d, MINIMIZED));
		thirdObjArray.put(1, new Objective(0d, MINIMIZED));

		// create a list of objective array.
		List<ObjectiveArray> firstList = new Vector<ObjectiveArray>();
		// put created obj arrays into first list.
		firstList.add(firstObjArray);
		firstList.add(secondObjArray);

		// create another list of objective array.
		List<ObjectiveArray> secondList = new Vector<ObjectiveArray>();
		// put created obj arrays into second list.
		secondList.add(firstObjArray);
		secondList.add(thirdObjArray);

		Assert.assertThat(
				MultiObjectiveUtils.completelyDominates(firstList, secondList),
				is(true));

	}
	
//	[80.0 (MIN), 432.52196585556237 (MIN)],
//	[78.0 (MIN), 549.1765923698327 (MIN)],
//	[72.0 (MIN), 549.1765923698327 (MIN)], 
//	[68.0 (MIN), 724.1585321412383 (MIN)], 
//	[66.0 (MIN), 1074.1224116840494 (MIN)], 
//	[64.0 (MIN), 1890.7047972839405 (MIN)], 
}
