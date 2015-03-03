package we.Heiden.gca.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Functions.Bag;
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
public class PlayerInteract implements Listener {
	
	public PlayerInteract(Plugin pl) {Bukkit.getPluginManager().registerEvents(this, pl);}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getItem() != null) {
			ItemStack bag = Others.ItemBag();
			ItemStack settings = Others.ItemSettings();
			if (e.getItem().equals(bag)) Bag.open(p);
			else if (e.getItem().equals(settings)) Settings.display(p);
		}
	}
}
