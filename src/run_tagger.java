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


public class run_tagger{

        static String testFile = "";
        static String modelFile = "";
        static String outputFile = "";

        static Model model = new Model();

        public static String[] viterbiTagger(String sentence){
            String[] tagSequence = {"ABC", "DEF", "EFG"};
            return tagSequence;
        }

        public static String applyTags(String sentence, String[] tagSequence){
            return "Hello/XXX How/ABC Are/DEF";
        }

        public static void main(String [] args){
            POSTags.initialize();					//To Intialize hashtable of POSTags to Index
            // Check if the number of arguments are correct
            if (args.length < 3) {
                System.err.println("USAGE:\tjava build_tagger <sents.test> <model_file> <sents.out>");
                System.exit(1);
            }
            // Store arguments in variables
            testFile = args[0];
            modelFile = args[1];
            outputFile = args[2];

            try {
			// Load model_file
    			ObjectInputStream inputStream = null;
    			inputStream = new ObjectInputStream(new FileInputStream(modelFile));
    			Object obj = null;
    			obj = inputStream.readObject();
    			Model model = null;
    			if (obj instanceof Model) {
    				model = (Model) obj;
    			}

                BufferedReader in = new BufferedReader(new FileReader(testFile));
                PrintWriter writer = new PrintWriter(new FileOutputStream(outputFile));

                while(in.ready()){
                    String sentence = in.readLine();
                    String tagSequence[] = viterbiTagger(sentence);
                    String taggedSentence = applyTags(sentence, tagSequence);
                    writer.println(taggedSentence);
                }

                in.close();
                writer.close();


            } catch (FileNotFoundException e) {
        		e.printStackTrace();
        	} catch (IOException e) {
        		e.printStackTrace();
        	} catch (ClassNotFoundException e) {
        		e.printStackTrace();
        	}
        }




}
