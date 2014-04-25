package tr.edu.metu.ceng.ms.thesis.moastar;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Objective;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveArray;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveBehaviour;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Obstacle;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Threat;
import tr.edu.metu.ceng.ms.thesis.modstarlite.ui.components.MOStaticMap;
//import tr.edu.metu.ceng.ms.thesis.modstarlite.util.StateWriter;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;

public class MOAStar {

	protected boolean debug = false;

	protected boolean printSolutionToFile = false;

	// variables given as parameter in findPath_MOAStar(boolean, boolean)
	protected boolean printMaps = true;
	protected boolean printPathData = true;

	protected long startTime, stopTime;

	protected Cell[][] map;

	protected BufferedWriter mapOutputFile;

	protected int numRows, numCols;

	// protected Agent agent;

	protected Cell startCell;
	protected Cell goalCell;

	protected List<Obstacle> obstacles = new Vector<Obstacle>();

	protected List<Threat> threats = new Vector<Threat>();

	private Vector<Path> solutionPaths = new Vector<Path>(0, 1);
	private Vector<TwoElement> solutions = new Vector<TwoElement>(0, 1);
	private Vector<TwoElement> solution_costs = new Vector<TwoElement>(0, 1);

	// private Vector<Cell> solution_goals = new Vector<Cell>(0, 1);

	private Vector<Cell> open = new Vector<Cell>(0, 1);
	private Vector<Cell> closed = new Vector<Cell>(0, 1);
	private Vector<TwoElement> ND = new Vector<TwoElement>(0, 1);

	protected final String MAP = "MAP";
	protected final String STARTGOAL = "STARTGOAL";
	protected final String HARDOBSTACLES = "HARDOBSTACLES";
	protected final String SOFTOBSTACLES = "SOFTOBSTACLES";
	protected final String THREATS = "THREATS";

	protected final String AGENT = "AGENT";

	private MOStaticMap smap;

	private static long totalExecTime = 0;

	private String executionFilePath;

	private static long iterationCount = 1;

	public MOAStar(MOStaticMap map, String executionFilePath) throws Exception {
		this.smap = map;
		this.executionFilePath = executionFilePath;
		wrapProperties();
	}

	public void wrapProperties() throws Exception {

		// map properties
		numCols = smap.size(0);
		numRows = smap.size(1);
		map = new Cell[numRows][];
		for (int i = 0; i < numRows; i++) {
			if (i == 0 || i == numRows - 1) {
				continue;
			} else {
				map[i] = new Cell[numCols];
				for (int j = 0; j < numCols; j++) {
					if (j == 0 || j == numCols - 1) {
						continue;
					} else {
						map[i][j] = new Cell(i, j, 1, Cell.EMPTY, Cell.NONE);
					}
				}
			}
		}

		// start cell properties.
		int r = smap.getStart()[1];
		int c = smap.getStart()[0];
		startCell = map[r][c];
		map[r][c].setOccupier(Cell.AGENT);
		map[r][c].setFlag(Cell.START);

		// goal cell properties.
		r = smap.getTmpGoal()[1];
		c = smap.getTmpGoal()[0];
		goalCell = map[r][c];
		map[r][c].setFlag(Cell.GOAL);

		// agent properties.
		// agent = new Agent();

		// obstacle properties.
		obstacles = smap.getObstacles();
		for (Obstacle obstacle : obstacles) {
			try {
				map[obstacle.getCoords().getInts()[1]][obstacle.getCoords()
						.getInts()[0]].setOccupier(Cell.OBSTACLE);
			} catch (Exception aioobe) {
				continue;
			}
		}

		// threat properties.
		threats = smap.getThreats();

	}

	/**** HERE GOES THE PATH-FINDER MOA* METHODS ****/

