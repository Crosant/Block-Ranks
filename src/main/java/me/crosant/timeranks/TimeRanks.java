package me.crosant.timeranks;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class TimeRanks extends JavaPlugin 
{
    
        public static String username;
	public static String password;
	public static String db;
	public static String host;
        public static String port;
    
        public static Map<String, Integer> player_blocks = new HashMap<String, Integer>();
        
        
        private final TimeRanksPlayerListener playerListener = new TimeRanksPlayerListener(this);
        private final TimeRanksBlockListener blockListener = new TimeRanksBlockListener(this);
    	protected FileConfiguration config;
	public static final Logger log = Logger.getLogger("Minecraft");
    
    @Override
   public void onDisable() {
        
        for(Player player : getServer().getOnlinePlayers()) 
        { 
        SQL.setBlocks(player.getName(), TimeRanks.player_blocks.get(player.getName()));
        }
        
        
		log.info("TimeRanks V0.0.1 has been disabled.");
	}

    @Override
	public void onEnable() {
        
                port = this.getConfig().getString("SQL.port");
        username = this.getConfig().getString("SQL.username");
        db = this.getConfig().getString("SQL.db");
        password = this.getConfig().getString("SQL.password");
        host = this.getConfig().getString("SQL.host");
        
                Connection conn = null;
        Statement  st = null;
        ResultSet  rs = null;
               boolean exists = (new File("plugins/TimeRanks/config.yml")).exists();	
            
            if (exists){
            
                

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
           rs = st.executeQuery( "select * from timeranks");
        }
        catch(SQLException sqle){
            
        }
        try {
            while (rs.next()) {
        
                
                
                
                
            int blocks = rs.getInt("blocks"); // Alternativ: result.getString(1);
            String playername1 = rs.getString("player");
            
            player_blocks.put(playername1, blocks);
            
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
        
    
        
        
            
            PluginManager pm = this.getServer().getPluginManager();
            
            pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal, this);
            
            
            
            
            
	
            }
			
		
    	config = getConfig();
        if (exists){}
        else{
        this.getConfig().set("Basic.activated", true);

        this.getConfig().set("SQL.username", "username");
        this.getConfig().set("SQL.password", "password");
        this.getConfig().set("SQL.db", "db");
        this.getConfig().set("SQL.host", "host");
        this.getConfig().set("SQL.port", "port");

        
        
        
        this.getConfig().set("Rank.1.name", "Rank1");
        this.getConfig().set("Rank.2.name", "Rank2");
        this.getConfig().set("Rank.3.name", "Rank3");
        this.getConfig().set("Rank.4.name", "Rank4");
        this.getConfig().set("Rank.5.name", "Rank5");
        this.getConfig().set("Rank.6.name", "Rank6");
        this.getConfig().set("Rank.1.blocks", "1");
        this.getConfig().set("Rank.2.blocks", "5");
        this.getConfig().set("Rank.3.blocks", "10");
        this.getConfig().set("Rank.4.blocks", "20");
        this.getConfig().set("Rank.5.blocks", "50");
        this.getConfig().set("Rank.6.blocks", "100");
        
        
        
        //this.getConfig().set("Basic.Permission", "yes");
    	this.getConfig().set("Messages.nopermission", "You don't have the required permissions to do this.");
    	
    	
    	
    	
    	this.saveConfig();
        }
    	
        port = this.getConfig().getString("SQL.port");
        username = this.getConfig().getString("SQL.username");
        db = this.getConfig().getString("SQL.db");
        password = this.getConfig().getString("SQL.password");
        host = this.getConfig().getString("SQL.host");
        
    	log.info("TimeRanks V0.0.1 has been enabled.");
    	
    	
    	
        }
        
    @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	Player player = null;
        Player user = null;

    	if (sender instanceof Player) {
    		player = (Player) sender;
    	}
     if (player == null) {
    		sender.sendMessage("this command can only be run by a player");
                return false;
    		} else {
         if (cmd.getName().equalsIgnoreCase("Ranks")) {
             
             if (args.length > 0){
                 if (args[0].equalsIgnoreCase("blocks")){
                    int Blocks1 = SQL.getBlocks(player.getName());
                    String Blocks = String.valueOf(Blocks1);
                    player.sendMessage("You have set " +Blocks + " Blocks");
                    return true;
                 }
             
             
                 
                 else if (args[0].equalsIgnoreCase("top5")){
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
           rs = st.executeQuery( "select * from timeranks ORDER BY blocks LIMIT 5");
        }
        catch(SQLException sqle){
                    System.out.println("Querry ist fehlgeschlagen: " + sqle.getMessage()); 

        }
        try {
            int i = 0;
            while (rs.next()) {
                    i++;
                       String playername = rs.getString("player"); // Alternativ: result.getString(1);
                       String blocks = rs.getString("blocks");
                       
                       player.sendMessage(i + ". " + playername + " with " + blocks + " blocks");
                       
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
        
        
    }
                     
                 }
                 
                 
             }
             
         }
     
     return true;
    }
}