package com.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;




























public class InventoryMoveItemListener
  implements Listener
{
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
  public void onOpen(InventoryMoveItemEvent event) {
    if (event.getDestination().getHolder() == null) {
      return;
    }
    boolean check_gui_in_plugins = false;
    
    if (event.getDestination().getHolder() instanceof com.GUI.CheckGui) {
      check_gui_in_plugins = true;
    } else if (event.getDestination().getHolder() instanceof com.GUI.CreateGui) {
      check_gui_in_plugins = true;
    } else if (event.getDestination().getHolder() instanceof com.GUI.DenyGui) {
      check_gui_in_plugins = true;
    } else if (event.getDestination().getHolder() instanceof com.GUI.InviteGui) {
      check_gui_in_plugins = true;
    } else if (event.getDestination().getHolder() instanceof com.GUI.MainGui) {
      check_gui_in_plugins = true;
    } else if (event.getDestination().getHolder() instanceof com.GUI.ManageGui) {
      check_gui_in_plugins = true;
    } else if (event.getDestination().getHolder() instanceof com.GUI.ManageGui2) {
      check_gui_in_plugins = true;
    } else if (event.getDestination().getHolder() instanceof com.GUI.TrustGui) {
      check_gui_in_plugins = true;
    } else if (event.getDestination().getHolder() instanceof com.GUI.VisitGui) {
      check_gui_in_plugins = true;
    } 
    
    if (!check_gui_in_plugins) {
      return;
    }
    event.setCancelled(true);
  }
}


