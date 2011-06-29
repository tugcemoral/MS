package tr.edu.metu.ec.pp.ga;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import tr.edu.metu.ec.pp.world.Cell;
import tr.edu.metu.ec.pp.world.Grid;
import tr.edu.metu.ec.pp.world.Path;

public class Population {

	/**
	 * Maximum number of evolution iteration.
	 */
	private static final int MAX_ITERATION = 3500;

	/**
	 * The # of elitists who pass through to next population.
	 */
	private static final int ELITISM_K = 5;

	/**
	 * The fixed population size.
	 */
	private static final int POPULATION_SIZE = 250 + ELITISM_K;

	/**
	 * Probability of crossover
	 */
	private static final double CROSSOVER_RATE = 0.8;

	/**
	 * Probability of mutation.
	 */
	private static final double MUTATION_RATE = 0.05;

	/**
	 * Individuals list of population.
	 */
	private List<Individual> individuals;

	/**
	 * Random Number generator
	 */
	private Random randomGenerator;

	/**
	 * Total fitness value of the population.
	 */
	private double totalFitness;

	public Population() {
		// individuals = new Individual[POP_SIZE];
		individuals = new Vector<Individual>();

		// init population
		for (int i = 0; i < POPULATION_SIZE; i++) {
			Individual individual = new Individual();
			individual.generateRandomPath(1, 1, Grid.getInstance().getN(), Grid
					.getInstance().getN());
			individuals.add(individual);
		}

		// evaluate current population.
		this.evaluate();
	}

	/**
	 * The main evolution loop.
	 * 
	 * @return
	 */
	public Population evolve() {
		double lastFitnessValue = 0.0d;

		// evolve through maximum iterations.
		for (int i = 1; i <= MAX_ITERATION; i++) {
			// create a new individuals list.
			List<Individual> newIndividuals = new Vector<Individual>();

			// get elites from existing population to new population.
			newIndividuals.addAll(getElites());

			// build new Population
			while (newIndividuals.size() < POPULATION_SIZE) {
				// make a roulette wheel selection and get two parents.
				List<Individual> parents = this.rouletteWheelSelection();

				// do the crossover.
				if (getRandomGenerator().nextDouble() < CROSSOVER_RATE) {
					// actually, they are offsprings.
					parents = this.crossover(parents);
				}
				// do the mutation for each parent / offspring
				if (getRandomGenerator().nextDouble() < MUTATION_RATE) {
					parents.get(0).mutate();
				}
				if (getRandomGenerator().nextDouble() < MUTATION_RATE) {
					parents.get(1).mutate();
				}

				// add to generated offsprings / parents to new population
				newIndividuals.addAll(parents);

			}

			// set population's new individuals.
			this.setIndividuals(newIndividuals);

			// evaluate the new population (new individuals)
			this.evaluate();

			// get the best individual of this population.
			Individual bestIndividual = this.findBestIndividual();

			// if there exists an evolution, print it out.
			if (bestIndividual.getFitnessValue() > lastFitnessValue) {
				// current population' s best fitness.
				System.out.println("Best fitness of #" + i + " th population: "
						+ bestIndividual.getFitnessValue());
				// current population' s total fitness
				System.out.println("Total fitness of #" + i
						+ " th population: " + this.getTotalFitness());
				// # of cells visited.
				System.out.println("# of visited cells in #" + i
						+ "th population: " + bestIndividual.getPath().size());

				// print the best path.
				Grid.getInstance().print(bestIndividual);

				// update the last fitness value.
				lastFitnessValue = bestIndividual.getFitnessValue();
			}

		}
		return this;
	}

	public Individual findBestIndividual() {
		int idxMax = 0 /* , idxMin = 0 */;
		double currentMax = 0.0d;
		// double currentMin = Double.MAX_VALUE;
		double currentVal;

		for (int i = 0; i < this.getIndividuals().size(); i++) {
			currentVal = getIndividuals().get(i).getFitnessValue();
			// if (currentMax < currentMin) {
			// currentMax = currentMin = currentVal;
			// idxMax /*= idxMin */ = i;
			// }
			if (currentVal > currentMax) {
				currentMax = currentVal;
				idxMax = i;
			}
			// if (currentVal < currentMin) {
			// currentMin = currentVal;
			// idxMin = i;
			// }
		}

		// return m_population[idxMin]; // minimization
		return getIndividuals().get(idxMax); // maximization
	}

	public double getTotalFitness() {
		return totalFitness;
	}

	public List<Individual> rouletteWheelSelection() {
		// create the selected individuals list.
		List<Individual> selectedIndividuals = new Vector<Individual>();

		// get the roulette wheel.
		double[] rouletteWheel = createRouletteWheel();

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
				selectedIndividuals.add(this.getIndividuals().get(index));
			}

		} while (!selectedIndividuals.get(0).intersects(
				selectedIndividuals.get(1)));

		return selectedIndividuals;
	}

	private double[] createRouletteWheel() {
		// create the roulette wheel array. Note that its parallel with
		// individuals list.
		double[] rouletteWheel = new double[this.getIndividuals().size() + 1];
		double value = 0d;
		rouletteWheel[0] = value;
		for (int i = 0; i < this.getIndividuals().size(); i++) {
			value += this.getIndividuals().get(i).getFitnessValue()
					/ this.getTotalFitness();
			rouletteWheel[i + 1] = value;
		}
		return rouletteWheel;
	}

	private List<Individual> crossover(List<Individual> parents) {
		// Individual[] newIndiv = new Individual[2];
		List<Individual> offsprings = new Vector<Individual>();

		// get the intersection cell of parents.
		Cell intersectedCell = parents.get(0).getAnyIntersectionCellWith(
				parents.get(1));

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
	}

	private double evaluate() {
		// initialize the total fitness function value.
		this.setTotalFitness(0d);
		for (int i = 0; i < POPULATION_SIZE; i++) {
			this.setTotalFitness(this.getTotalFitness()
					+ getIndividuals().get(i).evaluate());
		}
		return this.getTotalFitness();
	}

	private List<? extends Individual> getElites() {
		// create the elites list.
		List<Individual> elites = new Vector<Individual>();
		// find elites and add to elites list.
		for (int i = 0; i < ELITISM_K; i++) {
			// get the best individual and add it to elites list.
			Individual elite = this.findBestIndividual();
			elites.add(elite);
			// remove the individual to get next elite.
			this.removeIndividual(elite);
		}
		// add elites to population again.
		this.getIndividuals().addAll(elites);

		return elites;
	}

	private List<Individual> getIndividuals() {
		if (individuals == null) {
			individuals = new Vector<Individual>();
		}
		return individuals;
	}

	private Random getRandomGenerator() {
		if (randomGenerator == null) {
			randomGenerator = new Random();
		}
		return randomGenerator;
	}

	private void removeIndividual(Individual individual) {
		this.getIndividuals().remove(individual);
	}

	private void setIndividuals(List<Individual> individuals) {
		this.individuals = individuals;
	}

	private void setTotalFitness(double totalFitness) {
		this.totalFitness = totalFitness;
	}
}