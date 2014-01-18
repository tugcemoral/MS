//package tr.edu.metu.ceng.ms.thesis.mogpp.core.env;
//
//import java.text.DecimalFormat;
//import java.util.LinkedList;
//
//import tr.edu.metu.ceng.ms.thesis.mogpp.core.ga.Individual;
//
///**
// * The 2D maze environment, has nxn {@link Cell cells}
// * 
// * @author tugcem
// * 
// */
//public class Grid {
//
//	/**
//	 * The singleton instance of grid.
//	 */
//	private static Grid instance;
//
//	/**
//	 * Creates and returns an instance, if {@link #instance} has not
//	 * initialized. Otherwise returns created instance.
//	 * 
//	 * @return {@link #instance the instance}
//	 */
//	public static Grid getInstance() {
//		if (instance == null) {
//			instance = new Grid();
//		}
//		return instance;
//	}
//
//	/**
//	 * The private constructor to provide singletonity.
//	 */
//	private Grid() {
//	}
//
//	private int n;
//
//	/**
//	 * The nxn cells of the 2D grid world
//	 */
//	private Cell cells[][];
//
//	public int getN() {
//		return n;
//	}
//
//	public void setN(int n) {
//		this.n = n;
//	}
//
//	public Cell[][] getCells() {
//		if (cells == null) {
//			cells = new Cell[getN()][getN()];
//		}
//		return cells;
//	}
//
//	public void setCells(Cell[][] cells) {
//		this.cells = cells;
//	}
//
//	/**
//	 * Adds the cell to grid's {@link #cells} 2D array. If a cell exists on that
//	 * location, it is replaced by new one.
//	 * 
//	 * @param cell
//	 *            {@link Cell} to add.
//	 */
//	public void addCell(Cell cell) {
//		this.getCells()[cell.getLocation().getX() - 1][cell.getLocation()
//				.getY() - 1] = cell;
//	}
//
//	/**
//	 * Gets the cell from {@link #cells cells list} according to given
//	 * coordinates and returns it.
//	 * 
//	 * @param x
//	 *            the x location of the cell
//	 * @param y
//	 *            the y location of the cell
//	 * @return corresponding {@link Cell} instance.
//	 */
//	public Cell getCell(int x, int y) {
//		try {
//			return this.getCells()[x - 1][y - 1];
//		} catch (Exception e) {
//			return null;
//		}
//	}
//
//	public Cell getCell(Location location) {
//		return this.getCell(location.getX(), location.getY());
//	}
//
//	/**
//	 * Prints out the current state of the grid.
//	 */
//	public void print() {
//		printLine(3 * getN() + 2);
//		for (int i = getN(); i >= 1; i--) {
//			System.out.print("|");
//			for (int j = 1; j <= getN(); j++) {
//				System.out.print(getCell(j, i).getContent());
//			}
//			System.out.println("|");
//		}
//		printLine(3 * getN() + 2);
//	}
//
//	public void printWeights(Individual individual) {
//		// create a double formatter with three precision
//		DecimalFormat formatter = new DecimalFormat("0.00");
//
//		printLine(9 * getN() + 2);
//		for (int i = getN(); i >= 1; i--) {
//			System.out.print("|");
//			for (int j = 1; j <= getN(); j++) {
//				Cell cell = getCell(j, i);
//				if (cell.hasObstacle()) {
//					System.out.print("  " + formatter.format(1) + "  ");
//				} else {
//					boolean isCellOnPath = false;
//					for (Cell pathCell : individual.getPath().getCells()) {
//						if (cell.equals(pathCell)) {
//							System.out
//									.print(" ["
//											+ formatter.format(cell.getWeight())
//											+ "] ");
//							isCellOnPath = true;
//							break;
//						}
//					}
//					if (!isCellOnPath) {
//						System.out.print("  "
//								+ formatter.format(cell.getWeight()) + "  ");
//					}
//				}
//
//			}
//			System.out.println("|");
//		}
//		printLine(9 * getN() + 2);
//
//	}
//
//	private void printLine(int length) {
//		for (int i = 0; i < length; i++) {
//			System.out.print("-");
//		}
//		System.out.println();
//	}
//
//	public void print(Individual individual) {
//		// get individual's solution.
//		LinkedList<Cell> path = individual.getPath().getCells();
//
//		printLine(3 * getN() + 2);
//		for (int i = getN(); i >= 1; i--) {
//			System.out.print("|");
//			for (int j = 1; j <= getN(); j++) {
//				System.out.print(getCell(j, i).getContent(path));
//			}
//			System.out.println("|");
//		}
//		printLine(3 * getN() + 2);
//	}
//
//}
