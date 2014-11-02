package tr.edu.metu.ceng.ms.thesis.spea2.integration.core;

import java.util.Comparator;

public class SPEA2ShortestPathComparator implements Comparator<SPEA2Solution> {

	@Override
	public int compare(SPEA2Solution s1, SPEA2Solution s2) {
		if(s1.getObjective(0) < s2.getObjective(0)) {
			return -1;
		}else if(s1.getObjective(0) > s2.getObjective(0)) {
			return 1;
		}else {
			return 0;
		}
	}

}
