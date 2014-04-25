package tr.edu.metu.ceng.ms.thesis.modstarlite.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.Key;
import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.graph.MODStarDAG;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveArray;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path;
import tr.edu.metu.ceng.ms.thesis.modstarlite.util.MultiObjectiveUtils;
import tr.edu.metu.ceng.ms.thesis.modstarlite.util.StateWriter;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;

public abstract class MODStarLite<State extends Coordinate> {

	/**
	 * Defines a suitably large initial capacity for internal data structures to
	 * avoid Java's heuristics reallocating up from scratch.
	 */
	public static final int INITIAL_CAPACITY = 4096;

	protected State start;

	protected State goal;

	private final ValueMap g = new ValueMap();

	private final ValueMap rhs = new ValueMap();

//	private final KeyQueue U = new KeyQueue();
	
	private final MODStarDAG<State> U = new MODStarDAG<State>();
	
	private ObjectiveArray km = ObjectiveArray.ZERO.get(0);

	private List<Path<Coordinate>> solutionPaths;

	/**
	 * Initializes a MOD* search object with the specified start and goal
	 * states.
	 * 
	 * @param start
	 *            the desired start state
	 * @param goal
	 *            the desires end state
	 */
	public MODStarLite(State start, State goal) {
		this.start = start;
		this.goal = goal;

		rhs.put(goal, ObjectiveArray.ZERO);
		U.insert(goal, calculateKey(goal));
	}
	
	/**
	 * Change the start location after initialization. Used to cheaply move
	 * robot along path without needing to replan.
	 * 
	 * @param s
	 *            the new start state
	 */
	public void updateStart(State s) {
		State sLast = start;
		start = s;

		km = MultiObjectiveUtils.sum(km, h(sLast, start));
//		rhs.put(start, ObjectiveArray.positiveInfinityForMinimizedBehaviour());
	}

	protected void updateKm(State s) {
		km = MultiObjectiveUtils.sum(km, h(s, start));
	}

	public void updateGoal(State s) {
		// XXX: this method should change km or other stuff.
		this.goal = s;

		g.clear();
//		rhs.clear();
		U.clear();
		rhs.put(start, ObjectiveArray.positiveInfinityForMinimizedBehaviour());
		g.put(start, ObjectiveArray.positiveInfinityForMinimizedBehaviour());
//		rhs.put(goal, ObjectiveArray.ZERO);
//		U.insert(goal, calculateKey(goal));

		rhs.put(goal, ObjectiveArray.ZERO);
		U.insert(goal, calculateKey(goal));
	}
	
	/**
	 * Recomputes the lowest cost path through the map, taking into account any
	 * changes in start location and edge costs. If no path can be found this
	 * will return an empty list.
	 * 
	 * @return a list of states from start to goal
	 */
	public List<Path<Coordinate>> plan() {
		// the non-dominated paths found by MOD*Lite.
//		Map<State, List<Path<State>>> nonDominatedPathsMap = new HashMap<State, List<Path<State>>>();
//		// put start state and its first path.
//		nonDominatedPathsMap.put(start, initializeFirstPathFromStart());

		startTimer();
//		System.out.println("plan() initialized");
		// compute the shortest path.
		computeShortestPath();
		
//		System.out.println("shortest path calculated.");
//		System.out.println("Remaining size of U : " + U.size());
		
		// dumping U, g and rhs to a *.states file under resources.
//		 dumpActualStatesToFile();
		
		//initialize fastly generate solution paths.
		solutionPaths = new CopyOnWriteArrayList<Path<Coordinate>>();
		generateMOPathsWithBackpointers();
//		System.err.println("# of found paths to eliminate: " + solutionPaths.size());
		eliminateDominatedPaths();
		stopTimer();
//		System.out.println("paths generated.");
//		System.out.println("# of solutions: " + solutionPaths.size());
		
		return solutionPaths;
	}