	public List<tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path<Coordinate>> findPath_MOAStar()
			throws Exception {
		int i = 0, j = 0, k = 0, l = 0;
		long currTime;

		Path currentPath;
		// Vector solutionTimes=new Vector(0,1);
		Cell currentCell = null, neighborCell = null, neighborOfNeighborCell = null;

		initiateCells();
		startTimer();

		TwoElement dummyElem, dummyElem2;
		for (i = 0; i < 4; i++) {
			startCell.moa_g[i].add(new TwoElement(startCell, i, 0, 0, threats
					.size()));
			if ((neighborCell = getNeighbor(startCell, i)) != null) {
				if (neighborCell.isOccupied())
					continue;
				// calculate h
				dummyElem = new TwoElement(startCell, i, 0, 0, threats.size());
				setDistanceTraveledInThreatZone_h(startCell, neighborCell,
						dummyElem);
				dummyElem.value1 = getCellToCellHalfDistance(startCell,
						neighborCell);
				dummyElem.value2 = calculateDamage_h(startCell, neighborCell,
						(TwoElement) startCell.moa_g[i].elementAt(0), dummyElem);
				startCell.moa_h[i] = dummyElem;
				// calculate f

				dummyElem = computeTwoElement_f(startCell, i,
						((TwoElement) startCell.moa_g[i].elementAt(0)),
						startCell.moa_h[i]);
				// if (dummyElem.value2 > agent.getAllowedDamage())
				// continue;
				dummyElem.setParent(startCell, i,
						(TwoElement) (startCell.moa_g[i].elementAt(0)));
				startCell.moa_f[i].add(dummyElem);
			}
		}
		open.add(startCell);
		startCell.isInOpen = true;

		while (true) {
			// step 1
			ND = getNonDominatedElements(open);
			// step 2
			// 2.1
			if (ND.size() == 0) {
				break;
			} else {// 2.2
				currentCell = chooseMinimumDistanceOfMinimumDamageFrom(ND);
				if (currentCell.isOccupied())
					continue;
				currentCell.isCurrentlyOpen = true;
				open.remove(currentCell);
				currentCell.isInOpen = false;
				closed.add(currentCell);
				currentCell.isInClosed = true;
				currentCell.setVisited(true);
			}
			if (debug)
				simplePrint_MOAStar();

			// 3
			// prune this cell's neighbors' f values which don't dominate this
			// cell's g values
			makeNonDominated(currentCell.moa_g);

			// 4
			if (currentCell == goalCell) {

				for (i = 0; i < 4; i++) {
					for (j = 0; j < goalCell.moa_g[i].size(); j++) {
						dummyElem = (TwoElement) (goalCell.moa_g[i]
								.elementAt(j));
						dummyElem.isADiscoveredSolution = true;
					}
				}

				solution_costs.removeAllElements();
				for (i = 0; i < 4; i++) {
					for (j = 0; j < goalCell.moa_g[i].size(); j++) {
						dummyElem = ((TwoElement) goalCell.moa_g[i]
								.elementAt(j)).duplicate();

						solution_costs.add(dummyElem);
					}
				}
				makeNonDominated_SolutionCosts(solution_costs);
				continue;
			}

			// 5
			for (i = 0; i < 4; i++) {
				// for each successor of the currentcell
				neighborCell = getNeighbor(currentCell, i);

				if (neighborCell != null && !neighborCell.isOccupied()) {

					// neighborCell.parent=currentCell;
					// 5.3.1
					if (!neighborCell.isVisited() && !neighborCell.isProcessed) {
						// 5.3.1.1 and 5.3.1.2
						// add all of currentcell's f's twoelement to the g of
						// neighborcell
						for (j = 0; j < currentCell.moa_f[i].size(); j++) {
							dummyElem = ((TwoElement) (currentCell.moa_f[i]
									.elementAt(j))).duplicate();
							dummyElem.cell = neighborCell;
							dummyElem.side = (i + 2) % 4;
							dummyElem.setParent(currentCell, i,
									((TwoElement) (currentCell.moa_f[i]
											.elementAt(j))));
							(neighborCell.moa_g[(i + 2) % 4]).add(dummyElem);
							if (neighborCell == goalCell) {
								currTime = getCurrentCPUTime();
								if (currTime < dummyElem.finishTime) {
									dummyElem.finishTime = currTime;
									// solutionTimes.add(Math.round((currTime-startTime)/10000.0)/100.0);
								}
							}
						}
						// 5.3.1.2 //keep the nondominated g values
						// TODO
						makeNonDominated(neighborCell.moa_g);
						// 5.3.1.3 compute h and f at neighborcell
						for (j = 0; j < 4; j++) {// for each neighbor of
													// neighborcell
							neighborOfNeighborCell = getNeighbor(neighborCell,
									j);
							if (neighborOfNeighborCell == null
									|| neighborOfNeighborCell.isOccupied())
								continue;

							for (k = 0; k < 4; k++) {// for each g (direction)
								// for each g from that direction
								for (l = 0; l < neighborCell.moa_g[k].size(); l++) {
									// calculate an h for each g
									dummyElem = new TwoElement(neighborCell, j,
											0, 0, threats.size());
									setDistanceTraveledInThreatZone_h(
											neighborCell,
											neighborOfNeighborCell, dummyElem);
									dummyElem.value1 = getCellToCellHalfDistance(
											neighborCell,
											neighborOfNeighborCell);
									dummyElem.value2 = calculateDamage_h(
											neighborCell,
											neighborOfNeighborCell,
											(TwoElement) neighborCell.moa_g[k]
													.elementAt(l), dummyElem);
									// neighborCell.moa_h[j]=dummyElem;
									// calculate f

									dummyElem2 = computeTwoElement_f(
											neighborCell, j,
											((TwoElement) neighborCell.moa_g[k]
													.elementAt(l)), dummyElem);
									// if (dummyElem2.value2 > agent
									// .getAllowedDamage())
									// continue;
									dummyElem2.setParent(
											((TwoElement) neighborCell.moa_g[k]
													.elementAt(l)).cell, k,
											((TwoElement) neighborCell.moa_g[k]
													.elementAt(l)));
									neighborCell.moa_f[j].add(dummyElem2);
								}
							}
						}
						// 5.3.1.4 add n' to open
						if (!neighborCell.isOccupied()) {
							open.add(neighborCell);
							neighborCell.isProcessed = true;
							neighborCell.isInOpen = true;
						}

					} else {// 5.3.2
							// if the neighborcell can be reached from
							// currentcell with nondominated cost
						boolean anyNonDominatedPathFound = false;
						for (j = 0; j < currentCell.moa_f[i].size(); j++) {
							dummyElem = (TwoElement) currentCell.moa_f[i]
									.elementAt(j);
							boolean nonDominatingFound = true;

							for (k = 0; k < 4; k++) {
								for (l = 0; l < neighborCell.moa_g[k].size(); l++) {
									if (((TwoElement) neighborCell.moa_g[k]
											.elementAt(l))
											.dominatesOrEquals(dummyElem)) {
										nonDominatingFound = false;
										break;
									}
								}
								if (!nonDominatingFound)
									break;
							}
							// 5.3.2.1
							if (nonDominatingFound) {
								anyNonDominatedPathFound = true;
								dummyElem2 = dummyElem.duplicate();
								dummyElem2.cell = neighborCell;
								dummyElem2.side = (i + 2) % 4;
								dummyElem2.setParent(currentCell, i, dummyElem);
								neighborCell.moa_g[(i + 2) % 4].add(dummyElem2);
								if (neighborCell == goalCell) {
									currTime = getCurrentCPUTime();
									if (currTime < dummyElem2.finishTime) {
										dummyElem2.finishTime = currTime;
										// solutionTimes.add(Math.round((currTime-startTime)/10000.0)/100.0);
									}
								}

							}
						}
						// FIXME : 5.3.2.1 den sonra burasinin gelmesinde bir
						// hata olabilir.
						if (anyNonDominatedPathFound) {
							makeNonDominated(neighborCell.moa_g);
							// recalculate the f values for the neighborCell
							for (j = 0; j < 4; j++) {// for each neighbor of
														// neighborcell
								neighborOfNeighborCell = getNeighbor(
										neighborCell, j);
								if (neighborOfNeighborCell == null
										|| neighborOfNeighborCell.isOccupied())
									continue;

								for (k = 0; k < 4; k++) {// for each g
															// (direction)
									for (l = 0; l < neighborCell.moa_g[k]
											.size(); l++) {// for each g from
															// that direction
										// calculate an h for each g
										dummyElem = new TwoElement(
												neighborCell, j, 0, 0,
												threats.size());
										setDistanceTraveledInThreatZone_h(
												neighborCell,
												neighborOfNeighborCell,
												dummyElem);
										dummyElem.value1 = getCellToCellHalfDistance(
												neighborCell,
												neighborOfNeighborCell);
										dummyElem.value2 = calculateDamage_h(
												neighborCell,
												neighborOfNeighborCell,
												(TwoElement) neighborCell.moa_g[k]
														.elementAt(l),
												dummyElem);
										// neighborCell.moa_h[i]=dummyElem;
										// calculate f

										dummyElem2 = computeTwoElement_f(
												neighborCell,
												j,
												((TwoElement) neighborCell.moa_g[k]
														.elementAt(l)),
												dummyElem);
										// if (dummyElem2.value2 > agent
										// .getAllowedDamage())
										// continue;
										dummyElem2
												.setParent(
														((TwoElement) neighborCell.moa_g[k]
																.elementAt(l)).cell,
														k,
														((TwoElement) neighborCell.moa_g[k]
																.elementAt(l)));
										neighborCell.moa_f[j].add(dummyElem2);
									}
								}
							}

							makeNonDominated_f(neighborCell.moa_f);
							if (neighborCell.isInClosed) {
								closed.remove(neighborCell);
								neighborCell.isInClosed = false;

								if (!neighborCell.isInOpen) {
									open.add(neighborCell);
									neighborCell.isInOpen = true;
								}
							} else {
								if (!neighborCell.isInOpen) {
									open.add(neighborCell);
									neighborCell.isInOpen = true;
								}
							}

						}
					}
				}
			}
			currentCell.isCurrentlyOpen = false;
		}

		solutions = new Vector<TwoElement>(0, 1);
		for (i = 0; i < 4; i++) {
			for (j = 0; j < goalCell.moa_g[i].size(); j++) {
				solutions.add((TwoElement) (goalCell.moa_g[i].elementAt(j)));
			}
		}
		// sort w.r.t. path length
		QuickSort.quicksort(solutions, TwoElement.Comparator_Element1, true);
		for (i = 0; i < solutions.size(); i++) {
			dummyElem = (TwoElement) (solutions.elementAt(i));
			currentPath = new Path();
			currentPath.pathLength = dummyElem.value1;
			currentPath.damage = dummyElem.value2;
			currentPath.time = Math
					.round((dummyElem.finishTime - startTime) / 10000.0) / 100.0;// ms
			solutionPaths.add(currentPath);
		}

		stopTimer();

		// return solutionPaths;
		return generateAndDisplayPaths();
	}

