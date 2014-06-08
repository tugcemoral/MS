package tr.edu.metu.ceng.postit.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Vector;

import tr.edu.metu.ceng.postit.tagger.IPosTagger;

public class CorpusParser {

	private File inputFile;

	private IPosTagger posTagger;

	public CorpusParser(IPosTagger posTagger, String inputFilePath)
			throws FileNotFoundException {
		this(posTagger, new File(inputFilePath));
	}

	public CorpusParser(IPosTagger posTagger, File inputFile)
			throws FileNotFoundException {
		if (!inputFile.exists()) {
			throw new FileNotFoundException(MessageFormat.format(
					"Given input file {0} does not exists!",
					inputFile.getPath()));
		}
		this.inputFile = inputFile;
		this.posTagger = posTagger;
	}

	public static Sentence parseSentence(String sentenceStr) {
		Sentence sentence = new Sentence();
		String[] probableWords = sentenceStr.split("\\s+");
		int index = 0;
		for (String probableWord : probableWords) {
			sentence.addToken(new Token(index++, new WordTagPair(probableWord,
					null)));
		}
		return sentence;
	}

	public Corpus parse() {
		List<Sentence> sentences = new Vector<Sentence>();

		// construct alphabet buffered reader w/ alphabet file reader.
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(getInputFile()));
			String line = null;

			Sentence sentence = new Sentence();
			int index = 0;
			while ((line = br.readLine()) != null) {
				// parse the line wrt associated pos tagger.
				WordTagPair wordTagPair = this.getPosTagger().convert(line);

				// construct sentences list.
				if (wordTagPair.getWord().equals(".")) {
					// add token to sentence.
					sentence.addToken(new Token(index++, wordTagPair));
					// finalize sentence and store it.
					sentences.add(sentence);
					// initialize for new sentence.
					sentence = new Sentence();
					index = 0;
				} else {
					// add token to sentence.
					sentence.addToken(new Token(index++, wordTagPair));
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
		return new Corpus(sentences);
	}

//	 public MorphAnalysisResult parse() {
//	 //create the return type analysis result object.
//	 MorphAnalysisResult mar = new MorphAnalysisResult();
//	 // construct alphabet buffered reader w/ alphabet file reader.
//	 BufferedReader br = null;
//	 try {
//	 br = new BufferedReader(new FileReader(getInputFile()));
//	 String line = null;
//	
//	 //list of sentences.
//	 List<String> sentences = new Vector<String>();
//	 //map of reverse tag-words-list.
//	 Map<String, List<String>> reverseTagWordsMap = new HashMap<String,
//	 List<String>>();
//	 Map<String, List<String>> lexicon = new HashMap<String, List<String>>();
//	 StringBuilder sentenceBuilder = new StringBuilder();
//	
//	 while ((line = br.readLine()) != null) {
//	 //parse the line wrt associated pos tagger.
//	 WordTagPair wordTagPair = this.getPosTagger().convert(line);
//	
//	 // construct sentences list.
//	 if(wordTagPair.getWord().equals(".")) {
//	 sentenceBuilder.append(wordTagPair.getWord());
//	 //finalize sentence and store it.
//	 sentences.add(sentenceBuilder.toString());
//	 sentenceBuilder = new StringBuilder();
//	 }else {
//	 sentenceBuilder.append(wordTagPair.getWord()).append(" ");
//	 }
//	
//	 // construct reverse tag - words-list map.
//	 if(reverseTagWordsMap.containsKey(wordTagPair.getTag())) {
//	 //update existing tag's word list.
//	 List<String> words = reverseTagWordsMap.get(wordTagPair.getTag());
//	 words.add(wordTagPair.getWord());
//	 reverseTagWordsMap.put(wordTagPair.getTag(), words);
//	 }else {
//	 //initialize alphabet list of words for this tag.
//	 List<String> words = new Vector<String>();
//	 words.add(wordTagPair.getWord());
//	 //put tag with corresponding word list.
//	 reverseTagWordsMap.put(wordTagPair.getTag(), words);
//	 }
//	
//	 //construct word - tags-list map
//	 if(lexicon.containsKey(wordTagPair.getWord())) {
//	 //update existing tag's word list.
//	 List<String> tags = lexicon.get(wordTagPair.getWord());
//	 tags.add(wordTagPair.getTag());
//	 lexicon.put(wordTagPair.getWord(), tags);
//	 }else {
//	 //initialize alphabet list of words for this tag.
//	 List<String> tags = new Vector<String>();
//	 tags.add(wordTagPair.getTag());
//	 //put tag with corresponding word list.
//	 lexicon.put(wordTagPair.getWord(), tags);
//	 }
//	 }
//	
//	 //set constructed structures to MAR
//	 mar.setSentences(sentences);
//	 mar.setReverseTagWordsMap(reverseTagWordsMap);
//	 mar.setLexicon(lexicon);
//	 } catch (FileNotFoundException e) {
//	 // already checked, no need to re-check again.
//	 } catch (IOException ioe) {
//	 // that's interesting...
//	 ioe.printStackTrace();
//	 } finally {
//	 try {
//	 br.close();
//	 } catch (IOException e) {
//	 // what can i do, sometimes.
//	 }
//	 }
//	 return mar;
//	 }

	public File getInputFile() {
		return inputFile;
	}

	public IPosTagger getPosTagger() {
		return posTagger;
	}
}