	private void eliminateDominatedPaths() {
		
		for (int i = 0; i < solutionPaths.size(); i++) {

			Path<Coordinate> path1 = solutionPaths.get(i);
			if (path1.getCost() == null) {
				this.calculatePathCost(path1);
			}

			for (int j = i+1; j < solutionPaths.size(); j++) {


				Path<Coordinate> path2 = solutionPaths.get(j);
				if (path2.getCost() == null) {
					this.calculatePathCost(path2);
				}

				if (path1.getCost().dominates(path2.getCost())) {
					solutionPaths.remove(path2);
					if(j>=0)
						j--;
				} else if (path2.getCost().dominates(path1.getCost())) {
					solutionPaths.remove(path1);
					if(i>=0)
						i--;
				} else if (path1.getCost().equals(path2.getCost())) {
					solutionPaths.remove(path2);
					if(j>=0)
						j--;
				}
			}
		}		
		
	}

	private void dumpActualStatesToFile() {
		// create a new map utils instance and sort g and rhs.
		// MapUtils<State, List<ObjectiveArray>> mapUtils = new MapUtils<State,
		// List<ObjectiveArray>>();
		// SortedMap<State, List<ObjectiveArray>> sortedG = mapUtils.sort(g);
		// SortedMap<State, List<ObjectiveArray>> sortedRhs =
		// mapUtils.sort(rhs);

		// dump U, sorted g and rhs values...
		// StateWriter.getWriter().writeStates("U:", U, "\ng:", sortedG,
		// "\nrhs:",
		// sortedRhs);
		StateWriter.getWriter().writeStates("U:", U, "\ng:", g, "\nrhs:", rhs);
		StateWriter.getWriter().writeDifferences(g, rhs);
	}

	protected abstract void startTimer();
	
	protected abstract void stopTimer();
	
