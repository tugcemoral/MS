package tr.edu.metu.ceng.ms.thesis.modstarlite.test.core.ds.queue;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.Key;
import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.queue.MODStarQueue;
import tr.edu.metu.ceng.ms.thesis.modstarlite.test.util.ObjectiveUtil;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;

/**
 * Advanced tests on inserting, reinserting, removing states and state lists to
 * the {@link MODStarQueue}.
 */
public class TestAdvancedQueueOperations {
	
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
	
	@Test
	public void testScenerio1() {
		playScenario1();
	}


	/**
	 * First scenerio inserts four states (S1, S2, S3 and S4) to queue. The domination definition between these states are as follows:
	 * 
	 * <li>S1 dominates S4</li>
	 * <li>S2 dominates S4</li>
	 * <li>S3 dominates S4</li>
	 * <li>S1, S2 an dominates S4</li>
	 */
	private void playScenario1() {
		
	}
	
	
	private Key insertStateIntoQueue(int x, int y, int k11, int k12, int k21, int k22) {
		// create a new int coordinate and its key to put into queue.
		IntCoord state = ObjectiveUtil.createIntCoord(x, y);
		// creates a key with k1 = (k11, k12) and k2 = (k21, k22)
		Key key = ObjectiveUtil.generateKey(k11, k12, k21, k22);
		
		// insert key into queue.
		queue.insert(state, key);
		
		return key;
	}

	private Key insertStateIntoQueue(int x, int y, int k1Value,
			int k2Value) {
		// create a new int coordinate and its key to put into queue.
		IntCoord state = ObjectiveUtil.createIntCoord(x, y);
		// creates a key with k1 = (x, k1Value) and k2 = (y, k2Value)
		Key key = ObjectiveUtil.generateKey(state, k1Value, k2Value);

		// insert key into queue.
		queue.insert(state, key);

		return key;
	}


}
