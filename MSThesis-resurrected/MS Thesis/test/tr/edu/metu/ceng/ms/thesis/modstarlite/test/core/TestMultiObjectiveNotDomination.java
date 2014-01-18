package tr.edu.metu.ceng.ms.thesis.modstarlite.test.core;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.MOState;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Objective;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveArray;
import tr.edu.metu.ceng.ms.thesis.modstarlite.test.util.ObjectiveUtil;

public class TestMultiObjectiveNotDomination {

	@Rule
	public ExpectedException exc = ExpectedException.none();

	@Test
	public void testNotDominationOnMinimization() throws Exception {
		// create four minimization objectives with value.
		Objective objectiveA = ObjectiveUtil.createMinimizedObjective(2);
		Objective objectiveB = ObjectiveUtil.createMinimizedObjective(3);
		Objective objectiveC = ObjectiveUtil.createMinimizedObjective(4);
		Objective objectiveD = ObjectiveUtil.createMinimizedObjective(5);

		// create two new objective arrays with these objectives.
		Objective[] dominated = new Objective[] { objectiveA, objectiveD };
		Objective[] other = new Objective[] { objectiveB, objectiveC };

		// create two states with corresponding objective arrays.
		MOState stateA = new MOState(new ObjectiveArray(dominated), false);
		MOState stateB = new MOState(new ObjectiveArray(other), false);

		// assert that stateA can not dominate stateB.
		assertFalse(stateA.dominates(stateB));
		// assert that stateB can not dominate stateA.
		assertFalse(stateB.dominates(stateA));
	}

	@Test
	public void testDominationOnMaximization() throws Exception {
		// create four maximization objectives with value.
		Objective objectiveA = ObjectiveUtil.createMaximizedObjective(2);
		Objective objectiveB = ObjectiveUtil.createMaximizedObjective(3);
		Objective objectiveC = ObjectiveUtil.createMaximizedObjective(4);
		Objective objectiveD = ObjectiveUtil.createMaximizedObjective(5);

		// create two new objective arrays with these objectives.
		Objective[] dominated = new Objective[] { objectiveA, objectiveD };
		Objective[] other = new Objective[] { objectiveB, objectiveC };

		// create two states with corresponding objective arrays.
		MOState stateA = new MOState(new ObjectiveArray(dominated), false);
		MOState stateB = new MOState(new ObjectiveArray(other), false);

		// assert that stateA can not dominate stateB.
		assertFalse(stateA.dominates(stateB));
		// assert that stateB can not dominate stateA.
		assertFalse(stateB.dominates(stateA));
	}

	@Test
	public void testNotDominationOnMixedBehaviour() throws Exception {
		// create four min/max objectives with value.
		Objective objectiveA = ObjectiveUtil.createMinimizedObjective(2);
		Objective objectiveB = ObjectiveUtil.createMinimizedObjective(3);
		Objective objectiveC = ObjectiveUtil.createMaximizedObjective(4);
		Objective objectiveD = ObjectiveUtil.createMaximizedObjective(5);

		// create two new objective arrays with these objectives.
		Objective[] dominated = new Objective[] { objectiveA, objectiveC };
		Objective[] other = new Objective[] { objectiveB, objectiveD };

		// create two states with corresponding objective arrays.
		MOState stateA = new MOState(new ObjectiveArray(dominated), false);
		MOState stateB = new MOState(new ObjectiveArray(other), false);

		// assert that stateA can not dominate stateB.
		assertFalse(stateA.dominates(stateB));
		// assert that stateB can not dominate stateA.
		assertFalse(stateB.dominates(stateA));
	}

	@Test
	public void testNotDominationOnSameValues() throws Exception {
		// create two minimization objectives with value.
		Objective objectiveA = ObjectiveUtil.createMinimizedObjective(2);
		Objective objectiveB = ObjectiveUtil.createMinimizedObjective(3);

		// create two new objective arrays with these objectives.
		Objective[] dominated = new Objective[] { objectiveA, objectiveB };
		Objective[] other = new Objective[] { objectiveA, objectiveB };

		// create two states with corresponding objective arrays.
		MOState stateA = new MOState(new ObjectiveArray(dominated), false);
		MOState stateB = new MOState(new ObjectiveArray(other), false);

		// assert that we can not determine domination.
		assertFalse(stateA.dominates(stateB));
		assertFalse(stateB.dominates(stateA));
	}