	private void generateMOPathsWithBackpointers() {
		Queue<State> expandingStates = new LinkedList<State>();
		expandingStates.add(start);
		
//		int loopCounter = 0;
		
		while(!expandingStates.isEmpty()){
			
			State s = expandingStates.poll();
			
			if(s.getExpansion() > 100){
				System.err.println("found max for "+ s+", switch to another state.");
				continue;
			}
			
			s.setExpanded(true);
			s.incrExpansion();

//			if(s.get(0) == 49d && s.get(1) == 42d){
//				System.out.println();
//			}
//			if(s.get(0) == 49d && s.get(1) == 41d){
//				System.out.println();
//			}
			// if we reach goal state, break out.
			if (s.equals(goal)) {
				// expand remaining nodes.
				continue;
			}

			// If g(sStart) = Inf, then there is no known path - for this s,
			// continue on other states.
			if (MultiObjectiveUtils.equals(g.get(s),
					ObjectiveArray.positiveInfinityForMinimizedBehaviour())) {
				// search for other available states.
				continue;
			}

			// /////////////////// PHASE 1 ////////////////// //

			Map<State, List<ObjectiveArray>> nonDomSuccessors = new HashMap<State, List<ObjectiveArray>>();

			// the minRHS's list...
			List<List<ObjectiveArray>> minRHSs = new Vector<List<ObjectiveArray>>();
			minRHSs.add(ObjectiveArray.positiveInfinityForMinimizedBehaviour());

			for (State sPrime : succ(s)) {
				
//				if(!sPrime.isExpanded()){
				if(!s.getParents().keySet().contains(sPrime)){
					
				List<ObjectiveArray> rhsPrimes = MultiObjectiveUtils.sum(
						c(s, sPrime), g.get(sPrime));
				
					//if new one dominates all old ones.
					if (MultiObjectiveUtils.completeListDomination(rhsPrimes,
							minRHSs)) {
						nonDomSuccessors.clear();
						nonDomSuccessors.put(sPrime, rhsPrimes);

						minRHSs.clear();
						minRHSs.add(rhsPrimes);
					} else if (MultiObjectiveUtils.completeOneDomination(minRHSs,
							rhsPrimes)) {
						//if all old ones dominate the new one.
						continue;
					} else if (minRHSs.contains(rhsPrimes)) {
//						 continue;
						nonDomSuccessors.put(sPrime, rhsPrimes);
					} else {
						boolean isRhsPrimeDominatedByAMinRhs = false;
						for (List<ObjectiveArray> minRhs : minRHSs) {
							if (MultiObjectiveUtils.completelyDominates(minRhs,
									rhsPrimes)) {
								isRhsPrimeDominatedByAMinRhs = true;
							}
						}

						if (!isRhsPrimeDominatedByAMinRhs) {

							List<List<ObjectiveArray>> removalMinRHSs = new Vector<List<ObjectiveArray>>();

							for (List<ObjectiveArray> minRhs : minRHSs) {
								if (MultiObjectiveUtils.completelyDominates(
										rhsPrimes, minRhs)) {
									removalMinRHSs.add(minRhs);
								}
							}

							if (removalMinRHSs.size() > 0) {
								// remove from potential non dominator
								// successors.
								for (List<ObjectiveArray> removalMinRhs : removalMinRHSs) {

									List<State> removalStates = new Vector<State>();

									for (State state : nonDomSuccessors.keySet()) {
										if (nonDomSuccessors.get(state).equals(
												removalMinRhs)) {
											removalStates.add(state);
										}
									}

									for (State state : removalStates) {
										nonDomSuccessors.remove(state);
									}
								}
								// remove all from minRHSs.
								minRHSs.removeAll(removalMinRHSs);
							}

							// put rhs primes.
							nonDomSuccessors.put(sPrime, rhsPrimes);
							minRHSs.add(rhsPrimes);

						}
					}
				}
			}
			
			// ////////////// PHASE 2 //////////////// //

			for (State sPrime : nonDomSuccessors.keySet()) {
				
				//only occurs for start state.
				if(s.getParents().size() == 0){
					if(sPrime.getParents().size() == 0){
						//create a list of c's.
						List<ObjectiveArray> cs = new Vector<ObjectiveArray>();
						cs.add(c(s, sPrime));
						//put this list to parent's list.
						sPrime.getParents().put(s, cs);
						if(!expandingStates.contains(sPrime)){
							expandingStates.add(sPrime);
						}
					}else {
						System.err.println("This case never occurs, as i thought!");
					}
					
				} else {
					
					ObjectiveArray currentC = c(s, sPrime);
					
					//create a new list of objectives which aggregates all parents of s.
					List<ObjectiveArray> aggregatedParentObjectives = new Vector<ObjectiveArray>();
					//for each parent of s, calculate cumulative c values.
					for (Coordinate sParent : s.getParents().keySet()) {
						aggregatedParentObjectives.addAll(s.getParents().get(sParent));
					}
					
					//they have to be non-dom, always!
					List<ObjectiveArray> calculatedCsForSPrime = MultiObjectiveUtils
							.sum(currentC, aggregatedParentObjectives);

					if (sPrime.getParents().size() == 0) {
						sPrime.getParents().put(s, calculatedCsForSPrime);
						if (!expandingStates.contains(sPrime)) {
							expandingStates.add(sPrime);
						}
					} else {
						List<Coordinate> removalParents = new Vector<Coordinate>();
						boolean newSShouldBeAddedAsAParent = true;

						for (Coordinate sPrimeParent : sPrime.getParents()
								.keySet()) {

							List<ObjectiveArray> existingParentCs = sPrime
									.getParents().get(sPrimeParent);

							if (MultiObjectiveUtils.equals(existingParentCs,
									calculatedCsForSPrime)) {
								newSShouldBeAddedAsAParent = false;
								break;
							} else if (MultiObjectiveUtils.completelyDominates(
									existingParentCs, calculatedCsForSPrime)) {
								newSShouldBeAddedAsAParent = false;
								break;
							} else if (MultiObjectiveUtils.completelyDominates(
									calculatedCsForSPrime, existingParentCs)) {
								removalParents.add(sPrimeParent);
							} else {
								// we have a non-domination fight with an
								// existing parent, round 1, fight!

								List<ObjectiveArray> removalCalculatedCValues = new Vector<ObjectiveArray>();
								for (ObjectiveArray calculatedCForSPrime : calculatedCsForSPrime) {

									List<ObjectiveArray> removalObjArrays = new Vector<ObjectiveArray>();
									for (ObjectiveArray existingCForSPrime : existingParentCs) {

										if (calculatedCForSPrime
												.equals(existingCForSPrime)) {
											removalCalculatedCValues
													.add(calculatedCForSPrime);
											break;
										} else if (calculatedCForSPrime
												.dominates(existingCForSPrime)) {
											removalObjArrays
													.add(existingCForSPrime);
										} else if (existingCForSPrime
												.dominates(calculatedCForSPrime)) {
											removalCalculatedCValues
													.add(calculatedCForSPrime);
											break;
										}
//										else {
//											// round is tied, do nothing.
//										}

									}

									existingParentCs
											.removeAll(removalObjArrays);
									if (existingParentCs.size() == 0) {
										removalParents.add(sPrimeParent);
									}

								}

								calculatedCsForSPrime
										.removeAll(removalCalculatedCValues);
								if (calculatedCsForSPrime.size() == 0) {
									newSShouldBeAddedAsAParent = false;
								}

							}
						}

						// remove all removal parents from sPrime's existing
						// parents.
						for (Coordinate removalParent : removalParents) {
							sPrime.getParents().remove(removalParent);
						}

						if (newSShouldBeAddedAsAParent) {
							List<ObjectiveArray> aggregatedNonDomList;
							if (sPrime.getParents().get(s) != null) {
								aggregatedNonDomList = MultiObjectiveUtils
										.nonDominatedList(sPrime.getParents()
												.get(s), calculatedCsForSPrime);
							} else {
								aggregatedNonDomList = calculatedCsForSPrime;
							}
							sPrime.getParents().put(s, aggregatedNonDomList);

							if (!expandingStates.contains(sPrime)) {
								expandingStates.add(sPrime);
							}
						}
					}
				}
			}
			
			List<CoordObjArray> sPrimeSParents = new Vector<CoordObjArray>();
			
			for (State sPrime : nonDomSuccessors.keySet()) {
				Map<Coordinate, List<ObjectiveArray>> sPrimeParent = sPrime.getParents();
				if(sPrimeParent.containsKey(s)){
					sPrimeSParents.add(new CoordObjArray(sPrime, sPrimeParent.get(s)));
				}
			}
			
			for (int i = 0; i < sPrimeSParents.size(); i++) {
				for (int j = 0; j < sPrimeSParents.size(); j++) {
					if (i != j) {
						CoordObjArray first = sPrimeSParents.get(i);
						CoordObjArray second = sPrimeSParents.get(j);

						if (MultiObjectiveUtils.contains(first.getObjArray(),
								second.getObjArray())) {
							second.getCoord().getParents().remove(s);
							
							if(second.getCoord().getParents().isEmpty()){
								if(expandingStates.contains(second.getCoord())){
									expandingStates.remove(second.getCoord());
								}
							}
							
						}
					}
				}
			}
			
//			loopCounter++;
		}
		
//		System.out.println("Backpointers are set.");
		// reached to goal state and all backpointers are set. just follow them.
		
//		System.err.println("(23, 55) parents: " + getState(23d, 55d).getParents());
//		System.err.println("(23, 56) parents: " + getState(23d, 56d).getParents());
//		System.err.println("(24, 56) parents: " + getState(24d, 56d).getParents());
//		System.err.println("(25, 56) parents: " + getState(25d, 56d).getParents());
//		System.err.println("...");
//		System.err.println("(43, 56) parents: " + getState(43d, 56d).getParents());
//		System.err.println("obur koseye gecti");
//		System.err.println("(44, 56) parents: " + getState(44d, 56d).getParents());
//		System.err.println("(44, 55) parents: " + getState(44d, 55d).getParents());
//		System.err.println("(44, 54) parents: " + getState(44d, 54d).getParents());
//		System.err.println("(44, 53) parents: " + getState(44d, 53d).getParents());
//		System.err.println("(44, 52) parents: " + getState(44d, 52d).getParents());
//		System.err.println("(44, 51) parents: " + getState(44d, 51d).getParents());
//		System.err.println("(44, 50) parents: " + getState(44d, 50d).getParents());
//		System.err.println("(44, 49) parents: " + getState(44d, 49d).getParents());
//		System.err.println("(44, 48) parents: " + getState(44d, 48d).getParents());
//		System.err.println("(44, 47) parents: " + getState(44d, 47d).getParents());
//		System.err.println("(44, 46) parents: " + getState(44d, 46d).getParents());
//		System.err.println("(45, 46) parents: " + getState(45d, 46d).getParents());
//		System.err.println("(45, 45) parents: " + getState(45d, 45d).getParents());
//		System.err.println("------");
//		System.err.println("------");
//		System.err.println("------");
//		System.err.println("------");
//		System.err.println("------");
//		System.err.println("(49, 23) parents: " + getState(49d, 23d).getParents());
//		System.err.println("(49, 24) parents: " + getState(49d, 24d).getParents());
//		System.err.println("(49, 25) parents: " + getState(49d, 25d).getParents());
		
//		writeBackpointersToFile();
//		System.out.println("Parents' info dumped under /tmp");
		
//		System.err.println("(10, 8) parents: " + getState(10d, 8d).getParents());
//		System.err.println("(9, 8) parents: " + getState(9d, 8d).getParents());
//		System.err.println("(8, 8) parents: " + getState(8d, 8d).getParents());
//		System.err.println("(7, 8) parents: " + getState(7d, 8d).getParents());
//		System.err.println("(6, 8) parents: " + getState(6d, 8d).getParents());
		
		State goalState = this.getState(goal.get());
		for (Coordinate state : goalState.getParents().keySet()) {
			Path<Coordinate> path = new Path<Coordinate>();
			path.addFirst(goalState);
			pushToStack(path, state);
		}
	}
	
