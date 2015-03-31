package we.Heiden.gca.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Configs.Config;
import we.Heiden.gca.Utils.Functions;

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
public class PlayerRespawn implements Listener {
	
	public PlayerRespawn(Plugin pl) {Bukkit.getPluginManager().registerEvents(this, pl);}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		if (Config.get().contains("Hospital.Warp") && Config.get().contains("Hospital.Area") && Config.get().contains("Hospital.Respawn")) {
			e.setRespawnLocation(Functions.loadLoc("Hospital.Respawn", e.getPlayer()));
			PlayerMove.onHospital.add(e.getPlayer());
		}
	}
}
