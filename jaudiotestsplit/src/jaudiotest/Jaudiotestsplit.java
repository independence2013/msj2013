/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jaudiotest;

import jAudioFeatureExtractor.ACE.DataTypes.Batch;
import jAudioFeatureExtractor.DataModel;
import java.io.File;
import java.io.FilenameFilter;
import jAudioFeatureExtractor.ACE.XMLParsers.XMLDocumentParser;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeffrey
 */
public class Jaudiotestsplit {
    
    public static void main(String[] args) throws Exception {
        String[][] alldirectories = new String[8][];
        for(int x = 0; x<8; x++){
            String fdir = "F:\\Jeffrey\\Desktop\\individual\\cut songs\\" + x; //directory for songs
            File file = new File(fdir);
            alldirectories[x] = file.list();
        }
        int windowSize = 512; //size of the analysis window in samples
        double windowOverlap = 0; //percent overlap as a value between 0 and 1
        double samplingRate = 44100; //number of samples per second
        boolean normalize = false; //should the file be normalized before execution
        boolean perWindow = false; //should features be extracted on a window by window basis
        boolean overall = true; //should global features be extracted
        int outputType = 1; //what output format should extracted features be stored in
        String featureLocation = "F:\\Jeffrey\\Documents\\GitHub\\msj2013\\jaudioout\\definitions.xml"; //location of the feature definition file
        
        Object[] o = new Object[] {};
        try {
                o = (Object[]) XMLDocumentParser.parseXMLDocument("F:\\Jeffrey\\Documents\\GitHub\\msj2013\\jaudioout\\BatchFull", //location of XML file with batch settings
                                "batchFile");
        } catch (Exception e) {
                System.out.println("Error parsing the batch file");
                System.out.println(e.getMessage());
                System.exit(3);
        }
        String featureDestination;
        Batch b;
        DataModel dm = new DataModel("features.xml",null);
        //File[] tempfile = new File[1];
        for(int j = 0; j<8; j++){
            for(int z = 0; z<alldirectories[j].length; z++){
                String mdir = "F:\\Jeffrey\\Desktop\\individual\\cut songs\\" + j + "\\" + alldirectories[j][z]; //directory for songs
                File musicdir = new File(mdir);
                File[] allfiles = musicdir.listFiles(new FilenameFilter(){
                @Override
                public boolean accept(File dir, String name){
                    if(name.toLowerCase().endsWith(".jpg")){
                        return false;
                    }
                    return true;
                    }
                }
                );
                for (int i = 0; i < o.length; ++i) {
                    //System.out.println(allfiles[j][z].getName());
                    //tempfile[0] = allfiles[j][z];
                    featureDestination = "F:\\Jeffrey\\Desktop\\individual\\cut songs\\ARFFData\\"+j+"\\"+ alldirectories[j][z] +".arff"; //location where extracted features should be stored
                    b = (Batch) o[i];
                    b.setDestination(featureLocation,featureDestination);
                    dm.featureKey = new FileOutputStream(new File(b.getDestinationFK()));
                    dm.featureValue = new FileOutputStream(new File(b.getDestinationFV()));
                    b.setDataModel(dm);
                    b.setSettings(windowSize,windowOverlap,samplingRate,normalize,perWindow,overall,outputType);
                    b.setRecordings(allfiles);
                    b.execute();
                }
            }
        }
    }
}