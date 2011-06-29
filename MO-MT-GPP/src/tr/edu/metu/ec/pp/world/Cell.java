package tr.edu.metu.ec.pp.world;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Cell represents each cell in {@link Grid}
 * 
 * @author tugcem
 * 
 */
public class Cell {

	private int ID;

	/**
	 * Indicates whether this cell has an agent or not.
	 */
	private boolean hasAgent;

	/**
	 * Indicates whether this cell has an obstacle or not.
	 */
	private boolean hasObstacle;

	/**
	 * Weight of this cell
	 */
	private double weight;

	/**
	 * Consider 2D environment, x and y locations of cell in grid.
	 */
	private Location location;

	public Cell(int x, int y) {
		// create a new location with x and y.
		this.location = new Location(x, y);
	}

	public Cell(Location location) {
		this.location = location;
	}

	private void addAvailableCell(List<Cell> availableCells, int x, int y) {
		Cell cell = Grid.getInstance().getCell(x, y);
		if (cell != null && !cell.hasObstacle())
			availableCells.add(cell);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		if (ID != other.ID)
			return false;
		if (hasAgent != other.hasAgent)
			return false;
		if (hasObstacle != other.hasObstacle)
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (Double.doubleToLongBits(weight) != Double
				.doubleToLongBits(other.weight))
			return false;
		return true;
	}

	/**
	 * There could at most 8 possible cell (neighbors, cross-sides are
	 * included). for a cell.
	 * 
	 * @param cell
	 * @return
	 */
	public List<Cell> getAvailableCells(Cell cell) {
		// create a new available cells list.
		List<Cell> availableCells = new Vector<Cell>();

		// first, take a look to main-neighbors
		addAvailableCell(availableCells, cell.getLocation().getX(), cell
				.getLocation().getY() + 1);
		addAvailableCell(availableCells, cell.getLocation().getX(), cell
				.getLocation().getY() - 1);
		addAvailableCell(availableCells, cell.getLocation().getX() + 1, cell
				.getLocation().getY());
		addAvailableCell(availableCells, cell.getLocation().getX() - 1, cell
				.getLocation().getY());

		// then check cross-neighbors.
		addAvailableCell(availableCells, cell.getLocation().getX() + 1, cell
				.getLocation().getY() + 1);
		addAvailableCell(availableCells, cell.getLocation().getX() + 1, cell
				.getLocation().getY() - 1);
		addAvailableCell(availableCells, cell.getLocation().getX() - 1, cell
				.getLocation().getY() - 1);
		addAvailableCell(availableCells, cell.getLocation().getX() - 1, cell
				.getLocation().getY() + 1);

		return availableCells;
	}

	public String getContent() {
		return (this.hasAgent() ? " A " : (this.hasObstacle() ? " X " : " . "));
	}

	public String getContent(LinkedList<Cell> path) {

		for (int i = 0; i < path.size(); i++) {
			if (this.equals(path.get(i))) {
				if (i < 10)
					return " " + i + " ";
				else if (i >= 10 && i < 100)
					return " " + i;
				else if (i >= 100)
					return String.valueOf(i);
			}
		}
		return (this.hasObstacle() ? " X " : " . ");
	}

	public int getID() {
		return ID;
	}

	public Location getLocation() {
		return location;
	}

	public double getWeight() {
		return weight;
	}

	public boolean hasAgent() {
		return hasAgent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		result = prime * result + (hasAgent ? 1231 : 1237);
		result = prime * result + (hasObstacle ? 1231 : 1237);
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		long temp;
		temp = Double.doubleToLongBits(weight);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	public boolean hasObstacle() {
		return hasObstacle;
	}

	public void setHasAgent(boolean hasAgent) {
		this.hasAgent = hasAgent;
	}

	public void setHasObstacle(boolean isObstacle) {
		this.hasObstacle = isObstacle;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

}
