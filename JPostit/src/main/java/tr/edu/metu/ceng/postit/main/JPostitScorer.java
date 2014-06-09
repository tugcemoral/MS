package tr.edu.metu.ceng.postit.main;

import java.io.IOException;

import tr.edu.metu.ceng.postit.evaluation.Evaluation;
import tr.edu.metu.ceng.postit.tagger.IPosTagger;
import tr.edu.metu.ceng.postit.tagger.PosTagger1;
import tr.edu.metu.ceng.postit.tagger.PosTagger2;
import tr.edu.metu.ceng.postit.tagger.PosTagger3;

/**
 * A scorer for POS tagging. It reports overall tagging accuracy.
 */
public class JPostitScorer {

	private static final String MERGED_FILE = JPostitScorer.class.getResource(
			"/merged.txt").getFile();
	private static final String TRAIN_FILE = JPostitScorer.class.getResource(
			"/training.txt").getFile();
	private static final String DEVELOPMENT_FILE = JPostitScorer.class.getResource(
			"/development.txt").getFile();

	public static void main(String[] args) throws IOException, CloneNotSupportedException {

		JPostitScorer scorer = new JPostitScorer();
		scorer.scorePosTagger(new PosTagger1());
		scorer.scorePosTagger(new PosTagger2());
		scorer.scorePosTagger(new PosTagger3());
	}

	private void scorePosTagger(IPosTagger posTagger) throws IOException, CloneNotSupportedException {

		System.out
				.println("Scores for " + posTagger.getClass().getSimpleName() + ":");
		// train : merged, dev: merged.
		System.out.print("Merged / Merged : ");
		System.out.println(posTagger.evaluate(MERGED_FILE, MERGED_FILE));
		// train : merged, dev: train.
		System.out.print("Merged / Train : ");
		System.out.println(posTagger.evaluate(MERGED_FILE, TRAIN_FILE));
		// train : merged, dev: development.
		System.out.print("Merged / Dev : ");
		System.out.println(posTagger.evaluate(MERGED_FILE, DEVELOPMENT_FILE));
		
		// train : train, dev: merged.
		System.out.print("Train / Merged : ");
		System.out.println(posTagger.evaluate(TRAIN_FILE, MERGED_FILE));
		// train : train, dev: train.
		System.out.print("Train / Train : ");
		System.out.println(posTagger.evaluate(TRAIN_FILE, TRAIN_FILE));
		// train : train, dev: development.
		System.out.print("Train / Dev : ");
		System.out.println(posTagger.evaluate(TRAIN_FILE, DEVELOPMENT_FILE));

		// train : development, dev: merged.
		System.out.print("Dev / Merged : ");
		System.out.println(posTagger.evaluate(DEVELOPMENT_FILE, MERGED_FILE));
		// train : development, dev: train.
		System.out.print("Dev / Train : ");
		System.out.println(posTagger.evaluate(DEVELOPMENT_FILE, TRAIN_FILE));
		// train : development, dev: development.
		System.out.print("Dev / Dev : ");
		System.out.println(posTagger.evaluate(DEVELOPMENT_FILE, DEVELOPMENT_FILE));
		
		System.out.println("--------------------------------");
		System.out.println();
	}
}
