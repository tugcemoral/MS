package tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.graph;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;

public class DirectedAcylicGraph<V extends Vertex> {

	private static final int INITIAL_CAPACITY = 512;

	/**
	 * A map from nodes in the graph to sets of outgoing edges. Each set of
	 * edges is represented by a map from edges to doubles.
	 */
	private final Map<V, Set<V>> mGraph;

	/**
	 * A map holds nodes and their set of incoming vertices. Notice that this
	 * map is against the essence of DAG. However, these incoming nodes should
	 * be cached somewhere and be used when removing an edge.
	 */
	// private final Map<V, Set<V>> reversedMGraph;

	// private PriorityQueue<TopologicInDegreeElement> topologicalQueue;

	private Map<Integer, List<V>> topologicalMap;

	public DirectedAcylicGraph() {
		mGraph = new HashMap<V, Set<V>>();
		// reversedMGraph = new HashMap<V, Set<V>>();
		// topologicalQueue = new PriorityQueue<TopologicInDegreeElement>(
		// INITIAL_CAPACITY, new TopologicalSorter());
		topologicalMap = new HashMap<Integer, List<V>>();

	}

	/**
	 * Adds a new node to the graph. If the node already exists, this function
	 * is a no-op.
	 * 
	 * @param node
	 *            The node to add.
	 * @return Whether or not the node was added.
	 */
	public boolean addNode(V node) {
		// If the node already exists, don't do anything.
		if (mGraph.containsKey(node))
			return false;

		// Otherwise, add the node with an empty set of outgoing edges.
		mGraph.put(node, new HashSet<V>());

		// FIXME: assume that initial node has 0 incoming degree.
		List<V> list = topologicalMap.get(0);
		if (list == null) {
			List<V> zeros = new Vector<V>();
			zeros.add(node);
			topologicalMap.put(0, zeros);
		} else {
			list.add(node);
		}

		return true;
	}

	public boolean removeNode(V node) {
		if (!mGraph.containsKey(node))
			return false;

		// int inDegreeBeforeRemoval = node.getInDegree();

		Set<V> tmpToVertices = new HashSet<V>();
		tmpToVertices.addAll(mGraph.get(node));

		// first, remove outgoing edges.
		for (V toVertex : tmpToVertices) {
			this.removeEdge(node, toVertex);
		}

		List<Vertex> tmpFromVertices = new Vector<Vertex>();
		tmpFromVertices.addAll(node.getFromVertices());

		// then, remove incoming edges.
		for (Vertex fromVertex : tmpFromVertices) {
			this.removeEdge((V) fromVertex, node);
		}

		// now, node's incoming degree must be 0.
		assert (node.getInDegree() == 0);

		// remove node from mGraph and topological map.
		mGraph.remove(node);
		topologicalMap.get(0).remove(node);

		return true;
	}

	/**
	 * Given a start node, and a destination, adds an arc from the source node
	 * to the destination. If an arc already exists, this operation is a no-op.
	 * If either endpoint does not exist in the graph, throws a
	 * NoSuchElementException.
	 * 
	 * @param src
	 *            The source node.
	 * @param dest
	 *            The destination node.
	 * @throws NoSuchElementException
	 *             If either the start or destination nodes do not exist.
	 */
	public void addEdge(V src, V dest) {
		/* Confirm both endpoints exist. */
		if (!mGraph.containsKey(src) || !mGraph.containsKey(dest))
			throw new NoSuchElementException("Both nodes must be in the graph.");

		// Add the edge and increase indegree of destination.
		mGraph.get(src).add(dest);
		
		if(topologicalMap.get(dest.getInDegree()) == null){
			System.err.println(dest.getInDegree());
		}
		
		// remove dest from upper level to lower one.
		topologicalMap.get(dest.getInDegree()).remove(dest);

		// add source as destination's incoming node.
		dest.addFromVertices(src);

		List<V> inDegreeList = topologicalMap.get(dest.getInDegree());
		if (inDegreeList == null) {
			List<V> newDegrees = new Vector<V>();
			newDegrees.add(dest);
			topologicalMap.put(dest.getInDegree(), newDegrees);
		} else {
			inDegreeList.add(dest);
		}

	}

