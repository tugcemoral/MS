package tr.edu.metu.ceng.ms.thesis.modstarlite.test.core.ds.queue;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.Key;
import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.queue.MODStarQueue;
import tr.edu.metu.ceng.ms.thesis.modstarlite.test.util.ObjectiveUtil;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;

/**
 * Initial Tests tests atomic queue operations with single state.
 */
public class TestInitialQueueOperations {

	private static MODStarQueue<IntCoord> queue;
	
	@BeforeClass
	public static void initialSetup() {
		// create a mod star queue.
		queue = new MODStarQueue<IntCoord>();
	}
	
	@After
	public void tearDown() {
		queue.clear();
	}

	/**
	 * 
	 Assuming that queue is empty. (it's empty, though) So, adding a one new
	 * single key.
	 */
	@Test
	public void testInsertion() {
		// on big bang, queue is empty.
		Assert.assertThat(queue.size(), is(0));

		// insert an element...
		Key key = insertStateIntoQueue();

		// assert that queue size is 1 and peeked key is actual one.
		Assert.assertThat(queue.size(), is(1));
		Key topKey = queue.topKey();
		Assert.assertThat(key, equalTo(topKey));
		//assert that queue size is still 1.
		Assert.assertThat(queue.size(), is(1));
	}
	
	@Test
	public void testPeekList() {
		// insert an element into queue.
		insertStateIntoQueue();
		
		// create a new int coordinate and its key to check from queue.
		IntCoord state = ObjectiveUtil.createIntCoord(4, 4);
		//creates a key with k1 = (4, 5) and  k2 = (4, 6)
		Key assumedTopKey = ObjectiveUtil.generateKey(state, 5, 6);
		
		//so, peek the first element as list.
		List<Key> topKeyList = queue.topKeys();
		
		Assert.assertThat(topKeyList.get(0), equalTo(assumedTopKey));
		Assert.assertThat(topKeyList.size(), is(1));
	}

	@Test
	public void testPop() {
		// insert an element into queue.
		insertStateIntoQueue();
		
		// create a new int coordinate and to check from queue.
		IntCoord expectedState = ObjectiveUtil.createIntCoord(4, 4);
		
		//pop first element of the queue.
		IntCoord actualState = queue.pop();

		//queue should be empty.
		Assert.assertThat(queue.size(), is(0));
		Assert.assertThat(queue.isEmpty(), is(true));
		
		//popped element must be same with expected one.
		Assert.assertThat(actualState, equalTo(expectedState));
		
		//re-pop and that element must be null.
		Assert.assertThat(queue.pop(), equalTo(null));
	}
	
	@Test
	public void testRemove() {
		// insert an element into queue.
		insertStateIntoQueue();

		// create a new int coordinate and to check from queue.
		IntCoord removalState = ObjectiveUtil.createIntCoord(4, 4);

		//remove it from queue.
		queue.remove(removalState);
		
		//queue should be empty.
		Assert.assertThat(queue.size(), is(0));
	}
	
	private Key insertStateIntoQueue() {
		// create a new int coordinate and its key to put into queue.
		IntCoord state = ObjectiveUtil.createIntCoord(4, 4);
		//creates a key with k1 = (4, 5) and  k2 = (4, 6)
		Key key = ObjectiveUtil.generateKey(state, 5, 6);

		// insert key into queue.
		queue.insert(state, key);
		
		return key;
	}

}
