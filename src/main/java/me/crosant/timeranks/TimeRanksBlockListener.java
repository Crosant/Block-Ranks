/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.crosant.timeranks;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import ru.tehkode.permissions.PermissionEntity;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 *
 * @author Florian
 */
public class TimeRanksBlockListener extends BlockListener{

    public static TimeRanks plugin;
    
    TimeRanksBlockListener(TimeRanks instance) {
        plugin = instance;
    }
    
    
    @Override
            public void onBlockPlace(BlockPlaceEvent event){
                int blocks;
                Player player = event.getPlayer();
                Block block = event.getBlock();
                Material mat = block.getType();
                //blocks = SQL.getBlocks(player.getName());
                blocks = TimeRanks.player_blocks.get(player.getName());
                int blocks1 = blocks + 1;
                //SQL.setBlocks(player.getName(), blocks1);
                TimeRanks.player_blocks.put(player.getName(), blocks1);
                
               // player.sendMessage(TimeRanks.player_blocks.get(player.getName()).toString());
                
              if(Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")){
              PermissionManager manager = PermissionsEx.getPermissionManager();

              PermissionManager pex = PermissionsEx.getPermissionManager();
              PermissionEntity entity = pex.getUser(player);
              
              
              if(plugin.getConfig().getBoolean("Basic.activated") == true){
                  
              
                
                if (TimeRanks.player_blocks.get(player.getName()) > plugin.getConfig().getInt("Rank.6.blocks")){
                    
                    player.sendMessage("You are now a " + plugin.getConfig().getString("Rank.6.name"));

                  manager.getUser(player).addGroup(plugin.getConfig().getString("Rank.6.name"));  
                  if(manager.getUser(player).getGroupsNames().toString().contains(plugin.getConfig().getString("Rank.5.name")))manager.getUser(player).removeGroup(plugin.getConfig().getString("Rank.5.name"));   
                    
                }
                
                else if (TimeRanks.player_blocks.get(player.getName()) > plugin.getConfig().getInt("Rank.5.blocks")){
                    
                    player.sendMessage("You are now a " + plugin.getConfig().getString("Rank.5.name"));
                    
                    
                    manager.getUser(player).addGroup(plugin.getConfig().getString("Rank.5.name"));  
                 if(manager.getUser(player).getGroupsNames().toString().contains(plugin.getConfig().getString("Rank.4.name")))manager.getUser(player).removeGroup(plugin.getConfig().getString("Rank.4.name"));  
                }
                                
                else if (TimeRanks.player_blocks.get(player.getName()) > plugin.getConfig().getInt("Rank.4.blocks")){
                    
                    player.sendMessage("You are now a " + plugin.getConfig().getString("Rank.4.name"));
                    
                    
                    manager.getUser(player).addGroup(plugin.getConfig().getString("Rank.4.name"));  
                 if(manager.getUser(player).getGroupsNames().toString().contains(plugin.getConfig().getString("Rank.3.name")))manager.getUser(player).removeGroup(plugin.getConfig().getString("Rank.3.name"));   
                }
                                                
                else if (TimeRanks.player_blocks.get(player.getName()) > plugin.getConfig().getInt("Rank.3.blocks")){
                    
                    player.sendMessage("You are now a " + plugin.getConfig().getString("Rank.3.name"));
                    
                    
                    manager.getUser(player).addGroup(plugin.getConfig().getString("Rank.3.name"));  
                  if(manager.getUser(player).getGroupsNames().toString().contains(plugin.getConfig().getString("Rank.2.name")))manager.getUser(player).removeGroup(plugin.getConfig().getString("Rank.2.name"));   
                }
                                                                
                else if (TimeRanks.player_blocks.get(player.getName()) > plugin.getConfig().getInt("Rank.2.blocks")){
                    
                    player.sendMessage("You are now a " + plugin.getConfig().getString("Rank.2.name"));
                    
                    
                    manager.getUser(player).addGroup(plugin.getConfig().getString("Rank.2.name"));
                    
                    if(manager.getUser(player).getGroupsNames().toString().contains(plugin.getConfig().getString("Rank.1.name")))manager.getUser(player).removeGroup(plugin.getConfig().getString("Rank.1.name"));  
                  
                }
                                                                                
                else if (TimeRanks.player_blocks.get(player.getName()) > plugin.getConfig().getInt("Rank.1.blocks")){
                    
                    player.sendMessage("You are now a " + plugin.getConfig().getString("Rank.1.name"));
                    
                    
                    manager.getUser(player).addGroup(plugin.getConfig().getString("Rank.1.name"));  
 
                }
                                                                                                
                else{
                    
                }
              }
              }
              else {
                  
                     Logger.getLogger("Minecraft").warning("PermissionsEx plugin are not found.");

              }
              //  player.sendMessage("You placed a block with ID : " + mat);
    
}
}
