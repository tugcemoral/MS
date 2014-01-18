package tr.edu.metu.ceng.ms.thesis.modstarlite.test.core.ds.queue;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ TestInitialQueueOperations.class,
		TestElementaryQueueOperations.class, TestAdvancedQueueOperations.class})
public class AllCoreDSTests {

}
