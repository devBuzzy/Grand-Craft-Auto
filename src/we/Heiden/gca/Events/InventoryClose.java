package we.Heiden.gca.Events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Functions.Settings;
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
public class InventoryClose implements Listener {
	
	Plugin pl;
	public static List<Player> players = new ArrayList<Player>();
	
	public InventoryClose(Plugin pl) {
		this.pl = pl;
		Bukkit.getPluginManager().registerEvents(this, pl);}
	
	@EventHandler
	public void onPlayerCloseInventory(InventoryCloseEvent e) {
		if (e.getInventory() != null)
		if (!e.getInventory().getName().equals(Settings.name())) {
			players.add(((Player)e.getPlayer()));
			String invn = e.getInventory().getName();
			if (Functions.contains(Settings.menuNames, invn)) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
					public void run() {try {
						for (Player p : players) {
							Settings.display(p);
							players.remove(p);
						}
						} catch(Exception ex) {}
					}
				}, 1);
			}
		}
	}
}
