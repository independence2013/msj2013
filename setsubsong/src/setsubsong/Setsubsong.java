/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package setsubsong;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Jeffrey
 */
public class Setsubsong {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        Connection con = startconnection("orcl");
        String mdir = "F:\\Jeffrey\\Desktop\\individual\\cut songs\\classified subsong"; //directory for songs
        File musicdir = new File(mdir);
        File[] allfiles = musicdir.listFiles(new FilenameFilter(){ //use filter to make sure we don't read any album art files (.jpg)
            @Override
            public boolean accept(File dir, String name){
                if(name.toLowerCase().endsWith(".jpg")){
                    return false;
                }
                return true;
            }
        }
        );
        getmoods(allfiles, con);
    }
    
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
    
    public static void setsubsong(Connection con, String title, int[] values) throws SQLException{
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                   ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery(
            "SELECT S0, S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, S11, S12, S13, S14, S15, S16, S17, S18, S19, S20, S21, S22, S23, S24, S25, S26, S27, S28, S29, S30 FROM SONGTABLE WHERE TITLE = '" + title + "'"); //get row with the song
            while (uprs.next()) {
                for(int i = 0; i < values.length; i++){
                    uprs.updateInt("S" + i, values[i]);
                }
                uprs.updateRow(); //update database
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            if (stmt != null) { stmt.close(); } //close connection
        }
    }
    public static void getmoods(File[] allfiles, Connection con) throws SQLException{
        for(int z = 0; z < allfiles.length; z++){
            Path file;
            file = Paths.get(allfiles[z].getAbsolutePath());
            try(InputStream in = Files.newInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in))){
                String line = null;
                int[] moods = new int[31];
                for(int j = 0; j<31; j++){
                    moods[j] = -1;
                }
                int i = 0;
                boolean dataflag = false;
                while ((line = reader.readLine()) != null) { //if line not null (not file end)
                    if(!line.isEmpty()){
                        if(i < 31){
                            if(line.toLowerCase().equals("@data")){
                                dataflag = true;
                            }
                            if(dataflag && !line.equals("@data")){
                                int index = line.lastIndexOf(",");
                                System.out.println(line.substring(index+1));
                                moods[i] = Integer.parseInt(line.substring(index+1));
                                i++;
                            }
                        }
                    }
                }
                reader.close();
                setsubsong(con, allfiles[z].getName(),moods);
            } catch (IOException x){ //if there is an IO error when reading file
                System.err.println(x); //print out error
            }
        }
    }
}
