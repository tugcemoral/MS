package tr.edu.metu.ceng.ms.thesis.modstarlite.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import tr.edu.metu.ceng.ms.thesis.modstarlite.ui.components.MOStaticMap;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;
import tr.edu.metu.ceng.ms.thesis.robotutils.planning.DStarLite;
import tr.edu.metu.ceng.ms.thesis.robotutils.util.CoordUtils;

public class GridDStarLiteWithMOMap extends DStarLite<IntCoord> {

	private MOStaticMap map;

	public GridDStarLiteWithMOMap(MOStaticMap sm, IntCoord start, IntCoord goal) {
		super(start, goal);
		this.map = sm;
	}

	@Override
	protected double c(IntCoord a, IntCoord b) {
		if (CoordUtils.mdist(a, b) != 1) {
			return Double.POSITIVE_INFINITY;
		} else {
			// if it is an obstacle...
			boolean cA = map.hasObstacle(a.getInts());
			if (cA)
				return Double.POSITIVE_INFINITY;

			// if it is an obstacle...
			boolean cB = map.hasObstacle(b.getInts());
			if (cB)
				return Double.POSITIVE_INFINITY;

			double cost = 1.0;
			if (cost <= 0) {
				throw new IllegalStateException();
			}
			return cost;
		}

	}

	@Override
	protected double h(IntCoord a, IntCoord b) {
		return CoordUtils.mdist(a, b);
	}

	/**
	 * Returns a list of neighbors to the current grid cell, excluding neighbor
	 * cells that have negative cost values.
	 * 
	 * @param s
	 *            the current cell
	 * @return a list of neighboring cells
	 */
	protected Collection<IntCoord> nbrs(IntCoord s) {
		List<IntCoord> nbrs = new ArrayList<IntCoord>(2 * map.dims());

		for (int i = 0; i < map.dims(); i++) {
			int[] up = Arrays.copyOf(s.getInts(), s.getInts().length);
			up[i] += 1;
			nbrs.add(new IntCoord(up));

			int[] down = Arrays.copyOf(s.getInts(), s.getInts().length);
			down[i] -= 1;
			nbrs.add(new IntCoord(down));
		}

		return nbrs;
	}

	@Override
	protected Collection<IntCoord> succ(IntCoord s) {
		return nbrs(s);
	}

	@Override
	protected Collection<IntCoord> pred(IntCoord s) {
		return nbrs(s);
	}

	public void setObstacleOnCoordinate(IntCoord s, boolean b) {

		// If the cost hasn't changed, don't do anything
		if (b == map.hasObstacle(s.getInts()))
			return;

		// Change map cost
		map.setHasObstacle(b, s.getInts());

		updateVertex(s);
	}

	public void setStart(IntCoord start) {
		updateStart(start);
	}

}
