package we.Heiden.gca.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Functions.RobberyMode;

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
public class PlayerPickupItem implements Listener {

	public PlayerPickupItem(Plugin pl) {
		Bukkit.getPluginManager().registerEvents(this, pl);
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e) {
		if (!RobberyMode.Reward.isEmpty()) {
			if (!RobberyMode.Reward.containsKey(e.getPlayer())
					|| !RobberyMode.Reward.get(e.getPlayer()).equals(
							e.getItem())) {
				for (Player p : RobberyMode.Reward.keySet())
					if (e.getItem().equals(RobberyMode.Reward.get(p)))
						e.setCancelled(true);
			} else if (RobberyMode.Reward.containsKey(e.getPlayer())
					&& RobberyMode.Reward.get(e.getPlayer())
							.equals(e.getItem()))
				RobberyMode.Reward.remove(e.getPlayer());
		}
	}
}