	/**
	 * Removes the edge from src to dest from the graph. If the edge does not
	 * exist, this operation is a no-op. If either endpoint does not exist, this
	 * throws a NoSuchElementException.
	 * 
	 * @param src
	 *            The source node.
	 * @param dest
	 *            The destination node.
	 * @throws NoSuchElementException
	 *             If either node is not in the graph.
	 */
	public void removeEdge(V src, V dest) {
		/* Confirm both endpoints exist. */
		if (!mGraph.containsKey(src) || !mGraph.containsKey(dest))
			throw new NoSuchElementException("Both nodes must be in the graph.");

		mGraph.get(src).remove(dest);
		// remove dest from upper level to lower one.
		topologicalMap.get(dest.getInDegree()).remove(dest);
		if (topologicalMap.get(dest.getInDegree()).size() == 0)
			topologicalMap.remove(dest.getInDegree());

		dest.removeFromVertices(src);

		List<V> inDegreeList = topologicalMap.get(dest.getInDegree());
		if (inDegreeList == null) {
			List<V> newDegrees = new Vector<V>();
			newDegrees.add(dest);
			topologicalMap.put(dest.getInDegree(), newDegrees);
		} else {
			inDegreeList.add(dest);
		}

	}

	public List<V> getMinInDegrees() {
		if (!mGraph.isEmpty()) {
			int minDegree = 0;
			while (true) {
				if (topologicalMap.get(minDegree) != null) {
					break;
				}
				minDegree++;
			}

			return topologicalMap.get(minDegree);
		}
		return Collections.EMPTY_LIST;
	}

	public List<V> getActualNumberOfMinInDegrees(int number) {

		List<V> actualMinInDegrees = new Vector<V>();

		int keyNo = 0;
		int tryAndFind = 0;
		int remaining = number;
		while (actualMinInDegrees.size() < number) {
			List<V> correspondingMinInDegrees = getCorrespondingMinInDegrees(keyNo);
			if (correspondingMinInDegrees == Collections.EMPTY_LIST
					|| tryAndFind >= topologicalMap.size()) {
				break;
			} else if (correspondingMinInDegrees != null) {
				tryAndFind++;
				if (correspondingMinInDegrees.size() >= remaining) {
					actualMinInDegrees.addAll(correspondingMinInDegrees
							.subList(0, remaining));
				} else {
					actualMinInDegrees.addAll(correspondingMinInDegrees);
					remaining = remaining - correspondingMinInDegrees.size();
				}
			}
			keyNo++;
		}
		return actualMinInDegrees;
	}

	private List<V> getCorrespondingMinInDegrees(int degreeNo) {
		if (!mGraph.isEmpty()) {
			return topologicalMap.get(degreeNo);
		}
		return Collections.EMPTY_LIST;
	}

	public Set<V> vertices() {
		return mGraph.keySet();
	}

	/**
	 * Returns the number of nodes in the graph.
	 * 
	 * @return The number of nodes in the graph.
	 */
	public int size() {
		return mGraph.size();
	}

	/**
	 * Returns whether the graph is empty.
	 * 
	 * @return Whether the graph is empty.
	 */
	public boolean isEmpty() {
		return mGraph.isEmpty();
	}

	public void clear() {
		this.mGraph.clear();
		this.topologicalMap.clear();
	}

	@Override
	public String toString() {
		String str = "[";
		Map tmp = new HashMap(topologicalMap);

		for (Object key : tmp.keySet()) {
			str += key + " :: " + tmp.get(key).toString() + "\n";
		}
		return str + "]";
	}

	private final class TopologicInDegreeElement {

		private int inDegree;

		private List<V> vertices;

		public TopologicInDegreeElement(int inDegree, List<V> vertices) {
			this.inDegree = inDegree;
			this.vertices = vertices;
		}

		private int getInDegree() {
			return inDegree;
		}

		private List<V> getVertices() {
			return vertices;
		}

	}

	private final class TopologicalSorter implements
			Comparator<TopologicInDegreeElement> {

		@Override
		public int compare(TopologicInDegreeElement o1,
				TopologicInDegreeElement o2) {

			if (o1.getInDegree() < o2.getInDegree()) {
				return -1;
			} else if (o1.getInDegree() > o2.getInDegree()) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	public String stringifyGraph() {
		String str = "[";
		Map tmp = new HashMap(mGraph);

		for (Object key : tmp.keySet()) {
			str += key + " :: " + tmp.get(key).toString() + "\n";
		}
		return str + "]";
	}

}
