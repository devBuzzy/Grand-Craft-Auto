package we.Heiden.gca.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Functions.Settings;
import we.Heiden.gca.Misc.Others;

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
public class InventoryClick implements Listener {
	
	public InventoryClick(Plugin pl) {Bukkit.getPluginManager().registerEvents(this, pl);}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (e.getCurrentItem() != null) {
			ItemStack c = e.getCurrentItem();
			if (Others.ItemList(p).contains(c)) {
				e.setCancelled(true);
				p.updateInventory();
			} else {
				String invn = e.getInventory().getName();
				if (invn.equals(Settings.name())) {
					if (e.getInventory().contains(c)) {
						e.setCancelled(true);
						if (Settings.greffectList.contains(c)) Settings.greffects(p);
					}
				}
			}
		}
		if (e.getCursor() != null) {
			ItemStack c = e.getCursor();
			if (Others.ItemList(p).contains(c)) {
				e.setCancelled(true);
				p.updateInventory();
			}
		}
	}
}