	private final class CoordObjArray{

		private Coordinate coord; 
		
		private List<ObjectiveArray> objArray;

		public CoordObjArray(Coordinate coord, List<ObjectiveArray> objArray) {
			this.coord = coord;
			this.objArray = objArray;
		}

		public Coordinate getCoord() {
			return coord;
		}

		public List<ObjectiveArray> getObjArray() {
			return objArray;
		}
		
	}
	
	protected abstract void writeBackpointersToFile();
	
	protected abstract void calculatePathCost(Path<Coordinate> drawnPath);
	
	private void pushToStack(Path<Coordinate> path, Coordinate s) {
		if (s.equals(start)) {
			path.addFirst(s);
			solutionPaths.add(path);
//			eliminateDominatedPaths();
			return;
		}

		if (s.getParents().size() == 1) {
			path.addFirst(s);
			pushToStack(path, (Coordinate) s.getParents().keySet().toArray()[0]);
		} else {
			for (Coordinate sPrime : s.getParents().keySet()) {
				Path<Coordinate> duplicatedPath = new Path<Coordinate>();
				duplicatedPath.addAll(path);

				duplicatedPath.addFirst(s);
//				pushToStack(duplicatedPath, (Coordinate) s.getParents().keySet().toArray()[0]);
				pushToStack(duplicatedPath, (Coordinate) sPrime);
			}
		}

	}

