import java.util.*;

public class POSTags{
    static String[] posTagsList = {
            "CC", "CD", "DT", "EX", "FW", "IN", "JJ", "JJR", "JJS",         //Tags from 1-9
            "LS", "MD", "NN", "NNS", "NNP", "NNPS", "PDT", "POS", "PRP",    //Tags from 10-18
             "PRP$", "RB", "RBR", "RBS", "RP", "SYM", "TO", "UH", "VB",     //Tags from 19-27
			"VBD", "VBG", "VBN", "VBP", "VBZ", "WDT", "WP", "WP$", "WRB",   //Tags from 27-36
             "$", "#", "``", "''", "-LRB-", "-RRB-", ",", ".", ":" };       //Tags from 36-45

    static Hashtable<String, Integer> posToIndex = new Hashtable<String, Integer>;

    public static posInitialize(){
        for(int i=1;i<=45;i++){

        }

    }

    public static getIndex(String posTag ){

    }
    
    public static getPOSTag(String index){

    }


}
