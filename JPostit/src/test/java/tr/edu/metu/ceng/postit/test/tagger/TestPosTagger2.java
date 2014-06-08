package tr.edu.metu.ceng.postit.test.tagger;

import org.junit.Test;

import tr.edu.metu.ceng.postit.tagger.IPosTagger;
import tr.edu.metu.ceng.postit.tagger.PosTagger2;

public class TestPosTagger2 extends PosTaggerTestBase {

	@Test
	public void testConvertNounWithSingularHighVowelSentence() throws Exception {
		assertWordTagPair(NOUN_WITH_SINGULAR_HIGHVOWEL_SENTENCE, "haftasonu", "Noun+Nom");
	}
	
	@Test
	public void testConvertNounGenSentence() throws Exception {
		assertWordTagPair(NOUN_GEN_SENTENCE, "Saat", "Noun+Gen");
	}
	
	@Test
	public void testConvertNounSentence1() throws Exception {
		assertWordTagPair(NOUN_SENTENCE_1, "filmin", "Noun+Gen");
	}

	@Test
	public void testConvertAdverbSentence() throws Exception {
		assertWordTagPair(ADVERB_SENTENCE, "en", "Adverb");
	}

	@Test
	public void testConvertNounSentence2() throws Exception {
		assertWordTagPair(NOUN_SENTENCE_2, "can", "Noun+Nom");
	}

	@Test
	public void testConvertVerbToAdjSentence1() throws Exception {
		assertWordTagPair(VERB_TO_ADJ_SENTENCE_1, "alici", "Adj");
	}

	@Test
	public void testConvertNounSentence3() throws Exception {
		assertWordTagPair(NOUN_SENTENCE_3, "noktasi", "Noun+Nom");
	}

	@Test
	public void testConvertVerbToVerbToAdjSentence2() throws Exception {
		assertWordTagPair(VERB_TO_VERB_TO_ADJ_SENTENCE, "islenmis", "Adj");
	}

	@Test
	public void testConvertVerbToAdjSentence2() throws Exception {
		assertWordTagPair(VERB_TO_ADJ_SENTENCE_2, "olan", "Adj");
	}

	@Test
	public void testConvertNounSentence4() throws Exception {
		assertWordTagPair(NOUN_SENTENCE_4, "konunun", "Noun+Gen");
	}

	@Test
	public void testConvertVerbToVerbToNounSentence3() throws Exception {
		assertWordTagPair(VERB_TO_VERB_TO_NOUN_SENTENCE, "islenis", "Noun+Nom");
	}

	@Test
	public void testConvertNounSentence5() throws Exception {
		assertWordTagPair(NOUN_SENTENCE_5, "sekli", "Noun+Nom");
	}

	@Test
	public void testConvertPunctuationSentence() throws Exception {
		assertWordTagPair(PUNCTUATION_SENTENCE, ".", "?");
	}

	@Override
	protected IPosTagger getPosTagger() {
		return new PosTagger2();
	}
}
