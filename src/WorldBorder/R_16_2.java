package WorldBorder;

import net.minecraft.server.v1_16_R2.Packet;
import net.minecraft.server.v1_16_R2.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_16_R2.WorldBorder;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class R_16_2{
  public static void show(Player player, World world, double x, double z, double size) {
    WorldBorder worldBorder = new WorldBorder();
    worldBorder.world = ((CraftWorld)world).getHandle();
    worldBorder.setCenter(x, z);
    worldBorder.setSize(size);
    worldBorder.setDamageAmount(0.0D);
    worldBorder.setWarningDistance(0);
    PacketPlayOutWorldBorder packetPlayOutWorldBorder = new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE);
    (((CraftPlayer)player).getHandle()).playerConnection.sendPacket((Packet)packetPlayOutWorldBorder);
  }
  public static void hide(Player player, World world) {
    WorldBorder worldBorder = new WorldBorder();
    worldBorder.world = ((CraftWorld)world).getHandle();
    PacketPlayOutWorldBorder packetPlayOutWorldBorder = new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.LERP_SIZE);
    (((CraftPlayer)player).getHandle()).playerConnection.sendPacket((Packet)packetPlayOutWorldBorder);
  }
}
