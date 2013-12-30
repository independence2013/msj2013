/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import jAudioFeatureExtractor.ACE.DataTypes.Batch;
import jAudioFeatureExtractor.DataTypes.RecordingInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

public class jAudioClass {
    public void extract(String filePath){
        Batch data;
        RecordingInfo totaldata[][];
        RecordingInfo moredata = new RecordingInfo("F:\\");
        System.out.println(moredata);
        //totaldata[0] = moredata;
    
        //data.applySettings(totaldata,512,0,100,false,false,true,"","",0);
        //try {
        //    data.execute();
        //} catch (Exception ex) {
        //    Logger.getLogger(jAudioClass.class.getName()).log(Level.SEVERE, null, ex);
        //}
    }
}
