package we.Heiden.gca.core;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import we.Heiden.gca.Events.VehicleExit;

public class Timer2T extends BukkitRunnable {

	public Timer2T() {}
	
	public void run() {
		try {
			for (Player p : VehicleExit.delay.keySet()) {
				int time = VehicleExit.delay.get(p);
				time--;
				if (time+1 > 0) VehicleExit.delay.put(p, time);
				else VehicleExit.delay.remove(p);
			}
		} catch(Exception ex) {}
	}
}
