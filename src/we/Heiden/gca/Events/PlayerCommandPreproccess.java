package we.Heiden.gca.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.defaults.ReloadCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

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
public class PlayerCommandPreproccess implements Listener {

	public PlayerCommandPreproccess(Plugin pl) {
		Bukkit.getPluginManager().registerEvents(this, pl);
	}

	@EventHandler
	public void onPlayerCommandPreproccess(PlayerCommandPreprocessEvent e) {
		if (e.getPlayer().hasPermission(
				new ReloadCommand("Bukkit").getPermission())) {
			String[] args = e.getMessage().split(" ");
			if (args[0].equalsIgnoreCase("/rl")
					|| args[0].equalsIgnoreCase("/reload")) {
				e.setCancelled(true);
				boolean bol = true;
				if (args.length > 1 && args[1].equalsIgnoreCase("false"))
					bol = false;
				Bukkit.reload();
				if (bol)
					Bukkit.broadcastMessage(ChatColor.GREEN
							+ "Reload complete.");
			}
		}
	}
}
