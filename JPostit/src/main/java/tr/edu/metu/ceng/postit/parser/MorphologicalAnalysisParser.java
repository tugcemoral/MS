package tr.edu.metu.ceng.postit.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import tr.edu.metu.ceng.postit.tagger.IPosTagger;
import tr.edu.metu.ceng.postit.tagger.WordTagPair;

public class MorphologicalAnalysisParser {

	private File inputFile;
	
	private IPosTagger posTagger;

	public MorphologicalAnalysisParser(IPosTagger posTagger, String inputFilePath)
			throws FileNotFoundException {
		this(posTagger, new File(inputFilePath));
	}

	public MorphologicalAnalysisParser(IPosTagger posTagger, File inputFile)
			throws FileNotFoundException {
		if (!inputFile.exists()) {
			throw new FileNotFoundException(MessageFormat.format(
					"Given input file {0} does not exists!",
					inputFile.getPath()));
		}
		this.inputFile = inputFile;
		this.posTagger = posTagger;
	}

	public void parse() {
		// construct a buffered reader w/ a file reader.
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(getInputFile()));
			String line = null;
			
			//list of sentences.
			List<String> sentences = new Vector<String>();
			//map of reverse tag-words-list.
			Map<String, List<String>> reverseTagWordsMap = new HashMap<String, List<String>>();
			StringBuilder sentenceBuilder = new StringBuilder();
			
			
			while ((line = br.readLine()) != null) {
				//parse the line wrt associated pos tagger.
				WordTagPair wordTagPair = this.parse(line);
				
				// construct sentences list.
				if(wordTagPair.getWord().equals(".")) {
					sentenceBuilder.append(wordTagPair.getWord());
					//finalize sentence and store it.
					sentences.add(sentenceBuilder.toString());
					sentenceBuilder = new StringBuilder();
				}else {
					sentenceBuilder.append(wordTagPair.getWord()).append(" ");
				}
				
				// construct tag - words-list map.
				if(reverseTagWordsMap.containsKey(wordTagPair.getTag())) {
					//update existing tag's word list.
					List<String> words = reverseTagWordsMap.get(wordTagPair.getTag());
					words.add(wordTagPair.getWord());
					reverseTagWordsMap.put(wordTagPair.getTag(), words);
				}else {
					//initialize a list of words for this tag.
					List<String> words = new Vector<String>();
					words.add(wordTagPair.getWord());
					//put tag with corresponding word list.
					reverseTagWordsMap.put(wordTagPair.getTag(), words);
				}
				
				
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
		return getPosTagger().convert(morphologicalSentence);
	}

	public File getInputFile() {
		return inputFile;
	}

	public IPosTagger getPosTagger() {
		return posTagger;
	}
}