	public void updateVertex(State u) {

		if (!u.equals(goal)) {
			// find minimum non-dominated rhs'.
			List<ObjectiveArray> minRhs = ObjectiveArray
					.positiveInfinityForMinimizedBehaviour();

//			if(u.get(0) == 107d && u.get(1) == 106d){
//				System.err.println("107, 106 Founded on update vertex");
//			}
//			
//			if(u.get(0) == 108d && u.get(1) == 106d){
//				System.err.println("108, 106 Founded on update vertex");
//			}
			
			for (State sPrime : succ(u)) {
				
//				if(MultiObjectiveUtils.containsAnyDominatibleOA(g.get(sPrime))){
//					System.err.println("Founded! : "+ sPrime);
//				}
				
				List<ObjectiveArray> rhsPrimes = MultiObjectiveUtils.sum(
						c(u, sPrime), g.get(sPrime));

				// FIXME: This is the shortest way to calculate...
				// minRhs = MultiObjectiveUtils.nonDominatedList(rhsPrimes,
				// minRhs);
				// check that rhsPrimes completely dominates minRhs'.
				if(MultiObjectiveUtils.equals(minRhs, rhsPrimes)){
					continue;
				}else if (MultiObjectiveUtils.completelyDominates(rhsPrimes, minRhs)) {
					minRhs = rhsPrimes;
				} else if (MultiObjectiveUtils.completelyDominates(minRhs,
						rhsPrimes)) {
					// if minRhs completely dominates rhsPrimes, just keep
					// going...
					continue;
				} else {
					// third case occurs if two lists can not completely
					// dominate to each other...
					minRhs = MultiObjectiveUtils.nonDominatedList(minRhs,
							rhsPrimes);
					// minRhs = MultiObjectiveUtils.minByRisk(minRhs,
					// rhsPrimes);
				}
			}
			
			rhs.put(u, minRhs);
		}

//		if (U.contains(u)) {
			U.remove(u);
//		}

		if (
		// MultiObjectiveUtils.completelyDominates(g.get(u), rhs.get(u))
		// || MultiObjectiveUtils
		// .completelyDominates(rhs.get(u), g.get(u))
		// || MultiObjectiveUtils.aggregates(rhs.get(u), g.get(u))
		!MultiObjectiveUtils.equals(g.get(u), rhs.get(u)))

		{
			U.insert(u, calculateKey(u));
		}

		// if (!MultiObjectiveUtils.equals(g.get(u), rhs.get(u))) {
		// U.insert(u, calculateKey(u));
		// }
	}

