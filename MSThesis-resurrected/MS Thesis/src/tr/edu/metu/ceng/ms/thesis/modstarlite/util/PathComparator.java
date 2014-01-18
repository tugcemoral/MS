package tr.edu.metu.ceng.ms.thesis.modstarlite.util;

import java.util.Comparator;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;

public class PathComparator implements Comparator<Path<Coordinate>> {

	@Override
	public int compare(Path<Coordinate> o1, Path<Coordinate> o2) {
		double v1 = o1.getCost().get(0).getValue();
		double v2 = o2.getCost().get(0).getValue();
		return (v1 < v2) ? -1 : ((v1 > v2) ? 1 : 0); 
	}

}
