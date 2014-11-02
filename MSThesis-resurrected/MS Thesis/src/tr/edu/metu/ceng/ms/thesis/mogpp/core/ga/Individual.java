package tr.edu.metu.ceng.ms.thesis.mogpp.core.ga;

import java.util.List;
import java.util.Random;

import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.graph.Vertex;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Objective;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveArray;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveBehaviour;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path;
import tr.edu.metu.ceng.ms.thesis.mogpp.core.env.MOGeneticMap;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;

public class Individual extends Vertex {

	private static final int UTOPIC_TERMINATION_COEFFICIENT = 9000;

	private Path path;

	private ObjectiveArray fitnessValue;

	public Individual() {
	}

	public Individual(Path... subPaths) {
		// concat paths and set this new path.
		this.path = concatPaths(subPaths);
		calculatePathCost();
		this.evaluate();
		// this.fitnessValue = ObjectiveArray.SINGLE_ZERO;
	}

	private Path concatPaths(Path... subPaths) {
		// create a new whole-path.
		Path path = new Path();
		// concat all given paths to new path.
		for (Path subPath : subPaths) {
			path.addLast(subPath.getRoute());
		}
		return path;
	}

	public ObjectiveArray getFitnessValue() {
		return fitnessValue;
	}

	private void setFitnessValue(ObjectiveArray fitnessValue) {
		this.fitnessValue = fitnessValue;
	}

	public ObjectiveArray evaluate() {
		ObjectiveArray cost = this.getPath().getCost();

		Objective[] objectives = new Objective[cost.size()];

		int i = 0;
		for (Objective objective : cost.getObjectives()) {
			double value = objective.getValue();
			objectives[i] = new Objective((value != 0) ? (1 / Math.pow(value, 2)) : value,
					objective.getBehaviour());
			i++;
		}

		setFitnessValue(new ObjectiveArray(objectives));
		
		return this.getFitnessValue();
	}
	
//	public ObjectiveArray evaluate(){
//		ObjectiveArray cost = this.getPath().getCost();
//
//		Objective[] objectives = new Objective[cost.size()];
//
//		objectives[0] = new Objective((-1) * cost.get(0).getValue(),
//				cost.get(0).getBehaviour());
//		objectives[1] = Objective.ZERO;
//		
//		setFitnessValue(new ObjectiveArray(objectives));
//		return getFitnessValue();
//	}

	public boolean generateRandomPath(int startX, int startY, int goalX,
			int goalY) {
		MOGeneticMap gmap = MOGeneticMap.getInstance();
		// get the size of the grid
		int n = gmap.size(0);
		// the random generator to generate the corresponding cell.
		Random nextCellFinder = new Random();

		// final locations to finish.
		// int goalX = n, goalY = n;
		
		int possibleMaxSize = gmap.getPossibleMaxSize(startX, startY, goalX, goalY);

		boolean randomPathFound = false;
		while (!randomPathFound) {
			this.getPath().initialize();
			Agent.getAgent().setCurrentLocation(
					gmap.getCoordinate(startX, startY));
			this.getPath().add(Agent.getAgent().getCurrentLocation());
			// while (this.getPath().size() < n * n) {
			while (true) {
				// get the agent's cell.
				Coordinate agentCell = Agent.getAgent().getCurrentLocation();
				// get available cells of agent's cell.
				List<Coordinate> availableCells = gmap
						.getAvailableCells(agentCell, getPath());
				// generate the next random cell.
				Coordinate nextCell = availableCells.get((nextCellFinder
						.nextInt(availableCells.size())));
				
				// add randomly found cell to path.
				this.getPath().addLast(nextCell);
				// update agent's location
				Agent.getAgent().setCurrentLocation(nextCell);

				if (nextCell.get(0) == goalX && nextCell.get(1) == goalY) {
					randomPathFound = true;
					break;
				}
				
				if(this.getPath().size() == possibleMaxSize * UTOPIC_TERMINATION_COEFFICIENT) {
					return false;
				}
				
			}
		}
		
		this.getPath().eliminateCycles();

		calculatePathCost();
		evaluate();
		return randomPathFound;
	}

	public void calculatePathCost() {
		double totalPathRisk = 0d;
		for (Object currentCoord : this.getPath().getRoute()) {
			IntCoord intCoord = (IntCoord) currentCoord;
			totalPathRisk += MOGeneticMap.getInstance().get(intCoord.getInts())
					.getTotalRisk();
		}
		this.getPath().setCost(
				new ObjectiveArray(new Objective(this.getPath().size(),
						ObjectiveBehaviour.MINIMIZED), new Objective(
						totalPathRisk, ObjectiveBehaviour.MINIMIZED)));
	}

	public Path getPath() {
		if (path == null) {
			path = new Path();
		}
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public boolean intersects(Individual individual) {
		// check that this individual's solution intersects with given one's
		// solution.
		return this.getPath().intersects(individual.getPath());
	}

	public Coordinate getAnyIntersectionCellWith(Individual individual) {
		return this.getPath().getAnyIntersectionCellWith(individual.getPath());
	}

	public Coordinate getStartCell() {
		return (Coordinate) this.getPath().getRoute().getFirst();
	}

	public Coordinate getGoalCell() {
		return (Coordinate) this.getPath().getRoute().getLast();
	}

	/**
	 * TODO: start cell is included, finish cell is excluded (till it is not
	 * goal cell)
	 * 
	 * @param startCell
	 * @param finishCell
	 * @return
	 */
	public Path getSubPath(Coordinate fromCell, Coordinate toCell) {
		// create a new sub path.
		Path subPath = new Path();

		// find the start (from) cell and add through last (to) cell.
		for (int i = 0; i < this.getPath().size(); i++) {
			if (this.getPath().get(i).equals(fromCell)) {
				for (int k = i;; k++) {
					if (!this.getPath().get(k).equals(toCell)) {
						subPath.add(this.getPath().get(k));
					} else {
						if (toCell.equals(this.getGoalCell())) {
							subPath.add(toCell);
						}
						break;
					}
				}
				break;
			}
		}

		return subPath;
	}

	public void mutate() {
		// create a random generator.
		Random nextCellRndGen = new Random();
		// get a random cell on the path.
		Coordinate cell = this.getPath().get(
				nextCellRndGen.nextInt(this.getPath().size()));

		// extract the path from start cell to corresponding cell.
		Path subPath = this.getSubPath(this.getStartCell(), cell);

		// try to find an alternative sub-path from this random cell to goal
		// cell.
		this.generateRandomPath((int) cell.get(0), (int) cell.get(1),
				(int) this.getGoalCell().get(0), (int) this.getGoalCell()
						.get(1));

		// concat paths.
		this.setPath(this.concatPaths(subPath, this.getPath()));
		this.calculatePathCost();
		this.evaluate();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fitnessValue == null) ? 0 : fitnessValue.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Individual other = (Individual) obj;
		if (fitnessValue == null) {
			if (other.fitnessValue != null)
				return false;
		} else if (!fitnessValue.equals(other.fitnessValue))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
}
