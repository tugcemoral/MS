package tr.edu.metu.ceng.ms.thesis.mogpp.core;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveArray;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path;
import tr.edu.metu.ceng.ms.thesis.modstarlite.ui.components.MOStaticMap;
import tr.edu.metu.ceng.ms.thesis.mogpp.core.ga.Individual;
import tr.edu.metu.ceng.ms.thesis.mogpp.core.ga.Population;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;

public class MOGeneticPathPlanner {

	private IntCoord start;

	private IntCoord goal;

	private long startTime;
	
	protected long stopTime;
	
	private static long totalExecTime = 0;
	
	private static Logger logger = Logger.getLogger(MOGeneticPathPlanner.class);

	public MOGeneticPathPlanner(MOStaticMap sm, IntCoord start, IntCoord goal) {
		this.start = start;
		this.goal = goal;
	}

	public List<Path<Coordinate>> plan() {
		// create the file reader and read content.
		// GridFileReader gfReader = new GridFileReader();
		// gfReader.readGridInput();
		// create the grid.
		// GridFactory
		// .createGrid(sm.size(0), sm.getObstacles());
		
		startTimer();
		
		// initialize the population.
		Population initialPopulation = new Population(start, goal);

//		// current population' s best fitness
//		System.out.println("Best Fitness of initial population: "
//				+ initialPopulation.findBestIndividual().getFitnessValue());
//		// current population' s total fitness
//		System.out.println("Total fitness of initial population: "
//				+ initialPopulation.getTotalFitness());

		// evolve the population.
		Population finalPopulation = initialPopulation.evolve();
		// finally, syso weights.
		Individual finalBestIndividual = finalPopulation.findBestIndividual();
		// Grid.getInstance().printWeights(finalBestIndividual);

		List<Individual> bestIndividuals = finalPopulation
				.findBestIndividuals();
//		System.out.println("Found best individuals size: "
//				+ bestIndividuals.size());

		List<Path<Coordinate>> solutions = new Vector<Path<Coordinate>>();
		List<ObjectiveArray> solutionCosts = new Vector<ObjectiveArray>();
		for (Individual individual : bestIndividuals) {
			if (!solutionCosts.contains(individual.getPath().getCost())) {
				solutions.add(individual.getPath());
				solutionCosts.add(individual.getPath().getCost());
			}
		}
		
		stopTimer();

		// solutions.add(finalBestIndividual.getPath());
		return solutions;
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
//		iterationCount++;
		totalExecTime += execTime;
	}

	public static long getTotalExecTime() {
		return totalExecTime;
	}

	public void updateGoal(IntCoord tmpGoal) {
		this.goal = tmpGoal;
	}
	
	public void updateStart(IntCoord newStart) {
		this.start = newStart;
	}

}
