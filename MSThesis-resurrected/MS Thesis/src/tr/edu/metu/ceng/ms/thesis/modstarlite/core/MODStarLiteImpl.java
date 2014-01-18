package tr.edu.metu.ceng.ms.thesis.modstarlite.core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.MOState;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Objective;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveArray;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveBehaviour;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path;
import tr.edu.metu.ceng.ms.thesis.modstarlite.ui.components.MOGridMap;
import tr.edu.metu.ceng.ms.thesis.modstarlite.util.InconsistentObjectiveSizeException;
import tr.edu.metu.ceng.ms.thesis.modstarlite.util.StateWriter;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;
import tr.edu.metu.ceng.ms.thesis.robotutils.util.CoordUtils;

public class MODStarLiteImpl extends MODStarLite<IntCoord> {

	private MOGridMap map;

	protected long startTime;

	protected long stopTime;
	
	private static long totalExecTime = 0;
	
	private static long iterationCount = 1;
	
	private String executionFilePath;
	
	private static Logger logger = Logger.getLogger(MODStarLiteImpl.class);

	public MODStarLiteImpl(MOGridMap map, IntCoord start, IntCoord goal, String executionFilePath) {
		super(start, goal);
		this.map = map;
		this.executionFilePath = executionFilePath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tr.edu.metu.ceng.ms.thesis.modstarlite.core.MODStarLite#c(tr.edu.metu
	 * .ceng.ms.thesis.robotutils.data.Coordinate,
	 * tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate)
	 * 
	 * c function : c(s', s) = [1, threatRisk(s)]
	 */
	@Override
	protected ObjectiveArray c(IntCoord a, IntCoord b) {
		if (CoordUtils.mdist(a, b) != 1) {
			return ObjectiveArray.positiveInfinityForMinimizedBehaviour()
					.get(0);
		} else {
			// if it is an obstacle...
			if (map.hasObstacle(a.getInts())
					|| !map.isInViewingFrustumArea(a.getInts()))
				return ObjectiveArray.positiveInfinityForMinimizedBehaviour()
						.get(0);

			// if it is an obstacle...
			if (map.hasObstacle(b.getInts())
					|| !map.isInViewingFrustumArea(b.getInts()))
				return ObjectiveArray.positiveInfinityForMinimizedBehaviour()
						.get(0);

			// get constraints of corresponding coords.
			MOState sPrime = map.get(a.getInts());
			MOState s = map.get(b.getInts());

			// get objective arrays of these states...
			ObjectiveArray sPrimeObjArr = sPrime.getObjectives();
			ObjectiveArray sObjArr = s.getObjectives();

			if (sPrimeObjArr.size() != sObjArr.size()) {
				throw new InconsistentObjectiveSizeException();
			}

			// create the calculated obj array instance...
			ObjectiveArray c = new ObjectiveArray(sPrimeObjArr.size());

			c.put(0, new Objective(1d, sPrimeObjArr.get(0).getBehaviour()));
			c.put(1, new Objective(sObjArr.get(1).getValue(), sObjArr.get(1)
					.getBehaviour()));

			return c;
		}
	}

	// private ObjectiveArray calculateArithmeticAverage(
	// ObjectiveArray objectivesA, ObjectiveArray objectivesB) {
	//
	// if (objectivesA.size() != objectivesB.size()) {
	// throw new InconsistentObjectiveSizeException();
	// }
	//
	// // create the calculated obj array instance...
	// ObjectiveArray arr = new ObjectiveArray(objectivesA.size());
	//
	// for (int i = 0; i < objectivesA.size(); i++) {
	// // calculate the value for c function.
	// double value = (objectivesA.get(i).getValue() + objectivesB.get(i)
	// .getValue()) / 2;
	// // the actual cost moving from s to s'
	// value += 1;
	// // throw exc. if behaviours are inconsistent.
	// if (!objectivesA.get(i).getBehaviour()
	// .equals(objectivesB.get(i).getBehaviour())) {
	// throw new InconsistentObjectiveBehaviourException();
	// }
	// // create the objective instance...
	// Objective objective = new Objective(value, objectivesA.get(i)
	// .getBehaviour());
	// // put created objective into objective array.
	// arr.put(i, objective);
	// }
	// return arr;
	// }

	@Override
	protected ObjectiveArray h(IntCoord a, IntCoord b) {

		double mdist = CoordUtils.mdist(a, b);
		double totalRisk;
		try {
			// totalRisk = map.get(a.getInts()).getTotalRisk()
			// + map.get(b.getInts()).getTotalRisk();
			totalRisk = mdist;
		} catch (NullPointerException npe) {
			totalRisk = 0d;
		}

		return new ObjectiveArray(new Objective(mdist,
				ObjectiveBehaviour.MINIMIZED), new Objective(0d,
				ObjectiveBehaviour.MINIMIZED));

		// return CoordUtils.moMDist(a, b);
	}

	@Override
	protected Collection<IntCoord> pred(IntCoord s) {
		return nbrs(s, true);
		// return octalNbrs(s);
	}

	@Override
	protected Collection<IntCoord> succ(IntCoord s) {
		return nbrs(s, false);
		// return octalNbrs(s);
	}

	/**
	 * Returns a list of neighbors to the current grid cell, excluding neighbor
	 * cells that have negative cost values.
	 * 
	 * @param s
	 *            the current cell
	 * @return a list of neighboring cells
	 */
	// TODO: Non-dominated neighbors should be extracted here!
	private Collection<IntCoord> nbrs(IntCoord s, boolean isExpanded) {
		List<IntCoord> nbrs = new ArrayList<IntCoord>(2 * map.dims());

		for (int i = 0; i < map.dims(); i++) {
			// get the upper state and add to nbrs under constraints.
			int[] up = Arrays.copyOf(s.getInts(), s.getInts().length);
			up[i] += 1;
			if (map.insideBounds(up) && !map.get(up).hasObstacle()
					&& map.isInViewingFrustumArea(up)
			// && map.get(up).getCoords().isExpanded() == isExpanded
			)
				nbrs.add(map.get(up).getCoords());

			// get the lower state and add to nbrs under constraints.
			int[] down = Arrays.copyOf(s.getInts(), s.getInts().length);
			down[i] -= 1;
			if (map.insideBounds(down) && !map.get(down).hasObstacle()
					&& map.isInViewingFrustumArea(down)
			// && map.get(down).getCoords().isExpanded() == isExpanded
			)
				nbrs.add(map.get(down).getCoords());
		}

		return nbrs;
	}

	@SuppressWarnings("unused")
	private Collection<IntCoord> octalNbrs(IntCoord s) {
		List<IntCoord> nbrs = new ArrayList<IntCoord>(4 * map.dims());

		int[] right = Arrays.copyOf(s.getInts(), s.getInts().length);
		right[0] += 1;
		if (map.insideBounds(right) && !map.get(right).hasObstacle())
			nbrs.add(new IntCoord(right));

		int[] rightDown = Arrays.copyOf(s.getInts(), s.getInts().length);
		rightDown[0] += 1;
		rightDown[1] += 1;
		if (map.insideBounds(rightDown) && !map.get(rightDown).hasObstacle())
			nbrs.add(new IntCoord(rightDown));

		int[] down = Arrays.copyOf(s.getInts(), s.getInts().length);
		right[1] += 1;
		if (map.insideBounds(down) && !map.get(down).hasObstacle())
			nbrs.add(new IntCoord(down));

		int[] leftDown = Arrays.copyOf(s.getInts(), s.getInts().length);
		leftDown[0] -= 1;
		leftDown[1] += 1;
		if (map.insideBounds(leftDown) && !map.get(leftDown).hasObstacle())
			nbrs.add(new IntCoord(leftDown));

		int[] left = Arrays.copyOf(s.getInts(), s.getInts().length);
		left[0] -= 1;
		if (map.insideBounds(left) && !map.get(left).hasObstacle())
			nbrs.add(new IntCoord(left));

		int[] leftUp = Arrays.copyOf(s.getInts(), s.getInts().length);
		leftUp[0] -= 1;
		leftUp[1] -= 1;
		if (map.insideBounds(leftUp) && !map.get(leftUp).hasObstacle())
			nbrs.add(new IntCoord(leftUp));

		int[] up = Arrays.copyOf(s.getInts(), s.getInts().length);
		up[1] -= 1;
		if (map.insideBounds(up) && !map.get(up).hasObstacle())
			nbrs.add(new IntCoord(up));

		int[] rightUp = Arrays.copyOf(s.getInts(), s.getInts().length);
		rightUp[0] += 1;
		rightUp[1] -= 1;
		if (map.insideBounds(rightUp) && !map.get(rightUp).hasObstacle())
			nbrs.add(new IntCoord(rightUp));

		return nbrs;
	}

	public void setObstacleOnCoordinate(IntCoord s, boolean b) {
		// If the cost hasn't changed, don't do anything
		if (b == map.hasObstacle(s.getInts()))
			return;

		// Find all affected edges (tuples of this cell and its neighbors)
		Collection<IntCoord> preds = pred(s);
		Collection<IntCoord> succs = succ(s);

		// Record old costs
		HashMap<IntCoord, ObjectiveArray> predVals = new HashMap<IntCoord, ObjectiveArray>();
		for (IntCoord sPrime : preds) {
			predVals.put(sPrime, c(sPrime, s));
		}

		HashMap<IntCoord, ObjectiveArray> succVals = new HashMap<IntCoord, ObjectiveArray>();
		for (IntCoord sPrime : succs) {
			succVals.put(sPrime, c(s, sPrime));
		}

		// Change map cost
		map.setHasObstacle(b, s.getInts());

		// km = MultiObjectiveUtils.sum(km, h(sLast, start));

		// Flag changes to new costs
		for (IntCoord sPrime : preds) {
			flagChange(sPrime, s, predVals.get(sPrime), c(sPrime, s));
		}

		for (IntCoord sPrime : succs) {
			flagChange(s, sPrime, succVals.get(sPrime), c(s, sPrime));
		}

		updateKm(s);
	}

	public void setStart(IntCoord intCoord) {
		updateStart(intCoord);
	}

	public void updateAgentLocation(Coordinate agentLoc) {
		// update start location.
		updateStart((IntCoord) agentLoc);
	}

	@Override
	protected IntCoord getState(double... coords) {
		int x = (int) coords[0];
		int y = (int) coords[1];
		return map.get(x, y).getCoords();
	}
	
	@Override
	protected void calculatePathCost(Path<Coordinate> drawnPath) {
		double totalPathRisk = 0d;
		for (Coordinate currentCoord : drawnPath.getRoute()) {
			IntCoord intCoord = (IntCoord) currentCoord;
			totalPathRisk += map.get(intCoord.getInts()).getTotalRisk();
		}
		drawnPath.setCost(new ObjectiveArray(new Objective(drawnPath.size(),
				ObjectiveBehaviour.MINIMIZED), new Objective(totalPathRisk,
				ObjectiveBehaviour.MINIMIZED)));
	}

	protected void startTimer() {
		startTime = System.nanoTime();
		logger.info("Timer Started.");
	}

	protected void stopTimer() {
		stopTime = System.nanoTime();
		long execTime = (long) ((stopTime - startTime) / Math.pow(10, 6));
		logger.info("Timer Stopped, execution time: " + execTime + " ms");
//		StateWriter.getWriter().dumpTime(iterationCount, execTime, executionFilePath);
		iterationCount++;
		totalExecTime += execTime;
	}

	@Override
	protected void writeBackpointersToFile() {
		try {
			// open writers.
			FileWriter fWriter = new FileWriter("/tmp/backpointers", true);
			BufferedWriter bWriter = new BufferedWriter(fWriter);

			for (int i = 1; i < map.size(0); i++) {
				for (int j = 1; j < map.size(1); j++) {
					bWriter.write("(" + i + "," + j + ") parents: "
							+ getState((double) i, (double) j).getParents());
					bWriter.newLine();
				}
			}
			// Close the output stream
			bWriter.close();
			fWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public long getTotalExecTime() {
		return totalExecTime;
	}

//	@Override
//	protected void nonDominatedWithAll(
//			Set<IntCoord> potentialNonDominatedSuccessors, IntCoord sPrime) {
//
//		Set<IntCoord> removalStates = new HashSet<IntCoord>();
//		boolean isSPrimeAdded = true;
//		MOState mosPrime = map.get(sPrime.getInts());
//		for (IntCoord state : potentialNonDominatedSuccessors) {
//			MOState moState = map.get(state.getInts());
//			if (mosPrime.dominates(moState)) {
//				removalStates.add(state);
//			}
//			if (moState.dominates(mosPrime)) {
//				isSPrimeAdded = false;
//			}
//		}
//		potentialNonDominatedSuccessors.removeAll(removalStates);
//		if (isSPrimeAdded) {
//			potentialNonDominatedSuccessors.add(sPrime);
//		}
//
//	}
//
//	@Override
//	protected void considerAddingNeighbor(List<IntCoord> potentialNonDomStates,
//			IntCoord sPrime) {
//
//		Set<IntCoord> removalStates = new HashSet<IntCoord>();
//		boolean isSPrimeAdded = true;
//		MOState mosPrime = map.get(sPrime.getInts());
//		for (IntCoord state : potentialNonDomStates) {
//			MOState moState = map.get(state.getInts());
//			if (mosPrime.dominates(moState)) {
//				removalStates.add(state);
//			}
//			if (moState.dominates(mosPrime)) {
//				isSPrimeAdded = false;
//			}
//		}
//		potentialNonDomStates.removeAll(removalStates);
//		if (isSPrimeAdded) {
//			potentialNonDomStates.add(sPrime);
//		}
//
//	}
//
//	@Override
//	protected ObjectiveArray calculatePathCost(Path<IntCoord> path) {
//
//		double totalPathRisk = 0d;
//		for (IntCoord currentCoord : path.getRoute()) {
//			totalPathRisk += map.get(currentCoord.getInts()).getTotalRisk();
//		}
//		ObjectiveArray costOA = new ObjectiveArray(new Objective(path.size(),
//				ObjectiveBehaviour.MINIMIZED), new Objective(totalPathRisk,
//				ObjectiveBehaviour.MINIMIZED));
//		path.setCost(costOA);
//
//		return costOA;
//	}


}