	private List<tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path<Coordinate>> generateAndDisplayPaths()
			throws Exception {
		TwoElement dummyElem;
		double timeused;
		// create a new list of my-way paths.
		List<tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path<Coordinate>> realPaths = new Vector<tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path<Coordinate>>();

//		System.out.println("Solutions:");
		for (int i = 0; i < solutions.size(); i++) {

			// System.out.println("\n" + (i + 1)
			// + ": --------------------------------------------");
			dummyElem = solutions.elementAt(i);
//			System.out.println("Path length: "
//					+ Math.round(100.0 * dummyElem.value1) / 100.0);
//			System.out.println("Damage taken: " + dummyElem.value2);
			// System.out.println(dummyElem.finishTime);
			timeused = Math.round((dummyElem.finishTime - startTime) / 10000.0) / 100.0;
			// System.out.println("CPU time to reach this solution: " + timeused
			// + " msec");
			dummyElem.isADiscoveredSolution = true;
			if (printMaps) {
				markShortestPath_MOAStar(dummyElem);
				tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path<Coordinate> path = simplePrint_MOAStarPaths();
				realPaths.add(path);
			}
		}

		long execTime = (long) ((stopTime - startTime) / Math.pow(10, 6));
		totalExecTime += execTime;
//		System.out.println("Total Execution Time: " + execTime + " ms");
		// StateWriter.getWriter().dumpTime(iterationCount , execTime,
		// executionFilePath);
		iterationCount++;
//		System.out
//				.println("\n------------- END OF EXACT SOLUTIONS --------------");
		return realPaths;
	}

