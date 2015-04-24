package we.Heiden.gca.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import we.Heiden.gca.Functions.Cars;
import we.Heiden.gca.Messages.Messager;
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

public class VehicleBlockCollision implements Listener {

	public VehicleBlockCollision(Plugin pl) {
		Bukkit.getPluginManager().registerEvents(this, pl);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void VehicleCollison(VehicleBlockCollisionEvent e) {
		if (e.getVehicle() instanceof Minecart) {
			if (e.getVehicle().getPassenger() instanceof Player) {
				if (!e.getBlock().getType().equals(Material.AIR)) {
					Player p = (Player) e.getVehicle().getPassenger();
					if (Cars.vehicles.containsKey(e.getVehicle())
							&& Cars.vehicles.get(e.getVehicle()).equals(p)) {
						Location loc = e.getBlock().getLocation();
						loc.setY(loc.getY() + 1);
						if (loc.getBlock().getType()
								.equals(Material.STAINED_CLAY)
								|| e.getBlock().getType()
										.equals(Material.STAINED_CLAY)
								|| (loc.getBlock().getType()
										.equals(Material.STEP) && loc
										.getBlock().getData() == 3)) {
							VehicleMove.jump.add(p);
							double velocity = Cars.velocity.get(p)
									.doubleValue();
							if (velocity == 1)
								velocity = 2;
							else
								velocity = velocity * velocity;
							velocity = velocity / 10;
							e.getVehicle().setVelocity(
									Cars.vec.get(p).clone().multiply(velocity));
						} else {
							Cars.setGear(p, 0);
							Messager.e1("&c&lYou've crashed! &eTurn your car back on");
							e.getVehicle().setVelocity(new Vector(0, 0, 0));
							VehicleMove.CarStoped.add(p);
							Timer20T.actionBar.remove(p);
						}
					}
				}
			}
		}
	}
}
