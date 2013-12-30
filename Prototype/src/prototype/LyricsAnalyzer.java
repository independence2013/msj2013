/*
 * Analyzes the lyrics of a song
 */
package prototype;

//import java.io.PrintWriter;

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
        float[] scores = new float[10]; //first 8 numbers are scores for 8 individual moods , the last one is confidence
        String lyrics = song[2];
        if(lyrics == null){
            return scores;
        }
        lyrics = lyrics.toLowerCase(); //make lyrics all lowercase
        lyrics = lyrics.replaceAll("[^a-z' ]", ""); //get rid of punctuation except for apostrophes
        int[] found = new int[8];
        for(int i = 0; i<keywords.length; i++){ //loop for each keyword 
            int distance = (int) Math.sqrt(Math.pow((keywords[i].valence-5),2) + Math.pow((keywords[i].arousal-5),2)); //get distance from center of valence-arousal graph (5,5) [max possible distance is 4]
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
                if((keywords[i].valence >= 6)&&(keywords[i].valence <= 9)&&(keywords[i].arousal >= 6)&&(keywords[i].arousal <= 9)){ //mood 0: Pleasant, high energy (love)
                    scores[0] = scores[0]+distance;
                    found[0]++;
                }
                if((keywords[i].valence >= 6)&&(keywords[i].valence <= 9)&&(keywords[i].arousal >= 4)&&(keywords[i].arousal <= 6)){ //mood 1: Very pleasant, average energy (fascination, joyfulness)
                    scores[1] = scores[1]+distance;
                    found[1]++;
                }
                if((keywords[i].valence >= 6)&&(keywords[i].valence <= 9)&&(keywords[i].arousal >= 1)&&(keywords[i].arousal <= 4)){ //mood 2: Pleasant, low energy (satisfaction, relaxed)
                    scores[2] = scores[2]+distance;
                    found[2]++;
                }
                if((keywords[i].valence >= 4)&&(keywords[i].valence <= 6)&&(keywords[i].arousal >= 1)&&(keywords[i].arousal <= 4)){ //mood 3: Neutral, low energy (calm, awaiting)
                    scores[3] = scores[3]+distance;
                    found[3]++;
                }
                if((keywords[i].valence >= 1)&&(keywords[i].valence <= 4)&&(keywords[i].arousal >= 1)&&(keywords[i].arousal <= 4)){ //mood 4: Unpleasant, low energy (boredom, sadness)
                    scores[4] = scores[4]+distance;
                    found[4]++;
                }
                if((keywords[i].valence >= 1)&&(keywords[i].valence <= 4)&&(keywords[i].arousal >= 4)&&(keywords[i].arousal <= 6)){ //mood 5: Very unplesant, average energy (disappointment, jealousy)
                    scores[5] = scores[5]+distance;
                    found[5]++;
                }
                if((keywords[i].valence >= 1)&&(keywords[i].valence <= 4)&&(keywords[i].arousal >= 6)&&(keywords[i].arousal <= 9)){ //mood 6: Unpleasant, high energy (irritation, disgust, alarmed)
                    scores[6] = scores[6]+distance;
                    found[6]++;
                }
                if((keywords[i].valence >= 4)&&(keywords[i].valence <= 6)&&(keywords[i].arousal >= 6)&&(keywords[i].arousal <= 9)){ //mood 7: Neutral, high energy (surprised, eager)
                    scores[7] = scores[7]+distance;
                    found[7]++;
                }
                lastindex = index;
            }
        }       
        int totalfound = 0;
        int largestfound = 0;
        int large = 0; //holds index of mood that occured the most
        int large1 = 0; //large1 and large_1 holds indexes of the moods adjacent to large
        int large_1 = 0;
        for(int b = 0; b<8; b++){
            totalfound = totalfound + found[b]; //add up the number of words found int total (for use in confidence calculations)
            if(found[b]>largestfound){
                largestfound = found[b];
                large = b; //index of mood that occured the most times is recorded
            }
        }
        //System.out.println(large); //debug
        large1 = large+1; //get moods adjacent to the mood that occured the most
        large_1 = large-1;
        if(large1 == 8){ //if large was 7 and now is 8
            large1 = 0; //get the mood "above" it (needs to loop back to beginning of array)
        }
        if(large_1 == -1){ //if large was 0 and now is -1
            large_1 = 7; //get the mood "below" it (needs to loop back to end of array)
        }
        float confidence = 0;
        Float temp;
        int overall = 0; //mood number that is most prevalent (in terms of times occured and average "strength") in song
        float largestoverall = 0; //holds index of mood that is most prevalent
        for(int a = 0; a<8; a++){ //get average distance for each mood category
            scores[a] = scores[a]/(float) found[a]; //will divide by zero if no words are found in a category
            temp = scores[a];
            if(temp.isNaN()){ //if divied by zero (not a number)
                scores[a] = 0; //set to zero
            }
            if(found[a]*scores[a]>largestoverall){
                largestoverall = found[a]*scores[a];
                overall = a; //index of mood that is most prevalent is recorded
            }
            if(a==large||a==large1||a==large_1){ //add to confidence if the values are near the most apparent mood
                confidence = confidence + found[a];
            }
            else{ //subtract if the mood is not near the most appeared mood
                confidence = confidence - found[a];
            }
            System.out.println(scores[a]);
        }
        scores[9] = overall;
        //the absolute value of the confidence is multiplied by 100 (gets rid of ridculously small numbers) and divided by the number of keywords found in total (to prevent songs with few words from being analyzed and detected as very unconfident)
        scores[8] = (100*Math.abs(confidence))/totalfound; //final array index is for the confidence of the results
        System.out.println(confidence);
        System.out.println(song[0]);
        return scores;
    }
}