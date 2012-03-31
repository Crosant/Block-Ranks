package me.crosant.blockranks;

import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.*;
import net.milkbowl.vault.*;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.PermissionEntity;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;


public class BlockRanks extends JavaPlugin 
{
    public static Permission perms = null;
    public static BlockRanks plugin;
        public static String username;
	public static String password;
	public static String db;
	public static String host;
        public static String port;
        public static Economy economy = null;

        public static Map<String, Long> player_blocks = new HashMap<String, Long>();
         public static Map<String, Boolean> player_bool = new HashMap<String, Boolean>();
        
        private static Vault vault = null;


    	protected FileConfiguration config;
	private static final Logger log = Logger.getLogger("Minecraft");
    
    @Override
   public void onDisable() {
        
        
        List<Player> onlinePlayers = Arrays.asList(Bukkit.getServer().getOnlinePlayers());
        Iterator<Player> iterator = onlinePlayers.iterator();
        while(iterator.hasNext()) 
        
        { 
            Player onlinePlayer = iterator.next();
            me.crosant.blockranks.SQL.setBlocks(onlinePlayer.getName(), BlockRanks.player_blocks.get(onlinePlayer.getName()));
        }
        
            
		log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
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
               boolean exists = (new File("plugins/BlockRanks/config.yml")).exists();	
            
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
           String query =   ( "CREATE TABLE IF NOT EXISTS `blockranks` ("
  + "`id` int(10) NOT NULL AUTO_INCREMENT,"
  +"`player` varchar(100) NOT NULL,"
  +"`blocks` int(255) NOT NULL,"
  +"PRIMARY KEY (`id`),"
  +"UNIQUE KEY `player` (`player`)"
+") ");
                 
                 PreparedStatement preparedStmt = conn.prepareStatement(query);
                 
                 preparedStmt.executeUpdate();
           
           rs = st.executeQuery( "select * from BlockRanks");
        }
        catch(SQLException sqle){
            
        }
        try {
            while (rs.next()) {
        
                
                
                
                
            Long blocks = rs.getLong("blocks"); // Alternativ: result.getString(1);
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
            
            
            pm.registerEvents(new BlockRanksBlockListener(this), this);
            pm.registerEvents(new BlockRanksPlayerListener(), this);
            
            
            
            
            
	
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
        this.getConfig().set("Rank.1.blocks", 1);
        this.getConfig().set("Rank.2.blocks", 5);
        this.getConfig().set("Rank.3.blocks", 10);
        this.getConfig().set("Rank.4.blocks", 20);
        this.getConfig().set("Rank.5.blocks", 50);
        this.getConfig().set("Rank.6.blocks", 100);
        this.getConfig().set("Rank.1.money", 1);
        this.getConfig().set("Rank.2.money", 5);
        this.getConfig().set("Rank.3.money", 10);
        this.getConfig().set("Rank.4.money", 20);
        this.getConfig().set("Rank.5.money", 50);
        this.getConfig().set("Rank.6.money", 100);
        
        
        
        //this.getConfig().set("Basic.Permission", "yes");
    	this.getConfig().set("Messanges.nopermission", "You don't have the required permissions to do this.");
    	this.getConfig().set("Messanges.rankup", "You are now a %rank%");
    	this.getConfig().set("Messanges.blocks", "You have set %blocks% blocks");
        this.getConfig().set("Messanges.reloadstart", "Started");
        this.getConfig().set("Messanges.reloadend", "Finished");
        this.getConfig().set("Messanges.top5", " %player% mit %blocks%");
        this.getConfig().set("Messanges.money", "You have get %money%");
        //this.getConfig().set("Messanges.register", "%player% is now registered to BlockRanks");
        
  	
    	
    	
    	
    	this.saveConfig();
        }
    	
        port = this.getConfig().getString("SQL.port");
        username = this.getConfig().getString("SQL.username");
        db = this.getConfig().getString("SQL.db");
        password = this.getConfig().getString("SQL.password");
        host = this.getConfig().getString("SQL.host");
                Plugin x = this.getServer().getPluginManager().getPlugin("Vault");
        if(x != null & x instanceof Vault) {
            vault = (Vault) x;
            
            log.info(String.format("[%s] Hooked %s %s", getDescription().getName(), vault.getDescription().getName(), vault.getDescription().getVersion()));
        } else {
            log.warning(String.format("[%s] Vault was _NOT_ found! Disabling plugin.", getDescription().getName()));
            getPluginLoader().disablePlugin(this);
            return;
        }
        
        if (!setupEconomy() ) {
            log.info(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
		log.info(String.format("[%s] Enabled Version %s", getDescription().getName(), getDescription().getVersion()));
    	
                
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
             
             if (args.length == 0)
             {
                 
                 player.sendMessage("/Ranks blocks");
                 player.sendMessage("/Ranks top5");
                 player.sendMessage("/Ranks reload");
                 
             }
             
             if (args.length > 0){
                 if (args[0].equalsIgnoreCase("blocks")){
                    Long Blocks1 = player_blocks.get(player.getName());
                    String Blocks = String.valueOf(Blocks1);
                    player.sendMessage(this.getConfig().getString("Messanges.blocks").replace("%blocks%", Blocks));
                    return true;
                 }
             
             
                 
                 else if (args[0].equalsIgnoreCase("top5")){
                     
                          List<Player> onlinePlayers = Arrays.asList(Bukkit.getServer().getOnlinePlayers());
        Iterator<Player> iterator = onlinePlayers.iterator();
        while(iterator.hasNext()) 
        
        { 
            Player onlinePlayer = iterator.next();
            me.crosant.blockranks.SQL.setBlocks(onlinePlayer.getName(), BlockRanks.player_blocks.get(onlinePlayer.getName()));
        }
                     
                     
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
           rs = st.executeQuery( "select * from BlockRanks ORDER BY blocks DESC LIMIT 5");
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
                       
                       player.sendMessage(i + ". " + this.getConfig().getString("Messanges.top5").replace("%player%", playername).replace("%blocks%", blocks));
                       
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
                 
                 else if (args[0].equalsIgnoreCase("reload")){
                     if (perms.has(player, "BlockRanks.reload")){

                     Bukkit.getServer().broadcastMessage("[BlockRanks] "+ this.getConfig().getString("Messanges.reloadstart"));
                     
                     this.reloadConfig();
                     
                     Bukkit.getServer().broadcastMessage("[BlockRanks] " + this.getConfig().getString("Messanges.reloadend"));
                     }
                     else{
                         player.sendMessage(this.getConfig().getString("Messanges.nopermission"));
                     }
                 }
                    
                 else if (args[0].equalsIgnoreCase("purge") && perms.has(player, "BlockRanks.purge")){
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
         conn = DriverManager.getConnection("jdbc:mysql://" + BlockRanks.host + ":" + BlockRanks.port + "/" + BlockRanks.db, BlockRanks.username, BlockRanks.password); 
        } 
        catch(SQLException sqle) 
        { 
        System.out.println("Verbindung ist fehlgeschlagen: " + sqle.getMessage()); 
        }
        try
        {
           st = conn.createStatement();
          
           
                 String query =   ( "UPDATE `BlockRanks` SET `blocks` = 0 WHERE 1");
                 BlockRanks.player_blocks.clear();
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
             if(args.length > 1){
                 
                 if (args[0].equalsIgnoreCase("set") && perms.has(player, "BlockRanks.set")){
                     String s = args[2];
                     long lo;
                      try {
        lo = Long.parseLong(s.trim());
        BlockRanks.player_blocks.put(args[1], lo);
                    PermissionManager manager = PermissionsEx.getPermissionManager();

              PermissionManager pex = PermissionsEx.getPermissionManager();
              PermissionEntity entity = pex.getUser(player);
              String[] groups =  manager.getUser(player).getGroupsNames();
                      for(int i = 1; i <= 25; i++) manager.getUser(player).removeGroup(plugin.getConfig().getString("Rank."+ i +".name"));
              for(int i = 1; i <=25; i++){
                  
                 // System.out.println(plugin.getConfig().getString("Rank."+ i +".name"));
                  if(plugin.getConfig().getString("Rank."+ i +".name") != null){
                  
                      
                      //System.out.println(plugin.getConfig().getString("Rank."+ i +".name"));
                      if (i > 1){
                          //System.out.println(plugin.getConfig().getString("Rank."+ i +".name"));
                             //                 System.out.println(plugin.getConfig().getString("Messanges.rankup").replace("%rank%", plugin.getConfig().getString("Rank." + i + ".name")));
                          
                      if (BlockRanks.player_blocks.get(player.getName()) >= plugin.getConfig().getLong("Rank." + i + ".blocks")){
                 //   System.out.println(plugin.getConfig().getString("Messanges.rankup").replace("%rank%", plugin.getConfig().getString("Rank." + i + ".name")));
                    player.sendMessage(plugin.getConfig().getString("Messanges.rankup").replace("%rank%", plugin.getConfig().getString("Rank." + i + ".name")) );
                    plugin.giveCash(player, plugin.getConfig().getLong("Rank." + i + ".money"));
                    
                    
                    
                    for (int n = 0; n<groups.length; n++){
                        System.out.println(groups[n]);
                    manager.getUser(player).removeGroup(groups[n]);
                    }
                    
                    
                    
                    
                    
                    manager.getUser(player).addGroup(plugin.getConfig().getString("Rank." + i + ".name"));
                     for (int l = 0; l <groups.length; l++){
                  manager.getUser(player).addGroup(groups[l]);
                     }
                  int o = i-1;
                 manager.getUser(player).removeGroup(plugin.getConfig().getString("Rank."+ o + ".name"));   
                      }
                      }
                      else {
                     if (BlockRanks.player_blocks.get(player.getName()) >= plugin.getConfig().getLong("Rank." + i + ".blocks")){
                    
                    player.sendMessage(plugin.getConfig().getString("Messanges.rankup").replace("%rank%", plugin.getConfig().getString("Rank." + i + ".name")) );
                    plugin.giveCash(player, plugin.getConfig().getLong("Rank." + i + ".money"));
                                        for (int n = 0; n<groups.length; n++){
                    manager.getUser(player).removeGroup(groups[n]);
                    }
                                                          manager.getUser(player).addGroup(plugin.getConfig().getString("Rank." + i + ".name"));

                     for (int l = 0; l <groups.length; l++){
                  manager.getUser(player).addGroup(groups[l]);
                     }                                                }
                      }
                }
                  }
                  
              
                

              
                      
                      
                      
                      
                      
                      
                      
                      
                      
                      } catch (NumberFormatException nfe) {
         System.out.println("NumberFormatException: " + nfe.getMessage());
      }
                     
                     
                 }
                 
             }
                 
                 
             }
             
         }
     
     return true;
    }

        private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
    
    public boolean giveCash(Player player,Long amount){
        if (amount != 0){
        player.sendMessage(this.getConfig().getString("Messanges.money").replace("%money%", amount.toString()));
        economy.depositPlayer(player.getName(), amount);
        }
        return true;
        
    }
}
