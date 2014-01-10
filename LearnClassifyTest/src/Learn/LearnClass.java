/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Learn;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 *
 * @author Jeffrey
 */
public class LearnClass {
        Instances trainData;
    /**
     * Object that stores the filter
     */
    StringToWordVector filter;
    /**
     * Object that stores the classifier
     */
    FilteredClassifier classifier;

    /**
     * This method loads a dataset in ARFF format. If the file does not exist, or
     * it has a wrong format, the attribute trainData is null.
     * @param fileName The name of the file that stores the dataset.
     */
    public void loadDataset(String fileName) {
            try {
                    BufferedReader reader = new BufferedReader(new FileReader(fileName));
                    ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader);
                    trainData = arff.getData();
                    System.out.println("===== Loaded dataset: " + fileName + " =====");
                    reader.close();
            }
            catch (IOException e) {
                    System.out.println("Problem found when reading: " + fileName);
            }
    }

    /**
     * This method evaluates the classifier. As recommended by WEKA documentation,
     * the classifier is defined but not trained yet. Evaluation of previously
     * trained classifiers can lead to unexpected results.
     */
    public void evaluate() {
            try {
                    if(trainData.classIndex() == -1){
                        trainData.setClassIndex(trainData.numAttributes()-1);
                    }
                    filter = new StringToWordVector();
                    filter.setAttributeIndices("last");
                    classifier = new FilteredClassifier();
                    classifier.setFilter(filter);
                    classifier.setClassifier(new NaiveBayes());
                    Evaluation eval = new Evaluation(trainData);
                    eval.crossValidateModel(classifier, trainData, 10, new Random(1)); //10 fold cross validation
                    System.out.println(eval.toSummaryString());
                    System.out.println(eval.toClassDetailsString());
                    System.out.println("===== Evaluating on filtered (training) dataset done =====");
            }
            catch (Exception e) {
                    System.out.println("Problem found when evaluating");
            }
    }

    /**
     * This method trains the classifier on the loaded dataset.
     */
    public void learn() {
            try {
                    if(trainData.classIndex() == -1){
                        trainData.setClassIndex(trainData.numAttributes()-1);
                    }
                    filter = new StringToWordVector();
                    filter.setAttributeIndices("last");
                    classifier = new FilteredClassifier();
                    classifier.setFilter(filter);
                    classifier.setClassifier(new NaiveBayes());
                    classifier.buildClassifier(trainData);
                    // Uncomment to see the classifier
                    // System.out.println(classifier);
                    System.out.println("===== Training on filtered (training) dataset done =====");
            }
            catch (Exception e) {
                    System.out.println("Problem found when training");
            }
    }

    /**
     * This method saves the trained model into a file. This is done by
     * simple serialization of the classifier object.
     * @param fileName The name of the file that will store the trained model.
     */
    public void saveModel(String fileName) {
            try {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
        out.writeObject(classifier);
        out.close();
                     System.out.println("===== Saved model: " + fileName + " =====");
    } 
            catch (IOException e) {
                    System.out.println("Problem found when writing: " + fileName);
            }
    }
}
