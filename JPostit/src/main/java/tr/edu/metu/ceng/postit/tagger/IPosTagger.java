package tr.edu.metu.ceng.postit.tagger;

import java.io.IOException;

import tr.edu.metu.ceng.postit.data.WordTagPair;
import tr.edu.metu.ceng.postit.evaluation.Evaluation;

public interface IPosTagger {

	public WordTagPair convert(String analysis);
	
	public String tag(String sentence, String trainingFilePath) throws IOException;
	
	public String tag(String sentence) throws IOException;
	
	//used for evaluation...
	public Evaluation evaluate(String trainingFilePath, String developmentFilePath) throws IOException, CloneNotSupportedException;	
}
