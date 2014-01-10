/*
 * Main code
 */
package prototype;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jaudiotagger.audio.*;
import org.jaudiotagger.audio.exceptions.*;
import org.jaudiotagger.tag.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.FilenameFilter;
import java.util.Random;


public class Prototype {
    /*
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        /*
        Console c = System.console();
        if (c == null) {
            System.err.println("No console.");
            System.exit(1);
        }
        */
        String mdir = ".\\Songs"; //directory for songs
        File musicdir = new File(mdir);
        File[] allfiles = musicdir.listFiles(new FilenameFilter(){ //use filter to make sure we don't read any album art files (.jpg)
            @Override
            public boolean accept(File dir, String name){
                if(name.toLowerCase().endsWith(".jpg")){
                    return false;
                }
                return true;
            }
        }
        );
        
        LyricsClass demo1 = new LyricsClass();
        DatabaseClass demo2 = new DatabaseClass();
        LyricsAnalyzer demo3 = new LyricsAnalyzer();
        Connection connection = demo2.startconnection("orcl"); //get the connection to the database
        
        String filedir;
        for(int i = 0; i<allfiles.length; i++){ //loop where there are files that haven't been run through
            if(allfiles[i].isFile()){ //if it is a file
                filedir = allfiles[i].getAbsolutePath(); //get absoulte path of the files
                //System.out.println(filedir); //debug
                
                File currentfile = new File(filedir); //file loaded here
                AudioFile f = null;
                try {
                    f = AudioFileIO.read(currentfile);
                } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException ex) { //catch exceptions
                    Logger.getLogger(Prototype.class.getName()).log(Level.SEVERE, null, ex);
                }
                Tag tag = f.getTag();
                AudioHeader AudioHeader = f.getAudioHeader(); //get tags
                String artist = tag.getFirst(FieldKey.ARTIST).toLowerCase().replaceAll("[']","").replaceAll("\\(.*\\)","").replaceAll("[é]","e"); //make lowercase so any capitalization issues are gone
                String title = tag.getFirst(FieldKey.TITLE).toLowerCase().replaceAll("[']","").replaceAll("\\(.*\\)","").replaceAll("[é]","e");
                int length = f.getAudioHeader().getTrackLength(); //gives length in seconds
                System.out.println(title);
                System.out.println(artist);
                System.out.println(length);
                
                //String title = c.readLine("Title: "); //code for manual entry 
                //String artist = c.readLine("Artist: ");
                //int length = Integer.parseInt(c.readLine("Length: "));
                
                //artist = "backstreet boys"; //examples, will be removed
                //title = "we've got it goin on";
                
                String lyrics = demo1.webgrab(title, artist); //put title and artist into function that retrieves lyrics; html is returned as a string
                //System.out.println(lyrics); //lyrics is the String to remove tags from
                String cleaned = demo1.cleanup(lyrics);
                System.out.println(cleaned); //print out results; debug
                demo2.saveto(connection, title, artist, length, cleaned);
                Random rand = new Random();
                int value = 1000*(rand.nextInt(10)+5);
                try {
                    Thread.sleep(value);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }  
        
        Keywords[] keywords = demo2.getkeywords(connection); //fill an array with the keywords from the database (to be passed as an argument for the lyrics analyzer)
        String[] song = demo2.retrievelyrics(connection); //retrieve a song that hasn't been analyzed
        //System.out.println(song[0]); //debug
        //System.out.println(song[1]);
        //System.out.println(song[2]);
        /*
         * Psuedocode:
         * while there are songs that haven't been analyzed{
         *      -get an unanalyzed song
         *      -analyze it
         *      -save analysis to database and make make the analyzedflag of that song 1
         * }
         */
        float[] scores = new float[9];
        //Psuedocode implementation
        while(!song[0].equals("0")){
            scores = demo3.analysis2(song, keywords);
            //break;
            demo2.writescore(connection, song[0], song[1], scores); //writes the scores and confidence to the database
            song[0] = "0";
            song = demo2.retrievelyrics(connection); //retrieve another song
        }
        System.out.println("Done!");
    }
}