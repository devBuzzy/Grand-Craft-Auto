package we.Heiden.gca.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.Plugin;

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
	
	@EventHandler
	public void onVehicleExit(VehicleExitEvent e) {
		/*if (e.getVehicle() instanceof Minecart && e.getVehicle().getPassenger() instanceof Player) {
			if (CarsMain.vehicles.containsKey(e.getVehicle()) && CarsMain.vehicles.get(e.getVehicle()).equals(e.getVehicle().getPassenger())) {
				Minecart vehicle = (Minecart) e.getVehicle();
				Player p = (Player) e.getVehicle().getPassenger();
				
			}
		}*/
	}
}
