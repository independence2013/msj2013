/*
 * Bulk of code from: https://github.com/jmgomezh/tmweka/blob/master/FilteredClassifier/MyFilteredLearner.java
 * Some from: http://weka.wikispaces.com/Use+WEKA+in+your+Java+code
 */
package Classify;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
/**
 *
 * @author Jeffrey
 */
public class ClassifyClass {
    /**
     * Object that stores the classifier.
     */
    FilteredClassifier classifier;

    //loads the .arff file and classifies the song
    public void loadandclassify(String folderin, String folderout) {
        File musicdir = new File(folderin);
        File[] allfiles = musicdir.listFiles(new FilenameFilter(){ //use filter to make sure we don't read any album art files (.jpg)
            @Override
            public boolean accept(File dir, String name){
                if(name.toLowerCase().endsWith(".arff")){
                    return false;
                }
                return true;
            }
        }
        );
        String filename;
        String filedir;
        for(int i = 0; i<allfiles.length; i++){ //loop where there are files that haven't been run through
            if(allfiles[i].isFile()){ //if it is a file
                filename = allfiles[i].getName(); //get absoulte path of the files
                filedir = allfiles[i].getAbsolutePath(); //get absoulte path of the files
                try{
                Instances unlabeled = new Instances(
                                      new BufferedReader(
                                      new FileReader(filedir)));
                // set class attribute
                unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
                // create copy
                Instances labeled = new Instances(unlabeled);
                // label instances
                for (int j = 0; j < unlabeled.numInstances(); j++) {
                    try{
                        double clsLabel = classifier.classifyInstance(unlabeled.instance(j));
                        labeled.instance(j).setClassValue(clsLabel);
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
                // save labeled data
                BufferedWriter writer = new BufferedWriter(
                                        new FileWriter(folderout + "\\" + filename));
                    writer.write(labeled.toString());
                    writer.newLine();
                    writer.flush();
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(ClassifyClass.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * This method loads the model to be used as classifier.
     * @param fileName The name of the file that stores the text.
     */
    public void loadModel(String fileName) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            Object tmp = in.readObject();
            classifier = (FilteredClassifier) tmp;
            in.close();
            System.out.println("===== Loaded model: " + fileName + " =====");
        } 
        catch (Exception e) {
            // Given the cast, a ClassNotFoundException must be caught along with the IOException
            System.out.println("Problem found when reading: " + fileName);
        }
    }
}
