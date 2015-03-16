package we.Heiden.gca.Functions;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;

import we.Heiden.gca.Configs.Config;
import we.Heiden.gca.Configs.PlayerConfig;
import we.Heiden.gca.Holograms.HologramUtils;
import we.Heiden.gca.Holograms.NMSEntity;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.Utils.EntityHider;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.gca.core.Main;

public class Garage {

	public static HashMap<Player, Location> onGarage = new HashMap<Player, Location>();
	public static HashMap<Player, List<Entity>> exposed = new HashMap<Player, List<Entity>>();
	public static HashMap<Player, List<NMSEntity>> holograms = new HashMap<Player, List<NMSEntity>>();

	public static void enter(Player p) {
		Messager.load(p);
		Location loc = Functions.loadLoc("Garage.Warp", p);
		if (loc == null) Messager.e1("Config Error &7(&2Garage.Warp&7)");
		else {
			onGarage.put(p, p.getLocation());
			expose(p);
			p.teleport(loc);
			Messager.s1("Welcome to the Garage!");
		}
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
		Messager.s1("You had quit the garage");
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
				for (Player pl : Bukkit.getOnlinePlayers()) if (pl != p) {
					hider.hideEntity(pl, vehicle);
					/*hider.hideEntity(pl, hm.getBukkitEntityNMS());*/
				}
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
}