	@Test
	public void testNotDominationWhichHaveOneSameValue() throws Exception {
		// create five minimization objectives with value.
		Objective objectiveA = ObjectiveUtil.createMinimizedObjective(2);
		Objective objectiveB = ObjectiveUtil.createMinimizedObjective(3);
		Objective objectiveC = ObjectiveUtil.createMinimizedObjective(5);
		Objective objectiveD = ObjectiveUtil.createMinimizedObjective(6);
		Objective objectiveE = ObjectiveUtil.createMinimizedObjective(7);

		// create two new objective arrays with these objectives.
		Objective[] dominated = new Objective[] { objectiveA, objectiveB,
				objectiveE };
		Objective[] other = new Objective[] { objectiveA, objectiveC,
				objectiveD };

		// create two states with corresponding objective arrays.
		MOState stateA = new MOState(new ObjectiveArray(dominated), false);
		MOState stateB = new MOState(new ObjectiveArray(other), false);

		// assert that stateA can not dominate stateB.
		assertFalse(stateA.dominates(stateB));
		// assert that stateB can not dominate stateA.
		assertFalse(stateB.dominates(stateA));
	}

	@Test
	public void testNotDominationWhicHaveOneSameValueOnMixedBehaviours()
			throws Exception {
		// create five min/max objectives with value.
		Objective objectiveA = ObjectiveUtil.createMinimizedObjective(2);
		Objective objectiveB = ObjectiveUtil.createMinimizedObjective(3);
		Objective objectiveC = ObjectiveUtil.createMinimizedObjective(5);

		Objective objectiveD = ObjectiveUtil.createMaximizedObjective(6);
		Objective objectiveE = ObjectiveUtil.createMaximizedObjective(5);

		// create two new objective arrays with these objectives.
		Objective[] dominated = new Objective[] { objectiveA, objectiveB,
				objectiveE };
		Objective[] other = new Objective[] { objectiveA, objectiveC,
				objectiveD };

		// create two states with corresponding objective arrays.
		MOState stateA = new MOState(new ObjectiveArray(dominated), false);
		MOState stateB = new MOState(new ObjectiveArray(other), false);

		// assert that stateA can not dominate stateB.
		assertFalse(stateA.dominates(stateB));
		// assert that stateB can not dominate stateA.
		assertFalse(stateB.dominates(stateA));
	}

	@Test
	public void testNotDominationWhichHaveNSameValuesOnMixedBehaviours()
			throws Exception {
		// [2(min), 4(max), 6(min), 6(max)]
		// [2(min), 3(max), 5(min), 6(max)]
		// create several min/max objectives with value.
		Objective objectiveA = ObjectiveUtil.createMinimizedObjective(2);
		Objective objectiveB = ObjectiveUtil.createMinimizedObjective(5);
		Objective objectiveC = ObjectiveUtil.createMinimizedObjective(6);

		Objective objectiveD = ObjectiveUtil.createMaximizedObjective(3);
		Objective objectiveE = ObjectiveUtil.createMaximizedObjective(4);
		Objective objectiveF = ObjectiveUtil.createMaximizedObjective(6);
		// Objective objectiveG = ObjectiveUtil.createMaximizedObjective(7);

		// create two new objective arrays with these objectives. [2,4,6,6]
		Objective[] dominated = new Objective[] { objectiveA, objectiveE,
				objectiveC, objectiveF };
		// [2,3,5,6]
		Objective[] other = new Objective[] { objectiveA, objectiveD,
				objectiveB, objectiveF };

		// create two states with corresponding objective arrays.
		MOState stateA = new MOState(new ObjectiveArray(dominated), false);
		MOState stateB = new MOState(new ObjectiveArray(other), false);

		// assert that stateA can not dominate stateB.
		assertFalse(stateA.dominates(stateB));
		// assert that stateB can not dominate stateA.
		assertFalse(stateB.dominates(stateA));
	}

