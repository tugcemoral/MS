/*
 *  The MIT License
 * 
 *  Copyright 2010 Prasanna Velagapudi <psigen@gmail.com>.
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 * 
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 * 
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package tr.edu.metu.ceng.ms.thesis.robotutils.planning.optimized;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import tr.edu.metu.ceng.ms.thesis.robotutils.util.DoubleUtils;
import tr.edu.metu.ceng.ms.thesis.robotutils.util.PriorityQueue;

/**
 * This class implements the optimized D*-lite algorithm exactly as described in
 * [Koenig 2002]. D*-lite is an incremental variant of the A* search algorithm,
 * meaning that it can cheaply update itself to account for new obstacles.
 * 
 * Source: Koenig, S. and Likhachev, M. 2002. D*lite. In Eighteenth National
 * Conference on Artificial Intelligence (Edmonton, Alberta, Canada, July 28 -
 * August 01, 2002). R. Dechter, M. Kearns, and R. Sutton, Eds. American
 * Association for Artificial Intelligence, Menlo Park, CA, 476-483
 * 
 * @author Prasanna Velagapudi <psigen@gmail.com>
 */
public abstract class OptimizedDStarLite<State> {

	/**
	 * Defines a suitably large initial capacity for internal data structures to
	 * avoid Java's heuristics reallocating up from scratch.
	 */
	public static final int INITIAL_CAPACITY = 1024;

	protected State startState;

	protected final State goalState;

	private final ValueMap rhs = new ValueMap();

	private final ValueMap g = new ValueMap();

	private final KeyQueue U = new KeyQueue();

	private double _Km = 0;

	/**
	 * Initializes a D* search object with the specified start and goal states.
	 * This constructor roughly corresponds to the <i>initialize()<i> function
	 * in the pseudocode of the original paper.
	 * 
	 * @param start
	 *            the desired start state
	 * @param goal
	 *            the desires end state
	 */
	public OptimizedDStarLite(State start, State goal) {

		this.startState = start;
		this.goalState = goal;

		this.rhs.put(goalState, 0.0);
		U.insert(goalState, new Key(h(startState, goalState), 0));
	}

