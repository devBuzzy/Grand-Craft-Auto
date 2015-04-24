package we.Heiden.gca.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Functions.RobberyMode;
import we.Heiden.gca.Messages.Titles;

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
public class PlayerDeath implements Listener {

	public PlayerDeath(Plugin pl) {
		Bukkit.getPluginManager().registerEvents(this, pl);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (RobberyMode.robbery.containsKey(p)) {
			RobberyMode.busted(p);
			new Titles(p, "&4&lBusted").send();
		} else
			new Titles(p, "&c&lWasted").send();
	}
}
