package we.Heiden.gca.Events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import we.Heiden.gca.Functions.Cars;
import we.Heiden.gca.Functions.Garage;

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
public class VehicleMove implements Listener {
	
	public VehicleMove(Plugin pl) {Bukkit.getPluginManager().registerEvents(this, pl);}

	public static List<Player> forbidRide = new ArrayList<Player>();
	public static List<Player> CarStoped = new ArrayList<Player>();
	
	public static boolean canRide(Player p, Location to) {
		if (forbidRide.contains(p) || CarStoped.contains(p)) return false;
		else return rideable(to);
	}
	
	public static boolean canRide(Player p) {return canRide(p, p.getLocation());}
	
	public static boolean rideable(Location loc) {
		loc.setY(loc.getY()-1);
		Material mat = loc.getBlock().getType();
		return mat.equals(Material.STAINED_CLAY) || mat.equals(Material.IRON_BLOCK) || mat.equals(Material.STEP) || mat.equals(Material.AIR);
	}
	
	public static void setYaw(Player p, float yaw1) {
		double pitch = ((0 + 90) * Math.PI) / 180;
		double yaw  = ((yaw1 + 90)  * Math.PI) / 180;
		
		double x = Math.sin(pitch) * Math.cos(yaw);
		double y = Math.sin(pitch) * Math.sin(yaw);
		double z = Math.cos(pitch);
		
		Vector vector = new Vector(x, z, y);
		
		Cars.vec.put(p, vector);
	}
	
	public static List<Player> jump = new ArrayList<Player>();
	
	@EventHandler
	public void onVehicleMove(VehicleMoveEvent e) {
		if (e.getFrom().getBlock().getLocation() != e.getTo().getBlock().getLocation()) {
			if (e.getVehicle() instanceof Minecart) {
				if (e.getVehicle().getPassenger() instanceof Player) {
					Player p = (Player) e.getVehicle().getPassenger();
					if (Cars.vehicles.containsKey(e.getVehicle())) {
						if (canRide(p, e.getTo())) {
							double velocity = Cars.velocity.get(p).doubleValue();
							if (velocity == 1) velocity = 2;
							else velocity = velocity*velocity;
							velocity = velocity/10;
							Vector vec = Cars.vec.get(p).clone().multiply(velocity);
							if (jump.contains(p)) {
								vec.add(new Vector(0, 0.6D, 0));
								jump.remove(p);
							}
							e.getVehicle().setVelocity(vec);
						} else e.getVehicle().setVelocity(new Vector(0, 0, 0));
					}
				} else if (Cars.vehicles.containsKey(e.getVehicle())) e.getVehicle().teleport(e.getFrom());
				else for (Player p : Garage.exposed.keySet()) {
					if (Garage.exposed.get(p).contains(e.getVehicle())) e.getVehicle().teleport(e.getFrom());
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onPlayerMove(PlayerMoveEvent e) {
		if (e.getFrom().getYaw() != e.getTo().getYaw()) setYaw(e.getPlayer(), e.getTo().getYaw());
	}
}