	protected void makeNonDominated(Vector[] v) {
		int i, j, k, l;
		TwoElement elem1 = null, elem2 = null;
		for (i = 0; i < 4; i++) {
			for (j = 0; j < v[i].size(); j++) {
				elem1 = (TwoElement) (v[i].elementAt(j));
				for (k = 0; k < 4; k++) {
					for (l = 0; l < v[k].size(); l++) {
						elem2 = (TwoElement) (v[k].elementAt(l));
						if (elem1 != elem2 && elem1.dominatesOrEquals(elem2)) {
							v[k].remove(l);
							l--;
						}
					}
				}
			}
		}
	}

	protected void makeNonDominated_f(Vector[] v) {
		int i, j, k, l;
		TwoElement elem1 = null, elem2 = null;
		for (i = 0; i < 4; i++) {
			for (j = 0; j < v[i].size(); j++) {
				elem1 = (TwoElement) (v[i].elementAt(j));
				for (l = 0; l < v[i].size(); l++) {
					elem2 = (TwoElement) (v[i].elementAt(l));
					if (elem1 != elem2 && elem1.dominatesOrEquals(elem2)) {
						v[i].remove(l);
						l--;
					}
				}
			}
		}
	}

	protected void makeNonDominated_SolutionCosts(Vector<TwoElement> v) {
		TwoElement elem1 = null, elem2 = null;

		for (int j = 0; j < v.size(); j++) {
			elem1 = (TwoElement) (v.elementAt(j));
			for (int l = 0; l < v.size(); l++) {
				elem2 = (TwoElement) (v.elementAt(l));
				if (elem1 != elem2 && elem1.dominatesOrEquals(elem2)) {
					v.remove(l);
					l--;
				}
			}
		}
	}

	protected double calculateDamage_h(Cell parentCell, Cell neighborCell,
			TwoElement g, TwoElement h) throws Exception {
		int i;
		// add distance traveled in each threat zone separately ***WITHOUT***
		// considering cost factors of the cells

		double dist, totalDamage = 0, prevDamage = 0;

		// IMPORTANT!!! SOFT OBSTACLES' PROPERTIES ARE NOT INCLUDED INTO DAMAGE
		// CALCULATION!!!!
		for (i = 0; i < threats.size(); i++) {
			dist = g.distanceTraveledInThreatZone_i[i]
					+ h.distanceTraveledInThreatZone_i[i];
			// totalDamage += threats.get(i).distanceToDamage(dist,
			// agent.speed);
		}
		for (i = 0; i < threats.size(); i++) {
			dist = g.distanceTraveledInThreatZone_i[i];
			// prevDamage += threats.get(i).distanceToDamage(dist, agent.speed);
		}
		// return totalDamage - prevDamage;

		double risk = 0;
		for (Threat threat : threats) {
			risk += threat.getRiskByCoordinate(neighborCell.getRow(),
					neighborCell.getCol());
		}
		return risk;
	}

	public double getCellToCellHalfDistance(Cell c1, Cell c2) throws Exception {
		if (c1.isNeighbor(c2)) {
			return 1.0d;
		} else {
			throw new Exception("These cells are not neighbors! : " + c1 + c2);
		}
		// return (c1.costFactor + c2.costFactor)
		// * cellSize
		// * Math.sqrt(Math.abs(c1.getRow() - c2.getRow())
		// + Math.abs(c1.getCol() - c2.getCol())) / 2;
	}

	protected Cell chooseMinimumDistanceOfMinimumDamageFrom(Vector<TwoElement> v) {
		// Vector<TwoElement> nonDominated=new Vector<TwoElement>(0,10);

		// sort the vector by distance
		QuickSort.quicksort(v, TwoElement.Comparator_Element2, true);
		// sort inner equalities by damage
		int j = 0;
		for (int i = 1; i < v.size(); i++) {
			if (((TwoElement) v.elementAt(i - 1)).value1 < ((TwoElement) v
					.elementAt(i)).value1) {
				if (i > j) {
					QuickSort.quicksort(v, TwoElement.Comparator_Element1, j,
							i - 1, true);
				}
				j = i + 1;
			} else if (i == v.size() - 1) {
				if (i > j) {
					QuickSort.quicksort(v, TwoElement.Comparator_Element1, j,
							i, true);
				}
			}
		}
		if ((TwoElement) v.elementAt(0) == null)
			return null;
		return ((TwoElement) v.elementAt(0)).cell;
	}

	protected void initiateCells() throws Exception {
		int r, c, i;
		for (r = 0; r < numRows; r++) {
			for (c = 0; c < numCols; c++) {
				if (!(r == 0 || r == numRows - 1 || c == 0 || c == numCols - 1)) {
					map[r][c].g = 0;
					map[r][c].h = 0;
					map[r][c].f = 0;
					map[r][c].parent = null;
					map[r][c].setVisited(false);
					map[r][c].isProcessed = false;
					map[r][c].isFeasible = true;
					map[r][c].isCurrentlyOpen = false;
					map[r][c].isInShortestPath = false;
					map[r][c].isInHeap = false;
					map[r][c].isInDamageHeap = false;
					map[r][c].isInQueue = false;
					map[r][c].isInOpen = false;
					map[r][c].isInClosed = false;
					map[r][c].setTotalDamage(0.0);
					map[r][c].resetHeuristicsForMOAStar();

					// initiate how much distance is traveled in each threat
					// source
					map[r][c].clearThreatSourceDistances();
					for (i = 0; i < threats.size(); i++) {
						map[r][c].addNewThreatSource();
					}
				}
			}
		}
	}

