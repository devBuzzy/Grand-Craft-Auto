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
	
	public static boolean contains(ItemStack[] list, ItemStack i) {
		for (ItemStack is : list) if (is.equals(i)) return true;
		return false;
	}
	
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
						if (!c.equals(Others.ItemDefault())) {
							if (contains(Settings.gsparticles, c)) Settings.menu01(p);
							else if (Settings.greffectList.contains(c)) Settings.menu02(p);
							else if (contains(Settings.titles, c)) Settings.menu03(p);
							else if (contains(Settings.hotbars, c)) Settings.menu04(p);
							else if (Settings.gzoomList.contains(c)) Settings.menu05(p);	
							else if (contains(Settings.pvisibilities, c)) Settings.menu06(p);
							else if (contains(Settings.eparticles, c)) Settings.menu07(p);
							else if (Settings.qualityList.contains(c)) Settings.menu08(p);
						}
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
