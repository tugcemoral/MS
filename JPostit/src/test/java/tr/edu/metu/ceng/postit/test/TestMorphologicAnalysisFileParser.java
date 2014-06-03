package tr.edu.metu.ceng.postit.test;

import java.io.File;

import org.junit.Test;

import tr.edu.metu.ceng.postit.parser.MorphologicalAnalysisParser;

public class TestMorphologicAnalysisFileParser {

	private static final String MORPH_ANALYSIS_SAMPLE_FILE_PATH = "/morphAnalysisSample.txt";

	@Test
	public void testParseMorphologicAnalysisFile() throws Exception {

		// read the input file.
		File morphAnalysisSampleFile = new File(this.getClass()
				.getResource(MORPH_ANALYSIS_SAMPLE_FILE_PATH).getFile());

		// create a morph analysis parser.
		MorphologicalAnalysisParser parser = new MorphologicalAnalysisParser(
				morphAnalysisSampleFile);

		// parse input file and compare expected results.
		
		
		

	}

}
