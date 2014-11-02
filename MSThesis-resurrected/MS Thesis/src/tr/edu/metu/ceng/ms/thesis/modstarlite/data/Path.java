package tr.edu.metu.ceng.ms.thesis.modstarlite.data;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;

public class Path<T extends Coordinate> implements Cloneable {

	private LinkedList<T> route;

	private ObjectiveArray cost;

	public Path() {
		// initialize the route path...
		this.route = new LinkedList<T>();
	}

	public Path(LinkedList<T> route) {
		this.route = route;
		// this.cost =
	}

	public Path(LinkedList<T> route2, ObjectiveArray cost) {
		this.route = route2;
		this.cost = cost;
	}

	public LinkedList<T> getRoute() {
		if (route == null) {
			route = new LinkedList<T>();
		}
		return route;
	}

	private void setRoute(LinkedList<T> newRoute) {
		this.route = newRoute;
	}

	public boolean hasRoute() {
		return !this.getRoute().isEmpty();
	}

	public int size() {
		return this.getRoute().size();
	}

	public T get(int idx) {
		return this.getRoute().get(idx);
	}

	public void addAll(Path<T> path) {
		this.getRoute().addAll(path.getRoute());
		// this.setCost(MultiObjectiveUtils.sum(this.getCost(),
		// path.getCost()));
	}

	public void add(T newT) {
		this.getRoute().add(newT);
		// this.setCost(MultiObjectiveUtils.sum(
		// this.getCost(),
		// new ObjectiveArray(new Objective(1,
		// ObjectiveBehaviour.MINIMIZED), new Objective(newT
		// .getRisk(), ObjectiveBehaviour.MINIMIZED))));

	}

	public void addFirst(T newT) {
		this.getRoute().addFirst(newT);
		// this.setCost(MultiObjectiveUtils.sum(
		// this.getCost(),
		// new ObjectiveArray(new Objective(1,
		// ObjectiveBehaviour.MINIMIZED), new Objective(newT
		// .getRisk(), ObjectiveBehaviour.MINIMIZED))));

	}

	public void addLast(T newT) {
		this.getRoute().addLast(newT);
		// this.setCost(MultiObjectiveUtils.sum(
		// this.getCost(),
		// new ObjectiveArray(new Objective(1,
		// ObjectiveBehaviour.MINIMIZED), new Objective(newT
		// .getRisk(), ObjectiveBehaviour.MINIMIZED))));

	}

	@Override
	public String toString() {
		return this.getRoute().toString();
	}

	// @Override
	// public Path<T> clone() throws CloneNotSupportedException {
	// Path<T> path = new Path<T>();
	// path.setRoute((LinkedList<T>) this.getRoute().clone());
	// path.setCost(this.getCost());
	// return path;
	// }

	public boolean contains(T pathState) {
		return this.getRoute().contains(pathState);
	}

	public void add(int index, T newT) {
		this.getRoute().add(index, newT);
		// this.setCost(MultiObjectiveUtils.sum(
		// this.getCost(),
		// new ObjectiveArray(new Objective(1,
		// ObjectiveBehaviour.MINIMIZED), new Objective(newT
		// .getRisk(), ObjectiveBehaviour.MINIMIZED))));
	}

	public void addLast(LinkedList route2) {
		this.getRoute().addAll(route2);
	}

	public ObjectiveArray getCost() {
		// if (cost == null) {
		// cost = ObjectiveArray.SINGLE_ZERO;
		// }
		return cost;
	}

	public void setCost(ObjectiveArray costOA) {
		this.cost = costOA;
	}

	// public Path<T> subPathFromStart(T indexT) {
	// if (this.getRoute().contains(indexT)) {
	// List<T> subPath = this.getRoute().subList(0,
	// this.getRoute().indexOf(indexT) + 1);
	// LinkedList<T> route = new LinkedList<T>(subPath);
	// return new Path<T>(route);
	// }
	// return null;
	// }
	//
	// public Path<T> subPathToEnd(T indexT) {
	// if (this.getRoute().contains(indexT)) {
	// List<T> subPath = this.getRoute()
	// .subList(this.getRoute().indexOf(indexT) + 1,
	// this.getRoute().size());
	// LinkedList<T> route = new LinkedList<T>(subPath);
	// return new Path<T>(route);
	// }
	// return null;
	// }
	//
	// public void modifyPath(Path<T> headSubPath, T endLoc) {
	// LinkedList<T> newRoute = new LinkedList<T>();
	// newRoute.addAll(headSubPath.getRoute());
	//
	// Path<T> tailSubPath = this.subPathToEnd(endLoc);
	// for (T state : tailSubPath.getRoute()) {
	// newRoute.addLast(state);
	// }
	// this.setRoute(newRoute);
	//
	// // return constructPathFromSubPaths(headSubPath, subPathToEnd);
	// }
	//
	// // XXX: all paths must be given well-ordered and well-formed (dont have
	// // cracks between them.)
	// public Path<T> constructPathFromSubPaths(Path<T>... subPaths) {
	//
	// // create a new route.
	// LinkedList<T> route = new LinkedList<T>();
	// // ObjectiveArray cost = new ObjectiveArray(Objective.ZERO,
	// Objective.ZERO);
	// for (Path<T> path : subPaths) {
	// route.addAll(path.getRoute());
	// // MultiObjectiveUtils.sum(cost, path.getCost());
	// }
	//
	// return new Path<T>(route);
	// }

