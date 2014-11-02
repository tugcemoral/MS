package tr.edu.metu.ceng.ms.thesis.spea2.integration.core;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveArray;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path;
import tr.edu.metu.ceng.ms.thesis.modstarlite.ui.components.MOStaticMap;
import tr.edu.metu.ceng.ms.thesis.mogpp.core.exception.TempGoalShouldBeUpdatedException;
import tr.edu.metu.ceng.ms.thesis.mogpp.core.ga.Individual;
import tr.edu.metu.ceng.ms.thesis.mogpp.core.ga.Population;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;
import tr.edu.metu.ceng.ms.thesis.spea2.integration.env.MOSPEA2GeneticMap;
import tr.edu.metu.ceng.ms.thesis.spea2.main.SPEA2;
import tr.edu.metu.ceng.ms.thesis.spea2.util.JMException;

public class SPEA2PathPlanner {

	private IntCoord start;

	private IntCoord goal;

	private long startTime;
	
	protected long stopTime;
	
	private static long totalExecTime = 0;
	
	private static Logger logger = Logger.getLogger(SPEA2PathPlanner.class);

	public SPEA2PathPlanner(MOStaticMap sm, IntCoord start, IntCoord goal) {
		this.start = start;
		this.goal = goal;
	}

	public List<Path<Coordinate>> plan() {
		
		MOSPEA2GeneticMap map = MOSPEA2GeneticMap.getInstance();
		
		startTimer();
		
//		int maxIteration, int archiveSize, int populationSize,
//		double xoverRate, double xoverDistIndex, double mutationRate,
//		double mutationDistIndex
		
		SPEA2 spea2 = new SPEA2(map.getMaxIteration(), map.getArchiveSize(), map.getPopulationSize(), map.getCrossoverRate(), map.getCrossoverDistIndex(), map.getMutationRate(), map.getMutationDistIndex());
		// initialize the population.
		Population initialPopulation;
		SPEA2SolutionSet spea2SolutionSet;
		try {
			spea2SolutionSet = spea2.execute(start, goal);
		} catch (TempGoalShouldBeUpdatedException e) {
			return Collections.emptyList();
		} catch (JMException e) {
			e.printStackTrace();
			return Collections.emptyList();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}

//		// current population' s best fitness
//		System.out.println("Best Fitness of initial population: "
//				+ initialPopulation.findBestIndividual().getFitnessValue());
//		// current population' s total fitness
//		System.out.println("Total fitness of initial population: "
//				+ initialPopulation.getTotalFitness());

		
		// evolve the population.
//		Population finalPopulation = initialPopulation.evolve();
//		// finally, syso weights.
//		Individual finalBestIndividual = finalPopulation.findBestIndividual();
//		// Grid.getInstance().printWeights(finalBestIndividual);
//
//		List<Individual> bestIndividuals = finalPopulation
//				.findBestIndividuals();
//		System.out.println("Found best individuals size: "
//				+ bestIndividuals.size());

		List<SPEA2Solution> spea2Solutions = spea2SolutionSet.getSolutions();
		
//		System.out.println("-----------------");
		//constructs found paths for UI.
		List<Path<Coordinate>> solutions = new Vector<Path<Coordinate>>();
		List<ObjectiveArray> solutionCosts = new Vector<ObjectiveArray>();
		for (SPEA2Solution individual : spea2Solutions) {
			if (!solutionCosts.contains(individual.getPath().getCost())) {
				solutions.add(individual.getPath());
				solutionCosts.add(individual.getPath().getCost());
//				System.out.println(individual.getPath().getCost().toShortenString());
			}
		}
		
		stopTimer();

		// solutions.add(finalBestIndividual.getPath());
		return solutions;
	}
	
	protected void startTimer() {
		startTime = System.nanoTime();
//		logger.info("Timer Started.");
	}
	
	protected void stopTimer() {
		stopTime = System.nanoTime();
		long execTime = (long) ((stopTime - startTime) / Math.pow(10, 6));
//		logger.info("Timer Stopped, execution time: " + execTime + " ms");
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
