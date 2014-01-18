package tr.edu.metu.ceng.ms.thesis.modstarlite.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;

public class MapUtils<K extends Coordinate, V> {

	public SortedMap<K, V> sort(HashMap<K, V> map) {
		// create a new sorted visualized map.
		SortedMap<K, V> sortedMap = new SortedVisualizeMap(
				new BasicKeyComparator());

		System.out.println(map.keySet().size());
		for (K key : map.keySet()) {
			sortedMap.put(key, map.get(key));
		}
		System.out.println(sortedMap.keySet().size());
		return sortedMap;
	}

	private class SortedVisualizeMap extends TreeMap<K, V> {

		private static final long serialVersionUID = -1577510452310614824L;

		public SortedVisualizeMap(Comparator<K> comparator) {
			super(comparator);
		}

		@Override
		public String toString() {
			String message = "[";
			int i = 0;
			for (K state : keySet()) {
				if (i == keySet().size() - 1) {
					message += state + " = " + get(state) + "]";
				} else {
					message += state + " = " + get(state) + "\n";
				}
				i++;
			}
			return message;
		}
	}

	private final class BasicKeyComparator implements Comparator<K> {

		@Override
		public int compare(Coordinate c1, Coordinate c2) {
			//XXX: it is assumed that coordinates have two dimensions
			if(c1.get(1) < c2.get(1)){
				return -1;
			}else if(c1.get(1) > c2.get(1)) {
				return 1;
			}else {
				if(c1.get(0) < c2.get(0)) {
					return -1;
				}else if(c1.get(0) > c2.get(0)){
					return 1;
				}else {
					return 0;
				}
			}
		}
	}
}
