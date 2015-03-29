package we.Heiden.gca.Functions;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import we.Heiden.gca.Configs.Config;
import we.Heiden.gca.Configs.PlayerConfig;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.Utils.Confirmable;
import we.Heiden.gca.Utils.EntityHider;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.gca.core.Main;
import we.Heiden.gca.core.Timer20T;
import we.Heiden.hs2.Holograms.HologramUtils;
import we.Heiden.hs2.Holograms.NMSEntity;

public class Garage implements Confirmable {

	public static HashMap<Player, Location> onGarage = new HashMap<Player, Location>();
	public static HashMap<Player, List<Entity>> exposed = new HashMap<Player, List<Entity>>();
	public static HashMap<Player, List<NMSEntity>> holograms = new HashMap<Player, List<NMSEntity>>();
	public static HashMap<Entity, CarsEnum> cars = new HashMap<Entity, CarsEnum>();

	public static void enter(Player p) {
		if (Timer20T.toRemove.containsKey(p)) {
			remove(p, (Entity) Timer20T.toRemove.get(p).get(0));
			Timer20T.toRemove.remove(p);
		}
		Messager.load(p);
		Location loc = Functions.loadLoc("Garage.Warp", p);
		if (loc == null) Messager.e1("Config Error &7(&2Garage.Warp&7)");
		else {
			onGarage.put(p, p.getLocation());
			expose(p);
			p.teleport(loc);
			Messager.s1("Welcome back to your Garage");
		}
		p.getInventory().setItem(5, ItemUtils.ItemDefault());
		p.getInventory().setItem(6, ItemUtils.ItemDefault());
	}
	
	public static void exit(Player p) {
		if (!onGarage.containsKey(p)) return;
		Messager.load(p);
		Location loc = onGarage.get(p);
		loc.setYaw(loc.getYaw()+180F);
		if (loc.getYaw() >= 180) loc.setYaw(loc.getYaw()-360);
		p.teleport(loc);
		hide(p);
		onGarage.remove(p);
		Messager.s1("You've left your Garage");
	}
	
	public static void expose(Player p) {
		PlayerConfig.load(p);
		if (PlayerConfig.get().contains("Vehicles.Cars") && Config.get().contains("Garage.Slots")) {
			Object[] ls = Config.get().getConfigurationSection("Garage.Slots").getKeys(false).toArray();
			int n = -1;
			EntityHider hider = new EntityHider(Main.pl, EntityHider.Policy.BLACKLIST);
			for (String s : PlayerConfig.get().getStringList("Vehicles.Cars")) {
				n++;
				if (ls.length < n+1) break;
				Location loc = Functions.loadLoc("Garage.Slots." + ls[n], p);
				loc.setZ(loc.getZ()+0.5);
				loc.setX(loc.getX()-0.5);
				Location loc2 = loc.clone();
				loc2.setY(loc2.getY()+0.5D);
				CarsEnum car = CarsEnum.valueOf(s.substring(4).toUpperCase().replace(" ", "_"));
				NMSEntity hm = HologramUtils.spawnNMSArmorStand(loc2.getWorld(), loc2.getX(), loc2.getY(), loc2.getZ(), car.getCN());
				if (holograms.containsKey(p)) holograms.get(p).add(hm);
				else {
					List<NMSEntity> holograms2 = Functions.newList();
					holograms2.add(hm);
					holograms.put(p, holograms2);
				}
				float yaw = (float) Config.get().getDouble("Garage.Slots." + ls[n] + ".Rotation");
				loc.setYaw(yaw);
				loc.setY(loc.getY()+1D);
				Minecart vehicle = (Minecart) p.getWorld().spawnEntity(loc, EntityType.MINECART);
				vehicle.getLocation().setDirection(loc.getDirection());
				for (Player pl : Bukkit.getOnlinePlayers()) if (pl != p) hider.hideEntity(pl, vehicle);
				cars.put(vehicle, car);
				if (exposed.containsKey(p)) exposed.get(p).add(vehicle);
				else {
					List<Entity> ls2 = Functions.newList();
					ls2.add(vehicle);
					exposed.put(p, ls2);
				}
			}
		}
	}
	
	public static void hide(Player p) {
		if (exposed.containsKey(p)) for (Entity e : exposed.get(p)) e.remove();
		exposed.remove(p);
		if (holograms.containsKey(p)) for (NMSEntity e : holograms.get(p)) e.killEntityNMS();
		holograms.remove(p);
	}
	
	public static void remove(Player p, Entity ent) {
		Cars.enums.remove(p);
		Cars.velocity.remove(p);
		Cars.vec.remove(p);
		Cars.players.remove(p);
		Cars.vehicles.remove(ent);
		ent.remove();
		p.getInventory().setItem(5, ItemUtils.ItemDefault());
		p.getInventory().setItem(6, ItemUtils.ItemDefault());
	}
	
	private static HashMap<Player, Entity> car = new HashMap<Player, Entity>();
	
	public static void display(Player p, Entity ent) {
		ItemUtils.yes.put(p, new Garage());
		car.put(p, ent);
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&a&lUse this car?"));
		for (int n = 0; n < inv.getSize(); n++) inv.setItem(n, ItemUtils.ItemDefault());
		inv.setItem(12, ItemUtils.Yes());
		inv.setItem(14, ItemUtils.No());
		
		p.openInventory(inv);
	}
	
	public void no(Player p) { p.closeInventory(); }

	@Override
	public void yes(Player p) {
		p.closeInventory();
		CarsEnum care = cars.get(car.get(p));
		exit(p);
		p.getInventory().setItem(6, care.getItem());
	}
}
