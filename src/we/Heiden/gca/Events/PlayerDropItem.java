package we.Heiden.gca.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.Plugin;

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
public class PlayerDropItem implements Listener {
	
	public PlayerDropItem(Plugin pl) {Bukkit.getPluginManager().registerEvents(this, pl);}
	
	@EventHandler
	public void onPlayerItemDrop(PlayerDropItemEvent e) {
		if (e.getItemDrop() != null) {if (Others.getItem(e.getPlayer()).contains(e.getItemDrop().getItemStack())) e.setCancelled(true);}
	}
}
