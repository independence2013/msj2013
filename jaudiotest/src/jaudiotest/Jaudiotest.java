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
public class Jaudiotest {
    
    public static void main(String[] args) throws Exception {
        File[][] allfiles = new File[8][];
        for(int x = 0; x<8; x++){
            String mdir = "F:\\Jeffrey\\Music\\Songs\\wav\\" + x; //directory for songs
            File musicdir = new File(mdir);
            allfiles[x] = musicdir.listFiles(new FilenameFilter(){
                @Override
                public boolean accept(File dir, String name){
                    if(name.toLowerCase().endsWith(".wav")){
                        return true;
                    }
                    return false;
                }
            }
            ); 
        }
        int windowSize = 1048576; //size of the analysis window in samples
        double windowOverlap = 0; //percent overlap as a value between 0 and 1
        double samplingRate = 44100; //number of samples per second
        boolean normalize = false; //should the file be normalized before execution
        boolean perWindow = true; //should features be extracted on a window by window basis
        boolean overall = false; //should global features be extracted
        int outputType = 1; //what output format should extracted features be stored in
        String featureLocation = "F:\\Jeffrey\\Documents\\GitHub\\msj2013\\jaudioout\\definitions.xml"; //location of the feature definition file
        
        Object[] o = new Object[] {};
        try {
                o = (Object[]) XMLDocumentParser.parseXMLDocument("F:\\Jeffrey\\Documents\\GitHub\\msj2013\\jaudioout\\batchNoDev", //location of XML file with batch settings
                                "batchFile");
        } catch (Exception e) {
                System.out.println("Error parsing the batch file");
                System.out.println(e.getMessage());
                System.exit(3);
        }
        String featureDestination;
        Batch b;
        DataModel dm = new DataModel("features.xml",null);
        for(int j = 0; j<8; j++){
            for(int z = 0;z<allfiles[j].length; z++){
                for (int i = 0; i < o.length; ++i) {
                    System.out.println(allfiles[j][z].getName());
                    featureDestination = "F:\\Jeffrey\\Desktop\\individual\\cuttests\\44.1nodev_indiv\\"+j+"\\"+allfiles[j][z].getName()+".arff"; //location where extracted features should be stored
                    b = (Batch) o[i];
                    b.setDestination(featureLocation,featureDestination);
                    dm.featureKey = new FileOutputStream(new File(b.getDestinationFK()));
                    dm.featureValue = new FileOutputStream(new File(b.getDestinationFV()));
                    b.setDataModel(dm);
                    b.setSettings(windowSize,windowOverlap,samplingRate,normalize,perWindow,overall,outputType);
                    b.setRecordings(allfiles[j]);
                    b.execute();
                }
            }
        }
    }
}