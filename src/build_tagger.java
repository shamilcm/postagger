import java.io.Serializable;
import java.util.*;
import java.io.*;


public class build_tagger{

	static String trainingFile = "";
	static String devFile = "";
	static String modelFile = "";
	static Model model = new Model();

	public static void processSentence(String sentence){
		String[] wordTags = sentence.split(" ");

		model.incrementTagUnigramCounts("<START>");

		String prevTag = "<START>";

		for(int i=0; i<wordTags.length;i++){
			String wordTag[] =wordTags[i].split("/");
			String word = "";
			for (int j = 0; j < wordTag.length-1; j++) {
			   word = word + wordTag[j];
			}
			//if(i<wordTags.length-1) System.out.print(word + " ");
			//else System.out.println(word);
			String tag = wordTag[wordTag.length - 1];

		// Increment all counts in the model object
			model.incrementTagBigramCounts(prevTag, tag);
			model.incrementTagUnigramCounts(tag);
			model.incrementWordCounts(word);
			model.incrementWordTagCounts(word, tag);
		// Updating the previous 2 tags
			prevTag = tag;
		}
		model.incrementTagBigramCounts(prevTag, "<END>");

	}

	public static void main(String [] args){

		// Check if the number of arguments are correct
		if (args.length < 3) {
			System.err.println("USAGE:\tjava build_tagger <sents.train> <sents.devt> <model_file>");
			System.exit(1);
		}
		// Store arguments in variables
		trainingFile = args[0];
		devFile = args[1];
		modelFile = args[2];

		//Read from the file to update the counts
		try {
			BufferedReader in = new BufferedReader(new FileReader(trainingFile));
			while(in.ready()){
				String sentence = in.readLine();
				processSentence(sentence);
			}

			// Saving model to model_file
			ObjectOutputStream outputStream = null;
			try {
				outputStream = new ObjectOutputStream(new FileOutputStream(modelFile));
				outputStream.writeObject(model);
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					if (outputStream != null) {
						outputStream.flush();
						outputStream.close();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
