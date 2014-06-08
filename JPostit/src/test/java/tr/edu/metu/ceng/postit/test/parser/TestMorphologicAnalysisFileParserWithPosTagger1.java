package tr.edu.metu.ceng.postit.test.parser;

import static org.hamcrest.core.Is.*;
import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import tr.edu.metu.ceng.postit.data.Corpus;
import tr.edu.metu.ceng.postit.data.CorpusParser;
import tr.edu.metu.ceng.postit.tagger.PosTagger1;

public class TestMorphologicAnalysisFileParserWithPosTagger1 {

	private static final String MORPH_ANALYSIS_SAMPLE_FILE_PATH = "/morphAnalysisSample.txt";

	@Test
	public void testParseMorphologicAnalysisFile() throws Exception {

		// read the input file.
		File morphAnalysisSampleFile = new File(this.getClass()
				.getResource(MORPH_ANALYSIS_SAMPLE_FILE_PATH).getFile());

		// create alphabet morph analysis parser w/ posTagger1
		CorpusParser parser = new CorpusParser(
				new PosTagger1(), morphAnalysisSampleFile);

		// parse input file and compare expected results.
		Corpus corpus = parser.parse();
		
		Assert.assertThat(corpus.getSentences().size(), is(7));
		
	}
}
