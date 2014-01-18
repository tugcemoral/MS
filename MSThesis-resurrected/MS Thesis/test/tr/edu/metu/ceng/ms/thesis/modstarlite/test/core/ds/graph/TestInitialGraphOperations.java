package tr.edu.metu.ceng.ms.thesis.modstarlite.test.core.ds.graph;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

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
 * Initial Tests tests atomic graph operations with single state.
 */
public class TestInitialGraphOperations {

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
	 * 
	 Assuming that graph is empty. (it's empty, though) So, adding a one new
	 * single key.
	 */
	@Test
	public void testInsertion() {
		// on big bang, graph is empty.
		Assert.assertThat(graph.size(), is(0));

		// insert an element...
		Key key = insertStateIntoGraph();

		// assert that graph size is 1 and peeked key is actual one.
		Assert.assertThat(graph.size(), is(1));
		Key topKey = graph.topKey();
		Assert.assertThat(key, equalTo(topKey));
		// assert that graph size is still 1.
		Assert.assertThat(graph.size(), is(1));
	}

	@Test
	public void testPeekList() {
		// insert an element into graph.
		insertStateIntoGraph();

		// create a new int coordinate and its key to check from graph.
		IntCoord state = ObjectiveUtil.createIntCoord(4, 4);
		// creates a key with k1 = (4, 5) and k2 = (4, 6)
		Key assumedTopKey = ObjectiveUtil.generateKey(state, 5, 6);

		// so, peek the first element as list.
		List<Key> topKeyList = graph.topKeys();

		Assert.assertThat(topKeyList.get(0), equalTo(assumedTopKey));
		Assert.assertThat(topKeyList.size(), is(1));
	}

	@Test
	public void testPop() {
		// insert an element into graph.
		insertStateIntoGraph();

		// create a new int coordinate and to check from graph.
		IntCoord expectedState = ObjectiveUtil.createIntCoord(4, 4);

		// pop first element of the graph.
		IntCoord actualState = graph.pop();

		// graph should be empty.
		Assert.assertThat(graph.size(), is(0));
		Assert.assertThat(graph.isEmpty(), is(true));

		// popped element must be same with expected one.
		Assert.assertThat(actualState, equalTo(expectedState));

		// re-pop and that element must be null.
		Assert.assertThat(graph.pop(), equalTo(null));
	}

	@Test
	public void testRemove() {
		// insert an element into graph.
		Key key = insertStateIntoGraph();

		// create a new int coordinate and to check from graph.
		IntCoord removalState = ObjectiveUtil.createIntCoord(4, 4);

		// remove it from graph.
		graph.remove(removalState, key);

		// graph should be empty.
		Assert.assertThat(graph.size(), is(0));
	}

	private Key insertStateIntoGraph() {
		// create a new int coordinate and its key to put into graph.
		IntCoord state = ObjectiveUtil.createIntCoord(4, 4);
		// creates a key with k1 = (4, 5) and k2 = (4, 6)
		Key key = ObjectiveUtil.generateKey(state, 5, 6);

		// insert key into graph.
		graph.insert(state, key);

		return key;
	}

}
