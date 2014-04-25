package tr.edu.metu.ceng.ms.thesis.moastar;

import java.util.Comparator;

public class CellOrderComparator implements Comparator<Cell> {

	@Override
	public int compare(Cell c1, Cell c2) {
		if(c1.numberInShortestPath < c2.numberInShortestPath) {
			return -1;
		}else {
			return 1;
		}
	}

}
