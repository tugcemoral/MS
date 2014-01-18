package tr.edu.metu.ceng.ms.thesis.moastar;

import java.util.Vector;

public class Cell {
	// occupiers
	static public final int EMPTY = 1000;
	static public final int THREAT_SOURCE = 1001;
	static public final int AGENT = 1002;
	static public final int OBSTACLE = 1003;
	static public final int DUMMY = 1005;

	// cell flag
	static public final int START = 2000;
	static public final int GOAL = 2001;
	static public final int NONE = 2002;

	public Cell parent = null;
	public double g = 0.0;// costFactored distance covered from start point to
							// this cell's center
	public double h = 0.0;// euclidean distance from this cell to the goal
							// cell's center
	public double f = 0.0;// =g+h;

	public int numberInShortestPath = 0;

	public boolean isInHeap = false;
	public boolean isInDamageHeap = false;
	public boolean isInQueue = false;
	public boolean isProcessed = false;

	public boolean isInOpen = false;
	public boolean isInClosed = false;

	public boolean isCurrentlyOpen = false;

	public Cell[] path;
	public int explorationID = 0;

	public Vector[] label = new Vector[4];

	protected double totalDamage = 0.0;

	protected int row, col;

	// protected double size;

	public boolean isFeasible = true;

	protected double costFactor;
	protected int occupier;
	protected int flag;

	protected boolean isInShortestPath = false;

	// protected double centerX, centerY;

	protected boolean visited = false;

	protected Vector distanceTraveledInThreatZone = new Vector(0, 1);

	// MOA* variables
	public Vector[] moa_g = new Vector[4];
	// public Vector<TwoElement> moa_h=new Vector<TwoElement>(8,0);
	public TwoElement[] moa_h = new TwoElement[4];
	public Vector[] moa_f = new Vector[4];

	// protected Cell(){
	// }

	public Cell(int _row, int _column, double _costFactor, int _occupier,
			int _flag) {
		if (_row < 0) {
			System.out.println("Cell row number cannot be negative!");
			System.exit(1);
		}
		if (_column < 0) {
			System.out.println("Cell column number cannot be negative!");
			System.exit(1);
		}
		if (_costFactor < 1) {
			System.out.println("Cost factor cannot be less than 1!");
			System.exit(1);
		}
		if (_occupier != EMPTY && _occupier != THREAT_SOURCE
				&& _occupier != AGENT && _occupier != OBSTACLE
				&& _occupier != DUMMY) {
			System.out.println("Unknown cell occupier!");
			System.exit(1);
		}
		if (_flag != START && _flag != GOAL && _flag != NONE) {
			System.out.println("Unknown cell flag!");
			System.exit(1);
		}

		row = _row;
		col = _column;
		// size = _size;
		costFactor = _costFactor;
		occupier = _occupier;
		flag = _flag;
		// centerX = (col + 0.5) * size;
		// centerY = (row + 0.5) * size;
		resetHeuristicsForMOAStar();
	}

	public void resetHeuristicsForMOAStar() {
		for (int i = 0; i < 4; i++) {
			moa_g[i] = new Vector<TwoElement>(10, 2);
			// moa_h[i]=new Vector<TwoElement>(2,1);
			moa_f[i] = new Vector<TwoElement>(10, 2);
			label[i] = new Vector<TwoElement>(10, 2);
		}
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	// public double getSize() {
	// return size;
	// }

	public int getOccupier() {
		return occupier;
	}

	// public double getCostFactor() {
	// return costFactor;
	// }

	public int getFlag() {
		return flag;
	}

	public boolean isOccupied() {
		return (occupier == OBSTACLE || occupier == THREAT_SOURCE);
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean vis) {
		visited = vis;
	}

	public boolean getIsInShortestPath() {
		return isInShortestPath;
	}

	public void setIsInShortestPath(boolean val) {
		isInShortestPath = val;
	}

	public void setTotalDamage(double val) throws Exception {
		if (val < 0)
			throw new Exception(
					"Cell total damage cannot be set to a negative value!");
		totalDamage = val;
	}

	public void addToTotalDamage(double val) throws Exception {
		if (val < 0)
			throw new Exception(
					"Negative value cannot be added to cell total damage!");
		totalDamage += val;
	}

	public double getTotalDamage() {
		return totalDamage;
	}

	public void setFlag(int _flag) throws Exception {
		if (_flag != START && _flag != GOAL && _flag != NONE) {
			throw new Exception(
					"Cell occupier is being set to an unknown occupier type!");
		}
		flag = _flag;
	}

	public void setOccupier(int _occupier) throws Exception {
		if (_occupier != EMPTY && _occupier != THREAT_SOURCE
				&& _occupier != AGENT && _occupier != OBSTACLE) {
			throw new Exception(
					"Cell occupier is being set to an unknown occupier type!");
		}
		occupier = _occupier;
	}

	// public void setCostFactor(double _costFactor) throws Exception {
	// if (_costFactor < 1) {
	// throw new Exception("Cell cost factor cannot be less than 1!");
	// }
	// costFactor = _costFactor;
	// }

	public void clearThreatSourceDistances() {
		distanceTraveledInThreatZone = new Vector(0, 1);
	}

	public void addNewThreatSource() {
		distanceTraveledInThreatZone.add(new Double(0));
	}

	public void addDistanceTraveledInThreatZone(int sourceNumber, double dist) {
		Double val = new Double(
				((Double) (distanceTraveledInThreatZone.elementAt(sourceNumber)))
						.doubleValue() + dist);

		distanceTraveledInThreatZone.setElementAt(val, sourceNumber);
	}

	public void addDistanceTraveledInThreatZone_i(int sourceNumber,
			TwoElement te, double dist) {
		te.distanceTraveledInThreatZone_i[sourceNumber] += dist;
	}

	public void setDistanceTraveledInThreatZone_i(int sourceNumber,
			TwoElement te, double dist) {
		te.distanceTraveledInThreatZone_i[sourceNumber] = dist;
	}

	public double getDistanceTraveledInThreatZone_i(int sourceNumber,
			TwoElement te) {
		return te.distanceTraveledInThreatZone_i[sourceNumber];
	}

	public void setDistanceTraveledInThreatZone(int sourceNumber, double dist) {
		Double val = new Double(dist);

		distanceTraveledInThreatZone.setElementAt(val, sourceNumber);
	}

	public double getDistanceTraveledInThreatZone(int sourceNumber) {
		return ((Double) (distanceTraveledInThreatZone.elementAt(sourceNumber)))
				.doubleValue();
	}

	public boolean isNeighbor(Cell c2) {
		if (this.row == c2.getRow()) {
			if ((this.col == c2.getCol() - 1) || (this.col == c2.getCol() + 1)) {
				return true;
			}
		} else if (this.col == c2.getCol()) {
			if ((this.row == c2.getRow() - 1) || (this.row == c2.getRow() + 1)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "[" + row + "," + col + "]";
	}
}