package tr.edu.metu.ceng.ms.thesis.modstarlite.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tr.edu.metu.ceng.ms.thesis.modstarlite.test.core.TestDominationOnListPerspective;
import tr.edu.metu.ceng.ms.thesis.modstarlite.test.core.TestMultiObjectiveDomination;
import tr.edu.metu.ceng.ms.thesis.modstarlite.test.core.TestMultiObjectiveNotDomination;
import tr.edu.metu.ceng.ms.thesis.modstarlite.test.core.ds.queue.AllCoreDSTests;

@RunWith(Suite.class)
@SuiteClasses({ TestMultiObjectiveDomination.class,
		TestMultiObjectiveNotDomination.class,
		TestDominationOnListPerspective.class, AllCoreDSTests.class })
public class AllTests {
}
