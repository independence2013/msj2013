/*
 * Main code
 */
package prototype;

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

/**
 * @author Jeffrey
 */

public class Prototype {
    /*
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        String mdir = "F:\\Jeffrey\\Desktop\\sciproj\\Prototype\\Songs"; //directory for songs
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
        Connection connection = demo2.startconnection(); //get the connection to the database
        
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
                String artist = tag.getFirst(FieldKey.ARTIST).toLowerCase(); //make lowercase so any capitalization issues are gone
                String title = tag.getFirst(FieldKey.TITLE).toLowerCase();
                int length = f.getAudioHeader().getTrackLength(); //gives length in seconds
                System.out.println(title);
                System.out.println(artist);
                System.out.println(length);
                
                //artist = "backstreet boys"; //examples, will be removed
                //title = "we've got it goin on";
                
                String lyrics = demo1.webgrab(title, artist); //put title and artist into function that retrieves lyrics; html is returned as a string
                //System.out.println(lyrics); //lyrics is the String to remove tags from
                String cleaned = demo1.cleanup(lyrics);
                System.out.println(cleaned); //print out results
                demo2.saveto(connection, title, artist, length, cleaned);
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
        float[] scores = new float[2];
        //Psuedocode implementation
        while(!song[0].equals("0")){
            scores = demo3.wordanalyze(song, keywords);
            //break;
            System.out.println(scores[0]);
            System.out.println(scores[1]);
            demo2.writescore(connection, song[0], song[1], scores[0], scores[1]);
            song[0] = "0";
            song = demo2.retrievelyrics(connection);   
        }
        System.out.println("Done!");
    }
}