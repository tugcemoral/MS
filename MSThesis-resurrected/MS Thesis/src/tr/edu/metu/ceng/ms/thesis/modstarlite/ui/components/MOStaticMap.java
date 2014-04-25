package tr.edu.metu.ceng.ms.thesis.modstarlite.ui.components;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ExecutionType;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.MOState;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveBehaviour;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Obstacle;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.TemporaryGoalStrategy;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Threat;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.VFrustumStrategy;
import tr.edu.metu.ceng.ms.thesis.modstarlite.util.PropertiesReader;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;
import tr.edu.metu.ceng.ms.thesis.robotutils.util.CoordUtils;

public class MOStaticMap implements MOGridMap {

	private MOState[] map = null;

	private List<MOState> vFrustumArea = null;

	protected int[] sizes = new int[0];

	private int[] cumSizes = new int[0];

	private int[] start;

	private int[] currentAgentLocation;

	private int[] goal;

	private int[] tmpGoal;

	private List<MOState> possibleTmpGoals;

	private int length = 0;

	private int objectiveCount;

	private List<Threat> threats;

	private List<Obstacle> obstacles;

	private ObjectiveBehaviour[] objectiveBehaviours;

	private int[] viewingFrustumBorders;

	private ExecutionType executionType;

	protected MOStaticMap() {
	}

	public MOStaticMap(int... sizes) {
		resize(sizes);
	}

	public MOStaticMap(int width, int height, int[] start, int[] goal,
			int[] viewingFrustumArea, ExecutionType executionType,
			ObjectiveBehaviour[] objectiveBehaviours) {
		this.viewingFrustumBorders = viewingFrustumArea;
		this.objectiveCount = objectiveBehaviours.length;
		this.objectiveBehaviours = objectiveBehaviours;
		this.executionType = executionType;
		this.start = start;
		// agent' s initial location is start.
		this.currentAgentLocation = start;
		this.goal = goal;
		resize(width, height);
	}

	public void resize(int... _sizes) {
		sizes = Arrays.copyOf(_sizes, _sizes.length);
		cumSizes = new int[sizes.length];

		cumSizes[0] = 1;
		for (int i = 1; i < sizes.length; i++) {
			cumSizes[i] = cumSizes[i - 1] * sizes[i - 1];
		}

		length = cumSizes[sizes.length - 1] * sizes[sizes.length - 1];
		map = new MOState[length];
	}

	protected int index(int[] idx) {
		int linIdx = 0;

		for (int i = 0; i < sizes.length; i++) {
			if (idx[i] < 0)
				return -1;
			if (idx[i] >= sizes[i])
				return -1;

			linIdx += cumSizes[i] * idx[i];
		}

		return linIdx;
	}

	private int index(int x, int y) {
		return index(new int[]{x, y});
	}

	public int length() {
		return length;
	}

	public int size(int dim) {
		return sizes[dim];
	}

	public int[] sizes() {
		return Arrays.copyOf(sizes, sizes.length);
	}

	public int dims() {
		return sizes.length;
	}

	public MOState[] getData() {
		return map;
	}

	public boolean hasObstacle(int... idx) {
		int i = index(idx);
		return (i >= 0) ? map[i].hasObstacle() : true;
	}

	public MOState get(int... idx) {
		int i = index(idx);
		return (i >= 0) ? map[i] : MOState.getInfiniteState(this
				.getObjectiveBehaviours());
	}

	public void setHasObstacle(boolean hasObstacle, int... idx) {
		int i = index(idx);
		if (i >= 0) {
			map[i].setHasObstacle(hasObstacle);
		}
		if (hasObstacle) {
			// create a new obstacle and add it to map.
			this.addObstacle(new Obstacle(idx));
		} else {
			this.removeObstacle(new Obstacle(idx));
		}
	}

	public void set(MOState val, int... idx) {
		int i = index(idx);
		if (i >= 0)
			map[i] = val;
	}

	public int getObjectiveCount() {
		return objectiveCount;
	}

	public ObjectiveBehaviour[] getObjectiveBehaviours() {
		return objectiveBehaviours;
	}

