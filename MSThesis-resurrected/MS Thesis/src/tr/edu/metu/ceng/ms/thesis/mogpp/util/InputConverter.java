package tr.edu.metu.ceng.ms.thesis.mogpp.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.helpers.FileWatchdog;

/**
 * Converts raw obstacle inputs to formattable input as MOD*Lite input format.
 * 
 * @author tugcem
 * 
 */
public class InputConverter {

	private static final String INPUT_FILE_PATH = System
			.getProperty("user.dir") + "/resource/mogpp/inputForMOGPP.txt";

	private static final String OUTPUT_FILE_PATH = System
			.getProperty("user.dir") + "/resource/mogpp/outputForMOGPP.txt";

	public static void main(String[] args) throws IOException {
		InputConverter converter = new InputConverter();
		converter.readInputAndGenerateOutputForObstacles();
	}

	public void readInputAndGenerateOutputForObstacles() throws IOException {
		// open readers.
		FileReader fReader = new FileReader(INPUT_FILE_PATH);
		BufferedReader bReader = new BufferedReader(fReader);

		// open writers.
		FileWriter fWriter = new FileWriter(OUTPUT_FILE_PATH);
		BufferedWriter bWriter = new BufferedWriter(fWriter);

		String readLine;
		int obstacleNo = 1;
		while ((readLine = bReader.readLine()) != null) {
			String[] splittedObsLocs = readLine.trim().split(" ");
			bWriter.write("obstacle" + obstacleNo + " = " + splittedObsLocs[0]
					+ ", " + splittedObsLocs[1] + "\n");
			obstacleNo++;
		}

		bWriter.close();
		fWriter.close();
		bReader.close();
		fReader.close();

	}
}
