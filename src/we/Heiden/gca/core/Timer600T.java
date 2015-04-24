package we.Heiden.gca.core;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.scheduler.BukkitRunnable;

import we.Heiden.gca.Utils.UtilsMain;

public class Timer600T extends BukkitRunnable {

	public void run() {
		for (World w : Bukkit.getWorlds()) for (Entity e : w.getEntities()) if (e instanceof Villager) e.remove();
		UtilsMain.loadNpcs();
	}
}
