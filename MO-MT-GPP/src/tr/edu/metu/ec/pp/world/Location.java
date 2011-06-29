package tr.edu.metu.ec.pp.world;

/**
 * Axis locations for 2D virtual environment. Note that {@link #x} and
 * {@link #y} are considered as coordinates
 * 
 * @author tugcem
 * 
 */
public class Location {

	/**
	 * 2D environment "X axis" location
	 */
	private int x;

	/**
	 * 2D environment "Y axis" location
	 */
	private int y;

	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

}