	public void initialize() {
		this.getRoute().clear();
	}

	public boolean intersects(Path path) {
		if(findIntersectedCells(path).size() > 0){
			return true;
		}else {
			return false;
		}
		
//		boolean intersects = false;
//		// traverse both paths and try to find a mutual cell.
//		for (int i = 0; i < this.size(); i++) {
//			for (int j = 0; j < path.size(); j++) {
//				// note that intersected cells are not initial or final cells.
//				if ((!(this.get(i).equals(this.getRoute().getFirst())) || !(this
//						.get(i).equals(this.getRoute().getLast())))
//						&& (!(path.get(j).equals(path.getRoute().getFirst())) || !(path
//								.get(j).equals(path.getRoute().getLast())))) {
//					// if one of the cells is equal with given path' s one cell,
//					// that means these paths are intersected.
//					if (this.get(i).equals(path.get(j))) {
//						intersects = true;
//						break;
//					}
//				}
//			}
//			if (intersects)
//				break;
//		}
//		return intersects;
	}

	public T getAnyIntersectionCellWith(Path path) {
		// first, find intersected cells list.
		List<T> intersectedCells = findIntersectedCells(path);
		if(!(intersectedCells.size() > 0)){
//			System.err.println("intersected cell size is 0!");
			return null;
		}else {
			
		// return a randomly selected intersection cell.
		return intersectedCells.get((new Random()).nextInt(intersectedCells
				.size()));
		}
	}

	private List<T> findIntersectedCells(Path path) {
		// create a list to hold intersected cells.
		List<T> intersectedCells = new Vector<T>();

		// traverse both paths and try to find mutual cells.
		for (int i = 0; i < this.getRoute().size(); i++) {
			for (int j = 0; j < path.getRoute().size(); j++) {
				// note that intersected cells are not initial or final cells.
				boolean isCellFirst = this.getRoute().get(i)
						.equals(this.getRoute().getFirst())
						|| path.getRoute().get(j)
								.equals(path.getRoute().getFirst());

				boolean isCellLast = this.getRoute().get(i)
						.equals(this.getRoute().getLast())
						|| path.getRoute().get(j)
								.equals(path.getRoute().getLast());

				if (!isCellFirst && !isCellLast) {
					if (this.getRoute().get(i).equals(path.getRoute().get(j))) {
						if (!intersectedCells.contains(this.getRoute().get(i))) {
							intersectedCells.add(this.getRoute().get(i));
							if (intersectedCells.size() == 10) {
								return intersectedCells;
							}
						}
					}
				}
			}
		}

		return intersectedCells;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		result = prime * result + ((route == null) ? 0 : route.hashCode());
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
		Path other = (Path) obj;
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
			return false;
		if (route == null) {
			if (other.route != null)
				return false;
		} else if (!route.equals(other.route))
			return false;
		return true;
	}

	public void eliminateCycles() {

		int index = 0;
		boolean reachedToGoal = false;
		while (index < size()) {
			T indexedElement = getRoute().get(index);
			int lastIndexOfIndexedElement = getRoute().lastIndexOf(
					indexedElement);
			if (lastIndexOfIndexedElement > index) {
				List<T> headSubList = getRoute().subList(0, index);
				List<T> tailSubList = getRoute().subList(
						lastIndexOfIndexedElement, getRoute().size());

				LinkedList<T> newRoute = new LinkedList<T>();
				newRoute.addAll(headSubList);
				newRoute.addAll(tailSubList);

				setRoute(newRoute);
			}
			index++;
		}

		// T first = getRoute().getFirst();
		// // get last index of first element
		// int lastIndexOfFirst = getRoute().lastIndexOf(first);
		// List<T> subList = getRoute().subList(lastIndexOfFirst,
		// getRoute().size());
		// setRoute(new LinkedList<T>(subList));
	}

}
