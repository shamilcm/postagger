import java.io.Serializable;
import java.util.*;
import java.io.*;
import java.lang.*;

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

    public static String getTag(Integer index){
            return posTagsList[index];
    }

}


public class run_tagger{

        static String testFile = "";
        static String modelFile = "";
        static String outputFile = "";

        static Model model;

        public static String[] viterbiTagger(String sentence){
            String[] words = sentence.split(" ");
            Integer numWords = words.length;
            Integer numTags = POSTags.posTagsList.length;

            String[] tagSequence = new String[numWords];
            Integer[] tagIndexSequence = new Integer[numWords];

            Double[][] viterbiMatrix = new Double[numTags][numWords];
		    int[][] backPointers = new int[numTags][numWords];


            System.out.println(numTags);
            // Initialization Steps
            for (int  tagIndex = 0; tagIndex < numTags; tagIndex++) {

                viterbiMatrix[tagIndex][0] =    model.getTransitionProbabilityLog("<START>", POSTags.getTag(tagIndex))
                                                            + model.getObservationProbabilityLog(words[0], POSTags.getTag(tagIndex));
                backPointers[tagIndex][0] = -1;
            }

            // Filling hte matrix
            for(int wordIndex = 1; wordIndex < numWords; wordIndex++){
                for(int tagIndex = 0; tagIndex < numTags; tagIndex++){

                    //Find minimum in the previous column (set of tags). Minimum log probability = maximum probability
                    Double value = Double.NEGATIVE_INFINITY;
                    Integer backPointer = 0;

                    for(int prevTagIndex = 0; prevTagIndex < numTags; prevTagIndex++){
                        Double transProbability = viterbiMatrix[prevTagIndex][wordIndex-1]
                                                    + model.getTransitionProbabilityLog(POSTags.getTag(prevTagIndex), POSTags.getTag(tagIndex));
                        if(transProbability > value){
                            value = transProbability ;
                            backPointer = prevTagIndex;
                        }
                    }
                    viterbiMatrix[tagIndex][wordIndex] = value +  model.getObservationProbabilityLog(words[wordIndex], POSTags.getTag(tagIndex));
                    backPointers[tagIndex][wordIndex] = backPointer;

                }
            }

            // Termination Step
            Double value = Double.NEGATIVE_INFINITY;
            Integer backPointer = 0;

            for(int prevTagIndex = 0; prevTagIndex < numTags; prevTagIndex++){
                Double transProbability = viterbiMatrix[prevTagIndex][numWords-1]
                                            + model.getTransitionProbabilityLog(POSTags.getTag(prevTagIndex), "<END>");
                if(transProbability > value){
                    value = transProbability ;
                    backPointer = prevTagIndex;
                }
            }

            System.out.print("\t\t\t");
            for(int j=0; j < numWords; j++){
                System.out.print(" " + words[j] + "\t" );
            }
            System.out.println("");
            for(int i=0; i<numTags; i++){
                System.out.print(POSTags.getTag(i) + "\t\t\t");
                for(int j=0; j < numWords; j++){
                    System.out.printf("%.2f", viterbiMatrix[i][j]);
                    System.out.print( "\t" );
                }
                System.out.println("");
            }


            for(int i=0; i<numTags; i++){
                System.out.print(POSTags.getTag(i) + "\t\t\t");
                for(int j=1; j < numWords; j++){
                    System.out.print(POSTags.getTag(backPointers[i][j]) + "\t" );
                }
                System.out.println("");
            }



            // Trace Back for tag index sequence
            tagIndexSequence[numWords-1] = backPointer;
            for(int i = numWords-2; i>=0; i--){
                tagIndexSequence[i] = backPointers[backPointer][i+1];
                backPointer = tagIndexSequence[i];
            }

            //Convert Tag Index Sequence to Tag String Sequence
            for(int i = 0; i < numWords; i++){
                tagSequence[i] = POSTags.getTag(tagIndexSequence[i]);
            }

            return tagSequence;
        }

        public static String applyTags(String sentence, String[] tagSequence){
            String[] words = sentence.split(" ");
            String taggedSentence = "";
            Integer numWords = words.length;

            for(int i = 0; i < numWords; i++){
                taggedSentence = taggedSentence + " " + words[i] + "/" + tagSequence[i];
            }
            return taggedSentence;
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
    			if (obj instanceof Model) {
    				model = (Model) obj;
    			}
                System.out.println(model.getTransitionProbabilityLog("<START>","CC"));
                model.printModel();

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
