package tr.edu.metu.ceng.ms.thesis.modstarlite.data;

import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;

public class Obstacle {

	private IntCoord coords;

	public Obstacle(int... coords) {
		this.coords = new IntCoord(coords);
	}

	public IntCoord getCoords() {
		return coords;
	}

	public void setCoords(IntCoord coords) {
		this.coords = coords;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coords == null) ? 0 : coords.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Obstacle other = (Obstacle) obj;
		if (coords == null) {
			if (other.coords != null)
				return false;
		} else if (!coords.equals(other.coords))
			return false;
		return true;
	}

}
