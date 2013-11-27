/*
 * Code for starting the database connection and saving songs to the database
 */
package prototype;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


class DatabaseClass {
    //create connection to database
    public static Connection startconnection(String database){ //connection code is from http://www.mkyong.com/jdbc/connect-to-oracle-db-via-jdbc-driver-java/
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
	} catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
            return null;
	}
 
	Connection connection = null;
 
	try {
            connection = DriverManager.getConnection( //get connection to specified database
			"jdbc:oracle:thin:@localhost:1521:"+database, "sys as sysdba",
			"oracle10g");
	} catch (SQLException e) {
		System.out.println("Connection Failed! Check output console");
		e.printStackTrace();
                return null;
	}
 
	if (connection != null) {
		System.out.println("Connected.");
	} else {
		System.out.println("Failed to make connection.");
	}
        return connection;
    }
    
    //function saves song, artist, song length, and length id to database
    public static void saveto(Connection con, String title, String artist, int length, String cleanedly) throws SQLException{
        //gets rid of secondary artists (could be a problem if one artist has and as part of their name)
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
        //
        int artistid = 0;
        boolean artistpre = false;
        Statement stmt = null;
        String query =
            "SELECT ARTISTID FROM ARTISTS WHERE ARTISTNAME = '" + artist + "'"; //check to see if the artist is already registered
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) { //if the artist isn't found the artistid variable will remain 0
                artistid = rs.getInt("ARTISTID");
                break;
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            if (stmt != null) { stmt.close(); }
        }
        
        if(artistid != 0){ //if the artist was registered check to see if the song was already registered
            Statement stmt2 = null;
            String query2 =
                "SELECT TITLE,ARTISTID FROM SONGTABLE WHERE TITLE = '" + title + "' AND ARTISTID = '" + artistid + "'"; //check to see if the song is already registered
            try {
                stmt2 = con.createStatement();
                ResultSet rs = stmt2.executeQuery(query2);
                if(rs.next()) { //if the song is already registered
                    return; //exit function
                }
            } catch (SQLException e) {
                System.err.println(e);
            } finally {
                if (stmt2 != null) { stmt2.close(); }
            }
        }
        
        if(artistid == 0){ //if artist isn't registered
            Statement stmt3 = null;
            String query3 =
                "SELECT MAX(ARTISTID) \"MAXid\" FROM ARTISTS"; //get greatest artist id
            try {
                stmt3 = con.createStatement();
                ResultSet rs = stmt3.executeQuery(query3);
                while (rs.next()) {
                    artistid = rs.getInt("MAXid"); //get the greatest (last inputted) artist id
                    artistpre = true;                  
                }
            } catch (SQLException e) {
                System.err.println(e);
            } finally {
                if (stmt3 != null) { stmt3.close(); }
            }
        }
        
        con.setAutoCommit(false); //disable autocommit
        
        PreparedStatement pstmt;
        PreparedStatement pstmt2;
        if(!artistpre){ //if artist was already registered
            pstmt = con.prepareStatement("INSERT INTO SONGTABLE (TITLE, ARTISTID, SLENGTH, LYRICS, ANALYZEDFLAG) VALUES (?,?,?,?,'0')"); //all analyzedflags are initialized as zero
            pstmt.setString(1, title); //only insert the song and artist id into the maintable
            pstmt.setInt(2, artistid);
            pstmt.setInt(3, length); //write lengthid to database (length is in divisons of 15)
            pstmt.setString(4, cleanedly);
        }
        else{ //if artist wasn't registered before
            pstmt = con.prepareStatement("INSERT INTO SONGTABLE (TITLE, ARTISTID, SLENGTH, LYRICS, ANALYZEDFLAG) VALUES (?,?,?,?,'0')"); //FIXED
            pstmt.setString(1, title); //not only insert into the maintable but also insert into the artists table to register new artist
            pstmt.setInt(2, artistid + 1); //add one to the greatest (last inputted) artist id to get new artist id (IDs will be in increments of one)
            pstmt.setInt(3, length); //isert length of songs in seconds
            pstmt.setString(4, cleanedly); //insert cleaned lyrics
            
            pstmt2 = con.prepareStatement("INSERT INTO ARTISTS (ARTISTNAME, ARTISTID) VALUES (?,?)"); //put the artist and his/her id into the artist table
            pstmt2.setString(1, artist);
            pstmt2.setInt(2, artistid + 1); //duplicate of the earlier "artistid + 1" for the artist table
            pstmt2.addBatch();
            pstmt2.executeBatch();
        }
        pstmt.addBatch();
        try {
            int [] updateCounts = pstmt.executeBatch();
            con.commit(); //commit changes
            con.setAutoCommit(true); //reenable auto commit
        } catch (SQLException e){
            System.err.println(e);
        } finally {
            if (pstmt != null) { pstmt.close(); } //close connection
        }
    }
    
    //get a song that has not been analyzed
    public static String[] retrievelyrics(Connection con) throws SQLException{
        String[] song = new String[3];
        song[0] = "0";
        Statement stmt = null;
        String query =
                "SELECT TITLE,ARTISTID,LYRICS FROM SONGTABLE WHERE ANALYZEDFLAG = '0'"; //find the song with lyrics that haven't been analyzed
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()) { //get the first song in the resultset
               song[0] = rs.getString("TITLE"); //first element is the title
               song[1] = rs.getString("ARTISTID"); //ARTISTID IS SAVED AS A STRING (remember to convert to int later if needed)
               song[2] = rs.getString("LYRICS");
               //System.out.println(song[1]); //debug
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            if (stmt != null) { stmt.close(); } //close connection
        }
        return song; //returns an array
    }
    
    //writes a song's final scores to the database and makes the song's analyzedflag 1
    public static void writescore(Connection con, String title, String artistid, float[] mood) throws SQLException{
        Statement stmt = null;
        //insert the final scores for the song and change the analyzedflag
        //String query =
        //       "INSERT INTO SONGTABLE (ANALYZEDFLAG,VALENCE,AROUSAL) VALUES ('1','" + valence + "','" + arousal + "') WHERE TITLE = '" + title + "' AND ARTISTID = '" + artistid + "'";
        try {
            stmt = con.createStatement();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                   ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery(
            "SELECT ANALYZEDFLAG,MOOD0,MOOD1,MOOD2,MOOD3,MOOD4,MOOD5,MOOD6,MOOD7,CONFIDENCE,OVERALL FROM SONGTABLE WHERE TITLE = '" + title + "' AND ARTISTID = '" + artistid + "'"); //get row with the song
            while (uprs.next()) {
                uprs.updateInt("ANALYZEDFLAG", 1); //change analyzedflag to 1
                uprs.updateFloat("MOOD0", mood[0]); //write in 
                uprs.updateFloat("MOOD1", mood[1]); //write in
                uprs.updateFloat("MOOD2", mood[2]);
                uprs.updateFloat("MOOD3", mood[3]);
                uprs.updateFloat("MOOD4", mood[4]);
                uprs.updateFloat("MOOD5", mood[5]);
                uprs.updateFloat("MOOD6", mood[6]);
                uprs.updateFloat("MOOD7", mood[7]);
                uprs.updateFloat("CONFIDENCE", mood[8]);
                uprs.updateFloat("OVERALL", mood[9]);
                uprs.updateRow(); //update database
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            if (stmt != null) { stmt.close(); } //close connection
        }
    }
    
    //gets the keywords from the database and saves them to an array
    public static Keywords[] getkeywords(Connection con) throws SQLException{
        int i = 0;
        Keywords[] keywords = new Keywords[1034]; //initalizes an array with the keywords object
        Statement stmt = null;
        String query =
                "SELECT DESCRIPTION, VALENCEAVERAGE, AROUSALAVERAGE, DOMINANCEAVERAGE FROM KEYWORDS"; //get the keywords and their mood scores
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){ //loop through the keywords and save them to the array
                keywords[i] = new Keywords(); //set that array index to the keywords class
                keywords[i].keyword = rs.getString("DESCRIPTION");
                keywords[i].valence = rs.getFloat("VALENCEAVERAGE");
                keywords[i].arousal = rs.getFloat("AROUSALAVERAGE");
                keywords[i].dominance = rs.getFloat("DOMINANCEAVERAGE");
                i++; //increment i
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            if (stmt != null) { stmt.close(); } //close connection
        }
        return keywords;
    }
}