package tr.edu.metu.ec.pp;

import tr.edu.metu.ec.pp.ga.Population;
import tr.edu.metu.ec.pp.world.Grid;
import tr.edu.metu.ec.pp.world.GridFactory;
import tr.edu.metu.ec.pp.world.GridFileReader;

public class MOMTGPP {

	public static void main(String[] args) {
		// create an instance of MOMTGPP
		MOMTGPP momtgpp = new MOMTGPP();
		// start the algorithm.
		momtgpp.start();
	}

	public void start() {
		// create the file reader and read content.
		GridFileReader gfReader = new GridFileReader();
		gfReader.readGridInput();
		// create the grid.
		GridFactory
				.createGrid(gfReader.getReadN(), gfReader.getReadLocations());

		// initialize the population.
		Population initialPopulation = new Population();

		// current population' s best fitness
		System.out.println("Best Fitness of initial population: "
				+ initialPopulation.findBestIndividual().getFitnessValue());
		// current population' s total fitness
		System.out.println("Total fitness of initial population: "
				+ initialPopulation.getTotalFitness());

		// print the first (random) best path.
		Grid.getInstance().print(initialPopulation.findBestIndividual());

		// evolve the population.
		Population finalPopulation = initialPopulation.evolve();
		//finally, syso weights.
		Grid.getInstance().printWeights(finalPopulation.findBestIndividual());
		

		// // final population' s best fitness.
		// System.out.println("Best Fitness of last population: "
		// + finalPopulation.findBestIndividual().getFitnessValue());
		// // current population' s total fitness
		// System.out.println("Total fitness of last population: "
		// + finalPopulation.getTotalFitness());
		//
		// // print the final (evolved) best path.
		// Grid.getInstance().print(finalPopulation.findBestIndividual());
	}

}
