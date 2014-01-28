/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package learnclassifytest;

import Classify.ClassifyClass;
import Learn.LearnClass;
import java.io.Console;

/**
 *
 * @author Jeffrey
 */
public class LearnClassifyTest {

    public static void main(String[] args) {
        Console c = System.console();
        if (c == null) {
            System.err.println("No console.");
            System.exit(1);
        }
        LearnClass learner = new LearnClass();
        ClassifyClass classify = new ClassifyClass();
        String choice;
        while(true){
            choice = c.readLine("Classifier trained yet? (Y/N):");
            if(choice.toLowerCase().equals("y")||choice.toLowerCase().equals("n")){
                break;
            }
            else{
                System.out.println("Invalid choice. \n");
            }
        }
        String classifierpath;
        if(choice.equals("n")){
            String datapath = c.readLine("Dataset path: ");
            learner.loadDataset(datapath);
            learner.evaluate();
            learner.learn();
            classifierpath = c.readLine("Classifier output path: ");
            learner.saveModel(classifierpath);
        }
        else{
            classifierpath = c.readLine("Classifier path: ");
        }
        classify.loadModel(classifierpath);
        String folderpath = c.readLine("Folder of songs' (their attribute files) .arff files to be classified: ");
        String outputpath = c.readLine("Folder for classifed .arff files: ");
        classify.loadandclassify(folderpath,outputpath);
    }
}
