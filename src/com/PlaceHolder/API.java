package com.PlaceHolder;

import com.SelfHome.Main;
import com.SelfHome.Redis;
import com.SelfHome.Variable;
import com.SelfHome.init;
import com.Util.Home;
import com.Util.HomeAPI;
import com.Util.MySQL;
import com.Util.StaticsTick;
import com.Util.Util;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Nonnull;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;








public class API
  extends PlaceholderExpansion
{
  public static List<StaticsTick> world_StaticsTick = new ArrayList<>();


  
  public boolean canRegister() { return true; }


  
  @Nonnull
  public String getIdentifier() { return "SelfHome"; }


  
  @Nonnull
  public String getAuthor() { return "创作: QQ1242839141"; }


  
  @Nonnull
  public String getVersion() { return "1.0.0"; }





  
  public static String getRankName(int level) {
    init.refreshWorldStatics(false);
    
    if (Variable.world_StaticsTick.size() > level - 1) {
      return ((StaticsTick)Variable.world_StaticsTick.get(level - 1)).name;
    }
    return "";
  }




  
  public static String getCache(String p, String papi_name) {
    String temp = null;
    
    for (int i = 0; i < Variable.cache.size(); i++) {
      Redis cache = Variable.cache.get(i);
      if (cache.name.equalsIgnoreCase(p) && cache.papi_name.equalsIgnoreCase(papi_name)) {
        temp = cache.before_value;
        
        break;
      } 
    } 
    return temp;
  }
  
  public static void putCache(String p, String papi_name, String value) {
    List<Redis> cache = Variable.cache;
    Redis d = new Redis(p, papi_name, value);
    cache.add(d);
    Variable.cache = cache;
  }
  
  @Nonnull
  public String onRequest(OfflinePlayer player, String check) {
    String result_check = null;

    
    if (player == null) {
      
      if (check.contains("World_Name_")) {
        String home_name = check.replace("World_Name_", "");
        
        if (Util.CheckIsHome(home_name)) {
          String temp = Variable.Lang_YML.getString("PlaceHolders.WorldName");
          if (temp.contains("<PlayerName>")) {
            temp = temp.replace("<PlayerName>", 
                home_name);
          }
          if (temp.contains("<WorldName>")) {
            temp = temp.replace("<WorldName>", 
                home_name);
          }
          home_name = temp;
        } else if (Util.getAliasName(home_name) != null) {
          home_name = Util.getAliasName(home_name);
        } 
        result_check = String.valueOf(home_name);
      } 

      
      if (check.contains("World_Popularity_")) {
        String temp = check.replace("World_Popularity_", "");
        if (!Util.CheckIsHome(temp)) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
          return result_check;
        } 
        Home home = HomeAPI.getHome(temp);
        result_check = String.valueOf(home.getPopularity());
        return result_check;
      } 
      
      if (check.contains("World_Calc_")) {
        String temp = check.replace("World_Calc_", "");
        if (!Util.CheckIsHome(temp)) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
          return result_check;
        } 
        Home home = HomeAPI.getHome(temp);
        result_check = String.valueOf(home.getPopularity() * Main.JavaPlugin.getConfig().getDouble("PopularityAdd") + home.getFlowers() * Main.JavaPlugin.getConfig().getDouble("FlowerAdd"));
      } 

      
      if (check.contains("World_Flower_")) {
        String temp = check.replace("World_Flower_", "");
        if (!Util.CheckIsHome(temp)) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
          return result_check;
        } 
        Home home = HomeAPI.getHome(temp);
        result_check = String.valueOf(home.getFlowers());
        return result_check;
      } 
      
      if (check.contains("World_TrustList_")) {
        String temp = check.replace("World_TrustList_", "");
        if (!Util.CheckIsHome(temp)) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
          return result_check;
        } 
        
        if (Variable.bungee) {
          result_check = MySQL.getMembers(temp).toString();
        } else {
          
          File f = new File(Variable.Tempf, String.valueOf(temp) + ".yml");
          YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
          List<String> list = yml.getStringList("Members");
          result_check = list.toString();
        } 
      } 
      
      if (check.contains("World_ManageList_")) {
        String temp = check.replace("World_ManageList_", "");
        if (!Util.CheckIsHome(temp)) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
          return result_check;
        } 
        
        if (Variable.bungee) {
          result_check = MySQL.getOP(temp).toString();
        } else {
          
          File f = new File(Variable.Tempf, String.valueOf(temp) + ".yml");
          YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
          List<String> list = yml.getStringList("OP");
          result_check = list.toString();
        } 
      } 
      
      if (check.contains("World_BlackList_")) {
        String temp = check.replace("World_BlackList_", "");
        if (!Util.CheckIsHome(temp)) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
          return result_check;
        } 
        
        if (Variable.bungee) {
          result_check = MySQL.getDenys(temp).toString();
        } else {
          File f = new File(Variable.Tempf, String.valueOf(temp) + ".yml");
          YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
          List<String> list = yml.getStringList("Denys");
          result_check = list.toString();
        } 
      } 
      
      if (check.contains("World_Level_")) {
        String temp = check.replace("World_Level_", "");
        if (!Util.CheckIsHome(temp)) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
          return result_check;
        } 
        if (Variable.bungee) {
          result_check = MySQL.getLevel(temp);
        } else {
          File f = new File(Variable.Tempf, 
              String.valueOf(temp) + ".yml");
          YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
          result_check = String.valueOf(yml.getInt("Level"));
        } 
      } 
      
      if (check.contains("World_Type_")) {
        String temp = check.replace("World_Type_", "");
        if (!Util.CheckIsHome(temp)) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
          return result_check;
        } 

        
        result_check = Variable.Lang_YML.getString("PlaceHolders.Error");

        
        if (Bukkit.getWorld(String.valueOf(Variable.world_prefix) + temp) != null) {
          World world = Bukkit.getWorld(String.valueOf(Variable.world_prefix) + temp);
          result_check = world.getWorldType().toString();
        } 
      } 


      
      return result_check;
    }




    
    String cache = getCache(player.getPlayer().getName(), check);
    if (cache != null) {
      return cache;
    }



    
    if (check.equalsIgnoreCase("GetHomeName")) {
      Player p = player.getPlayer();
      Home home = HomeAPI.getHome(p.getName());
      if (home != null) {
        result_check = home.getName();
      } else {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
      }
      return result_check;
    } 

    
    if (check.equalsIgnoreCase("Name")) {
      String temp = player.getPlayer().getWorld().getName().replace(Variable.world_prefix, "");
      if (Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        temp = Variable.Lang_YML.getString("PlaceHolders.WorldName");
        if (temp.contains("<PlayerName>")) {
          temp = temp.replace("<PlayerName>", 
              player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""));
        }
        if (temp.contains("<WorldName>")) {
          temp = temp.replace("<WorldName>", 
              player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""));
        }
      } else if (Util.getAliasName(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, "")) != null) {
        temp = Util.getAliasName(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""));
      } else if (!PlaceholderAPI.setPlaceholders(player.getPlayer(), "%multiverse_world_alias%").equalsIgnoreCase("%multiverse_world_alias%")) {
        temp = PlaceholderAPI.setPlaceholders(player.getPlayer(), "%multiverse_world_alias%");
      } 
      result_check = String.valueOf(temp);
    } 
    
    if (check.equalsIgnoreCase("World_Alias")) {
      String temp = player.getPlayer().getWorld().getName().replace(Variable.world_prefix, "");
      if (Util.getAliasName(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, "")) != null) {
        temp = Util.getAliasName(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""));
      }
      else if (!PlaceholderAPI.setPlaceholders(player, "%multiverse_world_alias%").equalsIgnoreCase("%multiverse_world_alias%")) {
        temp = PlaceholderAPI.setPlaceholders(player, "%multiverse_world_alias%");
      } 
      result_check = String.valueOf(temp);
    } 




    
    if (check.equalsIgnoreCase("Tile")) {
      World world = Bukkit.getWorld(String.valueOf(Variable.world_prefix) + player.getPlayer().getName());
      if (world == null) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.Error");
        return result_check;
      } 
      
      
      int amount = 0;
      try {
          for(Chunk chunk:world.getLoadedChunks()) {
        	  for(BlockState bs:chunk.getTileEntities()) {
        		  amount++;
        	  }
          }
      }catch(ExceptionInInitializerError e) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Error");
          return result_check;
      }

      
      result_check = String.valueOf(amount);
    } 
    
    if (check.equalsIgnoreCase("World_Tile")) {
      World world = Bukkit.getWorld(
          String.valueOf(Variable.world_prefix) + player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""));
      if (world == null) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.Error");
        return result_check;
      } 
      int amount = 0;
      
      try {
          for(Chunk chunk:world.getLoadedChunks()) {
        	  for(BlockState bs:chunk.getTileEntities()) {
        		  amount++;
        	  }
          }
      }catch(ExceptionInInitializerError e) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Error");
          return result_check;
      }
      
      result_check = String.valueOf(amount);
    } 
    
    if (check.equalsIgnoreCase("Entity")) {
      World world = Bukkit.getWorld(String.valueOf(Variable.world_prefix) + player.getPlayer().getName());
      if (world == null) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.Error");
        return result_check;
      } 
      
      int amount = 0;