	protected TwoElement computeTwoElement_f(Cell currentCell, int direction,
			TwoElement g, TwoElement h) {
		TwoElement f = new TwoElement(currentCell, direction, g.value1
				+ h.value1, g.value2 + h.value2, threats.size());
		for (int i = 0; i < threats.size(); i++) {
			f.distanceTraveledInThreatZone_i[i] = g.distanceTraveledInThreatZone_i[i]
					+ h.distanceTraveledInThreatZone_i[i];
		}
		return f;
	}

	protected void startTimer() {
		startTime = System.nanoTime();
	}

	protected void stopTimer() {
		stopTime = System.nanoTime();
	}

	public Cell getNeighbor(Cell cell, int kk) throws Exception {
		int r = 0, c = 0;
		int thisRow = cell.getRow();
		int thisCol = cell.getCol();
		int rightCol = thisCol + 1;
		int leftCol = thisCol - 1;
		int topRow = thisRow - 1;
		int bottomRow = thisRow + 1;
		if (kk < 0 || kk > 3)
			throw new Exception("out of bounds");
		switch (kk) {
			case 0 : // right
				r = thisRow;
				c = rightCol;
				break;
			case 1 : // bottom
				r = bottomRow;
				c = thisCol;
				break;
			case 2 : // left
				r = thisRow;
				c = leftCol;
				break;
			case 3 :// top
				r = topRow;
				c = thisCol;
				break;
		}
		if (r <= 0 || r >= numRows - 1 || c <= 0 || c >= numCols - 1
				|| !smap.isInViewingFrustumArea(c, r))
			return null;
		return map[r][c];
	}

	protected void setDistanceTraveledInThreatZone_h(Cell parentCell,
			Cell childCell, TwoElement te) throws Exception {
		for (int i = 0; i < threats.size(); i++) {
			parentCell.addDistanceTraveledInThreatZone_i(
					i,
					te,
					threatExposureDistance(parentCell, childCell,
							threats.get(i)));
		}
	}

	protected Vector<TwoElement> getNonDominatedElements(Vector<Cell> v) {
		int i, j, k, l;
		TwoElement dummyElem;
		boolean dominatedBySolution;
		Vector<TwoElement> nonDominated = new Vector<TwoElement>(0, 10);
		// reset dominated data
		for (i = 0; i < v.size(); i++) {
			if ((Cell) v.elementAt(i) == goalCell) {
				for (j = 0; j < 4; j++) {
					for (k = 0; k < ((Cell) v.elementAt(i)).moa_g[j].size(); k++) {
						dummyElem = ((TwoElement) ((Cell) v.elementAt(i)).moa_g[j]
								.elementAt(k));
						dominatedBySolution = false;
						for (l = 0; l < solution_costs.size(); l++) {
							if (dummyElem.value1 >= ((TwoElement) solution_costs
									.elementAt(l)).value1
									&& dummyElem.value2 >= ((TwoElement) solution_costs
											.elementAt(l)).value2) {
								dominatedBySolution = true;
								break;
							}
						}

						if (!dominatedBySolution) {
							nonDominated.add(dummyElem);
						}
					}
				}
			} else {
				for (j = 0; j < 4; j++) {
					for (k = 0; k < ((Cell) v.elementAt(i)).moa_f[j].size(); k++) {

						dummyElem = ((TwoElement) ((Cell) v.elementAt(i)).moa_f[j]
								.elementAt(k));
						dominatedBySolution = false;
						for (l = 0; l < solution_costs.size(); l++) {
							if (dummyElem.value1 >= ((TwoElement) solution_costs
									.elementAt(l)).value1
									&& dummyElem.value2 >= ((TwoElement) solution_costs
											.elementAt(l)).value2) {
								dominatedBySolution = true;
								break;
							}
						}

						if (!dominatedBySolution) {
							nonDominated.add(dummyElem);
						}
						// ((TwoElement)((Cell)v.elementAt(i)).moa_f[j].elementAt(k)).isDominated=false;
					}
				}
			}
		}

		// sort the vector by distance
		QuickSort.quicksort(nonDominated, TwoElement.Comparator_Element1, true);
		// sort inner equalities by damage
		j = 0;
		for (i = 1; i < nonDominated.size(); i++) {
			if (((TwoElement) nonDominated.elementAt(i - 1)).value1 < ((TwoElement) nonDominated
					.elementAt(i)).value1) {
				if (i > j) {
					QuickSort.quicksort(nonDominated,
							TwoElement.Comparator_Element2, j, i - 1, true);
				}

				j = i + 1;
			} else if (i == nonDominated.size() - 1) {
				if (i > j) {
					QuickSort.quicksort(nonDominated,
							TwoElement.Comparator_Element2, j, i, true);
				}
			}
		}

		i = 0;
		for (i = 0; i < nonDominated.size() - 1; i++) {
			if (((TwoElement) nonDominated.elementAt(i))
					.dominates(((TwoElement) nonDominated.elementAt(i + 1)))) {
				nonDominated.remove(i + 1);
				i--;
			}
		}

		return nonDominated;
	}

