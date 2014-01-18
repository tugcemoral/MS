package tr.edu.metu.ceng.ms.thesis.mogpp.core.ds;

import java.util.List;

import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.graph.DirectedAcylicGraph;
import tr.edu.metu.ceng.ms.thesis.mogpp.core.ga.Individual;

public class MOGPPlannerDAG {

	private DirectedAcylicGraph<Individual> dag;

	public MOGPPlannerDAG() {
		dag = new DirectedAcylicGraph<Individual>();
	}

	public void insert(Individual indvToInsert) {
		// add it into dag.
		boolean addNode = dag.addNode(indvToInsert);
		if (addNode) {
			// traverse all other vertices and make-up edges.
			for (Individual vertex : dag.vertices()) {
				if (!vertex.equals(indvToInsert)) {
					if (indvToInsert.getPath().getCost()
							.dominates(vertex.getPath().getCost())) {
						dag.addEdge(indvToInsert, vertex);
					} else if (vertex.getPath().getCost()
							.dominates(indvToInsert.getPath().getCost())) {
						dag.addEdge(vertex, indvToInsert);
					}
				}
			}
		}
	}

	public void insertAll(List<? extends Individual> list) {
		for (Individual individual : list) {
			this.insert(individual);
		}
	}

	public Individual get(int i) {
		Individual individual = null;
		try {
			individual = (Individual) dag.vertices().toArray()[i];
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			aioobe.printStackTrace();
		}
		return individual;
	}
	
	public List<Individual> topIndividuals(){
		return dag.getMinInDegrees();
	}
	
	public List<? extends Individual> topIndividuals(int number){
		return dag.getActualNumberOfMinInDegrees(number);
	}

	public void remove(Individual individual) {
		dag.removeNode(individual);
	}

	public int size() {
		return dag.size();
	}

	public void clear() {
		dag.clear();
	}

	public void removeAll(List<? extends Individual> topIndividuals) {
		for (Individual individual : topIndividuals) {
			this.remove(individual);
		}
	}

}