for(Chunk chunk : world.getLoadedChunks()) {
	amount +=chunk.getEntities().length;
}
      
      result_check = String.valueOf(amount);
    } 
    
    if (check.equalsIgnoreCase("World_Entity")) {
      
      World world = Bukkit.getWorld(
          String.valueOf(Variable.world_prefix) + player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""));
      if (world == null) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.Error");
        return result_check;
      } 
      
      int amount = 0; int b; int k; Chunk[] arrayOfChunk;

					for(Chunk chunk : world.getLoadedChunks()) {
						amount +=chunk.getEntities().length;
					}

      
      result_check = String.valueOf(amount);
    } 

    
    if (check.equalsIgnoreCase("DropItem")) {
      World world = Bukkit.getWorld(String.valueOf(Variable.world_prefix) + player.getPlayer().getName());
      if (world == null) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.Error");
        return result_check;
      } 
      
      int amount = 0;


for(Chunk chunk : world.getLoadedChunks()) {
	for(Entity entity:chunk.getEntities()) {
		if (entity.getType() == EntityType.DROPPED_ITEM) {
			 amount++; 
		}
		
	}
}
      result_check = String.valueOf(amount);
    } 
    
    if (check.equalsIgnoreCase("World_DropItem")) {
      World world = Bukkit.getWorld(
          String.valueOf(Variable.world_prefix) + player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""));
      if (world == null) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.Error");
        return result_check;
      } 
      
      int amount = 0;
