package tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.queue;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.Key;
import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.StateKey;

public class MODStarQueue<State> {

	public static final int INITIAL_CAPACITY = 1024;

	@SuppressWarnings("rawtypes")
	// private PriorityQueue<List<StateKey>> pQueue;
	private List<List<StateKey>> pQueue;

	@SuppressWarnings({ "rawtypes" })
	public MODStarQueue() {
		// pQueue = new PriorityQueue<List<StateKey>>(INITIAL_CAPACITY,
		// new StateKeyComparator());
		pQueue = new CopyOnWriteArrayList<List<StateKey>>();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void remove(State s) {
		
		int[] containsIndexes = this.contains(s);
		
		if(containsIndexes[0] != -1) {
			pQueue.get(containsIndexes[0]).remove(containsIndexes[1]);
			if(pQueue.get(containsIndexes[0]).size() == 0) {
				pQueue.remove(containsIndexes[0]);
			}
		}

//		if (!pQueue.isEmpty()) {
//			for (Iterator iterator = pQueue.iterator(); iterator.hasNext();) {
//				List<StateKey> nextStateKeys = (List<StateKey>) iterator.next();
//
//				boolean removalFound = false;
//				int removalIndex = 0;
//				for (int i = 0; i < nextStateKeys.size(); i++) {
//					StateKey stateKey = nextStateKeys.get(i);
//					if (stateKey.getS().equals(s)) {
//						removalIndex = i;
//						removalFound = true;
//						break;
//					}
//				}
//
//				if (removalFound) {
//					nextStateKeys.remove(removalIndex);
//					// if list is finalized, remove from queue.
//					if (nextStateKeys.size() == 0) {
//						pQueue.remove(nextStateKeys);
//					}
//					break;
//				}
//			}
//		}
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String toString() {
		String str = "[";
//		PriorityQueue tmp = new PriorityQueue(pQueue);
		List tmp = new CopyOnWriteArrayList(pQueue);

		for(int i=0; i< tmp.size(); i++) {
			str += tmp.get(i).toString();
			if(i != tmp.size()-1) {
				str += ",\n";
			}
		}
		
//		while (!tmp.isEmpty()) {
//			str += tmp.get(0).toString();
//			if (!tmp.isEmpty())
//				str += ",\n";
//		}

		return str + "]";
	}
	
	public int[] contains(State s) {
		int[] containedIndex = new int[] { -1, -1 };

		for (int i = 0; i < pQueue.size(); i++) {
			List<StateKey> stateKeys = pQueue.get(i);

			for (int j = 0; j < stateKeys.size(); j++) {
				StateKey stateKey = stateKeys.get(j);
				if (stateKey.getS().equals(s)) {
					containedIndex[0] = i;
					containedIndex[1] = j;
					return containedIndex;
				}
			}
		}
		return containedIndex;
	}

	@SuppressWarnings("rawtypes")
	public void insert(State s, Key k) {

		// create a new StateKey pair.
		StateKey<State> stateKeyToInsert = new StateKey<State>(s, k);

		//if this state is already in the queue, remove it first.
		int[] containsIndexes = this.contains(s);
		if (containsIndexes[0] != -1) {
			pQueue.get(containsIndexes[0]).remove(containsIndexes[1]);
			if(pQueue.get(containsIndexes[0]).size() == 0) {
				pQueue.remove(containsIndexes[0]);
			}
		}
		
		// very first insertion.
		if (pQueue.size() == 0) {
			// create a new list and dump it.
			List<StateKey> list = new Vector<StateKey>();
			list.add(stateKeyToInsert);
			pQueue.add(list);
		} else {

			for (int n = 0; n < pQueue.size(); n++) {
				// get current state keys list.
				List<StateKey> currentStateKeys = pQueue.get(n);

				// counters that new key dominates or not dominates the current
				// one(s)
				// int dominateCount = 0;
				// int notDominateCount = 0;

				List<StateKey> dominates = new Vector<StateKey>();
				List<StateKey> notDominates = new Vector<StateKey>();
				List<StateKey> nonDominatibles = new Vector<StateKey>();

				for (int i = 0; i < currentStateKeys.size(); i++) {
					// get current state key to compare with newish one.
					StateKey currentStateKey = currentStateKeys.get(i);

					int compareTo = stateKeyToInsert.getK().compareTo(
							currentStateKey.getK());

					if (compareTo < 0) {
						dominates.add(currentStateKey);
					} else if (compareTo > 0) {
						notDominates.add(currentStateKey);
					} else {
						nonDominatibles.add(currentStateKey);
					}
				}

				// dominates all of them.
				if (dominates.size() == currentStateKeys.size()) {
					// add new comer into nth position.
					List<StateKey> newComerList = new Vector<StateKey>();
					newComerList.add(stateKeyToInsert);
					pQueue.add(n, newComerList);
					break;
				} else if (notDominates.size() == currentStateKeys.size()) {
					// it must be inserted as last element.
					if (n == pQueue.size() - 1) {
						List<StateKey> newComerList = new Vector<StateKey>();
						newComerList.add(stateKeyToInsert);
						pQueue.add(newComerList);
						break;
					} else {
						continue;
					}
				} else {
					// means that we have a non-dominatible issue, insert to
					// current list.
					// but before, we must re-insert both dominates and
					// notdominates.
					currentStateKeys.removeAll(dominates);
					currentStateKeys.removeAll(notDominates);

					for (StateKey stateKey : dominates) {
						this.insert((State) stateKey.getS(), stateKey.getK());
					}
					for (StateKey stateKey : notDominates) {
						this.insert((State) stateKey.getS(), stateKey.getK());
					}

					// finally add new one to current.
					currentStateKeys.add(stateKeyToInsert);
					break;
				}

			}

		}
	}

	public Key topKey() {
		if (!pQueue.isEmpty()) {
			return pQueue.get(0).get(0).getK();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public List<Key> topKeys() {
		// get top state keys...
		List<StateKey> topStateKeys = pQueue.get(0);

		// create a new topKeys list and fill it.
		List<Key> topKeys = new Vector<Key>();
		for (StateKey stateKey : topStateKeys) {
			topKeys.add(stateKey.getK());
		}

		return topKeys;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public State pop() {
		if (!pQueue.isEmpty()) {
			// // remove empty lists from head.
			// while ((pQueue.peek().size() == 0)) {
			// pQueue.poll();
			// }

			List<StateKey> topMostStateKeys = pQueue.get(0);
			if (topMostStateKeys.size() == 1) {
				// throw out topmost and return its state...
				pQueue.remove(0);
				return (State) topMostStateKeys.get(0).getS();
			} else {
				// just remove the returning object.
				return (State) topMostStateKeys.remove(0).getS();
//				return (State) topMostStateKeys.get(0).getS();
			}
		}
		return null;
	}

	public boolean isEmpty() {
		return pQueue.isEmpty();
	}

	public int size() {
		return pQueue.size();
	}

	public void clear() {
		pQueue.clear();
	}

}
