package tr.edu.metu.ceng.ms.thesis.mogpp.core.ga;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveArray;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path;
import tr.edu.metu.ceng.ms.thesis.modstarlite.util.MultiObjectiveUtils;
import tr.edu.metu.ceng.ms.thesis.mogpp.core.ds.MOGPPlannerDAG;
import tr.edu.metu.ceng.ms.thesis.mogpp.core.env.MOGeneticMap;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;

public class Population {

	/**
	 * Individuals list of population.
	 */
	// private List<Individual> individuals;

	private MOGPPlannerDAG individualsDAG;

	/**
	 * Random Number generator
	 */
	private Random randomGenerator;

	/**
	 * Total fitness value of the population.
	 */
	private ObjectiveArray totalFitness;

	private ObjectiveArray bestTotalFitness = ObjectiveArray.SINGLE_ZERO;

	// private double totalFitness;

	public Population(Coordinate start, Coordinate goal) {
		MOGeneticMap gmap = MOGeneticMap.getInstance();
		// individuals = new Individual[POP_SIZE];
		// individuals = new Vector<Individual>();
		individualsDAG = new MOGPPlannerDAG();
		
		// init population
		for (int i = 0; i < gmap.getPopulationSize(); i++) {
			Individual individual = new Individual();
			individual.generateRandomPath((int) start.get(0),
					(int) start.get(1), (int) goal.get(0), (int) goal.get(1));
			getIndividualsDAG().insert(individual);
		}
		// evaluate current population.
		this.evaluate(getIndividualsDAG().size());
	}

	/**
	 * The main evolution loop.
	 * 
	 * @return
	 */
	public Population evolve() {
		double lastFitnessValue = 0.0d;

		// evolve through maximum iterations.
		MOGeneticMap gMap = MOGeneticMap.getInstance();
		for (int i = 1; i <= gMap.getMaxIteration(); i++) {
			// create a new individuals list.
			MOGPPlannerDAG newPopulation = new MOGPPlannerDAG();
			newPopulation.insertAll(getElites());

			// get elites from existing population to new population.
			// getIndividualsDAG().insertAll(getElites());

			double[] rouletteWheel = createRouletteWheel();

			// build new Population
			while (newPopulation.size() < gMap.getPopulationSize()) {
				boolean crossovered = false;
				// make a roulette wheel selection and get two parents.
				List<Individual> parents = this
						.rouletteWheelSelection(rouletteWheel);

				// do the crossover.
				if (getRandomGenerator().nextDouble() < gMap.getCrossoverRate()) {
					// actually, they are offsprings.
					parents = this.crossover(parents);
					crossovered = true;
				}
				// do the mutation for each parent / offspring
				if (getRandomGenerator().nextDouble() < gMap.getMutationRate()) {
					parents.set(0, this.mutate(parents.get(0)));
					// parents.get(0).mutate();
				}
				if (getRandomGenerator().nextDouble() < gMap.getMutationRate()) {
					parents.set(1, this.mutate(parents.get(1)));
					// parents.get(1).mutate();
				}

				if (!crossovered) {
					// re-create new ones to avoid dag confliction
					parents.set(0, new Individual(parents.get(0).getPath()));
					parents.set(1, new Individual(parents.get(1).getPath()));
				}

				// add to generated offsprings / parents to new population
				newPopulation.insertAll(parents);

			}
			this.setIndividualsDAG(newPopulation);

			// evaluate the new population (new individuals)
			ObjectiveArray currentTotalFitness = this.evaluate(getIndividualsDAG().size());

			// get the best individual of this population.
			// Individual bestIndividual =
			Individual bestIndividual = this.findBestIndividual();

			if (getBestTotalFitness().dominates(currentTotalFitness)) {
				// current population' s best fitness
//				System.out.println("Best Fitness of " + i + "th population: "
//						+ bestIndividual.getFitnessValue());
//				// current population' s total fitness
//				System.out.println("Total fitness of " + i + "th population: "
//						+ getTotalFitness());
				setBestTotalFitness(currentTotalFitness);
			}

		}
		return this;
	}

	private Individual mutate(Individual individual) {

		Individual mutatedIndv = new Individual(individual.getPath());

		// create a random generator.
		Random nextCellRndGen = new Random();
		// get a random cell on the path.
		Coordinate cell = mutatedIndv.getPath().get(
				nextCellRndGen.nextInt(mutatedIndv.getPath().size()));

		// extract the path from start cell to corresponding cell.
		Path subPath = mutatedIndv.getSubPath(mutatedIndv.getStartCell(), cell);

		// try to find an alternative sub-path from this random cell to goal
		// cell.
		mutatedIndv.generateRandomPath((int) cell.get(0), (int) cell.get(1),
				(int) individual.getGoalCell().get(0), (int) individual
						.getGoalCell().get(1));

		subPath.addLast(mutatedIndv.getPath().getRoute());

		mutatedIndv.setPath(subPath);
		mutatedIndv.calculatePathCost();
		mutatedIndv.evaluate();

		return mutatedIndv;
	}

	public List<Individual> findBestIndividuals() {
		return getIndividualsDAG().topIndividuals();
	}

	public Individual findBestIndividual() {
		List<? extends Individual> topIndividuals = getIndividualsDAG()
				.topIndividuals();
		if (topIndividuals.equals(Collections.EMPTY_LIST)) {
			return null;
		}
		return topIndividuals.get(0);

		// int idxMax = 0 /* , idxMin = 0 */;
		// double currentMax = 0.0d;
		// // double currentMin = Double.MAX_VALUE;
		// double currentVal;
		//
		// for (int i = 0; i < this.getIndividualsDAG().size(); i++) {
		// currentVal = getIndividualsDAG().get(i).getFitnessValue();
		// // if (currentMax < currentMin) {
		// // currentMax = currentMin = currentVal;
		// // idxMax /*= idxMin */ = i;
		// // }
		// if (currentVal > currentMax) {
		// currentMax = currentVal;
		// idxMax = i;
		// }
		// // if (currentVal < currentMin) {
		// // currentMin = currentVal;
		// // idxMin = i;
		// // }
		// }
		//
		// // return m_population[idxMin]; // minimization
		// return getIndividualsDAG().get(idxMax); // maximization
	}

