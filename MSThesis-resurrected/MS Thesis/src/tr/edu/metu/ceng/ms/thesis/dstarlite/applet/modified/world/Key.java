package tr.edu.metu.ceng.ms.thesis.dstarlite.applet.modified.world;

import java.util.Comparator;

public class Key {
	private int a, b;
	public Cell cell;

	public Key(Cell cell) {
		a = Math.min(cell.g, cell.rhs);
		b = a;
		a = a != Integer.MAX_VALUE ? a + cell.h : a;

		this.cell = cell;
	}

	public String toString() {
		return new String("[" + a + "," + b + "] => " + cell);
	}

	public static class KeyComparator implements Comparator<Key> {
		public int compare(Key k1, Key k2) {
			if (k1.a == k2.a) {
				if (k1.b == k2.b) {
					if (k1.cell.x == k2.cell.x) {
						if (k1.cell.y == k2.cell.y)
							return 0;
						else
							return k1.cell.y - k2.cell.y;
					} else
						return k1.cell.x - k2.cell.x;
				} else
					return k1.b - k2.b;
			} else
				return k1.a - k2.a;
		}

		public boolean equals(Object obj) {
			return false;
		}
	}
}