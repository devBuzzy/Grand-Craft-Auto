package we.Heiden.gca.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Utils.ItemUtils;

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
public class PlayerDamage implements Listener {
	
	public PlayerDamage(Plugin pl) {Bukkit.getPluginManager().registerEvents(this, pl);}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (p.getInventory().getChestplate() != null && p.getInventory().getChestplate().equals(ItemUtils.JetPack()) && e.getCause() != null && 
					e.getCause().equals(DamageCause.FALL)) e.setCancelled(true);
		}
	}
}