	public ObjectiveArray getTotalFitness() {
		return totalFitness;
	}

	public List<Individual> rouletteWheelSelection(double[] rouletteWheel) {
		// create the selected individuals list.
		List<Individual> selectedIndividuals = new Vector<Individual>();

		do {
			// remove selected individuals if selected before.
			selectedIndividuals.clear();
			for (int k = 0; k < 2; k++) {
				int index = 0;
				// generate a number between 0 and 1.
				double randNum = getRandomGenerator().nextDouble();
				for (int i = 0; i < rouletteWheel.length; i++) {
					if (randNum < rouletteWheel[i]) {
						index = i - 1;
						break;
					}
				}
				selectedIndividuals.add(this.getIndividualsDAG().get(index));
			}

		} while (!selectedIndividuals.get(0).intersects(
				selectedIndividuals.get(1)));

		return selectedIndividuals;
	}

	private double[] createRouletteWheel() {
		// create the roulette wheel array. Note that its parallel with
		// individuals list.
		double[] rouletteWheel = new double[this.getIndividualsDAG().size() + 1];
		double value = 0d;
		rouletteWheel[0] = value;
		for (int i = 0; i < this.getIndividualsDAG().size(); i++) {

			ObjectiveArray fitnessValue = this.getIndividualsDAG().get(i)
					.getFitnessValue();
			double tmpValue = 0.0;
			for (int j = 0; j < fitnessValue.size(); j++) {
				double v1 = fitnessValue.get(j).getValue();
				double vT = getTotalFitness().get(j).getValue();
				tmpValue += (v1 / ((vT > 0) ? vT : 1));
			}
			value += tmpValue;
			rouletteWheel[i + 1] = value;
		}
		return rouletteWheel;
	}

	private List<Individual> crossover(List<Individual> parents) {
		// Individual[] newIndiv = new Individual[2];
		List<Individual> offsprings = new Vector<Individual>();

		// get the intersection cell of parents.
		Coordinate intersectedCell = parents.get(0).getAnyIntersectionCellWith(
				parents.get(1));
		
		if(intersectedCell != null){
			

		// extract the sub paths according to found intersection cell.
		Path firstSubPathOfFirstParent = parents.get(0).getSubPath(
				parents.get(0).getStartCell(), intersectedCell);
		Path secondSubPathOfSecondParent = parents.get(1).getSubPath(
				intersectedCell, parents.get(1).getGoalCell());

		// create the new offspring with sub paths and add it to corresponding
		// list.
		Individual firstOffspring = new Individual(firstSubPathOfFirstParent,
				secondSubPathOfSecondParent);
		offsprings.add(firstOffspring);

		// extract the sub paths according to found intersection cell.
		Path firstSubPathOfSecondParent = parents.get(1).getSubPath(
				parents.get(1).getStartCell(), intersectedCell);
		Path secondSubPathOfFirstParent = parents.get(0).getSubPath(
				intersectedCell, parents.get(0).getGoalCell());

		// create the new offspring with sub paths and add it to corresponding
		// list.
		Individual secondOffspring = new Individual(firstSubPathOfSecondParent,
				secondSubPathOfFirstParent);
		offsprings.add(secondOffspring);

		return offsprings;
		}else {
			return parents;
		}
	}

	private ObjectiveArray evaluate(int populationSize) {
		// initialize the total fitness function value.
		this.setTotalFitness(ObjectiveArray.SINGLE_ZERO);
		// this.setTotalFitness(0d);
		for (int i = 0; i < populationSize; i++) {
			this.setTotalFitness(MultiObjectiveUtils.sum(
					this.getTotalFitness(), getIndividualsDAG().get(i)
							.evaluate()));
		}
		return this.getTotalFitness();
	}

	private List<? extends Individual> getElites() {
		// create the elites list.
		// List<Individual> elites = new Vector<Individual>();
		// find elites and add to elites list.

		List<? extends Individual> topIndividuals = getIndividualsDAG()
				.topIndividuals(MOGeneticMap.getInstance().getElitism_k());
		getIndividualsDAG().removeAll(topIndividuals);
		return topIndividuals;

		// for (int i = 0; i < MOGeneticMap.getInstance().getElitism_k(); i++) {
		// // get the best individual and add it to elites list.
		// Individual elite = this.findBestIndividual();
		// if(elite == null){
		// break;
		// }
		// elites.add(elite);
		// // remove the individual to get next elite.
		// this.removeIndividual(elite);
		// }
		// add elites to population again.
		// this.getIndividualsDAG().insertAll(elites);

		// return elites;
	}

	private Random getRandomGenerator() {
		if (randomGenerator == null) {
			randomGenerator = new Random();
		}
		return randomGenerator;
	}

	private void setTotalFitness(ObjectiveArray totalFitness) {
		this.totalFitness = totalFitness;
	}

	private MOGPPlannerDAG getIndividualsDAG() {
		return individualsDAG;
	}

	private void setIndividualsDAG(MOGPPlannerDAG individualsDAG) {
		this.individualsDAG = individualsDAG;
	}

	public ObjectiveArray getBestTotalFitness() {
		return bestTotalFitness;
	}

	public void setBestTotalFitness(ObjectiveArray lastTotalFitness) {
		this.bestTotalFitness = lastTotalFitness;
	}

}