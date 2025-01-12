package com.Listeners;

import com.SelfHome.Main;
import com.SelfHome.Variable;
import com.Util.Home;
import com.Util.HomeAPI;
import com.Util.MySQL;
import com.Util.Util;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;


public class PlayerTeleportListener
  implements Listener
{
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
  public void onTeleport(PlayerTeleportEvent event) throws IOException {
    final World world = event.getTo().getWorld();
    
    
    if(Variable.flying_list.containsKey(event.getPlayer().getName())) {
    	String world_to = event.getTo().getWorld().getName();
    	if(!Variable.flying_list.get(event.getPlayer().getName()).equalsIgnoreCase(world_to)) {
    		if(event.getPlayer().getAllowFlight() == true) {
    			event.getPlayer().setAllowFlight(false);
    			event.getPlayer().sendMessage(Variable.Lang_YML.getString("ToggleccWorldDisableFlying"));
    		}
    		Variable.flying_list.remove(event.getPlayer().getName());
    	}
    	
    	
    }
    
    if (!Util.CheckIsHome(event.getTo().getWorld().getName().replace(Variable.world_prefix, ""))) {
      return;
    }







    
    if (!Main.JavaPlugin.getConfig().getBoolean("KeepInventory")) {
      event.getTo().getWorld().setGameRuleValue("keepInventory", "false");
    } else if (Main.JavaPlugin.getConfig().getBoolean("KeepInventory")) {
      event.getTo().getWorld().setGameRuleValue("keepInventory", "true");
    } 
    
    if (!Main.JavaPlugin.getConfig().getBoolean("doMobSpawning")) {
      event.getTo().getWorld().setGameRuleValue("doMobSpawning", "false");
    } else if (Main.JavaPlugin.getConfig().getBoolean("doMobSpawning")) {
      event.getTo().getWorld().setGameRuleValue("doMobSpawning", "true");
    } 
    if (!Main.JavaPlugin.getConfig().getBoolean("mobGriefing")) {
      event.getTo().getWorld().setGameRuleValue("mobGriefing", "false");
    } else if (Main.JavaPlugin.getConfig().getBoolean("mobGriefing")) {
      event.getTo().getWorld().setGameRuleValue("mobGriefing", "true");
    } 
    
    if (!Main.JavaPlugin.getConfig().getBoolean("doFireTick")) {
      event.getTo().getWorld().setGameRuleValue("doFireTick", "false");
    } else if (Main.JavaPlugin.getConfig().getBoolean("doFireTick")) {
      event.getTo().getWorld().setGameRuleValue("doFireTick", "true");
    } 







    
    final String name = world.getName().replace(Variable.world_prefix, "");
    Player p = event.getPlayer();




    
    if (name.equalsIgnoreCase(p.getName())) {
      int set_level = 1;
      for (int i = Main.JavaPlugin.getConfig().getInt("MaxLevel"); i > 0; i--) {
        if (p.hasPermission("SelfHome.Level." + i) && !p.isOp()) {
          set_level = i;
          break;
        } 
      } 
      if (Variable.bungee) {
        if (Integer.valueOf(MySQL.getLevel(name)).intValue() < set_level) {
          MySQL.setLevel(name, String.valueOf(set_level));
          String temp = Variable.Lang_YML.getString("AutoUpdateHomeLevel");
          p.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          p.sendMessage(temp);
          p.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        } 
      } else {
        File f2 = new File(Variable.Tempf, String.valueOf(name) + ".yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f2);
        if (yml.getInt("Level") < set_level) {
          yml.set("Level", Integer.valueOf(set_level));
          try {
            yml.save(f2);
          } catch (IOException e) {
            
            e.printStackTrace();
          } 
          String temp = Variable.Lang_YML.getString("AutoUpdateHomeLevel");
          p.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          p.sendMessage(temp);
          p.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        } 
      } 
    } 




    
    if (Variable.bungee) {
      
      if (MySQL.CheckIsAHome(name)) {
        Util.refreshBorder(event.getTo().getWorld());
        
        if (!Variable.KeepWorlds.contains(name)) {
          Variable.KeepWorlds.add(name);
        }
        if (!Util.Check(event.getPlayer(),event.getTo().getWorld().getName().replace(Variable.world_prefix, "")).booleanValue() && 
          !event.getPlayer().hasPermission("SelfHome.forcetp")&& !(HomeAPI.getHome(event.getTo().getWorld().getName()).isAllowStranger())) {
        p.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          String temp = Variable.Lang_YML.getString("TeleportStranger");
          p.sendMessage(temp);
          p.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          event.setCancelled(true);
          
          return;
        } 
        
        if (Util.CheckBlack(event.getPlayer(), 
            event.getTo().getWorld().getName().replace(Variable.world_prefix, "")).booleanValue() && 
          !event.getPlayer().isOp() && !event.getPlayer().hasPermission("SelfHome.forcetp")) {
          String temp = Variable.Lang_YML.getString("TeleportInBlack");
          p.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          p.sendMessage(temp);
          p.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          event.setCancelled(true);
          
          return;
        } 
        
        if (!event.getTo().getWorld().getName().replace(Variable.world_prefix, "").equalsIgnoreCase(event.getFrom().getWorld().getName().replace(Variable.world_prefix, ""))) {

          
          String temp = Variable.Lang_YML.getString("TeleportTip");
          if (temp.contains("<Name>")) {
            temp = temp.replace("<Name>", 
                event.getTo().getWorld().getName().replace(Variable.world_prefix, ""));
          }
          p.sendMessage(temp);













          
          if (!event.getPlayer().getName().equalsIgnoreCase(event.getTo().getWorld().getName().replace(Variable.world_prefix, "")))
          {
            if (event.getPlayer().hasPermission("SelfHome.Popularity"))
            {
              if (event.getPlayer().isOp()) {
                
                event.getPlayer().sendMessage(Variable.Lang_YML.getString("GivePopularityButOP"));

              
              }
              else if (Variable.popularity_list.containsKey(event.getPlayer().getName())) {
                List<String> list = (List<String>)Variable.popularity_list.get(event.getPlayer().getName());
                boolean has_vote = false;
                for (int i = 0; i < list.size(); i++) {
                  if (((String)list.get(i)).equalsIgnoreCase(event.getTo().getWorld().getName().replace(Variable.world_prefix, ""))) {
                    has_vote = true;
                    break;
                  } 
                } 
                if (!has_vote) {
                  list.add(event.getTo().getWorld().getName().replace(Variable.world_prefix, ""));
                  Variable.popularity_list.put(event.getPlayer().getName(), list);
                  Home home = HomeAPI.getHome(event.getTo().getWorld().getName().replace(Variable.world_prefix, ""));
                  
                  home.setPopularity(home.getPopularity() + 1);
                  String temp4 = Variable.Lang_YML.getString("PopularityAdd");
                  if (temp4.contains("<Name>")) {
                    temp4 = temp4.replace("<Name>", event.getTo().getWorld().getName().replace(Variable.world_prefix, ""));
                  }
                  event.getPlayer().sendMessage(temp4);

                  
                  String temp2 = Variable.Lang_YML.getString("PopularityAddToOwnerAndOP");
                  if (temp2.contains("<Player>")) {
                    temp2 = temp2.replace("<Player>", event.getPlayer().getName());
                  }
                  
                  for (String s : home.getOPs()) {
                    if (Bukkit.getPlayer(s) != null) {
                      Bukkit.getPlayer(temp2);
                    }
                  } 
                  
                  if (Bukkit.getPlayer(home.getName()) != null) {
                    Bukkit.getPlayer(home.getName()).sendMessage(temp2);
                  }
                }
              
              } else {
                
                List<String> list = new ArrayList<>();
                list.add(event.getTo().getWorld().getName().replace(Variable.world_prefix, ""));
                Variable.popularity_list.put(event.getPlayer().getName(), list);
                Home home = HomeAPI.getHome(event.getTo().getWorld().getName().replace(Variable.world_prefix, ""));
                home.setPopularity(1);

                
                String temp3 = Variable.Lang_YML.getString("PopularityAdd");
                if (temp3.contains("<Name>")) {
                  temp3 = temp3.replace("<Name>", event.getTo().getWorld().getName().replace(Variable.world_prefix, ""));
                }
                event.getPlayer().sendMessage(temp3);

                
                String temp2 = Variable.Lang_YML.getString("PopularityAddToOwnerAndOP");
                if (temp2.contains("<Player>")) {
                  temp2 = temp2.replace("<Player>", event.getPlayer().getName());
                }
                
                for (String s : home.getOPs()) {
                  if (Bukkit.getPlayer(s) != null) {
                    Bukkit.getPlayer(temp2);
                  }
                } 
                
                if (Bukkit.getPlayer(home.getName()) != null) {
                  Bukkit.getPlayer(home.getName()).sendMessage(temp2);
                }
              } 
            }
          }
        } 












































        
        MySQL.setVisitTime(event.getTo().getWorld().getName().replace(Variable.world_prefix, ""), String.valueOf(System.currentTimeMillis()));




        
        if (Main.JavaPlugin.getConfig().getString("Difficulty") != null) {
          if (Main.JavaPlugin.getConfig().getString("Difficulty").equalsIgnoreCase("")) {
            return;
          }
          if (Main.JavaPlugin.getConfig().getString("Difficulty").equalsIgnoreCase("Easy")) {
            event.getTo().getWorld().setDifficulty(Difficulty.EASY);
          } else if (Main.JavaPlugin.getConfig().getString("Difficulty").equalsIgnoreCase("Normal")) {
            event.getTo().getWorld().setDifficulty(Difficulty.NORMAL);
          } else if (Main.JavaPlugin.getConfig().getString("Difficulty").equalsIgnoreCase("Hard")) {
            event.getTo().getWorld().setDifficulty(Difficulty.HARD);
          } else if (Main.JavaPlugin.getConfig().getString("Difficulty").equalsIgnoreCase("Peaceful")) {
            event.getTo().getWorld().setDifficulty(Difficulty.PEACEFUL);
          } 
        } 
        
        if (Main.JavaPlugin.getConfig().getInt("MaxSpawnMonstersAmount") != -1) {
          world.setMonsterSpawnLimit(Main.JavaPlugin.getConfig().getInt("MaxSpawnMonstersAmount"));
        }
        
        if (Main.JavaPlugin.getConfig().getInt("MaxSpawnAnimalsAmount") != -1) {
          world.setMonsterSpawnLimit(Main.JavaPlugin.getConfig().getInt("MaxSpawnAnimalsAmount"));
        }
        
        boolean openborder = false;
        if (Main.JavaPlugin.getConfig().getString("CustomBorder") != null) {
          String temp = Main.JavaPlugin.getConfig().getString("CustomBorder");
          if (!temp.equalsIgnoreCase("")) {
            
            if (temp.contains("<Radius>")) {
              temp = temp.replace("<Radius>", String.valueOf((Main.JavaPlugin.getConfig().getInt("WorldBoard") + (Integer.valueOf(MySQL.getLevel(name)).intValue() - 1) * Main.JavaPlugin.getConfig().getInt("UpdateRadius")) / 2));
            }

            
            if (temp.contains("<Player>")) {
              temp = temp.replace("<Player>", event.getTo().getWorld().getName().replace(Variable.world_prefix, ""));
            }

            
            if (temp.contains("<X>")) {
              temp = temp.replace("<X>", String.valueOf(world.getSpawnLocation().getX()));
            }
            
            if (temp.contains("<Y>")) {
              temp = temp.replace("<Y>", String.valueOf(world.getSpawnLocation().getY()));
            }
            
            if (temp.contains("<Z>")) {
              temp = temp.replace("<Z>", String.valueOf(world.getSpawnLocation().getZ()));
            }
            
            openborder = true;
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), temp);
          } 
        } 
        
        if (!openborder) {
          (new BukkitRunnable()
            {
              public void run()
              {
                if (Main.JavaPlugin.getConfig().getBoolean("BorderSwitch")) {
                  try {
                    world.getWorldBorder().setCenter(world.getSpawnLocation());
                    world.getWorldBorder()
                      .setSize((Main.JavaPlugin.getConfig().getInt("WorldBoard") + (
                        Integer.valueOf(MySQL.getLevel(name)).intValue() - 1) * 
                        Main.JavaPlugin.getConfig().getInt("UpdateRadius")));
                  } catch (NoSuchMethodError e) {
                    Bukkit.getConsoleSender()
                      .sendMessage(Variable.Lang_YML.getString("BorderException"));
                  }
                
                }
              }
            }).runTaskLater((Plugin)Main.JavaPlugin, 5L);
        }
      }
    
    }
    else {
      
      File f2 = new File(Variable.Tempf, String.valueOf(name) + ".yml");
      
      if (f2.exists()) {




        
        Util.refreshBorder(event.getTo().getWorld());
        
        if (!Variable.KeepWorlds.contains(name)) {
          Variable.KeepWorlds.add(name);
        }
      
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f2);

        
        if (yml.getDouble("X") == 0.0D) {
          yml.set("X", Double.valueOf(event.getTo().getWorld().getSpawnLocation().getX()));
          yml.save(f2);
        } 
        if (yml.getDouble("Y") == 0.0D) {
          yml.set("Y", Double.valueOf(event.getTo().getWorld().getSpawnLocation().getY()));
          yml.save(f2);
        } 
        if (yml.getDouble("Z") == 0.0D) {
          yml.set("Z", Double.valueOf(event.getTo().getWorld().getSpawnLocation().getZ()));
          yml.save(f2);
        } 

        
        if (!Util.Check(event.getPlayer(), 
            event.getTo().getWorld().getName().replace(Variable.world_prefix, "")).booleanValue() && 
          !event.getPlayer().hasPermission("SelfHome.forcetp") && 
          !yml.getBoolean("Public") && !event.getPlayer().isOp()) {
          p.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          String temp = Variable.Lang_YML.getString("TeleportStranger");
          p.sendMessage(temp);
          p.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          event.setCancelled(true);
          
          return;
        } 
        
        if (Util.CheckBlack(event.getPlayer(), 
            event.getTo().getWorld().getName().replace(Variable.world_prefix, "")).booleanValue() && 
          !event.getPlayer().isOp() && !event.getPlayer().hasPermission("SelfHome.forcetp")) {
          String temp = Variable.Lang_YML.getString("TeleportInBlack");
          p.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          p.sendMessage(temp);
          p.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          event.setCancelled(true);
          
          return;
        } 
        
        if (!event.getTo().getWorld().getName().replace(Variable.world_prefix, "").equalsIgnoreCase(event.getFrom().getWorld().getName().replace(Variable.world_prefix, ""))) {

          
          String temp = Variable.Lang_YML.getString("TeleportTip");
          if (temp.contains("<Name>")) {
            temp = temp.replace("<Name>", 
                event.getTo().getWorld().getName().replace(Variable.world_prefix, ""));
          }
          p.sendMessage(temp);
        } 
        
        if (Main.JavaPlugin.getConfig().getString("Difficulty") != null) {
          if (Main.JavaPlugin.getConfig().getString("Difficulty").equalsIgnoreCase("")) {
            return;
          }
          if (Main.JavaPlugin.getConfig().getString("Difficulty").equalsIgnoreCase("Easy")) {
            event.getTo().getWorld().setDifficulty(Difficulty.EASY);
          } else if (Main.JavaPlugin.getConfig().getString("Difficulty").equalsIgnoreCase("Normal")) {
            event.getTo().getWorld().setDifficulty(Difficulty.NORMAL);
          } else if (Main.JavaPlugin.getConfig().getString("Difficulty").equalsIgnoreCase("Hard")) {
            event.getTo().getWorld().setDifficulty(Difficulty.HARD);
          } else if (Main.JavaPlugin.getConfig().getString("Difficulty").equalsIgnoreCase("Peaceful")) {
            event.getTo().getWorld().setDifficulty(Difficulty.PEACEFUL);
          } 
        } 
        
        if (Main.JavaPlugin.getConfig().getInt("MaxSpawnMonstersAmount") != -1) {
          world.setMonsterSpawnLimit(Main.JavaPlugin.getConfig().getInt("MaxSpawnMonstersAmount"));
        }
        
        if (Main.JavaPlugin.getConfig().getInt("MaxSpawnAnimalsAmount") != -1) {
          world.setMonsterSpawnLimit(Main.JavaPlugin.getConfig().getInt("MaxSpawnAnimalsAmount"));
        }
        
        boolean openborder = false;
        if (Main.JavaPlugin.getConfig().getString("CustomBorder") != null) {
          String temp = Main.JavaPlugin.getConfig().getString("CustomBorder");
          if (!temp.equalsIgnoreCase("")) {
            
            if (temp.contains("<Radius>")) {
              temp = temp.replace("<Radius>", 
                  String.valueOf((Main.JavaPlugin.getConfig().getInt("WorldBoard") + yml.getInt("Level") - 1) / 2));
            }
            
            if (temp.contains("<Player>")) {
              temp = temp.replace("<Player>", event.getTo().getWorld().getName().replace(Variable.world_prefix, ""));
            }
            
            if (temp.contains("<X>")) {
              temp = temp.replace("<X>", String.valueOf(world.getSpawnLocation().getX()));
            }
            
            if (temp.contains("<Y>")) {
              temp = temp.replace("<Y>", String.valueOf(world.getSpawnLocation().getY()));
            }
            
            if (temp.contains("<Z>")) {
              temp = temp.replace("<Z>", String.valueOf(world.getSpawnLocation().getZ()));
            }
            openborder = true;
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), temp);
          } 
        } 


        
        try {
          yml.save(f2);
        } catch (IOException e) {
          
          e.printStackTrace();
        } 
        
        if (!openborder)
          (new BukkitRunnable()
            {
              public void run()
              {
                if (Main.JavaPlugin.getConfig().getBoolean("BorderSwitch")) {
                  try {
                    world.getWorldBorder().setCenter(world.getSpawnLocation());
                    world.getWorldBorder().setSize((
                        Main.JavaPlugin.getConfig().getInt("WorldBoard") + (yml.getInt("Level") - 1) * 
                        Main.JavaPlugin.getConfig().getInt("UpdateRadius")));
                  } catch (NoSuchMethodError e) {
                    Bukkit.getConsoleSender()
                      .sendMessage(Variable.Lang_YML.getString("BorderException"));
                  }
                
                }
              }
            }).runTaskLater((Plugin)Main.JavaPlugin, 5L); 
      } 
    } 
  }
}