	protected double threatExposureDistance(Cell cell1, Cell cell2, Threat ts) {
		double dist = distanceToLineSegment(ts.getCenterX(), ts.getCenterY(),
				cell1.getRow(), cell1.getCol(), cell2.getRow(), cell2.getCol());
		// if the line passes through the threat zone
		if (dist < ts.getSize()) {
			double dCurr = distance(ts.getCenterX(), ts.getCenterY(),
					cell1.getRow(), cell1.getCol());
			double dGoal = distance(ts.getCenterX(), ts.getCenterY(),
					cell2.getRow(), cell2.getCol());
			// if the current cell is inside the threat zone, but goal is not
			if (dCurr <= ts.getSize() && dGoal >= ts.getSize()) {
				double perpDist = distanceToLine(ts.getCenterX(),
						ts.getCenterY(), cell1.getRow(), cell1.getCol(),
						cell2.getRow(), cell2.getCol());

				double part1 = Math.sqrt(ts.getSize() * ts.getSize() - perpDist
						* perpDist);
				double dist2 = distance(ts.getCenterX(), ts.getCenterY(),
						cell1.getRow(), cell1.getCol());
				double part2 = Math.sqrt(dist2 * dist2 - perpDist * perpDist);

				// if the center of threat zone is inside the line segment
				if (perpDist == dist) {
					return part1 + part2;
				} else {
					return part1 - part2;
				}
			}
			// if the goal cell is inside the threat zone but current cell is
			// not
			else if (dCurr >= ts.getSize() && dGoal <= ts.getSize()) {
				double perpDist = distanceToLine(ts.getCenterX(),
						ts.getCenterY(), cell1.getRow(), cell1.getCol(),
						cell2.getRow(), cell2.getCol());

				double part1 = Math.sqrt(ts.getSize() * ts.getSize() - perpDist
						* perpDist);
				double dist2 = distance(ts.getCenterX(), ts.getCenterY(),
						cell2.getRow(), cell2.getCol());
				double part2 = Math.sqrt(dist2 * dist2 - perpDist * perpDist);

				// if the center of threat zone is inside the line segment
				if (perpDist == dist) {
					return part1 + part2;
				} else {
					return part1 - part2;
				}
			}
			// if both cells are inside the threat zone
			else if (dCurr <= ts.getSize() && dGoal <= ts.getSize()) {
				return distance(cell1.getRow(), cell1.getCol(), cell2.getRow(),
						cell2.getCol());
			}
			// if both points are out of the threat zone
			else if (dCurr >= ts.getSize() && dGoal >= ts.getSize()) {
				return 2 * Math.sqrt(ts.getSize() * ts.getSize() - dist * dist);
			}
		} else {
			return 0.0;
		}
		return 0.0;
	}

	// returns the manhattan distance between two points
	protected double distance(double ax, double ay, double bx, double by) {
		// return (Math.abs(bx - ax) + Math.abs(by - ay));
		return Math.sqrt((bx - ax) * (bx - ax) + (by - ay) * (by - ay));
	}

	protected double distanceToLine(double cx, double cy, double ax, double ay,
			double bx, double by) {

		double r_numerator = (cx - ax) * (bx - ax) + (cy - ay) * (by - ay);
		double r_denomenator = (bx - ax) * (bx - ax) + (by - ay) * (by - ay);
		double r = r_numerator / r_denomenator;

		// double px = ax + r * (bx - ax);
		// double py = ay + r * (by - ay);

		double s = ((ay - cy) * (bx - ax) - (ax - cx) * (by - ay))
				/ r_denomenator;

		return Math.abs(s) * Math.sqrt(r_denomenator);

		// double normalLength = Math.sqrt((B.x - A.x) * (B.x - A.x) + (B.y -
		// A.y) * (B.y - A.y));
		// return Math.abs((P.x - A.x) * (B.y - A.y) - (P.y - A.y) * (B.x -
		// A.x)) / normalLength;

	}

	/**
	 * Taken from <a
	 * href="http://forums.codeguru.com/printthread.php?t=194400">codeguru</a>
	 * 
	 * @param cx
	 * @param cy
	 * @param ax
	 * @param ay
	 * @param bx
	 * @param by
	 * 
	 * @return
	 */
	protected double distanceToLineSegment(double cx, double cy, double ax,
			double ay, double bx, double by) {

		// distanceSegment = distance from the point to the line segment
		// distanceLine = distance from the point to the line (assuming infinite
		// extent in both directions

		double distanceSegment;
		double r_numerator = (cx - ax) * (bx - ax) + (cy - ay) * (by - ay);
		double r_denomenator = (bx - ax) * (bx - ax) + (by - ay) * (by - ay);
		double r = r_numerator / r_denomenator;

		// double px = ax + r * (bx - ax);
		// double py = ay + r * (by - ay);

		double s = ((ay - cy) * (bx - ax) - (ax - cx) * (by - ay))
				/ r_denomenator;

		double distanceLine = Math.abs(s) * Math.sqrt(r_denomenator);

		//
		// (xx,yy) is the point on the lineSegment closest to (cx,cy)
		//
		// double xx = px;
		// double yy = py;

		if ((r >= 0) && (r <= 1)) {
			distanceSegment = distanceLine;
		} else {

			double dist1 = (cx - ax) * (cx - ax) + (cy - ay) * (cy - ay);
			double dist2 = (cx - bx) * (cx - bx) + (cy - by) * (cy - by);
			if (dist1 < dist2) {
				// xx = ax;
				// yy = ay;
				distanceSegment = Math.sqrt(dist1);
			} else {
				// xx = bx;
				// yy = by;
				distanceSegment = Math.sqrt(dist2);
			}
		}
		return distanceSegment;
	}

