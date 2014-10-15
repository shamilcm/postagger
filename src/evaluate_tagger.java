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


public class evaluate_tagger{

    static String outputTaggedFile = "";
    static String correctTaggedFile = "";
    static String resultFile = "";

    static Result result = new Result();

    public static void processSentence(String outSentence, String corrSentence){
        String[] outWordTags = outSentence.split(" ");
        String[] corrWordTags = corrSentence.split(" ");
        if(outWordTags.length != corrWordTags.length){
                System.out.println("Output file and correctly tagged file mismatch! Exiting... ");
                System.exit(0);
        }

        for(int i=0; i<outWordTags.length;i++){
            String outWordTag[] = outWordTags[i].split("/");
            String corrWordTag[] = corrWordTags[i].split("/");

            String outTag = outWordTag[outWordTag.length - 1];
            String corrTag = corrWordTag[corrWordTag.length - 1];

            if(outTag.equals(corrTag)){
                result.incrementMatches();
            }
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
            System.out.println("Correctly Tagged words:" + result.getMatches());
            System.out.println("Total words:" + result.getNumWords());
            System.out.println("Accuracy: " + result.getAccuracy());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
