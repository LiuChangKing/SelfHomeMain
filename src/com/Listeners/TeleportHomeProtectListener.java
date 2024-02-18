package com.Listeners;

import com.SelfHome.Main;
import com.SelfHome.Variable;
import com.Util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportHomeProtectListener
        implements Listener {

  // 当玩家传送事件发生时触发
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
  public void onTeleport(final PlayerTeleportEvent event) {
    // 获取传送目标所在的世界
    World world = event.getTo().getWorld();
    // 如果传送目标所在的世界不是家的世界，则返回
    if (!Util.CheckIsHome(world.getName().replace(Variable.world_prefix, ""))) {
      return;
    }

    // 如果未启用生成保护，则返回
    if (!Main.JavaPlugin.getConfig().getBoolean("EnableSpawnProtection")) {
      return;
    }

    // 创建一个异步任务，用于检查传送目标是否安全
    (new BukkitRunnable() {
      public void run() {
        // 获取传送目标的位置
        Location loc = event.getTo();
        // 找到传送目标位置的最顶端非空气方块位置
        Location highestNonAirBlockLoc = findHighestNonAirBlockLocation(loc);
        boolean check = false;
        double firstY = highestNonAirBlockLoc.getY();
        // 从最顶端非空气方块位置向下遍历，检查是否有方块
        for (double i = firstY; i > 0.0D; i--) {
          loc.setY(i);
          // 如果有方块，则将检查标记设为 true，并跳出循环
          if (loc.getWorld().getBlockAt(loc).getType() != Material.AIR) {
            check = true;
            break;
          }
        }
        // 如果检查标记为 false，并且传送目标位置是空气，则执行生成保护操作
        if (!check && event.getTo().getWorld().getBlockAt(event.getTo()).getType() == Material.AIR) {
          event.getPlayer().sendMessage(Variable.Lang_YML.getString("SpawnProtection"));
          event.getTo().getWorld().getBlockAt(event.getTo()).setType(Material.GLASS);
        }

      }
    }).runTask((Plugin)Main.JavaPlugin);


  }

  // 找到传送目标位置的最顶端非空气方块位置
  private Location findHighestNonAirBlockLocation(Location location) {
    World world = location.getWorld();
    int x = location.getBlockX();
    int z = location.getBlockZ();

    assert world != null;
    // 从世界最高高度向下遍历
    for (int y = world.getMaxHeight(); y >= 0; y--) {
      Block block = world.getBlockAt(x, y, z);
      // 如果方块不是空气，则返回该方块位置
      if (block.getType() != Material.AIR) {
        return block.getLocation();
      }
    }

    return location;  // 如果所有方块都是空气，返回原始位置
  }

}
