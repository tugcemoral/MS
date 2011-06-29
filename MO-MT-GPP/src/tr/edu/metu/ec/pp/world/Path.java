package tr.edu.metu.ec.pp.world;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Path represents a valid way from start cell to goal cell. It has a linked
 * list of cells on it.
 * 
 * @author tugcem
 * 
 */
public class Path {

	private LinkedList<Cell> cells;

	public LinkedList<Cell> getCells() {
		if (cells == null) {
			cells = new LinkedList<Cell>();
		}
		return cells;
	}

	public void setCells(LinkedList<Cell> cells) {
		this.cells = cells;
	}

	/**
	 * Adds a new cell to path' s {@link #cells} linked list.
	 * 
	 * @param cell
	 *            the new cell to add.
	 */
	public void addCell(Cell cell) {
		this.getCells().addLast(cell);
	}

	public void addCells(LinkedList<Cell> newCells) {
		// add given new cells to path's linked list.
		for (Cell cell : newCells) {
			addCell(cell);
		}
	}

	@Override
	public String toString() {
		String toString = "";
		for (Cell cell : getCells()) {
			toString += "[" + cell.getLocation().getX() + " "
					+ cell.getLocation().getY() + "]";
		}
		return toString;
	}

	public void initialize() {
		this.getCells().clear();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cells == null) ? 0 : cells.hashCode());
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
		Path other = (Path) obj;
		if (cells == null) {
			if (other.cells != null)
				return false;
		} else if (!cells.equals(other.cells))
			return false;
		return true;
	}

	public boolean intersects(Path path) {
		boolean intersects = false;
		// traverse both paths and try to find a mutual cell.
		for (int i = 0; i < this.getCells().size(); i++) {
			for (int j = 0; j < path.getCells().size(); j++) {
				// note that intersected cells are not initial or final cells.
				if ((!(this.getCells().get(i)
						.equals(this.getCells().getFirst())) || !(this
						.getCells().get(i).equals(this.getCells().getLast())))
						&& (!(path.getCells().get(j).equals(path.getCells()
								.getFirst())) || !(path.getCells().get(j)
								.equals(path.getCells().getLast())))) {
					// if one of the cells is equal with given path' s one cell,
					// that means these paths are intersected.
					if (this.getCells().get(i).equals(path.getCells().get(j))) {
						intersects = true;
						break;
					}
				}
			}
			if (intersects)
				break;
		}
		return intersects;
	}

	public Cell getAnyIntersectionCellWith(Path path) {
		// first, find intersected cells list.
		List<Cell> intersectedCells = findIntersectedCells(path);
		// return a randomly selected intersection cell.
		return intersectedCells.get((new Random()).nextInt(intersectedCells
				.size()));
	}

	private List<Cell> findIntersectedCells(Path path) {
		// create a list to hold intersected cells.
		List<Cell> intersectedCells = new Vector<Cell>();

		// traverse both paths and try to find mutual cells.
		for (int i = 0; i < this.getCells().size(); i++) {
			for (int j = 0; j < path.getCells().size(); j++) {
				// note that intersected cells are not initial or final cells.
				boolean isCellFirst = this.getCells().get(i)
						.equals(this.getCells().getFirst())
						|| path.getCells().get(j)
								.equals(path.getCells().getFirst());

				boolean isCellLast = this.getCells().get(i)
						.equals(this.getCells().getLast())
						|| path.getCells().get(j)
								.equals(path.getCells().getLast());

				if (!isCellFirst && !isCellLast) {
					if (this.getCells().get(i).equals(path.getCells().get(j))) {
						intersectedCells.add(this.getCells().get(i));
					}
				}
			}
		}

		return intersectedCells;
	}

	public int size() {
		return this.getCells().size();
	}
}
