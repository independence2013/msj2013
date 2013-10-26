/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Mitchell
 */
public class LyricsAnalyzer {

    /**
     * @param args the command line arguments
     */
    public static String wordcollector () {
        String lyricsforanalyzing = "You walked in, Caught my attention." + 
                    " I've never seen A man with so much dimension." + 
                    " It's the way you walk, The way you talk, The way you" + 
                    " make me feel inside, It's in your smile, It's in your" + 
                    " eyes, I don't wanna wait for tonight.";

           
        try {
            lyricsforanalyzing = lyricsforanalyzing.toLowerCase();
            lyricsforanalyzing = lyricsforanalyzing.replaceAll("[^a-z' ]", "");
            String[] splitwords = lyricsforanalyzing.split(" ");
            PrintWriter out = new PrintWriter("cutlyrics.txt");    
            for (String result : splitwords) {
               System.out.println(result);
//                out.println(result);
                if (result.equals("i")){
                    System.out.println("HELP");
                    }
                }
            out.close();
            } catch (IOException e) {
            e.printStackTrace();
            } 
        return null;
    }
}
