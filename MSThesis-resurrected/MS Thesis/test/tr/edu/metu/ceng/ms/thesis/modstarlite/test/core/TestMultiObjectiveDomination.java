package tr.edu.metu.ceng.ms.thesis.modstarlite.test.core;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.MOState;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Objective;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveArray;
import tr.edu.metu.ceng.ms.thesis.modstarlite.test.util.ObjectiveUtil;

public class TestMultiObjectiveDomination {

	@Test
	public void testDominationOnMinimization() throws Exception {
		// create four minimization objectives with value.
		Objective objectiveA = ObjectiveUtil.createMinimizedObjective(2);
		Objective objectiveB = ObjectiveUtil.createMinimizedObjective(3);
		Objective objectiveC = ObjectiveUtil.createMinimizedObjective(4);
		Objective objectiveD = ObjectiveUtil.createMinimizedObjective(5);

		// create two new objective arrays with these objectives.
		Objective[] dominated = new Objective[] { objectiveA, objectiveC };
		Objective[] other = new Objective[] { objectiveB, objectiveD };

		// create two states with corresponding objective arrays.
		MOState stateA = new MOState(new ObjectiveArray(dominated), false);
		MOState stateB = new MOState(new ObjectiveArray(other), false);

		// assert that stateA dominates stateB.
		assertTrue(stateA.dominates(stateB));
		// assert that stateB can not dominate stateA.
		assertFalse(stateB.dominates(stateA));
	}
	
	@Test
	public void testDominationOnMinimizationWithEquality() throws Exception {
		// create four minimization objectives with value.
		Objective objectiveA = ObjectiveUtil.createMinimizedObjective(2);
		Objective objectiveB = ObjectiveUtil.createMinimizedObjective(3);
		Objective objectiveC = ObjectiveUtil.createMinimizedObjective(4);
//		Objective objectiveD = ObjectiveUtil.createMinimizedObjective(5);
		
		// create two new objective arrays with these objectives.
		Objective[] dominated = new Objective[] { objectiveA, objectiveC };
		Objective[] other = new Objective[] { objectiveB, objectiveC };
		
		// create two states with corresponding objective arrays.
		MOState stateA = new MOState(new ObjectiveArray(dominated), false);
		MOState stateB = new MOState(new ObjectiveArray(other), false);
		
		// assert that stateA dominates stateB.
		assertTrue(stateA.dominates(stateB));
		// assert that stateB can not dominate stateA.
		assertFalse(stateB.dominates(stateA));
	}

	@Test
	public void testDominationOnMaximization() throws Exception {
		// create four minimization objectives with value.
		Objective objectiveA = ObjectiveUtil.createMaximizedObjective(2);
		Objective objectiveB = ObjectiveUtil.createMaximizedObjective(3);
		Objective objectiveC = ObjectiveUtil.createMaximizedObjective(4);
		Objective objectiveD = ObjectiveUtil.createMaximizedObjective(5);

		// create two new objective arrays with these objectives.
		Objective[] dominated = new Objective[] { objectiveB, objectiveD };
		Objective[] other = new Objective[] { objectiveA, objectiveC };

		// create two states with corresponding objective arrays.
		MOState stateA = new MOState(new ObjectiveArray(dominated), false);
		MOState stateB = new MOState(new ObjectiveArray(other), false);

		// assert that stateA dominates stateB.
		assertTrue(stateA.dominates(stateB));
		// assert that stateB can not dominate stateA.
		assertFalse(stateB.dominates(stateA));
	}

