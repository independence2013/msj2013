/*
 * Analyzes the lyrics of a song
 */
package prototype;

//import java.io.PrintWriter;

/**
 * @author Mitchell
 */
public class LyricsAnalyzer {
    //Analyze song lyrics
    public static int[] wordanalyze(String[] song, Keywords[] keywords) {
        String lyrics = song[2]; //create a string that holds the lyrics (index 0 has the title, index 1 has the artist)
        /*
        String lyrics = "You walked in, Caught my attention." + //debug 
                    " I've never seen A man with so much dimension." + 
                    " It's the way you walk, The way you talk, The way you" + 
                    " make me feel inside, It's in your smile, It's in your" + 
                    " eyes, I don't wanna wait for tonight."; 
        */
        //try {
            lyrics = lyrics.toLowerCase(); //make lyrics all lowercase
            lyrics = lyrics.replaceAll("[^a-z' ]", ""); //get rid of punctuation except for apostrophes
            //String[] splitwords = lyricsforanalyzing.split(" "); //split lyrics at every space so every word is in a separate array index
            //(above is unneeded for String search, may change later though)
            /* //debug??
            PrintWriter out = new PrintWriter("cutlyrics.txt");    
            for (String result : splitwords) {
                System.out.println(result);
                //out.println(result);
                if (result.equals("i")){
                    System.out.println("HELP");
                }
            }
            out.close();
            */
            //} catch (IOException e) {
            //    e.printStackTrace();
            //}
        int totalfound = 0;
        int[] scores = new int[2]; //index 0 is the valence, index 1 is the arousal
        //VERY inefficient search algorithm, goes thorugh every element in the keywords array (but is still ok)
        for(int i = 0; i<keywords.length; i++){ //loop for each keyword 
            int timesfound = 0;
            int index = 0;
            int lastindex = -1;
            int wordlength = keywords[i].keyword.length();
            while((lyrics.length()-index)>=wordlength){
                index = lyrics.indexOf(keywords[i].keyword,lastindex+1); //search the line and return the index of where the keyword was found
                if(index == -1){ //if none found
                    break;
                }
                timesfound = timesfound + 1; //increment number of times the word was found
                lastindex = index;
                //System.out.println(line);
            }
            scores[0] = scores[0] + (keywords[i].valence * timesfound); //adds up all valence
            scores[1] = scores[1] + (keywords[i].arousal * timesfound); //adds up all the arousal
            totalfound = totalfound + timesfound; //add up total number of keywords found
        }
        scores[0] = scores[0]/totalfound; //basic averaging for valence and arousal
        scores[1] = scores[1]/totalfound;
        return scores;
    }
}
