package com.GUI;

import com.SelfHome.Main;
import com.SelfHome.Variable;
import java.util.ArrayList;
import java.util.List;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;


public class ManageGui2
  implements InventoryHolder
{
  public Inventory MainGui = Bukkit.createInventory(this, 45, Variable.GUI_YML.getString("Manage2Title"));

  
  public ManageGui2(final Player p) { (new BukkitRunnable()
      {
        public void run()
        {
          ManageGui2.this.MainGui.clear();
          
          if (Variable.GUI_YML.getBoolean("EnableManageGui2NormalPane")) {
            try {
              ItemStack itemStack = new ItemStack(Material.valueOf(Variable.GUI_YML.getString("PaneMaterial")));
            } catch (Exception e) {
              String temp5 = Variable.Lang_YML.getString("GlassPaneNotFound");
              if (temp5.contains("<Material>")) {
                temp5 = temp5.replace("<Material>", Variable.GUI_YML.getString("PaneMaterial"));
              }
              Bukkit.getConsoleSender().sendMessage(temp5);
              return;
            } 
            ItemStack blb1 = new ItemStack(Material.valueOf(Variable.GUI_YML.getString("PaneMaterial")));
            blb1.setDurability((short)15);
            ItemMeta i1 = blb1.getItemMeta();
            i1.setDisplayName("");
            blb1.setItemMeta(i1);
            for (int i = 0; i < 9; i++) {
              ManageGui2.this.MainGui.setItem(i, blb1);
            }
            ManageGui2.this.MainGui.setItem(9, blb1);
            ManageGui2.this.MainGui.setItem(18, blb1);
            ManageGui2.this.MainGui.setItem(27, blb1);
            ManageGui2.this.MainGui.setItem(17, blb1);
            ManageGui2.this.MainGui.setItem(26, blb1);
            ManageGui2.this.MainGui.setItem(35, blb1);
            for (int i = 36; i < 45; i++) {
              if (i != 40) {
                ManageGui2.this.MainGui.setItem(i, blb1);
              }
            } 
          } 
          
          ConfigurationSection cs = Variable.GUI_YML.getConfigurationSection("");
          for (String temp : cs.getKeys(false)) {
            if (Variable.GUI_YML.getString(String.valueOf(temp) + ".InMenu") == null) {
              continue;
            }
            if (!Variable.GUI_YML.getString(String.valueOf(temp) + ".InMenu").equalsIgnoreCase("Manage2")) {
              continue;
            }
            try {
              ItemStack itemStack = new ItemStack(
                  Material.valueOf(Variable.GUI_YML.getString(String.valueOf(temp) + ".Material")));
            } catch (Exception e) {
              String temp5 = Variable.Lang_YML.getString("MaterialNotFound");
              if (temp5.contains("<Material>")) {
                temp5 = temp5.replace("<Material>", Variable.GUI_YML.getString(String.valueOf(temp) + ".Material"));
              }
              if (temp5.contains("<ID>")) {
                temp5 = temp5.replace("<ID>", temp);
              }
              Bukkit.getConsoleSender().sendMessage(temp5);
              return;
            } 
            ItemStack item = new ItemStack(Material.valueOf(Variable.GUI_YML.getString(String.valueOf(temp) + ".Material")));
            if (Variable.GUI_YML.getInt(String.valueOf(temp) + ".SubID") != 0) {
              item.setDurability((short)Variable.GUI_YML.getInt(String.valueOf(temp) + ".SubID"));
            }
            ItemMeta meta = item.getItemMeta();
            List<String> lores = new ArrayList<>();
            meta.setDisplayName(Variable.GUI_YML.getString(String.valueOf(temp) + ".CustomName"));
            for (int i = 0; i < Variable.GUI_YML.getStringList(String.valueOf(temp) + ".Lores").size(); i++) {
              String tempstr = Variable.GUI_YML.getStringList(String.valueOf(temp) + ".Lores").get(i);
              try {
                tempstr = PlaceholderAPI.setPlaceholders(p, tempstr);
              } catch (Exception exception) {}


              
              lores.add(tempstr);
            } 
            
            for (int c = 0; c < Variable.GUI_YML.getStringList(String.valueOf(temp) + ".Enchants").size(); c++) {
              String[] tempenc = ((String)Variable.GUI_YML.getStringList(String.valueOf(temp) + ".Enchants").get(c)).split("\\,");
              meta.addEnchant(Enchantment.getByName(tempenc[0]), Integer.valueOf(tempenc[1]).intValue(), true);
            } 
            meta.setLore(lores);
            item.setItemMeta(meta);
            ManageGui2.this.MainGui.setItem(Variable.GUI_YML.getInt(String.valueOf(temp) + ".Index") - 1, item);
          }
        
        }
      }).runTaskAsynchronously((Plugin)Main.JavaPlugin); }




  
  public Inventory getInventory() { return this.MainGui; }
}


