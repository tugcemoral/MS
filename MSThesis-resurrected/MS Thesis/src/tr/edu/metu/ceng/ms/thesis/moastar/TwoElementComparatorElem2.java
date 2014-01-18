package tr.edu.metu.ceng.ms.thesis.moastar;

import java.util.Comparator;

public class TwoElementComparatorElem2 implements Comparator<TwoElement> {

	public TwoElementComparatorElem2() {
	}

	public int compare(TwoElement cell1, TwoElement cell2) {
		if (cell1.value2 < cell2.value2)
			return -1;
		else if (cell1.value2 > cell2.value2)
			return 1;
		else
			return 0;

	}

}