/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package arfftool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Jeffrey
 */
public class ArffTool {

    public static void main(String[] args) {
        Console c = System.console();
        if (c == null) {
            System.err.println("No console.");
            System.exit(1);
        }
        String directory = c.readLine("Directory to use for input: ");
        //directory = "F:\\Jeffrey\\Desktop"; //debug
        File arffdir = new File(directory);
        File[] allfiles = arffdir.listFiles(new FilenameFilter(){ //use filter to make sure we only read arff files
            @Override
            public boolean accept(File dir, String name){
                if(name.toLowerCase().endsWith(".arff")){
                    return true;
                }
                return false;
            }
        }
        );
        OUTER:
        while (true) {
            c.printf("=====Arff File Editor=====\n");
            String setting = c.readLine("Options:\n 1.Edit .arff files\n 2.Concatenate files\n 3.Exit\n");
            switch (setting) {
                case "1":
                    ArffTool.edit(allfiles,c);
                    break;
                case "2":
                    ArffTool.combine(allfiles,c);
                    break;
                case "3":
                    c.readLine("Press Enter to proceed.");
                    break OUTER;
                default:
                    c.printf("Invalid Option\n");
                    break;
            }
        }
    }
    public static void edit(File[] filelist, Console c){
        for(int i=0; i<filelist.length; i++){ //loop through all the files
            Path file;
            file = Paths.get(filelist[i].getAbsolutePath());
            String filename = filelist[i].getName();
            try(InputStream in = Files.newInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in))){
                String line = null;
                String lastline = "";
                String temp = "";
                String newline = System.getProperty("line.separator");
                while ((line = reader.readLine()) != null) { //if line not null (not file end)
                    temp += line + newline;
                    if(line.equals("@ATTRIBUTE \"Area Method of Moments of MFCCs Overall Average9\" NUMERIC")){
                        temp += "@ATTRIBUTE \"Mood\" {0,1,2,3,4,5,6,7}" + newline; //put mood attibute at the end
                    }
                    if(lastline.equals("@DATA")){
                        String mood = c.readLine("Mood number of "+filename+": "); //read in the mood specified by the user
                        temp = temp.trim();
                        temp += ","+mood;
                    }
                    lastline = line;
                }
                reader.close();
                try{
                    PrintWriter pw = new PrintWriter(filelist[i].getAbsolutePath());
                    pw.printf(temp); //write new text over old
                    pw.close();
                } catch (IOException e){
                    System.out.println(e);
                }
                //System.out.println(temp);
            } catch (IOException x){ //if there is an IO error when reading file
                System.err.println(x); //print out error
            }
        }
    }
    public static void combine(File[] filelist, Console c){
        String outputdir = c.readLine("Output directory: ");
        String values = "";
        String attributes = "";
        boolean saveattributes = true;
        for(int i=0; i<filelist.length; i++){
            Path file;
            file = Paths.get(filelist[i].getAbsolutePath());
            try(InputStream in = Files.newInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in))){
                String line = null;
                String lastline = "";
                String newline = System.getProperty("line.separator");
                while ((line = reader.readLine()) != null) { //if line not null (not file end)
                    if(lastline.equals("@DATA")){
                        saveattributes = false;
                        values += line + newline;
                    }
                    if(saveattributes){
                        attributes += line + newline;
                    }
                    lastline = line;
                }
                reader.close();
                PrintWriter pw = new PrintWriter(outputdir + "/compiled.arff");
                pw.printf(attributes);
                pw.printf(values);
                pw.flush();
                pw.close();
            } catch (IOException x){ //if there is an IO error when reading file
                System.err.println(x); //print out error
            }
        }
        //System.out.println(values);
    }
}
