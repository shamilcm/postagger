import java.io.Serializable;
import java.util.*;


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
