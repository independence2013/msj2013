/*
 * Code for training classifier
 * From https://github.com/jmgomezh/tmweka/tree/master/FilteredClassifier
 */
package prototype;

import java.io.BufferedReader;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.filters.Filter;

import java.io.FileReader;
import java.io.IOException;
import weka.core.converters.ArffLoader.ArffReader;

public class LearnClass {
    Instances trainData;
    
    public void loadData(String filename){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            ArffReader arff = new ArffReader(reader);
            trainData = arff.getData();
            System.out.println("===== Loaded dataset: " + filename + " =====");
            reader.close();
        }
        catch (IOException e){
            System.out.println("Problem found when reading: " + filename);
        }
    }
    public void train(){
        
    }
}