for(Chunk chunk : world.getLoadedChunks()) {
	for(Entity entity:chunk.getEntities()) {
		if (entity.getType() == EntityType.DROPPED_ITEM) {
			 amount++; 
		}
		
	}
}
      result_check = String.valueOf(amount);
    } 
    
    if (check.equalsIgnoreCase("World")) {
      result_check = String.valueOf(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""));
    }

    
    if (check.equalsIgnoreCase("World_Flower")) {
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
        return result_check;
      } 
      Home home = HomeAPI.getHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""));
      result_check = String.valueOf(home.getFlowers());
      return result_check;
    } 
    
    if (check.equalsIgnoreCase("Flower")) {
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      
      Home home = HomeAPI.getHome(player.getPlayer().getName());
      result_check = String.valueOf(home.getFlowers());
      return result_check;
    } 
    
    if (check.equalsIgnoreCase("World_Popularity")) {
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
        return result_check;
      } 
      Home home = HomeAPI.getHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""));
      result_check = String.valueOf(home.getPopularity());
      return result_check;
    } 
    
    if (check.equalsIgnoreCase("Popularity")) {
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      Home home = HomeAPI.getHome(player.getPlayer().getName());
      result_check = String.valueOf(home.getPopularity());
      return result_check;
    } 






    
    if (check.equalsIgnoreCase("World_Level")) {
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
        return result_check;
      } 
      if (Variable.bungee) {
        result_check = MySQL.getLevel(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""));
      } else {
        File f = new File(Variable.Tempf, 
            String.valueOf(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        result_check = String.valueOf(yml.getInt("Level"));
      } 
    } 
    
    if (check.equalsIgnoreCase("Level")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        result_check = MySQL.getLevel(player.getPlayer().getName());
      } else {
        File f = new File(Variable.Tempf, String.valueOf(player.getPlayer().getName()) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        result_check = String.valueOf(yml.getInt("Level"));
      } 
    } 

    
    if (check.equalsIgnoreCase("pvp")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        if (MySQL.getPVP(player.getPlayer().getName()).equalsIgnoreCase("true")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        } 
      } else {
        File f = new File(Variable.Tempf, String.valueOf(player.getPlayer().getName()) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        if (yml.getBoolean("pvp")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        } 
      } 
    } 

    
    if (check.equalsIgnoreCase("World_pvp")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        if (MySQL.getPVP(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))
          .equalsIgnoreCase("true")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        } 
      } else {
        File f = new File(Variable.Tempf, 
            String.valueOf(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        if (yml.getBoolean("pvp")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        } 
      } 
    } 

    
    if (check.equalsIgnoreCase("pickup")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        if (MySQL.getpickup(player.getPlayer().getName()).equalsIgnoreCase("true")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        } 
      } else {
        
        File f = new File(Variable.Tempf, String.valueOf(player.getPlayer().getName()) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        if (yml.getBoolean("pickup")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        } 
      } 
    } 


    
    if (check.equalsIgnoreCase("World_pickup")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        if (MySQL.getpickup(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))
          .equalsIgnoreCase("true")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        } 
      } else {
        
        File f = new File(Variable.Tempf, 
            String.valueOf(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        if (yml.getBoolean("pickup")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        } 
      } 
    } 

    
    if (check.equalsIgnoreCase("drop")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        if (MySQL.getdropitem(player.getPlayer().getName()).equalsIgnoreCase("true")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        } 
      } else {
        
        File f = new File(Variable.Tempf, String.valueOf(player.getPlayer().getName()) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        if (yml.getBoolean("drop")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        } 
      } 
    } 


    
    if (check.equalsIgnoreCase("World_drop")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        if (MySQL.getdropitem(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))
          .equalsIgnoreCase("true")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        } 
      } else {
        
        File f = new File(Variable.Tempf, 
            String.valueOf(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        if (yml.getBoolean("drop")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        } 
      } 
    } 


    
    if (check.equalsIgnoreCase("trustList")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        
        result_check = MySQL.getMembers(player.getPlayer().getName()).toString();
      }
      else {
        
        File f = new File(Variable.Tempf, String.valueOf(player.getPlayer().getName()) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        List<String> list = yml.getStringList("Members");
        result_check = list.toString();
      } 
    } 


    
    if (check.equalsIgnoreCase("World_trustList")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        
        result_check = MySQL.getMembers(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))
          .toString();
      }
      else {
        
        File f = new File(Variable.Tempf, 
            String.valueOf(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        List<String> list = yml.getStringList("Members");
        result_check = list.toString();
      } 
    } 


    
    if (check.equalsIgnoreCase("ManagerList")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        
        result_check = MySQL.getOP(player.getPlayer().getName()).toString();
      }
      else {
        
        File f = new File(Variable.Tempf, String.valueOf(player.getPlayer().getName()) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        List<String> list = yml.getStringList("OP");
        result_check = list.toString();
      } 
    } 

    
    if (check.equalsIgnoreCase("World_ManagerList")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        
        result_check = MySQL.getOP(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))
          .toString();
      } else {
        
        File f = new File(Variable.Tempf, 
            String.valueOf(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        List<String> list = yml.getStringList("OP");
        result_check = list.toString();
      } 
    } 

    
    if (check.equalsIgnoreCase("BlackList")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        
        result_check = MySQL.getDenys(player.getPlayer().getName()).toString();
      }
      else {
        
        File f = new File(Variable.Tempf, String.valueOf(player.getPlayer().getName()) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        List<String> list = yml.getStringList("Denys");
        result_check = list.toString();
      } 
    } 


    
    if (check.equalsIgnoreCase("World_BlackList")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        
        result_check = MySQL.getDenys(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))
          .toString();
      } else {
        
        File f = new File(Variable.Tempf, 
            String.valueOf(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        List<String> list = yml.getStringList("Denys");
        result_check = list.toString();
      } 
    } 


    
    if (check.equalsIgnoreCase("TrustAmount")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        
        result_check = String.valueOf(MySQL.getMembers(player.getPlayer().getName()).size());
      }
      else {
        
        File f = new File(Variable.Tempf, String.valueOf(player.getPlayer().getName()) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        result_check = String.valueOf(yml.getStringList("Members").size());
      } 
    } 


    
    if (check.equalsIgnoreCase("ManagerAmount")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        
        result_check = String.valueOf(MySQL.getOP(player.getPlayer().getName()).size());
      } else {
        
        File f = new File(Variable.Tempf, String.valueOf(player.getPlayer().getName()) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        result_check = String.valueOf(yml.getStringList("OP").size());
      } 
    } 


    
    if (check.equalsIgnoreCase("Radius")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        
        result_check = String.valueOf(Main.JavaPlugin.getConfig().getInt("WorldBoard") + (
            Integer.valueOf(MySQL.getLevel(player.getPlayer().getName())).intValue() - 1) * 
            Main.JavaPlugin.getConfig().getInt("UpdateRadius"));
      }
      else {
        
        File f = new File(Variable.Tempf, String.valueOf(player.getPlayer().getName()) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        result_check = String.valueOf(Main.JavaPlugin.getConfig().getInt("WorldBoard") + (
            yml.getInt("Level") - 1) * Main.JavaPlugin.getConfig().getInt("UpdateRadius"));
      } 
    } 


    
    if (check.equalsIgnoreCase("World_Radius")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        result_check = String.valueOf(Main.JavaPlugin.getConfig().getInt("WorldBoard") + (Integer.valueOf(
              MySQL.getLevel(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))).intValue() - 1) * 
            Main.JavaPlugin.getConfig().getInt("UpdateRadius"));
      } else {
        
        File f = new File(Variable.Tempf, 
            String.valueOf(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        result_check = String.valueOf(Main.JavaPlugin.getConfig().getInt("WorldBoard") + (
            yml.getInt("Level") - 1) * Main.JavaPlugin.getConfig().getInt("UpdateRadius"));
      } 
    } 

    
    if (check.equalsIgnoreCase("World_OwnerName")) {
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
        return result_check;
      } 
      
      result_check = player.getPlayer().getWorld().getName().replace(Variable.world_prefix, "");
    } 

    
    if (check.equalsIgnoreCase("World_OwnerDisplayName")) {
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
        return result_check;
      } 
      
      Player p = Bukkit.getPlayer(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""));
      result_check = p.getDisplayName();
    } 

    
    if (check.equalsIgnoreCase("World_TeleportLocation")) {
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        result_check = String.valueOf("§aX: §d" + String.format("%.2f", Double.valueOf(MySQL.getX(player.getPlayer().getWorld().getName()))) + "§a,Y: §d" + 
        		String.format("%.2f", Double.valueOf(MySQL.getY(player.getPlayer().getWorld().getName()))) + "§a,Z: §d" + 
        		String.format("%.2f", Double.valueOf(MySQL.getZ(player.getPlayer().getWorld().getName()))));
      } else {
        File f = new File(Variable.Tempf, 
            String.valueOf(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        result_check = String.valueOf("§aX: §d" + String.format("%.2f",yml.getDouble("X")) + "§a,Y: §d" + String.format("%.2f", yml.getDouble("Y")) + 
            "§a,Z: §d" + String.format("%.2f", yml.getDouble("Z")));
      } 
    } 

    
    if (check.equalsIgnoreCase("locktime")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        
        if (MySQL.getlocktime(player.getPlayer().getName()).equalsIgnoreCase("true")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        }
      
      } else {
        
        File f = new File(Variable.Tempf, String.valueOf(player.getPlayer().getName()) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        if (yml.getBoolean("locktime")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        } 
      } 
    } 


    
    if (check.equalsIgnoreCase("World_locktime")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        
        if (MySQL.getlocktime(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))
          .equalsIgnoreCase("true")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        }
      
      } else {
        
        File f = new File(Variable.Tempf, 
            String.valueOf(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        if (yml.getBoolean("locktime")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        } 
      } 
    } 

    
    if (check.equalsIgnoreCase("lockweather")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        
        if (MySQL.getlockweather(player.getPlayer().getName()).equalsIgnoreCase("true")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        }
      
      } else {
        
        File f = new File(Variable.Tempf, String.valueOf(player.getPlayer().getName()) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        
        if (yml.getBoolean("lockweather")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        } 
      } 
    } 


    
    if (check.equalsIgnoreCase("World_lockweather")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        
        if (MySQL.getlockweather(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))
          .equalsIgnoreCase("true")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        } 
      } else {
        
        File f = new File(Variable.Tempf, 
            String.valueOf(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        if (yml.getBoolean("lockweather")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
        } 
      } 
    } 

    
    if (check.equalsIgnoreCase("Public")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        
        if (MySQL.getPublic(player.getPlayer().getName()).equalsIgnoreCase("true")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Public");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NoPublic");
        }
      
      } else {
        
        File f = new File(Variable.Tempf, String.valueOf(player.getPlayer().getName()) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        if (yml.getBoolean("Public")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Public");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NoPublic");
        } 
      } 
    } 


    
    if (check.equalsIgnoreCase("World_Public")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        
        if (MySQL.getPublic(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))
          .equalsIgnoreCase("true")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Public");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NoPublic");
        }
      
      } else {
        
        File f = new File(Variable.Tempf, 
            String.valueOf(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, "")) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        if (yml.getBoolean("Public")) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.Public");
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NoPublic");
        } 
      } 
    } 


    
    if (check.equalsIgnoreCase("time")) {
      Long time = Long.valueOf(player.getPlayer().getWorld().getTime());
      Date date = new Date(time.longValue());
      result_check = String.valueOf(String.valueOf(date.getHours()) + ":" + date.getMinutes() + ":" + date.getSeconds());
    } 
    
    if (check.equalsIgnoreCase("UpdateMoney")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        
        if (Main.JavaPlugin.getConfig().getInt("MaxLevel") != 
          Integer.valueOf(MySQL.getLevel(player.getPlayer().getName())).intValue()) {
          result_check = String.valueOf(Main.JavaPlugin.getConfig().getDoubleList("MoneyNeed")
              .get(Integer.valueOf(MySQL.getLevel(player.getPlayer().getName())).intValue() - 1));
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.HasAlreadyReachLevelMax");
        }
      
      } else {
        
        File f = new File(Variable.Tempf, String.valueOf(player.getPlayer().getName()) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        
        if (Main.JavaPlugin.getConfig().getInt("MaxLevel") != yml.getInt("Level")) {
          result_check = String.valueOf(
              Main.JavaPlugin.getConfig().getDoubleList("MoneyNeed").get(yml.getInt("Level") - 1));
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.HasAlreadyReachLevelMax");
        } 
      } 
    } 


    
    if (check.equalsIgnoreCase("UpdatePoints")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        
        if (Main.JavaPlugin.getConfig().getInt("MaxLevel") != 
          Integer.valueOf(MySQL.getLevel(player.getPlayer().getName())).intValue()) {
          result_check = String.valueOf(Main.JavaPlugin.getConfig().getDoubleList("PointsNeed")
              .get(Integer.valueOf(MySQL.getLevel(player.getPlayer().getName())).intValue() - 1));
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.HasAlreadyReachLevelMax");
        } 
      } else {
        
        File f = new File(Variable.Tempf, String.valueOf(player.getPlayer().getName()) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        if (Main.JavaPlugin.getConfig().getInt("MaxLevel") != yml.getInt("Level")) {
          result_check = String.valueOf(
              Main.JavaPlugin.getConfig().getDoubleList("PointsNeed").get(yml.getInt("Level") - 1));
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.HasAlreadyReachLevelMax");
        } 
      } 
    } 


    
    if (check.equalsIgnoreCase("UpdateItems")) {
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        if (Main.JavaPlugin.getConfig().getInt("MaxLevel") != 
          Integer.valueOf(MySQL.getLevel(player.getPlayer().getName())).intValue()) {
          result_check = String.valueOf(((String)Main.JavaPlugin.getConfig().getStringList("ItemsNeed")
              .get(Integer.valueOf(MySQL.getLevel(player.getPlayer().getName())).intValue() - 1)).split(",")[0]);
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.HasAlreadyReachLevelMax");
        } 
      } else {
        File f = new File(Variable.Tempf, String.valueOf(player.getPlayer().getName()) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        if (Main.JavaPlugin.getConfig().getInt("MaxLevel") != yml.getInt("Level")) {
          result_check = String.valueOf(((String)Main.JavaPlugin.getConfig().getStringList("ItemsNeed")
              .get(yml.getInt("Level") - 1)).split(",")[0]);
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.HasAlreadyReachLevelMax");
        } 
      } 
    } 


    
    if (check.equalsIgnoreCase("UpdateItemsChineseName")) {
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        if (Main.JavaPlugin.getConfig().getInt("MaxLevel") != 
          Integer.valueOf(MySQL.getLevel(player.getPlayer().getName())).intValue()) {
          result_check = String.valueOf(((String)Main.JavaPlugin.getConfig().getStringList("ItemsChineseName")
              .get(Integer.valueOf(MySQL.getLevel(player.getPlayer().getName())).intValue() - 1)).split(",")[0]);
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.HasAlreadyReachLevelMax");
        } 
      } else {
        
        File f = new File(Variable.Tempf, String.valueOf(player.getPlayer().getName()) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        if (Main.JavaPlugin.getConfig().getInt("MaxLevel") != yml.getInt("Level")) {
          result_check = String.valueOf(((String)Main.JavaPlugin.getConfig().getStringList("ItemsChineseName")
              .get(yml.getInt("Level") - 1)).split(",")[0]);
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.HasAlreadyReachLevelMax");
        } 
      } 
    } 

    
    if (check.equalsIgnoreCase("UpdateItemsAmount")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      
      if (Variable.bungee) {
        if (Main.JavaPlugin.getConfig().getInt("MaxLevel") != 
          Integer.valueOf(MySQL.getLevel(player.getPlayer().getName())).intValue()) {
          result_check = String.valueOf(((String)Main.JavaPlugin.getConfig().getStringList("ItemsNeed")
              .get(Integer.valueOf(MySQL.getLevel(player.getPlayer().getName())).intValue() - 1)).split(",")[1]);
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.HasAlreadyReachLevelMax");
        } 
      } else {
        
        File f = new File(Variable.Tempf, String.valueOf(player.getPlayer().getName()) + ".yml");
        YamlConfiguration yml =  YamlConfiguration.loadConfiguration(f);
        if (Main.JavaPlugin.getConfig().getInt("MaxLevel") != yml.getInt("Level")) {
          result_check = String.valueOf(((String)Main.JavaPlugin.getConfig().getStringList("ItemsNeed")
              .get(yml.getInt("Level") - 1)).split(",")[1]);
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.HasAlreadyReachLevelMax");
        } 
      } 
    } 


    
    if (check.equalsIgnoreCase("UpdateRadius")) {
      result_check = String.valueOf(Main.JavaPlugin.getConfig().getInt("UpdateRadius"));
    }
    
    if (check.equalsIgnoreCase("MaxTiles")) {
      result_check = String.valueOf(Main.JavaPlugin.getConfig().getInt("MaxTiles"));
    }
    
    if (check.equalsIgnoreCase("UnLoadTiles")) {
      result_check = String.valueOf(Main.JavaPlugin.getConfig().getInt("UnLoadTiles"));
    }
    if (check.equalsIgnoreCase("MaxOP")) {
      result_check = String.valueOf(Main.JavaPlugin.getConfig().getInt("MaxOP"));
    }
    if (check.equalsIgnoreCase("MaxJoin")) {
      result_check = String.valueOf(Main.JavaPlugin.getConfig().getInt("MaxJoin"));
    }
    if (check.equalsIgnoreCase("MaxLevel")) {
      result_check = String.valueOf(Main.JavaPlugin.getConfig().getInt("MaxLevel"));
    }
    if (check.equalsIgnoreCase("MaxDelete")) {
      result_check = String.valueOf(Main.JavaPlugin.getConfig().getInt("MaxDelete"));
    }
    if (check.equalsIgnoreCase("DeleteItems")) {
      result_check = String.valueOf(Main.JavaPlugin.getConfig().getInt("DeleteItems"));
    }
    if (check.equalsIgnoreCase("DeleteEntities")) {
      result_check = String.valueOf(Main.JavaPlugin.getConfig().getInt("DeleteEntities"));
    }
    if (check.equalsIgnoreCase("Server")) {
      result_check = String.valueOf(Main.JavaPlugin.getConfig().getString("Server"));
    }
    if (check.equalsIgnoreCase("Prefix")) {
      result_check = String.valueOf(Main.JavaPlugin.getConfig().getString("Prefix"));
    }
    if (check.equalsIgnoreCase("Normal_WorldBoard")) {
      result_check = String.valueOf(Main.JavaPlugin.getConfig().getInt("WorldBoard"));
    }
    
    if (check.equalsIgnoreCase("KeepInventory")) {
      if (player.getPlayer().getWorld().getGameRuleValue("keepInventory").equalsIgnoreCase("true")) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
      } else {
        result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
      } 
    }
    
    if (check.equalsIgnoreCase("doMobSpawning")) {
      if (player.getPlayer().getWorld().getGameRuleValue("doMobSpawning").equalsIgnoreCase("true")) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
      } else {
        result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
      } 
    }
    
    if (check.equalsIgnoreCase("mobGriefing")) {
      if (player.getPlayer().getWorld().getGameRuleValue("mobGriefing").equalsIgnoreCase("false")) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
      } else {
        result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
      } 
    }
    
    if (check.equalsIgnoreCase("doFireTick")) {
      if (player.getPlayer().getWorld().getGameRuleValue("doFireTick").equalsIgnoreCase("false")) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
      } else {
        result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
      } 
    }
    
    if (check.equalsIgnoreCase("World_Difficulty")) {
      if (player.getPlayer().getWorld().getDifficulty() == Difficulty.EASY) {
        result_check = "简单";
      } else if (player.getPlayer().getWorld().getDifficulty() == Difficulty.NORMAL) {
        result_check = "普通";
      } else if (player.getPlayer().getWorld().getDifficulty() == Difficulty.HARD) {
        result_check = "困难";
      } else if (player.getPlayer().getWorld().getDifficulty() == Difficulty.PEACEFUL) {
        result_check = "和平";
      } 
    }
    
    if (check.equalsIgnoreCase("World_generateStructures")) {
      if (player.getPlayer().getWorld().canGenerateStructures()) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.Enable");
      } else {
        result_check = Variable.Lang_YML.getString("PlaceHolders.Disable");
      } 
    }
    
    if (check.equalsIgnoreCase("WorldList")) {
      List<String> list = new ArrayList<>();
      if (Variable.bungee) {
        List<String> lis = MySQL.getAllWorlds();
        list = lis;
      } else {
        File folder = new File(Variable.Tempf); int b; int k; File[] arrayOfFile;
        for (k = (arrayOfFile = folder.listFiles()).length, b = 0; b < k; ) { File temp = arrayOfFile[b];
          String want_to = temp.getPath().replace(Variable.Tempf, "").replace(".yml", "")
            .replace(Variable.file_loc_prefix, "");
          list.add(want_to); b++; }
      
      } 
      result_check = list.toString();
    } 
    
    if (check.equalsIgnoreCase("WorldAmount"))
    {
      if (Variable.bungee) {
        result_check = String.valueOf(MySQL.getAllWorldsAmount());
      } else {
        File folder = new File(Variable.Tempf);
        result_check = String.valueOf((folder.listFiles()).length);
      } 
    }

    
    if (check.equalsIgnoreCase("WholeMaxDelete")) {
      result_check = String.valueOf(Main.JavaPlugin.getConfig().getInt("MaxDelete"));
    }
    
    if (check.equalsIgnoreCase("WholeDelete")) {
      YamlConfiguration yml =  YamlConfiguration.loadConfiguration(Variable.f_log);
      List<String> list = yml.getStringList("DeleteTimes");
      if (list == null) {
        result_check = String.valueOf(0);
      }
      boolean check_contain = false;
      for (int c = 0; c < list.size(); c++) {
        String[] temp3 = ((String)list.get(c)).split(",");
        String name = temp3[0];
        if (name.equalsIgnoreCase(player.getPlayer().getName())) {
          check_contain = true;
          result_check = temp3[1];
        } 
      } 
      if (!check_contain) {
        result_check = String.valueOf(0);
      }
    } 
    
    if (check.equalsIgnoreCase("LoadWorlds")) {
      int amount = 0;
      for (World world : Bukkit.getWorlds()) {
        if (Util.CheckIsHome(world.getName().replace(Variable.world_prefix, ""))) {
          amount++;
        }
      } 
      result_check = String.valueOf(amount);
    } 
    
    if (check.toLowerCase().contains("HasPermission_")) {
      String temp = check.replace("HasPermission_", "");
      if (player.getPlayer().hasPermission("SelfHome." + temp)) {
        result_check = String.valueOf(Variable.Lang_YML.getString("PlaceHolders.HasPermision"));
      } else {
        result_check = String.valueOf(Variable.Lang_YML.getString("PlaceHolders.NoPermission"));
      } 
    } 


    
    if (check.equalsIgnoreCase("hasPermission")) {
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NotIsHome");
        return result_check;
      } 
      if (Util.Check(player.getPlayer(), player.getPlayer().getWorld().getName().replace(Variable.world_prefix, "")).booleanValue()) {
        result_check = "true";
      } else {
        result_check = "false";
      } 
    } 

    
    if (check.toUpperCase().contains("LEVELTOP_")) {
      String level = check.toUpperCase().replace("LEVELTOP_", "");
      if (Variable.bungee) {
        result_check = MySQL.getLevelTop(level);
      } else {
        File folder = new File(Variable.Tempf);
        List<com.PlaceHolder.Home> homelist = new ArrayList<>(); int b; int k; File[] arrayOfFile;
        for (k = (arrayOfFile = folder.listFiles()).length, b = 0; b < k; ) { File temp = arrayOfFile[b];
          String want_to = temp.getPath().replace(Variable.Tempf, "").replace(".yml", "").replace(Variable.file_loc_prefix, "");
          YamlConfiguration yml =  YamlConfiguration.loadConfiguration(temp);
          com.PlaceHolder.Home home = new  com.PlaceHolder.Home(want_to, yml.getInt("Level"), yml.getInt("flowers"), yml.getInt("popularity"));
          homelist.add(home); b++; }
        
        for (int i = 0; i < homelist.size() - 1; i++) {
          
          for (int j = 0; j < homelist.size() - 1 - i; j++) {
            
            if ((( com.PlaceHolder.Home)homelist.get(j)).level < (( com.PlaceHolder.Home)homelist.get(j + 1)).level) {
              
               com.PlaceHolder.Home temp = homelist.get(j);
              homelist.set(j, homelist.get(j + 1));
              homelist.set(j + 1, temp);
            } 
          } 
        } 

        
        if (homelist.get(Integer.valueOf(level).intValue() - 1) == null) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
          return result_check;
        } 
        
        result_check = (( com.PlaceHolder.Home)homelist.get(Integer.valueOf(level).intValue() - 1)).name;
      } 
    } 


    
    if (check.equalsIgnoreCase("MyLevelTop")) {
      if (Variable.bungee) {
        if (!MySQL.alreadyhastheplayerhome(player.getPlayer().getName())) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
          return result_check;
        } 
        result_check = String.valueOf(MySQL.getMyLevelTop(player.getPlayer().getName()));
      } else {
        File folder = new File(Variable.Tempf);
        List< com.PlaceHolder.Home> homelist = new ArrayList<>(); int b; int k; File[] arrayOfFile;
        for (k = (arrayOfFile = folder.listFiles()).length, b = 0; b < k; ) { File temp = arrayOfFile[b];
          String want_to = temp.getPath().replace(Variable.Tempf, "").replace(".yml", "").replace(Variable.file_loc_prefix, "");
          YamlConfiguration yml =  YamlConfiguration.loadConfiguration(temp);
           com.PlaceHolder.Home home = new  com.PlaceHolder.Home(want_to, yml.getInt("Level"), yml.getInt("flowers"), yml.getInt("popularity"));
          homelist.add(home); b++; }
        
        for (int i = 0; i < homelist.size() - 1; i++) {
          
          for (int j = 0; j < homelist.size() - 1 - i; j++) {
            
            if ((( com.PlaceHolder.Home)homelist.get(j)).level < (( com.PlaceHolder.Home)homelist.get(j + 1)).level) {
              
               com.PlaceHolder.Home temp = homelist.get(j);
              homelist.set(j, homelist.get(j + 1));
              homelist.set(j + 1, temp);
            } 
          } 
        } 


        
        int i = 0;
        boolean check_contain = false;
        for (i = 0; i < homelist.size(); i++) {
          if ((( com.PlaceHolder.Home)homelist.get(i)).name.equalsIgnoreCase(player.getPlayer().getName())) {
            check_contain = true;
            
            break;
          } 
        } 
        
        if (check_contain) {
          result_check = String.valueOf(i + 1);
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
          return result_check;
        } 
      } 
    }




    
    if (check.toUpperCase().contains("FlowerTOP_".toUpperCase())) {
      String level = check.toUpperCase().replace("FlowerTOP_".toUpperCase(), "");
      if (Variable.bungee) {
        result_check = MySQL.getFlowerTop(level);
      } else {
        File folder = new File(Variable.Tempf);
        List< com.PlaceHolder.Home> homelist = new ArrayList<>(); int b; int k; File[] arrayOfFile;
        for (k = (arrayOfFile = folder.listFiles()).length, b = 0; b < k; ) { File temp = arrayOfFile[b];
          String want_to = temp.getPath().replace(Variable.Tempf, "").replace(".yml", "").replace(Variable.file_loc_prefix, "");
          YamlConfiguration yml =  YamlConfiguration.loadConfiguration(temp);
           com.PlaceHolder.Home home = new  com.PlaceHolder.Home(want_to, yml.getInt("Level"), yml.getInt("flowers"), yml.getInt("popularity"));
          homelist.add(home); b++; }
        
        for (int i = 0; i < homelist.size() - 1; i++) {
          
          for (int j = 0; j < homelist.size() - 1 - i; j++) {
            
            if ((( com.PlaceHolder.Home)homelist.get(j)).flowers < (( com.PlaceHolder.Home)homelist.get(j + 1)).flowers) {
              
               com.PlaceHolder.Home temp = homelist.get(j);
              homelist.set(j, homelist.get(j + 1));
              homelist.set(j + 1, temp);
            } 
          } 
        } 
        
        if (homelist.get(Integer.valueOf(level).intValue() - 1) == null) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
          return result_check;
        } 
        
        result_check = (( com.PlaceHolder.Home)homelist.get(Integer.valueOf(level).intValue() - 1)).name;
      } 
    } 
    
    if (check.toUpperCase().contains("PopularityTOP_".toUpperCase())) {
      String level = check.toUpperCase().replace("PopularityTOP_".toUpperCase(), "");
      if (Variable.bungee) {
        result_check = MySQL.getPopularity(level);
      } else {
        File folder = new File(Variable.Tempf);
        List< com.PlaceHolder.Home> homelist = new ArrayList<>(); int b; int k; File[] arrayOfFile;
        for (k = (arrayOfFile = folder.listFiles()).length, b = 0; b < k; ) { File temp = arrayOfFile[b];
          String want_to = temp.getPath().replace(Variable.Tempf, "").replace(".yml", "").replace(Variable.file_loc_prefix, "");
          YamlConfiguration yml =  YamlConfiguration.loadConfiguration(temp);
           com.PlaceHolder.Home home = new  com.PlaceHolder.Home(want_to, yml.getInt("Level"), yml.getInt("flowers"), yml.getInt("popularity"));
          homelist.add(home); b++; }
        
        for (int i = 0; i < homelist.size() - 1; i++) {
          
          for (int j = 0; j < homelist.size() - 1 - i; j++) {
            
            if ((( com.PlaceHolder.Home)homelist.get(j)).popularity < (( com.PlaceHolder.Home)homelist.get(j + 1)).popularity) {
              
               com.PlaceHolder.Home temp = homelist.get(j);
              homelist.set(j, homelist.get(j + 1));
              homelist.set(j + 1, temp);
            } 
          } 
        } 

        
        if (homelist.get(Integer.valueOf(level).intValue() - 1) == null) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
          return result_check;
        } 
        
        result_check = (( com.PlaceHolder.Home)homelist.get(Integer.valueOf(level).intValue() - 1)).name;
      } 
    } 





    
    if (check.equalsIgnoreCase("MyFlowerTop")) {
      if (Variable.bungee) {
        if (!MySQL.alreadyhastheplayerhome(player.getPlayer().getName())) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
          return result_check;
        } 
        result_check = String.valueOf(MySQL.getMyLevelTop(player.getPlayer().getName()));
      } else {
        File folder = new File(Variable.Tempf);
        List< com.PlaceHolder.Home> homelist = new ArrayList<>(); int b; int k; File[] arrayOfFile;
        for (k = (arrayOfFile = folder.listFiles()).length, b = 0; b < k; ) { File temp = arrayOfFile[b];
          String want_to = temp.getPath().replace(Variable.Tempf, "").replace(".yml", "").replace(Variable.file_loc_prefix, "");
          YamlConfiguration yml =  YamlConfiguration.loadConfiguration(temp);
           com.PlaceHolder.Home home = new  com.PlaceHolder.Home(want_to, yml.getInt("Level"), yml.getInt("flowers"), yml.getInt("popularity"));
          homelist.add(home); b++; }
        
        for (int i = 0; i < homelist.size() - 1; i++) {
          
          for (int j = 0; j < homelist.size() - 1 - i; j++) {
            
            if ((( com.PlaceHolder.Home)homelist.get(j)).flowers < (( com.PlaceHolder.Home)homelist.get(j + 1)).flowers) {
              
               com.PlaceHolder.Home temp = homelist.get(j);
              homelist.set(j, homelist.get(j + 1));
              homelist.set(j + 1, temp);
            } 
          } 
        } 
        int i = 0;
        boolean check_contain = false;
        for (i = 0; i < homelist.size(); i++) {
          if ((( com.PlaceHolder.Home)homelist.get(i)).name.equalsIgnoreCase(player.getPlayer().getName())) {
            check_contain = true;
            
            break;
          } 
        } 
        if (check_contain) {
          result_check = String.valueOf(i + 1);
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
          return result_check;
        } 
      } 
    }




    
    if (check.equalsIgnoreCase("MyPopularityTop")) {
      if (Variable.bungee) {
        if (!MySQL.alreadyhastheplayerhome(player.getPlayer().getName())) {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
          return result_check;
        } 
        result_check = String.valueOf(MySQL.getMyLevelTop(player.getPlayer().getName()));
      } else {
        File folder = new File(Variable.Tempf);
        List< com.PlaceHolder.Home> homelist = new ArrayList<>(); int b; int k; File[] arrayOfFile;
        for (k = (arrayOfFile = folder.listFiles()).length, b = 0; b < k; ) { File temp = arrayOfFile[b];
          String want_to = temp.getPath().replace(Variable.Tempf, "").replace(".yml", "").replace(Variable.file_loc_prefix, "");
          YamlConfiguration yml =  YamlConfiguration.loadConfiguration(temp);
          com.PlaceHolder.Home home = new com.PlaceHolder.Home(want_to, yml.getInt("Level"), yml.getInt("flowers"), yml.getInt("popularity"));
          homelist.add(home); b++; }
        
        for (int i = 0; i < homelist.size() - 1; i++) {
          
          for (int j = 0; j < homelist.size() - 1 - i; j++) {
            
            if ((( com.PlaceHolder.Home)homelist.get(j)).popularity < (( com.PlaceHolder.Home)homelist.get(j + 1)).popularity) {
              
               com.PlaceHolder.Home temp = homelist.get(j);
              homelist.set(j, homelist.get(j + 1));
              homelist.set(j + 1, temp);
            } 
          } 
        } 
        int i = 0;
        boolean check_contain = false;
        for (i = 0; i < homelist.size(); i++) {
          if ((( com.PlaceHolder.Home)homelist.get(i)).name.equalsIgnoreCase(player.getPlayer().getName())) {
            check_contain = true;
            
            break;
          } 
        } 
        if (check_contain) {
          result_check = String.valueOf(i + 1);
        } else {
          result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
          return result_check;
        } 
      } 
    }





    
    if (check.equalsIgnoreCase("Flower")) {
      
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      Home home = HomeAPI.getHome(player.getPlayer().getName());
      result_check = String.valueOf(home.getFlowers());
    } 
    
    if (check.equalsIgnoreCase("World_Flower")) {
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      Home home = HomeAPI.getHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""));
      result_check = String.valueOf(home.getFlowers());
    } 

    
    if (check.equalsIgnoreCase("Popularity")) {
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      Home home = HomeAPI.getHome(player.getPlayer().getName());
      result_check = String.valueOf(home.getPopularity());
    } 
    
    if (check.equalsIgnoreCase("World_Popularity")) {
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      Home home = HomeAPI.getHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""));
      result_check = String.valueOf(home.getPopularity());
    } 



    
    if (check.equalsIgnoreCase("Calc")) {
      if (!Util.CheckIsHome(player.getPlayer().getName())) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      Home home = HomeAPI.getHome(player.getPlayer().getName());
      result_check = String.valueOf(home.getPopularity() * Main.JavaPlugin.getConfig().getDouble("PopularityAdd") + home.getFlowers() * Main.JavaPlugin.getConfig().getDouble("FlowerAdd"));
    } 
    if (check.equalsIgnoreCase("World_Calc")) {
      if (!Util.CheckIsHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""))) {
        result_check = Variable.Lang_YML.getString("PlaceHolders.NoHome");
        return result_check;
      } 
      Home home = HomeAPI.getHome(player.getPlayer().getWorld().getName().replace(Variable.world_prefix, ""));
      result_check = String.valueOf(home.getPopularity() * Main.JavaPlugin.getConfig().getDouble("PopularityAdd") + home.getFlowers() * Main.JavaPlugin.getConfig().getDouble("FlowerAdd"));
    } 

    
    if (check.toUpperCase().contains("RankTOP_".toUpperCase())) {
      int level = Integer.valueOf(check.toUpperCase().replace("RankTOP_".toUpperCase(), "")).intValue();
      result_check = getRankName(level);
    } 

    
    if (player != null) {
      putCache(player.getPlayer().getName(), check, result_check);
      Util.clearCache(player.getPlayer().getName(), check);
    } 







    if (result_check != null) {
      return result_check;
    }
    return "";
  }
}