	@Test
	public void testDominationOnMixedBehaviour() throws Exception {
		// create four minimization objectives with value.
		Objective objectiveA = ObjectiveUtil.createMinimizedObjective(2);
		Objective objectiveB = ObjectiveUtil.createMinimizedObjective(3);
		Objective objectiveC = ObjectiveUtil.createMaximizedObjective(4);
		Objective objectiveD = ObjectiveUtil.createMaximizedObjective(5);

		// create two new objective arrays with these objectives.
		Objective[] dominated = new Objective[] { objectiveA, objectiveD };
		Objective[] other = new Objective[] { objectiveB, objectiveC };

		// create two states with corresponding objective arrays.
		MOState stateA = new MOState(new ObjectiveArray(dominated), false);
		MOState stateB = new MOState(new ObjectiveArray(other), false);

		// assert that stateA dominates stateB.
		assertTrue(stateA.dominates(stateB));
		// assert that stateB can not dominate stateA.
		assertFalse(stateB.dominates(stateA));
	}

	@Test
	public void testDominationOnSameValueOnOne() throws Exception {
		// create four minimization objectives with value.
		Objective objectiveA = ObjectiveUtil.createMinimizedObjective(2);
		Objective objectiveB = ObjectiveUtil.createMinimizedObjective(3);
		Objective objectiveC = ObjectiveUtil.createMinimizedObjective(5);

		// create two new objective arrays with these objectives.
		Objective[] dominated = new Objective[] { objectiveA, objectiveB };
		Objective[] other = new Objective[] { objectiveA, objectiveC };

		// create two states with corresponding objective arrays.
		MOState stateA = new MOState(new ObjectiveArray(dominated), false);
		MOState stateB = new MOState(new ObjectiveArray(other), false);

		// assert that stateA dominates stateB.
		assertTrue(stateA.dominates(stateB));
		// assert that stateB can not dominate stateA.
		assertFalse(stateB.dominates(stateA));
	}

	@Test
	public void testDominationOnSameValueOnMixedBehaviours() throws Exception {
		// create four minimization objectives with value.
		Objective objectiveA = ObjectiveUtil.createMinimizedObjective(2);
		Objective objectiveB = ObjectiveUtil.createMinimizedObjective(3);
		Objective objectiveC = ObjectiveUtil.createMinimizedObjective(5);

		Objective objectiveD = ObjectiveUtil.createMaximizedObjective(6);
		Objective objectiveE = ObjectiveUtil.createMaximizedObjective(5);

		// create two new objective arrays with these objectives.
		Objective[] dominated = new Objective[] { objectiveA, objectiveB,
				objectiveD };
		Objective[] other = new Objective[] { objectiveA, objectiveC,
				objectiveE };

		// create two states with corresponding objective arrays.
		MOState stateA = new MOState(new ObjectiveArray(dominated), false);
		MOState stateB = new MOState(new ObjectiveArray(other), false);

		// assert that stateA dominates stateB.
		assertTrue(stateA.dominates(stateB));
		// assert that stateB can not dominate stateA.
		assertFalse(stateB.dominates(stateA));
	}

	@Test
	public void testDominationOnSameNValuesOnMixedBehaviours() throws Exception {
		// [2, 4, 5, 6]
		// [2, 3, 6, 6]
		// create four minimization objectives with value.
		Objective objectiveA = ObjectiveUtil.createMinimizedObjective(2);
		Objective objectiveB = ObjectiveUtil.createMinimizedObjective(5);
		Objective objectiveC = ObjectiveUtil.createMinimizedObjective(6);

		Objective objectiveD = ObjectiveUtil.createMaximizedObjective(3);
		Objective objectiveE = ObjectiveUtil.createMaximizedObjective(4);
		Objective objectiveF = ObjectiveUtil.createMaximizedObjective(6);

		// create two new objective arrays with these objectives. [2,4,5,6]
		Objective[] dominated = new Objective[] { objectiveA, objectiveE,
				objectiveB, objectiveF };
		// [2,3,6,6]
		Objective[] other = new Objective[] { objectiveA, objectiveD,
				objectiveC, objectiveF };

		// create two states with corresponding objective arrays.
		MOState stateA = new MOState(new ObjectiveArray(dominated), false);
		MOState stateB = new MOState(new ObjectiveArray(other), false);

		// assert that stateA dominates stateB.
		assertTrue(stateA.dominates(stateB));
		// assert that stateB can not dominate stateA.
		assertFalse(stateB.dominates(stateA));
	}

}
