package we.Heiden.gca.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Functions.RobberyMode;
import we.Heiden.gca.Utils.UtilsMain;

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
public class PlayerQuit implements Listener {

	public PlayerQuit(Plugin pl) {
		Bukkit.getPluginManager().registerEvents(this, pl);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		UtilsMain.save(p);
		if (RobberyMode.robbery.containsKey(p))
			RobberyMode.logOut(p);
	}
}
