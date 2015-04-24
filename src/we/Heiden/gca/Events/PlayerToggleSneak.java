package we.Heiden.gca.Events;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import we.Heiden.gca.Utils.Functions;
import we.Heiden.gca.Utils.ItemUtils;

/**
 * *********************************************
 * <p>
 * <b>This has been made by <i>Heiden Team</b>
 * <ul>
 * <li>Don't claim this class as your own
 * <li>Don't remove this disclaimer
 * </ul>
 * <b>All rights reserved
 * <p>
 * Heiden Team 2015
 * <p>
 * </b> *********************************************
 **/
public class PlayerToggleSneak implements Listener {

	public PlayerToggleSneak(Plugin pl) {
		Bukkit.getPluginManager().registerEvents(this, pl);
	}

	public static HashMap<Player, Integer> jetpack = new HashMap<Player, Integer>();

	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		if (p.getInventory().getChestplate() != null
				&& p.getInventory().getChestplate().equals(ItemUtils.JetPack())
				&& p.getLocation().getY() < 120) {
			p.setVelocity(p.getLocation().getDirection()
					.add(new Vector(0, 1, 0)));
			Location loc = p.getLocation().clone();
			loc.setY(loc.getY() - 1);
			Functions.displayEffect(loc, 29, 0, 10, p.getWorld().getPlayers());
			if (!jetpack.containsKey(p))
				jetpack.put(p, 1);
			int time = jetpack.get(p);
			short durability = (short) (time / 3);
			if (durability > p.getInventory().getChestplate().getType()
					.getMaxDurability()) {
				p.getInventory().setChestplate(null);
				jetpack.remove(p);
			} else {
				p.getInventory().getChestplate().setDurability(durability);
				time++;
				jetpack.put(p, time);
			}
			e.setCancelled(true);
		}
	}
}
