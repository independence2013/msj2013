/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package audiosplitter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import org.apache.commons.io.FilenameUtils;


/**
 *
 * @author Jeffrey
 */
public class Audiosplitter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String mdir = "F:\\Jeffrey\\Music\\Songs\\wav\\0"; //directory for songs
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
        split(allfiles);
    }
    
    public static void split(File[] files) throws Exception{
        for(int i = 0; i < files.length; i++){
            AudioInputStream stream = null;
            stream = AudioSystem.getAudioInputStream(files[i]);
            AudioFormat format = stream.getFormat();
            long filelength = files[i].length(); //length in bytes
            int bytes_p_sec = (int)format.getSampleRate()*format.getSampleSizeInBits()*format.getChannels()/8;
            long fileduration = filelength/bytes_p_sec;
            AudioInputStream shortstream = null;
            int copylength = (int)format.getSampleRate()*20; //20 seconds
            //System.out.println(copylength);
            int timestocut = (int)Math.ceil(fileduration/20);
            //File tempFile = null;
            for(int j = 0; j <= timestocut; j++){
                //System.out.println(stream.available());
//                if(copylength*format.getSampleSizeInBits()*format.getChannels()/8 > stream.available()){ //if there isn't 20 seconds of audio left
//                    shortstream = new AudioInputStream(stream,format,stream.available()*8/(format.getSampleSizeInBits()*format.getChannels())); //make the stream only until the end
//                }
//                else {
                    shortstream = new AudioInputStream(stream,format,copylength);
                //}
                File directory = new File("F:\\Jeffrey\\Desktop\\individual\\cut songs\\" + FilenameUtils.removeExtension(files[i].getName()));
                File tempFile = new File("F:\\Jeffrey\\Desktop\\individual\\cut songs\\" + FilenameUtils.removeExtension(files[i].getName()) + "\\" + j + ".wav");
                directory.mkdirs();
                tempFile.createNewFile();
                AudioSystem.write(shortstream, AudioFileFormat.Type.WAVE, tempFile);
                
                
                
                //tempFile.delete();
            }
            
            stream.close();
            shortstream.close();
        }
    }
    
    /*
    public static byte[] fileToByteArray(File tempfile) throws Exception{
        byte[] output = new byte[2];
        FileInputStream fi = new FileInputStream(tempfile);
        int data;
        short datatwo;
        boolean secondbyteflag = false;
        while((data = fi.read()) != -1){
            if(!secondbyteflag){
                output[0] = (byte)data;
                secondbyteflag = true;
            }
            else{
                output[1] = (byte)data;
                secondbyteflag = false;
                datatwo = (short) ((output[0] << 8) | (output[1] & 0xFF));
                System.out.println(datatwo);
            }
        }
        fi.close();
        return output;
    }
    */
}