	/**
	 * An exact cost function for the distance between two <i>neighboring</i>
	 * states. This function is undefined for non-neighboring states. The
	 * neighbor connectivity is determined by the pred() and succ() functions.
	 * 
	 * @param a
	 *            some initial state
	 * @param b
	 *            some final state
	 * @return the actual distance between the states.
	 */
	protected abstract ObjectiveArray c(State a, State b);

	/**
	 * An admissible heuristic function for the distance between two states. In
	 * actual use, the second vertex will always be the goal state.
	 * 
	 * The heuristic must follow these rules: 1) h(a, a) = 0 2) h(a, b) &lt;=
	 * c(a, c) + h(c, b) (where a and c are neighbors)
	 * 
	 * @param a
	 *            some initial state
	 * @param b
	 *            some final state
	 * @return the estimated distance between the states.
	 */
	protected abstract ObjectiveArray h(State a, State b);

	/**
	 * Returns the set of predecessor states to the specified state.
	 * 
	 * @param s
	 *            the specified state.
	 * @return A set of predecessor states.
	 */
	protected abstract Collection<State> pred(State s);

	/**
	 * Returns the set of successor states to the specified state.
	 * 
	 * @param s
	 *            the specified state.
	 * @return A set of successor states.
	 */
	protected abstract Collection<State> succ(State s);
	
	protected abstract State getState(double... d);

	/**
	 * Used to indicate that the distance from state A to state B has been
	 * changed from cOld to cNew, and needs to replanned in the next iteration.
	 * 
	 * @param u
	 *            some initial state
	 * @param v
	 *            some final state
	 * @param cOld
	 *            the old cost value
	 * @param cNew
	 *            the new cost value
	 */
	protected void flagChange(State u, State v, ObjectiveArray cOld,
			ObjectiveArray cNew) {

		if (cNew.dominates(cOld)
				|| !(cNew.dominates(cOld) || cOld.dominates(cNew))) {
			if (!u.equals(goal)) {
				List<ObjectiveArray> nonDominatedList = MultiObjectiveUtils
						.nonDominatedList(rhs.get(u),
								MultiObjectiveUtils.sum(cNew, g.get(v)));
				rhs.put(u, nonDominatedList);
			}
		} else if (MultiObjectiveUtils.equals(rhs.get(u),
				MultiObjectiveUtils.sum(cOld, g.get(v)))) {
			if (!u.equals(goal)) {
				List<ObjectiveArray> minRhs = ObjectiveArray
						.positiveInfinityForMinimizedBehaviour();

				for (State sPrime : succ(u)) {
					List<ObjectiveArray> rhsPrime = MultiObjectiveUtils.sum(
							c(u, sPrime), g.get(sPrime));

					if (MultiObjectiveUtils.completelyDominates(rhsPrime,
							minRhs)) {
						minRhs = rhsPrime;
					} else if (MultiObjectiveUtils.completelyDominates(minRhs,
							rhsPrime)) {
						// if minRhs completely dominates rhsPrimes, just keep
						// going...
						continue;
					} else {
						// third case occurs if two lists can not completely
						// dominate to each other...
						minRhs = MultiObjectiveUtils.nonDominatedList(minRhs,
								rhsPrime);
					}
				}
				rhs.put(u, minRhs);
			}
		} else {
			// System.out.println("----------The case occurs");
			// System.out.println("cNew: " + cNew);
			// System.out.println("cOld: " + cOld);
			// System.out.println("initial (u) : " + u);
			// System.out.println("final (v) : " + v);
		}

		updateVertex(u);
	}

