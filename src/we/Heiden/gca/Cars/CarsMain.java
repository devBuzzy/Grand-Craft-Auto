package we.Heiden.gca.Cars;

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

import we.Heiden.gca.Misc.ActionBar;
import we.Heiden.gca.Misc.Messager;
import we.Heiden.gca.Misc.Others;
import we.Heiden.gca.core.Timer20T;

public class CarsMain {

	public static HashMap <Entity, Player> vehicles = new HashMap<Entity, Player>();
	public static HashMap <Player, Entity> players = new HashMap<Player, Entity>();
	public static HashMap <Player, CarsEnum> enums = new HashMap<Player, CarsEnum>();
	public static HashMap<Player, Integer> velocity = new HashMap<Player, Integer>();
	public static HashMap<Player, Vector> vec = new HashMap<Player, Vector>();

	public static void spawnCar(Player p, CarsEnum car, Location loc) {
		/*int min = car.getMin();
		int max = car.getMax();*/
		while(!loc.getBlock().getType().equals(Material.AIR)) loc.setY(loc.getY()+1);
		loc.setZ(loc.getZ()+0.5D);
		loc.setX(loc.getX()+0.5D);
		Minecart vehicle = (Minecart) p.getWorld().spawnEntity(loc, EntityType.MINECART);
		vehicle.setMaxSpeed(50D);
		vehicles.put(vehicle, p);
		velocity.put(p, 1);
		players.put(p, vehicle);
		enums.put(p, car);
	}
	
	public static void spawnCar(Player p, CarsEnum car) {
		@SuppressWarnings("deprecation")
		Location loc = p.getLineOfSight(new HashSet<Byte>(), 1).get(0).getLocation();
		loc.setY(loc.getY()-1);
		spawnCar(p, car, loc);
	}
	
	public static void updateGear(Player p) {Timer20T.actionBar.put(p, Messager.s("Actual Gear: &b&l" + velocity.get(p)).get(0)); ActionBar.sendMessage(p, Timer20T.actionBar.get(p));}
	
	public static void gearUp(Player p) {
		Messager.load(p);
		int gear = velocity.get(p);
		int max = enums.get(p).getMax();
		gear++;
		if (gear > max) Messager.e1("Max Gear Reached");
		else {
			velocity.put(p, gear);
			updateGear(p);
			
			ItemStack gup = Others.GearUp();
			ItemStack gdown = Others.GearDown();
			
			if (gear != max) gup.setAmount(gear+1);
			else gup = Others.GearMax();
			gdown.setAmount(gear-1);
			
			p.getInventory().setItem(6, gup);
			p.getInventory().setItem(5, gdown);
			p.updateInventory();
		}
	}
	
	public static void gearDown(Player p) {
		Messager.load(p);
		int gear = velocity.get(p);
		int min = enums.get(p).getMin();
		gear--;
		
		velocity.put(p, gear);
		updateGear(p);
		
		ItemStack gup = Others.GearUp();
		ItemStack gdown = Others.GearDown();
		
		if (gear == min) gdown = enums.get(p).getKey();
		else gdown.setAmount(gear-1);
		gup.setAmount(gear+1);
		
		p.getInventory().setItem(6, gup);
		p.getInventory().setItem(5, gdown);
		p.updateInventory();
	}
}
