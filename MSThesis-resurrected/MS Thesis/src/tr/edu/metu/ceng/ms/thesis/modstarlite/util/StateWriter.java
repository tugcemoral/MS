package tr.edu.metu.ceng.ms.thesis.modstarlite.util;

import static tr.edu.metu.ceng.ms.thesis.modstarlite.util.FileWriterUtils.G_RHS_DIFF_FILES;
import static tr.edu.metu.ceng.ms.thesis.modstarlite.util.FileWriterUtils.RESOURCE;
import static tr.edu.metu.ceng.ms.thesis.modstarlite.util.FileWriterUtils.STATE_FILES;
import static tr.edu.metu.ceng.ms.thesis.modstarlite.util.FileWriterUtils.now;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import tr.edu.metu.ceng.ms.thesis.modstarlite.core.MODStarLite.ValueMap;

public class StateWriter {

	private static StateWriter writer;

	private StateWriter() {
	}

	public static StateWriter getWriter() {
		if (writer == null) {
			writer = new StateWriter();
		}
		return writer;
	}

	public void writeStates(Object... objectsToWrite) {
		try {
			// create a new file writer...
			FileWriter fWriter = new FileWriter(System.getProperty("user.dir")
					+ STATE_FILES + now() + ".states");
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

	public void writeDifferences(ValueMap g, ValueMap rhs) {

		try {
			// create a new file writer...
			FileWriter fWriter = new FileWriter(System.getProperty("user.dir")
					+ G_RHS_DIFF_FILES + now() + ".grhsdiff");
			BufferedWriter bWriter = new BufferedWriter(fWriter);

			if (g.keySet().size() != rhs.keySet().size()) {
				bWriter.write("g and rhs sizes are not equal:\nSize of g: "
						+ g.size() + "\nSize of rhs: " + rhs.size());
			} else {
				boolean anyLineWritten = false;
				for (Object state : g.keySet()) {
					List gValue = g.get(state);
					List rhsValue = rhs.get(state);
					if (!gValue.equals(rhsValue)) {
						bWriter.write(state.toString() + ": g: "
								+ gValue.toString() + " rhs: "
								+ rhsValue.toString() + "\n");
						anyLineWritten = true;
					}
				}

				if (g.size() > 0) {
					if (!anyLineWritten) {
						bWriter.write("All g and rhs values are equal.");
					}
				} else {
					bWriter.write("What? g and rhs' s are empty??");
				}
			}

			bWriter.close();
			fWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void dumpTime(long iterationCount, long execTime,
			String executionFilePath) {
		// create a new file writer...
		PrintWriter bWriter;
		try {
			File outTimeFile = new File(System.getProperty("user.dir")
					+ RESOURCE + executionFilePath + ".out.time");
			if (!outTimeFile.exists()) {
				outTimeFile.createNewFile();
			}
			bWriter = new PrintWriter(new FileOutputStream(outTimeFile, true));
			bWriter.append(iterationCount + "\t" + execTime + "\n");
			bWriter.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
