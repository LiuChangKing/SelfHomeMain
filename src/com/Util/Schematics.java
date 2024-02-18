package com.Util;  
  
import java.io.File;  
import java.io.IOException;  
  
import org.bukkit.Location;  
import org.bukkit.World;   
import com.sk89q.worldedit.data.DataException;
import com.SelfHome.Variable;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;  
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.schematic.SchematicFormat;  
  
public class Schematics {  
    public static void loadSchematic(String name, World world, Location location) {  
        boolean noAir = false;  
        boolean entities = true;  
        Vector position = new Vector(0, 0, 0);  
        EditSession editSession = null;  
        try {  
            // ����һ���µ�EditSession���󣬲���Bukkit��World����ת��ΪWorldEdit��World����  
        	editSession = new EditSessionBuilder(world.getName()).autoQueue(false).build(); 
        	System.out.println("·��:" + Variable.worldFinal + name);
        	System.out.println("SESSION:" + editSession);
            // ���ز�ճ��schematic  
            SchematicFormat.getFormat(new File(Variable.worldFinal + name)).load(new File(Variable.worldFinal + name)).paste(editSession, position, noAir, entities);  
        } catch (MaxChangedBlocksException e) {  
            e.printStackTrace();  
        } catch (DataException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // ȷ���ڷ�������ʱ�������Ƿ����쳣����ˢ�¶��и���  
            if (editSession != null) {  
                editSession.flushQueue();  
            }  
        }  
    }  
}