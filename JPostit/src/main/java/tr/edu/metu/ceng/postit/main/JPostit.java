package tr.edu.metu.ceng.postit.main;

import java.text.MessageFormat;

import tr.edu.metu.ceng.postit.tagger.IPosTagger;
import tr.edu.metu.ceng.postit.tagger.PosTagger1;
import tr.edu.metu.ceng.postit.tagger.PosTagger2;
import tr.edu.metu.ceng.postit.tagger.PosTagger3;

public class JPostit {

	private static final String DEFAULT_TRAINING_FILE_PATH = "./training.txt";

	private static final String WRONG_USAGE = "Usage: java -jar jpostit.jar (-t /path/to/training.txt [optional]) -pt (postagger1|postagger2|postagger3) -o (convert|tag) \"sentence\"";

	public static void main(String[] args) {
		JPostit jpostit = new JPostit();
		jpostit.start(args);
	}

	private void start(String[] args) {
		String trainFileInputCommand = null;
		String trainFilePath = null;

		String posTaggerCommand = null;
		String posTaggerName = null;

		String operationCommand = null;
		String operation = null;
		String sentence = null;

		if (args.length == 7) {
			trainFileInputCommand = args[0];
			trainFilePath = args[1];
			posTaggerCommand = args[2];
			posTaggerName = args[3];
			operationCommand = args[4];
			operation = args[5];
			sentence = args[6];
		} else if (args.length == 5) {
			trainFileInputCommand = "-t";
//			trainFilePath = getClass().getResource(DEFAULT_TRAINING_FILE_PATH)
//					.getFile();
			trainFilePath = DEFAULT_TRAINING_FILE_PATH;
			posTaggerCommand = args[0];
			posTaggerName = args[1];
			operationCommand = args[2];
			operation = args[3];
			sentence = args[4];
		} else {
			printErrorAndExit(MessageFormat
					.format("Unknown input parameters! {0} parameters are not acceptable",
							args.length));
		}

		if (trainFileInputCommand.equals("-t")
				&& posTaggerCommand.equals("-pt")
				&& operationCommand.equals("-o")) {
			try {
				IPosTagger posTagger = initializePosTagger(posTaggerName);
				if (operation.equals("convert")) {
					System.out.println(posTagger.convert(sentence));
				} else if (operation.equals("tag")) {
					System.out.println(posTagger.tag(sentence, trainFilePath));
				} else {
					throw new Exception(MessageFormat.format(
							"Command {0} not supported", operation));
				}
			} catch (Exception e) {
				printErrorAndExit(e.getMessage());
			}
		} else {
			printErrorAndExit("Probably order of commands are false!");
		}

		// gracefully exit.
		System.exit(0);
	}

	private IPosTagger initializePosTagger(String taggerName) throws Exception {
		if (taggerName.equalsIgnoreCase("postagger1")) {
			return new PosTagger1();
		} else if (taggerName.equalsIgnoreCase("postagger2")) {
			return new PosTagger2();
		} else if (taggerName.equalsIgnoreCase("postagger3")) {
			return new PosTagger3();
		} else {
			throw new Exception(MessageFormat.format(
					"PoSTagger {0} not found!", taggerName));
		}
	}

	private void printErrorAndExit(String message) {
		System.err.println(WRONG_USAGE);
		if (message != null) {
			System.err.println(message);
		}
		System.exit(0);
	}
}
