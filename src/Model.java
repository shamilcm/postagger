import java.io.Serializable;
import java.util.*;


class Model implements Serializable{
    private Hashtable<String, Integer> tagUnigramCounts;
    private Hashtable<String, Integer> tagBigramCounts;
    private Hashtable<String, Integer> tagTrigramCounts;

    private Hashtable<String, Integer> wordCounts = new Hashtable<String, Integer>();
    private Hashtable<String, Integer> wordTagCounts;
    private Hashtable<String, Integer> wordTagTypes;  //For a given tag, number of word types with that tag
    private Hashtable<String, Integer> tagBigramTypes;

    //Counts
    private Integer numTagUnigrams = 0;     // Sum(i) C(t_i)


    //Tag smoothing parameters
    private Double[] lambda = {0.8, 0.2};

    public Model(){
        tagUnigramCounts = new Hashtable<String, Integer>();
        tagBigramCounts = new Hashtable<String, Integer>();
        tagTrigramCounts = new Hashtable<String, Integer>();
        wordTagCounts = new Hashtable<String, Integer>();
        wordTagTypes = new Hashtable<String, Integer>();
        tagBigramTypes = new Hashtable<String, Integer>();


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

            // A new bigram is seen. Updating the seen bigram count for prevTag
            Integer typesCount = tagBigramTypes.get(prevTag);
            if (typesCount == null) {
                tagBigramTypes.put(prevTag, 1);
            }
            else{
                tagBigramTypes.put(prevTag, typesCount + 1);
            }

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

            Integer typesCount = wordTagTypes.get(tag);
            if (typesCount == null) {
                wordTagTypes.put(tag, 1);
            }
            else{
                wordTagTypes.put(tag, typesCount + 1);
            }
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

    public Integer getWordTagTypes(String tag){
        String keyString = tag;
        Integer count = wordTagTypes.get(tag);
        if (count == null) {
            return 0;
        }
        return count;
    }

    public Integer getTagBigramTypes(String tag){
        String keyString = tag;
        Integer count = tagBigramTypes.get(tag);
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
    public Double getTransitionProbabilityLog(String prevTag, String tag){
        String prevPrevTag = "";
        Double trigramProbability = 0.0;
        Double bigramProbability = 0.0;
        Double unigramProbability = 0.0;

        /*if(tag == "<END>"){
            if(getTagBigramCounts(prevTag, tag) > 0)
                bigramProbability = getTagBigramCounts(prevTag, tag) * 1.0 / getTagUnigramCounts(prevTag);
            return Math.log(bigramProbability);
        }*/


        // Trigram Conditional Probability: P(tag | prevPrevTag, prevTag)
        if(getTagTrigramCounts(prevPrevTag, prevTag, tag) > 0)
            trigramProbability = getTagTrigramCounts(prevPrevTag, prevTag, tag) * 1.0 / getTagBigramCounts(prevPrevTag, prevTag);


        // Bigram Conditional Probability: P(tag | prevTag)
        if(getTagBigramCounts(prevTag, tag) > 0)
            bigramProbability = getTagBigramCounts(prevTag, tag) * 1.0 / getTagUnigramCounts(prevTag);

        // Unigram Probability: P(tag)
        if(getTagUnigramCounts(tag) > 0)
            unigramProbability = getTagUnigramCounts(tag) * 1.0 / getNumTagUnigrams();

        //Witten-Bell smoothing parameters
        Integer v = tagUnigramCounts.size();
        Integer t = getTagBigramTypes(prevTag);
        Integer z = v - t;

        if(getTagBigramCounts(prevTag, tag) > 0){
            bigramProbability =  getTagBigramCounts(prevTag, tag) * 1.0 / (getTagUnigramCounts(prevTag) + t);

        }
        else{
            bigramProbability = (t*1.0) / (z * ( getTagUnigramCounts(prevTag) + t ));
        }
/*
        if(bigramProbability > 1.0){
            System.out.println("PrevTag:" +  prevTag);
            System.out.println("Tag:" +  tag);
            System.out.println("getTagBigramCounts:" +  getTagBigramCounts(prevTag, tag));
            System.out.println("getTagBigramCounts:" +  getTagUnigramCounts(prevTag)  );
            System.out.println("z:" +  z);
            System.out.println("v:" +  v);
            System.out.println("t:" +  t);

        }
*/
        Double transitionProbability = bigramProbability;

        return Math.log(transitionProbability);
    }

    public Double getObservationProbabilityLog(String word, String tag){
        Double observationProbability = 0.0;

        //Witten-Bell smoothing parameters
        Integer v = wordCounts.size();
        Integer t = getWordTagTypes(tag);
        Integer z = v - t;

        if(getWordTagCounts(word, tag) > 0){
            observationProbability = getWordTagCounts(word, tag) * 1.0 / ( getTagUnigramCounts(tag) + t);
        }
        else{
            observationProbability = (t*1.0) / (z * ( getTagUnigramCounts(tag) + t ));
        }
/*
        if(observationProbability > 1.0){
            System.out.println("Word:" +  word);
            System.out.println("Tag:" +  tag);
            System.out.println("getWordTagCounts:" +  getWordTagCounts(word, tag));
            System.out.println("getTagUnigramCounts:" +  getTagUnigramCounts(tag)  );
            System.out.println("z:" +  z);
            System.out.println("v:" +  v);
            System.out.println("t:" +  t);

        }
*/
        return Math.log(observationProbability);
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
