package we.Heiden.gca.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Cars.CarsMain;
import we.Heiden.gca.Misc.CMessager;
import we.Heiden.gca.Misc.Messager;
import we.Heiden.gca.Misc.Others;

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
public class VehicleExit implements Listener {
	
	public VehicleExit(Plugin pl) {Bukkit.getPluginManager().registerEvents(this, pl);}

	public static List<Player> confirm = new ArrayList<Player>();
	public static HashMap<Player, Integer> delay = new HashMap<Player, Integer>();
	
	@EventHandler
	public void onVehicleExit(VehicleExitEvent e) {
		if (e.getVehicle() instanceof Minecart && e.getVehicle().getPassenger() instanceof Player) {
			if (CarsMain.vehicles.containsKey(e.getVehicle()) && CarsMain.vehicles.get(e.getVehicle()).equals(e.getVehicle().getPassenger())) {
				Player p = (Player) e.getVehicle().getPassenger();
				if (!delay.containsKey(p)) {
					Messager.load(p);
					if (!VehicleMove.CarStoped.contains(p)) {
						Messager.e1("You must stop your car first");
						e.setCancelled(true);
					} else if (!confirm.contains(p)) {
						confirm.add(p);
						Messager.e1("Exit again to exit, Turn on your car to cancel");
						e.setCancelled(true);
					} else {
						Messager.s1("Successfully Exit");
						CMessager.load(p);
						CMessager.e1("Your car will go to the garage on 5 minutes");
						CarsMain.vec.remove(p);
						p.getInventory().setItem(6, CarsMain.enums.get(p).getKey());
						p.getInventory().setItem(5, Others.Garage());
					}
					delay.put(p, 5);
				} else e.setCancelled(true);
			}
		}
	}
}
