import java.io.Serializable;
import java.util.*;
import java.io.*;


class POSTags{
	static String[] posTagsList = {
			"CC", "CD", "DT", "EX", "FW", "IN", "JJ", "JJR", "JJS",         //Tags from 1-9
			"LS", "MD", "NN", "NNS", "NNP", "NNPS", "PDT", "POS", "PRP",    //Tags from 10-18
			"PRP$", "RB", "RBR", "RBS", "RP", "SYM", "TO", "UH", "VB",     //Tags from 19-27
			"VBD", "VBG", "VBN", "VBP", "VBZ", "WDT", "WP", "WP$", "WRB",   //Tags from 27-36
			"$", "#", "``", "''", "-LRB-", "-RRB-", ",", ".", ":" };       //Tags from 36-45

	static Hashtable<String, Integer> posToIndex = new Hashtable<String, Integer>();

/* To initialize Hashtable of POSTags to corresponding indices.
 */
	public static void initialize(){
		for(int i=0;i<45;i++){
			posToIndex.put(posTagsList[i],i);
		}

	}

	public static Integer getIndex(String posTag ){
		return posToIndex.get(posTag);
	}

	public static String getPOSTag(Integer index){
			return posTagsList[index];
	}

}


class Model implements Serializable{
	private Hashtable<String, Integer> tagUnigramCounts;
	private Hashtable<String, Integer> tagBigramCounts;
	private Hashtable<String, Integer> tagTrigramCounts;

	private Hashtable<String, Integer> wordCounts = new Hashtable<String, Integer>();
	private Hashtable<String, Integer> wordTagCounts;

	public Model(){
		tagUnigramCounts = new Hashtable<String, Integer>();
		tagBigramCounts = new Hashtable<String, Integer>();
		tagTrigramCounts = new Hashtable<String, Integer>();
		wordTagCounts = new Hashtable<String, Integer>();
	}

	public void incrementTagTrigramCounts(String tag, String prevTag, String prevPrevTag){
		String keyString = prevPrevTag + "," + prevTag + "," + tag;
		Integer count = tagTrigramCounts.get(keyString);
		if (count == null) {
		    tagTrigramCounts.put(keyString, 1);
		}
		else {
		    tagTrigramCounts.put(keyString, count + 1);
		}
	}

	public Integer getTagTrigramCounts(String tag, String prevTag, String prevPrevTag){
		String keyString = prevPrevTag + "," + prevTag + "," + tag;
		Integer count = tagTrigramCounts.get(keyString);
		if (count == null) {
			return 0;
		}
		return count;
	}


	public void incrementTagBigramCounts(String tag, String prevTag){
		String keyString = prevTag + "," + tag;
		Integer count = tagBigramCounts.get(keyString);
		if (count == null) {
			tagBigramCounts.put(keyString, 1);
		}
		else {
			tagBigramCounts.put(keyString, count + 1);
		}
	}

	public Integer getTagBigramCounts(String tag, String prevTag){
		String keyString = prevTag + "," + tag;
		Integer count = tagBigramCounts.get(keyString);
		if (count == null) {
			return 0;
		}
		return count;
	}

	public void incrementTagUnigramCounts(String tag){
		String keyString = tag;
		Integer count = tagUnigramCounts.get(keyString);
		if (count == null) {
			tagUnigramCounts.put(keyString, 1);
		}
		else {
			tagUnigramCounts.put(keyString, count + 1);
		}
	}

	public Integer getTagUnigramCounts(String tag){
		String keyString = tag;
		Integer count = tagUnigramCounts.get(keyString);
		if (count == null) {
			return 0;
		}
		return count;
	}

	public void incrementWordCounts(String word){
		String keyString = word;
		Integer count = wordCounts.get(keyString);
		if (count == null) {
			wordCounts.put(keyString, 1);
		}
		else {
			wordCounts.put(keyString, count + 1);
		}
	}

	public Integer getWordCounts(String word){
		String keyString = word;
		Integer count = wordCounts.get(keyString);
		if (count == null) {
			return 0;
		}
		return count;
	}

	public void incrementWordTagCounts(String word, String tag){
		String keyString = word + "," + tag;
		Integer count = wordTagCounts.get(keyString);
		if (count == null) {
			wordTagCounts.put(keyString, 1);
		}
		else {
			wordTagCounts.put(keyString, count + 1);
		}
	}

	public Integer getWordTagCounts(String word, String tag){
		String keyString = word + "," + tag;
		Integer count = wordTagCounts.get(keyString);
		if (count == null) {
			return 0;
		}
		return count;
	}


	public void printtable(){
	}
}


public class build_tagger{

	static String trainingFile = "";
	static String devFile = "";
	static String modelFile = "";
	static Model model = new Model();

	public static void processSentence(String sentence){
		String[] wordTags = sentence.split(" ");
		String prevPrevTag = "<PRESTART>";
		String prevTag = "<START>";
		for(int i=0; i<wordTags.length;i++){
			String wordTag[] =wordTags[i].split("/");
			String word = wordTag[0];
			String tag = wordTag[1];
		// Increment all counts in the model object
			model.incrementTagTrigramCounts(tag, prevTag, prevPrevTag);
			model.incrementTagBigramCounts(tag, prevTag);
			model.incrementTagUnigramCounts(tag);
			model.incrementWordCounts(word);
			model.incrementWordTagCounts(word, tag);
		// Updating the previous 2 tags
			prevPrevTag = prevTag;
			prevTag = tag;
		}
		model.incrementTagBigramCounts("<END>", prevTag);
	}

	public static void main(String [] args){
		POSTags.initialize();					//To Intialize hashtable of POSTags to Index
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
