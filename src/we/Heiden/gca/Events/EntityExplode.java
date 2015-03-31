package we.Heiden.gca.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Configs.Config;
import we.Heiden.gca.Utils.Functions;

/**
 * ********************************************* <p>
 * <b>This has been made by <i>Heiden Team</b>
 * <ul>
 * <li>Don't claim this class as your own
 * <li>Don't remove this disclaimer
 * </ul>
 *         <b>All rights reserved <p>
 *           Heiden Team 2015 <p></b>
 * ********************************************* 
 **/
public class EntityExplode implements Listener {
	
	public EntityExplode(Plugin pl) {Bukkit.getPluginManager().registerEvents(this, pl);}
	
	public static List<TNTPrimed> tnts = new ArrayList<TNTPrimed>();
	public static HashMap<Entity, Integer> restore = new HashMap<Entity, Integer>();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e) {
		if (tnts.contains(e.getEntity())) {
			tnts.remove(e.getEntity());
			int n = 0;
			for (Block b : e.blockList()) {
				n++;
				Config.get().set("Temp." + e.getEntity().getUniqueId() + "." + n + ".Type", b.getType());
				Config.get().set("Temp." + e.getEntity().getUniqueId() + "." + n + ".Data", b.getData());
				Functions.saveLoc("Temp." + e.getEntity().getUniqueId() + "." + n + ".Location", b.getLocation(), Config.get());
			}
			Config.save();
			restore.put(e.getEntity(), 10);
			int rand = new Random().nextInt(15);
			for (Block b : e.blockList()) {
				b.setType(Material.STAINED_CLAY);
				b.setData((byte) rand);
			}
		}
		e.blockList().clear();
	}
}
