package we.Heiden.gca.core;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import we.Heiden.gca.Messages.ActionBar;

public class Timer20T extends BukkitRunnable {

	public Timer20T() {}
	
	public static HashMap<Player, String> actionBar = new HashMap<Player, String>();
	
	public void run() {
		for (Player p : actionBar.keySet()) ActionBar.sendMessage(p, actionBar.get(p));
	}
}
