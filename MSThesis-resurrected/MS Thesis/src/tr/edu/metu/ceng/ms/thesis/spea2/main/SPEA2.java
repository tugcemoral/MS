//  SPEA2.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package tr.edu.metu.ceng.ms.thesis.spea2.main;

import java.util.Random;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path;
import tr.edu.metu.ceng.ms.thesis.mogpp.core.exception.TempGoalShouldBeUpdatedException;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;
import tr.edu.metu.ceng.ms.thesis.spea2.core.Operator;
import tr.edu.metu.ceng.ms.thesis.spea2.integration.core.SPEA2Solution;
import tr.edu.metu.ceng.ms.thesis.spea2.integration.core.SPEA2SolutionSet;
import tr.edu.metu.ceng.ms.thesis.spea2.integration.core.SPEA2ShortestPathComparator;
import tr.edu.metu.ceng.ms.thesis.spea2.operators.selection.BinaryTournament;
import tr.edu.metu.ceng.ms.thesis.spea2.util.JMException;
import tr.edu.metu.ceng.ms.thesis.spea2.util.Ranking;
import tr.edu.metu.ceng.ms.thesis.spea2.util.Spea2Fitness;

/**
 * This class representing the SPEA2 algorithm
 */
public class SPEA2{

	/**
	 * Defines the number of tournaments for creating the mating pool
	 */
	public static final int TOURNAMENTS_ROUNDS = 1;
	
	private Random randomGenerator;

	private int maxIteration;
	private int archiveSize;
	private int populationSize;
	private double xoverRate;
	private double xoverDistIndex;
	private double mutationRate;
	private double mutationDistIndex;

	/**
	 * Constructor. Create a new SPEA2 instance
	 * 
	 * @param problem
	 *            Problem to solve
	 */
	public SPEA2() {
	} 

	public SPEA2(int maxIteration, int archiveSize, int populationSize,
			double xoverRate, double xoverDistIndex, double mutationRate,
			double mutationDistIndex) {
		this.maxIteration = maxIteration;
		this.archiveSize = archiveSize;
		this.populationSize = populationSize;
		this.xoverRate = xoverRate;
		this.xoverDistIndex = xoverDistIndex;
		this.mutationRate = mutationRate;
		this.mutationDistIndex = mutationDistIndex;
	}

