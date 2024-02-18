package com.SelfHome;

import com.GUI.CheckGui;
import com.GUI.CreateGui;
import com.GUI.DenyGui;
import com.GUI.GiftGui;
import com.GUI.InviteGui;
import com.GUI.MainGui;
import com.GUI.ManageGui;
import com.GUI.ManageGui2;
import com.GUI.TrustGui;
import com.GUI.VisitGui;
import com.Listeners.WorldInitListener;
import com.Util.Channel;
import com.Util.CustomChunkGenerator;
import com.Util.FirstBorderShaped;
import com.Util.Home;
import com.Util.HomeAPI;
import com.Util.MySQL;
import com.Util.R1_12_2;
import com.Util.R1_7_10;
import com.Util.Schematics;
import com.Util.StaticsTick;
import com.Util.Util;
import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.bukkit.wrapper.AsyncWorld;
import com.boydti.fawe.util.TaskManager;
import com.comphenix.protocol.utility.StreamSerializer;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.WorldCreator;



import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.noise.PerlinNoiseGenerator;

import com.sk89q.worldedit.WorldEdit;

import WorldBorder.WBControl;









public class CommandListener
  implements CommandExecutor, TabExecutor
{
  public void invite_guoqi(final Player p) { (new BukkitRunnable()
      {
        public void run() {
          if (Variable.invite_list.containsKey(p.getName())) {
            if (p != null) {
              String temp2 = Variable.Lang_YML.getString("InviteOtherHasBeenOutDated");
              if (temp2.contains("<Name>")) {
                temp2 = temp2.replace("<Name>", Variable.invite_list.get(p.getName()));
              }
              p.sendMessage(temp2);
            } 
            Player beinvite = Bukkit.getPlayer(Variable.invite_list.get(p.getName()));
            if (beinvite != null) {
              String temp = Variable.Lang_YML.getString("InviteHasBeenOutDated");
              if (temp.contains("<Name>")) {
                temp = temp.replace("<Name>", p.getName());
              }
              
              beinvite.sendMessage(temp);
            } 
            Variable.invite_list.remove(p.getName());
          } 
        }
      }).runTaskLater((Plugin)Main.JavaPlugin, 600L); }

  
  @EventHandler
  public boolean onCommand(final CommandSender sender, Command cmd, String Label, final String[] args) {
    if (!cmd.getName().equalsIgnoreCase("sh")) {
      return false;
    }
    

    
    if (Main.JavaPlugin.getConfig().getBoolean("DisableFunctionButTeleport") && Variable.bungee) {
      if (sender instanceof Player) {
        final Player p = (Player)sender;
        
        if (args.length == 1 && (
          args[0].equalsIgnoreCase("home") || args[0].equalsIgnoreCase("h"))) {
          if (!MySQL.alreadyhastheplayerjoin(p.getName()) && 
            !MySQL.alreadyhastheplayerhome(p.getName())) {
            String temp = Variable.Lang_YML.getString("NoCreateOrJoin");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            return false;
          } 
          
          if (MySQL.alreadyhastheplayerjoin(p.getName()) && !MySQL.getJoinServer(p.getName()).equalsIgnoreCase(Main.JavaPlugin.getConfig().getString("Server"))) {
            try {
              Channel.waitDelayToSomeWhere(p, MySQL.getJoinServer(p.getName()), "sh h");
            } catch (IOException e) {
              
              e.printStackTrace();
            } 
            
            Channel.sendPlayerToServer(p, MySQL.getJoinServer(p.getName()));
            return false;
          }

          
          if (MySQL.alreadyhastheplayerhome(p.getName()) &&  !MySQL.getServer(p.getName()).equalsIgnoreCase(Main.JavaPlugin.getConfig().getString("Server"))) {
            try {
              Channel.waitDelayToSomeWhere(p, MySQL.getServer(p.getName()), "sh h");
            } catch (IOException e) {
              
              e.printStackTrace();
            } 
            
            Channel.sendPlayerToServer(p, MySQL.getServer(p.getName()));
            return false;
          } 
        } 


        
        
        
        
        if (args.length == 4 && 
      	      args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("setlevel")) {
      	      if (sender instanceof Player) {
      	        Player temp = (Player)sender;
      	        if (!temp.isOp()) {
      	          sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
      	          return false;
      	        }
      	      }
      	      
      	      if (!Util.CheckIsHome(args[2])) {
      	        String tip = Variable.Lang_YML.getString("NowIsNotHome");
      	        sender.sendMessage(tip);
      	        return false;
      	      } 

      	      
      	      if (Variable.bungee) {
      	        MySQL.setLevel(args[2], String.valueOf(Integer.valueOf(args[3])));
      	      } else {
      	        File f2 = new File(Variable.Tempf, String.valueOf(args[2]) + ".yml");
      	        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f2);
      	        yamlConfiguration.set("Level", Integer.valueOf(args[3]));
      	        try {
      	          yamlConfiguration.save(f2);
      	        } catch (IOException e) {
      	          
      	          e.printStackTrace();
      	        } 
      	      } 
      	      FirstBorderShaped.AddShapeBorder(Bukkit.getWorld(Variable.world_prefix +args[2]));
      	      sender.sendMessage(Variable.Lang_YML.getString("AdminSetLevelSuccess"));
      	      return false;
      	    } 
        
        
        
        
        
        if (args.length == 2 && 
          args[0].equalsIgnoreCase("create") && 
          Main.JavaPlugin.getConfig().getBoolean("BungeeCord")) {
          if (MySQL.alreadyhastheplayerjoin(p.getName())) {
            String temp_BungeeCord = Variable.Lang_YML.getString("HasBeenJoin");
            if (temp_BungeeCord.contains("<ServerName>")) {
              temp_BungeeCord = temp_BungeeCord.replace("<ServerName>", MySQL.getJoinServer(p.getName()));
            }
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp_BungeeCord);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            return false;
          } 
          if (MySQL.alreadyhastheplayerhome(p.getName())) {
            String temp_BungeeCord = Variable.Lang_YML.getString("HasBeenCreate");
            if (temp_BungeeCord.contains("<ServerName>")) {
              temp_BungeeCord = temp_BungeeCord.replace("<ServerName>", MySQL.getServer(p.getName()));
            }
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp_BungeeCord);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            return false;
          } 

          
          if (Main.JavaPlugin.getConfig().getBoolean("AutoReCreateInLowerLagHome") && !Variable.wait_to_command.containsKey(p.getName()) && Main.JavaPlugin.getConfig().getBoolean("BungeeCord")) {
            if (Main.JavaPlugin.getConfig().getString("DecideBy").equalsIgnoreCase("Player")) {
              if (!MySQL.getLowerstLagServer().equalsIgnoreCase(Main.JavaPlugin.getConfig().getString("Server"))) {
                try {
                  Channel.waitToCommand(p, MySQL.getLowerstLagServer(), "sh create " + args[1]);
                } catch (IOException e) {
                  
                  e.printStackTrace();
                } 
                p.sendMessage(Variable.Lang_YML.getString("StartLowestLagServer"));
                Channel.sendPlayerToServer(p, MySQL.getLowerstLagServer());
                return false;
              }
            
            }
            else if (!MySQL.getHighestTPSServer().equalsIgnoreCase(Main.JavaPlugin.getConfig().getString("Server"))) {
              
              double now = 0.0D;
              if (Bukkit.getVersion().contains("1.7.10")) {
                now = R1_7_10.getTps();
              } else {
                double se1 = Double.valueOf(PlaceholderAPI.setPlaceholders(null, "%server_tps_1%").replace("*", "")).doubleValue();
                double se2 = Double.valueOf(PlaceholderAPI.setPlaceholders(null, "%server_tps_5%").replace("*", "")).doubleValue();
                double se3 = Double.valueOf(PlaceholderAPI.setPlaceholders(null, "%server_tps_15%").replace("*", "")).doubleValue();
                now = (se1 + se2 + se3) / 3.0D;
              } 

              
              if (MySQL.getServerAmount(MySQL.getLowerstLagServer()) != now) {
                try {
                  Channel.waitToCommand(p, MySQL.getHighestTPSServer(), "sh create " + args[1]);
                } catch (IOException e) {
                  
                  e.printStackTrace();
                } 
                p.sendMessage(Variable.Lang_YML.getString("StartLowestLagServer"));
                Channel.sendPlayerToServer(p, MySQL.getHighestTPSServer());
                return false;
              } 
            } 
            
            return false;
          } 
        } 






        
        if (args.length == 1 && 
          args[0].equalsIgnoreCase("reload")) {
          
          if (sender instanceof Player) {
            Player temp = (Player)sender;
            if (!temp.isOp()) {
              sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
              return false;
            } 
          } 
          for (Player pe : Bukkit.getOnlinePlayers()) {
            if (pe.getOpenInventory() != null) {
              InventoryHolder inv = pe.getOpenInventory().getTopInventory().getHolder();
              if (inv instanceof CheckGui || inv instanceof CreateGui || inv instanceof DenyGui || 
                inv instanceof InviteGui || inv instanceof MainGui || inv instanceof ManageGui || 
                inv instanceof ManageGui2 || inv instanceof TrustGui || inv instanceof VisitGui) {
                sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                sender.sendMessage(Variable.Lang_YML.getString("CloseGuiWhenPluginReload"));
                sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
                pe.closeInventory();
              } 
            } 
          } 
          
          for (World temp : Bukkit.getWorlds()) {
            if (Variable.hololist.containsKey(temp.getName())) {
              for (Hologram temp2 : Variable.hololist.get(temp.getName())) {
                temp2.delete();
              }
            }
          } 
          
          Main.init();
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(Variable.Lang_YML.getString("ReloadSuccess"));
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 





        
        if (args.length == 2 && (
          args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("visit") || args[0].equalsIgnoreCase("v"))) {
          if (!Main.JavaPlugin.getConfig().getBoolean("Permission.Visit") && 
            !p.hasPermission("SelfHome.Visit")) {
            String tip = Variable.Lang_YML.getString("NoPermissionCheck");
            if (tip.contains("<Permission>")) {
              tip = tip.replace("<Permission>", "SelfHome.Visit");
            }
            p.sendMessage(tip);
            return false;
          } 



          
          if (Variable.bungee) {
            if (Util.CheckIsHome(args[1])) {
              if (!MySQL.getServer(args[1]).equalsIgnoreCase(Main.JavaPlugin.getConfig().getString("Server"))) {
                try {
                  Channel.waitDelayToSomeWhere(p, MySQL.getServer(args[1]), "sh visit " + args[1]);
                } catch (IOException e) {
                  
                  e.printStackTrace();
                } 
                
                Channel.sendPlayerToServer(p, MySQL.getServer(args[1]));
                return false;
              } 
            } else {
              String temp = Variable.Lang_YML.getString("TpNotExist");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            } 
          }
          return false;
        } 





        
        if (args.length == 1 && (
          args[0].equalsIgnoreCase("Open") || args[0].equalsIgnoreCase("Menu"))) {
          MainGui gui = new MainGui(p);
          p.openInventory(gui.getInventory());
          return false;
        } 

        
        if (args.length == 2 && 
          args[0].equalsIgnoreCase("Open") && args[1].equalsIgnoreCase("Main")) {
          MainGui gui = new MainGui(p);
          p.openInventory(gui.getInventory());
          return false;
        } 

        
        if (args.length == 2 && 
          args[0].equalsIgnoreCase("Open") && args[1].equalsIgnoreCase("Check")) {
          CheckGui gui = new CheckGui(p);
          p.openInventory(gui.getInventory());
          return false;
        } 
        
        if (args.length == 2 && 
          args[0].equalsIgnoreCase("Open") && args[1].equalsIgnoreCase("Create")) {
          CreateGui gui = new CreateGui(p);
          p.openInventory(gui.getInventory());
          return false;
        } 
        
        if (args.length == 2 && 
          args[0].equalsIgnoreCase("Open") && args[1].equalsIgnoreCase("Manage")) {
          ManageGui gui = new ManageGui(p);
          p.openInventory(gui.getInventory());
          return false;
        } 
        
        if (args.length == 2 && 
          args[0].equalsIgnoreCase("Open") && args[1].equalsIgnoreCase("Manage2")) {
          ManageGui2 gui = new ManageGui2(p);
          p.openInventory(gui.getInventory());
          return false;
        } 
        
        if (args.length == 2 && 
          args[0].equalsIgnoreCase("Open") && args[1].equalsIgnoreCase("Visit")) {
          VisitGui gui = new VisitGui();
          p.openInventory(gui.getInventory());
          return false;
        } 
        
        if (args.length == 2 && 
          args[0].equalsIgnoreCase("Open") && args[1].equalsIgnoreCase("Invite")) {
          InviteGui gui = new InviteGui();
          p.openInventory(gui.getInventory());
          return false;
        } 
        
        if (args.length == 2 && 
          args[0].equalsIgnoreCase("Open") && args[1].equalsIgnoreCase("Trust")) {
          TrustGui gui = new TrustGui();
          p.openInventory(gui.getInventory());
          return false;
        } 
        
        if (args.length == 2 && 
          args[0].equalsIgnoreCase("Open") && args[1].equalsIgnoreCase("Deny")) {
          DenyGui gui = new DenyGui();
          p.openInventory(gui.getInventory());
          return false;
        } 

        
        if (args.length == 1 && 
          args[0].equalsIgnoreCase("close")) {
          if (p.getOpenInventory() != null) {
            p.closeInventory();
          }
          return false;
        } 
      } 





















      
      sender.sendMessage(Variable.Lang_YML.getString("DisableFunctionTip"));
      return false;
    } 
    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("reload")) {
      
      if (sender instanceof Player) {
        Player temp = (Player)sender;
        if (!temp.isOp()) {
          sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
          return false;
        } 
      } 
      for (Player p : Bukkit.getOnlinePlayers()) {
        if (p.getOpenInventory() != null) {
          InventoryHolder inv = p.getOpenInventory().getTopInventory().getHolder();
          if (inv instanceof CheckGui || inv instanceof CreateGui || inv instanceof DenyGui || 
            inv instanceof InviteGui || inv instanceof MainGui || inv instanceof ManageGui || 
            inv instanceof ManageGui2 || inv instanceof TrustGui || inv instanceof VisitGui) {
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(Variable.Lang_YML.getString("CloseGuiWhenPluginReload"));
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            p.closeInventory();
          } 
        } 
      } 
      
      for (World temp : Bukkit.getWorlds()) {
        if (Variable.hololist.containsKey(temp.getName())) {
          for (Hologram temp2 : Variable.hololist.get(temp.getName())) {
            temp2.delete();
          }
        }
      } 
      
      Main.init();
      sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
      sender.sendMessage(Variable.Lang_YML.getString("ReloadSuccess"));
      sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
      return false;
    } 

    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("setspawn")) {
      if (sender instanceof Player) {
        Player temp = (Player)sender;
        if (!temp.isOp()) {
          sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
          return false;
        } 
      } else {
        sender.sendMessage(Variable.Lang_YML.getString("CommandSenderIsNotAllowToUseTheCommand"));
        return false;
      } 
      final Player p = (Player)sender;
      World world = p.getWorld();
      
      if (Bukkit.getVersion().contains("1.7.10") || Bukkit.getVersion().contains("1.7.2")) {
        world.setSpawnLocation((int)p.getLocation().getX(), (int)p.getLocation().getY(), (int)p.getLocation().getZ());
      } else {
        world.setSpawnLocation(p.getLocation());
      } 
      
      if(Variable.hook_multiverseCore == true) {
		   MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
	      	MVWorldManager mv_m = mvcore.getMVWorldManager();
      	MultiverseWorld mv = mv_m.getMVWorld(p.getLocation().getWorld().getName());
      	mv.setSpawnLocation(p.getLocation());
      }
      
      
      
      sender.sendMessage(Variable.Lang_YML.getString("AdminSetSpawnSuccess"));
      return false;
    } 



    
    if (args.length == 3 && 
      args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("clearout")) {
      if (sender instanceof Player) {
        Player temp = (Player)sender;
        if (!temp.isOp()) {
          sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
          return false;
        } 
      } 

      
      if (!Variable.wait_to_confirm_command.contains(sender.getName())) {
        Variable.wait_to_confirm_command.add(sender.getName());
        String message2 = Variable.Lang_YML.getString("OutdateWorldConfirm");
        if (message2.contains("<Day>")) {
          message2 = message2.replace("<Day>", args[2]);
        }
        sender.sendMessage(message2);
        
        (new BukkitRunnable()
          {
            public void run()
            {
              if (Variable.wait_to_confirm_command.contains(sender.getName())) {
                Variable.wait_to_confirm_command.remove(sender.getName());
              
              }
            }
          }).runTaskLater((Plugin)Main.JavaPlugin, 100L);

        
        return false;
      } 
      
      Variable.wait_to_confirm_command.remove(sender.getName());
      
      long now = System.currentTimeMillis();
      int amount = 0;
      List<String> who_has_been_delete = new ArrayList<>();
      if (Variable.bungee) {
        for (String worldname : MySQL.getAllWorlds()) {
          long before_time = Long.valueOf(MySQL.getVisitTime(worldname)).longValue();
          long distance = (now - before_time) / 86400000L;
          if (distance > Long.valueOf(args[2]).longValue()) {
            HomeAPI.delHome(worldname);
            who_has_been_delete.add(worldname);
            amount++;
          } 
        } 
      } else {
        File folder = new File(Variable.Tempf); int b; int j; File[] arrayOfFile;
        for (j = (arrayOfFile = folder.listFiles()).length, b = 0; b < j; ) { File temp = arrayOfFile[b];
          long lastModified = temp.lastModified();
          long distance = (now - lastModified) / 86400000L;
          if (distance > Long.valueOf(args[2]).longValue()) {
            String want_to = temp.getPath().replace(Variable.Tempf, "").replace(".yml", "").replace(Variable.file_loc_prefix, "");
            HomeAPI.delHome(want_to);
            who_has_been_delete.add(want_to);
            amount++;
          }  b++; }
      
      } 
      String message = Variable.Lang_YML.getString("OutdatedWorldHasBeenDeleted");
      if (message.contains("<Amount>")) {
        message = message.replace("<Amount>", String.valueOf(amount));
      }
      if (message.contains("<List>")) {
        message = message.replace("<List>", who_has_been_delete.toString());
      }
      
      sender.sendMessage(message);
      return false;
    } 



    
    if (args.length == 3 && 
      args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("load")) {
      Player p2;
					if (sender instanceof Player) {
        p2 = (Player)sender;
        if (!p2.isOp()) {
          sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
          return false;
        } 
      } 


      File newf;
      if (Variable.world_prefix.equalsIgnoreCase("")) {
        if (Bukkit.getVersion().toString().toUpperCase().contains("ARCLIGHT")) {
          newf = new File(Variable.single_server_gen + Variable.world_prefix + Variable.file_loc_prefix + args[2]);
        } else {
          newf = new File(Variable.single_server_gen + "world" + Variable.file_loc_prefix + args[2]);
        } 
      } else {
        newf = new File(Variable.single_server_gen + Variable.world_prefix + Variable.file_loc_prefix + args[2]);
      } 
      
      if (!newf.exists() && !newf.isDirectory() && !args[2].equalsIgnoreCase("world")) {
        sender.sendMessage(Variable.Lang_YML.getString("WorldIsNotExist"));
        return false;
      } 
      
      WorldCreator creator = null;
      creator = new WorldCreator(Variable.world_prefix + args[2]);
      Variable.create_list_home.add(Variable.world_prefix + args[2]);
      Bukkit.createWorld(creator);
      sender.sendMessage("Loaded the World: " + args[2]);
      World world = Bukkit.getWorld(args[2]);
      if (sender instanceof Player) {
         Player p = (Player)sender;
        p.teleport(world.getSpawnLocation());
        p.sendMessage(Variable.Lang_YML.getString("WorldTeleport"));
      } 
      return false;
    } 


    
    if (args.length == 3 && 
      args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("tp")) {
      Player p3; if (sender instanceof Player) {
        p3 = (Player)sender;
        if (!p3.isOp() && !p3.hasPermission("SelfHome.Admin.TP." + args[2]) && !p3.hasPermission("SelfHome.Admin.TP.*")) {
          sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
          return false;
        } 
      } 

      File newf;
      if (Variable.world_prefix.equalsIgnoreCase("")) {
        if (Bukkit.getVersion().toString().toUpperCase().contains("ARCLIGHT")) {
          newf = new File(Variable.single_server_gen + Variable.world_prefix + Variable.file_loc_prefix + args[2]);
        } else {
          newf = new File(Variable.single_server_gen + "world" + Variable.file_loc_prefix + args[2]);
        } 
      } else {
        newf = new File(Variable.single_server_gen + Variable.world_prefix + Variable.file_loc_prefix + args[2]);
      } 
      
      if (!newf.exists() && !newf.isDirectory() && !args[2].equalsIgnoreCase("world")) {
        sender.sendMessage(Variable.Lang_YML.getString("WorldIsNotExist"));
        return false;
      } 
      
      WorldCreator creator = null;
      Variable.create_list_home.add(Variable.world_prefix + args[2]);
      creator = new WorldCreator(Variable.world_prefix + args[2]);
      Bukkit.createWorld(creator);
      
      if (sender instanceof Player) {
        final Player p = (Player)sender;
        World world = Bukkit.getWorld(args[2]);
        p.teleport(world.getSpawnLocation());
        p.sendMessage(Variable.Lang_YML.getString("WorldTeleport"));
      } 
      return false;
    } 



    if (args.length == 4 && 
    	      args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("setlevel")) {
    	      if (sender instanceof Player) {
    	        Player temp = (Player)sender;
    	        if (!temp.isOp()) {
    	          sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
    	          return false;
    	        } 
    	      } 
    	      
    	      if (!Util.CheckIsHome(args[2])) {
    	        String tip = Variable.Lang_YML.getString("NowIsNotHome");
    	        sender.sendMessage(tip);
    	        return false;
    	      } 

    	      
    	      if (Variable.bungee) {
    	        MySQL.setLevel(args[2], String.valueOf(Integer.valueOf(args[3])));
    	      } else {
    	        File f2 = new File(Variable.Tempf, String.valueOf(args[2]) + ".yml");
    	        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f2);
    	        yamlConfiguration.set("Level", Integer.valueOf(args[3]));
    	        try {
    	          yamlConfiguration.save(f2);
    	        } catch (IOException e) {
    	          
    	          e.printStackTrace();
    	        } 
    	      } 
    	      FirstBorderShaped.AddShapeBorder(Bukkit.getWorld(Variable.world_prefix +args[2]));
    	      sender.sendMessage(Variable.Lang_YML.getString("AdminSetLevelSuccess"));
    	      return false;
    	    } 
    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("export")) {
      
      if (sender instanceof Player) {
        Player temp = (Player)sender;
        if (!temp.isOp()) {
          sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
          return false;
        } 
      } 
      
      if (!Variable.bungee) {
        sender.sendMessage(Variable.Lang_YML.getString("ExportOrImportButBungeeCordHasBeenDisabled"));
        return false;
      } 
      
      MySQL.data_export(sender);
      return false;
    } 

    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("import")) {
      if (sender instanceof Player) {
        Player temp = (Player)sender;
        if (!temp.isOp()) {
          sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
          return false;
        } 
      } 
      
      if (!Variable.bungee) {
        sender.sendMessage(Variable.Lang_YML.getString("ExportOrImportButBungeeCordHasBeenDisabled"));
        return false;
      } 

      
      MySQL.data_import(sender);
      return false;
    } 


    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("pwp")) {
      
      if (sender instanceof Player) {
        Player temp = (Player)sender;
        if (!temp.isOp()) {
          sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
          return false;
        } 
      } 

      
      String gen_mdk = "plugins\\PlayerWorldsPro";
      YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(new File(gen_mdk, "config.yml"));
      String prefix = yamlConfiguration1.getString("Basic.World-Prefix");
      
      HashMap<String, String> map = new HashMap<>();
      YamlConfiguration yamlConfiguration2 = YamlConfiguration.loadConfiguration(new File(gen_mdk, "players.yml"));
      for (String key : yamlConfiguration2.getKeys(false)) {
        map.put(key, yamlConfiguration2.getString(String.valueOf(key) + ".Name"));
      }
      
      YamlConfiguration yamlConfiguration3 = YamlConfiguration.loadConfiguration(new File(gen_mdk, "data.yml"));
      for (String key : yamlConfiguration3.getKeys(true)) {
        File newf; File oldf; if ((key.split("\\.")).length != 2) {
          continue;
        }
        String uuid = key.split("\\.")[1];
        boolean lockweather = !yamlConfiguration3.getBoolean("Worlds." + uuid + ".1.WeatherCycle");
        boolean pvp = !yamlConfiguration3.getBoolean("Worlds." + uuid + ".1.PvP");
        boolean pickup = !yamlConfiguration3.getBoolean("Worlds." + uuid + ".1.Item-Pickup");
        boolean drop = !yamlConfiguration3.getBoolean("Worlds." + uuid + ".1.Drop-Item");
        boolean publicAccess = false;
        boolean has_set_spawn = false;
        double X = 0.0D;
        double Y = 0.0D;
        double Z = 0.0D;
        boolean has_set_Members = false;
        List<String> list = new ArrayList<>();
        String publicswitch = yamlConfiguration3.getString("Worlds." + uuid + ".1.Access");
        
        if (publicswitch.equalsIgnoreCase("Public")) {
          publicAccess = true;
        }
        
        if (yamlConfiguration3.getString("Worlds." + uuid + ".1.Spawn") != null) {
          has_set_spawn = true;
          String[] temp = yamlConfiguration3.getString("Worlds." + uuid + ".1.Spawn").split(";");
          X = Double.valueOf(temp[0]).doubleValue();
          Y = Double.valueOf(temp[1]).doubleValue();
          Z = Double.valueOf(temp[2]).doubleValue();
        } 
        
        List<String> Trustlist = new ArrayList<>();
        if (yamlConfiguration3.getStringList("Worlds." + uuid + ".1.Members") != null) {
          list = yamlConfiguration3.getStringList("Worlds." + uuid + ".1.Members");
          for (int i = 0; i < list.size(); i++) {
            if (map.get(list.get(i)) != null)
            {
              
              if (!((String)map.get(list.get(i))).contains("\\-")) {
                Trustlist.add(map.get(list.get(i)));
                has_set_Members = true;
              } 
            }
          } 
        } 
        String name = map.get(uuid);
        File f2 = new File(Variable.Tempf, name + ".yml");

        
        if (f2.exists()) {
          return false;
        }
        
        try {
          f2.createNewFile();
        } catch (IOException e1) {
          
          e1.printStackTrace();
        } 
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f2);
        yamlConfiguration.createSection("Members");
        yamlConfiguration.createSection("OP");
        yamlConfiguration.createSection("Denys");
        yamlConfiguration.createSection("Public");
        yamlConfiguration.createSection("Level");
        yamlConfiguration.createSection("pvp");
        yamlConfiguration.createSection("pickup");
        yamlConfiguration.createSection("drop");
        yamlConfiguration.createSection("Server");
        yamlConfiguration.createSection("locktime");
        yamlConfiguration.createSection("lockweather");
        yamlConfiguration.createSection("time");
        yamlConfiguration.createSection("icon");
        yamlConfiguration.createSection("advertisement");
        yamlConfiguration.createSection("limitblock");
        yamlConfiguration.set("Public", Boolean.valueOf(publicAccess));
        yamlConfiguration.set("pickup", Boolean.valueOf(pickup));
        yamlConfiguration.set("drop", Boolean.valueOf(drop));
        yamlConfiguration.set("pvp", Boolean.valueOf(pvp));
        yamlConfiguration.set("locktime", Boolean.valueOf(false));
        yamlConfiguration.set("time", Integer.valueOf(0));
        yamlConfiguration.set("lockweather", Boolean.valueOf(lockweather));
        int set_level = 1;
        yamlConfiguration.set("Level", Integer.valueOf(set_level));
        yamlConfiguration.set("Server", Main.JavaPlugin.getConfig().getString("Server"));
        
        yamlConfiguration.createSection("flowers");
        yamlConfiguration.createSection("popularity");
        yamlConfiguration.createSection("gifts");
        
        yamlConfiguration.set("flowers", Integer.valueOf(0));
        yamlConfiguration.set("popularity", Integer.valueOf(0));
        yamlConfiguration.set("gifts", new ArrayList());
        yamlConfiguration.set("advertisement", new ArrayList());
        yamlConfiguration.set("limitblock", new ArrayList());
        yamlConfiguration.set("icon", "");
        try {
          yamlConfiguration.save(f2);
        } catch (IOException e) {
          e.printStackTrace();
        } 
        
        if (has_set_Members && 
          Trustlist != null) {
          yamlConfiguration.set("Members", Trustlist);
        }


        
        yamlConfiguration.createSection("X");
        yamlConfiguration.createSection("Y");
        yamlConfiguration.createSection("Z");
        if (has_set_spawn) {
          yamlConfiguration.set("X", Double.valueOf(X));
          yamlConfiguration.set("Y", Double.valueOf(Y));
          yamlConfiguration.set("Z", Double.valueOf(Z));
        } else {
          yamlConfiguration.set("X", Double.valueOf(0.0D));
          yamlConfiguration.set("Y", Double.valueOf(0.0D));
          yamlConfiguration.set("Z", Double.valueOf(0.0D));
        } 
        
        try {
          yamlConfiguration.save(f2);
        } catch (IOException e) {
          e.printStackTrace();
        } 
        
        sender.sendMessage("成功导出" + name + ".yml到本插件的数据文件夹");
        System.out.println("成功导出" + name + ".yml到本插件的数据文件夹");

    
        if (Variable.world_prefix.equalsIgnoreCase("")) {
          if (Bukkit.getVersion().toString().toUpperCase().contains("ARCLIGHT")) {
            oldf = new File(Variable.single_server_gen + "PlayerWorldsPro" + Variable.file_loc_prefix);
          } else {
            oldf = new File(Variable.single_server_gen + "world" + Variable.file_loc_prefix);
          } 
        } else {
          oldf = new File(Variable.single_server_gen + "PlayerWorldsPro" + Variable.file_loc_prefix);
        } 


        
        if (Variable.world_prefix.equalsIgnoreCase("")) {
          if (Bukkit.getVersion().toString().toUpperCase().contains("ARCLIGHT")) {
            newf = new File(Variable.single_server_gen + Variable.world_prefix);
          } else {
            newf = new File(Variable.single_server_gen + "world" + Variable.file_loc_prefix);
          } 
        } else {
          newf = new File(Variable.single_server_gen + Variable.world_prefix);
        } 



        	
        File oldFile = new File(String.valueOf(oldf.getPath().toString()) + Variable.file_loc_prefix + prefix + uuid);
        System.out.println(oldFile.getPath());
        File newFile = new File(String.valueOf(newf.getPath().toString()) + Variable.file_loc_prefix + name);
        if (oldFile.renameTo(newFile)) {
          sender.sendMessage(name + "玩家的存档文件重命名成功");
          System.out.println(name + "玩家的存档文件重命名成功"); continue;
        } 
        sender.sendMessage(name + "玩家重命名失败！");
        System.out.println(name + "玩家重命名失败！");
      } 





      
      return false;
    } 

















































































































































































































    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("ForceDelete")) {
      Object f; if (sender instanceof Player) {
        Player temp = (Player)sender;
        if (!temp.isOp()) {
          sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
          return false;
        } 
      } 
      World world = Bukkit.getWorld(Variable.world_prefix + args[1]);
      
      if (world != null) {
        for (Player p6 : Bukkit.getWorld(Variable.world_prefix + args[1]).getPlayers()) {
          p6.teleport(Bukkit.getWorld("world").getSpawnLocation());
          p6.sendMessage(Variable.Lang_YML.getString("WorldHasBeenForceDelete"));
        } 
        Bukkit.unloadWorld(Variable.world_prefix + args[1], true);
        sender.sendMessage(Variable.Lang_YML.getString("WorldHasBeenForceDeleteSuccess"));
      }
      
      if(Variable.hook_multiverseCore == true) {
		   MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
	      	MVWorldManager mv_m = mvcore.getMVWorldManager();
	      	mv_m.removeWorldFromConfig(Variable.world_prefix + args[1]);
       }
      
      YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(Variable.f_log);
      yamlConfiguration.set("NowID", Integer.valueOf(yamlConfiguration.getInt("NowID") - 1));
      try {
        yamlConfiguration.save(Variable.f_log);
      } catch (IOException e2) {
        e2.printStackTrace();
      } 

      
      if (Variable.world_prefix.equalsIgnoreCase("")) {
        
        if (Bukkit.getVersion().toString().toUpperCase().contains("ARCLIGHT") || Bukkit.getVersion().toString().contains("1.20.1")) {
          
          f = new File(Variable.single_server_gen + Variable.world_prefix + args[1]);
        } else {
          f = new File(Variable.single_server_gen + "world" + Variable.file_loc_prefix + args[1]);
        } 
      } else {
        
        f = new File(Variable.single_server_gen + Variable.world_prefix + args[1]);
      } 
      
      Util.deleteFile((File)f);
      sender.sendMessage(Variable.Lang_YML.getString("WorldHasBeenDeleted"));
      
      if (Variable.bungee) {
        MySQL.removePlayer(args[1]);
        sender.sendMessage(Variable.Lang_YML.getString("WorldConfigHasBeenDeleted"));
      } else {
        File f2 = new File(Variable.Tempf, String.valueOf(args[1]) + ".yml");
        if (f2.exists()) {
          sender.sendMessage(Variable.Lang_YML.getString("WorldConfigHasBeenDeleted"));
          f2.delete();
        } 
      } 

      
      return false;
    } 











    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("UnLoad")) {
      
      if (sender instanceof Player) {
        Player temp = (Player)sender;
        if (!temp.isOp()) {
          sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
          return false;
        } 
      } 
      
      for (Player p6 : Bukkit.getWorld(args[1]).getPlayers()) {
        p6.teleport(Bukkit.getWorld("world").getSpawnLocation());
      }
      Bukkit.unloadWorld(args[1], true);
      sender.sendMessage(Variable.Lang_YML.getString("ForceUnLoadWorld"));
      return false;
    } 







    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("rank")) {



      
      if (sender instanceof Player) {
        Player temp = (Player)sender;
        if (!Main.JavaPlugin.getConfig().getBoolean("Permission.CommandUser") && 
          !temp.hasPermission("SelfHome.Rank") && !temp.hasPermission("SelfHome.command.user")) {
          String tip = Variable.Lang_YML.getString("NoPermissionCheck");
          if (tip.contains("<Permission>")) {
            tip = tip.replace("<Permission>", "SelfHome.Rank");
          }
          temp.sendMessage(tip);
          return false;
        } 
      } 



      
      (new BukkitRunnable()
        {
          public void run()
          {
            int YS = Integer.valueOf(args[1]).intValue();
            
            if (Variable.world_StaticsTick.size() == 0) {
              init.refreshWorldStatics(false);
            }
            
            for (int c = 0; c < Main.JavaPlugin.getConfig().getStringList("StatisticsTop").size(); c++) {
              String a = Main.JavaPlugin.getConfig().getStringList("StatisticsTop").get(c);
              sender.sendMessage(a);
            } 
            
            for (int i = 10 * YS - 10; i < YS * 10 && i < Variable.world_StaticsTick.size(); i++) {
              StaticsTick s = Variable.world_StaticsTick.get(i);
              String temp = Main.JavaPlugin.getConfig().getString("ShowFormat");
              if (temp.contains("<index>")) {
                temp = temp.replace("<index>", String.valueOf(i + 1));
              }
              if (temp.contains("<world>")) {
                temp = temp.replace("<world>", s.name);
              }
              if (temp.contains("<tile>")) {
                temp = temp.replace("<tile>", String.valueOf(s.tile));
              }
              if (temp.contains("<chunk>")) {
                temp = temp.replace("<chunk>", String.valueOf(s.chunk));
              }
              if (temp.contains("<entity>")) {
                temp = temp.replace("<entity>", String.valueOf(s.entity));
              }
              if (temp.contains("<drop>")) {
                temp = temp.replace("<drop>", String.valueOf(s.drop));
              }
              if (temp.contains("<tps>")) {
                temp = temp.replace("<tps>", String.format(Main.JavaPlugin.getConfig().getString("FormatInfo"), new Object[] { Double.valueOf(s.tps) }));
              }
              sender.sendMessage(temp);
            } 

            
            for (int c = 0; c < Main.JavaPlugin.getConfig().getStringList("StatisticsEnd").size(); c++) {
              String a = Main.JavaPlugin.getConfig().getStringList("StatisticsEnd").get(c);
              sender.sendMessage(a);
            }
          
          }
        }).runTaskAsynchronously((Plugin)Main.JavaPlugin);
      return false;
    } 




    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("dimension")) {
      
      if (sender instanceof Player) {
        Player temp = (Player)sender;
        if (!temp.isOp()) {
          sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
          return false;
        } 
      } 
      
      if (Bukkit.getBukkitVersion().toString().toUpperCase().contains("1.12.2")) {
        for (World temp : Bukkit.getWorlds()) {
          sender.sendMessage("§dWorld:§b" + temp.getName() + "§d,Dimension:§b" + R1_12_2.getID(temp));
        }
      } else if (Bukkit.getBukkitVersion().toString().toUpperCase().contains("1.7.10")) {
        for (World temp : Bukkit.getWorlds()) {
          sender.sendMessage("§dWorld:§b" + temp.getName() + "§d,Dimension:§b" + R1_7_10.getID(temp));
        }
      } else {
        sender.sendMessage(Variable.Lang_YML.getString("DimensionNotAllow"));
      } 
      return false;
    } 






    
    if (args.length == 4) {
      if (sender instanceof Player) {
        Player temp = (Player)sender;
        if (!temp.isOp()) {
          sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
          return false;
        } 
      } 
      
      if (args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("create")) {
        
        if (Main.JavaPlugin.getConfig().getBoolean("BungeeCord")) {
          if (MySQL.alreadyhastheplayerjoin(args[2])) {
            String temp_BungeeCord = Variable.Lang_YML.getString("AdminCreateHasJoinButNotServer");
            if (temp_BungeeCord.contains("<ServerName>")) {
              temp_BungeeCord = temp_BungeeCord.replace("<ServerName>", MySQL.getJoinServer(args[2]));
            }
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp_BungeeCord);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            return false;
          } 
          if (MySQL.alreadyhastheplayerhome(args[2])) {
            String temp_BungeeCord = Variable.Lang_YML.getString("AdminCreateHasCreateButNotServer");
            if (temp_BungeeCord.contains("<ServerName>")) {
              temp_BungeeCord = temp_BungeeCord.replace("<ServerName>", MySQL.getServer(args[2]));
            }
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp_BungeeCord);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            return false;
          } 
        } else {
          
          File f2 = new File(Variable.Tempf, String.valueOf(args[2].replace(Variable.world_prefix, "")) + ".yml");
          
          if (f2.exists()) {
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(Variable.Lang_YML.getString("HasBeenCreate"));
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            return false;
          } 
          
          boolean has_been_join = false;
          File folder = new File(Variable.Tempf); int b; int j; File[] arrayOfFile;
          for (j = (arrayOfFile = folder.listFiles()).length, b = 0; b < j; ) { File temp = arrayOfFile[b];
            String want_to = temp.getPath().replace(Variable.Tempf, "").replace(".yml", "")
              .replace(Variable.file_loc_prefix, "");
            YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(temp);
            for (int i = 0; i < yamlConfiguration1.getStringList("OP").size(); i++) {
              String temp_str = yamlConfiguration1.getStringList("OP").get(i);
              if (temp_str.equalsIgnoreCase(args[2])) {
                has_been_join = true;
                break;
              } 
            } 
            b++; }
          
          if (has_been_join) {
            String temp = Variable.Lang_YML.getString("HasAlreadyJoinOthers");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            return false;
          } 
        } 

        
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(Variable.f_log);
        if (!yamlConfiguration.contains("NowID")) {
          yamlConfiguration.set("NowID", Integer.valueOf(0));
        }
        if (!yamlConfiguration.contains("MaxID")) {
          yamlConfiguration.set("MaxID", Integer.valueOf(1000));
        }
        try {
          yamlConfiguration.save(Variable.f_log);
        } catch (IOException e2) {
          
          e2.printStackTrace();
        } 
        
        int nowID = yamlConfiguration.getInt("NowID");
        int MaxID = yamlConfiguration.getInt("MaxID");
        
        if (nowID >= MaxID) {
          
          String temp = Variable.Lang_YML.getString("ReachMaxCreate");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
        
        String v = args[3];

        

        
        
        
        if (v.equalsIgnoreCase("1")) {
          
          WorldCreator creator = null;
          creator = new WorldCreator(Variable.world_prefix + args[2]);
          
  
          
 
          if (Main.JavaPlugin.getConfig().getBoolean("generateStructures")) {
        	  creator = creator.generateStructures(true);
          } else {
        	  creator = creator.generateStructures(false);
          }
          
          
          creator.type(WorldType.NORMAL);
          creator =  creator.type(WorldType.NORMAL);
          Variable.create_list_home.add(Variable.world_prefix + args[2]);
          Bukkit.createWorld(creator);
          
          
        }
        else if (v.equalsIgnoreCase("2")) {
          WorldCreator creator = null;
          creator = new WorldCreator(Variable.world_prefix + args[2]);
          Main.JavaPlugin.getDefaultWorldGenerator(Variable.world_prefix + args[2], "");
          if (Main.JavaPlugin.getConfig().getBoolean("generateStructures")) {
        	  creator = creator.generateStructures(true);
          } else {
        	  creator = creator.generateStructures(false);
          } 
          
          creator = creator.type(WorldType.FLAT);
          Variable.create_list_home.add(Variable.world_prefix + args[2]);
          Bukkit.createWorld(creator);
          
        }
        else {
          String oldDir = String.valueOf(Variable.worldFinal) + v;
          String newDir = "";
          if (Variable.world_prefix.equalsIgnoreCase("")) {
            if (Bukkit.getVersion().toString().toUpperCase().contains("ARCLIGHT") || Bukkit.getVersion().toString().contains("1.20.1")) {
              newDir = Variable.single_server_gen + Variable.world_prefix + args[2];
            } else {
              newDir = Variable.single_server_gen + "world" + Variable.file_loc_prefix + args[2];
            } 
          } else {
            newDir = Variable.single_server_gen + Variable.world_prefix + args[2];
          } 
          
          File exist_file = new File(oldDir);
          if (!exist_file.exists()) {
            String temp = Variable.Lang_YML.getString("WorldFileNotExist");
            if (temp.contains("<name>")) {
              temp = temp.replace("<name>", v);
            }
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            return false;
          } 
          Util.copyDir(oldDir, newDir);

          
          WorldCreator creator = null;
          creator = new WorldCreator(Variable.world_prefix + args[2]);
          
          if (Main.JavaPlugin.getConfig().getBoolean("generateStructures")) {
            creator.generateStructures(true);
          } else {
            creator.generateStructures(false);
          } 
          Variable.create_list_home.add(Variable.world_prefix + args[2]);
          Bukkit.createWorld(creator);
        }
        
        
        
	      if(Variable.hook_multiverseCore) {
			  String seed =  Long.toString(Main.JavaPlugin.getConfig().getLong("Seed"));
			  if(seed.equalsIgnoreCase("0")) {
				  seed = "";
			  }
			     MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		      	MVWorldManager mv_m = mvcore.getMVWorldManager();
		      	
		      	
		      	if(mv_m.isMVWorld(Variable.world_prefix+args[2])) {
		      		mv_m.removeWorldFromConfig(Variable.world_prefix+args[2]);
		      	}
		      	
		      	 if (v.equalsIgnoreCase("1")) {
		      		mv_m.addWorld(Variable.world_prefix +  args[2], Environment.NORMAL, seed, WorldType.NORMAL, Main.JavaPlugin.getConfig().getBoolean("generateStructures"), "");
		      	 }else if(v.equalsIgnoreCase("2")) {
		      		mv_m.addWorld(Variable.world_prefix +  args[2], Environment.NORMAL, seed, WorldType.FLAT, Main.JavaPlugin.getConfig().getBoolean("generateStructures"), "");
		      	 }else {
		      		mv_m.addWorld(Variable.world_prefix +  args[2], Environment.NORMAL, seed, WorldType.NORMAL, Main.JavaPlugin.getConfig().getBoolean("generateStructures"), "");
		      	 }

				  if (Main.JavaPlugin.getConfig().getBoolean("EnableChatPrefix")) {
					  MultiverseWorld mv = mv_m.getMVWorld(Variable.world_prefix + args[2]);
				      	World world = Bukkit.getWorld(Variable.world_prefix + args[2]);
				        String temp = Variable.Lang_YML.getString("PlaceHolders.WorldName");
				        if (temp.contains("<PlayerName>")) {
				          temp = temp.replace("<PlayerName>", 
				              world.getName().replace(Variable.world_prefix, ""));
				        }
				        if (temp.contains("<WorldName>")) {
				          temp = temp.replace("<WorldName>", 
				        		  world.getName().replace(Variable.world_prefix, ""));
				        }
				      	mv.setAlias(temp);
				  }
		      	
		      	MultiverseWorld mv = mv_m.getMVWorld(Variable.world_prefix + args[2]);
		      	mv.setAutoLoad(false);
	      }

        
        
        
        World world = Bukkit.getWorld(Variable.world_prefix + args[2]);
        
        if (Variable.bungee) {
          MySQL.insertvalue(args[2], "", "", "", 
              String.valueOf(Main.JavaPlugin.getConfig().getBoolean("NormalPublic")), "1", 
              String.valueOf(Main.JavaPlugin.getConfig().getBoolean("NormalPVP")), 
              String.valueOf(Main.JavaPlugin.getConfig().getBoolean("NormalPickup")), 
              String.valueOf(Main.JavaPlugin.getConfig().getBoolean("NormalDrop")), 
              Main.JavaPlugin.getConfig().getString("Server"), "false", "false", "0", 
              String.valueOf(world.getSpawnLocation().getX()), 
              String.valueOf(world.getSpawnLocation().getY()), 
              String.valueOf(world.getSpawnLocation().getZ()), "0", "0", "", "", "", "","");
        } else {
          File f2 = new File(Variable.Tempf, String.valueOf(args[2]) + ".yml");
          if (!f2.exists()) {
            try {
              f2.createNewFile();
            } catch (IOException e1) {
              
              e1.printStackTrace();
            } 
            YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(f2);
            
            yamlConfiguration1.createSection("Members");
            yamlConfiguration1.createSection("OP");
            yamlConfiguration1.createSection("Denys");
            yamlConfiguration1.createSection("Public");
            yamlConfiguration1.createSection("Level");
            yamlConfiguration1.createSection("pvp");
            yamlConfiguration1.createSection("pickup");
            yamlConfiguration1.createSection("drop");
            yamlConfiguration1.createSection("Server");
            
            yamlConfiguration1.createSection("locktime");
            yamlConfiguration1.createSection("lockweather");
            yamlConfiguration1.createSection("time");


            
            if (!yamlConfiguration.contains("NowID")) {
              yamlConfiguration.set("NowID", Integer.valueOf(0));
            }
            if (!yamlConfiguration.contains("MaxID")) {
              yamlConfiguration.set("MaxID", Integer.valueOf(1000));
            }
            
            try {
              yamlConfiguration.save(Variable.f_log);
            } catch (IOException e2) {
              
              e2.printStackTrace();
            } 
            
            yamlConfiguration1.set("Public", Boolean.valueOf(Main.JavaPlugin.getConfig().getBoolean("NormalPublic")));
            yamlConfiguration1.set("pickup", Boolean.valueOf(Main.JavaPlugin.getConfig().getBoolean("NormalPVP")));
            yamlConfiguration1.set("drop", Boolean.valueOf(Main.JavaPlugin.getConfig().getBoolean("NormalPickup")));
            yamlConfiguration1.set("pvp", Boolean.valueOf(Main.JavaPlugin.getConfig().getBoolean("NormalDrop")));
            
            yamlConfiguration1.set("locktime", Boolean.valueOf(false));
            yamlConfiguration1.set("time", Integer.valueOf(0));
            yamlConfiguration1.set("lockweather", Boolean.valueOf(false));
            
            int set_level = 1;
            yamlConfiguration1.set("Level", Integer.valueOf(set_level));
            
            yamlConfiguration1.set("Server", Main.JavaPlugin.getConfig().getString("Server"));
            try {
              yamlConfiguration1.save(f2);
            } catch (IOException e) {
              
              e.printStackTrace();
            } 
            
            yamlConfiguration.set("NowID", Integer.valueOf(nowID + 1));
            
            try {
              yamlConfiguration.save(Variable.f_log);
            } catch (IOException e2) {
              
              e2.printStackTrace();
            } 


            
            yamlConfiguration1.createSection("flowers");
            yamlConfiguration1.createSection("popularity");
            yamlConfiguration1.createSection("gifts");
            yamlConfiguration1.createSection("icon");
            yamlConfiguration1.createSection("advertisement");
            yamlConfiguration1.createSection("limitblock");
            yamlConfiguration1.set("flowers", Integer.valueOf(0));
            yamlConfiguration1.set("popularity", Integer.valueOf(0));
            yamlConfiguration1.set("gifts", new ArrayList());
            yamlConfiguration1.set("icon", "");
            yamlConfiguration1.set("advertisement", new ArrayList());
            yamlConfiguration1.set("limitblock", new ArrayList());
            yamlConfiguration1.createSection("X");
            yamlConfiguration1.createSection("Y");
            yamlConfiguration1.createSection("Z");
            Location loc = world.getSpawnLocation();
            yamlConfiguration1.set("X", Double.valueOf(loc.getX()));
            yamlConfiguration1.set("Y", Double.valueOf(loc.getY()));
            yamlConfiguration1.set("Z", Double.valueOf(loc.getZ()));
            try {
              yamlConfiguration1.save(f2);
            } catch (IOException e) {
              
              e.printStackTrace();
            } 
            
            if (Main.JavaPlugin.getConfig().getInt("MaxSpawnMonstersAmount") != -1) {
              world.setMonsterSpawnLimit(Main.JavaPlugin.getConfig().getInt("MaxSpawnMonstersAmount"));
            }
            
            if (Main.JavaPlugin.getConfig().getInt("MaxSpawnAnimalsAmount") != -1) {
              world.setMonsterSpawnLimit(Main.JavaPlugin.getConfig().getInt("MaxSpawnAnimalsAmount"));
            }
//            Schematics.loadSchematic("1.schematic", world, world.getSpawnLocation());
            FirstBorderShaped.ShapeBorder(world);
            
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(Variable.Lang_YML.getString("AdminCreateHomeForPlayerSuccess"));
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          } else {
            
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(Variable.Lang_YML.getString("AdminCreateHomeForPlayerFailed"));
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            return false;
          } 
        } 

        
        return false;
      } 
    } 

    
    if (args.length == 1 && 
    	      args[0].equalsIgnoreCase("Help")) {
    	      Bukkit.dispatchCommand(sender, "sh help 1");
    	      return false;
    } 
    
    if (args.length == 1 && 
    	      args[0].equalsIgnoreCase("rank")) {
    	      Bukkit.dispatchCommand(sender, "sh rank 1");
    	      return false;
    	    } 
    
    //玩家指令
    if (!(sender instanceof Player)) {
      sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
      sender.sendMessage(Variable.Lang_YML.getString("CommandSenderTip"));
      sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
      return false;
    } 
    final Player p = (Player)sender;
    
    if (Util.CheckIllegalName(p).booleanValue()) {
      sender.sendMessage(Variable.Lang_YML.getString("PlayerHasIllegalName"));
      return false;
    } 
    


    
    if (args.length == 1 && (
      args[0].equalsIgnoreCase("Open") || args[0].equalsIgnoreCase("Menu"))) {
      MainGui gui = new MainGui(p);
      p.openInventory(gui.getInventory());
      return false;
    } 

    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("Open") && args[1].equalsIgnoreCase("Main")) {
      MainGui gui = new MainGui(p);
      p.openInventory(gui.getInventory());
      return false;
    } 

    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("Open") && args[1].equalsIgnoreCase("Check")) {
      CheckGui gui = new CheckGui(p);
      p.openInventory(gui.getInventory());
      return false;
    } 
    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("Open") && args[1].equalsIgnoreCase("Create")) {
      CreateGui gui = new CreateGui(p);
      p.openInventory(gui.getInventory());
      return false;
    } 
    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("Open") && args[1].equalsIgnoreCase("Manage")) {
      ManageGui gui = new ManageGui(p);
      p.openInventory(gui.getInventory());
      return false;
    } 
    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("Open") && args[1].equalsIgnoreCase("Manage2")) {
      ManageGui2 gui = new ManageGui2(p);
      p.openInventory(gui.getInventory());
      return false;
    } 
    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("Open") && args[1].equalsIgnoreCase("Visit")) {
      VisitGui gui = new VisitGui();
      p.openInventory(gui.getInventory());
      return false;
    } 
    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("Open") && args[1].equalsIgnoreCase("Invite")) {
      InviteGui gui = new InviteGui();
      p.openInventory(gui.getInventory());
      return false;
    } 
    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("Open") && args[1].equalsIgnoreCase("Trust")) {
      TrustGui gui = new TrustGui();
      p.openInventory(gui.getInventory());
      return false;
    } 
    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("Open") && args[1].equalsIgnoreCase("Deny")) {
      DenyGui gui = new DenyGui();
      p.openInventory(gui.getInventory());
      return false;
    } 

    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("close")) {
      if (p.getOpenInventory() != null) {
        p.closeInventory();
      }
      return false;
    } 

    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("Help")) {
      if (args[1].equalsIgnoreCase("1")) {
        
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        for (int i = 0; i < Variable.Lang_YML.getStringList("Help-1").size(); i++) {
          String[] str = ((String)Variable.Lang_YML.getStringList("Help-1").get(i)).split(",");
          
          if(Variable.has_no_click_message) {
        	  p.sendMessage(str[0]);
          }else {
              TextComponent e1 = new TextComponent(str[0]);
              
              if (str[0].contains("下一") || str[0].contains("第一") || str[0].contains("First") || 
                str[0].contains("Next") || str[0].contains("上一")) {
                e1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, str[1]));
              } else {
                e1.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, str[1]));
              } 
              p.spigot().sendMessage((BaseComponent)e1);
          }
          

        }
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        
        return false;
      }  if (args[1].equalsIgnoreCase("2")) {
        
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        for (int i = 0; i < Variable.Lang_YML.getStringList("Help-2").size(); i++) {
          String[] str = ((String)Variable.Lang_YML.getStringList("Help-2").get(i)).split(",");
          if(Variable.has_no_click_message) {
        	  p.sendMessage(str[0]);
          }else {
              TextComponent e1 = new TextComponent(str[0]);
              
              if (str[0].contains("下一") || str[0].contains("第一") || str[0].contains("First") || 
                str[0].contains("Next") || str[0].contains("上一")) {
                e1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, str[1]));
              } else {
                e1.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, str[1]));
              } 
              
              p.spigot().sendMessage((BaseComponent)e1);

          }
        } 
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        
        return false;
      }  if (args[1].equalsIgnoreCase("3")) {
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        for (int i = 0; i < Variable.Lang_YML.getStringList("Help-3").size(); i++) {
          String[] str = ((String)Variable.Lang_YML.getStringList("Help-3").get(i)).split(",");
          if(Variable.has_no_click_message) {
        	  p.sendMessage(str[0]);
          }else {
        	  TextComponent e1 = new TextComponent(str[0]);
              
              if (str[0].contains("下一") || str[0].contains("第一") || str[0].contains("Next") || 
                str[0].contains("First") || str[0].contains("上一")) {
                e1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, str[1]));
              } else {
                e1.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, str[1]));
              } 
              p.spigot().sendMessage((BaseComponent)e1);
          }
          
        } 
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        
        return false;
      }if (args[1].equalsIgnoreCase("4")) {
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          for (int i = 0; i < Variable.Lang_YML.getStringList("Help-4").size(); i++) {
            String[] str = ((String)Variable.Lang_YML.getStringList("Help-4").get(i)).split(",");
            if(Variable.has_no_click_message) {
          	  p.sendMessage(str[0]);
            }else {
                TextComponent e1 = new TextComponent(str[0]);
                
                if (str[0].contains("下一") || str[0].contains("第一") || str[0].contains("Next") || 
                  str[0].contains("First") || str[0].contains("上一")) {
                  e1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, str[1]));
                } else {
                  e1.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, str[1]));
                } 
                p.spigot().sendMessage((BaseComponent)e1);
            }

          } 
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          
          return false;
        }if (args[1].equalsIgnoreCase("5")) {
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            for (int i = 0; i < Variable.Lang_YML.getStringList("Help-5").size(); i++) {
              String[] str = ((String)Variable.Lang_YML.getStringList("Help-5").get(i)).split(",");
              if(Variable.has_no_click_message) {
              	  p.sendMessage(str[0]);
                }else {
                	TextComponent e1 = new TextComponent(str[0]);
                    
                    if (str[0].contains("下一") || str[0].contains("第一") || str[0].contains("Next") || 
                      str[0].contains("First") || str[0].contains("上一")) {
                      e1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, str[1]));
                    } else {
                      e1.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, str[1]));
                    } 
                    p.spigot().sendMessage((BaseComponent)e1);
                }
              
            } 
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            
            return false;
          }if (args[1].equalsIgnoreCase("6")) {
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              for (int i = 0; i < Variable.Lang_YML.getStringList("Help-6").size(); i++) {
                String[] str = ((String)Variable.Lang_YML.getStringList("Help-6").get(i)).split(",");
                if(Variable.has_no_click_message) {
                	  p.sendMessage(str[0]);
                  }else {
                      TextComponent e1 = new TextComponent(str[0]);
                      
                      if (str[0].contains("下一") || str[0].contains("第一") || str[0].contains("Next") || 
                        str[0].contains("First") || str[0].contains("上一")) {
                        e1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, str[1]));
                      } else {
                        e1.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, str[1]));
                      } 
                      p.spigot().sendMessage((BaseComponent)e1);
                    } 
                  }

              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              
              return false;
            }
    }



    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("check")) {
      Player p5 = null;
      if (sender instanceof Player) {
        p5 = (Player)sender;
      }
      if (!Main.JavaPlugin.getConfig().getBoolean("Permission.CommandUser") && 
        !p.hasPermission("SelfHome.check") && !p.hasPermission("SelfHome.command.user")) {
        String tip = Variable.Lang_YML.getString("NoPermissionCheck");
        if (tip.contains("<Permission>")) {
          tip = tip.replace("<Permission>", "SelfHome.Check");
        }
        p.sendMessage(tip);
        return false;
      }

      
      (new BukkitRunnable()
        {
          Player p5;
          
          public void run() {
            File folder = new File(Variable.Tempf);
            sender.sendMessage(Variable.Lang_YML.getString("CheckListTitle"));
            
            if (Variable.bungee) {
              List<String> members = MySQL.CheckHasPermission(p.getName());
              for (String e : members) {
            	  
            	  
            	  if(Variable.has_no_click_message) {
                  	  p.sendMessage("§e" + e + Variable.Lang_YML.getString("CheckSuffix"));
            	  }else {
            		  TextComponent Click_Message = new TextComponent(
                              "§e" + e + Variable.Lang_YML.getString("CheckSuffix"));
                          Click_Message
                            .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sh v " + e));
                          p.spigot().sendMessage((BaseComponent)Click_Message);
            	  }
            	  
                
              } 
            } else {
              int b; int j; File[] arrayOfFile; for (j = (arrayOfFile = folder.listFiles()).length, b = 0; b < j; ) { File temp = arrayOfFile[b];
                String want_to = temp.getPath().replace(Variable.Tempf, "").replace(".yml", "")
                  .replace(Variable.file_loc_prefix, "");
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(temp);
                for (int i = 0; i < yamlConfiguration.getStringList("Members").size(); i++) {
                  String temp_str = yamlConfiguration.getStringList("Members").get(i);
                  if (temp_str.equalsIgnoreCase(this.p5.getPlayer().getName()) || temp_str.equals("*")) {
                	  
                	  
                	  if(Variable.has_no_click_message) {
                      	  p.sendMessage( "§e" + want_to + Variable.Lang_YML.getString("CheckSuffix"));
                	  }else {
                          TextComponent Click_Message = new TextComponent(
                                  "§e" + want_to + Variable.Lang_YML.getString("CheckSuffix"));
                              Click_Message.setClickEvent(
                                  new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sh v " + want_to));
                              p.spigot().sendMessage((BaseComponent)Click_Message);
                	  }
                	  

                  } 
                } 
                b++; }
            
            } 
            sender.sendMessage(Variable.Lang_YML.getString("CheckListEnd"));
          }
        }).runTask((Plugin)Main.JavaPlugin);
      return false;
    } 

    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("setSpawn")) {
      if (!Main.JavaPlugin.getConfig().getBoolean("Permission.SetSpawn") && 
        !p.hasPermission("SelfHome.SetSpawn")) {
        String tip = Variable.Lang_YML.getString("NoPermissionCheck");
        if (tip.contains("<Permission>")) {
          tip = tip.replace("<Permission>", "SelfHome.SetSpawn");
        }
        p.sendMessage(tip);
        return false;
      } 


      
      if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
        
        if (Bukkit.getVersion().contains("1.7.10") || Bukkit.getVersion().contains("1.7.2")) {
          p.getWorld().setSpawnLocation((int)p.getLocation().getX(), (int)p.getLocation().getY(), (int)p.getLocation().getZ());
        } else {
          p.getWorld().setSpawnLocation(p.getLocation());
        } 


        
        if (Main.JavaPlugin.getConfig().getBoolean("BorderSwitch")) {
          try {
            World world = Bukkit.getWorld(
                Variable.world_prefix + p.getWorld().getName().replace(Variable.world_prefix, ""));
            world.getWorldBorder().setCenter(p.getLocation());
            world.getWorldBorder().setSize(world.getWorldBorder().getSize());
          } catch (NoSuchMethodError e) {
            Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("BorderException"));
          } 
        }
        
        String temp = Variable.Lang_YML.getString("SetSpawnSuccess");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
      } else {
        
        String temp = Variable.Lang_YML.getString("NoOwnerAndManagerPermission");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
      } 
      
      return false;
    } 

    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("mobs")) {
      Player p3 = (Player)sender;
      if (p3.isOp()) {
        String result = "";
        for (Entity entity : p3.getWorld().getEntities()) {
          if (entity instanceof org.bukkit.entity.LivingEntity) {
            result = String.valueOf(result) + " " + entity.getType().toString();
          }
        } 
        p3.sendMessage(result);
      } else {
        
        String temp = Variable.Lang_YML.getString("AdminCommand");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
      } 
      return false;
    }
    
    
    
    if (args.length == 2 && 
    	      args[0].equalsIgnoreCase("setBiome")) {
    	
    	if(sender instanceof Player) {
  	      Player p3 = (Player)sender;
  	      if (p3.hasPermission("SelfHome.Biome")) {


  	    		        // AsyncWorld world = AsyncWorld.wrap(bukkitWorld); // 或是封包已存在的世界
  	    		       World world = p3.getWorld();
  	    	    	  Chunk chunk = p3.getWorld().getChunkAt(p3.getLocation());
  	    	    	  
  	    	    	  try {
  	  	    	    	  for(int x = 0;x<16;x++) {
  	  	    	    		  for(int z = 0;z<16;z++) {
  	  	    	    			  for(int y = 0;y<255;y++) {
  	  	        	    			  Block block = chunk.getBlock(x, y, z);
  	  	        	    			  
  	  	            	    		  block.setBiome(Biome.valueOf(args[1].toUpperCase()));
  	  	    	    			  }
  	  	        	    	  }
  	  	    	    	  }
  	    	    	  }catch (IllegalArgumentException e) {
  	    	    		  p.sendMessage(Variable.Lang_YML.getString("BiomeError"));
  	    	    		  return false;
  	    	    	  }
  	    	    	  

  	
  	    	  
  	    	  World tworld = p3.getWorld();
  	    	  for(Player t: tworld.getPlayers()) {
  	    		  t.sendMessage(Variable.Lang_YML.getString("BiomeChangeTip"));
  	    	  }
  	    	  
  	      }else {
  	    	  
  	    	 String Language = Variable.Lang_YML.getString("NoPermissionCheck");
  	       if (Language.contains("<Permission>")) {
  	         Language = Language.replace("<Permission>", "SelfHome.Biome");
  	       }
  	       if (!p.hasPermission("SelfHome.Biome")) {
  	         sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
  	         sender.sendMessage(Language);
  	         sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
  	         return false;
  	       } 
  	       
  	    	  
  	    	  
  	    	  
  	      }
  	      
  	      
  	      
  	      
    	}

    	      return false;
    	    }
    
    

    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("update")) {
      if (!Main.JavaPlugin.getConfig().getBoolean("Permission.CommandUser") && 
        !p.hasPermission("SelfHome.Update") && !p.hasPermission("SelfHome.command.user")) {
        String tip = Variable.Lang_YML.getString("NoPermissionCheck");
        if (tip.contains("<Permission>")) {
          tip = tip.replace("<Permission>", "SelfHome.Update");
        }
        p.sendMessage(tip);
        return false;
      } 

      
      if (!Util.CheckIsHome(p.getWorld().getName().replace(Variable.world_prefix, ""))) {
        String tip = Variable.Lang_YML.getString("NowIsNotHome");
        p.sendMessage(tip);
        return false;
      } 
      
      if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
        File f = new File(Variable.Tempf, 
            String.valueOf(p.getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
        
        if (Variable.bungee)
        {
          Integer Now = Integer.valueOf(MySQL.getLevel(p.getWorld().getName().replace(Variable.world_prefix, "")));
          
          if (Now.intValue() >= Main.JavaPlugin.getConfig().getInt("MaxLevel")) {
            
            String temp = Variable.Lang_YML.getString("ReachMaxLevel");
            if (temp.contains("<Level>")) {
              temp = temp.replace("<Level>", String.valueOf(Now));
            }
            
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          }
          else {
            
            double GetMoney = Variable.econ.getBalance((OfflinePlayer)p);
            if (GetMoney >= ((Double)Main.JavaPlugin.getConfig().getDoubleList("MoneyNeed").get(Now.intValue() - 1)).doubleValue()) {
              
              if (Variable.PlyaerPointsModule) {
                Integer GetPoints = Integer.valueOf(Variable.playerPoints.getAPI().look(p.getUniqueId()));
                if (GetPoints.intValue() < ((Integer)Main.JavaPlugin.getConfig().getIntegerList("PointsNeed")
                  .get(Now.intValue() - 1)).intValue()) {
                  String temp = Variable.Lang_YML.getString("UpdateNoPoints");
                  if (temp.contains("<NeedPoints>")) {
                    temp = temp.replace("<NeedPoints>", String.valueOf(Main.JavaPlugin
                          .getConfig().getIntegerList("PointsNeed").get(Now.intValue() - 1)));
                  }
                  sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                  sender.sendMessage(temp);
                  sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
                  return false;
                } 
              } 

              
              if (!((String)Main.JavaPlugin.getConfig().getStringList("ItemsNeed").get(Now.intValue() - 1)).equalsIgnoreCase("")) {
                String[] temp = ((String)Main.JavaPlugin.getConfig().getStringList("ItemsNeed").get(Now.intValue() - 1))
                  .split(",");
                ItemStack i = new ItemStack(Material.valueOf(temp[0]));
                i.setAmount(Integer.valueOf(temp[1]).intValue());
                if (!p.getInventory().containsAtLeast(i, i.getAmount())) {
                  String lang = Variable.Lang_YML.getString("UpdateNoEnoughItems");
                  if (lang.contains("<Amount>")) {
                    lang = lang.replace("<Amount>", String.valueOf(i.getAmount()));
                  }
                  if (lang.contains("<Item>")) {
                    lang = lang.replace("<Item>", String.valueOf(i.getType().toString()));
                  }
                  sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                  p.sendMessage(lang);
                  sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
                  return false;
                } 
                int amount = i.getAmount();
                for (int e = 0; e < p.getInventory().getSize(); e++) {
                  if (p.getInventory().getItem(e) != null) {

                    
                    ItemStack i_temp = p.getInventory().getItem(e);
                    if (i_temp.getType() == i.getType()) {
                      if (i_temp.getAmount() > amount) {
                        ItemStack clone = i_temp.clone();
                        clone.setAmount(i_temp.getAmount() - amount);
                        p.getInventory().setItem(e, clone);
                        break;
                      } 
                      amount -= i_temp.getAmount();
                      p.getInventory().setItem(e, null);
                    } 
                  } 
                } 
              } 


              
              if (Variable.PlyaerPointsModule) {
                Variable.playerPoints.getAPI().take(p.getUniqueId(), (
                    (Integer)Main.JavaPlugin.getConfig().getIntegerList("PointsNeed").get(Now.intValue() - 1)).intValue());
              }
              
              Variable.econ.withdrawPlayer((OfflinePlayer)p, (
                  (Double)Main.JavaPlugin.getConfig().getDoubleList("MoneyNeed").get(Now.intValue() - 1)).doubleValue());
              
              MySQL.setLevel(p.getWorld().getName().replace(Variable.world_prefix, ""), String.valueOf(Now.intValue() + 1));
              
              
              if(Variable.hook_FastAsyncWorldEdit && Main.JavaPlugin.getConfig().getBoolean("FaweSwitch") && Main.JavaPlugin.getConfig().getBoolean("UpdateClearOld")) {
            	  FirstBorderShaped.AddShapeBorder(p.getWorld());
              }
             

              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              String temp = Variable.Lang_YML.getString("UpdateToNewLevel");
              if (temp.contains("<Level>")) {
                temp = temp.replace("<Level>", 
                    String.valueOf(MySQL.getLevel(p.getWorld().getName().replace(Variable.world_prefix, ""))));
              }
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              
              if (Main.JavaPlugin.getConfig().getBoolean("BorderSwitch")) {
                try {
                  World world = Bukkit.getWorld(Variable.world_prefix + p.getName());
                  world.getWorldBorder().setCenter(world.getSpawnLocation());
                  world.getWorldBorder().setSize(world.getWorldBorder().getSize() + 
                      Main.JavaPlugin.getConfig().getInt("UpdateRadius"));
                } catch (Exception e) {
                  Bukkit.getConsoleSender()
                    .sendMessage(Variable.Lang_YML.getString("BorderException"));
                }
              
              }
            } else {
              
              String temp = Variable.Lang_YML.getString("UpdateNoEnoughMoney");
              if (temp.contains("<NeedMoney>")) {
                temp = temp.replace("<NeedMoney>", 
                    String.valueOf(Main.JavaPlugin.getConfig().getDoubleList("MoneyNeed").get(Now.intValue() - 1)));
              }
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            } 
          } 

          
          Util.refreshBorder(p.getWorld());
        }
        else
        {
          YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f);
          Integer Now = Integer.valueOf(yamlConfiguration.getInt("Level"));
          if (Now.intValue() >= Main.JavaPlugin.getConfig().getInt("MaxLevel")) {
            
            String temp = Variable.Lang_YML.getString("ReachMaxLevel");
            if (temp.contains("<Level>")) {
              temp = temp.replace("<Level>", String.valueOf(yamlConfiguration.getInt("Level")));
            }
            
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          }
          else {
            
            double GetMoney = Variable.econ.getBalance((OfflinePlayer)p);
            if (GetMoney >= ((Double)Main.JavaPlugin.getConfig().getDoubleList("MoneyNeed").get(Now.intValue() - 1)).doubleValue()) {
              
              if (Variable.PlyaerPointsModule) {
                Integer GetPoints = Integer.valueOf(Variable.playerPoints.getAPI().look(p.getUniqueId()));
                if (GetPoints.intValue() < ((Integer)Main.JavaPlugin.getConfig().getIntegerList("PointsNeed")
                  .get(Now.intValue() - 1)).intValue()) {
                  String temp = Variable.Lang_YML.getString("UpdateNoPoints");
                  if (temp.contains("<NeedPoints>")) {
                    temp = temp.replace("<NeedPoints>", String.valueOf(Main.JavaPlugin
                          .getConfig().getIntegerList("PointsNeed").get(Now.intValue() - 1)));
                  }
                  sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                  sender.sendMessage(temp);
                  sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
                  return false;
                } 
              } 


              
              if (!((String)Main.JavaPlugin.getConfig().getStringList("ItemsNeed").get(Now.intValue() - 1)).equalsIgnoreCase("")) {
                String[] temp = ((String)Main.JavaPlugin.getConfig().getStringList("ItemsNeed").get(Now.intValue() - 1))
                  .split(",");
                ItemStack i = new ItemStack(Material.valueOf(temp[0]));
                i.setAmount(Integer.valueOf(temp[1]).intValue());
                if (!p.getInventory().containsAtLeast(i, i.getAmount())) {
                  String lang = Variable.Lang_YML.getString("UpdateNoEnoughItems");
                  if (lang.contains("<Amount>")) {
                    lang = lang.replace("<Amount>", String.valueOf(i.getAmount()));
                  }
                  if (lang.contains("<Item>")) {
                    lang = lang.replace("<Item>", String.valueOf(i.getType().toString()));
                  }
                  sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                  p.sendMessage(lang);
                  sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
                  return false;
                } 
                int amount = i.getAmount();
                for (int e = 0; e < p.getInventory().getSize(); e++) {
                  if (p.getInventory().getItem(e) != null) {

                    
                    ItemStack i_temp = p.getInventory().getItem(e);
                    if (i_temp.getType() == i.getType()) {
                      if (i_temp.getAmount() > amount) {
                        ItemStack clone = i_temp.clone();
                        clone.setAmount(i_temp.getAmount() - amount);
                        p.getInventory().setItem(e, clone);
                        break;
                      } 
                      amount -= i_temp.getAmount();
                      p.getInventory().setItem(e, null);
                    } 
                  } 
                } 
              } 


              
              if (Variable.PlyaerPointsModule)
              {
                Variable.playerPoints.getAPI().take(p.getUniqueId(), (
                    (Integer)Main.JavaPlugin.getConfig().getIntegerList("PointsNeed").get(Now.intValue() - 1)).intValue());
              }

              
              Variable.econ.withdrawPlayer((OfflinePlayer)p, (
                  (Double)Main.JavaPlugin.getConfig().getDoubleList("MoneyNeed").get(Now.intValue() - 1)).doubleValue());
              yamlConfiguration.set("Level", Integer.valueOf(yamlConfiguration.getInt("Level") + 1));
              try {
                yamlConfiguration.save(f);
              } catch (IOException e) {
                
                e.printStackTrace();
              }
              yamlConfiguration = YamlConfiguration.loadConfiguration(f);
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              String temp = Variable.Lang_YML.getString("UpdateToNewLevel");
              if (temp.contains("<Level>")) {
                temp = temp.replace("<Level>", String.valueOf(yamlConfiguration.getInt("Level")));
              }
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              FirstBorderShaped.AddShapeBorder(p.getWorld());
              
              if (Main.JavaPlugin.getConfig().getBoolean("BorderSwitch")) {
                try {
                  World world = Bukkit.getWorld(Variable.world_prefix + p.getName());
                  world.getWorldBorder().setCenter(world.getSpawnLocation());
                  world.getWorldBorder().setSize(world.getWorldBorder().getSize() + 
                      Main.JavaPlugin.getConfig().getInt("UpdateRadius"));
                } catch (Exception e) {
                  Bukkit.getConsoleSender()
                    .sendMessage(Variable.Lang_YML.getString("BorderException"));
                }
              
              }
            } else {
              
              String temp = Variable.Lang_YML.getString("UpdateNoEnoughMoney");
              if (temp.contains("<NeedMoney>")) {
                temp = temp.replace("<NeedMoney>", 
                    String.valueOf(Main.JavaPlugin.getConfig().getDoubleList("MoneyNeed").get(Now.intValue() - 1)));
              }
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            } 
          } 

          
          Util.refreshBorder(p.getWorld());
        }
      
      } else {
        
        String temp = Variable.Lang_YML.getString("NoOwnerAndManagerPermission");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
      } 
      
      return false;
    } 




    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("look")) {
      if (!Main.JavaPlugin.getConfig().getBoolean("Permission.CommandUser") && 
        !p.hasPermission("SelfHome.Look") && !p.hasPermission("SelfHome.command.user")) {
        String tip = Variable.Lang_YML.getString("NoPermissionCheck");
        if (tip.contains("<Permission>")) {
          tip = tip.replace("<Permission>", "SelfHome.Look");
        }
        p.sendMessage(tip);
        return false;
      } 

      
      if (!Util.CheckIsHome(p.getWorld().getName().replace(Variable.world_prefix, ""))) {
        String tip = Variable.Lang_YML.getString("NowIsNotHome");
        p.sendMessage(tip);
        return false;
      } 
      
      sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
      for (int i = 0; i < Variable.Lang_YML.getStringList("LookInfo").size(); i++) {
        String temp = Variable.Lang_YML.getStringList("LookInfo").get(i);
        temp = PlaceholderAPI.setPlaceholders(p, temp);
        sender.sendMessage(temp);
      } 
      sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
      return false;
    } 

    



    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("nbt")) {
      if (p.isOp()) {
        if (Variable.Debug.contains(p.getName())) {
          Variable.Debug.remove(p.getName());
          sender.sendMessage(Variable.Lang_YML.getString("DisableNBTDebug"));
        } else {
          Variable.Debug.add(p.getName());
          sender.sendMessage(Variable.Lang_YML.getString("EnableNBTDebug"));
        } 
        return false;
      } 
      sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
      
      return false;
    } 


    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("item")) {
      if (sender instanceof Player) {
        Player temp = (Player)sender;
        if (!temp.isOp()) {
          sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
          return false;
        } 
      } else {
        sender.sendMessage(Variable.Lang_YML.getString("CommandSenderIsNotAllowToUseTheCommand"));
        return false;
      } 
      Player pl = (Player)sender;
      if (pl.getItemInHand() == null) {
        pl.sendMessage("§enull");
      }
      else if (pl.getItemInHand().getType() == Material.AIR) {
        pl.sendMessage("§eAIR");
      } else {
    	  
    	  if(Variable.has_no_click_message) {
    		  pl.sendMessage("§e" + Util.getItemNBTString(pl.getItemInHand()));
    	  }else {
    		  TextComponent Send_Block_Message = new TextComponent("§e" + Util.getItemNBTString(pl.getItemInHand()) + " §b>> §dCopy");
    	        Send_Block_Message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Util.getItemNBTString(pl.getItemInHand())));
    	        pl.spigot().sendMessage((BaseComponent)Send_Block_Message);
    	  }
    	  
      
      } 
      
      return false;
    } 




    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("item")) {
      if (p.isOp()) {
        ItemStack i = p.getItemInHand();
        
        p.sendMessage("§e§l§m--------------§7[§eDeBug§7]§e§l§m--------------");
        
        if(Variable.has_no_click_message) {
  		  p.sendMessage("§eMaterial:§d" + i.getType().toString() + 
                  "§e,SubID:§d" + i.getDurability());
        }else {
            TextComponent Send_Block_Message = new TextComponent("§eMaterial:§d" + i.getType().toString() + 
                    "§e,SubID:§d" + i.getDurability() + " §b>> §dCopy");
                Send_Block_Message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
                      "Material:" + i.getType().toString() + ",SubID:" + i.getDurability()));
                p.spigot().sendMessage((BaseComponent)Send_Block_Message);  
  		}
        

        p.sendMessage("§e§l§m--------------§7[§eDebug§7]§e§l§m--------------");
      } else {
        
        p.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
      } 
      return false;
    } 

    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("wholeDelete")) {
      if (!Main.JavaPlugin.getConfig().getBoolean("Permission.CommandUser") && 
        !p.hasPermission("SelfHome.WholeDelete") && !p.hasPermission("SelfHome.command.user")) {
        String tip = Variable.Lang_YML.getString("NoPermissionCheck");
        if (tip.contains("<Permission>")) {
          tip = tip.replace("<Permission>", "SelfHome.WholeDelete");
        }
        p.sendMessage(tip);
        return false;
      } 


      
      if (!Util.CheckIsHome(p.getLocation().getWorld().getName().replace(Variable.world_prefix, ""))) {
        String temp = Variable.Lang_YML.getString("NowIsNotHome");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        return false;
      } 
      
      if (!p.getWorld().getName().replace(Variable.world_prefix, "").equalsIgnoreCase(p.getName())) {
        String temp = Variable.Lang_YML.getString("DeleteNotIsMyHome");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        return false;
      } 
      
      boolean real_delete = false;
      for (int d = 0; d < Variable.waitDeleteconfirm.size(); d++) {
        if (((String)Variable.waitDeleteconfirm.get(d)).equalsIgnoreCase(p.getName())) {
          real_delete = true;
        }
      } 
      
      if (real_delete) {
        Variable.waitDeleteconfirm.remove(p);
      } else {
        
    	  if(Variable.has_no_click_message) {
    		  p.sendMessage(Variable.Lang_YML.getString("ConfirmDelete"));
    	  }else {
    	      TextComponent Click_Message = new TextComponent(Variable.Lang_YML.getString("ConfirmDelete"));
    	        Click_Message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sh wholedelete"));
    	        p.spigot().sendMessage((BaseComponent)Click_Message); 
    	  }
    	  
  
        
        Variable.waitDeleteconfirm.add(p.getName());
        
        (new BukkitRunnable()
          {
            public void run()
            {
              for (int i = 0; i < Variable.waitDeleteconfirm.size(); i++) {
                if (((String)Variable.waitDeleteconfirm.get(i)).equalsIgnoreCase(p.getName())) {
                  Variable.waitDeleteconfirm.remove(p.getName());
                }
              }
            
            }
          }).runTaskLater((Plugin)Main.JavaPlugin, 100L);

        
        return false;
      } 
      
      YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(Variable.f_log);
      List<String> list = yamlConfiguration.getStringList("DeleteTimes");
      if (list == null) {
        list = new ArrayList<>();
        yamlConfiguration.set("DeleteTimes", list);
        try {
          yamlConfiguration.save(Variable.f_log);
        } catch (IOException e) {
          
          e.printStackTrace();
        } 
      } 
      boolean check_contain = false;
      for (int c = 0; c < list.size(); c++) {
        String[] temp3 = ((String)list.get(c)).split(",");
        String name = temp3[0];
        if (name.equalsIgnoreCase(p.getName())) {
          check_contain = true;
          int cs = Integer.valueOf(temp3[1]).intValue();
          if (cs >= Main.JavaPlugin.getConfig().getInt("MaxDelete")) {
            String temp5 = Variable.Lang_YML.getString("MaxDeleteLanguage");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp5);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            return false;
          } 

          
          boolean cooldown_check = false;
          for (int i = 0; i < Variable.Deletecooldown.size(); i++) {
            if (((String)Variable.Deletecooldown.get(i)).equalsIgnoreCase(p.getName())) {
              cooldown_check = true;
            }
          } 
          
          if (cooldown_check) {
            p.sendMessage(Variable.Lang_YML.getString("IsDeleteCooldown"));
            return false;
          } 
          Variable.Deletecooldown.add(p.getName());

          
          (new BukkitRunnable()
            {
              public void run() {
                if (Variable.Deletecooldown.contains(p.getName())) {
                  Variable.Deletecooldown.remove(p.getName());
                  String temp = Variable.Lang_YML.getString("DeleteCooldownEnd");
                  
                  sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                  sender.sendMessage(temp);
                  sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
                } 
              }
            }).runTaskLater((Plugin)Main.JavaPlugin, 1200L);
          list.set(c, name + "," + (cs + 1));
          yamlConfiguration.set("DeleteTimes", list);
          try {
            yamlConfiguration.save(Variable.f_log); break;
          } catch (IOException e) {
            
            e.printStackTrace();
            
            break;
          } 
        } 
      } 
      
      if (!check_contain) {
        list.add(String.valueOf(p.getName()) + ",1");
        yamlConfiguration.set("DeleteTimes", list);
        try {
          yamlConfiguration.save(Variable.f_log);
        } catch (IOException e) {
          
          e.printStackTrace();
        } 
      } 

      
      yamlConfiguration = YamlConfiguration.loadConfiguration(Variable.f_log);
      if (!yamlConfiguration.contains("NowID")) {
        yamlConfiguration.set("NowID", Integer.valueOf(0));
      }
      if (!yamlConfiguration.contains("MaxID")) {
        yamlConfiguration.set("MaxID", Integer.valueOf(1000));
      }
      try {
        yamlConfiguration.save(Variable.f_log);
      } catch (IOException e2) {
        
        e2.printStackTrace();
      } 
      
      yamlConfiguration.set("NowID", Integer.valueOf(yamlConfiguration.getInt("NowID") - 1));
      
      try {
        yamlConfiguration.save(Variable.f_log);
      } catch (IOException e) {
        
        e.printStackTrace();
      } 

      
      String temp = Variable.Lang_YML.getString("WholeDeleteSuccess");
      sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
      sender.sendMessage(temp);
      sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
      
      Variable.Deletecooldown.remove(p);
      
      if (Variable.bungee) {
        MySQL.removePlayer(p.getName());
      } else {
        File f2 = new File(Variable.Tempf, String.valueOf(p.getName()) + ".yml");
        f2.delete();
      } 
      
      World world = Bukkit.getWorld(Variable.world_prefix + p.getName());
      
      if (world != null) {
        for (Player p6 : Bukkit.getWorld(Variable.world_prefix + p.getName()).getPlayers()) {
          p6.teleport(Bukkit.getWorld("world").getSpawnLocation());
          p6.sendMessage(Variable.Lang_YML.getString("WorldHasBeenDeleted"));
        } 
      }
      
      Bukkit.unloadWorld(Variable.world_prefix + p.getName(), true);
      
      if(Variable.hook_multiverseCore == true) {
		   MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
	      	MVWorldManager mv_m = mvcore.getMVWorldManager();
	      	mv_m.removeWorldFromConfig(Variable.world_prefix+p.getName());
       }
      
      (new BukkitRunnable()
        {
          public void run() {
            if (Variable.world_prefix.equalsIgnoreCase("")) {
              if (Bukkit.getVersion().toString().toUpperCase().contains("ARCLIGHT") || Bukkit.getVersion().toString().contains("1.20.1")) {
                File f = new File(Variable.single_server_gen + Variable.world_prefix + p.getName());
                Util.deleteFile(f);
              } else {
                File f = new File(Variable.single_server_gen + "world" + Variable.file_loc_prefix + p.getName());
                Util.deleteFile(f);
              } 
            } else {
              
              File f = new File(Variable.single_server_gen + Variable.world_prefix + p.getName());
              Util.deleteFile(f);
            }
          
          }
        }).runTaskLater((Plugin)Main.JavaPlugin, 5L);
      
      return false;
    } 



    
    if (args.length == 3 && 
      args[0].equalsIgnoreCase("gift") && args[1].equalsIgnoreCase("send") && args[2].equalsIgnoreCase("all")) {
      
      if (sender instanceof Player) {
        Player temp = (Player)sender;
        if (!temp.isOp()) {
          sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
          return false;
        } 
      } 
      
      ItemStack c = p.getItemInHand().clone();
      if (c == null) {
        p.sendMessage(Variable.Lang_YML.getString("SendButTheHandIsAir"));
        return false;
      } 
      if (c.getType() == Material.AIR) {
        p.sendMessage(Variable.Lang_YML.getString("SendButTheHandIsAir"));
        return false;
      } 
      
      List<String> has_send_list = new ArrayList<>();
      List<String> has_not_send_list = new ArrayList<>();
      
      for (Home home : HomeAPI.getHomes()) {
        
        ItemStack i = c.clone();
        
        String home_name = home.getName();
        if (Variable.has_open_gifts_list.containsKey(home_name)) {
          Player has_open = Bukkit.getPlayer(Variable.has_open_gifts_list.get(home_name));
          if (has_open != null) {
            has_open.sendMessage(Variable.Lang_YML.getString("OperatorSendGiftButOpen"));
            has_open.closeInventory();
          } 
        } 


        
        List<String> gifts = new ArrayList<>(home.getGifts());
        
        if (gifts.size() >= 45) {
          has_not_send_list.add(home.getName());
          continue;
        } 
        has_send_list.add(home.getName());
        StreamSerializer ss = new StreamSerializer();
        if (!Variable.Lang_YML.getString("GiftLoreAddPrefix").equalsIgnoreCase("")) {
          String lore = String.valueOf(Variable.Lang_YML.getString("GiftLoreAddPrefix")) + p.getName();
          if (i.hasItemMeta()) {
            ItemMeta meta = i.getItemMeta();
            if (meta.hasLore()) {
              List<String> lores = meta.getLore();
              lores.add(lore);
              meta.setLore(lores);
              i.setItemMeta(meta);
            } else {
              List<String> lores = new ArrayList<>();
              lores.add(lore);
              meta.setLore(lores);
              i.setItemMeta(meta);
            } 
          } else {
            ItemMeta meta = i.getItemMeta();
            List<String> lores = new ArrayList<>();
            lores.add(lore);
            meta.setLore(lores);
            i.setItemMeta(meta);
          } 
        } 
        
        try {
          gifts.add(ss.serializeItemStack(i));
        } catch (IOException e) {
          
          e.printStackTrace();
        } 
        try {
          home.setGifts(gifts);
        } catch (IOException e) {
          
          e.printStackTrace();
        } 
        
        String temp = Variable.Lang_YML.getString("GiftAdd");
        if (temp.contains("<Name>")) {
          temp = temp.replace("<Name>", p.getName());
        }
        
        if(Variable.has_no_click_message) {
            if (Bukkit.getPlayer(home.getName()) != null) {
                Bukkit.getPlayer(home.getName()).sendMessage(temp);
              }
        }else {
        	  TextComponent Click_Message = new TextComponent(temp);
              Click_Message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sh gift open"));
              
              if (Bukkit.getPlayer(home.getName()) != null) {
                Bukkit.getPlayer(home.getName()).spigot().sendMessage((BaseComponent)Click_Message);
              }
        }
        
      
      } 


      
      String temp2 = Variable.Lang_YML.getString("SuccessedSendToAll");
      if (temp2.contains("<List>")) {
        temp2 = temp2.replace("<List>", has_send_list.toString());
      }
      p.sendMessage(temp2);

      
      if (has_not_send_list.size() != 0) {
        String temp3 = Variable.Lang_YML.getString("FailedSendToAll");
        if (temp3.contains("<List>")) {
          temp3 = temp3.replace("<List>", has_not_send_list.toString());
        }
        p.sendMessage(temp3);
      } 


      
      return false;
    } 




    
    if (args.length == 3 && 
      args[0].equalsIgnoreCase("gift") && args[1].equalsIgnoreCase("send")) {
      if (!p.hasPermission("SelfHome.Gift.Send")) {
        p.sendMessage(Variable.Lang_YML.getString("NoPermissionSendTheItemToGift"));
        return false;
      } 
      String home_name = args[2];
      if (!Util.CheckIsHome(home_name)) {
        String temp = Variable.Lang_YML.getString("NowIsNotHome");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        return false;
      } 

      
      if (p.getName().equalsIgnoreCase(home_name)) {
        String temp = Variable.Lang_YML.getString("SendButTheMyHome");
        sender.sendMessage(temp);
        return false;
      } 
      
      ItemStack i = p.getItemInHand();
      if (i == null) {
        p.sendMessage(Variable.Lang_YML.getString("SendButTheHandIsAir"));
        return false;
      } 
      if (i.getType() == Material.AIR) {
        p.sendMessage(Variable.Lang_YML.getString("SendButTheHandIsAir"));
        return false;
      } 

      
      if (Variable.has_open_gifts_list.containsKey(home_name)) {
        p.sendMessage(Variable.Lang_YML.getString("SendButTheInvHasBeenOpen"));
        return false;
      } 
      
      Home home = HomeAPI.getHome(home_name);
      List<String> gifts = new ArrayList<>(home.getGifts());
      
      if (gifts.size() >= 45) {
        p.sendMessage(Variable.Lang_YML.getString("GiftFail"));
        return false;
      } 
      StreamSerializer ss = new StreamSerializer();
      
      if (!Variable.Lang_YML.getString("GiftLoreAddPrefix").equalsIgnoreCase("")) {
        String lore = String.valueOf(Variable.Lang_YML.getString("GiftLoreAddPrefix")) + p.getName();
        
        if (i.hasItemMeta()) {
          ItemMeta meta = i.getItemMeta();
          if (meta.hasLore()) {
            List<String> lores = meta.getLore();
            lores.add(lore);
            meta.setLore(lores);
            i.setItemMeta(meta);
          } else {
            List<String> lores = new ArrayList<>();
            lores.add(lore);
            meta.setLore(lores);
            i.setItemMeta(meta);
          } 
        } else {
          ItemMeta meta = i.getItemMeta();
          List<String> lores = new ArrayList<>();
          lores.add(lore);
          meta.setLore(lores);
          i.setItemMeta(meta);
        } 
      } 


      
      try {
        gifts.add(ss.serializeItemStack(i));
        p.getInventory().remove(i);
      }
      catch (IOException e) {
        
        e.printStackTrace();
      } 
      try {
        home.setGifts(gifts);
      } catch (IOException e) {
        
        e.printStackTrace();
      } 
      
      String temp2 = Variable.Lang_YML.getString("GiftSuccess");
      if (temp2.contains("<Name>")) {
        temp2 = temp2.replace("<Name>", home_name);
      }
      p.sendMessage(temp2);






      
      String temp = Variable.Lang_YML.getString("GiftAdd");
      if (temp.contains("<Name>")) {
        temp = temp.replace("<Name>", p.getName());
      }
      
      
      if(Variable.has_no_click_message) {
    	  if (Bukkit.getPlayer(home.getName()) != null && 
    	            Bukkit.getPlayer(home.getName()).isOnline() && !home.getName().equalsIgnoreCase(p.getName())) {
    	            Bukkit.getPlayer(home.getName()).sendMessage(temp);
    	          }
      }else {
          TextComponent Click_Message = new TextComponent(temp);
          Click_Message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sh gift open"));


          
          if (Bukkit.getPlayer(home.getName()) != null && 
            Bukkit.getPlayer(home.getName()).isOnline() && !home.getName().equalsIgnoreCase(p.getName())) {
            Bukkit.getPlayer(home.getName()).spigot().sendMessage((BaseComponent)Click_Message);
          }
      }
      


      
      return false;
    } 


    
    if (args.length == 4 && 
      args[0].equalsIgnoreCase("popularity") && args[1].equalsIgnoreCase("add")) {
      if (sender instanceof Player) {
        Player temp = (Player)sender;
        if (!temp.isOp()) {
          sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
          return false;
        } 
      } 
      Home home = HomeAPI.getHome(args[2]);
      if (home == null) {
        sender.sendMessage(Variable.Lang_YML.getString("PopularityAddButHomeIsNotExist"));
        return false;
      } 
      
      try {
        home.setPopularity(home.getPopularity() + Integer.valueOf(args[3]).intValue());
      } catch (NumberFormatException e) {
        
        e.printStackTrace();
      } catch (IOException e) {
        
        e.printStackTrace();
      } 
      
      String temp2 = Variable.Lang_YML.getString("PopularityAddSuccess");
      if (temp2.contains("<Now>")) {
        temp2 = temp2.replace("<Now>", String.valueOf(home.getPopularity()));
      }
      p.sendMessage(temp2);
      return false;
    } 


    
    if (args.length == 4 && 
      args[0].equalsIgnoreCase("flower") && args[1].equalsIgnoreCase("add")) {
      if (sender instanceof Player) {
        Player temp = (Player)sender;
        if (!temp.isOp()) {
          sender.sendMessage(Variable.Lang_YML.getString("PlayerIsNotOperator"));
          return false;
        } 
      } 
      Home home = HomeAPI.getHome(args[2]);
      if (home == null) {
        sender.sendMessage(Variable.Lang_YML.getString("FlowerAddButHomeIsNotExist"));
        return false;
      } 
      
      try {
        home.setFlowers(home.getFlowers() + Integer.valueOf(args[3]).intValue());
      } catch (NumberFormatException e) {
        
        e.printStackTrace();
      } catch (IOException e) {
        
        e.printStackTrace();
      } 
      
      String temp2 = Variable.Lang_YML.getString("FlowerAddSuccess");
      if (temp2.contains("<Now>")) {
        temp2 = temp2.replace("<Now>", String.valueOf(home.getFlowers()));
      }
      p.sendMessage(temp2);
      return false;
    } 





    
    if (args.length == 3 && 
      args[0].equalsIgnoreCase("gift") && args[1].equalsIgnoreCase("inv")) {
      if (!p.hasPermission("SelfHome.Gift.Inv")) {
        p.sendMessage(Variable.Lang_YML.getString("InvPlayersGiftGuiButNoPermission"));
        return false;
      } 
      String home_name = args[2];
      if (!Util.CheckIsHome(home_name)) {
        String temp = Variable.Lang_YML.getString("NowIsNotHome");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        return false;
      } 

      
      if (Variable.has_open_gifts_list.containsKey(home_name)) {
        String str = Variable.Lang_YML.getString("HasAlreadyOpenByOthers");
        if (str.contains("<Name>")) {
          str = str.replace("<Name>", Variable.has_open_gifts_list.get(home_name));
        }
        p.sendMessage(str);
        return false;
      } 
      Variable.has_open_gifts_list.put(home_name, p.getName());

      
      GiftGui giftgui = new GiftGui(p, home_name);
      p.openInventory(giftgui.getInventory());
      return false;
    } 


    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("Icon")) {
      if (!p.hasPermission("SelfHome.Icon")) {
        p.sendMessage(Variable.Lang_YML.getString("NoPermissionSetIcon"));
        return false;
      } 
      
      if (!Util.CheckIsHome(p.getWorld().getName().replace(Variable.world_prefix, ""))) {
        String temp = Variable.Lang_YML.getString("NowIsNotHome");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        return false;
      } 

      
      if (!Util.Check(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
        p.sendMessage(Variable.Lang_YML.getString("NoPermissionSetOthersIcon"));
        return false;
      } 

      
      ItemStack i = p.getItemInHand();
      if (i == null) {
        p.sendMessage(Variable.Lang_YML.getString("SetIconButHandIsAir"));
        return false;
      } 
      if (i.getType() == Material.AIR) {
        p.sendMessage(Variable.Lang_YML.getString("SetIconButHandIsAir"));
        return false;
      } 


      
      Home home = HomeAPI.getHome(p.getWorld().getName().replace(Variable.world_prefix, ""));
      try {
        home.setIcon(String.valueOf(i.getType().toString()) + ":" + i.getDurability());
      } catch (IOException e) {
        
        e.printStackTrace();
      } 
      
      if (i.getAmount() == 1) {
        p.setItemInHand(null);
      } else {
        i.setAmount(i.getAmount() - 1);
        p.setItemInHand(i);
      } 
      p.sendMessage(Variable.Lang_YML.getString("SetIconSuccess"));
      return false;
    }

    
    if (args.length == 5 && args[0].equalsIgnoreCase("AddBlockLimit")) {
    	if(sender instanceof Player) {
    		Player te = (Player) sender;
    		if(!(te.isOp())) {
    			te.sendMessage(Variable.Lang_YML.getString("NoPermissionSetCustomBlockLimit"));
    	        return false;
    			
    		}
    	}
    	Home home = HomeAPI.getHome(args[1]);
    	if(home == null) {
    		sender.sendMessage(Variable.Lang_YML.getString("SetCustomBlockButHomeIsNull"));
    		return false;
    	}
    	String str = args[2] + "|" + args[3].toUpperCase() + "|";
    	List<String> list = home.getLimitBlock();
    	
		List<String> list2 = new ArrayList<String>();
		for(String e: list) {
			list2.add(e);
		}
    	
    	boolean success = false;
    	for(int c = 0;c<list2.size();c++) {
    		String temp = list2.get(c);
    		if(temp.contains(str)) {
    			success = true;
    			String[] args2 = temp.split("\\|");
    			int amount = Integer.valueOf(args2[2]);
    			int now = amount + Integer.valueOf(args[4]);
    			list2.set(c, str+String.valueOf(now));
    			
        		try {
        			home.setLimitBlock(list2);
        		} catch (IOException e) {
        			// TODO 自动生成的 catch 块
        			e.printStackTrace();
        		}
    			
    	    	String send_message = Variable.Lang_YML.getString("AddCustomBlockSuccess");
    	    	if(send_message.contains("<Name>")) {
    	    		send_message = send_message.replace("<Name>", args[1]);
    	    	}
    	    	if(send_message.contains("<NBT>")) {
    	    		send_message = send_message.replace("<NBT>", args[3].toUpperCase());
    	    	}  
    	    	if(send_message.contains("<Amount>")) {
    	    		send_message = send_message.replace("<Amount>", String.valueOf(amount));
    	    	}  
    	    	if(send_message.contains("<Type>")) {
    	    		send_message = send_message.replace("<Type>", args[2]);
    	    	}   
    	    	if(send_message.contains("<NowAmount>")) {
    	    		send_message = send_message.replace("<NowAmount>", String.valueOf(now));
    	    	}
    	    	sender.sendMessage(send_message);
    			break;
    			
    		}
    	}
    	
    	if(success == false) {
    		list2.add(str);
    		try {
    			home.setLimitBlock(list2);
    		} catch (IOException e) {
    			// TODO 自动生成的 catch 块
    			e.printStackTrace();
    		}
        	
        	String send_message = Variable.Lang_YML.getString("SetCustomBlockSuccess");
        	if(send_message.contains("<Name>")) {
        		send_message = send_message.replace("<Name>", args[1]);
        	}
        	if(send_message.contains("<NBT>")) {
        		send_message = send_message.replace("<NBT>", args[3].toUpperCase());
        	}  
        	if(send_message.contains("<Amount>")) {
        		send_message = send_message.replace("<Amount>", args[4]);
        	}  
        	if(send_message.contains("<Type>")) {
        		send_message = send_message.replace("<Type>", args[2]);
        	}   
        	sender.sendMessage(send_message);
    		
    		
    	}


		return false;  
    }
    

    if (args.length == 5 && args[0].equalsIgnoreCase("SetBlockLimit")) {
    	if(sender instanceof Player) {
    		Player te = (Player) sender;
    		if(!(te.isOp())) {
    			te.sendMessage(Variable.Lang_YML.getString("NoPermissionSetCustomBlockLimit"));
    	        return false;
    			
    		}
    	}
    	Home home = HomeAPI.getHome(args[1]);
    	if(home == null) {
    		sender.sendMessage(Variable.Lang_YML.getString("SetCustomBlockButHomeIsNull"));
    		return false;
    	}
    	String str = args[2] + "|" + args[3].toUpperCase() + "|" + args[4];
    	List<String> list = home.getLimitBlock();
    	

		List<String> list2 = new ArrayList<String>();
		for(String e: list) {
			list2.add(e);
		}
		
    	
    	
    	for(int c = 0 ;c<list2.size();c++) {
    		String tem = list2.get(c);
    		if(tem.contains(args[2] + "|" + args[3].toUpperCase())) {
    			list2.remove(c);
    		}
    	}


    	list2.add(str);
    	
    	try {
			home.setLimitBlock(list2);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    	
    	String send_message = Variable.Lang_YML.getString("SetCustomBlockSuccess");
    	if(send_message.contains("<Name>")) {
    		send_message = send_message.replace("<Name>", args[1]);
    	}
    	if(send_message.contains("<NBT>")) {
    		send_message = send_message.replace("<NBT>", args[3].toUpperCase());
    	}  
    	if(send_message.contains("<Amount>")) {
    		send_message = send_message.replace("<Amount>", args[4]);
    	}  
    	if(send_message.contains("<Type>")) {
    		send_message = send_message.replace("<Type>", args[2]);
    	}   
    	sender.sendMessage(send_message);
		return false;  
    }
    
    if (args.length == 4 && args[0].equalsIgnoreCase("DelBlockLimit")) {
    	if(sender instanceof Player) {
    		Player te = (Player) sender;
    		if(!(te.isOp())) {
    			te.sendMessage(Variable.Lang_YML.getString("NoPermissionSetCustomBlockLimit"));
    	        return false;
    			
    		}
    	}
    	Home home = HomeAPI.getHome(args[1]);
    	if(home == null) {
    		sender.sendMessage(Variable.Lang_YML.getString("SetCustomBlockButHomeIsNull"));
    		return false;
    	}
    	String str = args[2] + "|" + args[3].toUpperCase();
    	List<String> list = home.getLimitBlock();
		List<String> list2 = new ArrayList<String>();
		for(String e: list) {
			list2.add(e);
		}
		
    	
    	boolean remove_success = false;
    	for(int c = 0;c<list2.size();c++) {
    		String str2 = list2.get(c);
    		if(str2.contains(args[2] + "|" + args[3].toUpperCase())) {
    			list2.remove(c);
    			remove_success = true;
    		}
    		
    	}
    	
    	if(!(remove_success)) {
    		sender.sendMessage(Variable.Lang_YML.getString("SetCustomBlockButHomeButNotContain"));
    		return false;
    	}
    	  	
    	try {
			home.setLimitBlock(list2);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    	
    	String send_message = Variable.Lang_YML.getString("RemoveCustomBlockSuccess");
    	if(send_message.contains("<Name>")) {
    		send_message = send_message.replace("<Name>", args[1]);
    	}
    	if(send_message.contains("<NBT>")) {
    		send_message = send_message.replace("<NBT>", args[3].toUpperCase());
    	}  
    	if(send_message.contains("<Type>")) {
    		send_message = send_message.replace("<Type>", args[2]);
    	}   
    	sender.sendMessage(send_message);
		return false;  
    }
    
    
    
    
    if (args.length >= 2 && 
      args[0].equalsIgnoreCase("info")) {
    	
    	String str = "";
    	for(int d = 1 ; d<args.length;d++) {
    		str += " " + args[d];
    	}
    	
      if (!p.hasPermission("SelfHome.info")) {
        p.sendMessage(Variable.Lang_YML.getString("NoPermissionSetInfo"));
        return false;
      } 
      
      if (!Util.CheckIsHome(p.getWorld().getName().replace(Variable.world_prefix, ""))) {
        String temp = Variable.Lang_YML.getString("NowIsNotHome");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        return false;
      } 
      
      if (!Util.Check(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
        p.sendMessage(Variable.Lang_YML.getString("NoPermissionSetOthersInfo"));
        return false;
      } 
      
      List<String> adv = new ArrayList<>();
      
      if (str.contains(",")) {
        String[] content = str.split(",");
        
        for (int i = 0; i < content.length; i++) {
          content[i] = "§f" + content[i];
          if (content[i].contains("&")) {
            if (!p.hasPermission("SelfHome.Info.Color")) {
              p.sendMessage(Variable.Lang_YML.getString("NoPermissionSetColorInfo"));
              return false;
            } 
            content[i] = content[i].replace("&", "§");
          } 
        } 

        
        adv = Arrays.asList(content);
      } else {
        if (str.contains("&")) {
          if (!p.hasPermission("SelfHome.Info.Color")) {
            p.sendMessage(Variable.Lang_YML.getString("NoPermissionSetColorInfo"));
            return false;
          } 
          str = str.replace("&", "§");
        } 
        
        adv.add("§f" + str);
      } 
      
      Home home = HomeAPI.getHome(p.getWorld().getName().replace(Variable.world_prefix, ""));
      try {
        home.setAdvertisement(adv);
      } catch (IOException e) {
        
        e.printStackTrace();
      } 
      
      p.sendMessage(Variable.Lang_YML.getString("SetInfoSuccess"));
      return false;
    } 




    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("gift") && args[1].equalsIgnoreCase("open")) {
      
      if (!p.hasPermission("SelfHome.Gift.Open")) {
        p.sendMessage(Variable.Lang_YML.getString("NoPermissionOpenGift"));
        return false;
      } 
      
      if (!Util.CheckIsHome(p.getWorld().getName().replace(Variable.world_prefix, ""))) {
        String temp = Variable.Lang_YML.getString("NowIsNotHome");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        return false;
      } 
      
      if (!Util.Check(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
        p.sendMessage(Variable.Lang_YML.getString("NoPermissionOpenOthersGift"));
        return false;
      } 
      
      if (Variable.has_open_gifts_list.containsKey(p.getWorld().getName().replace(Variable.world_prefix, ""))) {
        String str = Variable.Lang_YML.getString("HasAlreadyOpenByOthers");
        if (str.contains("<Name>")) {
          str = str.replace("<Name>", Variable.has_open_gifts_list.get(p.getWorld().getName().replace(Variable.world_prefix, "")));
        }
        p.sendMessage(str);
        return false;
      } 
      Variable.has_open_gifts_list.put(p.getWorld().getName().replace(Variable.world_prefix, ""), p.getName());

      
      GiftGui giftgui = new GiftGui(p, p.getWorld().getName().replace(Variable.world_prefix, ""));
      p.openInventory(giftgui.getInventory());
      return false;
    } 


    
    if (args.length == 1 && (
      args[0].equalsIgnoreCase("home") || args[0].equalsIgnoreCase("h"))) {
      if (Variable.bungee) {
        
        if (!MySQL.alreadyhastheplayerjoin(p.getName()) && !MySQL.alreadyhastheplayerhome(p.getName())) {
          String temp = Variable.Lang_YML.getString("NoCreateOrJoin");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 


        
        if (MySQL.alreadyhastheplayerjoin(p.getName())) {
          
          int amount = 0;
          for (Player count_p : Bukkit.getOnlinePlayers()) {
            if (count_p.getWorld().getName().replace(Variable.world_prefix, "").equalsIgnoreCase(MySQL.getJoinHome(p.getName()))) {
              amount++;
            }
          } 
          
          boolean has_been_load = false;
          if (Main.JavaPlugin.getConfig().getBoolean("MoveWorldAfterUnLoad") && 
            Bukkit.getWorld(Variable.world_prefix + MySQL.getJoinHome(p.getName())) != null) {
            has_been_load = true;
          }


          
          if (Main.JavaPlugin.getConfig().getBoolean("AutoMoveWorldFilesToOther") && !has_been_load && !Variable.wait_to_command.containsKey(p.getName()) && Main.JavaPlugin.getConfig().getBoolean("BungeeCord") && !Variable.has_already_move_world.contains(p.getName()) && amount == 0)
          {
            
            if (Main.JavaPlugin.getConfig().getString("DecideBy").equalsIgnoreCase("Player")) {

              
              if (!MySQL.getLowerstLagServer().equalsIgnoreCase(MySQL.getServer(MySQL.getJoinHome(p.getName()))) && 
                MySQL.getServerAmount(MySQL.getLowerstLagServer()) != Bukkit.getOnlinePlayers().size()) {
                try {
                  Channel.waitToLoad(p, MySQL.getLowerstLagServer(), MySQL.getJoinHome(p.getName()));
                } catch (IOException e) {
                  
                  e.printStackTrace();
                } 
                p.sendMessage(Variable.Lang_YML.getString("StartLowestLagServer"));
                Channel.sendPlayerToServer(p, MySQL.getLowerstLagServer());
                return false;
              
              }

            
            }
            else if (!MySQL.getHighestTPSServer().equalsIgnoreCase(MySQL.getServer(MySQL.getJoinHome(p.getName())))) {
              
              double now = 0.0D;
              if (Bukkit.getVersion().contains("1.7.10")) {
                now = R1_7_10.getTps();
              } else {
                double se1 = Double.valueOf(PlaceholderAPI.setPlaceholders(null, "%server_tps_1%").replace("*", "")).doubleValue();
                double se2 = Double.valueOf(PlaceholderAPI.setPlaceholders(null, "%server_tps_5%").replace("*", "")).doubleValue();
                double se3 = Double.valueOf(PlaceholderAPI.setPlaceholders(null, "%server_tps_15%").replace("*", "")).doubleValue();
                now = (se1 + se2 + se3) / 3.0D;
              } 

              
              if (MySQL.getServerAmount(MySQL.getLowerstLagServer()) != now) {
                try {
                  Channel.waitToLoad(p, MySQL.getHighestTPSServer(), MySQL.getJoinHome(p.getName()));
                } catch (IOException e) {
                  
                  e.printStackTrace();
                } 
                p.sendMessage(Variable.Lang_YML.getString("StartLowestLagServer"));
                Channel.sendPlayerToServer(p, MySQL.getHighestTPSServer());
                return false;
              } 
            } 
          }


          
          if (!MySQL.getJoinServer(p.getName()).equalsIgnoreCase(Main.JavaPlugin.getConfig().getString("Server"))) {
            try {
              Channel.waitDelayToSomeWhere(p, MySQL.getJoinServer(p.getName()), "sh h");
            } catch (IOException e) {
              
              e.printStackTrace();
            } 
            Channel.sendPlayerToServer(p, MySQL.getJoinServer(p.getName()));
            return false;
          } 
        } 
        
        if (MySQL.alreadyhastheplayerhome(p.getName())) {
          
          int amount = 0;
          for (Player count_p : Bukkit.getOnlinePlayers()) {
            if (count_p.getWorld().getName().replace(Variable.world_prefix, "").equalsIgnoreCase(p.getName())) {
              amount++;
            }
          } 
          
          boolean has_been_load = false;
          if (Main.JavaPlugin.getConfig().getBoolean("MoveWorldAfterUnLoad") && 
            Bukkit.getWorld(Variable.world_prefix + p.getName()) != null) {
            has_been_load = true;
          }

          
          if (Main.JavaPlugin.getConfig().getBoolean("AutoMoveWorldFilesToOther") && !has_been_load && !Variable.wait_to_command.containsKey(p.getName()) && Main.JavaPlugin.getConfig().getBoolean("BungeeCord") && !Variable.has_already_move_world.contains(p.getName()) && amount == 0) {
            if (Main.JavaPlugin.getConfig().getString("DecideBy").equalsIgnoreCase("Player")) {


              
              if (!MySQL.getLowerstLagServer().equalsIgnoreCase(MySQL.getServer(p.getName())) && 
                MySQL.getServerAmount(MySQL.getLowerstLagServer()) != Bukkit.getOnlinePlayers().size()) {
                try {
                  Channel.waitToLoad(p, MySQL.getLowerstLagServer(), p.getName());
                } catch (IOException e) {
                  
                  e.printStackTrace();
                } 
                p.sendMessage(Variable.Lang_YML.getString("StartLowestLagServer"));
                Channel.sendPlayerToServer(p, MySQL.getLowerstLagServer());
                return false;
              
              }

            
            }
            else if (!MySQL.getHighestTPSServer().equalsIgnoreCase(MySQL.getServer(p.getName()))) {
              
              double now = 0.0D;
              if (Bukkit.getVersion().contains("1.7.10")) {
                now = R1_7_10.getTps();
              } else {
                double se1 = Double.valueOf(PlaceholderAPI.setPlaceholders(null, "%server_tps_1%").replace("*", "")).doubleValue();
                double se2 = Double.valueOf(PlaceholderAPI.setPlaceholders(null, "%server_tps_5%").replace("*", "")).doubleValue();
                double se3 = Double.valueOf(PlaceholderAPI.setPlaceholders(null, "%server_tps_15%").replace("*", "")).doubleValue();
                now = (se1 + se2 + se3) / 3.0D;
              } 
              
              if (MySQL.getServerAmount(MySQL.getLowerstLagServer()) != now) {
                try {
                  Channel.waitToLoad(p, MySQL.getHighestTPSServer(), p.getName());
                } catch (IOException e) {
                  
                  e.printStackTrace();
                } 
                p.sendMessage(Variable.Lang_YML.getString("StartLowestLagServer"));
                Channel.sendPlayerToServer(p, MySQL.getHighestTPSServer());
                return false;
              } 
            } 
          }



          
          if (!MySQL.getServer(p.getName()).equalsIgnoreCase(Main.JavaPlugin.getConfig().getString("Server"))) {
            try {
              Channel.waitDelayToSomeWhere(p, MySQL.getServer(p.getName()), "sh h");
            } catch (IOException e) {
              
              e.printStackTrace();
            } 
            
            Channel.sendPlayerToServer(p, MySQL.getServer(p.getName()));
            return false;
          } 
        } 
        
        if (MySQL.alreadyhastheplayerjoin(p.getName())) {
          World world = Bukkit.getWorld(Variable.world_prefix + MySQL.getJoinHome(p.getName()));
          if (world == null) {
            WorldCreator creator = new WorldCreator(Variable.world_prefix + MySQL.getJoinHome(p.getName()));
            Variable.create_list_home.add(Variable.world_prefix + MySQL.getJoinHome(p.getName()));
            Bukkit.createWorld(creator);
          } 
          world = Bukkit.getWorld(Variable.world_prefix + MySQL.getJoinHome(p.getName()));
          Location loc = world.getSpawnLocation();
          loc = Util.getNotAir(loc);
          loc.setX(Double.valueOf(MySQL.getX(MySQL.getJoinHome(p.getName()))).doubleValue());
          loc.setY(Double.valueOf(MySQL.getY(MySQL.getJoinHome(p.getName()))).doubleValue());
          loc.setZ(Double.valueOf(MySQL.getZ(MySQL.getJoinHome(p.getName()))).doubleValue());
          p.teleport(loc);
        } else {
          
          World world = Bukkit.getWorld(Variable.world_prefix + p.getName());
          if (world == null) {
            WorldCreator creator = new WorldCreator(Variable.world_prefix + p.getName());
            Variable.create_list_home.add(Variable.world_prefix + p.getName());
            Bukkit.createWorld(creator);
          } 
          world = Bukkit.getWorld(Variable.world_prefix + p.getName());
          
          Location loc = world.getSpawnLocation();
          loc = Util.getNotAir(loc);
          loc.setX(Double.valueOf(MySQL.getX(p.getName())).doubleValue());
          loc.setY(Double.valueOf(MySQL.getY(p.getName())).doubleValue());
          loc.setZ(Double.valueOf(MySQL.getZ(p.getName())).doubleValue());
          p.teleport(loc);
        }
      
      } else {
        
        String what_has_been_join = "";
        boolean has_been_join = false;
        File folder = new File(Variable.Tempf); int b; int j;
        File[] arrayOfFile;
        for (j = (arrayOfFile = folder.listFiles()).length, b = 0; b < j; ) { File temp = arrayOfFile[b];
          String want_to = temp.getPath().replace(Variable.Tempf, "").replace(".yml", "")
            .replace(Variable.file_loc_prefix, "");
          YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(temp);
          for (int i = 0; i < yamlConfiguration.getStringList("OP").size(); i++) {
            String temp_str = yamlConfiguration.getStringList("OP").get(i);
            if (temp_str.equalsIgnoreCase(p.getName())) {
              what_has_been_join = want_to;
              has_been_join = true;
              break;
            } 
          } 
          b++; }
        
        File f2 = new File(Variable.Tempf, String.valueOf(p.getName()) + ".yml");
        if (f2.exists() || has_been_join) {
          
          if (!what_has_been_join.equalsIgnoreCase("")) {
            World world = Bukkit.getWorld(Variable.world_prefix + what_has_been_join);
            if (world == null) {
              
              WorldCreator creator = new WorldCreator(Variable.world_prefix + what_has_been_join);
              Variable.create_list_home.add(Variable.world_prefix + what_has_been_join);
              Bukkit.createWorld(creator);
            } 
            
            world = Bukkit.getWorld(Variable.world_prefix + what_has_been_join);
            Location loc = world.getSpawnLocation();
            loc = Util.getNotAir(loc);
            File tp_set = new File(Variable.Tempf, 
                String.valueOf(world.getName().replace(Variable.world_prefix, "")) + ".yml");
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(tp_set);
            loc.setX(yamlConfiguration.getDouble("X"));
            loc.setY(yamlConfiguration.getDouble("Y"));
            loc.setZ(yamlConfiguration.getDouble("Z"));
            loc = Util.getNotAir(loc);
            p.teleport(loc);
          } else {
            
            World world = Bukkit.getWorld(Variable.world_prefix + p.getName());
            if (world == null) {
              WorldCreator creator = new WorldCreator(Variable.world_prefix + p.getName());
              Variable.create_list_home.add(Variable.world_prefix + p.getName());
              Bukkit.createWorld(creator);
            } 
            world = Bukkit.getWorld(Variable.world_prefix + p.getName());
            Location loc = world.getSpawnLocation();
            loc = Util.getNotAir(loc);
            File tp_set = new File(Variable.Tempf, String.valueOf(p.getName()) + ".yml");
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(tp_set);
            loc.setX(yamlConfiguration.getDouble("X"));
            loc.setY(yamlConfiguration.getDouble("Y"));
            loc.setZ(yamlConfiguration.getDouble("Z"));
            loc = Util.getNotAir(loc);
            p.teleport(loc);
          } 
        } else {
          
          String temp = Variable.Lang_YML.getString("NoCreateOrJoin");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
      } 

      
      return false;
    } 

    
    if (args.length == 1) {
      if (args[0].equalsIgnoreCase("public")) {
        if (!Main.JavaPlugin.getConfig().getBoolean("Permission.CommandUser") && 
          !p.hasPermission("SelfHome.Public") && !p.hasPermission("SelfHome.command.user")) {
          String tip = Variable.Lang_YML.getString("NoPermissionCheck");
          if (tip.contains("<Permission>")) {
            tip = tip.replace("<Permission>", "SelfHome.Public");
          }
          p.sendMessage(tip);
          return false;
        } 


        
        if (Variable.bungee) {
          if (!MySQL.CheckIsAHome(p.getWorld().getName().replace(Variable.world_prefix, ""))) {
            String tip = Variable.Lang_YML.getString("NowIsNotHome");
            p.sendMessage(tip);
            return false;
          } 
        } else {
          File f2 = new File(Variable.Tempf, 
              String.valueOf(p.getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
          if (!f2.exists()) {
            String tip = Variable.Lang_YML.getString("NowIsNotHome");
            p.sendMessage(tip);
            return false;
          } 
        } 
        
        File f2 = new File(Variable.Tempf, String.valueOf(p.getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
        
        if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
          
          if (Variable.bungee) {
            
            if (MySQL.getPublic(p.getWorld().getName().replace(Variable.world_prefix, "")).equals("true")) {
              MySQL.setPublic(p.getWorld().getName().replace(Variable.world_prefix, ""), "false");
              
              String temp = Variable.Lang_YML.getString("DisablePublic");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              for (Player pt : Bukkit.getOnlinePlayers()) {
                if (!Util.Check(pt, 
                    p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue() && 
                  pt.getWorld().getName().replace(Variable.world_prefix, "").equalsIgnoreCase(
                    p.getWorld().getName().replace(Variable.world_prefix, "")))
                {
                  String bekicked = Main.JavaPlugin.getConfig().getString("BeKickedCommand");
                  if (bekicked.contains("<Name>")) {
                    bekicked = bekicked.replace("<Name>", pt.getName());
                  }
                  Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), bekicked);
                  
                  pt.sendMessage(
                      "§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
                  String temp2 = Variable.Lang_YML.getString("BeKicked");
                  pt.sendMessage(temp2);
                  pt.sendMessage(
                      "§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
                }
              
              } 
            } else {
              
              MySQL.setPublic(p.getWorld().getName().replace(Variable.world_prefix, ""), "true");
              String temp = Variable.Lang_YML.getString("EnablePublic");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            } 
          } else {
            
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f2);
            if (yamlConfiguration.getBoolean("Public")) {
              yamlConfiguration.set("Public", Boolean.valueOf(false));
              try {
                yamlConfiguration.save(f2);
              } catch (IOException e) {
                
                e.printStackTrace();
              } 
              String temp = Variable.Lang_YML.getString("DisablePublic");
              
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              
              for (Player pt : Bukkit.getOnlinePlayers()) {
                if (!Util.Check(pt, 
                    p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue() && 
                  pt.getWorld().getName().replace(Variable.world_prefix, "").equalsIgnoreCase(
                    p.getWorld().getName().replace(Variable.world_prefix, "")))
                {
                  String bekicked = Variable.Lang_YML.getString("BeKickedCommand");
                  if (bekicked.contains("<Name>")) {
                    bekicked = bekicked.replace("<Name>", pt.getName());
                  }
                  Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), bekicked);
                  
                  pt.sendMessage(
                      "§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
                  String temp2 = Variable.Lang_YML.getString("BeKicked");
                  pt.sendMessage(temp2);
                  pt.sendMessage(
                      "§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
                }
              
              } 
            } else {
              
              yamlConfiguration.set("Public", Boolean.valueOf(true));
              try {
                yamlConfiguration.save(f2);
              } catch (IOException e) {
                
                e.printStackTrace();
              } 
              String temp = Variable.Lang_YML.getString("EnablePublic");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            } 
          } 
        } else {
          
          String temp = Variable.Lang_YML.getString("NoOwnerAndManagerPermission");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        } 
        return false;
      } 
      
      if (args[0].equalsIgnoreCase("tpset")) {
        if (Variable.bungee) {
          if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
            MySQL.setX(p.getWorld().getName().replace(Variable.world_prefix, ""), 
                String.valueOf(p.getLocation().getX()));
            MySQL.setY(p.getWorld().getName().replace(Variable.world_prefix, ""), 
                String.valueOf(p.getLocation().getY()));
            MySQL.setZ(p.getWorld().getName().replace(Variable.world_prefix, ""), 
                String.valueOf(p.getLocation().getZ()));
            
            String temp = Variable.Lang_YML.getString("TpSetSuccess");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            return false;
          } 
          String temp = Variable.Lang_YML.getString("NoOwnerAndManagerPermission");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
        
        File f2 = new File(Variable.Tempf, 
            String.valueOf(p.getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
        if (!Main.JavaPlugin.getConfig().getBoolean("Permission.CommandUser") && 
          !p.hasPermission("SelfHome.tpset") && !p.hasPermission("SelfHome.command.user")) {
          String tip = Variable.Lang_YML.getString("NoPermissionCheck");
          if (tip.contains("<Permission>")) {
            tip = tip.replace("<Permission>", "SelfHome.TpSet");
          }
          p.sendMessage(tip);
          return false;
        } 

        
        if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
          YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f2);
          
          yamlConfiguration.set("X", Double.valueOf(p.getLocation().getX()));
          yamlConfiguration.set("Y", Double.valueOf(p.getLocation().getY()));
          yamlConfiguration.set("Z", Double.valueOf(p.getLocation().getZ()));
          
          try {
            yamlConfiguration.save(f2);
          } catch (IOException e) {
            
            e.printStackTrace();
          }
          

          
          String temp = Variable.Lang_YML.getString("TpSetSuccess");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        } else {
          
          String temp = Variable.Lang_YML.getString("NoOwnerAndManagerPermission");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 


        
        return false;
      }



      
      if (args[0].equalsIgnoreCase("flower")) {
        if (sender instanceof Player)
        {
          
          if (!p.getName().equalsIgnoreCase(p.getWorld().getName().replace(Variable.world_prefix, ""))) {
            
            int set_flower = Main.JavaPlugin.getConfig().getInt("MaxFlowers");
            for (int i = Main.JavaPlugin.getConfig().getInt("MaxFlowers") + 100; i > 0; i--) {
              if (p.hasPermission("SelfHome.Flowers." + i) && !p.isOp()) {
                set_flower = i;
                
                break;
              } 
            } 
            if (Variable.flowers_list.containsKey(p.getName())) {
              int has_give_amount = ((Integer)Variable.flowers_list.get(p.getName())).intValue();

              
              if (has_give_amount >= set_flower) {
                String temp = Variable.Lang_YML.getString("FlowersMax");
                if (temp.contains("<Max>")) {
                  temp = temp.replace("<Max>", String.valueOf(set_flower));
                }
                p.sendMessage(temp);
                return false;
              } 

              
              Variable.flowers_list.put(p.getName(), Integer.valueOf(has_give_amount + 1));
              Home home = HomeAPI.getHome(p.getWorld().getName().replace(Variable.world_prefix, ""));
              try {
                home.setFlowers(home.getFlowers() + 1);
              } catch (IOException e) {
                
                e.printStackTrace();
              } 
              String temp = Variable.Lang_YML.getString("FlowersAdd");
              if (temp.contains("<Name>")) {
                temp = temp.replace("<Name>", p.getWorld().getName().replace(Variable.world_prefix, ""));
              }
              if (temp.contains("<Now>")) {
                temp = temp.replace("<Now>", String.valueOf(Variable.flowers_list.get(p.getName())));
              }
              if (temp.contains("<Max>")) {
                temp = temp.replace("<Max>", String.valueOf(Main.JavaPlugin.getConfig().getInt("MaxFlowers")));
              }
              p.sendMessage(temp);

              
              String temp2 = Variable.Lang_YML.getString("FlowersAddToOwnerAndOP");
              if (temp2.contains("<Player>")) {
                temp2 = temp2.replace("<Player>", p.getName());
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
            else {
              
              Variable.flowers_list.put(p.getName(), Integer.valueOf(1));
              Home home = HomeAPI.getHome(p.getWorld().getName().replace(Variable.world_prefix, ""));
              try {
                home.setFlowers(1);
              } catch (IOException e) {
                
                e.printStackTrace();
              } 

              
              String temp = Variable.Lang_YML.getString("FlowersAdd");
              if (temp.contains("<Name>")) {
                temp = temp.replace("<Name>", p.getWorld().getName().replace(Variable.world_prefix, ""));
              }
              if (temp.contains("<Now>")) {
                temp = temp.replace("<Now>", "1");
              }
              if (temp.contains("<Max>")) {
                temp = temp.replace("<Max>", String.valueOf(Main.JavaPlugin.getConfig().getInt("MaxFlowers")));
              }
              p.sendMessage(temp);

              
              String temp2 = Variable.Lang_YML.getString("FlowersAddToOwnerAndOP");
              if (temp2.contains("<Player>")) {
                temp2 = temp2.replace("<Player>", p.getName());
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
            
            p.sendMessage(Variable.Lang_YML.getString("FlowersMySelf"));
            return false;
          } 
        }

        
        return false;
      } 

      
      if (args[0].equalsIgnoreCase("MobSpawn")) {
        if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
          if (!p.hasPermission("SelfHome.MobSpawn")) {
            String tip = Variable.Lang_YML.getString("NoPermissionCheck");
            if (tip.contains("<Permission>")) {
              tip = tip.replace("<Permission>", "SelfHome.MobSpawn");
            }
            p.sendMessage(tip);
            return false;
          } 
          World world = p.getWorld();
          if (world.getGameRuleValue("doMobSpawning").equalsIgnoreCase("false")) {
            world.setGameRuleValue("doMobSpawning", "true");
            
            if(Variable.hook_multiverseCore == true) {
 			   MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		      	MVWorldManager mv_m = mvcore.getMVWorldManager();
            	MultiverseWorld mv = mv_m.getMVWorld(p.getLocation().getWorld().getName());
            	mv.setAllowMonsterSpawn(true);
            }
            
            
            if (world.getDifficulty() == Difficulty.PEACEFUL) {
              world.setDifficulty(Difficulty.HARD);
              if(Variable.hook_multiverseCore == true) {
   			   MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		      	MVWorldManager mv_m = mvcore.getMVWorldManager();
              	MultiverseWorld mv = mv_m.getMVWorld(p.getLocation().getWorld().getName());
              	mv.setDifficulty(Difficulty.HARD);
              }
            }
            
            String temp = Variable.Lang_YML.getString("EnableMobSpawn");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          }
          else if (world.getGameRuleValue("doMobSpawning").equalsIgnoreCase("true")) {
            world.setGameRuleValue("doMobSpawning", "false");
            if (Main.JavaPlugin.getConfig().getString("Difficulty").equalsIgnoreCase("Easy")) {
              world.setDifficulty(Difficulty.EASY);
              if(Variable.hook_multiverseCore == true) {
   			   MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		      	MVWorldManager mv_m = mvcore.getMVWorldManager();
                	MultiverseWorld mv = mv_m.getMVWorld(p.getLocation().getWorld().getName());
                	mv.setDifficulty(Difficulty.EASY);
                }
            } else if (Main.JavaPlugin.getConfig().getString("Difficulty").equalsIgnoreCase("Normal")) {
              world.setDifficulty(Difficulty.NORMAL);
              if(Variable.hook_multiverseCore == true) {
   			   MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		      	MVWorldManager mv_m = mvcore.getMVWorldManager();
                	MultiverseWorld mv = mv_m.getMVWorld(p.getLocation().getWorld().getName());
                	mv.setDifficulty(Difficulty.NORMAL);
                }
            } else if (Main.JavaPlugin.getConfig().getString("Difficulty").equalsIgnoreCase("Hard")) {
              world.setDifficulty(Difficulty.HARD);
              if(Variable.hook_multiverseCore == true) {
   			   MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		      	MVWorldManager mv_m = mvcore.getMVWorldManager();
                	MultiverseWorld mv = mv_m.getMVWorld(p.getLocation().getWorld().getName());
                	mv.setDifficulty(Difficulty.HARD);
                }
            } else if (Main.JavaPlugin.getConfig().getString("Difficulty").equalsIgnoreCase("Peaceful")) {
              world.setDifficulty(Difficulty.PEACEFUL);
              if(Variable.hook_multiverseCore == true) {
   			   MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		      	MVWorldManager mv_m = mvcore.getMVWorldManager();
                	MultiverseWorld mv = mv_m.getMVWorld(p.getLocation().getWorld().getName());
                	mv.setDifficulty(Difficulty.PEACEFUL);
                }
            } 
            
            String temp = Variable.Lang_YML.getString("DisableMobSpawn");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          } 

          
          return false;
        } 
        String temp = Variable.Lang_YML.getString("NoOwnerAndManagerPermission");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        return false;
      } 

      
      if (args[0].equalsIgnoreCase("pvp")) {
        File f2 = new File(Variable.Tempf, String.valueOf(p.getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
        if (!Main.JavaPlugin.getConfig().getBoolean("Permission.CommandUser") && 
          !p.hasPermission("SelfHome.PVP") && !p.hasPermission("SelfHome.command.user")) {
          String tip = Variable.Lang_YML.getString("NoPermissionCheck");
          if (tip.contains("<Permission>")) {
            tip = tip.replace("<Permission>", "SelfHome.PVP");
          }
          p.sendMessage(tip);
          return false;
        } 


        
        if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
          
          if (Variable.bungee) {
            
            if (MySQL.getPVP(p.getWorld().getName().replace(Variable.world_prefix, ""))
              .equalsIgnoreCase("true")) {
              MySQL.setpvp(p.getWorld().getName().replace(Variable.world_prefix, ""), "false");
              if(Variable.hook_multiverseCore == true) {
   			   MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		      	MVWorldManager mv_m = mvcore.getMVWorldManager();
              	MultiverseWorld mv = mv_m.getMVWorld(p.getLocation().getWorld().getName());
              	mv.setPVPMode(false);
              }
              String temp = Variable.Lang_YML.getString("DisablePVP");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            }
            else {
              
              MySQL.setpvp(p.getWorld().getName().replace(Variable.world_prefix, ""), "true");
              
              if(Variable.hook_multiverseCore == true) {
   			   MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		      	MVWorldManager mv_m = mvcore.getMVWorldManager();
                	MultiverseWorld mv = mv_m.getMVWorld(p.getLocation().getWorld().getName());
                	mv.setPVPMode(true);
                }
              
              String temp = Variable.Lang_YML.getString("EnablePVP");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            }
          
          } else {
            
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f2);
            if (yamlConfiguration.getBoolean("pvp")) {
              yamlConfiguration.set("pvp", Boolean.valueOf(false));
              try {
                yamlConfiguration.save(f2);
              } catch (IOException e) {
                
                e.printStackTrace();
              }
              if(Variable.hook_multiverseCore == true) {
   			   MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		      	MVWorldManager mv_m = mvcore.getMVWorldManager();
              	MultiverseWorld mv = mv_m.getMVWorld(p.getLocation().getWorld().getName());
              	mv.setPVPMode(false);
              }
              String temp = Variable.Lang_YML.getString("DisablePVP");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            } else {
              
              yamlConfiguration.set("pvp", Boolean.valueOf(true));
              try {
                yamlConfiguration.save(f2);
              } catch (IOException e) {
                
                e.printStackTrace();
              } 
              if(Variable.hook_multiverseCore == true) {
   			   MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		      	MVWorldManager mv_m = mvcore.getMVWorldManager();
              	MultiverseWorld mv = mv_m.getMVWorld(p.getLocation().getWorld().getName());
              	mv.setPVPMode(true);
              }
              String temp = Variable.Lang_YML.getString("EnablePVP");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            }
          
          } 
        } else {
          
          String temp = Variable.Lang_YML.getString("NoOwnerAndManagerPermission");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        } 
        return false;
      } 
      
      if (args[0].equalsIgnoreCase("pickup")) {
        File f2 = new File(Variable.Tempf, String.valueOf(p.getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
        if (!Main.JavaPlugin.getConfig().getBoolean("Permission.CommandUser") && 
          !p.hasPermission("SelfHome.PickUp") && !p.hasPermission("SelfHome.command.user")) {
          String tip = Variable.Lang_YML.getString("NoPermissionCheck");
          if (tip.contains("<Permission>")) {
            tip = tip.replace("<Permission>", "SelfHome.PickUp");
          }
          p.sendMessage(tip);
          return false;
        } 


        
        if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
          
          if (Variable.bungee) {
            
            if (MySQL.getpickup(p.getWorld().getName().replace(Variable.world_prefix, ""))
              .equalsIgnoreCase("true")) {
              MySQL.setpickup(p.getWorld().getName().replace(Variable.world_prefix, ""), "false");
              String temp = Variable.Lang_YML.getString("DisablePickup");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            } else {
              MySQL.setpickup(p.getWorld().getName().replace(Variable.world_prefix, ""), "true");
              String temp = Variable.Lang_YML.getString("EnablePickup");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            }
          
          }
          else {
            
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f2);
            if (yamlConfiguration.getBoolean("pickup")) {
              yamlConfiguration.set("pickup", Boolean.valueOf(false));
              try {
                yamlConfiguration.save(f2);
              } catch (IOException e) {
                
                e.printStackTrace();
              } 
              String temp = Variable.Lang_YML.getString("DisablePickup");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            } else {
              
              yamlConfiguration.set("pickup", Boolean.valueOf(true));
              try {
                yamlConfiguration.save(f2);
              } catch (IOException e) {
                
                e.printStackTrace();
              } 
              String temp = Variable.Lang_YML.getString("EnablePickup");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            }
          
          } 
        } else {
          
          String temp = Variable.Lang_YML.getString("NoOwnerPermission");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        } 
        return false;
      } 
      
      if (args[0].equalsIgnoreCase("drop")) {
        File f2 = new File(Variable.Tempf, String.valueOf(p.getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
        if (!Main.JavaPlugin.getConfig().getBoolean("Permission.CommandUser") && 
          !p.hasPermission("SelfHome.Drop") && !p.hasPermission("SelfHome.command.user")) {
          String tip = Variable.Lang_YML.getString("NoPermissionCheck");
          if (tip.contains("<Permission>")) {
            tip = tip.replace("<Permission>", "SelfHome.Drop");
          }
          p.sendMessage(tip);
          return false;
        } 



        
        if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
          
          if (Variable.bungee) {
            
            if (MySQL.getdropitem(p.getWorld().getName().replace(Variable.world_prefix, ""))
              .equalsIgnoreCase("true")) {
              MySQL.setdropitem(p.getWorld().getName().replace(Variable.world_prefix, ""), "false");
              String temp = Variable.Lang_YML.getString("DisableDrop");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            } else {
              MySQL.setdropitem(p.getWorld().getName().replace(Variable.world_prefix, ""), "true");
              String temp = Variable.Lang_YML.getString("EnableDrop");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            }
          
          } else {
            
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f2);
            if (yamlConfiguration.getBoolean("drop")) {
              yamlConfiguration.set("drop", Boolean.valueOf(false));
              try {
                yamlConfiguration.save(f2);
              } catch (IOException e) {
                
                e.printStackTrace();
              } 
              String temp = Variable.Lang_YML.getString("DisableDrop");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            } else {
              
              yamlConfiguration.set("drop", Boolean.valueOf(true));
              try {
                yamlConfiguration.save(f2);
              } catch (IOException e) {
                
                e.printStackTrace();
              } 
              String temp = Variable.Lang_YML.getString("EnableDrop");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            }
          
          } 
        } else {
          
          String temp = Variable.Lang_YML.getString("NoOwnerPermission");
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        } 
        return false;
      } 
    } 



    
    if (args.length == 2)
    {
      if (args[0].equalsIgnoreCase("GAMEMODE")) {
        if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
          
          if (args[1].equalsIgnoreCase("EASY")) {
            if (!p.hasPermission("SelfHome.GAMEMODE.EASY")) {
              String tip = Variable.Lang_YML.getString("NoPermissionCheck");
              if (tip.contains("<Permission>")) {
                tip = tip.replace("<Permission>", "SelfHome.GAMEMODE.EASY");
              }
              p.sendMessage(tip);
              return false;
            } 
            p.getWorld().setDifficulty(Difficulty.EASY);
            String temp = Variable.Lang_YML.getString("DifficultyModify");
            if (temp.contains("<Mode>")) {
              temp = temp.replace("<Mode>", "EASY");
            }
            p.sendMessage(temp);
            return false;
          }  if (args[1].equalsIgnoreCase("HARD")) {
            if (!p.hasPermission("SelfHome.GAMEMODE.HARD")) {
              String tip = Variable.Lang_YML.getString("NoPermissionCheck");
              if (tip.contains("<Permission>")) {
                tip = tip.replace("<Permission>", "SelfHome.GAMEMODE.HARD");
              }
              p.sendMessage(tip);
              return false;
            } 
            p.getWorld().setDifficulty(Difficulty.HARD);
            String temp = Variable.Lang_YML.getString("DifficultyModify");
            if (temp.contains("<Mode>")) {
              temp = temp.replace("<Mode>", "HARD");
            }
            p.sendMessage(temp);
            return false;
          }  if (args[1].equalsIgnoreCase("PEACEFUL")) {
            if (!p.hasPermission("SelfHome.GAMEMODE.PEACEFUL")) {
              String tip = Variable.Lang_YML.getString("NoPermissionCheck");
              if (tip.contains("<Permission>")) {
                tip = tip.replace("<Permission>", "SelfHome.GAMEMODE.PEACEFUL");
              }
              p.sendMessage(tip);
              return false;
            } 
            p.getWorld().setDifficulty(Difficulty.PEACEFUL);
            String temp = Variable.Lang_YML.getString("DifficultyModify");
            if (temp.contains("<Mode>")) {
              temp = temp.replace("<Mode>", "PEACEFUL");
            }
            p.sendMessage(temp);
            return false;
          }
        
        } else {
          
          String temp = Variable.Lang_YML.getString("NoOwnerAndManagerPermission");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
      }
    }






    
    if (args.length == 2 && (
      args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("visit") || args[0].equalsIgnoreCase("v"))) {
      if (!Main.JavaPlugin.getConfig().getBoolean("Permission.Visit") && 
        !p.hasPermission("SelfHome.Visit")) {
        String tip = Variable.Lang_YML.getString("NoPermissionCheck");
        if (tip.contains("<Permission>")) {
          tip = tip.replace("<Permission>", "SelfHome.Visit");
        }
        p.sendMessage(tip);
        return false;
      } 


      
      if (Variable.bungee) {
        
        if (Util.CheckIsHome(args[1]))
        {
          int amount = 0;
          for (Player count_p : Bukkit.getOnlinePlayers()) {
            if (count_p.getWorld().getName().replace(Variable.world_prefix, "").equalsIgnoreCase(args[1])) {
              amount++;
            }
          } 

          
          boolean has_been_load = false;
          if (Main.JavaPlugin.getConfig().getBoolean("MoveWorldAfterUnLoad") && 
            Bukkit.getWorld(Variable.world_prefix + args[1]) != null) {
            has_been_load = true;
          }



          
          if (Main.JavaPlugin.getConfig().getBoolean("AutoMoveWorldFilesToOther") && !has_been_load && !Variable.wait_to_command.containsKey(p.getName()) && Main.JavaPlugin.getConfig().getBoolean("BungeeCord") && !Variable.has_already_move_world.contains(p.getName()) && amount == 0) {
            if (Main.JavaPlugin.getConfig().getString("DecideBy").equalsIgnoreCase("Player")) {

              
              if (!MySQL.getLowerstLagServer().equalsIgnoreCase(MySQL.getServer(args[1])) && 
                MySQL.getServerAmount(MySQL.getLowerstLagServer()) != Bukkit.getOnlinePlayers().size()) {
                try {
                  Channel.waitToLoad(p, MySQL.getLowerstLagServer(), args[1]);
                } catch (IOException e) {
                  
                  e.printStackTrace();
                } 
                p.sendMessage(Variable.Lang_YML.getString("StartLowestLagServer"));
                Channel.sendPlayerToServer(p, MySQL.getLowerstLagServer());
                return false;
              
              }

            
            }
            else if (!MySQL.getHighestTPSServer().equalsIgnoreCase(MySQL.getServer(args[1]))) {
              double now = 0.0D;
              if (Bukkit.getVersion().contains("1.7.10")) {
                now = R1_7_10.getTps();
              } else {
                double se1 = Double.valueOf(PlaceholderAPI.setPlaceholders(null, "%server_tps_1%").replace("*", "")).doubleValue();
                double se2 = Double.valueOf(PlaceholderAPI.setPlaceholders(null, "%server_tps_5%").replace("*", "")).doubleValue();
                double se3 = Double.valueOf(PlaceholderAPI.setPlaceholders(null, "%server_tps_15%").replace("*", "")).doubleValue();
                now = (se1 + se2 + se3) / 3.0D;
              } 
              if (MySQL.getServerAmount(MySQL.getLowerstLagServer()) != now) {
                try {
                  Channel.waitToLoad(p, MySQL.getHighestTPSServer(), args[1]);
                } catch (IOException e) {
                  
                  e.printStackTrace();
                } 
                p.sendMessage(Variable.Lang_YML.getString("StartLowestLagServer"));
                Channel.sendPlayerToServer(p, MySQL.getHighestTPSServer());
                return false;
              } 
            } 
          }



          
          if (MySQL.getServer(args[1]).equalsIgnoreCase(Main.JavaPlugin.getConfig().getString("Server"))) {
            World world = Bukkit.getWorld(Variable.world_prefix + args[1]);
            WorldCreator creator = new WorldCreator(Variable.world_prefix + args[1]);
            Variable.create_list_home.add(Variable.world_prefix + args[1]);
            Bukkit.createWorld(creator);
            world = Bukkit.getWorld(Variable.world_prefix + args[1]);
            Location loc = world.getSpawnLocation();
            loc = Util.getNotAir(loc);
            loc.setX(Double.valueOf(MySQL.getX(args[1])).doubleValue());
            loc.setY(Double.valueOf(MySQL.getY(args[1])).doubleValue());
            loc.setZ(Double.valueOf(MySQL.getZ(args[1])).doubleValue());
            
            p.teleport(loc);
          } else {

            
            try {
              
              Channel.waitDelayToSomeWhere(p, MySQL.getServer(args[1]), "sh v " + args[1]);
            } catch (IOException e) {
              
              e.printStackTrace();
            } 
            
            Channel.sendPlayerToServer(p, MySQL.getServer(args[1]));


          
          }



        
        }
        else
        {

          
          String temp = Variable.Lang_YML.getString("TpNotExist");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        }
      
      } else {
        
        File f = new File(Variable.Tempf, String.valueOf(args[1]) + ".yml");
        if (f.exists()) {
          YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f);
          World world = Bukkit.getWorld(Variable.world_prefix + args[1]);
          WorldCreator creator = new WorldCreator(Variable.world_prefix + args[1]);
          Variable.create_list_home.add(Variable.world_prefix + args[1]);
          Bukkit.createWorld(creator);
          world = Bukkit.getWorld(Variable.world_prefix + args[1]);
          Location loc = world.getSpawnLocation();
          loc = Util.getNotAir(loc);
          loc.setX(yamlConfiguration.getDouble("X"));
          loc.setY(yamlConfiguration.getDouble("Y"));
          loc.setZ(yamlConfiguration.getDouble("Z"));
          
          p.teleport(loc);
        } else {
          
          String temp = Variable.Lang_YML.getString("TpNotExist");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        } 
      } 

      
      return false;
    } 

    
    if (args.length == 2 && (
      args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("i"))) {
      String Name = args[1];
      
      if (Variable.bungee) {
        if (!Main.JavaPlugin.getConfig().getBoolean("Permission.CommandUser") && 
          !p.hasPermission("SelfHome.Invite") && !p.hasPermission("SelfHome.command.user")) {
          String tip = Variable.Lang_YML.getString("NoPermissionCheck");
          if (tip.contains("<Permission>")) {
            tip = tip.replace("<Permission>", "SelfHome.Invite");
          }
          p.sendMessage(tip);
          return false;
        } 


        
        if (!MySQL.alreadyhastheplayerhome(p.getName())) {
          String temp = Variable.Lang_YML.getString("NoHome");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
        
        if (args[1].equalsIgnoreCase(p.getName())) {
          String temp = Variable.Lang_YML.getString("InviteMySelf");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
        
        List<String> blacklist = MySQL.getDenys(p.getName());
        if (blacklist == null) {
          blacklist = new ArrayList<>();
        }
        for (int i = 0; i < blacklist.size(); i++) {
          if (((String)blacklist.get(i)).equalsIgnoreCase(args[1])) {
            String temp = Variable.Lang_YML.getString("HasAlreadyInBlack");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            return false;
          } 
        } 
        
        List<String> memberlist = MySQL.getMembers(p.getName());
        if (memberlist == null) {
          memberlist = new ArrayList<>();
        }
        for (int i = 0; i < memberlist.size(); i++) {
          if (((String)memberlist.get(i)).equalsIgnoreCase(args[1])) {
            String temp = Variable.Lang_YML.getString("HasAlreadyTrust");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            return false;
          } 
        } 
        
        List<String> OP_List = MySQL.getOP(p.getName());
        if (OP_List == null) {
          OP_List = new ArrayList<>();
        }
        Boolean CheckSame = Boolean.valueOf(false);
        for (int i = 0; i < OP_List.size(); i++) {
          if (((String)OP_List.get(i)).equalsIgnoreCase(Name)) {
            CheckSame = Boolean.valueOf(true);
            String temp = Variable.Lang_YML.getString("HasAlreadyOP");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            return false;
          } 
        } 
        
        if (!CheckSame.booleanValue())
        {
          if (MySQL.alreadyhastheplayerhome(args[1])) {
            String temp = Variable.Lang_YML.getString("InvitePlayerWhoHasCreateHome");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            return false;
          } 
          
          if (MySQL.alreadyhastheplayerjoin(args[1])) {
            String temp = Variable.Lang_YML.getString("InvitePlayerWhoHasJoinOthers");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            return false;
          } 
          
          if (Variable.invite_list.containsKey(p.getName())) {
            String temp = Variable.Lang_YML.getString("HasInInviteCooldown");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            return false;
          } 
          
          if (Variable.invite_list.containsValue(args[1])) {
            String temp = Variable.Lang_YML.getString("InvitePlayerWhoHasBeenAlreadyInvited");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            
            return false;
          } 
          
          if (OP_List.size() + 2 > Main.JavaPlugin.getConfig().getInt("MaxOP")) {
            
            int max_player = Main.JavaPlugin.getConfig().getInt("MaxOP");
            for (int i = Main.JavaPlugin.getConfig().getInt("MaxOP") * 3; i > Main.JavaPlugin
              .getConfig().getInt("MaxOP"); i--) {
              if (p.hasPermission("SelfHome.MaxOP." + i)) {
                max_player = i;
                
                break;
              } 
            } 
            if (OP_List.size() + 2 > max_player) {
              String temp = Variable.Lang_YML.getString("ReachMaxOP");
              if (temp.contains("<MaxAmount>")) {
                temp = temp.replace("<MaxAmount>", String.valueOf(max_player));
              }
              
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            } else {
              
              Player be_invite = Bukkit.getPlayer(args[1]);
              if (be_invite == null) {
                String temp = Variable.Lang_YML.getString("NoPlayerExist");
                sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                sender.sendMessage(temp);
                sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
                return false;
              } 
              
              Variable.invite_list.put(p.getName(), args[1]);
              invite_guoqi(p);
              
              String temp = Variable.Lang_YML.getString("SendInviteToPlayer");
              if (temp.contains("<Name>")) {
                temp = temp.replace("<Name>", args[1]);
              }
              
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              
              if (be_invite != null) {
                String temp2 = Variable.Lang_YML.getString("InviteMessage");
                if (temp2.contains("<player>")) {
                  temp2 = temp2.replace("<player>", p.getName());
                }
                
                if(Variable.has_no_click_message) {
                	 be_invite.sendMessage(temp2);	
                }else {
                    TextComponent Click_Message = new TextComponent(temp2);
                    Click_Message
                      .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sh accept"));
                    be_invite.spigot().sendMessage((BaseComponent)Click_Message);	
                }
                

              } 
            } 

            
            return false;
          } 
          
          Player be_invite = Bukkit.getPlayer(args[1]);
          if (be_invite == null) {
            String temp = Variable.Lang_YML.getString("NoPlayerExist");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            return false;
          } 
          
          Variable.invite_list.put(p.getName(), args[1]);
          invite_guoqi(p);
          
          String temp = Variable.Lang_YML.getString("SendInviteToPlayer");
          if (temp.contains("<Name>")) {
            temp = temp.replace("<Name>", args[1]);
          }
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          
          if (be_invite != null) {
            String temp2 = Variable.Lang_YML.getString("InviteMessage");
            if (temp2.contains("<player>")) {
              temp2 = temp2.replace("<player>", p.getName());
            }
            
            if(Variable.has_no_click_message) {
            	be_invite.sendMessage(temp2);
           }else {
               TextComponent Click_Message = new TextComponent(temp2);
               Click_Message
                 .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sh accept"));
               be_invite.spigot().sendMessage((BaseComponent)Click_Message);
           }
            
            

          
          }
        
        }
      
      }
      else {
        
        File f2 = new File(Variable.Tempf, String.valueOf(p.getName()) + ".yml");
        if (f2.exists()) {
          if (!Main.JavaPlugin.getConfig().getBoolean("Permission.CommandUser") && 
            !p.hasPermission("SelfHome.Invite") && !p.hasPermission("SelfHome.command.user")) {
            String tip = Variable.Lang_YML.getString("NoPermissionCheck");
            if (tip.contains("<Permission>")) {
              tip = tip.replace("<Permission>", "SelfHome.Invite");
            }
            p.sendMessage(tip);
            return false;
          } 


          
          YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f2);
          
          if (args[1].equalsIgnoreCase(p.getName())) {
            String temp = Variable.Lang_YML.getString("InviteMySelf");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            return false;
          } 
          
          List<String> blacklist = yamlConfiguration.getStringList("Denys");
          if (blacklist == null) {
            blacklist = new ArrayList<>();
          }
          for (int i = 0; i < blacklist.size(); i++) {
            if (((String)blacklist.get(i)).equalsIgnoreCase(args[1])) {
              String temp = Variable.Lang_YML.getString("HasAlreadyInBlack");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              return false;
            } 
          } 
          
          List<String> memberlist = yamlConfiguration.getStringList("Members");
          if (memberlist == null) {
            memberlist = new ArrayList<>();
          }
          for (int i = 0; i < memberlist.size(); i++) {
            if (((String)memberlist.get(i)).equalsIgnoreCase(args[1])) {
              String temp = Variable.Lang_YML.getString("HasAlreadyTrust");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              return false;
            } 
          } 
          
          List<String> OP_List = yamlConfiguration.getStringList("OP");
          if (OP_List == null) {
            OP_List = new ArrayList<>();
          }
          Boolean CheckSame = Boolean.valueOf(false);
          for (int i = 0; i < OP_List.size(); i++) {
            if (((String)OP_List.get(i)).equalsIgnoreCase(Name)) {
              CheckSame = Boolean.valueOf(true);
              String temp = Variable.Lang_YML.getString("HasAlreadyOP");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              return false;
            } 
          } 
          
          if (!CheckSame.booleanValue())
          {
            File be_invite_file = new File(Variable.Tempf, String.valueOf(args[1]) + ".yml");
            if (be_invite_file.exists()) {
              String temp = Variable.Lang_YML.getString("InvitePlayerWhoHasCreateHome");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              return false;
            } 
            boolean has_been_join = false;
            File folder = new File(Variable.Tempf); int b; int j; File[] arrayOfFile;
            for (j = (arrayOfFile = folder.listFiles()).length, b = 0; b < j; ) { File temp = arrayOfFile[b];
              String want_to = temp.getPath().replace(Variable.Tempf, "").replace(".yml", "")
                .replace(Variable.file_loc_prefix, "");
              YamlConfiguration yamlConfiguration1 = 
                YamlConfiguration.loadConfiguration(temp);
              for (int i = 0; i < yamlConfiguration1.getStringList("OP").size(); i++) {
                String temp_str = yamlConfiguration1.getStringList("OP").get(i);
                if (temp_str.equalsIgnoreCase(args[1])) {
                  has_been_join = true;
                  break;
                } 
              } 
              b++; }
            
            if (has_been_join) {
              String temp = Variable.Lang_YML.getString("InvitePlayerWhoHasJoinOthers");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              return false;
            } 
            
            if (Variable.invite_list.containsKey(p.getName())) {
              String temp = Variable.Lang_YML.getString("HasInInviteCooldown");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              return false;
            } 
            
            if (Variable.invite_list.containsValue(args[1])) {
              String temp = Variable.Lang_YML.getString("InvitePlayerWhoHasBeenAlreadyInvited");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              
              return false;
            } 
            
            if (OP_List.size() + 2 > Main.JavaPlugin.getConfig().getInt("MaxOP")) {
              
              int max_player = Main.JavaPlugin.getConfig().getInt("MaxOP");
              for (int i = Main.JavaPlugin.getConfig().getInt("MaxOP") * 3; i > Main.JavaPlugin
                .getConfig().getInt("MaxOP"); i--) {
                if (p.hasPermission("SelfHome.MaxOP." + i)) {
                  max_player = i;
                  
                  break;
                } 
              } 
              if (OP_List.size() + 2 > max_player) {
                String temp = Variable.Lang_YML.getString("ReachMaxOP");
                if (temp.contains("<MaxAmount>")) {
                  temp = temp.replace("<MaxAmount>", String.valueOf(max_player));
                }
                
                sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                sender.sendMessage(temp);
                sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              } else {
                
                Player be_invite = Bukkit.getPlayer(args[1]);
                if (be_invite == null) {
                  String temp = Variable.Lang_YML.getString("NoPlayerExist");
                  sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                  sender.sendMessage(temp);
                  sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
                  return false;
                } 
                
                Variable.invite_list.put(p.getName(), args[1]);
                invite_guoqi(p);
                
                String temp = Variable.Lang_YML.getString("SendInviteToPlayer");
                if (temp.contains("<Name>")) {
                  temp = temp.replace("<Name>", args[1]);
                }
                
                sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                sender.sendMessage(temp);
                sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
                
                if (be_invite != null) {
                  String temp2 = Variable.Lang_YML.getString("InviteMessage");
                  if (temp2.contains("<player>")) {
                    temp2 = temp2.replace("<player>", p.getName());
                  }
                  
                  if(Variable.has_no_click_message) {
                	  be_invite.sendMessage(temp2);
                  }else {
                      TextComponent Click_Message = new TextComponent(temp2);
                      Click_Message.setClickEvent(
                          new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sh accept"));
                      be_invite.spigot().sendMessage((BaseComponent)Click_Message);
                  }
                  

                } 
              } 

              
              return false;
            } 
            
            Player be_invite = Bukkit.getPlayer(args[1]);
            if (be_invite == null) {
              String temp = Variable.Lang_YML.getString("NoPlayerExist");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              return false;
            } 
            
            Variable.invite_list.put(p.getName(), args[1]);
            invite_guoqi(p);
            
            String temp = Variable.Lang_YML.getString("SendInviteToPlayer");
            if (temp.contains("<Name>")) {
              temp = temp.replace("<Name>", args[1]);
            }
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            
            if (be_invite != null) {
              String temp2 = Variable.Lang_YML.getString("InviteMessage");
              if (temp2.contains("<player>")) {
                temp2 = temp2.replace("<player>", p.getName());
              }
              
              if(Variable.has_no_click_message) {
            	  be_invite.sendMessage(temp2);
              }else {
                  TextComponent Click_Message = new TextComponent(temp2);
                  Click_Message
                    .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sh accept"));
                  be_invite.spigot().sendMessage((BaseComponent)Click_Message);
              }
              

            }
          
          }
        
        }
        else {
          
          String temp = Variable.Lang_YML.getString("NoHome");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
      } 

      
      return false;
    } 


    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("accept")) {
      
      if (Variable.bungee) {
        if (!Variable.invite_list.containsValue(p.getName())) {
          String temp = Variable.Lang_YML.getString("HasNoOthersInvite");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
        String who_invite = "";
        for (String Key : Variable.invite_list.keySet()) {
          if (((String)Variable.invite_list.get(Key)).equalsIgnoreCase(p.getName())) {
            who_invite = Key;
            break;
          } 
        } 
        if (!Util.CheckIsHome(who_invite)) {
          String temp = Variable.Lang_YML.getString("InviteAcceptNoExistHome");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
        
        List<String> OP = MySQL.getOP(who_invite);
        String result = MySQL.getListStringSpiltByDot(OP);
        if (result == null || result.equalsIgnoreCase("")) {
          result = p.getName();
        } else {
          result = String.valueOf(result) + "," + p.getName();
        } 
        
        MySQL.setOP(who_invite, result);
        
        String temp2 = Variable.Lang_YML.getString("SuccessJoinOthers");
        if (temp2.contains("<Name>")) {
          temp2 = temp2.replace("<Name>", who_invite);
        }
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp2);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        Player who_invite_player = Bukkit.getPlayer(who_invite);
        if (who_invite_player != null) {
          String temp = Variable.Lang_YML.getString("SuccessInviteOther");
          if (temp.contains("<Name>")) {
            temp = temp.replace("<Name>", p.getName());
          }
          who_invite_player
            .sendMessage("§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
          who_invite_player.sendMessage(temp);
          who_invite_player
            .sendMessage("§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
        } 
        Variable.invite_list.remove(who_invite);
      } else {
        if (!Variable.invite_list.containsValue(p.getName())) {
          String temp = Variable.Lang_YML.getString("HasNoOthersInvite");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
        String who_invite = "";
        for (String Key : Variable.invite_list.keySet()) {
          if (((String)Variable.invite_list.get(Key)).equalsIgnoreCase(p.getName())) {
            who_invite = Key;
            break;
          } 
        } 
        File f2 = new File(Variable.Tempf, String.valueOf(who_invite) + ".yml");
        if (!f2.exists()) {
          String temp = Variable.Lang_YML.getString("InviteAcceptNoExistHome");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f2);
        List<String> OP = yamlConfiguration.getStringList("OP");
        if (OP == null) {
          OP = new ArrayList<>();
        }
        OP.add(p.getName());
        yamlConfiguration.set("OP", OP);
        try {
          yamlConfiguration.save(f2);
        } catch (IOException e) {
          
          e.printStackTrace();
        } 
        String temp2 = Variable.Lang_YML.getString("SuccessJoinOthers");
        if (temp2.contains("<Name>")) {
          temp2 = temp2.replace("<Name>", who_invite);
        }
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp2);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        Player who_invite_player = Bukkit.getPlayer(who_invite);
        if (who_invite_player != null) {
          String temp = Variable.Lang_YML.getString("SuccessInviteOther");
          if (temp.contains("<Name>")) {
            temp = temp.replace("<Name>", p.getName());
          }
          who_invite_player
            .sendMessage("§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
          who_invite_player.sendMessage(temp);
          who_invite_player
            .sendMessage("§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
        } 
        Variable.invite_list.remove(who_invite);
      } 
      
      return false;
    } 

    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("locktime")) {
      File f2 = new File(Variable.Tempf, String.valueOf(p.getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
      if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {

        
        if (!Main.JavaPlugin.getConfig().getBoolean("Permission.LockTime") && 
          !p.hasPermission("SelfHome.locktime")) {
          
          String temp = Variable.Lang_YML.getString("NoPermissionLockTime");
          if (temp.contains("<Permission>")) {
            temp = temp.replace("<Permission>", "SelfHome.LockTime");
          }
          
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 


        
        if (Variable.bungee) {
          if (MySQL.getlocktime(p.getWorld().getName().replace(Variable.world_prefix, "")).equalsIgnoreCase("true")) {
            
            MySQL.setlocktime(p.getWorld().getName().replace(Variable.world_prefix, ""), "false");
            
            String temp = Variable.Lang_YML.getString("TimeUnLocked");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          } else {
            MySQL.setlocktime(p.getWorld().getName().replace(Variable.world_prefix, ""), "true");
            MySQL.settime(p.getWorld().getName().replace(Variable.world_prefix, ""), String.valueOf(p.getWorld().getTime()));
            String temp = Variable.Lang_YML.getString("TimeLocked");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          } 
        } else {
          YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f2);
          if (yamlConfiguration.getBoolean("locktime")) {
            yamlConfiguration.set("locktime", Boolean.valueOf(false));
            try {
              yamlConfiguration.save(f2);
            } catch (IOException e) {
              
              e.printStackTrace();
            } 
            String temp = Variable.Lang_YML.getString("TimeUnLocked");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          } else {
            
            yamlConfiguration.set("locktime", Boolean.valueOf(true));
            yamlConfiguration.set("time", Long.valueOf(p.getWorld().getTime()));
            try {
              yamlConfiguration.save(f2);
            } catch (IOException e) {
              
              e.printStackTrace();
            } 
            String temp = Variable.Lang_YML.getString("TimeLocked");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          } 
        } 
      } else {
        
        String temp = Variable.Lang_YML.getString("NoOwnerAndManagerPermission");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
      } 
      return false;
    } 

    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("lockweather")) {
      File f2 = new File(Variable.Tempf, String.valueOf(p.getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
      if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
        
        String Language = Variable.Lang_YML.getString("NoPermissionCheck");
        if (Language.contains("<Permission>")) {
          Language = Language.replace("<Permission>", "SelfHome.lockWeather");
        }
        
        if (!Main.JavaPlugin.getConfig().getBoolean("Permission.LockWeather") && 
          !p.hasPermission("SelfHome.lockweather")) {
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(Language);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 


        
        if (Variable.bungee) {
          if (MySQL.getlockweather(p.getWorld().getName().replace(Variable.world_prefix, "")).equalsIgnoreCase("true")) {
            MySQL.setlockweather(p.getWorld().getName().replace(Variable.world_prefix, ""), "false");
            
            String temp = Variable.Lang_YML.getString("WeatherUnLocked");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          } else {
            MySQL.setlockweather(p.getWorld().getName().replace(Variable.world_prefix, ""), "true");
            
            String temp = Variable.Lang_YML.getString("WeatherLocked");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          } 
        } else {
          
          YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f2);
          if (yamlConfiguration.getBoolean("lockweather")) {
            yamlConfiguration.set("lockweather", Boolean.valueOf(false));
            try {
              yamlConfiguration.save(f2);
            } catch (IOException e) {
              
              e.printStackTrace();
            } 
            String temp = Variable.Lang_YML.getString("WeatherUnLocked");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          } else {
            
            yamlConfiguration.set("lockweather", Boolean.valueOf(true));
            try {
              yamlConfiguration.save(f2);
            } catch (IOException e) {
              
              e.printStackTrace();
            } 
            String temp = Variable.Lang_YML.getString("WeatherLocked");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          }
        
        } 
      } else {
        
        String temp = Variable.Lang_YML.getString("NoOwnerPermission");
        
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
      } 
      return false;
    } 

    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("day")) {
      if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
        String Language = Variable.Lang_YML.getString("NoPermissionCheck");
        if (Language.contains("<Permission>")) {
          Language = Language.replace("<Permission>", "SelfHome.Day");
        }
        
        if (!Main.JavaPlugin.getConfig().getBoolean("Permission.Day") && 
          !p.hasPermission("SelfHome.Day")) {
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(Language);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
        if(HomeAPI.getHome(p.getWorld().getName()).isLocktime()) {
        	try {
				HomeAPI.getHome(p.getWorld().getName()).setLocktime(false);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
        }
        p.sendMessage(Variable.Lang_YML.getString("TimeDay"));
        p.getWorld().setTime(0L);
      } else {
        String temp = Variable.Lang_YML.getString("NoOwnerAndManagerPermission");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
      } 
      return false;
    } 

    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("sun")) {
      if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
        String Language = Variable.Lang_YML.getString("NoPermissionCheck");
        if (Language.contains("<Permission>")) {
          Language = Language.replace("<Permission>", "SelfHome.Sun");
        }
        
        if (!Main.JavaPlugin.getConfig().getBoolean("Permission.Sun") && 
          !p.hasPermission("SelfHome.Sun")) {
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(Language);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
        if(HomeAPI.getHome(p.getWorld().getName()).isLockweather()) {
        	try {
				HomeAPI.getHome(p.getWorld().getName()).setLockweather(false);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
        }
        p.sendMessage(Variable.Lang_YML.getString("WeatherSun"));
        p.getWorld().setStorm(false);
        p.getWorld().setThundering(false);
      } else {
        
        String temp = Variable.Lang_YML.getString("NoOwnerAndManagerPermission");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
      } 
      return false;
    } 

    
    if (args.length == 1 && 
    	      args[0].equalsIgnoreCase("togglecc")) {
    	      if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
    	        String Language = Variable.Lang_YML.getString("NoPermissionCheck");
    	        if (Language.contains("<Permission>")) {
    	          Language = Language.replace("<Permission>", "SelfHome.Togglecc");
    	        }
    	        
  
    	        
    	        
    	        if (!p.hasPermission("SelfHome.Togglecc")) {
    	          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
    	          sender.sendMessage(Language);
    	          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
    	          return false;
    	        }
    	        
    	        	WBControl.togglecc(p);
    	        
    	        
    	      } else {
    	        
    	        String temp = Variable.Lang_YML.getString("NoOwnerAndManagerPermission");
    	        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
    	        sender.sendMessage(temp);
    	        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
    	      } 
    	      return false;
    	    } 
    
    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("nether")) {

      
      if (!Main.JavaPlugin.getConfig().getBoolean("EnableNetherTeleport")) {
        p.sendMessage(Variable.Lang_YML.getString("NoOpenNetherTeleport"));
        return false;
      } 


      
      String Language = Variable.Lang_YML.getString("NoPermissionCheck");
      if (Language.contains("<Permission>")) {
        Language = Language.replace("<Permission>", "SelfHome.Nether");
      }
      if (!Main.JavaPlugin.getConfig().getBoolean("Permission.Nether") && 
        !p.hasPermission("SelfHome.Nether")) {
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(Language);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        return false;
      } 

      
      World world = Bukkit.getWorld(Main.JavaPlugin.getConfig().getString("NetherWorldName"));
      if (world == null) {
        WorldCreator creator = new WorldCreator(Main.JavaPlugin.getConfig().getString("NetherWorldName"));
        Variable.create_list_home.add(Main.JavaPlugin.getConfig().getString("NetherWorldName"));
        Bukkit.createWorld(creator);
      } 
      world = Bukkit.getWorld(Main.JavaPlugin.getConfig().getString("NetherWorldName"));
      
      for (int i = 0; i < Main.JavaPlugin.getConfig().getStringList("NeitherGameRules").size(); i++) {
        String[] temp = ((String)Main.JavaPlugin.getConfig().getStringList("NeitherGameRules").get(i)).split(",");
        world.setGameRuleValue(temp[0], temp[1]);
      } 
      
      p.teleport(world.getSpawnLocation());
      return false;
    } 

    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("end")) {
      if (!Main.JavaPlugin.getConfig().getBoolean("EnableEndTeleport")) {
        p.sendMessage(Variable.Lang_YML.getString("NoOpenEndTeleport"));
        return false;
      } 
      String Language = Variable.Lang_YML.getString("NoPermissionCheck");
      if (Language.contains("<Permission>")) {
        Language = Language.replace("<Permission>", "SelfHome.End");
      }
      if (!Main.JavaPlugin.getConfig().getBoolean("Permission.End") && 
        !p.hasPermission("SelfHome.End")) {
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(Language);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        return false;
      } 
      
      World world = Bukkit.getWorld(Main.JavaPlugin.getConfig().getString("EndWorldName"));
      if (world == null) {
        WorldCreator creator = new WorldCreator(Main.JavaPlugin.getConfig().getString("EndWorldName"));
        Variable.create_list_home.add(Main.JavaPlugin.getConfig().getString("EndWorldName"));
        Bukkit.createWorld(creator);
      } 
      
      world = Bukkit.getWorld(Main.JavaPlugin.getConfig().getString("EndWorldName"));
      for (int i = 0; i < Main.JavaPlugin.getConfig().getStringList("EndGameRules").size(); i++) {
        String[] temp = ((String)Main.JavaPlugin.getConfig().getStringList("EndGameRules").get(i)).split(",");
        world.setGameRuleValue(temp[0], temp[1]);
      } 
      p.teleport(world.getSpawnLocation());
      return false;
    } 

    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("rain")) {
      if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
        String Language = Variable.Lang_YML.getString("NoPermissionCheck");
        if (Language.contains("<Permission>")) {
          Language = Language.replace("<Permission>", "SelfHome.Rain");
        }
        if (!Main.JavaPlugin.getConfig().getBoolean("Permission.Rain") && 
          !p.hasPermission("SelfHome.Rain")) {
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(Language);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        }
        if(HomeAPI.getHome(p.getWorld().getName()).isLockweather()) {
        	try {
				HomeAPI.getHome(p.getWorld().getName()).setLockweather(false);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
        }
        p.sendMessage(Variable.Lang_YML.getString("WeatherRain"));
        p.getWorld().setStorm(true);
      } else {
        
        String temp = Variable.Lang_YML.getString("NoOwnerAndManagerPermission");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
      } 
      return false;
    }
    
    
    
    
    
    
    
    
    
    
    if (args.length == 1 && 
    	      args[0].equalsIgnoreCase("seed")) {
    	      if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
    	        String Language = Variable.Lang_YML.getString("NoPermissionCheck");
    	        if (Language.contains("<Permission>")) {
    	          Language = Language.replace("<Permission>", "SelfHome.Seed");
    	        }
    	        if (!p.hasPermission("SelfHome.Seed")) {
    	          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
    	          sender.sendMessage(Language);
    	          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
    	          return false;
    	        } 
    	        String message = Variable.Lang_YML.getString("LookSeed");
    	        if(message.contains("<Seed>")) {
    	        	message = message.replace("<Seed>", String.valueOf(p.getWorld().getSeed()));
    	        }
    	        p.sendMessage(message);
    	      } else {
    	        String temp = Variable.Lang_YML.getString("NoOwnerAndManagerPermission");
    	        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
    	        sender.sendMessage(temp);
    	        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
    	      } 
    	      return false;
    	    }
    
    
    
    if (args.length == 2 && 
  	      args[0].equalsIgnoreCase("fly")) {
    	 if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
  	        String Language = Variable.Lang_YML.getString("NoPermissionCheck");
  	        if (Language.contains("<Permission>")) {
  	          Language = Language.replace("<Permission>", "SelfHome.Fly");
  	        }
  	        if (!p.hasPermission("SelfHome.Fly")) {
  	          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
  	          sender.sendMessage(Language);
  	          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
  	          return false;
  	        } 
  	        
  	        
  	        if(args[1].equalsIgnoreCase("off")) {
  	  	      
  	        	for(Player e : p.getWorld().getPlayers()) {
  	        	  String message = Variable.Lang_YML.getString("DisableFly");
    	  	        e.sendMessage(message);
    	  	        
    	  	      if(Variable.flying_list.containsKey(e.getName())) {
    	  	    	Variable.flying_list.remove(e.getName());
    	  	        }
    	  	        
    	  	      if(e.getAllowFlight()==true) {
	  	  	        	e.setAllowFlight(false);
	  	  	        }

  	        	}
  	  	        
  	        }else {

  	        	
  	        	for(Player e : p.getWorld().getPlayers()) {
  	  	  	        String message = Variable.Lang_YML.getString("EnableFly");
  	  	  	        e.sendMessage(message);
  	  	  	        if(!e.getAllowFlight()) {
  	  	  	        	if(!(Variable.flying_list.containsKey(e.getName()))) {
  	  	  	        	Variable.flying_list.put(e.getName(), p.getWorld().getName());
  	  	  	        	}
  	  	  	        	e.setAllowFlight(true);
  	  	  	        }
  	  	  	       
  	        	}

  	        }
  	        

  	      } else {
  	    	 String temp = Variable.Lang_YML.getString("NoOwnerAndManagerPermission");
  	        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
  	        sender.sendMessage(temp);
  	        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
  	      } 
  	      return false;
  	    }
    
    
    
    
    
    
    

    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("night")) {
      if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
        String temp = Variable.Lang_YML.getString("NoNightPermission");
        if (temp.contains("<Permission>")) {
          temp = temp.replace("<Permission>", "SelfHome.Night");
        }
        if (!Main.JavaPlugin.getConfig().getBoolean("Permission.Night") && 
          !p.hasPermission("SelfHome.night")) {
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
        if(HomeAPI.getHome(p.getWorld().getName()).isLocktime()) {
        	try {
				HomeAPI.getHome(p.getWorld().getName()).setLocktime(false);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
        }
        p.sendMessage(Variable.Lang_YML.getString("TimeNight"));
        p.getWorld().setTime(14000L);
      } else {
        String temp = Variable.Lang_YML.getString("NoOwnerAndManagerPermission");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
      } 
      return false;
    } 

    
    if (args.length == 2 && (
      args[0].equalsIgnoreCase("trust") || args[0].equalsIgnoreCase("t") || args[0].equalsIgnoreCase("add"))) {
      String Name = args[1];
      if (!Main.JavaPlugin.getConfig().getBoolean("Permission.CommandUser") && 
        !p.hasPermission("SelfHome.Trust") && !p.hasPermission("SelfHome.command.user")) {
        String tip = Variable.Lang_YML.getString("NoPermissionCheck");
        if (tip.contains("<Permission>")) {
          tip = tip.replace("<Permission>", "SelfHome.Trust");
        }
        p.sendMessage(tip);
        return false;
      } 


      
      File f2 = new File(Variable.Tempf, String.valueOf(p.getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
      if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
        
        if (args[1].equalsIgnoreCase(p.getName())) {
          String temp = Variable.Lang_YML.getString("AddOwnerToTrust");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
        
        if (Variable.bungee) {
          
          List<String> blacklist = 
            MySQL.getDenys(p.getWorld().getName().replace(Variable.world_prefix, ""));
          if (blacklist == null) {
            blacklist = new ArrayList<>();
          }
          for (int i = 0; i < blacklist.size(); i++) {
            if (((String)blacklist.get(i)).equalsIgnoreCase(args[1])) {
              String temp = Variable.Lang_YML.getString("HasAlreadyInBlack");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              return false;
            } 
          } 
          
          List<String> save = MySQL.getMembers(p.getWorld().getName().replace(Variable.world_prefix, ""));
          if (save == null) {
            save = new ArrayList<>();
          }
          Boolean CheckSame = Boolean.valueOf(false);
          for (int i = 0; i < save.size(); i++) {
            if (((String)save.get(i)).equalsIgnoreCase(Name)) {
              String temp = Variable.Lang_YML.getString("HasAlreadyTrust");
              CheckSame = Boolean.valueOf(true);
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            } 
          } 
          
          if (!CheckSame.booleanValue()) {
            if (save.size()+1 > Main.JavaPlugin.getConfig().getInt("MaxJoin")) {
              
              int max_player = Main.JavaPlugin.getConfig().getInt("MaxJoin");
              for (int i = Main.JavaPlugin.getConfig().getInt("MaxJoin") * 3; i > Main.JavaPlugin
                .getConfig().getInt("MaxJoin"); i--) {
                if (p.hasPermission("SelfHome.MaxJoin." + i)) {
                  max_player = i;
                  
                  break;
                } 
              } 
              if (save.size() + 2 > max_player) {
                String temp = Variable.Lang_YML.getString("MaxJoinMembers");
                if (temp.contains("<MaxAmount>")) {
                  temp = temp.replace("<MaxAmount>", String.valueOf(max_player));
                }
                sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                sender.sendMessage(temp);
                sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              }
              else {
                
                String result = MySQL.getListStringSpiltByDot(MySQL.getMembers(p.getWorld().getName().replace(Variable.world_prefix, "")));
                
                if (result == null || result.equalsIgnoreCase("")) {
                  result = Name;
                } else {
                  result = String.valueOf(result) + "," + Name;
                } 
                
                MySQL.setMembers(p.getWorld().getName().replace(Variable.world_prefix, ""), result);
                
                String temp = Variable.Lang_YML.getString("AddTrustSuccess");
                if (temp.contains("<Name>")) {
                  temp = temp.replace("<Name>", Name);
                }
                
                sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                sender.sendMessage(temp);
                sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              } 
              
              return false;
            } 
            
            String result = MySQL.getListStringSpiltByDot(
                MySQL.getMembers(p.getWorld().getName().replace(Variable.world_prefix, "")));
            if (result == null || result.equalsIgnoreCase("")) {
              result = Name;
            } else {
              result = String.valueOf(result) + "," + Name;
            } 
            MySQL.setMembers(p.getWorld().getName().replace(Variable.world_prefix, ""), result);
            
            String temp = Variable.Lang_YML.getString("AddTrustSuccess");
            if (temp.contains("<Name>")) {
              temp = temp.replace("<Name>", Name);
            }
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          }
        
        } else {
          
          YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f2);
          List<String> blacklist = yamlConfiguration.getStringList("Denys");
          if (blacklist == null) {
            blacklist = new ArrayList<>();
          }
          for (int i = 0; i < blacklist.size(); i++) {
            if (((String)blacklist.get(i)).equalsIgnoreCase(args[1])) {
              String temp = Variable.Lang_YML.getString("HasAlreadyInBlack");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              return false;
            } 
          } 
          
          List<String> save = yamlConfiguration.getStringList("Members");
          if (save == null) {
            save = new ArrayList<>();
          }
          Boolean CheckSame = Boolean.valueOf(false);
          for (int i = 0; i < save.size(); i++) {
            if (((String)save.get(i)).equalsIgnoreCase(Name)) {
              String temp = Variable.Lang_YML.getString("HasAlreadyTrust");
              CheckSame = Boolean.valueOf(true);
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
            } 
          } 
          
          if (!CheckSame.booleanValue()) {
            if (save.size() + 2 > Main.JavaPlugin.getConfig().getInt("MaxJoin")) {
              
              int max_player = Main.JavaPlugin.getConfig().getInt("MaxJoin");
              for (int i = Main.JavaPlugin.getConfig().getInt("MaxJoin") * 3; i > Main.JavaPlugin
                .getConfig().getInt("MaxJoin"); i--) {
                if (p.hasPermission("SelfHome.MaxJoin." + i)) {
                  max_player = i;
                  
                  break;
                } 
              } 
              if (save.size() + 2 > max_player) {
                String temp = Variable.Lang_YML.getString("MaxJoinMembers");
                if (temp.contains("<MaxAmount>")) {
                  temp = temp.replace("<MaxAmount>", String.valueOf(max_player));
                }
                sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                sender.sendMessage(temp);
                sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              } else {
                
                save.add(Name);
                yamlConfiguration.set("Members", save);
                try {
                  yamlConfiguration.save(f2);
                } catch (IOException e) {
                  
                  e.printStackTrace();
                } 
                
                String temp = Variable.Lang_YML.getString("AddTrustSuccess");
                if (temp.contains("<Name>")) {
                  temp = temp.replace("<Name>", Name);
                }
                
                sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                sender.sendMessage(temp);
                sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              } 
              
              return false;
            } 
            
            save.add(Name);
            yamlConfiguration.set("Members", save);
            try {
              yamlConfiguration.save(f2);
            } catch (IOException e) {
              
              e.printStackTrace();
            } 
            String temp = Variable.Lang_YML.getString("AddTrustSuccess");
            if (temp.contains("<Name>")) {
              temp = temp.replace("<Name>", Name);
            }
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          }
        
        }
      
      } else {
        
        String temp = Variable.Lang_YML.getString("NoOwnerPermission");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        return false;
      } 
      return false;
    } 


    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("Deny")) {
      String Name = args[1];
      if (!Main.JavaPlugin.getConfig().getBoolean("Permission.CommandUser") && 
        !p.hasPermission("SelfHome.Deny") && !p.hasPermission("SelfHome.command.user")) {
        String tip = Variable.Lang_YML.getString("NoPermissionCheck");
        if (tip.contains("<Permission>")) {
          tip = tip.replace("<Permission>", "SelfHome.Deny");
        }
        p.sendMessage(tip);
        return false;
      } 


      
      File f2 = new File(Variable.Tempf, String.valueOf(p.getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
      if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
        
        if (args[1].equalsIgnoreCase(p.getName())) {
          String temp = Variable.Lang_YML.getString("AddOwnerToBlack");
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
        
        if (Variable.bungee) {
          
          List<String> trustlist = 
            MySQL.getMembers(p.getWorld().getName().replace(Variable.world_prefix, ""));
          if (trustlist == null) {
            trustlist = new ArrayList<>();
          }
          for (int i = 0; i < trustlist.size(); i++) {
            if (((String)trustlist.get(i)).equalsIgnoreCase(args[1])) {
              String temp = Variable.Lang_YML.getString("HasInTrust");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              return false;
            } 
          } 
          
          List<String> oplist = 
            MySQL.getOP(p.getWorld().getName().replace(Variable.world_prefix, ""));
          if (oplist == null) {
            oplist = new ArrayList<>();
          }
          for (int i = 0; i < oplist.size(); i++) {
            if (((String)oplist.get(i)).equalsIgnoreCase(args[1])) {
              String temp = Variable.Lang_YML.getString("HasInManager");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              return false;
            } 
          } 
          
          List<String> save = 
            MySQL.getDenys(p.getWorld().getName().replace(Variable.world_prefix, ""));
          
          if (save == null) {
            save = new ArrayList<>();
          }
          Boolean CheckSame = Boolean.valueOf(false);
          for (int i = 0; i < save.size(); i++) {
            if (((String)save.get(i)).equalsIgnoreCase(Name)) {
              CheckSame = Boolean.valueOf(true);
              String temp = Variable.Lang_YML.getString("HasAlreadyExistBlack");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              return false;
            } 
          } 
          
          if (!CheckSame.booleanValue()) {
            String result = MySQL.getListStringSpiltByDot(
                MySQL.getDenys(p.getWorld().getName().replace(Variable.world_prefix, "")));
            if (result == null || result.equalsIgnoreCase("")) {
              result = Name;
            } else {
              result = String.valueOf(result) + "," + Name;
            } 
            MySQL.setDenys(p.getWorld().getName().replace(Variable.world_prefix, ""), result);
            
            for (Player pt : Bukkit.getWorld(Variable.world_prefix + p.getName()).getPlayers()) {
              if (pt.getName().equalsIgnoreCase(Name)) {
                String temp = Variable.Lang_YML.getString("BeKicked");
                pt.sendMessage("§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
                pt.sendMessage(temp);
                pt.sendMessage("§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
                
                String bekicked = Variable.Lang_YML.getString("BeKickedCommand");
                if (bekicked.contains("<Name>")) {
                  bekicked = bekicked.replace("<Name>", pt.getName());
                }
                Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), bekicked);
              } 
            } 

            
            String temp = Variable.Lang_YML.getString("AddBlackSuccess");
            if (temp.contains("<Name>")) {
              temp = temp.replace("<Name>", Name);
            }
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          } 
        } else {
          
          YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f2);
          
          List<String> trustlist = yamlConfiguration.getStringList("Members");
          if (trustlist == null) {
            trustlist = new ArrayList<>();
          }
          for (int i = 0; i < trustlist.size(); i++) {
            if (((String)trustlist.get(i)).equalsIgnoreCase(args[1])) {
              String temp = Variable.Lang_YML.getString("HasInTrust");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              return false;
            } 
          } 
          
          List<String> oplist = yamlConfiguration.getStringList("OP");
          if (oplist == null) {
            oplist = new ArrayList<>();
          }
          for (int i = 0; i < oplist.size(); i++) {
            if (((String)oplist.get(i)).equalsIgnoreCase(args[1])) {
              String temp = Variable.Lang_YML.getString("HasInManager");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              return false;
            } 
          } 
          
          List<String> save = yamlConfiguration.getStringList("Denys");
          
          if (save == null) {
            save = new ArrayList<>();
          }
          Boolean CheckSame = Boolean.valueOf(false);
          for (int i = 0; i < save.size(); i++) {
            if (((String)save.get(i)).equalsIgnoreCase(Name)) {
              CheckSame = Boolean.valueOf(true);
              String temp = Variable.Lang_YML.getString("HasAlreadyExistBlack");
              sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
              sender.sendMessage(temp);
              sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              return false;
            } 
          } 
          
          if (!CheckSame.booleanValue()) {
            save.add(Name);
            yamlConfiguration.set("Denys", save);
            try {
              yamlConfiguration.save(f2);
            } catch (IOException e) {
              
              e.printStackTrace();
            } 
            
            for (Player pt : Bukkit.getWorld(Variable.world_prefix + p.getName()).getPlayers()) {
              if (pt.getName().equalsIgnoreCase(Name)) {
                String temp = Variable.Lang_YML.getString("BeKicked");
                pt.sendMessage("§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
                pt.sendMessage(temp);
                pt.sendMessage("§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
                
                String bekicked = Variable.Lang_YML.getString("BeKickedCommand");
                if (bekicked.contains("<Name>")) {
                  bekicked = bekicked.replace("<Name>", pt.getName());
                }
                Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), bekicked);
              } 
            } 

            
            String temp = Variable.Lang_YML.getString("AddBlackSuccess");
            if (temp.contains("<Name>")) {
              temp = temp.replace("<Name>", Name);
            }
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          }
        
        } 
      } else {
        
        String temp = Variable.Lang_YML.getString("NoOwnerPermission");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        return false;
      } 
      return false;
    } 

    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("UnDeny")) {
      String Name = args[1];
      File f2 = new File(Variable.Tempf, String.valueOf(p.getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
      if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
        
        if (Variable.bungee) {
          
          List<String> save = 
            MySQL.getDenys(p.getWorld().getName().replace(Variable.world_prefix, ""));
          Boolean Check = Boolean.valueOf(false);
          
          if (save == null) {
            save = new ArrayList<>();
          }
          
          for (int i = 0; i < save.size(); i++) {
            if (((String)save.get(i)).equalsIgnoreCase(Name)) {
              Check = Boolean.valueOf(true);
            }
          } 
          
          if (Check.booleanValue()) {
            for (int i = 0; i < save.size(); i++) {
              if (((String)save.get(i)).equalsIgnoreCase(Name)) {
                String result = MySQL.getListStringSpiltByDot(
                    MySQL.getDenys(p.getWorld().getName().replace(Variable.world_prefix, "")));
                result = result.replace("," + (String)save.get(i), "");
                result = result.replace(save.get(i), "");
                MySQL.setDenys(p.getWorld().getName().replace(Variable.world_prefix, ""), result);
                
                String temp = Variable.Lang_YML.getString("RemoveBlackSuccess");
                if (temp.contains("<Name>")) {
                  temp = temp.replace("<Name>", Name);
                }
                sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                sender.sendMessage(temp);
                sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              } 
            } 
          } else {
            
            String temp = Variable.Lang_YML.getString("NoBlackExist");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          } 
        } else {
          YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f2);
          List<String> save = yamlConfiguration.getStringList("Denys");
          Boolean Check = Boolean.valueOf(false);
          
          if (save == null) {
            save = new ArrayList<>();
          }
          
          for (int i = 0; i < save.size(); i++) {
            if (((String)save.get(i)).equalsIgnoreCase(Name)) {
              Check = Boolean.valueOf(true);
            }
          } 
          
          if (Check.booleanValue()) {
            for (int i = 0; i < save.size(); i++) {
              if (((String)save.get(i)).equalsIgnoreCase(Name)) {
                save.remove(i);
                yamlConfiguration.set("Denys", save);
                try {
                  yamlConfiguration.save(f2);
                } catch (IOException e) {
                  
                  e.printStackTrace();
                } 
                String temp = Variable.Lang_YML.getString("RemoveBlackSuccess");
                if (temp.contains("<Name>")) {
                  temp = temp.replace("<Name>", Name);
                }
                sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                sender.sendMessage(temp);
                sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              } 
            } 
          } else {
            
            String temp = Variable.Lang_YML.getString("NoBlackExist");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          } 
        } 
      } else {
        
        String temp = Variable.Lang_YML.getString("NoOwnerAndManagerPermission");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        return false;
      } 
      return false;
    } 

    
    if (args.length == 2 && (
      args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete") || 
      args[0].equalsIgnoreCase("del"))) {
      String Name = args[1];
      File f2 = new File(Variable.Tempf, String.valueOf(p.getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
      if (Util.CheckOwnerAndManagerAndOP(p, p.getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
        
        if (Variable.bungee) {
          
          List<String> save = 
            MySQL.getMembers(p.getWorld().getName().replace(Variable.world_prefix, ""));
          Boolean Check = Boolean.valueOf(false);
          
          if (save == null) {
            save = new ArrayList<>();
          }
          
          for (int i = 0; i < save.size(); i++) {
            if (((String)save.get(i)).equalsIgnoreCase(Name)) {
              Check = Boolean.valueOf(true);
            }
          } 
          
          if (Check.booleanValue()) {
            for (int i = 0; i < save.size(); i++) {
              if (((String)save.get(i)).equalsIgnoreCase(Name)) {
                String result = MySQL.getListStringSpiltByDot(
                    MySQL.getMembers(p.getWorld().getName().replace(Variable.world_prefix, "")));
                result = result.replace("," + (String)save.get(i), "");
                result = result.replace(save.get(i), "");
                MySQL.setMembers(p.getWorld().getName().replace(Variable.world_prefix, ""), result);
                String temp = Variable.Lang_YML.getString("RemoveTrustPlayer");
                if (temp.contains("<Name>")) {
                  temp = temp.replace("<Name>", args[1]);
                }
                sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                sender.sendMessage(temp);
                sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              } 
            } 
          } else {
            
            String temp = Variable.Lang_YML.getString("NoTrustExist");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          }
        
        } else {
          
          YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f2);
          List<String> save = yamlConfiguration.getStringList("Members");
          Boolean Check = Boolean.valueOf(false);
          
          if (save == null) {
            save = new ArrayList<>();
          }
          
          for (int i = 0; i < save.size(); i++) {
            if (((String)save.get(i)).equalsIgnoreCase(Name)) {
              Check = Boolean.valueOf(true);
            }
          } 
          
          if (Check.booleanValue()) {
            for (int i = 0; i < save.size(); i++) {
              if (((String)save.get(i)).equalsIgnoreCase(Name)) {
                save.remove(i);
                yamlConfiguration.set("Members", save);
                try {
                  yamlConfiguration.save(f2);
                } catch (IOException e) {
                  
                  e.printStackTrace();
                } 
                String temp = Variable.Lang_YML.getString("RemoveTrustPlayer");
                if (temp.contains("<Name>")) {
                  temp = temp.replace("<Name>", args[1]);
                }
                sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                sender.sendMessage(temp);
                sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              } 
            } 
          } else {
            
            String temp = Variable.Lang_YML.getString("NoTrustExist");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          }
        
        } 
      } else {
        
        String temp = Variable.Lang_YML.getString("NoOwnerAndManagerPermission");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        return false;
      } 
      return false;
    } 

    
    if (args.length == 1 && (
      args[0].equalsIgnoreCase("quit") || args[0].equalsIgnoreCase("q"))) {
      if (!Main.JavaPlugin.getConfig().getBoolean("Permission.CommandUser") && 
        !p.hasPermission("SelfHome.Quit") && !p.hasPermission("SelfHome.command.user")) {
        String tip = Variable.Lang_YML.getString("NoPermissionCheck");
        if (tip.contains("<Permission>")) {
          tip = tip.replace("<Permission>", "SelfHome.Quit");
        }
        p.sendMessage(tip);
        return false;
      } 


      
      if (Variable.bungee) {
        
        boolean has_been_quit = MySQL.PlayerQuitHome(p.getName());
        
        if (!has_been_quit) {
          String Message = Variable.Lang_YML.getString("QuitButNoJoin");
          p.sendMessage(Message);
          return false;
        } 
        String Message = Variable.Lang_YML.getString("QuitSuccess");
        p.sendMessage(Message);
        return false;
      } 


      
      File folder = new File(Variable.Tempf);
      boolean has_been_quit = false; int b; int j;
      File[] arrayOfFile;
      label3063: for (j = (arrayOfFile = folder.listFiles()).length, b = 0; b < j; ) { File temp = arrayOfFile[b];
        String want_to = temp.getPath().replace(Variable.Tempf, "").replace(".yml", "")
          .replace(Variable.file_loc_prefix, "");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(temp);
        List<String> Ops = yamlConfiguration.getStringList("OP");
        for (int i = 0; i < Ops.size(); i++) {
          String temp_str = yamlConfiguration.getStringList("OP").get(i);
          if (temp_str.equalsIgnoreCase(p.getName())) {
            has_been_quit = true;
            String Message = Variable.Lang_YML.getString("QuitSuccess");
            if (Message.contains("<Name>")) {
              Message = Message.replace("<Name>", want_to);
            }
            if (Bukkit.getPlayer(want_to) != null && 
              Bukkit.getPlayer(want_to) != null) {
              String ManagerQuitTip = Variable.Lang_YML.getString("QuitManager");
              if (ManagerQuitTip.contains("<Name>")) {
                ManagerQuitTip = ManagerQuitTip.replace("<Name>", p.getName());
              }
              Bukkit.getPlayer(want_to).sendMessage(ManagerQuitTip);
            } 
            
            p.sendMessage(Message);
            Ops.remove(i);
            yamlConfiguration.set("OP", Ops);
            
            try {
              yamlConfiguration.save(temp); break label3063;
            } catch (IOException e) {
              
              e.printStackTrace();
              
              break label3063;
            } 
          } 
        } 
        
        b++; }
      
      if (!has_been_quit) {
        String Message = Variable.Lang_YML.getString("QuitButNoJoin");
        p.sendMessage(Message);
        return false;
      } 


      
      return false;
    } 

    
    if (args.length == 2 && (
      args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("k"))) {
      
      String Name = args[1];
      if (p.getWorld().getName().replace(Variable.world_prefix, "")
        .equalsIgnoreCase(p.getWorld().getName().replace(Variable.world_prefix, ""))) {
        
        if (Variable.bungee) {
          
          List<String> save = MySQL.getOP(p.getName());
          Boolean Check = Boolean.valueOf(false);
          
          if (save == null) {
            save = new ArrayList<>();
          }
          
          for (int i = 0; i < save.size(); i++) {
            if (((String)save.get(i)).equalsIgnoreCase(Name)) {
              Check = Boolean.valueOf(true);
            }
          } 
          
          if (Check.booleanValue()) {
            for (int i = 0; i < save.size(); i++) {
              if (((String)save.get(i)).equalsIgnoreCase(Name)) {
                
                String result = MySQL.getListStringSpiltByDot(
                    MySQL.getOP(p.getWorld().getName().replace(Variable.world_prefix, "")));
                if (result.contains("," + Name)) {
                  result = result.replace("," + Name, "");
                } else if (result.contains(Name)) {
                  result = result.replace(Name, "");
                } 
                MySQL.setOP(p.getWorld().getName().replace(Variable.world_prefix, ""), result);
                
                Player pt = Bukkit.getPlayer(Name);
                if (pt != null && 
                  pt.getWorld().getName().replace(Variable.world_prefix, "")
                  .equalsIgnoreCase(p.getName())) {
                  
                  String bekicked = Main.JavaPlugin.getConfig().getString("BeKickedCommand");
                  if (bekicked.contains("<Name>")) {
                    bekicked = bekicked.replace("<Name>", pt.getName());
                  }
                  Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), bekicked);
                  
                  String temp = Variable.Lang_YML.getString("BeKicked");
                  pt.sendMessage(
                      "§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
                  pt.sendMessage(temp);
                  pt.sendMessage(
                      "§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
                } 
                
                String temp = Variable.Lang_YML.getString("KickSuccess");
                if (temp.contains("<Name>")) {
                  temp = temp.replace("<Name>", Name);
                }
                sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                sender.sendMessage(temp);
                sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              } 
            } 
          } else {
            
            String temp = Variable.Lang_YML.getString("KickNotExist");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          }
        
        } else {
          
          File f2 = new File(Variable.Tempf, 
              String.valueOf(p.getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
          YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f2);
          List<String> save = yamlConfiguration.getStringList("OP");
          Boolean Check = Boolean.valueOf(false);
          
          if (save == null) {
            save = new ArrayList<>();
          }
          
          for (int i = 0; i < save.size(); i++) {
            if (((String)save.get(i)).equalsIgnoreCase(Name)) {
              Check = Boolean.valueOf(true);
            }
          } 
          
          if (Check.booleanValue()) {
            for (int i = 0; i < save.size(); i++) {
              if (((String)save.get(i)).equalsIgnoreCase(Name)) {
                save.remove(i);
                yamlConfiguration.set("OP", save);
                try {
                  yamlConfiguration.save(f2);
                } catch (IOException e) {
                  
                  e.printStackTrace();
                } 
                
                Player pt = Bukkit.getPlayer(Name);
                if (pt != null && 
                  pt.getWorld().getName().replace(Variable.world_prefix, "")
                  .equalsIgnoreCase(p.getName())) {
                  
                  String bekicked = Main.JavaPlugin.getConfig().getString("BeKickedCommand");
                  if (bekicked.contains("<Name>")) {
                    bekicked = bekicked.replace("<Name>", pt.getName());
                  }
                  Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), bekicked);
                  
                  String temp = Variable.Lang_YML.getString("BeKicked");
                  pt.sendMessage(
                      "§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
                  pt.sendMessage(temp);
                  pt.sendMessage(
                      "§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
                } 
                
                String temp = Variable.Lang_YML.getString("KickSuccess");
                if (temp.contains("<Name>")) {
                  temp = temp.replace("<Name>", Name);
                }
                sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
                sender.sendMessage(temp);
                sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
              } 
            } 
          } else {
            
            String temp = Variable.Lang_YML.getString("KickNotExist");
            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
            sender.sendMessage(temp);
            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          }
        
        } 
      } else {
        
        String temp = Variable.Lang_YML.getString("NoOwnerPermission");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        return false;
      } 
      return false;
    } 

    
    if (args.length == 1 && 
      args[0].equalsIgnoreCase("create")) {
      
      if (args.length < 2 && Main.JavaPlugin.getConfig().getString("NormalType").equalsIgnoreCase("0")) {
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        String temp = Variable.Lang_YML.getString("CreateHelp");
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        return false;
      } 
      
      if (args.length == 1 && !Main.JavaPlugin.getConfig().getString("NormalType").equalsIgnoreCase("0")) {
        Bukkit.dispatchCommand(sender, 
            "sh create " + Main.JavaPlugin.getConfig().getString("NormalType"));
      }
      return false;
    } 


    
    if (args.length == 2 && 
      args[0].equalsIgnoreCase("create")) {
      
      if (Main.JavaPlugin.getConfig().getBoolean("BungeeCord")) {
        if (MySQL.alreadyhastheplayerjoin(p.getName())) {
          String temp_BungeeCord = Variable.Lang_YML.getString("HasBeenJoin");
          if (temp_BungeeCord.contains("<ServerName>")) {
            temp_BungeeCord = temp_BungeeCord.replace("<ServerName>", MySQL.getJoinServer(p.getName()));
          }
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp_BungeeCord);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
        if (MySQL.alreadyhastheplayerhome(p.getName())) {
          String temp_BungeeCord = Variable.Lang_YML.getString("HasBeenCreate");
          if (temp_BungeeCord.contains("<ServerName>")) {
            temp_BungeeCord = temp_BungeeCord.replace("<ServerName>", MySQL.getServer(p.getName()));
          }
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(temp_BungeeCord);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
      } else {
        File f2 = new File(Variable.Tempf, String.valueOf(p.getName().replace(Variable.world_prefix, "")) + ".yml");
        if (f2.exists()) {
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          String temp3 = Variable.Lang_YML.getString("HasBeenJoin");
          if (temp3.contains("<ServerName>")) {
            temp3 = temp3.replace("<ServerName>", Main.JavaPlugin.getConfig().getString("Server"));
          }
          sender.sendMessage(temp3);
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
      } 




      
      if (Main.JavaPlugin.getConfig().getBoolean("AutoReCreateInLowerLagHome") && !Variable.wait_to_command.containsKey(p.getName()) && Main.JavaPlugin.getConfig().getBoolean("BungeeCord")) {
        if (Main.JavaPlugin.getConfig().getString("DecideBy").equalsIgnoreCase("Player")) {
          if (!MySQL.getLowerstLagServer().equalsIgnoreCase(Main.JavaPlugin.getConfig().getString("Server"))) {
            try {
              Channel.waitToCommand(p, MySQL.getLowerstLagServer(), "sh create " + args[1]);
            } catch (IOException e) {
              
              e.printStackTrace();
            } 
            p.sendMessage(Variable.Lang_YML.getString("StartLowestLagServer"));
            Channel.sendPlayerToServer(p, MySQL.getLowerstLagServer());
            return false;
          }
        
        }
        else if (!MySQL.getHighestTPSServer().equalsIgnoreCase(Main.JavaPlugin.getConfig().getString("Server"))) {
          double now = 0.0D;
          if (Bukkit.getVersion().contains("1.7.10")) {
            now = R1_7_10.getTps();
          } else {
            double se1 = Double.valueOf(PlaceholderAPI.setPlaceholders(null, "%server_tps_1%").replace("*", "")).doubleValue();
            double se2 = Double.valueOf(PlaceholderAPI.setPlaceholders(null, "%server_tps_5%").replace("*", "")).doubleValue();
            double se3 = Double.valueOf(PlaceholderAPI.setPlaceholders(null, "%server_tps_15%").replace("*", "")).doubleValue();
            now = (se1 + se2 + se3) / 3.0D;
          } 
          if (MySQL.getServerAmount(MySQL.getLowerstLagServer()) != now) {
            try {
              Channel.waitToCommand(p, MySQL.getHighestTPSServer(), "sh create " + args[1]);
            } catch (IOException e) {
              
              e.printStackTrace();
            } 
            p.sendMessage(Variable.Lang_YML.getString("StartLowestLagServer"));
            Channel.sendPlayerToServer(p, MySQL.getHighestTPSServer());
            return false;
          } 
        } 
      }







      
      YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(Variable.f_log);
      if (!yamlConfiguration.contains("NowID")) {
        yamlConfiguration.set("NowID", Integer.valueOf(0));
      }
      if (!yamlConfiguration.contains("MaxID")) {
        yamlConfiguration.set("MaxID", Integer.valueOf(1000));
      }
      try {
        yamlConfiguration.save(Variable.f_log);
      } catch (IOException e2) {
        
        e2.printStackTrace();
      } 
      
      int nowID = yamlConfiguration.getInt("NowID");
      int MaxID = yamlConfiguration.getInt("MaxID");
      
      if (nowID >= MaxID) {
        
        String temp = Variable.Lang_YML.getString("ReachMaxCreate");
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        return false;
      } 
      
      String v = args[1];
      
      if (!Main.JavaPlugin.getConfig().getBoolean("Permission.Create-" + v) && 
        !p.hasPermission("SelfHome.Create." + v) && !p.hasPermission("SelfHome.Create.*")) {
        String temp = Variable.Lang_YML.getString("NoPermissionCreate");
        if (temp.contains("<Permission>")) {
          temp = temp.replace("<Permission>", "SelfHome.Create." + v);
        }
        
        sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
        sender.sendMessage(temp);
        sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
        return false;
      } 


	      if (v.equalsIgnoreCase("1")) {
	          WorldCreator creator = null;
	          creator = new WorldCreator(Variable.world_prefix + p.getName());
	          if (Main.JavaPlugin.getConfig().getLong("Seed") != 0L) {
	            creator.seed(Main.JavaPlugin.getConfig().getLong("Seed"));
	          }
	          
	          if (Main.JavaPlugin.getConfig().getBoolean("generateStructures")) {
	            creator.generateStructures(true);
	          } else {
	            creator.generateStructures(false);
	          } 
	          
	          creator.type(WorldType.NORMAL);
	          Variable.create_list_home.add(Variable.world_prefix + p.getName());
	          World world = Bukkit.createWorld(creator);

	        }
	        else if (v.equalsIgnoreCase("2")) {
	          WorldCreator creator = null;
	          creator = new WorldCreator(Variable.world_prefix + p.getName());
	          if (Main.JavaPlugin.getConfig().getLong("Seed") != 0L) {
	            creator.seed(Main.JavaPlugin.getConfig().getLong("Seed"));
	          }
	          if (Main.JavaPlugin.getConfig().getBoolean("generateStructures")) {
	            creator.generateStructures(true);
	          } else {
	            creator.generateStructures(false);
	          }

	          creator.type(WorldType.FLAT);
	          Variable.create_list_home.add(Variable.world_prefix + p.getName());
	          World world = Bukkit.createWorld(creator);
	         
	        } else {
	          File newf;
	          
	          if (!Main.JavaPlugin.getConfig().getBoolean("Permission.Create-" + v) && 
	            !p.hasPermission("SelfHome.Create." + v) && !p.hasPermission("SelfHome.Create.*")) {
	            String temp = Variable.Lang_YML.getString("NoPermissionCreate");
	            if (temp.contains("<Permission>")) {
	              temp = temp.replace("<Permission>", "SelfHome.Create." + v);
	            }
	            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
	            sender.sendMessage(temp);
	            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
	            return false;
	          } 

	          
	          String oldDir = String.valueOf(Variable.worldFinal) + v;

	          
	          if (Variable.world_prefix.equalsIgnoreCase("")) {
	            if (Bukkit.getVersion().toString().toUpperCase().contains("ARCLIGHT") || Bukkit.getVersion().toString().contains("1.20.1")) {
	              newf = new File(Variable.single_server_gen + Variable.world_prefix);
	            } else {
	              newf = new File(Variable.single_server_gen + "world" + Variable.file_loc_prefix);
	            } 
	          } else {
	            newf = new File(Variable.single_server_gen + Variable.world_prefix);
	          } 

	          
	          String newDir = String.valueOf(newf.getPath().toString()) + Variable.file_loc_prefix + p.getName();

	          
	          File exist_file = new File(oldDir);
	          if (!exist_file.exists()) {
	            String temp = Variable.Lang_YML.getString("WorldFileNotExist");
	            if (temp.contains("<name>")) {
	              temp = temp.replace("<name>", v);
	            }
	            sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
	            sender.sendMessage(temp);
	            sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
	            return false;
	          } 
	          Util.copyDir(oldDir, newDir);
	          
	          WorldCreator creator = null;
	          creator = new WorldCreator(Variable.world_prefix + p.getName());
	          
	          if (Main.JavaPlugin.getConfig().getBoolean("generateStructures")) {
	            creator.generateStructures(true);
	          } else {
	            creator.generateStructures(false);
	          }
	          
	          
	          Variable.create_list_home.add(Variable.world_prefix + p.getName());
	          World world = Bukkit.createWorld(creator);
	        }

      
      
	      if(Variable.hook_multiverseCore) {
			  String seed =  Long.toString(Main.JavaPlugin.getConfig().getLong("Seed"));
			  if(seed.equalsIgnoreCase("0")) {
				  seed = "";
			  }
			     MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		      	MVWorldManager mv_m = mvcore.getMVWorldManager();
		      	
		      	if(mv_m.isMVWorld(Variable.world_prefix+p.getName())) {
		      		mv_m.removeWorldFromConfig(Variable.world_prefix+p.getName());
		      	}
		      	
		      	 if (v.equalsIgnoreCase("1")) {
		      		mv_m.addWorld(Variable.world_prefix + p.getName(), Environment.NORMAL, seed, WorldType.NORMAL, Main.JavaPlugin.getConfig().getBoolean("generateStructures"), "");
		      	 }else if(v.equalsIgnoreCase("2")) {
		      		mv_m.addWorld(Variable.world_prefix + p.getName(), Environment.NORMAL, seed, WorldType.FLAT, Main.JavaPlugin.getConfig().getBoolean("generateStructures"), "");
		      	 }else {
		      		mv_m.addWorld(Variable.world_prefix + p.getName(), Environment.NORMAL, seed, WorldType.NORMAL, Main.JavaPlugin.getConfig().getBoolean("generateStructures"), "");
		      	 }

				  if (Main.JavaPlugin.getConfig().getBoolean("EnableChatPrefix")) {
					  MultiverseWorld mv = mv_m.getMVWorld(Variable.world_prefix + p.getName());
				      	World world = Bukkit.getWorld(Variable.world_prefix+p.getName());
				        String temp = Variable.Lang_YML.getString("PlaceHolders.WorldName");
				        if (temp.contains("<PlayerName>")) {
				          temp = temp.replace("<PlayerName>", 
				              world.getName().replace(Variable.world_prefix, ""));
				        }
				        if (temp.contains("<WorldName>")) {
				          temp = temp.replace("<WorldName>", 
				        		  world.getName().replace(Variable.world_prefix, ""));
				        }
				      	mv.setAlias(temp);
				  }
		      	
		      	MultiverseWorld mv = mv_m.getMVWorld(Variable.world_prefix + p.getName());
		      	mv.setAutoLoad(false);
	      }
      


      
      World world = Bukkit.getWorld(Variable.world_prefix + p.getName());

      
      if (!Main.JavaPlugin.getConfig().getBoolean("KeepInventory")) {
        world.setGameRuleValue("keepInventory", "false");
      } else if (Main.JavaPlugin.getConfig().getBoolean("KeepInventory")) {
        world.setGameRuleValue("keepInventory", "true");
      } 
      
      if (!Main.JavaPlugin.getConfig().getBoolean("doMobSpawning")) {
        world.setGameRuleValue("doMobSpawning", "false");
      } else if (Main.JavaPlugin.getConfig().getBoolean("doMobSpawning")) {
        world.setGameRuleValue("doMobSpawning", "true");
      } 
      if (!Main.JavaPlugin.getConfig().getBoolean("mobGriefing")) {
        world.setGameRuleValue("mobGriefing", "false");
      } else if (Main.JavaPlugin.getConfig().getBoolean("mobGriefing")) {
        world.setGameRuleValue("mobGriefing", "true");
      } 
      
      if (!Main.JavaPlugin.getConfig().getBoolean("doFireTick")) {
        world.setGameRuleValue("doFireTick", "false");
      } else if (Main.JavaPlugin.getConfig().getBoolean("doFireTick")) {
        world.setGameRuleValue("doFireTick", "true");
      } 




      
      if (Variable.bungee) {

        
        int set_level = 1;
        for (int i = Main.JavaPlugin.getConfig().getInt("MaxLevel"); i > 0; i--) {
          if (p.hasPermission("SelfHome.Level." + i) && !p.isOp()) {
            set_level = i;
            
            break;
          } 
        } 
        MySQL.insertvalue(p.getName(), "", "", "", 
            String.valueOf(Main.JavaPlugin.getConfig().getBoolean("NormalPublic")), String.valueOf(set_level), 
            String.valueOf(Main.JavaPlugin.getConfig().getBoolean("NormalPVP")), 
            String.valueOf(Main.JavaPlugin.getConfig().getBoolean("NormalPickup")), 
            String.valueOf(Main.JavaPlugin.getConfig().getBoolean("NormalDrop")), 
            Main.JavaPlugin.getConfig().getString("Server"), "false", "false", "0", 
            String.valueOf(world.getSpawnLocation().getX()), 
            String.valueOf(world.getSpawnLocation().getY()), 
            String.valueOf(world.getSpawnLocation().getZ()), "0", "0", "", "", "", "","");


        
        if (Main.JavaPlugin.getConfig().getBoolean("ClearInventoryBeforeCreate")) {
          p.getInventory().clear();
          p.sendMessage(Variable.Lang_YML.getString("ClearInventoryBeforeCreate"));
        } 
        
        for (int c = 0; c < Main.JavaPlugin.getConfig().getStringList("DispathCommand").size(); c++) {
          String temp = Main.JavaPlugin.getConfig().getStringList("DispathCommand").get(c);
          if (temp.contains("<Name>")) {
            temp = temp.replace("<Name>", p.getName());
          }
          
          if (temp.contains("[console]")) {
            temp = temp.replace("[console]", "");
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), temp);
          } else if (temp.contains("[player]")) {
            temp = temp.replace("[player]", "");
            Bukkit.dispatchCommand((CommandSender)p, temp);
          }
        
        }
      
      }
      else {
        
        File f2 = new File(Variable.Tempf, String.valueOf(p.getName()) + ".yml");
        if (!f2.exists()) {
          try {
            f2.createNewFile();
          } catch (IOException e1) {
            
            e1.printStackTrace();
          } 
          YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(f2);
          yamlConfiguration1.createSection("Members");
          yamlConfiguration1.createSection("OP");
          yamlConfiguration1.createSection("Denys");
          yamlConfiguration1.createSection("Public");
          yamlConfiguration1.createSection("Level");
          yamlConfiguration1.createSection("pvp");
          yamlConfiguration1.createSection("pickup");
          yamlConfiguration1.createSection("drop");
          yamlConfiguration1.createSection("Server");
          yamlConfiguration1.createSection("locktime");
          yamlConfiguration1.createSection("lockweather");
          yamlConfiguration1.createSection("time");



          
          if (!yamlConfiguration.contains("NowID")) {
            yamlConfiguration.set("NowID", Integer.valueOf(0));
          }
          if (!yamlConfiguration.contains("MaxID")) {
            yamlConfiguration.set("MaxID", Integer.valueOf(1000));
          }
          
          try {
            yamlConfiguration.save(Variable.f_log);
          } catch (IOException e2) {
            
            e2.printStackTrace();
          } 
          
          yamlConfiguration1.set("Public", Boolean.valueOf(Main.JavaPlugin.getConfig().getBoolean("NormalPublic")));
          yamlConfiguration1.set("pickup", Boolean.valueOf(Main.JavaPlugin.getConfig().getBoolean("NormalPVP")));
          yamlConfiguration1.set("drop", Boolean.valueOf(Main.JavaPlugin.getConfig().getBoolean("NormalPickup")));
          yamlConfiguration1.set("pvp", Boolean.valueOf(Main.JavaPlugin.getConfig().getBoolean("NormalDrop")));
          
          yamlConfiguration1.set("locktime", Boolean.valueOf(false));
          yamlConfiguration1.set("time", Integer.valueOf(0));
          yamlConfiguration1.set("lockweather", Boolean.valueOf(false));
          
          int set_level = 1;
          for (int i = Main.JavaPlugin.getConfig().getInt("MaxLevel"); i > 0; i--) {
            if (p.hasPermission("SelfHome.Level." + i) && !p.isOp()) {
              set_level = i;
              
              break;
            } 
          } 
          yamlConfiguration1.set("Level", Integer.valueOf(set_level));
          
          yamlConfiguration1.set("Server", Main.JavaPlugin.getConfig().getString("Server"));
          try {
            yamlConfiguration1.save(f2);
          } catch (IOException e) {
            
            e.printStackTrace();
          } 
          
          yamlConfiguration.set("NowID", Integer.valueOf(nowID + 1));
          
          try {
            yamlConfiguration.save(Variable.f_log);
          } catch (IOException e2) {
            
            e2.printStackTrace();
          } 
          
          if (Main.JavaPlugin.getConfig().getInt("MaxSpawnMonstersAmount") != -1) {
            world.setMonsterSpawnLimit(Main.JavaPlugin.getConfig().getInt("MaxSpawnMonstersAmount"));
          }
          
          if (Main.JavaPlugin.getConfig().getInt("MaxSpawnAnimalsAmount") != -1) {
            world.setMonsterSpawnLimit(Main.JavaPlugin.getConfig().getInt("MaxSpawnAnimalsAmount"));
          }
          
          if(Main.JavaPlugin.getConfig().getInt("MaxSpawnAnimalsAmount") == 0) {
        	  if(Variable.hook_multiverseCore == true) {
   			    MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		      	MVWorldManager mv_m = mvcore.getMVWorldManager();
              	MultiverseWorld mv = mv_m.getMVWorld(p.getLocation().getWorld().getName());
              	mv.setAllowAnimalSpawn(false); 
        	  }
          }
          
          if(Main.JavaPlugin.getConfig().getInt("MaxSpawnMonstersAmount") == 0) {
        	  if(Variable.hook_multiverseCore == true) {
   			        MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		      	    MVWorldManager mv_m = mvcore.getMVWorldManager();
                	MultiverseWorld mv = mv_m.getMVWorld(p.getLocation().getWorld().getName());
                	mv.setAllowMonsterSpawn(false);
        	  }
        }
          
          
          yamlConfiguration1.createSection("X");
          yamlConfiguration1.createSection("Y");
          yamlConfiguration1.createSection("Z");
          Location loc = world.getSpawnLocation();
          yamlConfiguration1.set("X", Double.valueOf(loc.getX()));
          yamlConfiguration1.set("Y", Double.valueOf(loc.getY()));
          yamlConfiguration1.set("Z", Double.valueOf(loc.getZ()));

          
          yamlConfiguration1.createSection("flowers");
          yamlConfiguration1.createSection("popularity");
          yamlConfiguration1.createSection("gifts");
          yamlConfiguration1.createSection("icon");
          yamlConfiguration1.createSection("advertisement");
          yamlConfiguration1.createSection("limitblock");
          yamlConfiguration1.set("flowers", Integer.valueOf(0));
          yamlConfiguration1.set("popularity", Integer.valueOf(0));
          yamlConfiguration1.set("gifts", new ArrayList());
          yamlConfiguration1.set("icon", "");
          yamlConfiguration1.set("advertisement", new ArrayList());
          yamlConfiguration1.set("limitblock", new ArrayList());
          try {
            yamlConfiguration1.save(f2);
          } catch (IOException e) {
            
            e.printStackTrace();
          } 
          
          p.teleport(world.getSpawnLocation());
          FirstBorderShaped.ShapeBorder(world);
          if (Main.JavaPlugin.getConfig().getBoolean("ClearInventoryBeforeCreate")) {
            p.getInventory().clear();
            p.sendMessage(Variable.Lang_YML.getString("ClearInventoryBeforeCreate"));
          } 
          
          for (int c = 0; c < Main.JavaPlugin.getConfig().getStringList("DispathCommand").size(); c++) {
            String temp = Main.JavaPlugin.getConfig().getStringList("DispathCommand").get(c);
            if (temp.contains("<Name>")) {
              temp = temp.replace("<Name>", p.getName());
            }
            
            if (temp.contains("[console]")) {
              temp = temp.replace("[console]", "");
              Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), temp);
            } else if (temp.contains("[player]")) {
              temp = temp.replace("[player]", "");
              Bukkit.dispatchCommand((CommandSender)p, temp);
            }
          
          }
        
        } else {
          
          sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
          sender.sendMessage(Variable.Lang_YML.getString("AlreadyHome"));
          sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
          return false;
        } 
      } 

      
      return false;
    } 


    
    sender.sendMessage(Variable.Lang_YML.getString("HeadLineTtitle"));
    
    if(Variable.has_no_click_message) {
    	p.sendMessage(Variable.Lang_YML.getString("ErrorHelp"));
    }else {
        TextComponent Click_Message = new TextComponent(Variable.Lang_YML.getString("ErrorHelp"));
        Click_Message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sh Help 1"));
        p.spigot().sendMessage((BaseComponent)Click_Message);
    }
    

    sender.sendMessage(Variable.Lang_YML.getString("BottomLineTtitle"));
    return false;
  }



  
  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 1) {
      List<String> list = new ArrayList<>();
      list.add("open");
      list.add("create");
      list.add("look");
      list.add("tpSet");
      list.add("invite");
      list.add("accept");
      list.add("deny");
      list.add("add");
      list.add("pvp");
      list.add("drop");
      list.add("pickup");
      list.add("public");
      list.add("setspawn");
      list.add("kick");
      list.add("remove");
      list.add("check");
      list.add("rank");
      list.add("sun");
      list.add("rain");
      list.add("night");
      list.add("day");
      list.add("lockTime");
      list.add("lockWeather");
      list.add("reload");
      list.add("mobs");
      list.add("nbt");
      list.add("admin");
      list.add("wholedelete");
      list.add("forceDelete");
      list.add("unLoad");
      list.add("MobSpawn");
      list.add("GameMode");
      
      list.add("flower");
      list.add("popularity");
      list.add("gift");
      list.add("icon");
      list.add("info");
      list.add("setBiome");
      return list;
    } 
    if (args.length == 2) {

      
      if (args[0].equalsIgnoreCase("info")) {
        List<String> list = new ArrayList<>();
        list.add("第一,第二,第三行[逗号为分隔符]");
        return list;
      } 
      
      if (args[0].equalsIgnoreCase("create")) {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("其他类型");
        return list;
      } 
      if (args[0].equalsIgnoreCase("invite")) {
        List<String> list = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
          list.add(p.getName());
        }
        return list;
      } 
      if (args[0].equalsIgnoreCase("kick")) {
        List<String> list = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
          list.add(p.getName());
        }
        return list;
      } 
      if (args[0].equalsIgnoreCase("add")) {
        List<String> list = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
          list.add(p.getName());
        }
        return list;
      } 
      if (args[0].equalsIgnoreCase("remove")) {
        List<String> list = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
          list.add(p.getName());
        }
        return list;
      } 
      if (args[0].equalsIgnoreCase("deny")) {
        List<String> list = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
          list.add(p.getName());
        }
        return list;
      }
      
      if (args[0].equalsIgnoreCase("SetBiome")) {
          List<String> list = new ArrayList<>();
          for(Biome b:Biome.values()) {
        	  list.add(b.toString());
          }
          return list;
        } 
      
      if (args[0].equalsIgnoreCase("undeny")) {
        List<String> list = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
          list.add(p.getName());
        }
        return list;
      } 
      
      if (args[0].equalsIgnoreCase("rank")) {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        return list;
      } 
      
      if (args[0].equalsIgnoreCase("unLoad")) {
        List<String> list = new ArrayList<>();
        for (World p : Bukkit.getWorlds()) {
          list.add(p.getName());
        }
        return list;
      } 
      
      if (args[0].equalsIgnoreCase("forcedelete")) {
        List<String> list = new ArrayList<>();
        for (World p : Bukkit.getWorlds()) {
          list.add(p.getName().replaceAll(Variable.world_prefix, ""));
        }
        return list;
      } 
      
      if (args[0].equalsIgnoreCase("admin")) {
        List<String> list = new ArrayList<>();
        list.add("setSpawn");
        list.add("dimension");
        list.add("export");
        list.add("import");
        list.add("setlevel");
        list.add("pwp");
        return list;
      } 

      
      if (args[0].equalsIgnoreCase("gift")) {
        List<String> list = new ArrayList<>();
        list.add("open");
        list.add("send");
        list.add("inv");
        return list;
      } 
      
      if (args[0].equalsIgnoreCase("flower")) {
        List<String> list = new ArrayList<>();
        list.add("add");
        return list;
      } 
      
      if (args[0].equalsIgnoreCase("popularity")) {
        List<String> list = new ArrayList<>();
        list.add("add");
        return list;
      } 
      
      if (args[0].equalsIgnoreCase("GameMode")) {
        List<String> list = new ArrayList<>();
        list.add("EASY");
        list.add("HARD");
        list.add("PEACEFUL");
        return list;
      } 
    } 


    
    if (args.length == 3 && 
      args[0].equalsIgnoreCase("gift") && args[1].equalsIgnoreCase("send")) {
      List<String> list = new ArrayList<>();
      for (Player p : Bukkit.getOnlinePlayers()) {
        list.add(p.getName());
      }
      return list;
    } 

    
    if (args.length == 3 && 
      args[0].equalsIgnoreCase("gift") && args[1].equalsIgnoreCase("inv")) {
      List<String> list = new ArrayList<>();
      for (Player p : Bukkit.getOnlinePlayers()) {
        list.add(p.getName());
      }
      return list;
    } 

    
    if (args.length == 3 && 
      args[0].equalsIgnoreCase("flower") && args[1].equalsIgnoreCase("add")) {
      List<String> list = new ArrayList<>();
      for (Player p : Bukkit.getOnlinePlayers()) {
        list.add(p.getName());
      }
      return list;
    } 

    
    if (args.length == 3 && 
      args[0].equalsIgnoreCase("popularity") && args[1].equalsIgnoreCase("add")) {
      List<String> list = new ArrayList<>();
      for (Player p : Bukkit.getOnlinePlayers()) {
        list.add(p.getName());
      }
      return list;
    } 



    
    return null;
  }
}


