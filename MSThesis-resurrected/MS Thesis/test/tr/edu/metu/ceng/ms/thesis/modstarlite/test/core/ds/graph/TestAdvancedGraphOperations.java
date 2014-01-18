package tr.edu.metu.ceng.ms.thesis.modstarlite.test.core.ds.graph;

import static org.hamcrest.core.Is.is;

import java.util.List;
import java.util.Vector;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.Key;
import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.StateKey;
import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.graph.MODStarDAG;
import tr.edu.metu.ceng.ms.thesis.modstarlite.test.util.ObjectiveUtil;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;

/**
 * Advanced tests on inserting, reinserting, removing states and state lists to
 * the {@link MODStargraph}.
 */
public class TestAdvancedGraphOperations {

	private static MODStarDAG<IntCoord> graph;

	@BeforeClass
	public static void initialSetup() {
		// create a mod star dag.
		graph = new MODStarDAG<IntCoord>();
	}

	@After
	public void tearDown() {
		graph.clear();
	}

	/**
	 * This test adds Scenario 1 tests with {@link #insertScenario1States()}
	 * method and checks for top keys.
	 */
	@Test
	public void testScenario1Case1() {
		List<StateKey<IntCoord>> insertedStates = insertScenario1States();

		Assert.assertThat(graph.size(), is(5));

		List<StateKey<IntCoord>> topStateKeys = graph.topStateKeys();

		// assert that top keys list have 2 elements.
		Assert.assertThat(topStateKeys.size(), is(2));
		// assert that top keys list should contain only S3 and S5
		Assert.assertTrue(topStateKeys.contains(insertedStates.get(2)));
		Assert.assertTrue(topStateKeys.contains(insertedStates.get(4)));
	}

	/**
	 * This test adds Scenario 1 tests with {@link #insertScenario1States()}
	 * method and remove S1 from graph, check for modifications.
	 */
	@Test
	public void testScenario1Case2() {
		List<StateKey<IntCoord>> insertedStates = insertScenario1States();

		StateKey<IntCoord> S1 = insertedStates.get(0);
		// remove S1 from graph.
		graph.remove(S1);

		Assert.assertThat(graph.size(), is(4));
		Assert.assertFalse(graph.contains(S1));

		List<StateKey<IntCoord>> topStateKeys = graph.topStateKeys();

		// assert that top keys list have 2 elements and contain only S3 and S5
		Assert.assertThat(topStateKeys.size(), is(2));
		Assert.assertTrue(topStateKeys.contains(insertedStates.get(2)));
		Assert.assertTrue(topStateKeys.contains(insertedStates.get(4)));

	}

	/**
	 * This test adds Scenario 1 tests with {@link #insertScenario1States()}
	 * method and remove S1 and S3 from graph sequentially, check for
	 * modifications.
	 */
	@Test
	public void testScenario1Case3() {
		List<StateKey<IntCoord>> insertedStates = insertScenario1States();

		StateKey<IntCoord> S1 = insertedStates.get(0);
		StateKey<IntCoord> S3 = insertedStates.get(2);
		// remove S1 and S3 from graph.
		graph.remove(S1);
		graph.remove(S3);

		Assert.assertThat(graph.size(), is(3));
		Assert.assertFalse(graph.contains(S1));
		Assert.assertFalse(graph.contains(S3));

		List<StateKey<IntCoord>> topStateKeys = graph.topStateKeys();

		// assert that top keys list have 2 elements and contain only S2 and S5
		Assert.assertThat(topStateKeys.size(), is(2));
		Assert.assertTrue(topStateKeys.contains(insertedStates.get(1)));
		Assert.assertTrue(topStateKeys.contains(insertedStates.get(4)));

	}
	
	@Test
	public void testScenario1Case4(){
		List<StateKey<IntCoord>> insertedStates = insertScenario1States();

		graph.pop();
		graph.pop();
		graph.pop();
		graph.pop();
		
	}

	/**
	 * First scenerio inserts four states (S1, S2, S3 and S4) to graph. The
	 * domination definition between these states are as follows:
	 * 
	 * <li>S1 dominates S4</li> <li>S2 dominates S4</li> <li>S3 dominates S4</li>
	 * <li>S5 dominates S4</li>
	 * <p>
	 * <li>S3 dominates S1</li>
	 * <p>
	 * <li>S1 dominates S2</li>
	 */
	private List<StateKey<IntCoord>> insertScenario1States() {

		StateKey<IntCoord> s1 = insertStateIntoGraph(1, 1, 4, 8, 0, 0);// S1
		StateKey<IntCoord> s2 = insertStateIntoGraph(2, 2, 5, 8, 0, 0);// S2
		StateKey<IntCoord> s3 = insertStateIntoGraph(3, 3, 3, 8, 0, 0);// S3
		StateKey<IntCoord> s4 = insertStateIntoGraph(4, 4, 9, 9, 0, 0);// S4
		StateKey<IntCoord> s5 = insertStateIntoGraph(5, 5, 8, 3, 0, 0);// S5

		// create a new return list.
		List<StateKey<IntCoord>> insertedStateKeys = new Vector<StateKey<IntCoord>>();

		insertedStateKeys.add(s1);
		insertedStateKeys.add(s2);
		insertedStateKeys.add(s3);
		insertedStateKeys.add(s4);
		insertedStateKeys.add(s5);

		return insertedStateKeys;
	}

	private StateKey<IntCoord> insertStateIntoGraph(int x, int y, int k11,
			int k12, int k21, int k22) {
		// create a new int coordinate and its key to put into graph.
		IntCoord state = ObjectiveUtil.createIntCoord(x, y);
		// creates a key with k1 = (k11, k12) and k2 = (k21, k22)
		Key key = ObjectiveUtil.generateKey(k11, k12, k21, k22);

		// create a new state-key object to insert into graph.
		StateKey<IntCoord> stateKey = new StateKey<IntCoord>(state, key);

		// insert key into graph.
		graph.insert(stateKey);

		return stateKey;
	}

	private Key insertStateIntoGraph(int x, int y, int k1Value, int k2Value) {
		// create a new int coordinate and its key to put into graph.
		IntCoord state = ObjectiveUtil.createIntCoord(x, y);
		// creates a key with k1 = (x, k1Value) and k2 = (y, k2Value)
		Key key = ObjectiveUtil.generateKey(state, k1Value, k2Value);

		// insert key into graph.
		graph.insert(state, key);

		return key;
	}

}
