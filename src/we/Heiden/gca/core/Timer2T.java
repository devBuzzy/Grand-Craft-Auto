package we.Heiden.gca.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import we.Heiden.gca.Events.PlayerToggleSneak;
import we.Heiden.gca.Events.VehicleExit;
import we.Heiden.gca.Weapons.WeaponHandler;
import we.Heiden.gca.Weapons.WeaponUtils;
import we.Heiden.gca.Weapons.Weapons;

public class Timer2T extends BukkitRunnable {

	public static HashMap<Player, List<Object>> shoots = new HashMap<Player, List<Object>>();
	
	public void run() {
		if (!VehicleExit.delay.isEmpty()) try {
			for (Player p : VehicleExit.delay.keySet()) {
				int time = VehicleExit.delay.get(p);
				time--;
				if (time+1 > 0) VehicleExit.delay.put(p, time);
				else VehicleExit.delay.remove(p);
			}
		} catch(Exception ex) { }
		
		if (!shoots.isEmpty()) try {
			for (Player p : shoots.keySet()) {
				Weapons wep = (Weapons) shoots.get(p).get(0);
				List<Object> lo = new ArrayList<Object>();
				lo.addAll(Arrays.asList(wep, p.getLocation()));
				WeaponHandler.bullet.put(WeaponUtils.shootSingle(wep.Accuarcy, p, Arrow.class), lo);
				int shoot = (int) shoots.get(p).get(1);
				shoot--;
				if (shoot > 0) shoots.get(p).set(1, shoot);
				else shoots.remove(p);
			}
		} catch(Exception ex) { }
		
		if (!PlayerToggleSneak.jetpack.isEmpty()) 
			for (Player p : PlayerToggleSneak.jetpack.keySet()) {
				int time = PlayerToggleSneak.jetpack.get(p);
				time--;
				if (time > 0) PlayerToggleSneak.jetpack.put(p, time);
				else PlayerToggleSneak.jetpack.remove(p);
				p.getInventory().getChestplate().setDurability((short) (time/3));
			}
	}
}
