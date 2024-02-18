package com.Listeners;

import com.SelfHome.Main;
import com.SelfHome.Variable;
import com.Util.Home;
import com.Util.HomeAPI;
import com.Util.Util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityInteractByEntityListener
  implements Listener {
	
	List<String> list = new ArrayList<String>();
	
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onInteract(PlayerInteractAtEntityEvent event) {
    if (event.isCancelled()) {
      return;
    }
    if (event.getRightClicked().getType() == EntityType.PLAYER) {
      return;
    }
    if (!Util.CheckIsHome(event.getPlayer().getWorld().getName())) {
      return;
    }
    if (!Util.Check(event.getPlayer(), event.getPlayer().getWorld().getName()).booleanValue() && 
      Main.JavaPlugin.getConfig().getBoolean("EnableEntityInteract")) {
      event.getPlayer().sendMessage(Variable.Lang_YML.getString("ProtectEntity"));
      event.setCancelled(true);
      
      if(event.getRightClicked().getType().toString().toUpperCase().contains("BLADESTAND") && event.getPlayer().getItemInHand().getType().toString().equalsIgnoreCase("AIR")) {
    	  list.add(event.getPlayer().getName());
    	  return;
      }
      
      if(list.contains(event.getPlayer().getName())) {
    	  
    	  ItemStack item = event.getPlayer().getItemInHand();
    	  
    	  if(!item.getType().toString().toUpperCase().contains("BLADE")) {
    		  list.remove(event.getPlayer().getName());
    		  return;
    	  }
    	  Bukkit.dispatchCommand((CommandSender)event.getPlayer(), "sh gift send " + event.getPlayer().getWorld().getName());
    	  list.remove(event.getPlayer().getName());
    	  
      }
      
      

    } 
  }

}


