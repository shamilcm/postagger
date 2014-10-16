import java.util.*;
import java.io.*;

class Result{
        private Integer matches=0;
        private Integer numWords=0;

        public void incrementMatches(){
            matches++;
        }

        public void incrementNumWords(){
            numWords++;
        }

        public Integer getMatches(){
            return matches;
        }

        public Integer getNumWords(){
            return numWords;
        }

        public Double getAccuracy(){
            return matches*100.0/numWords;
        }

}

/* This class is to calculate the accuracy of the POS tagger based on a test data
*/

public class evaluate_tagger{

    static String outputTaggedFile = "";
    static String correctTaggedFile = "";
    static String resultFile = "";

    static Result result = new Result();

    // Function to count the number of correctly tagged words for each sentence
    public static void processSentence(String outSentence, String corrSentence){
        String[] outWordTags = outSentence.split(" ");
        String[] corrWordTags = corrSentence.split(" ");
        // Check for sentence length mismatch
        if(outWordTags.length != corrWordTags.length){
                System.out.println("Output file and correctly tagged file mismatch! Exiting... ");
                System.exit(0);
        }
        //Loop through the word-tag pairs
        for(int i=0; i<outWordTags.length;i++){
            //Split the word and the tag for output and correct word-tag pairs
            String outWordTag[] = outWordTags[i].split("/");
            String corrWordTag[] = corrWordTags[i].split("/");
            //Get the output tag and correct tag for each word
            String outTag = outWordTag[outWordTag.length - 1];
            String corrTag = corrWordTag[corrWordTag.length - 1];
            // Increment the matches by 1 if the output tag is equal to the correct tag
            if(outTag.equals(corrTag)){
                result.incrementMatches();
            }
            // Increment the number of words enountered by 1
            result.incrementNumWords();
        }
    }

    public static void main(String [] args){
        if (args.length < 3) {
            System.err.println("USAGE:\tjava build_tagger <sents.output> <sents.correct> <result_file>");
            System.exit(1);
        }
        // Store arguments in variables
        outputTaggedFile = args[0];
        correctTaggedFile = args[1];
        resultFile = args[2];
        try {
            BufferedReader inOutput = new BufferedReader(new FileReader(outputTaggedFile));
            BufferedReader inCorrect = new BufferedReader(new FileReader(correctTaggedFile));
            PrintWriter writer = new PrintWriter(new FileOutputStream(resultFile));

            while(inOutput.ready() && inCorrect.ready()){
                String outSentence = inOutput.readLine();
                String corrSentence = inCorrect.readLine();
                processSentence(outSentence, corrSentence);
            }
            writer.println("Correctly Tagged words:\t" + result.getMatches());
            writer.println("Total words:\t" + result.getNumWords());
            writer.println("Accuracy:\t" + result.getAccuracy() + "%");

            inOutput.close();
            inCorrect.close();
            writer.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