	// prints the map with visited cells
	public void simplePrint_MOAStar() throws Exception {
		int r, c;
		if (map == null) {
			throw new Exception("The map is not loaded!");
		}
		System.out.print("\n   ");
		for (c = 0; c < numCols; c++) {
			if (c < 10)
				System.out.print("" + c + " ");
			else
				System.out.print("" + c);
		}
		System.out.println();
		for (r = 1; r < numRows - 1; r++) {
			if (r < 10)
				System.out.print("" + r + "  ");
			else
				System.out.print("" + r + " ");

			for (c = 1; c < numCols - 1; c++) {

				if (map[r][c].getFlag() == Cell.GOAL)
					System.out.print("G");
				else if (map[r][c].getFlag() == Cell.START)
					System.out.print("S");
				else if (map[r][c].isCurrentlyOpen)
					System.out.print("*");
				else if (map[r][c].isInOpen)
					System.out.print("+");
				else if (map[r][c].isInClosed)
					System.out.print("-");

				else if (map[r][c].getOccupier() == Cell.OBSTACLE)
					System.out.print("X");
				else if (map[r][c].getOccupier() == Cell.THREAT_SOURCE)
					System.out.print("o");
				else {
					if (map[r][c].isInShortestPath) {
						System.out.print("");
					} else {
						if (map[r][c].isVisited()) {
							System.out.print("/");
						} else {
							System.out.print(".");
						}
					}
				}
				System.out.print(" ");
			}
			System.out.println();
		}
	}

	protected long getCurrentCPUTime() {
		return System.nanoTime();
	}

	protected void markShortestPath_MOAStar(TwoElement te) {
		Cell currentCell = goalCell;
		TwoElement goalTE = te;
		te.isADiscoveredSolution = true;
		int count = 0, r, c;
		for (r = 1; r < numRows - 1; r++) {
			for (c = 1; c < numCols - 1; c++) {
				map[r][c].isInShortestPath = false;
			}
		}

		while (currentCell != null && currentCell != startCell) {
			currentCell.isInShortestPath = true;
			// currentCell.numberInShortestPath=count;
			count++;
			currentCell = te.parentCell;
			te = (te.parentTwoElement).parentTwoElement;
		}
		currentCell = goalCell;
		te = goalTE;
		while (currentCell != null && currentCell != startCell) {
			count--;
			currentCell.numberInShortestPath = count;
			currentCell = te.parentCell;
			te = (te.parentTwoElement).parentTwoElement;
		}
	}

	public tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path<Coordinate> simplePrint_MOAStarPaths()
			throws Exception {

		List<Cell> path = new Vector<Cell>();
		
		// add start cell to path.
		// path.add(new IntCoord(startCell.getCol(), startCell.getRow()));

		int r, c;
		if (map == null) {
			throw new Exception("The map is not loaded!");
		}
		// System.out.print("\n   ");
		// for (c = 1; c < numCols - 1; c++) {
		// if (c < 10)
		// System.out.print("" + c + " ");
		// else
		// System.out.print("" + (c % 100));
		// }
		// System.out.println();
		for (r = 1; r < numRows - 1; r++) {
			// if (r < 10)
			// System.out.print("" + r + "   ");
			// else if (r < 100)
			// System.out.print("" + r + "  ");
			// else
			// System.out.print("" + r + " ");
			for (c = 1; c < numCols - 1; c++) {

				// if (map[r][c].getFlag() == Cell.GOAL) {
				//
				// // IntCoord goalCoord = new IntCoord(r + 1, c + 1);
				// // path.addLast(goalCoord);
				//
				// System.out.print("G");
				// } else if (map[r][c].getFlag() == Cell.START) {
				//
				// // IntCoord startCoord = new IntCoord(r + 1, c + 1);
				// // path.add(startCoord);
				//
				// System.out.print("S");
				// } else if (map[r][c].isCurrentlyOpen)
				// System.out.print("*");
				// else if (map[r][c].isInOpen)
				// System.out.print("+");
				// // else if(map[r][c].isInClosed) System.out.print("-");
				//
				// else if (map[r][c].getOccupier() == Cell.OBSTACLE)
				// System.out.print("X");
				// else if (map[r][c].getOccupier() == Cell.THREAT_SOURCE)
				// System.out.print("o");
				// else {
				// if (map[r][c].isInShortestPath) {
				// System.out.print("");
				// } else {
				// if (map[r][c].isVisited()) {
				// System.out.print("/");
				// } else {
				// System.out.print(".");
				// }
				// }
				// }
				/*
				 * if(map[r][c].getOccupier() == Cell.AGENT) {
				 * System.out.print("A"); }
				 */
				
				if (map[r][c].isInShortestPath
						&& map[r][c].getFlag() != Cell.START
						&& map[r][c].getFlag() != Cell.GOAL) {
					
					path.add(map[r][c]);

//					IntCoord pathStateCoord = new IntCoord(c, r);
//					path.add(pathStateCoord);

					// int num = map[r][c].numberInShortestPath % 100;
					// if (num < 10) {
					// System.out.print("" + num + " ");
					// } else {
					// System.out.print("" + num);
					// }
				}
				// else if (map[r][c].isVisited()) {
				// System.out.print(" ");
				// } else {
				// System.out.print(" ");
				// }
			}
			// System.out.println();
		}
		// add goal cell to path.
		// path.add(new IntCoord(goalCell.getCol(), goalCell.getRow()));

		
		Collections.sort(path, new CellOrderComparator());
		
		// create a real path for this solution.
		tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path<Coordinate> orderedPath = new tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path<Coordinate>();

		for (Cell cell : path) {
			IntCoord pathStateCoord = new IntCoord(cell.col, cell.row);
			orderedPath.add(pathStateCoord);
		}
		
		// printIntoFile();
		orderedPath.addFirst(smap.get(smap.getStart()).getCoords());
		orderedPath.addLast(smap.get(smap.getTmpGoal()).getCoords());
		calculatePathCost(orderedPath);
		return orderedPath;
	}

