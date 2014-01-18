package tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.graph;

import java.util.List;
import java.util.Vector;

import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.Key;
import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.StateKey;

public class MODStarDAG<State> {

	private DirectedAcylicGraph<StateKey<State>> dag;

	public MODStarDAG() {
		dag = new DirectedAcylicGraph<StateKey<State>>();
	}

	public void insert(State s, Key k) {
		// create a new StateKey pair.
		StateKey<State> stateKey = new StateKey<State>(s, k);
		this.insert(stateKey);
	}

	public void insert(StateKey<State> stateKeyToInsert) {
		// add it into dag.
		boolean addNode = dag.addNode(stateKeyToInsert);
		if (addNode) {
			// traverse all other vertices and make-up edges.
			for (StateKey<State> vertex : dag.vertices()) {
				if (!vertex.equals(stateKeyToInsert)) {
					int compareTo = stateKeyToInsert.getK().compareTo(
							vertex.getK());
					if (compareTo < 0) {
						dag.addEdge(stateKeyToInsert, vertex);
					} else if (compareTo > 0) {
						dag.addEdge(vertex, stateKeyToInsert);
					}
				}
			}
		}
	}

	public List<Key> topKeys() {
		
		List<StateKey<State>> minInDegrees = dag.getMinInDegrees();

		// create the list of keys wrt minInDegrees.
		List<Key> minKeys = new Vector<Key>();

		for (StateKey<State> stateKey : minInDegrees) {
			minKeys.add(stateKey.getK());
		}

		return minKeys;
	}

	public Key topKey() {
		if (!dag.isEmpty()) {
			return this.topKeys().get(0);
		}
		return null;
	}

	public void clear() {
		dag.clear();
	}

	public int size() {
		return dag.size();
	}

	public State pop() {
		if (!dag.isEmpty()) {
			StateKey<State> stateKey = dag.getMinInDegrees().get(0);
			dag.removeNode(stateKey);
			return stateKey.getS();
		}
		return null;
	}

	public boolean isEmpty() {
		return dag.isEmpty();
	}

	public void remove(State s, Key k) {
		StateKey<State> sk = new StateKey<State>(s, k);
		this.remove(sk);
	}

	public void remove(StateKey<State> sk) {
		dag.removeNode(sk);
	}
	
	/**
	 * Removes all states corresponding by given state <code>s</code>
	 * @param s
	 */
	public void remove(State s){
		List<StateKey> removalStateKeys = new Vector<StateKey>(); 
		
		for (StateKey<State> vertex : dag.vertices()) {
			if(vertex.getS().equals(s)){
				removalStateKeys.add(vertex);
				break;
			}
		}
		
//		if(removalStateKeys.size() > 1){
//			System.out.println("asd");
////			System.err.println("Removal Vertex represents two states");
//		}
		
		for (StateKey removalStateKey : removalStateKeys) {
			dag.removeNode(removalStateKey);
		}
	}

	public List<StateKey<State>> topStateKeys() {
		return dag.getMinInDegrees();
	}

	public boolean contains(StateKey<State> stateKey) {
		return dag.vertices().contains(stateKey);
	}
	
	public String toString() {
		return dag.stringifyGraph();
	}

}
