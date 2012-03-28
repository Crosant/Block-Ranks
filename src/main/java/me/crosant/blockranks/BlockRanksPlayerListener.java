/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.crosant.blockranks;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Florian
 */
public class BlockRanksPlayerListener implements Listener{
            public static String playername;
            public static BlockRanks plugin;
       

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event){
            
        Player player = event.getPlayer();
        
        
        
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
         
        
         conn = DriverManager.getConnection("jdbc:mysql://" + BlockRanks.host + ":"
                    + BlockRanks.port + "/" + BlockRanks.db + "?" + "user=" + BlockRanks.username + "&"
                    + "password=" + BlockRanks.password);
                 } 
        catch(SQLException sqle) 
        { 
        System.out.println("Verbindung ist fehlgeschlagen: " + sqle.getMessage()); 
        }
        try
        {
           st = conn.createStatement();
           rs = st.executeQuery( "select player from BlockRanks where player = '" + player.getName() + "'");
        }
        catch(SQLException sqle){
            System.out.println("Query ist fehlgeschlagen: " + sqle.getMessage());
        }
        try {
            while (rs.next()) {

                        playername = rs.getString("player"); // Alternativ: result.getString(1);
                    //    conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
        }
       // System.out.println("!!!!!!!!!!!!!!!!!                          " + playername);
        try {
            if(playername != null){
               // System.out.println(playername);
            }
            else{
                
                st.executeUpdate( "INSERT INTO BlockRanks (player, blocks) values ('" + player.getName() + "', 0)");
                BlockRanks.player_blocks.put(player.getName(), Long.valueOf("0"));
                //Bukkit.getServer().broadcastMessage(plugin.getConfig().getString("Messanges.register").replace("%player%", player.getName()));
            }
        } catch (SQLException sqle) {
            System.out.println("Query ist fehlgeschlagen: " + sqle.getMessage());
        }
        
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Query ist fehlgeschlagen: " + ex.getMessage());
        }
        
                //    System.out.println(player + " " + blocks);
        
    }
        
    
        @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        
        String player = event.getPlayer().getName();
       
        SQL.setBlocks(player, BlockRanks.player_blocks.get(player));
        
        //System.out.println(player + "    " + BlockRanks.player_blocks.get(player));
        
    }
        
        
        
        
}
        
        
        
        
        
        
        
        
    