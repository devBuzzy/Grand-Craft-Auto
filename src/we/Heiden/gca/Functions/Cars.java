package we.Heiden.gca.Functions;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import we.Heiden.gca.Events.VehicleMove;
import we.Heiden.gca.Messages.ActionBar;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.gca.core.Timer20T;

public class Cars {

	public static HashMap<Entity, Player> vehicles = new HashMap<Entity, Player>();
	public static HashMap<Player, Entity> players = new HashMap<Player, Entity>();
	public static HashMap<Player, CarsEnum> enums = new HashMap<Player, CarsEnum>();
	public static HashMap<Player, Integer> velocity = new HashMap<Player, Integer>();
	public static HashMap<Player, Vector> vec = new HashMap<Player, Vector>();

	public static void spawnCar(Player p, CarsEnum car, Location loc) {
		while(!loc.getBlock().getType().equals(Material.AIR)) loc.setY(loc.getY()+1);
		loc.setZ(loc.getZ()+0.5D);
		loc.setX(loc.getX()+0.5D);
		Minecart vehicle = (Minecart) p.getWorld().spawnEntity(loc, EntityType.MINECART);
		vehicle.setMaxSpeed(50D);
		vehicles.put(vehicle, p);
		velocity.put(p, 1);
		players.put(p, vehicle);
		enums.put(p, car);
		VehicleMove.CarStoped.add(p);
	}
	
	public static void spawnCar(Player p, CarsEnum car) {
		@SuppressWarnings("deprecation")
		Location loc = p.getLineOfSight(new HashSet<Byte>(), 1).get(0).getLocation();
		loc.setY(loc.getY()-1);
		spawnCar(p, car, loc);
	}
	
	public static void updateGear(Player p) {Timer20T.actionBar.put(p, Messager.s("Gear: &b&l" + velocity.get(p)).get(0)); ActionBar.sendMessage(p, Timer20T.actionBar.get(p));}
	
	public static void gearUp(Player p) {
		Messager.load(p);
		int gear = velocity.get(p);
		int max = enums.get(p).getMax();
		gear++;
		if (gear > max) Messager.e1("Max Gear Reached");
		else {
			velocity.put(p, gear);
			updateGear(p);
			
			ItemStack gup = ItemUtils.GearUp();
			ItemStack gdown = ItemUtils.GearDown();
			
			if (gear != max) gup.setAmount(gear+1);
			else gup = ItemUtils.GearMax();
			gdown.setAmount(gear-1);
			
			p.getInventory().setItem(6, gup);
			p.getInventory().setItem(5, gdown);
			p.updateInventory();
		}
	}
	
	public static void gearDown(Player p) {
		int gear = velocity.get(p);
		int min = enums.get(p).getMin();
		gear--;
		
		velocity.put(p, gear);
		updateGear(p);
		
		ItemStack gup = ItemUtils.GearUp();
		ItemStack gdown = ItemUtils.GearDown();
		
		if (gear == min) gdown = enums.get(p).getKey();
		else gdown.setAmount(gear-1);
		gup.setAmount(gear+1);
		
		p.getInventory().setItem(6, gup);
		p.getInventory().setItem(5, gdown);
		p.updateInventory();
	}
	
	public static void setGear(Player p, int n) {
		if (!players.containsKey(p)) return;
		CarsEnum e = enums.get(p);
		if (n != 0) {
			if (n > e.getMax()) n = e.getMax();
			else if (n < e.getMin()) n = e.getMin();
		}
		velocity.put(p, n);
		updateGear(p);
		
		int max = enums.get(p).getMax();
		int min = enums.get(p).getMin();
		
		ItemStack gup = ItemUtils.GearUp();
		ItemStack gdown = ItemUtils.GearDown();
		
		if (n != 0) {
			if (n != max) gup.setAmount(n+1);
			else gup = ItemUtils.GearMax();
			if (n == min) gdown = enums.get(p).getKey();
			else gdown.setAmount(n-1);
		} else {
			gup = enums.get(p).getKey();
			gdown = ItemUtils.Garage();
		}
		
		p.getInventory().setItem(6, gup);
		p.getInventory().setItem(5, gdown);
		p.updateInventory();
	}
}
