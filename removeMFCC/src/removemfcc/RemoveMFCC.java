/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package removemfcc;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
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
public class RemoveMFCC {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //editmfcc();
        editmfcc2();
        //editmood();
        // TODO code application logic here
    }
    
    public static void editmfcc(){
        String inputDir = "F:\\Jeffrey\\Desktop\\compiled.arff";
        Path file;
        file = Paths.get(inputDir);
        try(InputStream in = Files.newInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))){
            String line = null;
            String temp = "";
            String newline = System.getProperty("line.separator");
            boolean dataflag = false;
            while ((line = reader.readLine()) != null) { //if line not null (not file end)
                if(!line.isEmpty()){
                    if(line.equals("@DATA")){
                        //temp +=  "@ATTRIBUTE \"Mood\" {0,1,2,3,4,5,6,7}" + newline;
                        dataflag = true;
                    }
                    if(dataflag && !line.equals("@DATA")){
                        int index = line.length()-1;
                        for(int i = 0;i<11;i++){
                            index = line.lastIndexOf(",",index)-1;
                        }
                        //System.out.println(cut(line,index+2,line.length()-1));
                        temp += cut(line,index+2,line.length()-1) + newline;
                    }
                    else {
                        temp += line + newline;
                    }
                }
            }
            System.out.println(temp);
            reader.close();
//            try{
//                PrintWriter pw = new PrintWriter(inputDir);
//                pw.printf(temp); //write new text over old
//                pw.close();
//            } catch (IOException e){
//                System.out.println(e);
//            }
            //System.out.println(temp);
        } catch (IOException x){ //if there is an IO error when reading file
            System.err.println(x); //print out error
        }
    }
    
    public static void editmfcc2(){
        String inputDir = "F:\\Jeffrey\\Desktop\\compiled.arff";
        Path file;
        file = Paths.get(inputDir);
        try(InputStream in = Files.newInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))){
            String line = null;
            String temp = "";
            String newline = System.getProperty("line.separator");
            boolean dataflag = false;
            while ((line = reader.readLine()) != null) { //if line not null (not file end)
                if(!line.isEmpty()){
                    if(line.equals("@DATA")){
                        //temp +=  "@ATTRIBUTE \"Mood\" {0,1,2,3,4,5,6,7}" + newline;
                        dataflag = true;
                    }
                    if(dataflag && !line.equals("@DATA")){
                        int index = line.length()-1;
                        for(int i = 0;i<36;i++){
                            index = line.lastIndexOf(",",index)-1;
                        }
                        int index2 = line.length()-1;
                        for(int j = 0;j<26;j++){
                            index2 = line.lastIndexOf(",",index2)-1;
                        }
                        System.out.println(cut(line,index+2,index2+2));
                        temp += cut(line,index+2,index2+2) + newline;
                    }
                    else {
                        temp += line + newline;
                    }
                }
            }
            //System.out.println(temp);
            reader.close();
            try{
                PrintWriter pw = new PrintWriter(inputDir);
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
    
    public static void editmood(){
        String inputDir = "F:\\Jeffrey\\Desktop\\individual\\cuttests\\44.1nodev_indiv\\filled in dataset\\7compiled.arff";
        Path file;
        file = Paths.get(inputDir);
        try(InputStream in = Files.newInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))){
            String line = null;
            String temp = "";
            String newline = System.getProperty("line.separator");
            boolean dataflag = false;
            while ((line = reader.readLine()) != null) { //if line not null (not file end)
                if(!line.isEmpty()){
                    if(line.equals("@DATA")){
                        //temp +=  "@ATTRIBUTE \"Mood\" {0,1,2,3,4,5,6,7}" + newline;
                        dataflag = true;
                    }
                    if(dataflag && !line.equals("@DATA")){
                        String shorter = line.substring(0,line.lastIndexOf(","));
                        shorter = shorter + ",7";
                        temp += shorter + newline;
                        //System.out.println(shorter);
                    }
                    else {
                        temp += line + newline;
                    }
                }
            }
            System.out.println(temp);
            reader.close();
//            try{
//                PrintWriter pw = new PrintWriter(inputDir);
//                pw.printf(temp); //write new text over old
//                pw.close();
//            } catch (IOException e){
//                System.out.println(e);
//            }
            //System.out.println(temp);
        } catch (IOException x){ //if there is an IO error when reading file
            System.err.println(x); //print out error
        }
    }    
    
    
    public static String cut(String input,int start, int end){
        String output = input.substring(0,start) + input.substring(end,input.length());
        return output;
    }
}
