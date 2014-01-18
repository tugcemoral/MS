package tr.edu.metu.ceng.ms.thesis.robotutils.planning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;
import tr.edu.metu.ceng.ms.thesis.robotutils.ui.GridMap;
import tr.edu.metu.ceng.ms.thesis.robotutils.util.CoordUtils;

public class GridDStarLite extends DStarLite<IntCoord> {

	private GridMap map;

	public GridDStarLite(GridMap map, IntCoord start, IntCoord goal) {
		super(start, goal);
		this.map = map;
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

	@Override
	protected double h(IntCoord a, IntCoord b) {
		return CoordUtils.mdist(a, b);
	}

	@Override
	protected double c(IntCoord a, IntCoord b) {
		if (CoordUtils.mdist(a, b) != 1) {
			return Double.POSITIVE_INFINITY;
		} else {
			double cA = map.get(a.getInts());
			if (cA < 0)
				return Double.POSITIVE_INFINITY;

			double cB = map.get(b.getInts());
			if (cB < 0)
				return Double.POSITIVE_INFINITY;

			double cost = (cA + cB) / 2.0 + 1.0;
			if (cost <= 0) {
				throw new IllegalStateException();
			}
			return cost;
		}
	}
	
	public void setCost(IntCoord s, byte val) {

		// If the cost hasn't changed, don't do anything
		if (val == map.get(s.getInts()))
			return;

//		// Find all affected edges (tuples of this cell and its neighbors)
//		Collection<IntCoord> preds = pred(s);
//		Collection<IntCoord> succs = succ(s);
//
//		// Record old costs
//		HashMap<IntCoord, Double> predVals = new HashMap<IntCoord, Double>();
//		for (IntCoord sPrime : preds) {
//			predVals.put(sPrime, c(sPrime, s));
//		}
//
//		HashMap<IntCoord, Double> succVals = new HashMap<IntCoord, Double>();
//		for (IntCoord sPrime : succs) {
//			succVals.put(sPrime, c(s, sPrime));
//		}

		// Change map cost
		map.set(val, s.getInts());

		updateVertex(s);
		
		// Flag changes to new costs
//		for (IntCoord sPrime : preds) {
//			flagChange(sPrime, s, predVals.get(sPrime), c(sPrime, s));
//		}
//
//		for (IntCoord sPrime : succs) {
//			flagChange(s, sPrime, succVals.get(sPrime), c(s, sPrime));
//		}
	}

	public void setStart(IntCoord start) {
		updateStart(start);
	}

}
