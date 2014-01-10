/*
 * Bulk of code from: https://github.com/jmgomezh/tmweka/blob/master/FilteredClassifier/MyFilteredLearner.java
 */
package Classify;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
/**
 *
 * @author Jeffrey
 */
public class ClassifyClass {
    /**
     * String that stores the text to classify
     */
    String text;
    /**
     * Object that stores the instance.
     */
    Instances instances;
    /**
     * Object that stores the classifier.
     */
    FilteredClassifier classifier;

    /**
     * This method loads the text to be classified.
     * @param fileName The name of the file that stores the text.
     */
    public void load(String fileName) {
            try {
                    BufferedReader reader = new BufferedReader(new FileReader(fileName));
                    String line;
                    text = "";
                    while ((line = reader.readLine()) != null) {
            text = text + " " + line;
        }
                    System.out.println("===== Loaded text data: " + fileName + " =====");
                    reader.close();
                    System.out.println(text);
            }
            catch (IOException e) {
                    System.out.println("Problem found when reading: " + fileName);
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

    /**
     * This method creates the instance to be classified, from the text that has been read.
     */
    /*
    public void makeInstance() {
            // Create the attributes, class and text
            FastVector fvNominalVal = new FastVector(2);
            fvNominalVal.addElement("spam");
            fvNominalVal.addElement("ham");
            Attribute attribute1 = new Attribute("class", fvNominalVal);
            Attribute attribute2 = new Attribute("text",(FastVector) null);
            // Create list of instances with one element
            FastVector fvWekaAttributes = new FastVector(2);
            fvWekaAttributes.addElement(attribute1);
            fvWekaAttributes.addElement(attribute2);
            instances = new Instances("Test relation", fvWekaAttributes, 1);           
            // Set class index
            instances.setClassIndex(0);
            // Create and add the instance
            DenseInstance instance = new DenseInstance(2);
            instance.setValue(attribute2, text);
            // Another way to do it:
            // instance.setValue((Attribute)fvWekaAttributes.elementAt(1), text);
            instances.add(instance);
             System.out.println("===== Instance created with reference dataset =====");
            System.out.println(instances);
    }
    */

    /**
     * This method performs the classification of the instance.
     * Output is done at the command-line.
     */
    public void classify() {
            try {
                    double pred = classifier.classifyInstance(instances.instance(0));
                    System.out.println("===== Classified instance =====");
                    System.out.println("Class predicted: " + instances.classAttribute().value((int) pred));
            }
            catch (Exception e) {
                    System.out.println("Problem found when classifying the text");
            }                
    }
}
