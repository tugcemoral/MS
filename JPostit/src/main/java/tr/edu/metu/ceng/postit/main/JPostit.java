package tr.edu.metu.ceng.postit.main;

import java.text.MessageFormat;

import tr.edu.metu.ceng.postit.tagger.IPosTagger;
import tr.edu.metu.ceng.postit.tagger.PosTagger1;
import tr.edu.metu.ceng.postit.tagger.PosTagger2;
import tr.edu.metu.ceng.postit.tagger.PosTagger3;

public class JPostit {

	private static final String WRONG_USAGE = "Usage: java -jar jpostit.jar -t (postagger1|postagger2|postagger3) -o (convert|tag) \"sentence\"";

	public static void main(String[] args) {
		JPostit jpostit = new JPostit();
		jpostit.start(args);
	}

	private void start(String[] args) {
		if (args.length != 5) {
			printErrorAndExit(null);
		} else {
			if (args[0].equals("-t") && args[2].equals("-o")) {
				try {
					IPosTagger posTagger = initializePosTagger(args[1]);
					if (args[3].equals("convert")) {
						System.out.println(posTagger.convert(args[4]));
					} else if (args[3].equals("tag")) {
						System.out.println(posTagger.tag(args[4]));
					} else {
						throw new Exception(MessageFormat.format("Command {0} not supported", args[3]));
					}
				} catch (Exception e) {
					printErrorAndExit(e.getMessage());
				}
			} else {
				printErrorAndExit("Probably order of commands are false!");
			}
		}
		//gracefully exit.
		System.exit(0);
	}

	private IPosTagger initializePosTagger(String taggerName) throws Exception{
		if(taggerName.equalsIgnoreCase("postagger1")){
			return new PosTagger1();
		}else if(taggerName.equalsIgnoreCase("postagger2")){
			return new PosTagger2();
		}else if(taggerName.equalsIgnoreCase("postagger3")){
			return new PosTagger3();
		}else {
			throw new Exception(MessageFormat.format("PoSTagger {0} not found!", taggerName));
		}
	}

	private void printErrorAndExit(String message) {
		System.err.println(WRONG_USAGE);
		if(message != null){
			System.err.println(message);
		}
		System.exit(0);
	}
}
