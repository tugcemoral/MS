package tr.edu.metu.ceng.ms.thesis.mogpp.core.env;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.MOState;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path;
import tr.edu.metu.ceng.ms.thesis.modstarlite.ui.components.MOStaticMap;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;

public class MOGeneticMap extends MOStaticMap {

	private static MOGeneticMap gMap;

	public static MOGeneticMap getInstance() {
		if (gMap == null) {
			gMap = new MOGeneticMap();
		}
		return gMap;
	}

	private MOGeneticMap() {
	}

	/**
	 * Maximum number of evolution iteration.
	 */
	private int maxIteration;

	/**
	 * The # of elitists who pass through to next population.
	 */
	private int elitism_k;

	/**
	 * The fixed population size.
	 */
	private int populationSize;

	/**
	 * Probability of crossover
	 */
	private double crossoverRate;

	/**
	 * Probability of mutation.
	 */
	private double mutationRate;

	public int getMaxIteration() {
		return maxIteration;
	}

	public void setMaxIteration(int maxIteration) {
		this.maxIteration = maxIteration;
	}

	public int getElitism_k() {
		return elitism_k;
	}

	public void setElitism_k(int elitism_k) {
		this.elitism_k = elitism_k;
	}

	public double getCrossoverRate() {
		return crossoverRate;
	}

	public void setCrossoverRate(double crossoverRate) {
		this.crossoverRate = crossoverRate;
	}

	public double getMutationRate() {
		return mutationRate;
	}

	public void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public Coordinate getCoordinate(int x, int y) {
		return super.get(x, y).getCoords();
	}

	public List<Coordinate> getAvailableCells(Coordinate actualCoord, Path path) {
//		List<Coordinate> nbrs = new ArrayList<Coordinate>(2 * dims());
//		IntCoord actualIntCoord = (IntCoord) actualCoord;
//
//		for (int i = 0; i < dims(); i++) {
//			// get the upper state and add to nbrs under constraints.
//			int[] up = Arrays.copyOf(actualIntCoord.getInts(),
//					actualIntCoord.getInts().length);
//			up[i] += 1;
//			MOState upState = get(up);
//			if (insideBounds(up) && !upState.hasObstacle()
//					&& isInViewingFrustumArea(up)
//					&& !path.contains(upState.getCoords()))
//				nbrs.add(upState.getCoords());
//
//			// get the lower state and add to nbrs under constraints.
//			int[] down = Arrays.copyOf(actualIntCoord.getInts(),
//					actualIntCoord.getInts().length);
//			down[i] -= 1;
//			MOState downState = get(down);
//			if (insideBounds(down) && !downState.hasObstacle()
//					&& isInViewingFrustumArea(down)
//					&& !path.contains(downState.getCoords()))
//				nbrs.add(downState.getCoords());
//		}
//
//		if (nbrs.size() == 0) {
			return nbrs(actualCoord);
//		}

//		return nbrs;

	}

	private List<Coordinate> nbrs(Coordinate actualCoord) {

		List<Coordinate> nbrs = new ArrayList<Coordinate>(2 * dims());
		IntCoord actualIntCoord = (IntCoord) actualCoord;

		for (int i = 0; i < dims(); i++) {
			// get the upper state and add to nbrs under constraints.
			int[] up = Arrays.copyOf(actualIntCoord.getInts(),
					actualIntCoord.getInts().length);
			up[i] += 1;
			MOState upState = get(up);
			if (insideBounds(up) && !upState.hasObstacle()
					&& isInViewingFrustumArea(up))
				nbrs.add(upState.getCoords());

			// get the lower state and add to nbrs under constraints.
			int[] down = Arrays.copyOf(actualIntCoord.getInts(),
					actualIntCoord.getInts().length);
			down[i] -= 1;
			MOState downState = get(down);
			if (insideBounds(down) && !downState.hasObstacle()
					&& isInViewingFrustumArea(down))
				nbrs.add(downState.getCoords());
		}

		return nbrs;
	}

}
