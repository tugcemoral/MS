//package tr.edu.metu.ceng.ms.thesis.mogpp.core.env;
//
//import java.util.List;
//import java.util.Random;
//
//public class GridFactory {
//
//	public static Grid createGrid(int n, List<String[]> obstacleLocations) {
//		// create a random generator to generate weight of a cell.
//		Random weightGen = new Random();
//
//		// get the grid instance.
//		Grid grid = Grid.getInstance();
//
//		// set size of the grid.
//		grid.setN(n);
//
//		// add cells.
//		for (int i = 1; i <= n; i++) {
//			for (int j = 1; j <= n; j++) {
//				// create a cell with x and y coordinates.
//				Cell cell = new Cell(j, i);
//				cell.setID((i - 1) * n + j);
//				cell.setHasObstacle(false);
//				cell.setWeight(weightGen.nextDouble()); // set weight of every
//														// cell between 0 and
//														// 1d.
//				// set agent' s initial location as (1, 1)
//				if ((i == 1) && (j == 1)) {
//					cell.setHasAgent(true);
//				}
//
//				// add this cell to grid.
//				grid.addCell(cell);
//			}
//		}
//		// set initial obstacle locations.
//		for (String[] locations : obstacleLocations) {
//			int x = Integer.valueOf(locations[0]);
//			int y = Integer.valueOf(locations[1]);
//			// set the obstacle boolean variable.
//			grid.getCell(x, y).setHasObstacle(true);
//		}
//
//		// set agent's location.
//		Agent.getAgent().setCurrentLocation(new Location(1, 1));
//
//		return grid;
//	}
//
//}
