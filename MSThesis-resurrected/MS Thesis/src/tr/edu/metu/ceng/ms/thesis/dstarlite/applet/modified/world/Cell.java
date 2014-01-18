package tr.edu.metu.ceng.ms.thesis.dstarlite.applet.modified.world;

public class Cell {
	public int g, rhs, h;
	public Cell parent;
	public int x, y;
	public byte real_type, type_robot_vision;
	public int iteration; /* Iteration of last change. */
	public boolean used;

	public final static byte FREE = 0;
	public final static byte WALL = 1;
	public final static byte PATH = 2;

	public Cell(int x, int y) {
		parent = null;
		this.x = x;
		this.y = y;
		this.h = 0;
		used = false;
		type_robot_vision = real_type = FREE;
		g = rhs = Integer.MAX_VALUE;
		iteration = 0;
	}

	public String toString() {
		return new String(Integer.toString(this.x + 1)
				+ Character.toString((char) (this.y + 'A')));
	}

}