	/**
	 * Runs of the Spea2 algorithm.
	 * 
	 * @return a <code>SolutionSet</code> that is a set of non dominated
	 *         solutions as a result of the algorithm execution
	 * @throws JMException
	 * @throws TempGoalShouldBeUpdatedException 
	 */
	public SPEA2SolutionSet execute(Coordinate start, Coordinate goal) throws JMException, ClassNotFoundException, TempGoalShouldBeUpdatedException {

		Operator crossoverOperator, mutationOperator, selectionOperator;
		SPEA2SolutionSet solutionSet, archive, offSpringSolutionSet;

		// Read the params
		// populationSize = ((Integer) getInputParameter("populationSize"))
		// .intValue();
		// archiveSize = ((Integer)
		// getInputParameter("archiveSize")).intValue();
		// maxEvaluations = ((Integer) getInputParameter("maxEvaluations"))
		// .intValue();

		// Read the operators
//		crossoverOperator = operators_.get("crossover");
//		mutationOperator = operators_.get("mutation");
		selectionOperator = new BinaryTournament(null);

		// Initialize the variables
		solutionSet = new SPEA2SolutionSet(populationSize);
		archive = new SPEA2SolutionSet(archiveSize);
		
		int evaluations = 0;
		// -> Create the initial solutionSet
		SPEA2Solution newSolution;
		for (int i = 0; i < populationSize; i++) {
			newSolution = new SPEA2Solution();
			boolean isGenerated = newSolution.generateRandomPath((int) start.get(0),
					(int) start.get(1), (int) goal.get(0), (int) goal.get(1));
			
			//instantly break if any individual failed to construct a path.
			if(!isGenerated) {
				throw new TempGoalShouldBeUpdatedException();
			}
//			problem_.evaluate(newSolution);
//			problem_.evaluateConstraints(newSolution);
//			evaluations++;
			solutionSet.add(newSolution);
		}

		while (evaluations < maxIteration) {
			// create a new individuals list.
			SPEA2SolutionSet union = ((SPEA2SolutionSet) solutionSet).union(archive);
			Spea2Fitness spea = new Spea2Fitness(union);
			spea.fitnessAssign();
			archive = spea.environmentalSelection(archiveSize);
			// Create a new offspringPopulation
			offSpringSolutionSet = new SPEA2SolutionSet(populationSize);
			
			SPEA2Solution[] parents = new SPEA2Solution[2];
			while (offSpringSolutionSet.size() < populationSize) {
				boolean crossovered = false;
				//select parents.
				parents[0] = (SPEA2Solution) selectionOperator.execute(archive);
				parents[1] = (SPEA2Solution) selectionOperator.execute(archive);

				// make the crossover and mutation
//				Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
//				mutationOperator.execute(offSprings.get(0));
				if (getRandomGenerator().nextDouble() < xoverRate) {
					parents = crossover(parents);
					crossovered = true;
				}
				if (getRandomGenerator().nextDouble() < mutationRate) {
					parents[0] = mutate(parents[0]);
				}
				if (getRandomGenerator().nextDouble() < mutationRate) {
					parents[1] = mutate(parents[1]);
				}
				
				if (!crossovered) {
					// re-create new ones to avoid dag confliction
					parents[0] = new SPEA2Solution(parents[0].getPath());
					parents[1] = new SPEA2Solution(parents[1].getPath());
				}
				
//				problem_.evaluate(offSpring[0]);
//				problem_.evaluateConstraints(offSpring[0]);
				
				// add to generated offsprings / parents to new population
				offSpringSolutionSet.add(parents[0]);
				evaluations++;
			} // while
				// End Create a offSpring solutionSet
			solutionSet = offSpringSolutionSet;
		} // while

		//return sorted (by first objective) non-dominated solutions.
		Ranking ranking = new Ranking(archive);
		SPEA2SolutionSet spea2SolutionSet = ranking.getSubfront(0);
		spea2SolutionSet.sort(new SPEA2ShortestPathComparator());
		
		return spea2SolutionSet;
	}

	private SPEA2Solution[] crossover(SPEA2Solution[] parents) {
		// Individual[] newIndiv = new Individual[2];
		SPEA2Solution[] offsprings = new SPEA2Solution[2];

		// get the intersection cell of parents.
		Coordinate intersectedCell = parents[0].getAnyIntersectionCellWith(
				parents[1]);

		if (intersectedCell != null) {

			// extract the sub paths according to found intersection cell.
			Path firstSubPathOfFirstParent = parents[0].getSubPath(
					parents[0].getStartCell(), intersectedCell);
			Path secondSubPathOfSecondParent = parents[1].getSubPath(
					intersectedCell, parents[1].getGoalCell());

			// create the new offspring with sub paths and add it to
			// corresponding
			// list.
			SPEA2Solution firstOffspring = new SPEA2Solution(
					firstSubPathOfFirstParent, secondSubPathOfSecondParent);
			
			offsprings[0] = firstOffspring;

			// extract the sub paths according to found intersection cell.
			Path firstSubPathOfSecondParent = parents[1].getSubPath(
					parents[1].getStartCell(), intersectedCell);
			Path secondSubPathOfFirstParent = parents[0].getSubPath(
					intersectedCell, parents[0].getGoalCell());

			// create the new offspring with sub paths and add it to
			// corresponding
			// list.
			SPEA2Solution secondOffspring = new SPEA2Solution(
					firstSubPathOfSecondParent, secondSubPathOfFirstParent);
			offsprings[1] = secondOffspring;

			return offsprings;
		} else {
			return parents;
		}
	}
	
	private SPEA2Solution mutate(SPEA2Solution individual) {

		SPEA2Solution mutatedIndv = new SPEA2Solution(individual.getPath());

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
//		mutatedIndv.evaluate();

		return mutatedIndv;
	}

	private Random getRandomGenerator() {
		if (randomGenerator == null) {
			randomGenerator = new Random();
		}
		return randomGenerator;
	}

}
