/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jaudiotagger.audio.*;
import org.jaudiotagger.audio.exceptions.*;
import org.jaudiotagger.tag.*;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.SQLException;

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
        File[] allfiles = musicdir.listFiles(//new FilenameFilter(){ //only list mp3 files in the directory (removed after realizing all files will work)
            //@Override
            //public boolean accept(File dir, String name){
            //return name.toLowerCase().endsWith(".mp3");
            //}
            //}
        );
        
        LyricsClass demo1 = new LyricsClass();
        DatabaseClass demo2 = new DatabaseClass();
        Connection connection = demo2.startconnection();
        
        String filedir;
        for(int i = 0; i<allfiles.length; i++){ //loop where there are files that haven't ben run through
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
        System.out.println("Done!");
    }
}