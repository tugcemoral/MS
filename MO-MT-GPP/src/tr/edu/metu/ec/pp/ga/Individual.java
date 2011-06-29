package tr.edu.metu.ec.pp.ga;

import java.util.List;
import java.util.Random;

import tr.edu.metu.ec.pp.world.Agent;
import tr.edu.metu.ec.pp.world.Cell;
import tr.edu.metu.ec.pp.world.Grid;
import tr.edu.metu.ec.pp.world.Location;
import tr.edu.metu.ec.pp.world.Path;

public class Individual {

	private Path path;

	private double fitnessValue;

	public Individual(Path... subPaths) {
		// concat paths and set this new path.
		this.path = concatPaths(subPaths);
		this.fitnessValue = 0d;
	}

	private Path concatPaths(Path... subPaths) {
		// create a new whole-path.
		Path path = new Path();
		// concat all given paths to new path.
		for (Path subPath : subPaths) {
			path.addCells(subPath.getCells());
		}
		return path;
	}

	public double getFitnessValue() {
		return fitnessValue;
	}

	private void setFitnessValue(double fitnessValue) {
		this.fitnessValue = fitnessValue;
	}

	public double evaluate() {
		double totalWeights = 0d;
		for (Cell cellOnPath : this.getPath().getCells()) {
			totalWeights += cellOnPath.getWeight();
		}
		// set the fitness value of this individual. Note that shorter paths
		// will have larger fitness function.
		int numberOfCells = this.getPath().getCells().size();
		this.setFitnessValue(1 / (Math.pow((totalWeights / numberOfCells), 2) * Math
				.pow(numberOfCells, 2)));
		return this.getFitnessValue();
	}

	public boolean generateRandomPath(int startX, int startY, int goalX,
			int goalY) {
		// get the size of the grid
		int n = Grid.getInstance().getN();
		// the random generator to generate the corresponding cell.
		Random nextCellFinder = new Random();

		// final locations to finish.
		// int goalX = n, goalY = n;

		boolean randomPathFound = false;
		while (!randomPathFound) {
			this.getPath().initialize();
			Agent.getAgent().setCurrentLocation(new Location(startX, startY));
			this.getPath().addCell(
					Grid.getInstance().getCell(
							Agent.getAgent().getCurrentLocation()));
			while (this.getPath().getCells().size() < n * n) {
				// get the agent's cell.
				Cell agentCell = Grid.getInstance().getCell(
						Agent.getAgent().getCurrentLocation());
				// get available cells of agent's cell.
				List<Cell> availableCells = agentCell
						.getAvailableCells(agentCell);
				// generate the next random cell.
				Cell nextCell = availableCells.get((nextCellFinder
						.nextInt(availableCells.size())));

				// add randomly found cell to path.
				this.getPath().addCell(nextCell);
				// update agent's location
				Agent.getAgent().setCurrentLocation(nextCell.getLocation());

				if (nextCell.getLocation().getX() == goalX
						&& nextCell.getLocation().getY() == goalY) {
					randomPathFound = true;
					// System.out.println(this.getPath().toString());
					break;
				}
			}
		}
		return randomPathFound;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(fitnessValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (Double.doubleToLongBits(fitnessValue) != Double
				.doubleToLongBits(other.fitnessValue))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
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

	public Cell getAnyIntersectionCellWith(Individual individual) {
		return this.getPath().getAnyIntersectionCellWith(individual.getPath());
	}

	public Cell getStartCell() {
		return this.getPath().getCells().getFirst();
	}

	public Cell getGoalCell() {
		return this.getPath().getCells().getLast();
	}

	/**
	 * TODO: start cell is included, finish cell is excluded (till it is not
	 * goal cell)
	 * 
	 * @param startCell
	 * @param finishCell
	 * @return
	 */
	public Path getSubPath(Cell fromCell, Cell toCell) {
		// create a new sub path.
		Path subPath = new Path();

		// find the start (from) cell and add through last (to) cell.
		for (int i = 0; i < this.getPath().getCells().size(); i++) {
			if (this.getPath().getCells().get(i).equals(fromCell)) {
				for (int k = i;; k++) {
					if (!this.getPath().getCells().get(k).equals(toCell)) {
						subPath.addCell(this.getPath().getCells().get(k));
					} else {
						if (toCell.equals(this.getGoalCell())) {
							subPath.addCell(toCell);
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
		Cell cell = this.getPath().getCells()
				.get(nextCellRndGen.nextInt(this.getPath().getCells().size()));

		// extract the path from start cell to corresponding cell.
		Path subPath = this.getSubPath(this.getStartCell(), cell);

		// try to find an alternative sub-path from this random cell to goal
		// cell.
		this.generateRandomPath(cell.getLocation().getX(), cell.getLocation()
				.getY(), this.getGoalCell().getLocation().getX(), this
				.getGoalCell().getLocation().getY());

		// concat paths.
		this.setPath(this.concatPaths(subPath, this.getPath()));
	}
}
