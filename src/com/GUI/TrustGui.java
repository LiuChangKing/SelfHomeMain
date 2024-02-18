package com.GUI;

import com.SelfHome.Main;
import com.SelfHome.Variable;
import java.util.ArrayList;
import java.util.List;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;



public class TrustGui
  implements InventoryHolder
{
  private Inventory MainGui = Bukkit.createInventory(this, 54, Variable.GUI_YML.getString("TrustTitle"));
  private int MaxPage = 0;
  private int NowPage = 0;
  private List<Player> players = new ArrayList<>();





  
  public TrustGui() { (new BukkitRunnable()
      {
        public void run() {
          TrustGui.this.MaxPage = 0;
          TrustGui.this.NowPage = 0;
          TrustGui.this.players.clear();
          
          boolean next_page = false;
          int amount = 0;
          for (Player p : Bukkit.getOnlinePlayers()) {
            TrustGui.this.players.add(p);
          }
          TrustGui.this.MaxPage = (int)Math.ceil(Bukkit.getOnlinePlayers().size() / 28.0D);




          
          TrustGui.this.MainGui.clear();
          
          if (Variable.GUI_YML.getBoolean("EnableTrustGuiNormalPane")) {
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
              TrustGui.this.MainGui.setItem(i, blb1);
            }
            TrustGui.this.MainGui.setItem(9, blb1);
            TrustGui.this.MainGui.setItem(18, blb1);
            TrustGui.this.MainGui.setItem(27, blb1);
            TrustGui.this.MainGui.setItem(17, blb1);
            TrustGui.this.MainGui.setItem(26, blb1);
            TrustGui.this.MainGui.setItem(35, blb1);
            TrustGui.this.MainGui.setItem(36, blb1);
            TrustGui.this.MainGui.setItem(44, blb1);
            for (int i = 45; i < 54; i++) {
              if (i != 49) {
                TrustGui.this.MainGui.setItem(i, blb1);
              }
            } 
          } 
          
          ItemStack next = new ItemStack(Material.valueOf(Variable.GUI_YML.getString("NextMaterial")));
          ItemMeta next_meta = next.getItemMeta();
          next_meta.setDisplayName(Variable.GUI_YML.getString("Next"));
          next.setItemMeta(next_meta);
          TrustGui.this.MainGui.setItem(53, next);
          ItemStack prev = new ItemStack(Material.valueOf(Variable.GUI_YML.getString("PrevMaterial")));
          ItemMeta prev_meta = next.getItemMeta();
          prev_meta.setDisplayName(Variable.GUI_YML.getString("Prev"));
          prev.setItemMeta(prev_meta);
          TrustGui.this.MainGui.setItem(45, prev);
          
          ConfigurationSection cs = Variable.GUI_YML.getConfigurationSection("");
          for (String temp : cs.getKeys(false)) {
            if (Variable.GUI_YML.getString(String.valueOf(temp) + ".InMenu") == null) {
              continue;
            }
            if (!Variable.GUI_YML.getString(String.valueOf(temp) + ".InMenu").equalsIgnoreCase("Trust")) {
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
                tempstr = PlaceholderAPI.setPlaceholders(null, tempstr);
              } catch (Exception exception) {}


              
              lores.add(tempstr);
            } 
            
            for (int c = 0; c < Variable.GUI_YML.getStringList(String.valueOf(temp) + ".Enchants").size(); c++) {
              String[] tempenc = ((String)Variable.GUI_YML.getStringList(String.valueOf(temp) + ".Enchants").get(c)).split("\\,");
              meta.addEnchant(Enchantment.getByName(tempenc[0]), Integer.valueOf(tempenc[1]).intValue(), true);
            } 
            meta.setLore(lores);
            item.setItemMeta(meta);
            TrustGui.this.MainGui.setItem(Variable.GUI_YML.getInt(String.valueOf(temp) + ".Index") - 1, item);
          } 


          
          for (int c = TrustGui.this.NowPage * 28; c < TrustGui.this.players.size() && c < (TrustGui.this.NowPage + 1) * 28 && c >= 0; c++) {
            Player temp = TrustGui.this.players.get(c);
            if (Variable.GUI_YML.getString("HeadMaterial").toUpperCase().contains("HEAD") || 
              Variable.GUI_YML.getString("HeadMaterial").toUpperCase().contains("SKULL")) {
              try {
                ItemStack itemStack = new ItemStack(Material.valueOf(Variable.GUI_YML.getString("HeadMaterial")), 
                    1, (short)SkullType.PLAYER.ordinal());
              } catch (Exception e) {
                String temp5 = Variable.Lang_YML.getString("PlayerHeadMaterialNotFound");
                if (temp5.contains("<Material>")) {
                  temp5 = temp5.replace("<Material>", Variable.GUI_YML.getString("HeadMaterial"));
                }
                Bukkit.getConsoleSender().sendMessage(temp5);
                return;
              } 
              ItemStack skull = new ItemStack(Material.valueOf(Variable.GUI_YML.getString("HeadMaterial")), 1, 
                  (short)SkullType.PLAYER.ordinal());
              SkullMeta player_SKULL = (SkullMeta)skull.getItemMeta();
              if (Variable.GUI_YML.getBoolean("EnableSkullSkin") && 
                temp != null) {
                
                try {
                  player_SKULL.setOwningPlayer((OfflinePlayer)temp);
                } catch (Exception exception) {}
              }


              
              player_SKULL.setDisplayName(String.valueOf(Variable.Lang_YML.getString("TrustGuiPrefix")) + temp.getName());
              
              List<String> lores = new ArrayList<>();
              for (String str : Variable.Lang_YML.getStringList("TrustGuiLores")) {
                lores.add(str);
              }
              player_SKULL.setLore(lores);

              
              skull.setItemMeta((ItemMeta)player_SKULL);
              
              TrustGui.this.MainGui.addItem(new ItemStack[] { skull });
            } else {
              ItemStack item = new ItemStack(Material.valueOf(Variable.GUI_YML.getString("HeadMaterial")));
              ItemMeta i_meta = item.getItemMeta();
              i_meta.setDisplayName(String.valueOf(Variable.Lang_YML.getString("TrustGuiPrefix")) + temp.getName());
              List<String> lores = new ArrayList<>();
              for (String str : Variable.Lang_YML.getStringList("TrustGuiLores")) {
                lores.add(str);
              }
              i_meta.setLore(lores);
              item.setItemMeta(i_meta);
              TrustGui.this.MainGui.addItem(new ItemStack[] { item });



            
            }




          
          }
        
        }
      }).runTaskAsynchronously((Plugin)Main.JavaPlugin); }









  
  public void OpenNextInventory(final Player p) { (new BukkitRunnable()
      {
        public void run() {
          if (TrustGui.this.NowPage + 2 > TrustGui.this.MaxPage) {
            return;
          }

          
          TrustGui.this.NowPage = TrustGui.this.NowPage + 1;
          TrustGui.this.MainGui.clear();
          if (Variable.GUI_YML.getBoolean("EnableTrustGuiNormalPane")) {
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
              TrustGui.this.MainGui.setItem(i, blb1);
            }
            TrustGui.this.MainGui.setItem(9, blb1);
            TrustGui.this.MainGui.setItem(18, blb1);
            TrustGui.this.MainGui.setItem(27, blb1);
            TrustGui.this.MainGui.setItem(17, blb1);
            TrustGui.this.MainGui.setItem(26, blb1);
            TrustGui.this.MainGui.setItem(35, blb1);
            TrustGui.this.MainGui.setItem(36, blb1);
            TrustGui.this.MainGui.setItem(44, blb1);
            for (int i = 45; i < 54; i++) {
              if (i != 49) {
                TrustGui.this.MainGui.setItem(i, blb1);
              }
            } 
          } 
          
          ItemStack next = new ItemStack(Material.valueOf(Variable.GUI_YML.getString("NextMaterial")));
          ItemMeta next_meta = next.getItemMeta();
          next_meta.setDisplayName(Variable.GUI_YML.getString("Next"));
          next.setItemMeta(next_meta);
          TrustGui.this.MainGui.setItem(53, next);
          ItemStack prev = new ItemStack(Material.valueOf(Variable.GUI_YML.getString("PrevMaterial")));
          ItemMeta prev_meta = next.getItemMeta();
          prev_meta.setDisplayName(Variable.GUI_YML.getString("Prev"));
          prev.setItemMeta(prev_meta);
          TrustGui.this.MainGui.setItem(45, prev);
          
          ConfigurationSection cs = Variable.GUI_YML.getConfigurationSection("");
          for (String temp : cs.getKeys(false)) {
            if (Variable.GUI_YML.getString(String.valueOf(temp) + ".InMenu") == null) {
              continue;
            }
            if (!Variable.GUI_YML.getString(String.valueOf(temp) + ".InMenu").equalsIgnoreCase("Trust")) {
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
            TrustGui.this.MainGui.setItem(Variable.GUI_YML.getInt(String.valueOf(temp) + ".Index") - 1, item);
          } 

          
          for (int c = TrustGui.this.NowPage * 28; c < TrustGui.this.players.size() && c < (TrustGui.this.NowPage + 1) * 28 && c >= 0; c++) {
            Player p = TrustGui.this.players.get(c);
            if (Variable.GUI_YML.getString("HeadMaterial").toUpperCase().contains("HEAD") || 
              Variable.GUI_YML.getString("HeadMaterial").toUpperCase().contains("SKULL")) {
              try {
                ItemStack itemStack = new ItemStack(Material.valueOf(Variable.GUI_YML.getString("HeadMaterial")), 
                    1, (short)SkullType.PLAYER.ordinal());
              } catch (Exception e) {
                String temp5 = Variable.Lang_YML.getString("PlayerHeadMaterialNotFound");
                if (temp5.contains("<Material>")) {
                  temp5 = temp5.replace("<Material>", Variable.GUI_YML.getString("HeadMaterial"));
                }
                Bukkit.getConsoleSender().sendMessage(temp5);
                return;
              } 
              ItemStack skull = new ItemStack(Material.valueOf(Variable.GUI_YML.getString("HeadMaterial")), 1, 
                  (short)SkullType.PLAYER.ordinal());
              SkullMeta player_SKULL = (SkullMeta)skull.getItemMeta();
              if (Variable.GUI_YML.getBoolean("EnableSkullSkin") && 
                p != null) {
                
                try {
                  player_SKULL.setOwningPlayer((OfflinePlayer)p);
                } catch (Exception exception) {}
              }


              
              player_SKULL.setDisplayName(String.valueOf(Variable.Lang_YML.getString("TrustGuiPrefix")) + p.getName());
              List<String> lores = new ArrayList<>();
              for (String str : Variable.Lang_YML.getStringList("TrustGuiLores")) {
                lores.add(str);
              }
              player_SKULL.setLore(lores);

              
              skull.setItemMeta((ItemMeta)player_SKULL);
              
              TrustGui.this.MainGui.addItem(new ItemStack[] { skull });
            } else {
              ItemStack item = new ItemStack(Material.valueOf(Variable.GUI_YML.getString("HeadMaterial")));
              ItemMeta i_meta = item.getItemMeta();
              i_meta.setDisplayName(String.valueOf(Variable.Lang_YML.getString("TrustGuiPrefix")) + p.getName());
              List<String> lores = new ArrayList<>();
              for (String str : Variable.Lang_YML.getStringList("TrustGuiLores")) {
                lores.add(str);
              }
              i_meta.setLore(lores);
              
              item.setItemMeta(i_meta);
              TrustGui.this.MainGui.addItem(new ItemStack[] { item });
            } 
          } 
          
      	  new BukkitRunnable()
          {
            public void run()
            {
            	 p.openInventory(TrustGui.this.MainGui);
            }
          }.runTask(Main.JavaPlugin);
          
        }
      }).runTaskAsynchronously((Plugin)Main.JavaPlugin); }



  
  public void OpenPrevInventory(final Player p) { (new BukkitRunnable()
      {
        public void run() {
          if (TrustGui.this.NowPage - 1 < 0) {
            return;
          }
          
          TrustGui.this.NowPage = TrustGui.this.NowPage - 1;

          
          TrustGui.this.MainGui.clear();
          
          if (Variable.GUI_YML.getBoolean("EnableTrustGuiNormalPane")) {
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
              TrustGui.this.MainGui.setItem(i, blb1);
            }
            TrustGui.this.MainGui.setItem(9, blb1);
            TrustGui.this.MainGui.setItem(18, blb1);
            TrustGui.this.MainGui.setItem(27, blb1);
            TrustGui.this.MainGui.setItem(17, blb1);
            TrustGui.this.MainGui.setItem(26, blb1);
            TrustGui.this.MainGui.setItem(35, blb1);
            TrustGui.this.MainGui.setItem(36, blb1);
            TrustGui.this.MainGui.setItem(44, blb1);
            for (int i = 45; i < 54; i++) {
              if (i != 49) {
                TrustGui.this.MainGui.setItem(i, blb1);
              }
            } 
          } 
          
          ItemStack next = new ItemStack(Material.valueOf(Variable.GUI_YML.getString("NextMaterial")));
          ItemMeta next_meta = next.getItemMeta();
          next_meta.setDisplayName(Variable.GUI_YML.getString("Next"));
          next.setItemMeta(next_meta);
          TrustGui.this.MainGui.setItem(53, next);
          ItemStack prev = new ItemStack(Material.valueOf(Variable.GUI_YML.getString("PrevMaterial")));
          ItemMeta prev_meta = next.getItemMeta();
          prev_meta.setDisplayName(Variable.GUI_YML.getString("Prev"));
          prev.setItemMeta(prev_meta);
          TrustGui.this.MainGui.setItem(45, prev);
          
          ConfigurationSection cs = Variable.GUI_YML.getConfigurationSection("");
          for (String temp : cs.getKeys(false)) {
            if (Variable.GUI_YML.getString(String.valueOf(temp) + ".InMenu") == null) {
              continue;
            }
            if (!Variable.GUI_YML.getString(String.valueOf(temp) + ".InMenu").equalsIgnoreCase("Trust")) {
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
            TrustGui.this.MainGui.setItem(Variable.GUI_YML.getInt(String.valueOf(temp) + ".Index") - 1, item);
          } 
          
          for (int c = TrustGui.this.NowPage * 28; c < TrustGui.this.players.size() && c < (TrustGui.this.NowPage + 1) * 28 && c >= 0; c++) {
            Player p = TrustGui.this.players.get(c);
            if (Variable.GUI_YML.getString("HeadMaterial").toUpperCase().contains("HEAD") || 
              Variable.GUI_YML.getString("HeadMaterial").toUpperCase().contains("SKULL")) {
              try {
                ItemStack itemStack = new ItemStack(Material.valueOf(Variable.GUI_YML.getString("HeadMaterial")), 
                    1, (short)SkullType.PLAYER.ordinal());
              } catch (Exception e) {
                String temp5 = Variable.Lang_YML.getString("PlayerHeadMaterialNotFound");
                if (temp5.contains("<Material>")) {
                  temp5 = temp5.replace("<Material>", Variable.GUI_YML.getString("HeadMaterial"));
                }
                Bukkit.getConsoleSender().sendMessage(temp5);
                return;
              } 
              ItemStack skull = new ItemStack(Material.valueOf(Variable.GUI_YML.getString("HeadMaterial")), 1, 
                  (short)SkullType.PLAYER.ordinal());
              SkullMeta player_SKULL = (SkullMeta)skull.getItemMeta();
              if (Variable.GUI_YML.getBoolean("EnableSkullSkin") && 
                p != null) {
                
                try {
                  player_SKULL.setOwningPlayer((OfflinePlayer)p);
                } catch (Exception exception) {}
              }


              
              player_SKULL.setDisplayName(String.valueOf(Variable.Lang_YML.getString("TrustGuiPrefix")) + p.getName());
              
              List<String> lores = new ArrayList<>();
              for (String str : Variable.Lang_YML.getStringList("TrustGuiLores")) {
                lores.add(str);
              }
              player_SKULL.setLore(lores);
              
              skull.setItemMeta((ItemMeta)player_SKULL);
              
              TrustGui.this.MainGui.addItem(new ItemStack[] { skull });
            } else {
              ItemStack item = new ItemStack(Material.valueOf(Variable.GUI_YML.getString("HeadMaterial")));
              ItemMeta i_meta = item.getItemMeta();
              i_meta.setDisplayName(String.valueOf(Variable.Lang_YML.getString("TrustGuiPrefix")) + p.getName());
              List<String> lores = new ArrayList<>();
              for (String str : Variable.Lang_YML.getStringList("TrustGuiLores")) {
                lores.add(str);
              }
              i_meta.setLore(lores);
              item.setItemMeta(i_meta);
              TrustGui.this.MainGui.addItem(new ItemStack[] { item });
            } 
          } 
         
          
      	  new BukkitRunnable()
          {
            public void run()
            {
            	 p.openInventory(TrustGui.this.MainGui);
            }
          }.runTask(Main.JavaPlugin);
          
        }
      }).runTaskAsynchronously((Plugin)Main.JavaPlugin); }




  
  public int getMaxPage() { return this.MaxPage; }


  
  public int getNowPage() { return this.NowPage; }




  
  public Inventory getInventory() { return this.MainGui; }
}


