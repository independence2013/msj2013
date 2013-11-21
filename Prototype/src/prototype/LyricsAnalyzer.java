/*
 * Analyzes the lyrics of a song
 */
package prototype;

//import java.io.PrintWriter;
import java.lang.Math;

/**
 * @author Mitchell
 */
public class LyricsAnalyzer {
    //Analyze song lyrics
    public static float[] analysis(String[] song, Keywords[] keywords) { //first prototype
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
        float[] scores = new float[2]; //index 0 is the valence, index 1 is the arousal
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
                else{
                    System.out.print(keywords[i].valence + " " + keywords[i].arousal  + " "); //debug
                    System.out.println(index);
                }
                timesfound = timesfound + 1; //increment number of times the word was found
                lastindex = index;
                //System.out.println(line);
            }
            scores[0] = scores[0] + (keywords[i].valence * timesfound); //adds up all valence
            scores[1] = scores[1] + (keywords[i].arousal * timesfound); //adds up all the arousal
            totalfound = totalfound + timesfound; //add up total number of keywords found
        }
        //System.out.println(totalfound); //debug
        System.out.println(song[0]);
        scores[0] = scores[0]/totalfound; //basic averaging for valence and arousal
        scores[1] = scores[1]/totalfound;
        return scores;
    }
    
    public static float[] analysis2(String song[], Keywords[] keywords){ //second prototype
        String lyrics = song[2];
        lyrics = lyrics.toLowerCase(); //make lyrics all lowercase
        lyrics = lyrics.replaceAll("[^a-z' ]", ""); //get rid of punctuation except for apostrophes
        float[] scores = new float[9]; //first 8 numbers are scores for 8 individual moods , the last one is confidence
        int[] found = new int[8];
        for(int i = 0; i<keywords.length; i++){ //loop for each keyword 
            int distance = (int) Math.sqrt(Math.pow((keywords[i].valence-5),2) + Math.pow((keywords[i].arousal-5),2));
            int index = 0;
            int lastindex = -1;
            int wordlength = keywords[i].keyword.length();
            while((lyrics.length()-index)>=wordlength){
                index = lyrics.indexOf(keywords[i].keyword,lastindex+1); //search the line and return the index of where the keyword was found
                if(index == -1){ //if none found
                    break;
                }
                else{
                    System.out.print(keywords[i].valence + " " + keywords[i].arousal  + " "); //debug
                    System.out.println(index);
                }
                //there must be a better way of doing this
                //words with integer values will go into multiple categories (but highly unlikely)
                if((keywords[i].valence >= 6)&&(keywords[i].valence <= 9)&&(keywords[i].arousal >= 6)&&(keywords[i].arousal <= 9)){ //mood: Pleasant, high energy
                    scores[0] = scores[0]+distance;
                    found[0]++;
                }
                if((keywords[i].valence >= 6)&&(keywords[i].valence <= 9)&&(keywords[i].arousal >= 4)&&(keywords[i].arousal <= 6)){ //mood: Very pleasant, average energy
                    scores[1] = scores[1]+distance;
                    found[1]++;
                }
                if((keywords[i].valence >= 6)&&(keywords[i].valence <= 9)&&(keywords[i].arousal >= 1)&&(keywords[i].arousal <= 4)){ //mood: Pleasant, low energy
                    scores[2] = scores[2]+distance;
                    found[2]++;
                }
                if((keywords[i].valence >= 4)&&(keywords[i].valence <= 6)&&(keywords[i].arousal >= 1)&&(keywords[i].arousal <= 4)){ //mood: Neutral, low energy (calm)
                    scores[3] = scores[3]+distance;
                    found[3]++;
                }
                if((keywords[i].valence >= 1)&&(keywords[i].valence <= 4)&&(keywords[i].arousal >= 1)&&(keywords[i].arousal <= 4)){ //mood: Unpleasant, low energy
                    scores[4] = scores[4]+distance;
                    found[4]++;
                }
                if((keywords[i].valence >= 1)&&(keywords[i].valence <= 4)&&(keywords[i].arousal >= 4)&&(keywords[i].arousal <= 6)){ //mood: Very unplesant, average energy
                    scores[5] = scores[5]+distance;
                    found[5]++;
                }
                if((keywords[i].valence >= 1)&&(keywords[i].valence <= 4)&&(keywords[i].arousal >= 6)&&(keywords[i].arousal <= 9)){ //mood: Unpleasant, high energy
                    scores[6] = scores[6]+distance;
                    found[6]++;
                }
                if((keywords[i].valence >= 4)&&(keywords[i].valence <= 6)&&(keywords[i].arousal >= 6)&&(keywords[i].arousal <= 9)){ //mood: Neutral, high energy (surprised)
                    scores[7] = scores[7]+distance;
                    found[7]++;
                }
                lastindex = index;
            }
        }
        int largest = 0;
        int large = 0;
        for(int b = 0; b<8; b++){
            if(found[b]>largest){
                largest = found[b];
                large = b;
            }
        }
        float confidence = 0;
        Float temp;
        for(int a = 0; a<8; a++){ //get average distance for each mood category
            scores[a] = scores[a]/(float) found[a]; //will divide by zero if no words are found in a category
            temp = scores[a];
            if(temp.isNaN()){ //if divied by zero (not a number)
                scores[a] = 0; //set to zero
            }
            if(a==large){
                confidence = confidence + scores[a];
            }
            if(a!=large){
                confidence = confidence - scores[a];
            }
            System.out.println(scores[a]);
        }
        System.out.println(confidence);
        return scores;
    }
}
