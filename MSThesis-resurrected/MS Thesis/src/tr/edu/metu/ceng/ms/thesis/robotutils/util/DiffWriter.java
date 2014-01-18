package tr.edu.metu.ceng.ms.thesis.robotutils.util;

import static tr.edu.metu.ceng.ms.thesis.modstarlite.util.FileWriterUtils.now;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import tr.edu.metu.ceng.ms.thesis.robotutils.planning.DStarLite.ValueMap;

public class DiffWriter {

	private static final String DSTAR_G_RHS_DIFF = "/resource/dStarGandRHSdifferences/";
	
	private static final String DSTAR_STATES = "/resource/dStarStates/";

	private static DiffWriter writer;

	public static DiffWriter getWriter() {
		if (writer == null) {
			writer = new DiffWriter();
		}
		return writer;
	}

	private DiffWriter() {
	}

	public void writeDifferences(ValueMap g, ValueMap rhs) {

		try {
			// create a new file writer...
			FileWriter fWriter = new FileWriter(System.getProperty("user.dir")
					+ DSTAR_G_RHS_DIFF + now() + ".txtdiff");
			BufferedWriter bWriter = new BufferedWriter(fWriter);

			boolean anyLineWritten = false;
			int iterationCount = 0;
			for (Object state : g.keySet()) {
				Double gValue = g.get(state);
				Double rhsValue = rhs.get(state);
				if (!gValue.equals(rhsValue)
//						&& (!gValue.equals(Double.POSITIVE_INFINITY) || !rhsValue
//								.equals(Double.POSITIVE_INFINITY))
								) {
					bWriter.write(state.toString() + ": g: "
							+ gValue.toString() + " rhs: "
							+ rhsValue.toString() + "\n");
					anyLineWritten = true;
				}
				iterationCount++;
			}

			if (g.keySet().size() != rhs.keySet().size()) {
				bWriter.write("g and rhs sizes are not equal:\nSize of g: "
						+ g.size() + "\nSize of rhs: " + rhs.size());
			} else {
				bWriter.write("All g and rhs values are equal.");
			}
			bWriter.write("\nIteration Count : " + iterationCount);

			bWriter.close();
			fWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void writeStates(Object... objectsToWrite) {

		try {
			// create a new file writer...
			FileWriter fWriter = new FileWriter(System.getProperty("user.dir")
					+ DSTAR_STATES + now() + ".states");
			BufferedWriter bWriter = new BufferedWriter(fWriter);

			// write given objects sequentially.
			for (Object object : objectsToWrite) {
				bWriter.write(object.toString() + "\n");
			}

			bWriter.close();
			fWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
