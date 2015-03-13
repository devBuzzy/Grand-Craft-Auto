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
	
	@SuppressWarnings("deprecation")
	public static boolean rideable(Location loc) {
		loc.setY(loc.getY()-1);
		Material mat = loc.getBlock().getType();
		if (mat.equals(Material.STAINED_CLAY) || mat.equals(Material.IRON_BLOCK)) {
			if (mat.equals(Material.STAINED_CLAY)) {if (loc.getBlock().getData() == 15) return true;
			} else {
				boolean bol = false;
				for (int n = 1; n <= 4; n++) {
					Location loc2 = loc.clone();
					if (n == 1) loc2.setZ(loc2.getZ()-1);
					else if (n == 2) loc2.setZ(loc2.getZ()+1);
					else if (n == 3) loc2.setX(loc2.getX()-1);
					else loc2.setX(loc2.getX()+1);
					if (loc2.getBlock().getType().equals(Material.STAINED_CLAY) && loc2.getBlock().getData() == 15) bol = true;
					if (bol) break;
				}
				if (bol) return true;
			}
		}
		return false;
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
							e.getVehicle().setVelocity(Cars.vec.get(p).clone().multiply(velocity));
						} else e.getVehicle().setVelocity(new Vector(0, 0, 0));
					}
				}
				else if (Cars.vehicles.containsKey(e.getVehicle())) e.getVehicle().teleport(e.getFrom());
			}
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onPlayerMove(PlayerMoveEvent e) {
		if (e.getFrom().getYaw() != e.getTo().getYaw()) setYaw(e.getPlayer(), e.getTo().getYaw());
	}
}
