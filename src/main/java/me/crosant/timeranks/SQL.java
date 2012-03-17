package me.crosant.timeranks;

import java.net.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;

/**
 *
 * @author Florian
 */
public class SQL {
     public static String blocks; 
    public static Long getBlocks(String player) {
        Connection conn = null;
        Statement  st = null;
        ResultSet  rs = null;
        try 
        { 
             Class.forName("org.gjt.mm.mysql.Driver"); 
        } 
        catch(ClassNotFoundException cnfe) 
        { 
            System.out.println("Treiber kann nicht geladen werden: "+cnfe.getMessage()); 
        }
        
        try 
        { 
         
        
         conn = DriverManager.getConnection("jdbc:mysql://" + TimeRanks.host + ":"
                    + TimeRanks.port + "/" + TimeRanks.db + "?" + "user=" + TimeRanks.username + "&"
                    + "password=" + TimeRanks.password);
                 } 
        catch(SQLException sqle) 
        { 
        System.out.println("Verbindung ist fehlgeschlagen: " + sqle.getMessage()); 
        }
        try
        {
           st = conn.createStatement();
           rs = st.executeQuery( "select blocks from timeranks where player = '" + player + "'");
        }
        catch(SQLException sqle){
            
        }
        try {
            while (rs.next()) {

                        blocks = rs.getString("blocks"); // Alternativ: result.getString(1);
                    //    conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                //    System.out.println(player + " " + blocks);
                    
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Query ist fehlgeschlagen: " + ex.getMessage());
        }
        return Long.valueOf(blocks);
        
    }
    
        public static void setBlocks(String player, Long Blocks){
        Connection conn = null;
        Statement  st = null;
        ResultSet  rs = null;
        try 
        { 
             Class.forName("org.gjt.mm.mysql.Driver"); 
        } 
        catch(ClassNotFoundException cnfe) 
        { 
            System.out.println("Treiber kann nicht geladen werden: "+cnfe.getMessage()); 
        }
        
        try 
        { 
         conn = DriverManager.getConnection("jdbc:mysql://" + TimeRanks.host + ":" + TimeRanks.port + "/" + TimeRanks.db, TimeRanks.username, TimeRanks.password); 
        } 
        catch(SQLException sqle) 
        { 
        System.out.println("Verbindung ist fehlgeschlagen: " + sqle.getMessage()); 
        }
        try
        {
           st = conn.createStatement();
          
           
                 String query =   ( "UPDATE `timeranks` SET `blocks` = "+ Blocks +" WHERE `player` ='" + player + "'");
                 
                 PreparedStatement preparedStmt = conn.prepareStatement(query);
                 
                 preparedStmt.executeUpdate();
                 
               //  conn.close();
                 
                // rs = st.executeUpdate(query);
        }
        catch(SQLException sqle){
            System.out.println("Query ist fehlgeschlagen: " + sqle.getMessage());
        }
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Query ist fehlgeschlagen: " + ex.getMessage());
        }
        
    }
        
        
        

        
        
        
    
    
}
