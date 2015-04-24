package we.Heiden.gca.Events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Functions.Cars;
import we.Heiden.gca.Messages.CMessager;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.gca.core.Timer20T;

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
public class VehicleExit implements Listener {

	public VehicleExit(Plugin pl) {
		Bukkit.getPluginManager().registerEvents(this, pl);
	}

	public static List<Player> confirm = new ArrayList<Player>();
	public static HashMap<Player, Integer> delay = new HashMap<Player, Integer>();

	@EventHandler
	public void onVehicleExit(VehicleExitEvent e) {
		if (e.getVehicle() instanceof Minecart
				&& e.getVehicle().getPassenger() instanceof Player) {
			if (Cars.vehicles.containsKey(e.getVehicle())
					&& Cars.vehicles.get(e.getVehicle()).equals(
							e.getVehicle().getPassenger())) {
				Player p = (Player) e.getVehicle().getPassenger();
				if (!delay.containsKey(p)) {
					Messager.load(p);
					if (!VehicleMove.CarStoped.contains(p)) {
						Messager.e1("You must stop your car first");
						e.setCancelled(true);
					} else if (!confirm.contains(p)) {
						confirm.add(p);
						Messager.e1("Press shift again to exit.");
						e.setCancelled(true);
					} else {
						Messager.s1("Successfully exited");
						CMessager.load(p);
						CMessager
								.e1("Your car will go in your garage in 5 minutes");
						Cars.vec.remove(p);
						p.getInventory().setItem(6, Cars.enums.get(p).getKey());
						p.getInventory().setItem(5, ItemUtils.Garage());
						List<Object> lo = Functions.newList();
						lo.addAll(Arrays.asList(e.getVehicle(), 300));
						Timer20T.toRemove.put(p, lo);
					}
					delay.put(p, 5);
				} else
					e.setCancelled(true);
			}
		}
	}
}
