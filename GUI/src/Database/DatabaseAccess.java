/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

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
     
     public static int[] retrievesubsong(Connection con, String title, int artistid) throws SQLException{
        int[] song = new int[31];
        Statement stmt = null;
        String query =
                "SELECT S0, S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, S11, S12, S13, S14, S15, S16, S17, S18, S19, S20, S21, S22, S23, S24, S25, S26, S27, S28, S29, S30 FROM SONGTABLE WHERE ARTISTID = '" + artistid + "' AND TITLE = '"+ title +"'"; //find the song with lyrics that haven't been analyzed
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()) { //get the first song in the resultset (there should only be one)
                for(int i = 0; i < 20; i++){
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
     
    public static void setsubsong(Connection con, String title, int artistid, int[] values) throws SQLException{
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                   ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery(
            "SELECT S0, S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, S11, S12, S13, S14, S15, S16, S17, S18, S19, S20, S21, S22, S23, S24, S25, S26, S27, S28, S29, S30 FROM SONGTABLE WHERE TITLE = '" + title + "' AND ARTISTID = '" + artistid + "'"); //get row with the song
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
    
    public static String idtoArtist(Connection con, int id) throws SQLException{
        String artist = "";
        Statement stmt = null;
        String query =
                "SELECT ARTISTNAME FROM ARTISTS WHERE ARTISTID = '" + id + "'"; //find the song with lyrics that haven't been analyzed
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()) { //get the first artist in the resultset (there should only be one)
                artist = rs.getString("ARTISTNAME");
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            if (stmt != null) { stmt.close(); } //close connection
        }
        return artist;
    }
    
    public static int artisttoId(Connection con, String name) throws SQLException{
        int id = -1;
        Statement stmt = null;
        String query =
                "SELECT ARTISTID FROM ARTISTS WHERE ARTISTNAME = '" + name + "'"; //find the song with lyrics that haven't been analyzed
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()) { //get the first artist in the resultset (there should only be one)
                id = rs.getInt("ARTISTNAME");
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            if (stmt != null) { stmt.close(); } //close connection
        }
        return id;
    }
    
    public static void getSearchResults(Connection con, int[] mood, int length) throws SQLException{
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
        String moodsquery = "";
        if(moods != ""){
            moods = moods.substring(0, moods.lastIndexOf(","));
            moodsquery = " AND AUDIOMOOD IN ("+moods+")";
        }
        String query =
                "SELECT TITLE,ARTISTID FROM SONGTABLE WHERE SLENGTH BETWEEN " + lowerlength + " AND " + upperlength + moodsquery;
        System.out.println(query);
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                System.out.println(rs.getString("TITLE"));
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            if (stmt != null) { stmt.close(); } //close connection
        }
    }
}