	/**
	 * Recomputes the lowest cost path through the map, taking into account any
	 * changes in start location and edge costs. If no path can be found this
	 * will return an empty list.
	 * 
	 * @return a list of states from start to goal
	 */
	public List<State> plan() {

		LinkedList<State> path = new LinkedList<State>();
		State s = startState;
		path.add(s);

		computeShortestPath();
		while (!s.equals(goalState)) {
			// If rhs(sStart) = Inf, then there is no known path
			if (Double.isInfinite(rhs.get(s))) {
				return Collections.emptyList();
			}

			Collection<State> succs = succ(s);
			Double minRhs = Double.POSITIVE_INFINITY;
			State minS = null;

			for (State sPrime : succs) {
				double rhsPrime = c(s, sPrime) + g.get(sPrime);
				if (rhsPrime < minRhs) {
					minRhs = rhsPrime;
					minS = sPrime;
				}
			}

			s = minS;
			path.addLast(s);
		}

		return path;
	}
	/**
	 * Change the start location after initialization. Used to cheaply move
	 * robot along path without needing to replan.
	 * 
	 * @param s
	 *            the new start state
	 */
	public void updateStart(State s) {

		State sLast = startState;
		startState = s;

		_Km += h(sLast, startState);
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
	protected abstract double c(State a, State b);
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
	protected void flagChange(State u, State v, double cOld, double cNew) {

		if (cOld > cNew) {
			if (!u.equals(goalState)) {
				rhs.put(u, Math.min(rhs.get(u), cNew + g.get(v)));
			}
		} else if (DoubleUtils.equals(rhs.get(u), cOld + g.get(v))) {
			if (!u.equals(goalState)) {
				double minRhs = Double.POSITIVE_INFINITY;

				for (State sPrime : succ(u)) {
					double rhsPrime = c(u, sPrime) + g.get(sPrime);
					if (rhsPrime < minRhs) {
						minRhs = rhsPrime;
					}
				}

				rhs.put(u, minRhs);
			}
		}

		updateVertex(u);
	}

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
	protected abstract double h(State a, State b);
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

	Key calculateKey(State s) {

		double k1 = Math.min(g.get(s), rhs.get(s)) + h(startState, s) + _Km;
		double k2 = Math.min(g.get(s), rhs.get(s));

		return new Key(k1, k2);
	}

	void computeShortestPath() {

		while (!U.isEmpty()
				&& (U.topKey().compareTo(calculateKey(startState)) < 0 || rhs
						.get(startState) > g.get(startState))) {

			State u = U.top();
			Key kOld = U.topKey();
			Key kNew = calculateKey(u);

			if (kOld.compareTo(kNew) < 0) {
				U.update(u, kNew);
			} else if (g.get(u) > rhs.get(u)) {
				g.put(u, rhs.get(u));
				U.remove(u);

				for (State s : pred(u)) {
					if (!s.equals(goalState)) {
						rhs.put(s, Math.min(rhs.get(s), c(s, u) + g.get(u)));
					}

					updateVertex(s);
				}
			} else {
				double gOld = g.get(u);
				g.put(u, Double.POSITIVE_INFINITY);

				Collection<State> preds = pred(u);
				preds.add(u);

				for (State s : preds) {
					if (DoubleUtils.equals(rhs.get(s), c(s, u) + gOld)) {
						if (!s.equals(goalState)) {
							double minRhs = Double.POSITIVE_INFINITY;

							for (State sPrime : succ(s)) {
								double rhsPrime = c(s, sPrime) + g.get(sPrime);
								if (rhsPrime < minRhs) {
									minRhs = rhsPrime;
								}
							}

							rhs.put(s, minRhs);
						}
					}

					updateVertex(s);
				}
			}
		}
	}

	void updateVertex(State u) {

		if (!DoubleUtils.equals(g.get(u), rhs.get(u)) && U.contains(u)) {
			U.update(u, calculateKey(u));
		} else if (!DoubleUtils.equals(g.get(u), rhs.get(u))
				&& !U.contains(u)) {
			U.insert(u, calculateKey(u));
		} else if (DoubleUtils.equals(g.get(u), rhs.get(u)) && U.contains(u)) {
			U.remove(u);
		}
	}

	/**
	 * A tuple with two components used to assign priorities to states in the D*
	 * search. Keys are compared according to a lexical ordering, e.g. k &lt k'
	 * iff either k1 &lt k'1 or (k1 = k'1 and k2 &lt k'2).
	 */
	final class Key implements Comparable<Key> {

		final double a;
		final double b;

		public Key(double a, double b) {

			this.a = a;
			this.b = b;
		}

		public int compareTo(Key that) {

			if (this.a < that.a) {
				return -1;
			} else if (this.a > that.a) {
				return 1;
			} else {
				if (this.b < that.b) {
					return -1;
				} else if (this.b > that.b) {
					return 1;
				} else {
					return 0;
				}
			}
		}

		@Override
		public String toString() {
			return "<" + a + "," + b + ">";
		}
	}

	/**
	 * A simple wrapper to a priority queue that internally stores a tuple of
	 * State and Key with the correct comparison and equals operators. This
	 * cheaply implements the lookup behavior expected by D*.
	 */
	final class KeyQueue {

		private PriorityQueue<StateKey> _queue;

		public KeyQueue() {
			_queue = new PriorityQueue(INITIAL_CAPACITY,
					new StateKeyComparator());
		}

		public boolean contains(State s) {
			return _queue.contains(new StateKey(s, null));
		}

		public void insert(State s, Key k) {
			_queue.add(new StateKey(s, k));
		}

		public boolean isEmpty() {
			return _queue.isEmpty();
		}

		public void remove(State s) {
			_queue.remove(new StateKey(s, null));
		}

		public State top() {
			return _queue.peek().s;
		}

		public Key topKey() {
			return _queue.peek().k;
		}

		@Override
		public String toString() {
			String str = "[";
			PriorityQueue tmp = new PriorityQueue(_queue);

			while (!tmp.isEmpty()) {
				str += tmp.poll().toString();
				if (!tmp.isEmpty())
					str += ",";
			}

			return str + "]";
		}

		public void update(State s, Key k) {
			_queue.update(new StateKey(s, k));
		}

		private final class StateKey {

			final State s;
			Key k;

			public StateKey(State s, Key k) {
				this.s = s;
				this.k = k;
			}

			@Override
			@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
			public boolean equals(Object obj) {
				// We do a dangerous unchecked comparison (no null-test, etc.)
				// because this is a private class and should be well-behaved.
				StateKey that = (StateKey) obj;
				return (this.s.equals(that.s));
			}

			@Override
			public int hashCode() {
				int hash = 7;
				hash = 53 * hash + (this.s != null ? this.s.hashCode() : 0);
				return hash;
			}

			@Override
			public String toString() {
				return "[" + s.toString() + "," + k.toString() + "]";
			}
		}

		private final class StateKeyComparator implements Comparator<StateKey> {

			public final int compare(StateKey o1, StateKey o2) {
				// Lowest key = highest priority (reversing lexical ordering)
				return o1.k.compareTo(o2.k);
			}
		}
	}

	/**
	 * A simple wrapper to a HashMap that returns infinity if a match is not
	 * found. This cheaply implements the lookup behavior required by D*.
	 */
	final class ValueMap extends HashMap<State, Double> {

		@Override
		public Double get(Object key) {

			@SuppressWarnings("element-type-mismatch")
			Double res = super.get(key);

			if (res == null) {
				return Double.POSITIVE_INFINITY;
			} else {
				return res;
			}
		}
	}
}