	private void printIntoFile() throws IOException {
		int r;
		int c;
		if (printSolutionToFile) {
			// BufferedWriter fout=new BufferedWriter(new
			// FileWriter(solutionFilename));
			for (c = 1; c < numCols - 1; c++) {
				if (c < 10)
					mapOutputFile.write("" + c + " ");
				else
					mapOutputFile.write("" + (c % 100));
			}
			mapOutputFile.newLine();
			for (r = 1; r < numRows - 1; r++) {
				if (r < 10)
					mapOutputFile.write("" + r + "   ");
				else if (r < 100)
					mapOutputFile.write("" + r + "  ");
				else
					mapOutputFile.write("" + r + " ");
				for (c = 1; c < numCols - 1; c++) {

					if (map[r][c].getFlag() == Cell.GOAL)
						mapOutputFile.write("G");
					else if (map[r][c].getFlag() == Cell.START)
						mapOutputFile.write("S");
					else if (map[r][c].isCurrentlyOpen)
						mapOutputFile.write("*");
					else if (map[r][c].isInOpen)
						mapOutputFile.write("+");
					// else if(map[r][c].isInClosed) System.out.print("-");

					else if (map[r][c].getOccupier() == Cell.OBSTACLE)
						mapOutputFile.write("X");
					else if (map[r][c].getOccupier() == Cell.THREAT_SOURCE)
						mapOutputFile.write("o");
					else {
						if (map[r][c].isInShortestPath) {
							mapOutputFile.write("");
						} else {
							if (map[r][c].isVisited()) {
								mapOutputFile.write("/");
							} else {
								mapOutputFile.write(".");
							}
						}
					}
					if (map[r][c].isInShortestPath
							&& map[r][c].getFlag() != Cell.START
							&& map[r][c].getFlag() != Cell.GOAL) {
						int num = map[r][c].numberInShortestPath % 100;
						if (num < 10) {
							mapOutputFile.write("" + num + " ");
						} else {
							mapOutputFile.write("" + num);
						}
					} else if (map[r][c].isVisited()) {
						mapOutputFile.write(" ");
					} else {
						mapOutputFile.write(" ");
					}
				}
				mapOutputFile.newLine();
			}
			mapOutputFile.newLine();
		}
	}

	public void updateAgentLocation(Coordinate newAgentLoc) throws Exception {

		// update old cell as empty cell.
		int r = smap.getStart()[1];
		int c = smap.getStart()[0];
		// startCell = map[r][c];
		map[r][c].setOccupier(Cell.EMPTY);
		map[r][c].setFlag(Cell.NONE);

		startCell = map[(int) newAgentLoc.get()[1]][(int) newAgentLoc.get()[0]];
		map[(int) newAgentLoc.get()[1]][(int) newAgentLoc.get()[0]]
				.setOccupier(Cell.AGENT);
		map[(int) newAgentLoc.get()[1]][(int) newAgentLoc.get()[0]]
				.setFlag(Cell.START);

	}

	public void updateGoal(IntCoord tmpGoal) throws Exception {

		solutionPaths = new Vector<Path>(0, 1);
		solutions = new Vector<TwoElement>(0, 1);
		solution_costs = new Vector<TwoElement>(0, 1);

		open = new Vector<Cell>(0, 1);
		closed = new Vector<Cell>(0, 1);
		ND = new Vector<TwoElement>(0, 1);

		for (int i = 0; i < numRows; i++) {
			if (i == 0 || i == numRows - 1) {
				continue;
			} else {
				map[i] = new Cell[numCols];
				for (int j = 0; j < numCols; j++) {
					if (j == 0 || j == numCols - 1) {
						continue;
					} else {
						map[i][j] = new Cell(i, j, 1, Cell.EMPTY, Cell.NONE);
					}
				}
			}
		}

		// start cell properties.
		int r = smap.getStart()[1];
		int c = smap.getStart()[0];
		startCell = map[r][c];
		map[r][c].setOccupier(Cell.AGENT);
		map[r][c].setFlag(Cell.START);

		// goal cell properties.
		r = smap.getTmpGoal()[1];
		c = smap.getTmpGoal()[0];
		goalCell = map[r][c];
		map[r][c].setFlag(Cell.GOAL);

		// agent properties.
		// agent = new Agent();

		// obstacle properties.
		obstacles = smap.getObstacles();
		for (Obstacle obstacle : obstacles) {
			map[obstacle.getCoords().getInts()[1]][obstacle.getCoords()
					.getInts()[0]].setOccupier(Cell.OBSTACLE);
		}

	}

	public long getTotalExecTime() {
		return totalExecTime;
	}

	protected void calculatePathCost(
			tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path<Coordinate> drawnPath) {
		double totalPathRisk = 0d;
		for (Coordinate currentCoord : drawnPath.getRoute()) {
			IntCoord intCoord = (IntCoord) currentCoord;
			totalPathRisk += smap.get(intCoord.getInts()).getTotalRisk();
		}
		drawnPath.setCost(new ObjectiveArray(new Objective(drawnPath.size(),
				ObjectiveBehaviour.MINIMIZED), new Objective(totalPathRisk,
				ObjectiveBehaviour.MINIMIZED)));
	}

}
