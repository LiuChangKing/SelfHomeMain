/*      */ package com.SelfHome;
/*      */ 
/*      */ import com.Util.Home;
/*      */ import com.Util.HomeAPI;
/*      */ import com.Util.MySQL;
/*      */ import com.Util.R1_12_2;
/*      */ import com.Util.R1_7_10;
/*      */ import com.Util.StaticsTick;
/*      */ import com.Util.Util;
import com.Util.ZIP;

import WorldBorder.WBControl;

/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.time.LocalDateTime;
/*      */ import java.time.format.DateTimeFormatter;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import org.bukkit.Bukkit;
/*      */ import org.bukkit.Chunk;
/*      */ import org.bukkit.World;
/*      */ import org.bukkit.block.BlockState;
/*      */ import org.bukkit.command.CommandSender;
/*      */ import org.bukkit.configuration.file.YamlConfiguration;
/*      */ import org.bukkit.entity.Entity;
/*      */ import org.bukkit.entity.EntityType;
/*      */ import org.bukkit.entity.Item;
/*      */ import org.bukkit.entity.Player;
/*      */ import org.bukkit.event.Listener;
/*      */ import org.bukkit.inventory.ItemStack;
/*      */ import org.bukkit.plugin.Plugin;
/*      */ import org.bukkit.potion.PotionEffect;
/*      */ import org.bukkit.potion.PotionEffectType;
/*      */ import org.bukkit.scheduler.BukkitRunnable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class init
/*      */   implements Listener
/*      */ {
/*      */   public static void refreshWorldStatics(boolean broad) {
/*   54 */     Variable.list_home.clear();
/*   55 */     if (Variable.bungee) {
/*   56 */       for (String str : MySQL.getAllWorlds()) {
/*   57 */         Variable.list_home.add(str);
/*      */       }
/*      */     } else {
/*   60 */       File folder = new File(String.valueOf(Main.JavaPlugin.getDataFolder().getPath().toString()) + Variable.file_loc_prefix + "playerdata"); int b; int k; File[] arrayOfFile;
/*   61 */       for (k = (arrayOfFile = folder.listFiles()).length, b = 0; b < k; ) { File temp = arrayOfFile[b];
/*   62 */         String want_to = temp.getPath().replace(String.valueOf(Main.JavaPlugin.getDataFolder().getPath().toString()) + Variable.file_loc_prefix + "playerdata", "").replace(Variable.file_loc_prefix, "").replace(".yml", "");
/*   63 */         Variable.list_home.add(want_to);
/*      */         
/*      */         b++; }
/*      */     
/*      */     } 
/*   68 */     Variable.world_StaticsTick.clear();
/*      */     
/*   70 */     boolean check_has = false;
/*      */     
/*   72 */     for (World world : Bukkit.getWorlds()) {
/*   73 */       if (!Util.CheckIsHome(world.getName())) {
/*      */         continue;
/*      */       }
/*   76 */       check_has = true;
/*      */       
/*   78 */       int chunks = 0;
/*   79 */       int tiles = 0;
/*   80 */       int entity = 0;
/*   81 */       int dropitem = 0; int b; int k;
/*      */       Chunk[] arrayOfChunk;
/*   83 */       for (k = (arrayOfChunk = world.getLoadedChunks()).length, b = 0; b < k; ) {Chunk chunk = arrayOfChunk[b];
/*   84 */         chunks++; int b1; int m; BlockState[] arrayOfBlockState;
/*   85 */         for (m = (arrayOfBlockState = chunk.getTileEntities()).length, b1 = 0; b1 < m; ) { BlockState bs = arrayOfBlockState[b1];
/*   86 */           tiles++; b1++; }
/*      */         
/*      */         Entity[] arrayOfEntity;
/*   89 */         for (int e = (arrayOfEntity = chunk.getEntities()).length; b1 < e; ) { Entity et = arrayOfEntity[b1];
/*   90 */           if (et.getType() != EntityType.DROPPED_ITEM) {
/*   91 */             entity++;
/*      */           } else {
/*   93 */             Item i = (Item)et;
/*   94 */             dropitem += i.getItemStack().getAmount();
/*      */           } 
/*      */           
/*      */           b1++; }
/*      */         
/*      */         b++; }
/*      */       
/*  101 */       double calc_tps = tiles * Main.JavaPlugin.getConfig().getDouble("OneTileTick") + entity * Main.JavaPlugin.getConfig().getDouble("OneEntityTick") + dropitem * Main.JavaPlugin.getConfig().getDouble("OneDropTick") + chunks * Main.JavaPlugin.getConfig().getDouble("OneChunkTick");
/*  102 */       StaticsTick temp = new StaticsTick(world.getName().replaceAll(Variable.world_prefix, ""), tiles, chunks, entity, dropitem, calc_tps);
/*  103 */       Variable.world_StaticsTick.add(temp);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  108 */     if (!check_has) {
/*      */       return;
/*      */     }
/*      */     
/*  112 */     if (broad) {
/*  113 */       for (int c = 0; c < Main.JavaPlugin.getConfig().getStringList("StatisticsTop").size(); c++) {
/*  114 */         String a = Main.JavaPlugin.getConfig().getStringList("StatisticsTop").get(c);
/*  115 */         Bukkit.broadcastMessage(a);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  120 */       for (int i = 0; i < Variable.world_StaticsTick.size() - 1; i++) {
/*      */         
/*  122 */         for (int j = 0; j < Variable.world_StaticsTick.size() - 1 - i; j++) {
/*      */           
/*  124 */           if (((StaticsTick)Variable.world_StaticsTick.get(j)).tps < ((StaticsTick)Variable.world_StaticsTick.get(j + 1)).tps) {
/*      */             
/*  126 */             StaticsTick temp = Variable.world_StaticsTick.get(j);
/*  127 */             Variable.world_StaticsTick.set(j, Variable.world_StaticsTick.get(j + 1));
/*  128 */             Variable.world_StaticsTick.set(j + 1, temp);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  133 */       for (int i = 0; i < Variable.world_StaticsTick.size() && i < Main.JavaPlugin.getConfig().getInt("ShowAmount"); i++) {
/*  134 */         StaticsTick s = Variable.world_StaticsTick.get(i);


					if(s.tps == 0.00) {
						continue;
					}

/*  135 */         String temp = Main.JavaPlugin.getConfig().getString("ShowFormat");
/*  136 */         if (temp.contains("<index>")) {
/*  137 */           temp = temp.replace("<index>", String.valueOf(i + 1));
/*      */         }
/*  139 */         if (temp.contains("<world>")) {
/*  140 */           temp = temp.replace("<world>", s.name);
/*      */         }
/*  142 */         if (temp.contains("<tile>")) {
/*  143 */           temp = temp.replace("<tile>", String.valueOf(s.tile));
/*      */         }
/*  145 */         if (temp.contains("<chunk>")) {
/*  146 */           temp = temp.replace("<chunk>", String.valueOf(s.chunk));
/*      */         }
/*  148 */         if (temp.contains("<entity>")) {
/*  149 */           temp = temp.replace("<entity>", String.valueOf(s.entity));
/*      */         }
/*  151 */         if (temp.contains("<drop>")) {
/*  152 */           temp = temp.replace("<drop>", String.valueOf(s.drop));
/*      */         }
/*  154 */         if (temp.contains("<tps>")) {
/*  155 */           temp = temp.replace("<tps>", String.format(Main.JavaPlugin.getConfig().getString("FormatInfo"), new Object[] { Double.valueOf(s.tps) }));
/*      */         }
/*  157 */         Bukkit.broadcastMessage(temp);
/*      */       } 
/*      */ 
/*      */       
/*  161 */       for (int c = 0; c < Main.JavaPlugin.getConfig().getStringList("StatisticsEnd").size(); c++) {
/*  162 */         String a = Main.JavaPlugin.getConfig().getStringList("StatisticsEnd").get(c);
/*  163 */         Bukkit.broadcastMessage(a);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void init() {
	
	
	
	
	
	if(Main.JavaPlugin.getConfig().getBoolean("BorderSwitch")) {
		/*  181 */     (new BukkitRunnable()
				/*      */       {
				/*      */         public void run()
				/*      */         {
				/*  185 */           for (World world : Bukkit.getWorlds())
				/*      */           {
				/*  187 */             if (!Util.CheckIsHome(world.getName().replace(Variable.world_prefix, ""))) {
				/*      */               continue;
				/*      */             }
				/*  190 */             Home home = HomeAPI.getHome(world.getName().replace(Variable.world_prefix, ""));
				/*      */             
				/*  192 */             if (Main.JavaPlugin.getConfig().getBoolean("BorderSwitch")) {
				/*      */               try {
				/*  194 */                 world.getWorldBorder().setCenter(world.getSpawnLocation());
				/*  195 */                 world.getWorldBorder().setSize((
				/*  196 */                     Main.JavaPlugin.getConfig().getInt("WorldBoard") + (home.getLevel() - 1) * 
				/*  197 */                     Main.JavaPlugin.getConfig().getInt("UpdateRadius")));


				for(Player p :world.getPlayers()) {
					if(Variable.has_already_hide_border.contains(p.getName())) {
						WBControl.setEnable(p);
					}else{
						WBControl.setDisable(p);
					}
				}


				/*  198 */               } catch (NoSuchMethodError e) {
				/*  199 */                 Bukkit.getConsoleSender()
				/*  200 */                   .sendMessage(Variable.Lang_YML.getString("BorderException"));
				/*      */               }
				/*      */             
				/*      */             }
				/*      */           }
				/*      */         
				/*      */         }
				/*  207 */       }).runTaskTimer((Plugin)Main.JavaPlugin, 0L, 60L);
	}
	
	

/*      */ 
/*      */ 
/*      */     
/*  211 */     (new BukkitRunnable()
/*      */       {
/*      */         public void run() {
/*  214 */           Calendar cal = Calendar.getInstance();
/*  215 */           int hour = cal.getTime().getHours();
/*  216 */           int minute = cal.getTime().getMinutes();
/*  217 */           int seconds = cal.getTime().getSeconds();
/*  218 */           if (hour == 0 && minute == 0 && seconds == 0) {
/*  219 */             Variable.popularity_list.clear();
/*  220 */             Variable.flowers_list.clear();
/*      */           }
/*      */         
/*      */         }
/*  224 */       }).runTaskTimerAsynchronously((Plugin)Main.JavaPlugin, 0L, 20L);
/*      */     
/*  226 */     (new BukkitRunnable()
/*      */       {
/*      */         public void run() {
/*  229 */           Variable.list_home.clear();
/*  230 */           if (Variable.bungee) {
/*  231 */             for (String str : MySQL.getAllWorlds()) {
/*  232 */               Variable.list_home.add(str);
/*      */             }
/*      */           } else {
/*  235 */             File folder = new File(String.valueOf(Main.JavaPlugin.getDataFolder().getPath().toString()) + Variable.file_loc_prefix + "playerdata"); int b; int i; File[] arrayOfFile;
/*  236 */             for (i = (arrayOfFile = folder.listFiles()).length, b = 0; b < i; ) { File temp = arrayOfFile[b];
/*  237 */               String want_to = temp.getPath().replace(String.valueOf(Main.JavaPlugin.getDataFolder().getPath().toString()) + Variable.file_loc_prefix + "playerdata", "").replace(Variable.file_loc_prefix, "").replace(".yml", "");
/*  238 */               Variable.list_home.add(want_to);
/*      */               
/*      */               b++; }
/*      */           
/*      */           } 
/*      */         }
/*  244 */       }).runTaskTimer((Plugin)Main.JavaPlugin, 0L, 100L);
/*      */ 
/*      */ 
/*      */     
/*  248 */     if (!Main.JavaPlugin.getConfig().getBoolean("DisableFunctionButTeleport") && 
/*  249 */       Main.JavaPlugin.getConfig().getBoolean("EnableBlackItemsUseInNoPermission")) {
/*  250 */       (new BukkitRunnable()
/*      */         {
/*      */           public void run() {
/*  253 */             for (World world : Bukkit.getWorlds()) {
/*  254 */               if (!Util.CheckIsHome(world.getName().replace(Variable.world_prefix, ""))) {
/*      */                 continue;
/*      */               }
/*  257 */               for (Player p : world.getPlayers()) {
/*      */                 
/*  259 */                 if (Util.Check(p, world.getName().replace(Variable.world_prefix, "")).booleanValue())
/*      */                   continue; 
/*      */                 int b;
/*      */                 int k;
/*      */                 ItemStack[] arrayOfItemStack;
/*  264 */                 label33: for (k = (arrayOfItemStack = p.getInventory().getContents()).length, b = 0; b < k; ) { ItemStack i = arrayOfItemStack[b];
/*  265 */                   String nbt = Util.getItemNBTString(i);
/*  266 */                   for (int j = 0; j < Main.JavaPlugin.getConfig().getStringList("BlackItems").size(); j++) {
/*  267 */                     if (nbt.toUpperCase().contains(((String)Main.JavaPlugin.getConfig().getStringList("BlackItems").get(j)).toUpperCase())) {
/*  268 */                       String command = Main.JavaPlugin.getConfig().getString("BeKickedCommand");
/*  269 */                       if (command.contains("<Name>")) {
/*  270 */                         command = command.replace("<Name>", p.getName());
/*      */                       }
/*  272 */                       Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), command);
/*  273 */                       String message = Variable.Lang_YML.getString("TakeBlackItemsInNoPermissionHome");
/*  274 */                       if (message.contains("<type>")) {
/*  275 */                         message = message.replace("<type>", ((String)Main.JavaPlugin.getConfig().getStringList("BlackItems").get(j)).toUpperCase());
/*      */                       }
/*  277 */                       p.sendMessage(message);
/*      */                       break label33;
/*      */                     } 
/*      */                   } 
/*      */                   b++; }
/*      */               
/*      */               } 
/*      */             } 
/*      */           }
/*  286 */         }).runTaskTimerAsynchronously((Plugin)Main.JavaPlugin, 0L, 20L);
/*      */     }
/*      */ 
/*      */     
/*  290 */     if (!Main.JavaPlugin.getConfig().getBoolean("DisableFunctionButTeleport") && 
/*  291 */       Main.JavaPlugin.getConfig().getBoolean("CustomEntityMax")) {
/*  292 */       (new BukkitRunnable()
/*      */         {
/*      */           public void run()
/*      */           {
/*  296 */             for (World world : Bukkit.getWorlds()) {
/*  297 */               if (!Util.CheckIsHome(world.getName().replace(Variable.world_prefix, ""))) {
/*      */                 continue;
/*      */               }
/*  300 */               HashMap<String, Integer> entity_map = new HashMap<>();
/*  301 */               for (Entity entity : world.getEntities())
/*      */               {
/*      */ 
/*      */ 
/*      */                 
/*  306 */                 String type = null;
/*  307 */                 if (Bukkit.getBukkitVersion().toString().contains("1.12.2")) {
/*  308 */                   type = R1_12_2.getName(entity);
/*  309 */                 } else if (Bukkit.getBukkitVersion().toString().contains("1.7.10")) {
/*  310 */                   type = R1_7_10.getName(entity);
/*      */                 } else {
/*  312 */                   type = entity.getType().toString().toUpperCase();
/*      */                 } 
/*      */ 
/*      */                 
/*  316 */                 if (entity instanceof org.bukkit.entity.Animals) {
/*  317 */                   type = "Animals";
/*      */                 }
/*      */ 
/*      */ 
/*      */                 
/*  322 */                 if (!entity_map.containsKey(type)) {
/*  323 */                   entity_map.put(type, Integer.valueOf(1)); continue;
/*      */                 } 
/*  325 */                 int now_amount = ((Integer)entity_map.get(type)).intValue();
/*  326 */                 for (int c = 0; c < Main.JavaPlugin.getConfig().getStringList("EntityList").size(); c++) {
/*  327 */                   String[] args = ((String)Main.JavaPlugin.getConfig().getStringList("EntityList").get(c)).split("\\|");
/*  328 */                   if (args[0].toUpperCase().contains(type.toUpperCase()))
/*      */                   {
/*      */                     
/*  331 */                     int Max_Amount = Integer.valueOf(args[1]).intValue();
/*  332 */                     if (now_amount > Max_Amount) {
/*  333 */                       entity.remove();
/*      */                     } else {
/*  335 */                       entity_map.put(type, Integer.valueOf(now_amount + 1));
/*      */                     }
/*      */                   
/*      */                   }
/*      */                 
/*      */                 }
/*      */               
/*      */               }
/*      */             
/*      */             } 
/*      */           }
/*  346 */         }).runTaskTimer((Plugin)Main.JavaPlugin, 0L, Main.JavaPlugin.getConfig().getLong("CheckEntityInterval") * 20L);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  353 */     if (!Main.JavaPlugin.getConfig().getBoolean("DisableFunctionButTeleport") && 
/*  354 */       Main.JavaPlugin.getConfig().getBoolean("EnableTilesAndChunksAndDropItemsStatisticsTop")) {
/*  355 */       (new BukkitRunnable()
/*      */         {
/*      */           
/*      */           public void run()
/*      */           {
/*  360 */             init.refreshWorldStatics(true);
/*      */           }
/*  363 */         }).runTaskTimer((Plugin)Main.JavaPlugin, 0L, Main.JavaPlugin.getConfig().getLong("ShowTimes") * 20L);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  371 */     if (!Main.JavaPlugin.getConfig().getBoolean("DisableFunctionButTeleport")) {
/*  372 */       if (Main.JavaPlugin.getConfig().getLong("SaveTime") != 0L) {
/*  373 */         Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("EnableAutoSaveWorld"));
/*  374 */         (new BukkitRunnable()
/*      */           {
/*      */             public void run() {
/*  377 */               for (World temp : Bukkit.getWorlds()) {
								boolean is_jump =false;
									for(int i = 0; i<Main.JavaPlugin.getConfig().getStringList("UnAutoSaveWorlds").size();i++) {
										String str = Main.JavaPlugin.getConfig().getStringList("UnAutoSaveWorlds").get(i);
										if(str.equalsIgnoreCase(temp.getName().replace(Variable.world_prefix, ""))) {
											is_jump = true;
											break;
										}
									}
									if(is_jump == false) {
										  temp.save();
									}

/*      */               }
/*  380 */               Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("AutoSaveSuccess"));
/*      */             }
/*  383 */           }).runTaskTimer((Plugin)Main.JavaPlugin, 0L, Main.JavaPlugin.getConfig().getLong("SaveTime") * 20L);
/*      */       } else {
/*  385 */         Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("DisableAutoSaveWorld"));
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  392 */     if (!Main.JavaPlugin.getConfig().getBoolean("DisableFunctionButTeleport")) {
/*  393 */       (new BukkitRunnable()
/*      */         {
/*      */           public void run()
/*      */           {
/*  397 */             if (Main.JavaPlugin.getConfig().getInt("ArmorStand") == -1) {
/*      */               return;
/*      */             }
/*      */             
/*  401 */             for (World world : Bukkit.getWorlds())
/*      */             {
/*  403 */               if (!Util.CheckIsHome(world.getName().replaceAll(Variable.world_prefix, ""))) {
/*      */                 continue;
/*      */               }
/*      */               
/*  407 */               int amount = 0;
/*  408 */               for (Entity entity : world.getEntities())
/*      */               {
/*      */                 
/*  411 */                 amount++;
/*      */                 
/*  413 */                 if (entity.getType() == EntityType.ARMOR_STAND && amount > Main.JavaPlugin.getConfig().getInt("ArmorStand")) {
/*  414 */                   entity.remove();
/*  415 */                   amount--;
/*      */                 
/*      */                 }
/*      */ 
/*      */               
/*      */               }
/*      */ 
/*      */             
/*      */             }
/*      */           
/*      */           }
/*  426 */         }).runTaskTimer((Plugin)Main.JavaPlugin, 0L, 100L);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  434 */     if (!Main.JavaPlugin.getConfig().getBoolean("DisableFunctionButTeleport")) {
/*  435 */       if (Main.JavaPlugin.getConfig().getLong("AutoBackup") != 0L) {
/*  436 */         (new BukkitRunnable()
/*      */           {
/*      */             public void run() {
/*  439 */               if (Variable.check_first_start) {
/*  440 */                 Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("EnableAutoBackup"));
/*  441 */                 Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("EnableAutoBackupButFirstTime"));
/*  442 */                 Variable.check_first_start = false;
/*      */               } else {
/*  444 */                 LocalDateTime now = LocalDateTime.now();
/*  445 */                 String time = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm"));
/*      */ 
/*      */                 
/*  448 */                 if (Variable.bungee) {
/*  449 */                   File f = null;
/*      */                   
/*  451 */                   String OriginalBackup_location = String.valueOf(Variable.custom_autobackup_location) + Variable.file_loc_prefix + time;
/*      */                   
/*  453 */                   if (!Main.JavaPlugin.getConfig().getString("CustomBackupLocation").equalsIgnoreCase("")) {
/*  454 */                     OriginalBackup_location = String.valueOf(Main.JavaPlugin.getConfig().getString("CustomBackupLocation")) + time;
/*      */                   }
/*      */ 
/*      */                   
/*  458 */                   boolean check_has_copy = true;
/*  459 */                   String folderToCompress = "";
/*  460 */                   for (String worldname : MySQL.getAllWorlds()) {
/*      */                     
/*  462 */                     if (!MySQL.getServer(worldname).equalsIgnoreCase(Main.JavaPlugin.getConfig().getString("Server"))) {
/*      */                       continue;
/*      */                     }
/*      */                     
/*  466 */                     if (MySQL.getVisitTime(worldname).equalsIgnoreCase("")) {
/*  467 */                       MySQL.setVisitTime(worldname, String.valueOf(System.currentTimeMillis()));
/*      */                     }
/*      */                     
/*  470 */                     long before_time = Long.valueOf(MySQL.getVisitTime(worldname)).longValue();
/*  471 */                     long distance = (System.currentTimeMillis() - before_time) / 86400000L;
/*  472 */                     if (distance > Main.JavaPlugin.getConfig().getLong("NoBackup")) {
/*      */                       continue;
/*      */                     }
/*      */ 
/*      */                     
/*  477 */                     if (Variable.world_prefix.equalsIgnoreCase("")) {
/*  478 */                       if (Bukkit.getVersion().toString().toUpperCase().contains("ARCLIGHT")) {
/*  479 */                         f = new File(String.valueOf(Variable.single_server_gen) + Variable.world_prefix + worldname);
/*      */                       } else {
/*  481 */                         f = new File(String.valueOf(Variable.single_server_gen) + "world" + Variable.file_loc_prefix + worldname);
/*      */                       } 
/*      */                     } else {
/*  484 */                       f = new File(String.valueOf(Variable.single_server_gen) + Variable.world_prefix + worldname);
/*      */                     } 
/*      */                     
/*  487 */                     String oldDir = OriginalBackup_location + Variable.file_loc_prefix + worldname;
/*      */                     
/*      */                     try {
/*  490 */                       Util.copyDir(f.getPath(), oldDir);
/*  491 */                       folderToCompress = String.valueOf(Variable.custom_autobackup_location) + Variable.file_loc_prefix + time;
/*  492 */                     } catch (Exception e) {
/*  493 */                       check_has_copy = false;
/*      */                     } 
/*      */                   } 
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/*  500 */                   if (check_has_copy) {
/*  501 */                     String zipFileName = String.valueOf(OriginalBackup_location) + ".zip";

								try {
    								ZIP.zipFolder(OriginalBackup_location, zipFileName);
								} catch (IOException e) {
									e.printStackTrace();
								}

/*  513 */                     Util.deleteFile(new File(OriginalBackup_location));
/*  514 */                     Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("BungeeCordModuleAutoBackupSuccess"));
/*      */                   
/*      */                   }
/*      */                 
/*      */                 }
/*      */                 else {
/*      */                   
/*  521 */                   File folder = new File(Variable.Tempf);
/*  522 */                   String OriginalBackup_location = String.valueOf(Variable.custom_autobackup_location) + Variable.file_loc_prefix + time;
/*      */ 
/*      */                   
/*  525 */                   if (!Main.JavaPlugin.getConfig().getString("CustomBackupLocation").equalsIgnoreCase("")) {
/*  526 */                     OriginalBackup_location = String.valueOf(Main.JavaPlugin.getConfig().getString("CustomBackupLocation")) + time;
/*      */                   }
/*      */                   
/*  529 */                   String folderToCompress = null;
/*      */                   
/*  531 */                   boolean check_has_copy = true; int b; int i; File[] arrayOfFile;
/*  532 */                   for (i = (arrayOfFile = folder.listFiles()).length, b = 0; b < i; ) { File temp = arrayOfFile[b];
/*  533 */                     long lastModified = temp.lastModified();
/*  534 */                     long nowlong = System.currentTimeMillis();
/*  535 */                     long distance = (nowlong - lastModified) / 86400000L;
/*  536 */                     if (distance <= Main.JavaPlugin.getConfig().getLong("NoBackup")) {
/*      */ 
/*      */                       
/*  539 */                       String want_to = temp.getPath().replace(Variable.Tempf, "").replace(".yml", "").replace(Variable.file_loc_prefix, "");
/*  540 */                       if (Bukkit.getWorld(String.valueOf(Variable.world_prefix) + want_to) != null) {
/*  541 */                         Bukkit.getWorld(String.valueOf(Variable.world_prefix) + want_to).save();
/*      */                       }
/*      */ 
/*      */                       
/*  545 */                       String oldDir = OriginalBackup_location + Variable.file_loc_prefix + want_to;
/*      */ 
/*      */ 
/*      */ 
/*      */                       
/*  550 */                       File f = null;
/*  551 */                       if (Variable.world_prefix.equalsIgnoreCase("")) {
/*  552 */                         if (Bukkit.getVersion().toString().toUpperCase().contains("ARCLIGHT")) {
/*  553 */                           f = new File(String.valueOf(Variable.single_server_gen) + Variable.world_prefix + want_to);
/*      */                         } else {
/*  555 */                           f = new File(String.valueOf(Variable.single_server_gen) + "world" + Variable.file_loc_prefix + want_to);
/*      */                         } 
/*      */                       } else {
/*  558 */                         f = new File(String.valueOf(Variable.single_server_gen) + Variable.world_prefix + want_to);
/*      */                       } 
/*      */                       
/*      */                       try {
/*  562 */                         Util.copyDir(f.getPath(), oldDir);
/*  563 */                         folderToCompress = String.valueOf(Variable.custom_autobackup_location) + Variable.file_loc_prefix + time;
/*  564 */                       } catch (Exception e) {
/*  565 */                         check_has_copy = false;
/*      */                       } 
/*      */                     } 
/*      */                     
/*      */                     b++; }
/*      */                   
/*  571 */                   if (check_has_copy) {
/*  572 */                     String zipFileName = String.valueOf(OriginalBackup_location) + ".zip";

try {
	ZIP.zipFolder(OriginalBackup_location, zipFileName);
} catch (IOException e) {
	e.printStackTrace();
}
/*      */
/*      */ 
/*      */ 
/*      */                     
/*  582 */                     Util.deleteFile(new File(OriginalBackup_location));
/*  583 */                     Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("SingleServerModuleAutoBackupSuccess"));
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/*      */                   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*      */                 }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               }
/*      */             
/*      */             }
/*  603 */           }).runTaskTimer((Plugin)Main.JavaPlugin, 0L, Main.JavaPlugin.getConfig().getLong("AutoBackup") * 20L);
/*      */       } else {
/*  605 */         Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("DisableAutoBackup"));
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  634 */     if (!Main.JavaPlugin.getConfig().getBoolean("DisableFunctionButTeleport") && 
/*  635 */       Main.JavaPlugin.getConfig().getLong("OptimizeTime") != 0L) {
/*  636 */       (new BukkitRunnable()
/*      */         {
/*      */           public void run() {
/*  639 */             boolean has_been_solve = false;
/*      */             
/*  641 */             for (World world : Bukkit.getWorlds()) {
/*  642 */               if (!Util.CheckIsHome(world.getName().replace(Variable.world_prefix, ""))) {
/*      */                 continue;
/*      */               }
/*      */               
/*  646 */               boolean in_whitelist = false;
/*      */               
/*  648 */               for (int i = 0; i < Main.JavaPlugin.getConfig().getStringList("UnOptimizeWorlds").size(); i++) {
/*  649 */                 if (((String)Main.JavaPlugin.getConfig().getStringList("UnOptimizeWorlds").get(i)).equalsIgnoreCase(world.getName().replace(Variable.world_prefix, ""))) {
/*  650 */                   in_whitelist = true;
/*      */                   
/*      */                   break;
/*      */                 } 
/*      */               } 
/*  655 */               if (in_whitelist) {
/*      */                 continue;
/*      */               }
/*      */               
/*  659 */               if (Main.JavaPlugin.getConfig().getInt("OptimizeType") == 1) {
/*  660 */                 if (world.getPlayers().size() == 0) {
/*  661 */                   has_been_solve = true;
/*  662 */                   Bukkit.unloadWorld(world, true);
/*      */                 }  continue;
/*  664 */               }  if (Main.JavaPlugin.getConfig().getInt("OptimizeType") == 2) {
/*  665 */                 int b; int j; Chunk[] arrayOfChunk; for (j = (arrayOfChunk = world.getLoadedChunks()).length, b = 0; b < j; ) { Chunk temp_chunk = arrayOfChunk[b];
/*  666 */                   boolean check_player = false;
/*  667 */                   boolean check_cable = false; int b1; int k;
/*      */                   BlockState[] arrayOfBlockState;
/*  669 */                   for (k = (arrayOfBlockState = temp_chunk.getTileEntities()).length, b1 = 0; b1 < k; ) { BlockState bs = arrayOfBlockState[b1];
/*  670 */                     


try {
	if (Util.getNBTString(bs).toUpperCase().contains("IC2:CABLE")) {
		/*  671 */                       check_cable = true; break;
		/*      */                     }
}catch(NoClassDefFoundError e) {
	check_cable = false;
}



/*      */                     b1++; }
/*      */                   
/*      */                   Entity[] arrayOfEntity;
/*  676 */                   for (int ke = (arrayOfEntity = temp_chunk.getEntities()).length; b1 < ke; ) { Entity ee = arrayOfEntity[b1];
/*  677 */                     if (ee instanceof Player) {
/*  678 */                       check_player = true;
/*      */                       break;
/*      */                     } 
/*      */                     b1++; }
/*      */                   
/*  683 */                   if (!check_player && !check_cable) {
/*  684 */                     has_been_solve = true;
/*  685 */                     temp_chunk.unload(true);
/*      */                   } 
/*      */                   
/*      */                   b++; }
/*      */               
/*      */               } 
/*      */             } 
/*  692 */             if (has_been_solve) {
/*  693 */               if (Main.JavaPlugin.getConfig().getInt("OptimizeType") == 1) {
/*  694 */                 Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("OptimizeTypeOne"));
/*  695 */               } else if (Main.JavaPlugin.getConfig().getInt("OptimizeType") == 2) {
/*  696 */                 Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("OptimizeTypeTwo"));
/*      */               
/*      */               }
/*      */             
/*      */             }
/*      */           }
/*  702 */         }).runTaskTimer((Plugin)Main.JavaPlugin, 0L, Main.JavaPlugin.getConfig().getLong("OptimizeTime") * 20L);
/*      */     }
/*      */ 
/*      */     
/*  706 */     if (!Main.JavaPlugin.getConfig().getBoolean("DisableFunctionButTeleport")) {
/*  707 */       if (Main.JavaPlugin.getConfig().getLong("CheckTime") != 0L) {
/*  708 */         Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("EnableHomeTileCheck"));
/*  709 */         (new BukkitRunnable() {
/*  710 */             int i = 0;
/*      */ 
/*      */             
/*      */             public void run() {
/*  714 */               List<String> WarnList = new ArrayList<>();
/*  715 */               List<String> UnLoadList = new ArrayList<>();
/*  716 */               String WarnStr = "";
/*  717 */               String UnLoadStr = "";
/*      */ 
/*      */ 
/*      */               
/*  721 */               for (StaticsTick st : Variable.world_StaticsTick) {
/*  722 */                 World world = Bukkit.getWorld(String.valueOf(Variable.world_prefix) + st.name);
/*  723 */                 int tiles = st.tile;
/*  724 */                 if (tiles >= Main.JavaPlugin.getConfig().getInt("UnLoadTiles")) {
/*  725 */                   this.i++;
/*  726 */                   UnLoadList.add(world.getName().replace(Variable.world_prefix, ""));
/*  727 */                   if (world.getName() == null) {
/*      */                     continue;
/*      */                   }
/*      */                   
/*  731 */                   for (Player p : world.getPlayers()) {
/*  732 */                     p.teleport(Bukkit.getWorld("world").getSpawnLocation());
/*  733 */                     Bukkit.getConsoleSender()
/*  734 */                       .sendMessage(Variable.Lang_YML.getString("PlayerBeKickedByBanHome"));
/*      */                   } 
/*      */                   
/*  737 */                   Bukkit.unloadWorld(world.getName().replace(Variable.world_prefix, ""), true); continue;
/*  738 */                 }  if (tiles >= Main.JavaPlugin.getConfig().getInt("MaxTiles")) {
/*  739 */                   this.i++;
/*  740 */                   WarnList.add(world.getName().replace(Variable.world_prefix, ""));
/*      */                 } 
/*      */               } 
/*      */ 
/*      */ 
/*      */               
/*  746 */               if (WarnList.size() == 0 && UnLoadList.size() == 0) {
/*      */                 return;
/*      */               }
/*      */               
/*  750 */               if (Main.JavaPlugin.getConfig().getBoolean("CheckTipToAllPlayers")) {
/*  751 */                 Bukkit.broadcastMessage("§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
/*      */               } else {
/*  753 */                 Bukkit.getConsoleSender()
/*  754 */                   .sendMessage("§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
/*      */               } 
/*      */               
/*  757 */               if (Main.JavaPlugin.getConfig().getBoolean("CheckTipToAllPlayers")) {
/*  758 */                 if (WarnList.size() != 0) {
/*  759 */                   for (int i = 0; i < Variable.Lang_YML.getStringList("WarnLanguage").size(); i++) {
/*  760 */                     String message = Variable.Lang_YML.getStringList("WarnLanguage").get(i);
/*  761 */                     if (message.contains("<WarnList>")) {
/*  762 */                       message = message.replace("<WarnList>", WarnList.toString());
/*      */                     }
/*  764 */                     Bukkit.broadcastMessage(message);
/*      */                   } 
/*      */                 }
/*  767 */                 if (UnLoadList.size() != 0) {
/*  768 */                   for (int i = 0; i < Variable.Lang_YML.getStringList("UnLoadLanguage").size(); i++) {
/*  769 */                     String message = Variable.Lang_YML.getStringList("UnLoadLanguage").get(i);
/*  770 */                     if (message.contains("<UnLoadList>")) {
/*  771 */                       message = message.replace("<UnLoadList>", UnLoadList.toString());
/*      */                     }
/*  773 */                     Bukkit.broadcastMessage(message);
/*      */                   }
/*      */                 
/*      */                 }
/*      */               } else {
/*      */                 
/*  779 */                 if (WarnList.size() != 0) {
/*  780 */                   for (int i = 0; i < Variable.Lang_YML.getStringList("WarnLanguage").size(); i++) {
/*  781 */                     String message = Variable.Lang_YML.getStringList("WarnLanguage").get(i);
/*  782 */                     if (message.contains("<WarnList>")) {
/*  783 */                       message = message.replace("<WarnList>", WarnList.toString());
/*      */                     }
/*  785 */                     Bukkit.getConsoleSender().sendMessage(message);
/*      */                   } 
/*      */                 }
/*      */                 
/*  789 */                 if (UnLoadList.size() != 0) {
/*  790 */                   for (int i = 0; i < Variable.Lang_YML.getStringList("UnLoadLanguage").size(); i++) {
/*  791 */                     String message = Variable.Lang_YML.getStringList("UnLoadLanguage").get(i);
/*  792 */                     if (message.contains("<UnLoadList>")) {
/*  793 */                       message = message.replace("<UnLoadList>", UnLoadList.toString());
/*      */                     }
/*  795 */                     Bukkit.getConsoleSender().sendMessage(message);
/*      */                   } 
/*      */                 }
/*      */               } 
/*      */ 
/*      */               
/*  801 */               if (Main.JavaPlugin.getConfig().getBoolean("CheckTipToAllPlayers")) {
/*  802 */                 Bukkit.broadcastMessage("§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
/*      */               } else {
/*  804 */                 Bukkit.getConsoleSender()
/*  805 */                   .sendMessage("§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
/*      */               } 
/*      */             }
/*  808 */           }).runTaskTimer((Plugin)Main.JavaPlugin, 0L, Main.JavaPlugin.getConfig().getLong("CheckTime") * 20L);
/*      */       } 
/*      */ 
/*      */       
/*  812 */       if (!Main.JavaPlugin.getConfig().getBoolean("DisableFunctionButTeleport"))
/*      */       {
/*  814 */         (new BukkitRunnable()
/*      */           {
/*      */             public void run() {
/*  817 */               for (World temp : Bukkit.getWorlds()) {
/*      */                 
/*  819 */                 String check_world_is_home = temp.getName().replace(Variable.world_prefix, "");
/*  820 */                 if (!Util.CheckIsHome(check_world_is_home)) {
/*      */                   continue;
/*      */                 }
/*      */                 
/*  824 */                 Integer Amount = Integer.valueOf(0);
/*  825 */                 Boolean Check = Boolean.valueOf(false);
/*  826 */                 Integer Del = Integer.valueOf(0);
/*  827 */                 for (Entity entity : temp.getEntities()) {
/*  828 */                   if (entity instanceof org.bukkit.entity.LivingEntity) {
/*      */                     
/*  830 */                     boolean check_white = false;
/*  831 */                     for (int c = 0; c < Main.JavaPlugin.getConfig().getStringList("WhiteEntities")
/*  832 */                       .size(); c++) {
/*  833 */                       String white = Main.JavaPlugin.getConfig().getStringList("WhiteEntities").get(c);
/*  834 */                       if (white.equalsIgnoreCase(entity.getType().toString())) {
/*  835 */                         check_white = true;
/*      */                         
/*      */                         break;
/*      */                       } 
/*      */                     } 
/*  840 */                     if (!check_white) {
/*  841 */                       Amount = Integer.valueOf(Amount.intValue() + 1);
/*  842 */                       if (Amount.intValue() > Main.JavaPlugin.getConfig().getInt("DeleteEntities") && 
/*  843 */                         !(entity instanceof Player)) {
/*  844 */                         entity.remove();
/*  845 */                         Check = Boolean.valueOf(true);
/*  846 */                         Del = Integer.valueOf(Del.intValue() + 1);
/*      */                       } 
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*      */ 
/*      */                 
/*  853 */                 if (Check.booleanValue())
/*      */                 {
/*  855 */                   String temp5 = Main.JavaPlugin.getConfig().getString("ClearEntity");
/*  856 */                   if (temp5.contains("<Name>")) {
/*  857 */                     temp5 = temp5.replace("<Name>", temp.getName());
/*      */                   }
/*  859 */                   if (temp5.contains("<Amount>")) {
/*  860 */                     temp5 = temp5.replace("<Amount>", String.valueOf(Del));
/*      */                   }
/*      */                   
/*  863 */                   Bukkit.broadcastMessage(temp5);
/*      */                 }
/*      */               
/*      */               } 
/*      */             }
/*  868 */           }).runTaskTimer((Plugin)Main.JavaPlugin, 0L, Main.JavaPlugin.getConfig().getLong("CheckTime") * 20L);
/*      */       }
/*      */ 
/*      */       
/*  872 */       if (!Main.JavaPlugin.getConfig().getBoolean("DisableFunctionButTeleport")) {
/*  873 */         (new BukkitRunnable()
/*      */           {
/*      */             public void run() {
/*  876 */               for (World temp : Bukkit.getWorlds()) {
/*      */                 
/*  878 */                 String check_world_is_home = temp.getName().replace(Variable.world_prefix, "");
/*  879 */                 if (!Util.CheckIsHome(check_world_is_home)) {
/*      */                   continue;
/*      */                 }
/*      */                 
/*  883 */                 Integer Amount = Integer.valueOf(0);
/*  884 */                 Boolean Check = Boolean.valueOf(false);
/*  885 */                 Integer Del = Integer.valueOf(0);
/*  886 */                 for (Entity entity : temp.getEntities()) {
/*  887 */                   if (entity.getType() == EntityType.DROPPED_ITEM) {
/*  888 */                     Amount = Integer.valueOf(Amount.intValue() + 1);
/*  889 */                     if (Amount.intValue() > Main.JavaPlugin.getConfig().getInt("DeleteItems")) {
/*      */                       
/*  891 */                       Check = Boolean.valueOf(true);
/*  892 */                       Del = Integer.valueOf(Del.intValue() + 1);
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*  896 */                 if (Check.booleanValue())
/*      */                 {
/*  898 */                   String temp5 = Variable.Lang_YML.getString("ClearDropItems");
/*  899 */                   if (temp5.contains("<Name>")) {
/*  900 */                     temp5 = temp5.replace("<Name>", temp.getName());
/*      */                   }
/*  902 */                   if (temp5.contains("<Amount>")) {
/*  903 */                     temp5 = temp5.replace("<Amount>", String.valueOf(Del));
/*      */                   }
/*      */                   
/*  906 */                   Bukkit.broadcastMessage(temp5);
/*      */                 }
/*      */               
/*      */               } 
/*      */             }
/*  911 */           }).runTaskTimer((Plugin)Main.JavaPlugin, 0L, Main.JavaPlugin.getConfig().getLong("CheckTime") * 20L);
/*      */       } else {
/*      */         
/*  914 */         Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("DisableHomeTileCheck"));
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  920 */     if (!Main.JavaPlugin.getConfig().getBoolean("DisableFunctionButTeleport")) {
/*  921 */       (new BukkitRunnable()
/*      */         {
/*      */           public void run()
/*      */           {
/*  925 */             if (Variable.bungee) {
/*      */               
/*  927 */               List<String> list = MySQL.getAllWorlds();
/*  928 */               for (String worldname : list) {
								
/*  929 */                 if (Bukkit.getWorld(String.valueOf(Variable.world_prefix) + worldname) == null) {
/*      */                   continue;
/*      */                 }

/*  932 */                 World world = Bukkit.getWorld(String.valueOf(Variable.world_prefix) + worldname);
                
/*  934 */                 if (MySQL.getlocktime(worldname).equalsIgnoreCase("true")) {

/*  935 */                   world.setTime(Long.valueOf(MySQL.gettime(worldname)).longValue());
/*      */                 }
/*      */               }
/*      */             
/*      */             }
/*      */             else {
/*      */               
/*  942 */               File folder = new File(Variable.Tempf);
/*  943 */               if (folder.listFiles() == null)
/*      */                 return;  int b; int i;
/*      */               File[] arrayOfFile;
/*  946 */               for (i = (arrayOfFile = folder.listFiles()).length, b = 0; b < i; ) { File temp = arrayOfFile[b];
/*  947 */                 String want_to = temp.getPath().replace(Variable.Tempf, "").replace(".yml", "")
/*  948 */                   .replace(Variable.file_loc_prefix, "");
/*  949 */                 if (Bukkit.getWorld(String.valueOf(Variable.world_prefix) + want_to) != null) {
/*      */ 
/*      */                   
/*  952 */                   World world = Bukkit.getWorld(String.valueOf(Variable.world_prefix) + want_to);
/*  953 */                   YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(temp);
/*  954 */                   if (yamlConfiguration.getBoolean("locktime")) {
/*  955 */                     world.setTime(yamlConfiguration.getLong("time"));
/*      */                   }
/*      */                 } 
/*      */                 b++; }
/*      */             
/*      */             } 
/*      */           }
/*  962 */         }).runTaskTimer((Plugin)Main.JavaPlugin, 0L, 60L);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  967 */     if (!Main.JavaPlugin.getConfig().getBoolean("DisableFunctionButTeleport")) {
/*  968 */       (new BukkitRunnable()
/*      */         {
/*      */           public void run() {
/*  971 */             for (Player p : Bukkit.getOnlinePlayers()) {
/*      */               
/*  973 */               if (Variable.DispathCommand.contains(p.getName())) {
/*  974 */                 String temp5 = Variable.Lang_YML.getString("OverSomeBorderTip");
/*  975 */                 if (!temp5.equalsIgnoreCase("")) {
/*  976 */                   p.sendMessage("§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
/*  977 */                   p.sendMessage(temp5);
/*  978 */                   p.sendMessage("§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
/*  979 */                   Bukkit.dispatchCommand((CommandSender)p, Main.JavaPlugin.getConfig().getString("BorderCommand"));
/*  980 */                   Variable.DispathCommand.remove(p.getName());
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           }
/*  985 */         }).runTaskTimer((Plugin)Main.JavaPlugin, 0L, 20L);
/*      */     }
/*      */     
/*  988 */     if (!Main.JavaPlugin.getConfig().getBoolean("DisableFunctionButTeleport")) {
/*  989 */       (new BukkitRunnable()
/*      */         {
/*      */           public void run()
/*      */           {
/*  993 */             for (Player p : Bukkit.getOnlinePlayers()) {
/*  994 */               if (Variable.AddDebuff.contains(p.getName())) {
/*  995 */                 p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 10));
/*      */                 
/*  997 */                 String temp5 = Variable.Lang_YML.getString("OverBorderTip");
/*  998 */                 p.sendMessage("§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
/*  999 */                 p.sendMessage(temp5);
/* 1000 */                 p.sendMessage("§a§l§m--------------" + Variable.Prefix + "§a§l§m--------------");
/* 1001 */                 Variable.AddDebuff.remove(p.getName());
/*      */               }
/*      */             
/*      */             } 
/*      */           }
/* 1006 */         }).runTaskTimer((Plugin)Main.JavaPlugin, 0L, 20L);
/*      */     }
/*      */ 
/*      */     
/* 1010 */     if (!Main.JavaPlugin.getConfig().getBoolean("DisableFunctionButTeleport")) {
/* 1011 */       if (Main.JavaPlugin.getConfig().getBoolean("CustomTileMax")) {
/* 1012 */         Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("EnableCustomTileMaxFunction"));
/*      */       } else {
/* 1014 */         Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("DisableCustomTileMaxFunction"));
/*      */       } 
/*      */       
/* 1017 */       if (Main.JavaPlugin.getConfig().getBoolean("EnableBlackEntities")) {
/* 1018 */         Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("EnableBlackEntitiesFunction"));
/*      */       } else {
/* 1020 */         Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("DisableBlackEntitiesFunction"));
/*      */       } 
/*      */       
/* 1023 */       if (!Main.JavaPlugin.getConfig().getString("CustomBorder").equalsIgnoreCase("")) {
/* 1024 */         Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("EnableCustomBorder"));
/*      */       }
/*      */       
/* 1027 */       if (!Main.JavaPlugin.getConfig().getBoolean("KeepInventory")) {
/* 1028 */         Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("DisableWholeKeepInventory"));
/* 1029 */       } else if (Main.JavaPlugin.getConfig().getBoolean("KeepInventory")) {
/* 1030 */         Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("EnableWholeKeepInventory"));
/*      */       } 
/*      */       
/* 1033 */       if (!Main.JavaPlugin.getConfig().getBoolean("doMobSpawning")) {
/* 1034 */         Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("DisableMobSpawning"));
/* 1035 */       } else if (Main.JavaPlugin.getConfig().getBoolean("doMobSpawning")) {
/* 1036 */         Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("EnabledoMobSpawning"));
/*      */       } 
/*      */       
/* 1039 */       if (!Main.JavaPlugin.getConfig().getBoolean("mobGriefing")) {
/* 1040 */         Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("DisablemobGriefing"));
/* 1041 */       } else if (Main.JavaPlugin.getConfig().getBoolean("mobGriefing")) {
/* 1042 */         Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("EnablemobGriefing"));
/*      */       } 
/*      */       
/* 1045 */       if (!Main.JavaPlugin.getConfig().getBoolean("doFireTick")) {
/* 1046 */         Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("DisabledoFireTick"));
/* 1047 */       } else if (Main.JavaPlugin.getConfig().getBoolean("doFireTick")) {
/* 1048 */         Bukkit.getConsoleSender().sendMessage(Variable.Lang_YML.getString("EnabledoFireTick"));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\Administrator\Desktop\[家园]SelfHomeMain v2.0.2.5.jar!\com\SelfHome\init.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.2
 */