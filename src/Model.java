import java.io.Serializable;
import java.util.*;


class Model implements Serializable{
    private Hashtable<String, Integer> tagUnigramCounts;
    private Hashtable<String, Integer> tagBigramCounts;
    private Hashtable<String, Integer> tagTrigramCounts;

    private Hashtable<String, Integer> wordCounts = new Hashtable<String, Integer>();
    private Hashtable<String, Integer> wordTagCounts;

    //Counts
    private Integer numTagUnigrams = 0;     // Sum(i) C(t_i)


    //Tag smoothing parameters
    private Double[] lambda = {0.4, 0.3, 0.3};

    public Model(){
        tagUnigramCounts = new Hashtable<String, Integer>();
        tagBigramCounts = new Hashtable<String, Integer>();
        tagTrigramCounts = new Hashtable<String, Integer>();
        wordTagCounts = new Hashtable<String, Integer>();


    }

    public void incrementTagTrigramCounts(String prevPrevTag, String prevTag, String tag){
        String keyString = prevPrevTag + "," + prevTag + "," + tag;
        Integer count = tagTrigramCounts.get(keyString);
        if (count == null) {
            tagTrigramCounts.put(keyString, 1);
        }
        else {
            tagTrigramCounts.put(keyString, count + 1);
        }
    }

    public Integer getTagTrigramCounts(String prevPrevTag, String prevTag, String tag){
        String keyString = prevPrevTag + "," + prevTag + "," + tag;
        Integer count = tagTrigramCounts.get(keyString);
        if (count == null) {
            return 0;
        }
        return count;
    }


    public void incrementTagBigramCounts(String prevTag, String tag){
        String keyString = prevTag + "," + tag;
        Integer count = tagBigramCounts.get(keyString);
        if (count == null) {
            tagBigramCounts.put(keyString, 1);
        }
        else {
            tagBigramCounts.put(keyString, count + 1);
        }
    }

    public Integer getTagBigramCounts(String prevTag, String tag){
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

    public Integer getNumTagUnigrams(){
        if(numTagUnigrams == 0 ){
            for (Integer count : tagUnigramCounts.values()){
                numTagUnigrams = numTagUnigrams + count;
            }
        }
        return numTagUnigrams;
    }


/* Transition probability: Interpolated probabilty of going from prevPrevTag to prevTag to tag
*/
    public Double getTransitionProbability(String prevPrevTag, String prevTag, String tag){
        Double trigramProbability = 0.0;
        Double bigramProbability = 0.0;
        Double unigramProbability = 0.0;

        // Trigram Conditional Probability: P(tag | prevPrevTag, prevTag)
        if(getTagTrigramCounts(prevPrevTag, prevTag, tag) > 0)
            trigramProbability = getTagTrigramCounts(prevPrevTag, prevTag, tag) * 1.0 / getTagBigramCounts(prevPrevTag, prevTag);
        // Bigram Conditional Probability: P(tag | prevTag)
        if(getTagBigramCounts(prevTag, tag) > 0)
            bigramProbability = getTagBigramCounts(prevTag, tag) * 1.0 / getTagUnigramCounts(prevTag);
        // Unigram Probability: P(tag)
        if(getTagUnigramCounts(tag) > 0)
            unigramProbability = getTagUnigramCounts(tag) * 1.0 / numTagUnigrams;

        Double transitionProbability = lambda[0]*trigramProbability + lambda[1]*bigramProbability + lambda[2]*unigramProbability;

        return transitionProbability;
    }

    public Double getObservationProbability(String word, String tag){
        Double observationProbability = 0.0;
        if(getWordTagCounts(word, tag) > 0){
            observationProbability = getWordTagCounts(word, tag) * 1.0 / getTagUnigramCounts(tag);
        }
        return observationProbability;
    }

    public void printModel(){

        System.out.println("Total Number of tags:" + getNumTagUnigrams() + "\n" );
        System.out.println("tagUnigramCounts: " + tagUnigramCounts.size() + "\n");
        System.out.println(tagUnigramCounts);
        System.out.println("");
        System.out.println("tagBigramCounts: " + tagBigramCounts.size() + "\n");
        System.out.println(tagBigramCounts);
        System.out.println("");
        System.out.println("tagTrigramCounts: " + tagTrigramCounts.size() + "\n");
        System.out.println(tagTrigramCounts);
        System.out.println("");
        System.out.println("wordCounts: " + wordCounts.size() + "\n");
        System.out.println(wordCounts);
        System.out.println("");
        System.out.println("wordTagCounts: " + wordTagCounts.size() + "\n");
        System.out.println(wordTagCounts);

    }
}