	@Test
	public void testNotDominationOnComplexMixedBehaviours() throws Exception {
		// [1(min), 2(min), 4(max), 5(min), 6(max)]
		// [2(min), 2(min), 3(max), 6(min), 7(max)]
		// create several min/max objectives with value.
		Objective objective0 = ObjectiveUtil.createMinimizedObjective(1);
		Objective objectiveA = ObjectiveUtil.createMinimizedObjective(2);
		Objective objectiveB = ObjectiveUtil.createMinimizedObjective(5);
		Objective objectiveC = ObjectiveUtil.createMinimizedObjective(6);

		Objective objectiveD = ObjectiveUtil.createMaximizedObjective(3);
		Objective objectiveE = ObjectiveUtil.createMaximizedObjective(4);
		Objective objectiveF = ObjectiveUtil.createMaximizedObjective(6);
		Objective objectiveG = ObjectiveUtil.createMaximizedObjective(7);

		// create two new objective arrays with these objectives. [2,4,5,6]
		Objective[] dominated = new Objective[] { objective0, objectiveA,
				objectiveE, objectiveB, objectiveF };
		// [2,3,6,7]
		Objective[] other = new Objective[] { objectiveA, objectiveA,
				objectiveD, objectiveC, objectiveG };

		// create two states with corresponding objective arrays.
		MOState stateA = new MOState(new ObjectiveArray(dominated), false);
		MOState stateB = new MOState(new ObjectiveArray(other), false);

		// assert that stateA can not dominate stateB.
		assertFalse(stateA.dominates(stateB));
		// assert that stateB can not dominate stateA.
		assertFalse(stateB.dominates(stateA));
	}
	
	@Test
	public void checkInconsistentBehaviours() throws Exception {
		// create four minimization objectives with value.
		Objective objectiveA = ObjectiveUtil.createMinimizedObjective(2);
		Objective objectiveB = ObjectiveUtil.createMaximizedObjective(3);
		Objective objectiveC = ObjectiveUtil.createMaximizedObjective(4);
		Objective objectiveD = ObjectiveUtil.createMaximizedObjective(5);

		// create two new objective arrays with these objectives.
		Objective[] dominated = new Objective[] { objectiveA, objectiveD };
		Objective[] other = new Objective[] { objectiveB, objectiveC };

		// create two states with corresponding objective arrays.
		MOState stateA = new MOState(new ObjectiveArray(dominated), false);
		MOState stateB = new MOState(new ObjectiveArray(other), false);

		exc.expect(IllegalStateException.class);
		// assert that stateA dominates stateB.
		assertTrue(stateA.dominates(stateB));
		// assert that stateB can not dominate stateA.
		assertFalse(stateB.dominates(stateA));
	}

	@Test
	public void checkIrrelevantObjectivesSize() throws Exception {
		// create four minimization objectives with value.
		Objective objectiveA = ObjectiveUtil.createMinimizedObjective(2);
		Objective objectiveB = ObjectiveUtil.createMaximizedObjective(3);
		Objective objectiveC = ObjectiveUtil.createMaximizedObjective(4);
		Objective objectiveD = ObjectiveUtil.createMaximizedObjective(5);

		// create two new objective arrays with these objectives.
		Objective[] dominated = new Objective[] { objectiveA };
		Objective[] other = new Objective[] { objectiveB, objectiveC,
				objectiveD };

		// create two states with corresponding objective arrays.
		MOState stateA = new MOState(new ObjectiveArray(dominated), false);
		MOState stateB = new MOState(new ObjectiveArray(other), false);

		exc.expect(IllegalStateException.class);
		// assert that stateA dominates stateB.
		assertTrue(stateA.dominates(stateB));
		// assert that stateB can not dominate stateA.
		assertFalse(stateB.dominates(stateA));
	}

}
