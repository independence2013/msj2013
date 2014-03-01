/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import GUI.DBRow;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Jeffrey
 */
public class DatabaseAccess {
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
     
     public static int[] retrievesubsong(Connection con, String title, String artistname) throws SQLException{
        int[] song = new int[31];
        Statement stmt = null;
        String query =
                "SELECT SONGTABLE.S0, SONGTABLE.S1, SONGTABLE.S2, SONGTABLE.S3, SONGTABLE.S4, SONGTABLE.S5, "
                + "SONGTABLE.S6, SONGTABLE.S7, SONGTABLE.S8, SONGTABLE.S9, SONGTABLE.S10, "
                + "SONGTABLE.S11, SONGTABLE.S12, SONGTABLE.S13, SONGTABLE.S14, SONGTABLE.S15, "
                + "SONGTABLE.S16, SONGTABLE.S17, SONGTABLE.S18, SONGTABLE.S19, SONGTABLE.S20, "
                + "SONGTABLE.S21, SONGTABLE.S22, SONGTABLE.S23, SONGTABLE.S24, SONGTABLE.S25, "
                + "SONGTABLE.S26, SONGTABLE.S27, SONGTABLE.S28, SONGTABLE.S29, SONGTABLE.S30 "
                + "FROM SONGTABLE INNER JOIN ARTISTS ON SONGTABLE.ARTISTID = ARTISTS.ARTISTID WHERE ARTISTNAME = '" + artistname + "' AND TITLE = '"+ title +"'";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()) { //get the first song in the resultset (there should only be one)
                for(int i = 0; i < 31; i++){
                    song[i] = rs.getInt("S" + i);
                }
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            if (stmt != null) { stmt.close(); } //close connection
        }
        return song;
    }
    
    public static DBRow[] getSearchResults(Connection con, int[] mood, int length, String name, String artist) throws SQLException{
        String moodsquery = "";
        String lengthsquery = "";
        String namesquery = "";
        String artistsquery = "";
        DBRow[] output = new DBRow[25];
        Statement stmt = null;
        int upperlength = length + 30;
        int lowerlength = length - 30;
        if(lowerlength < 0){
            lowerlength = 0;
        }
        String moods = "";
        for(int i = 0; i<mood.length; i++){
            moods = moods + mood[i] +  ",";
        }
        if(length != 0){
            lengthsquery = "WHERE SLENGTH BETWEEN " + lowerlength + " AND " + upperlength;
        }
        if(!moods.equals("")){
            moods = moods.substring(0, moods.lastIndexOf(","));
            moodsquery = " AND AUDIOMOOD IN ("+moods+")";
            if(lengthsquery.equals("")){
                moodsquery = "WHERE AUDIOMOOD IN ("+moods+")";
            }
        }
        if(!name.equals("")){
            namesquery = " AND TITLE LIKE '%"+name.toLowerCase()+"%'";
            if(moodsquery.equals("")){
                namesquery = "WHERE TITLE LIKE '%"+name.toLowerCase()+"%'";
            }
        }
        if(!artist.equals("")){
            artistsquery = " AND ARTISTNAME LIKE '%"+artist.toLowerCase()+"%'";
            if(namesquery.equals("")){
                artistsquery = "WHERE ARTISTNAME LIKE '%"+artist.toLowerCase()+"%'";
            }
        }
        String query =
                "SELECT SONGTABLE.TITLE, SONGTABLE.AUDIOMOOD, SONGTABLE.SLENGTH, SONGTABLE.ARTISTID, ARTISTS.ARTISTNAME FROM SONGTABLE INNER JOIN ARTISTS ON SONGTABLE.ARTISTID = ARTISTS.ARTISTID " + lengthsquery + moodsquery + namesquery + artistsquery;
        System.out.println(query);
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int i = 0;
            while(rs.next()&&(i<25)) {
                output[i] = new DBRow();
                output[i].name = rs.getString("TITLE");
                output[i].artist = rs.getString("ARTISTNAME");
                output[i].mood = rs.getInt("AUDIOMOOD");
                output[i].length = rs.getInt("SLENGTH");
                i++;
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            if (stmt != null) { stmt.close(); } //close connection
        }
        return output;
    }
    
        public static void setsubsong(Connection con, String title, int artistid, int[] values) throws SQLException{
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                   ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery(
            "SELECT S0, S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, S11, S12, S13, S14, S15, S16, S17, S18, S19, S20, S21, S22, S23, S24, S25, S26, S27, S28, S29, S30 FROM SONGTABLE WHERE TITLE = '" + title + "' AND ARTISTID = '" + artistid + "'");
            while (uprs.next()) {
                if(values.length <= 20){
                    for(int i = 0; i < values.length; i++){
                        uprs.updateInt("S" + i, values[i]);
                    }
                }
                else{
                    for(int i = 0; i < 20; i++){
                        uprs.updateInt("S" + i, values[i]);
                    }
                }
                uprs.updateRow(); //update database
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            if (stmt != null) { stmt.close(); } //close connection
        }
    }
        
    public static String retrievelyrics(Connection con, String title, String artistname) throws SQLException{
        String lyrics = "";
        Statement stmt = null;
        String query =
                "SELECT SONGTABLE.LYRICS FROM SONGTABLE INNER JOIN ARTISTS ON SONGTABLE.ARTISTID = ARTISTS.ARTISTID WHERE ARTISTNAME = '" + artistname + "' AND TITLE = '"+ title +"'";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()) { //get the first song in the resultset (there should only be one)
                lyrics = rs.getString("LYRICS");
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            if (stmt != null) { stmt.close(); } //close connection
        }
        return lyrics;
    }
}
