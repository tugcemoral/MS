package tr.edu.metu.ceng.postit.test.tagger;

import static org.hamcrest.core.Is.is;

import org.junit.Assert;

import tr.edu.metu.ceng.postit.tagger.IPosTagger;
import tr.edu.metu.ceng.postit.tagger.WordTagPair;

public abstract class PosTaggerTestBase {

	protected static final String NOUN_SENTENCE_1 = "filmin ( film ) film+Noun+A3sg+Pnon(+nHn )+Gen";
	protected static final String ADVERB_SENTENCE = "en ( en ) en+Adverb";
	protected static final String NOUN_SENTENCE_2 = "can ( can ) can+Noun+A3sg+Pnon+Nom";
	protected static final String VERB_TO_ADJ_SENTENCE_1 = "alici ( al ) al+Verb+Pos(+yHcH )^DB+Adj+Agt";
	protected static final String NOUN_SENTENCE_3 = "noktasi ( nokta ) nokta+Noun+A3sg(+sH )+P3sg+Nom";
	protected static final String VERB_TO_VERB_TO_ADJ_SENTENCE = "islenmis ( iSle ) isle+Verb(+Hn )^DB+Verb+Pass+Pos(+mHS )^DB+Adj+NarrPart";
	protected static final String VERB_TO_ADJ_SENTENCE_2 = "olan ( ol ) ol+Verb+Pos(+yAn )^DB+Adj+PresPart";
	protected static final String NOUN_SENTENCE_4 = "konunun ( konu ) konu+Noun+A3sg+Pnon(+nHn )+Gen";
	protected static final String VERB_TO_VERB_TO_NOUN_SENTENCE = "islenis ( iSle ) isle+Verb(+Hn )^DB+Verb+Pass+Pos(+yHS )^DB+Noun+Inf3+A3sg+Pnon+Nom";
	protected static final String NOUN_SENTENCE_5 = "sekli ( Sekil ) sekil+Noun+A3sg(+sH )+P3sg+Nom";
	protected static final String PUNCTUATION_SENTENCE = ".	.	+?";

	protected abstract IPosTagger getPosTagger();
	
	protected void assertWordTagPair(String morphologicSentence,
			String expectedWord, String expectedTag) {
		// initialize pos tagger.
		IPosTagger posTagger = getPosTagger();
		// convert the sentence to word tag pair.
		WordTagPair wtp = posTagger.convert(morphologicSentence);
		// assert word and tags.
		Assert.assertThat(wtp.getWord(), is(expectedWord));
		Assert.assertThat(wtp.getTag(), is(expectedTag));
	}
}
