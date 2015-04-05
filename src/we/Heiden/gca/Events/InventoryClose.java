package we.Heiden.gca.Events;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Commands.TradeCommand;
import we.Heiden.gca.Functions.CellPhone;
import we.Heiden.gca.Functions.Settings;
import we.Heiden.gca.Functions.Trade;
import we.Heiden.gca.Stores.ClerkStore;
import we.Heiden.gca.Utils.Displayable;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.gca.core.Timer5T;

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
	public static HashMap<Player, Displayable> players = new HashMap<Player, Displayable>();
	
	public InventoryClose(Plugin pl) {
		this.pl = pl;
		Bukkit.getPluginManager().registerEvents(this, pl);}
	
	@EventHandler
	public void onPlayerCloseInventory(InventoryCloseEvent e) {
		if (e.getInventory() != null)
		if (!e.getInventory().getName().equals(Settings.name())) {
			Player p = (Player) e.getPlayer();
			String invn = e.getInventory().getName();
			if (Functions.contains(Settings.menuNames, invn)) openInv(p, new Settings());
			else if ((invn.equals(ClerkStore.foodN) || invn.equals(ClerkStore.weaponsN)) && !ClerkStore.temp01.containsKey(p)) openInv(p, new ClerkStore());
			else if (invn.equals(ChatColor.translateAlternateColorCodes('&', "&a&lPersonal Backpack"))) {
				p.getInventory().setItem(15, ItemUtils.ItemDefault());
				p.getInventory().setItem(16, ItemUtils.ItemDefault());
				Timer5T.toUpdate.add(p);
			} else if (invn.equals(Trade.displayN())) {
				if (!TradeCommand.finished.containsKey(p)) Trade.cancel(p);
			} else if (invn.startsWith(CellPhone.contactN)) openInv(p, new CellPhone());
		}
	}

	public void openInv(Player p, Displayable displayable) {
		players.put(p, displayable);
		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
			public void run() {
				try {
					for (Player p : players.keySet()) {
						players.get(p).display(p);
						players.remove(p);
					}
				} catch(Exception ex) {}
			}
		}, 1);
	}
}
