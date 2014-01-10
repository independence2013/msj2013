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
            if(choice.equals("Y")||choice.equals("N")){
                break;
            }
            else{
                System.out.println("Invalid choice. \n");
            }
        }
        String classifierpath;
        if(choice.equals("N")){
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
        String folderpath = c.readLine("Folder of songs' (their attribute files) .arff files to be classified: /n");
        classify.loadModel(classifierpath);
        classify.load(folderpath);
            
    }
}
