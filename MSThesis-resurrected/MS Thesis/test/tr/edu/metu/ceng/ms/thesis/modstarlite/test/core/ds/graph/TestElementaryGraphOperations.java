package tr.edu.metu.ceng.ms.thesis.modstarlite.test.core.ds.graph;

import static org.hamcrest.core.Is.is;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.Key;
import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.graph.MODStarDAG;
import tr.edu.metu.ceng.ms.thesis.modstarlite.test.util.ObjectiveUtil;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;

/**
 * Elementary tests include only cases that each element on graph should be a
 * single state, not a list.
 */
public class TestElementaryGraphOperations {

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

	@Test
	public void testSameStateInsertion() {
		// sequentially insert same states.
		insertStateIntoGraph(4, 4, 5, 6);
		Key key = insertStateIntoGraph(4, 4, 5, 6);

		// assert that only one state is added to graph.
		Assert.assertThat(graph.size(), is(1));

		List<Key> topKeys = graph.topKeys();
		// and topKeys of graph has 1 element.
		Assert.assertThat(topKeys.size(), is(1));
		Assert.assertThat(topKeys.contains(key), is(true));
	}

	/**
	 * This test checks addition two states S1 and S2. S2 is added first and
	 * than S1 is added. S1 dominates on S2.
	 */
	@Test
	public void testNotDominatibleStateInsertion() {
		// first insert S2 and then S1 state.
		Key s2Key = insertStateIntoGraph(4, 4, 5, 6);
		Key s1Key = insertStateIntoGraph(3, 4, 2, 6);

		// assert that boths states should be added to graph.
		Assert.assertThat(graph.size(), is(2));

		List<Key> topKeys = graph.topKeys();
		// and topKeys of graph has 1 element.
		Assert.assertThat(topKeys.size(), is(1));
		Assert.assertThat(topKeys.contains(s1Key), is(true));

		// remove s1 from graph.
		graph.pop();

		List<Key> newTopKeys = graph.topKeys();
		// and topKeys of graph has 1 element.
		Assert.assertThat(newTopKeys.size(), is(1));
		Assert.assertThat(newTopKeys.contains(s2Key), is(true));

	}

	/**
	 * This test checks addition two states S1 and S2. S2 is added first and
	 * than S1 is added. S2 dominates on S1.
	 */
	@Test
	public void testDominatibleStateInsertion() {

		// first insert S2 and then S1 state.
		Key s2Key = insertStateIntoGraph(3, 4, 2, 6);
		Key s1Key = insertStateIntoGraph(4, 4, 5, 6);

		// assert that boths states should be added to graph.
		Assert.assertThat(graph.size(), is(2));

		List<Key> topKeys = graph.topKeys();
		// and topKeys of graph has 1 element.
		Assert.assertThat(topKeys.size(), is(1));
		Assert.assertThat(topKeys.contains(s2Key), is(true));

		// remove s2 from graph.
		graph.pop();

		List<Key> newTopKeys = graph.topKeys();
		// and topKeys of graph has 1 element.
		Assert.assertThat(newTopKeys.size(), is(1));
		Assert.assertThat(newTopKeys.contains(s1Key), is(true));

	}

	/**
	 * This test checks addition two states S1 and S2. S2 is added first and
	 * than S1 is added. S1 and S2 are non-dominatibles.
	 */
	@Test
	public void testNonDominatibleStateInsert() {

		// first insert S2 and then S1 state.
		Key s2Key = insertStateIntoGraph(3, 4, 8, 6);
		Key s1Key = insertStateIntoGraph(4, 4, 5, 6);

		// assert that boths states should be added to graph.
		Assert.assertThat(graph.size(), is(2));

		List<Key> topKeys = graph.topKeys();
		// and topKeys of graph has 1 element.
		Assert.assertThat(topKeys.size(), is(2));
		Assert.assertThat(topKeys.contains(s1Key), is(true));
		Assert.assertThat(topKeys.contains(s2Key), is(true));

		// remove s2 from graph.
		graph.pop();

		List<Key> newTopKeys = graph.topKeys();
		// and topKeys of graph has 1 element.
		Assert.assertThat(newTopKeys.size(), is(1));

	}

	/**
	 * Inserts S1 = [(12,91), (4,91)] and S2 = [(14,91), (4,91)] and expecting
	 * that graph should have <li># S1 # <li># S2 #
	 */
	@Test
	public void testScenario1() {

		// first insert S1 and then S2 state.
		Key s1Key = insertStateIntograph(3, 4, 12, 91, 4, 91);
		Key s2Key = insertStateIntograph(4, 4, 14, 91, 4, 91);

		// assert that boths states should be added to graph.
		Assert.assertThat(graph.size(), is(2));

		List<Key> topKeys = graph.topKeys();
		// and topKeys of graph has 1 element.
		Assert.assertThat(topKeys.size(), is(1));
		Assert.assertThat(topKeys.contains(s1Key), is(true));

		// remove s1 from graph.
		graph.pop();

		List<Key> newTopKeys = graph.topKeys();
		// and topKeys of graph has 1 element.
		Assert.assertThat(newTopKeys.size(), is(1));
		Assert.assertThat(newTopKeys.contains(s2Key), is(true));

	}

	/**
	 * This test checks addition two states S1 and S2. S2 is added first and
	 * than S1 is added. S1 and S2 keys are equal.
	 */
	@Test
	public void testEqualStateKeyInsert() {

		// first insert S2 and then S1 state.
		Key s2Key = insertStateIntograph(3, 4, 12, 5, 8, 6);
		Key s1Key = insertStateIntograph(4, 4, 12, 5, 8, 6);

		// assert that boths states should be added to graph.
		Assert.assertThat(graph.size(), is(2));

		List<Key> topKeys = graph.topKeys();
		// and topKeys of graph has 1 element.
		Assert.assertThat(topKeys.size(), is(2));
		Assert.assertThat(topKeys.contains(s1Key), is(true));
		Assert.assertThat(topKeys.contains(s2Key), is(true));

		// remove s2 from graph.
		graph.pop();

		List<Key> newTopKeys = graph.topKeys();
		// and topKeys of graph has 1 element.
		Assert.assertThat(newTopKeys.size(), is(1));
		Assert.assertThat(newTopKeys.contains(s1Key), is(true));
	}

	private Key insertStateIntograph(int x, int y, int k11, int k12, int k21,
			int k22) {
		// create a new int coordinate and its key to put into graph.
		IntCoord state = ObjectiveUtil.createIntCoord(x, y);
		// creates a key with k1 = (k11, k12) and k2 = (k21, k22)
		Key key = ObjectiveUtil.generateKey(k11, k12, k21, k22);

		// insert key into graph.
		graph.insert(state, key);

		return key;
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
