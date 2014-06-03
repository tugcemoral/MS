package tr.edu.metu.ceng.postit.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;

import tr.edu.metu.ceng.postit.tagger.WordTagPair;

public class MorphologicalAnalysisParser {

	private File inputFile;

	public MorphologicalAnalysisParser(String inputFilePath)
			throws FileNotFoundException {
		this(new File(inputFilePath));
	}

	public MorphologicalAnalysisParser(File inputFile)
			throws FileNotFoundException {
		if (!inputFile.exists()) {
			throw new FileNotFoundException(MessageFormat.format(
					"Given input file {0} does not exists!",
					inputFile.getPath()));
		}
		this.inputFile = inputFile;
	}

	public void parse() {
		// construct a buffered reader w/ a file reader.
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(getInputFile()));
			String line = null;
			while ((line = br.readLine()) != null) {
				
				this.parse(line);
				
			}
		} catch (FileNotFoundException e) {
			// already checked, no need to re-check again.
		} catch (IOException ioe) {
			// that's interesting...
			ioe.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// what can i do, sometimes.
			}
		}
	}

	private WordTagPair parse(String morphologicalSentence) {
		return null;
		
		
		
	}

	public File getInputFile() {
		return inputFile;
	}
}
