package tr.edu.metu.ceng.ms.thesis.moastar;

import java.util.Comparator;

public class TwoElementComparatorElem1 implements Comparator<TwoElement> {

	public TwoElementComparatorElem1() {
	}

	public int compare(TwoElement cell1, TwoElement cell2) {
		if (cell1.value1 < cell2.value1)
			return -1;
		else if (cell1.value1 > cell2.value1)
			return 1;
		else
			return 0;
	}
}