	private Key calculateKey(State s) {

		// get the min(g(s), rhs(s)) : non-dominated objective array list.
		List<ObjectiveArray> nonDominatedMinList = MultiObjectiveUtils
				.nonDominatedList(g.get(s), rhs.get(s));
		// List<ObjectiveArray> nonDominatedMinList = MultiObjectiveUtils
		// .minByRisk(g.get(s), rhs.get(s));

		// generate the keys for this state.
		List<ObjectiveArray> k1 = MultiObjectiveUtils.sum(
				MultiObjectiveUtils.sum(h(start, s), km), nonDominatedMinList);
		List<ObjectiveArray> k2 = nonDominatedMinList;

		return new Key(k1, k2);
	}

	private void computeShortestPath() {

		int loopCount = 0;

//		while (!U.isEmpty()
//		 && (U.topKey().compareTo(calculateKey(start)) < 0
//
////		 !MultiObjectiveUtils.completelyDominates(calculateKey(start).objectivesListA,
////		 U.topKey().objectivesListA)
//
//		 ||
////		 !MultiObjectiveUtils.equals(rhs.get(start),g.get(start))
//		 MultiObjectiveUtils.completelyDominates(g.get(start),
//		 rhs.get(start)) || MultiObjectiveUtils
//		 .completelyDominates(rhs.get(start), g.get(start)))
//		) {
		
		
		while(
				!MultiObjectiveUtils.dominatesAll(calculateKey(start), U.topKeys())
//				!U.isEmpty()
				){

			Key kOld = U.topKey();
			State u = U.pop();
			
			Key kNew = calculateKey(u);
			if (kOld.compareTo(kNew) < 0) {
				// if (u.getInsertionCount() < 2) {
				U.insert(u, kNew);
				// u.inserted();
				// }
			} else if (MultiObjectiveUtils.completelyDominates(rhs.get(u),
					g.get(u))) {
				g.put(u, rhs.get(u));
				for (State s : pred(u)) {
					updateVertex(s);
				}
			}
			else if (MultiObjectiveUtils.completelyDominates(g.get(u),
					rhs.get(u))) {
				g.put(u, ObjectiveArray.positiveInfinityForMinimizedBehaviour());
				for (State s : pred(u)) {
					updateVertex(s);
				}
				updateVertex(u);
			} else
			// if(!MultiObjectiveUtils.equals(rhs.get(u), g.get(u)))
			{
//				 g.put(u, MultiObjectiveUtils.nonDominatedList(g.get(u),
//				 rhs.get(u)));
				g.put(u, rhs.get(u));
				 for (State s : pred(u)) {
				 updateVertex(s);
				 }
			}
			loopCount++;
		}
		
//		System.out.println("### LC: " + loopCount);

	}
	
	/**
	 * A simple wrapper to a HashMap that returns infinity if a match is not
	 * found. This cheaply implements the lookup behavior required by D*.
	 */
	public class ValueMap extends HashMap<State, List<ObjectiveArray>> {

		private static final long serialVersionUID = -3887177997092179096L;

		@Override
		public List<ObjectiveArray> get(Object key) {
			List<ObjectiveArray> res = super.get(key);
			if (res == null) {
				return ObjectiveArray.positiveInfinityForMinimizedBehaviour();
			} else if (res.size() == 0) {
				// return
				// ObjectiveArray.positiveInfinityForMinimizedBehaviour();
				return res;
			} else {
				return res;
			}
		}

		@Override
		public String toString() {
			String message = "[";
			int i = 0;
			for (State state : keySet()) {
				if (i == keySet().size() - 1) {
					message += state + " = " + get(state) + "]";
				} else {
					message += state + " = " + get(state) + "\n";
				}
				i++;
			}
			return message;
		}
	}
	
	public abstract long getTotalExecTime();

}
