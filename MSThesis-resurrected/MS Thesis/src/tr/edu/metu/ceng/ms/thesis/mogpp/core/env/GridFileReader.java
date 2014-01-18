//package tr.edu.metu.ceng.ms.thesis.mogpp.core.env;
//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.List;
//import java.util.Vector;
//
//public class GridFileReader {
//
//	private static final String INPUT_10_LOCATION = "files/input_10.txt";
//
//	private static final String INPUT_20_LOCATION = "files/input_20.txt";
//
//	private static final String INPUT_30_LOCATION = "files/input_30.txt";
//
//	private int readN;
//
//	private List<String[]> readLocations;
//
//	public void readGridInput() {
//		try {
//			// create a file reader and read input file.
//			FileReader fReader = new FileReader(INPUT_20_LOCATION);
//			BufferedReader bReader = new BufferedReader(fReader);
//
//			// set the "n" of grid world.
//			setReadN(Integer.valueOf(bReader.readLine()));
//
//			List<String[]> obstacleLocations = new Vector<String[]>();
//			String line;
//			// construct the obstacle locations.
//			while ((line = bReader.readLine()) != null) {
//				String[] locations = line.split(" ");
//				obstacleLocations.add(locations);
//			}
//			// set obstacle locations list.
//			setReadLocations(obstacleLocations);
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public int getReadN() {
//		return readN;
//	}
//
//	public List<String[]> getReadLocations() {
//		return readLocations;
//	}
//
//	private void setReadN(int readN) {
//		this.readN = readN;
//	}
//
//	private void setReadLocations(List<String[]> readLocations) {
//		this.readLocations = readLocations;
//	}
//
//}