	@Override
	public boolean insideBounds(int... idx) {
		for (int i = 0; i < idx.length; i++) {
			if (idx[i] <= 0 || idx[i] >= sizes[i] - 1)
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		String mapStr = "{";

		for (int i = 1; i < sizes[0] - 1; i++) {
			for (int j = 1; j < sizes[1] - 1; j++) {
				mapStr += "[" + j + " " + i + "]="
						+ get(j, i).toShortenString() + ", ";
			}
			mapStr += "\n";
		}
		return mapStr;

	}

	public void locateThreat(Threat threat) {
		// get threat properties.
		Coordinate startCoord = threat.getStartCoord();
		int threatSize = threat.getSize();

		// traverse corresponding cells and set their required properties.
		for (int i = (int) startCoord.get(0); i < startCoord.get(0)
				+ threatSize; i++) {
			for (int j = (int) startCoord.get(1); j < startCoord.get(1)
					+ threatSize; j++) {
				// get actual initialized state.
				MOState moState = this.get(i, j);
				// set inThreat and total risk.
				moState.setInThreat(true);
				moState.addRisk(threat.getThreatZoneRisk(i, j));
			}
		}

		// add this threat into threats list of map.
		this.addThreat(threat);
	}

	public void locateObstacle(Obstacle obstacle) {
		// get obstacle's coordinates.
		Coordinate coords = obstacle.getCoords();
		// put properties for this obstacle.
		this.get((int) coords.get(0), (int) coords.get(1)).setHasObstacle(true);
		this.addObstacle(obstacle);
	}

	public List<Threat> getThreats() {
		if (threats == null) {
			threats = new Vector<Threat>();
		}
		return threats;
	}

	public void addThreat(Threat threat) {
		this.getThreats().add(threat);
	}

	public List<Obstacle> getObstacles() {
		if (obstacles == null) {
			obstacles = new Vector<Obstacle>();
		}
		return obstacles;
	}

	public void addObstacle(Obstacle obstacle) {
		this.getObstacles().add(obstacle);
	}

	private void removeObstacle(Obstacle obstacle) {
		this.getObstacles().remove(obstacle);
	}

	public int[] getStart() {
		return start;
	}

	public void setStart(int[] start) {
		this.start = start;
	}

	public int[] getGoal() {
		return goal;
	}

	public void setGoal(int[] goal) {
		this.goal = goal;
	}

	public Coordinate getGoalAsCoordinate() {
		return new IntCoord(this.getGoal());
	}

	@Override
	public boolean isInViewingFrustumArea(int... idx) {
		int i = index(idx);
		return (i >= 0) ? map[i].isInViewingFrustum() : false;
	}

	// only used while generating the first map.
	public void setInViewingFrustum(MOState state) {
		int i = index(state.getCoords().getInts());
		int x = state.getCoords().getInts()[0];
		int y = state.getCoords().getInts()[1];

		if ((x <= this.getStart()[0] + this.getViewingFrustumBorders()[0] && x >= this
				.getStart()[0] - this.getViewingFrustumBorders()[0])
				&& (y <= this.getStart()[1]
						+ this.getViewingFrustumBorders()[1] && y >= this
						.getStart()[1] - this.getViewingFrustumBorders()[1])) {
			map[i].setInViewingFrustum(true);
			map[i].setTraversable(true);
			addToVFrustumArea(state);
		} else {
			map[i].setInViewingFrustum(false);
			map[i].setTraversable(false);
		}
	}

	@Override
	public void setInViewingFrustum(boolean isInViewingFrustum, int... idx) {
		int i = index(idx);
		if (i >= 0) {
			map[i].setInViewingFrustum(isInViewingFrustum);
		}
	}

	public int[] getViewingFrustumBorders() {
		return viewingFrustumBorders;
	}

	public void setViewingFrustumBorders(int[] viewingFrustumArea) {
		this.viewingFrustumBorders = viewingFrustumArea;
	}

	public ExecutionType getExecutionType() {
		return executionType;
	}

	public void setExecutionType(ExecutionType executionType) {
		this.executionType = executionType;
	}

	public int[] getTmpGoal() {
		return tmpGoal;
	}

	public IntCoord updateTmpGoal() {
		this.tmpGoal = findTmpGoal().getInts();
		return new IntCoord(tmpGoal);
	}

	public IntCoord findTmpGoal() {

		IntCoord tmpGoal = new IntCoord(getGoal());
		IntCoord goalState = new IntCoord(getGoal());

		if (PropertiesReader.getTmpGoalStrategy().equals(
				TemporaryGoalStrategy.NEAREST_BY_SP)) {

			MOState tmpGoalState = null;
			double minMDist = Double.POSITIVE_INFINITY;
			for (MOState state : getvFrustumArea()) {
				if (!state.hasObstacle()) {
					double mDist = CoordUtils.mdist(state.getCoords(),
							goalState);
					if ((mDist < minMDist) && (!state.isPossibleTmpGoal())) {
						minMDist = mDist;
						tmpGoalState = state;
					}
				}
			}

			tmpGoalState.setPossibleTmpGoal(true);
			getPossibleTmpGoals().add(tmpGoalState);

			tmpGoal = tmpGoalState.getCoords();
		} else {
			double minRisk = Double.POSITIVE_INFINITY;
			// create a list..
			List<MOState> potentialTmpGoals = new Vector<MOState>();
			for (MOState state : getvFrustumArea()) {
				if (!state.hasObstacle()) {
					double totalRiskOfCurrState = state.getTotalRisk();
					if (totalRiskOfCurrState <= minRisk) {
						minRisk = totalRiskOfCurrState;
						potentialTmpGoals.add(state);
					}
				}
			}
			double minMDist = Double.POSITIVE_INFINITY;
			for (MOState state : potentialTmpGoals) {
				double mDist = CoordUtils.mdist(state.getCoords(), goalState);
				if (mDist < minMDist) {
					minMDist = mDist;
					tmpGoal = state.getCoords();
				}
			}
		}

		return tmpGoal;
	}

	public void clearPossibleTmpGoals() {
		for (MOState possibleTmpGoal : getPossibleTmpGoals()) {
			possibleTmpGoal.setPossibleTmpGoal(false);
		}
		getPossibleTmpGoals().clear();
	}

	public void addToVFrustumArea(MOState state) {
		if (!this.getvFrustumArea().contains(state)) {
			state.setInViewingFrustum(true);
			this.getvFrustumArea().add(state);
		}
	}

	public void removeFromVFrustumArea(MOState state) {
		state.setInViewingFrustum(false);
		this.getvFrustumArea().remove(state);
	}

	// @Override
	// public boolean isTraversable(int... idx) {
	// int i = index(idx);
	// return (i >= 0) ? map[i].isTraversable() : false;
	// }

	public List<MOState> getvFrustumArea() {
		if (vFrustumArea == null) {
			vFrustumArea = new Vector<MOState>();
		}
		return vFrustumArea;
	}

	public IntCoord getCurrentAgentLocation() {
		return new IntCoord(currentAgentLocation);
	}

	public void setCurrentAgentLocation(Coordinate newLocation) {
		this.currentAgentLocation = ((IntCoord) newLocation).getInts();

		int xPosBorder = this.getCurrentAgentLocation().getInts()[0]
				+ this.getViewingFrustumBorders()[0];
		int xNegBorder = this.getCurrentAgentLocation().getInts()[0]
				- this.getViewingFrustumBorders()[0];
		int yPosBorder = this.getCurrentAgentLocation().getInts()[1]
				+ this.getViewingFrustumBorders()[1];
		int yNegBorder = this.getCurrentAgentLocation().getInts()[1]
				- this.getViewingFrustumBorders()[1];

		if (PropertiesReader.getvFrustumStrategy().equals(
				VFrustumStrategy.REFRESH)) {
			disposeCurrentVFrustumArea();
		}
		for (int i = xNegBorder; i <= xPosBorder; i++) {
			for (int j = yNegBorder; j <= yPosBorder; j++) {
				if (insideBounds(i, j)) {
					MOState state = map[index(i, j)];
					state.setInViewingFrustum(true);
					state.setTraversable(true);
					addToVFrustumArea(state);
				}
			}
		}

	}

	private void disposeCurrentVFrustumArea() {
		for (MOState moState : getvFrustumArea()) {
			moState.setInViewingFrustum(false);
		}
		this.getvFrustumArea().clear();
	}

	public void setCurrentAgentLocation(int[] newLocation) {
		this.currentAgentLocation = newLocation;
	}

	public void clearBackpointers() {

		for (int i = 0; i < sizes[0]; i++) {
			for (int j = 0; j < sizes[1]; j++) {
				if (i == 0 || i == sizes[0] - 1 || j == 0 || j == sizes[1] - 1) {
					continue;
				} else {
					this.get(i, j).getCoords().getParents().clear();
				}
			}
		}
	}

	public void setObjectiveBehaviours(ObjectiveBehaviour[] objectiveBehaviours) {
		this.objectiveBehaviours = objectiveBehaviours;
	}

	private List<MOState> getPossibleTmpGoals() {
		if (possibleTmpGoals == null) {
			possibleTmpGoals = new Vector<MOState>();
		}
		return possibleTmpGoals;
	}

}
