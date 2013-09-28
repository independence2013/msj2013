/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;

/**
 * @author Jeffrey
 */
class LyricsClass {
    //grab lyrics from internet
    public static String webgrab(String title, String artist){
        title = title + " ";
        int andindex = 0;
        andindex = artist.indexOf(" & "); //looks for the & symbol (so additional artists dont confuse the azlyrics search)
        if(andindex != -1){
            artist = artist.substring(0,andindex);
        }
        else{ //if you dont find the symbol
            andindex = artist.indexOf(" and "); //look for the word "and"
            if(andindex != -1){
                artist = artist.substring(0,andindex);
            }
        }
        final WebClient webClient = new WebClient();
        webClient.setJavaScriptEnabled(false); //disable javascript; speeds up page loads by A TON and gets rid of any errors that could occur when loading them
        String lyrics = null;
        // Get the first page
        try{
            final HtmlPage page1 = webClient.getPage("http://www.azlyrics.com"); //load azlyrics.com
            //System.out.println(page1.asText()); //debug
        
            final HtmlForm form = page1.getFirstByXPath("//form[@class = 'search']"); //finds the form to fill; find the tag with the class search under a form tag
            final HtmlTextInput textField = form.getFirstByXPath("//input[@class = 'search_text']"); //finds the search box; find tag with class search_text under an input tag within the form
            final HtmlButton button = form.getFirstByXPath("//button[@class = 'search_btn']"); //finds the search button; find tag with search_btn under a button tag within the form

            // Change the value of the text field: put in the title and artist
            textField.setValueAttribute(title + artist);
            
            // Now submit the form by clicking the button and get back the second page.
            final HtmlPage page2 = button.click();
            
            //System.out.println(page2.asText()); //debug
        
            //find the html hyperlink (anchor) under the a div tag with the id of inn; and under that find another div tag with a class of sen; and under that find an anchor tag
            final HtmlAnchor link = page2.getFirstByXPath("//div[@id = 'inn']/div[@class = 'sen']/a"); //this finds the first result
        
            //Below code was when trying to optimize the run time because the loading of pages took very long
            //Wanted to use the URL library to get last page to speed up page loading
            //No longer used after javascript was disabled (javascript was slwing down the loading, loads now take ~3 seconds)
        
            //System.out.println(link.asXml());
            /*
            String finallink = link.asXml();
            Pattern pattern = Pattern.compile("\"");
            Matcher matcher = pattern.matcher(finallink);
        
            int x = 0;
            int start = 0;
            int end = 0;
            boolean found = false;
            while(matcher.find()){
                found = true;
                if(x == 0){
                    start = matcher.end();
                    x = 1;
                }
                else if(x == 1){
                    end = matcher.start();
                }
            }
            if(found){
                 String lastURL = finallink.substring(start,end);
                 //System.out.println(lastURL); //debug
                 URL myUrl = new URL(lastURL);
                 BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                            myUrl.openStream()));
                String newlinechar = System.getProperty("line.separator");
                String line;
                while ((line = in.readLine()) != null){
                    lyrics = lyrics + line + newlinechar;
                }
                in.close();
                //System.out.println(lyrics); //debug
            }
            */
            if(link != null){ //if there was a result returned
                final HtmlPage page3 = link.click(); //click the first result hyperlink
                lyrics = page3.getWebResponse().getContentAsString(); //return HTML as a string
                webClient.closeAllWindows(); //cloes windows
            }
            else{
                lyrics = "NONE FOUND"; //for debugger to find; after the lyrics go throught the lyric cleaner it will return nothing: "" (not null though)
            }
        }
        catch (IOException | FailingHttpStatusCodeException | ElementNotFoundException e){
            e.printStackTrace();
        }
        return lyrics;
    }
    
    
    //cleanup function
    public static String cleanup(String raw_html){
        boolean ChorusVar = false;
        String ChorusHold = "";
        String savedly = "";
        try{
            BufferedReader in = new BufferedReader(new StringReader(raw_html)); //initialize a string reader
        
            boolean x = false;
            String line;
            String paragraph = "";
            String lastline = "";

            while ((line = in.readLine()) != null){ //read the string line by line
                if (line.equals("<!-- start of lyrics -->")){ //if start lyrics comment is found
                    x = true;
                }
                if (x == true){ //after the start was found
                    Pattern braket_find = Pattern.compile("\\[.*\\]"); //use regex to find things in brackets
                    Matcher matchline = braket_find.matcher(line);
                    
                    String found = "";
                    int indexofb = 0;
                    
                    while(matchline.find()){ //if found
                        found = matchline.group(); //save the braketed tag
                        indexofb = matchline.start(); //store starting index of brakets
                    }
                    
                    //BEGIN CHORUS DETECTION
                    if(found.equals("[Chorus:]")){ //if line with [Chorus:] is found
                        ChorusVar = true; //set ChorusVar true
                    }
                    
                    if((ChorusVar == true) && (line.equals("<br />"))){ //if a line with just <br /> is found, stop saving
                        ChorusVar = false; //set ChorusVar false
                    }
                    
                    if(ChorusVar == true && !found.equals("[Chorus:]")){ // if ChorusVar is true (end of chorus not found) and the line isn't [Chorus:]
                        ChorusHold = ChorusHold + line; //save lines of chorus
                        //System.out.println(ChorusHold); //debug
                    }
                    //END CHORUS DETECTION
                    
                    //BEGIN CHORUS INSERTION
                    if(found.equals("[Chorus]")){
                        line = ChorusHold;
                    }
                    //END CHORUS INSERTION
                    
                    //BEGIN PARAGRAPH MULTIPLIER SEARCH AND INSERT
                    String numberfound = "";
                    int number = 1;
                    
                    Pattern number_find = Pattern.compile("\\d"); //use regex to find numbers
                    Matcher matchbraket = number_find.matcher(lastline); //find the number of times to multiply paragraph by
                    
                    if(!lastline.matches(".+<i>.*")){ //if the previous line didn't have any lyrics (prevents conflicts between the paragraph and line duplicator)           
                        if(line.equals("<br />")){ //if the end of a paragraph is found
                            while(matchbraket.find()){ //if a number is found
                                numberfound = matchbraket.group(); //save number
                                number = Integer.parseInt(numberfound); //convert string to integer
                            }
                            while((number - 1) > 0){ //the while loop will loop the number of times the number is
                                line = line + paragraph; //add paragraph to the end of the current line
                                number--; //decrement the number
                            }
                            //System.out.println(paragraph); //debug
                            paragraph = ""; //clear paragraph
                        }
                        else{
                            paragraph = paragraph + line; //add line to paragraph
                            lastline = line; //store lastline (so when we are on the line with just <br /> we can still get the number of times to multiply by
                        }
                    }
                    //END PARAGRAPH MULTIPLIER SEARCH AND INSERT
                    
                    //BEGIN LINE MULTIPLIER SEARCH AND INSERT 
                    matchbraket = number_find.matcher(found); //search for numbers inside brakets
                    while(matchbraket.find()){ //if a number is found (using above regex so the pattern doesnt need to be recompiled)
                        numberfound = matchbraket.group(); //save number
                        number = Integer.parseInt(numberfound); //convert string to integer
                        line = line.substring(0,indexofb); //get rid of the braket tag from the line (from index 0 to start index of brakets
                    }
                    
                    String templine = line.substring(0,indexofb); //store the line again temporarily
                    while((number - 1) > 0){ //the while loop will loop the number of times the number is
                        line = line + templine; //duplicate original line
                        number--; //decrement the number
                    }
                    //END LINE MULTIPLIER SEARCH AND INSERT
                    
                    savedly = savedly + line; //start saving the lines to one string
                }
                if (line.equals("<!-- end of lyrics -->")){ //if end lyrics comment is found
                    //System.out.println(ChorusHold); //debug
                    break; //jump out of the stringreader while loop
                }
            }
        
        savedly = Jsoup.parse(savedly).text(); //use Jsoup library to remove remaining HTML tags in lyrics
        savedly = savedly.toLowerCase(); //make all text lowercase for searching later
        
        //get rid of any remaining braket tags
        int startfind = 0;
        int endfind = 0;
        while(savedly.indexOf("[") != -1){ //while a braket is found
            startfind = savedly.indexOf("[", endfind); //find the first start braket since the last found braket
            endfind = savedly.indexOf("]", endfind); //find the first end braket since the last found braket
            String toreplace = savedly.substring(startfind, endfind+1); //get the substring to replace
            savedly = savedly.replace(toreplace, ""); //replace (will replace ALL that match [not just the specific one found])
        }
        
        in.close(); //close reader when done
        } catch(IOException e){
            System.err.println(e);
        }
        return savedly;
    }
    
    /*
    public static int lengthtoid(int length){ //turns length in seconds to length/15 rounded to nearest whole number
        int id = 0;
        if(length < 600){ //if song is less than 10 minutes
            id = length/15;
            id = (int)Math.round(id); //round to nearest integer
            System.out.println(id);
            return id;
        }
        else{ //anything 10 minutes or longer will have an id of 40
            id = 40;
            return id;
        }
    }
    */
}