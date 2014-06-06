package tr.edu.metu.ceng.postit.test.parser;

import java.io.File;

import org.junit.Test;

import tr.edu.metu.ceng.postit.parser.MorphologicalAnalysisParser;
import tr.edu.metu.ceng.postit.tagger.PosTagger1;

public class TestMorphologicAnalysisFileParserWithPosTagger1 {

	private static final String MORPH_ANALYSIS_SAMPLE_FILE_PATH = "/morphAnalysisSample.txt";

	@Test
	public void testParseMorphologicAnalysisFile() throws Exception {

		// read the input file.
		File morphAnalysisSampleFile = new File(this.getClass()
				.getResource(MORPH_ANALYSIS_SAMPLE_FILE_PATH).getFile());

		// create a morph analysis parser w/ posTagger1
		MorphologicalAnalysisParser parser = new MorphologicalAnalysisParser(
				new PosTagger1(), morphAnalysisSampleFile);

		// parse input file and compare expected results.
		parser.parse();
	}

}
