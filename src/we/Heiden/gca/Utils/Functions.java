package we.Heiden.gca.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import we.Heiden.gca.Messages.Messager;

public class Functions {
	
	public static FileConfiguration fc = null;
	
	public static boolean contains(Object[] list, Object i) {
		for (Object o : list) if (o.equals(i)) return true;
		return false;
	}
	
	public static <T> List<T> newList() {
		return new ArrayList<T>();
	}
	
	public static boolean isInt(Player p, String s, String type, boolean zero, boolean negative, boolean msg) {
		try {
			int i = Integer.parseInt(s);
			if (i == 0 && zero) {
				if (msg) p.sendMessage("Error: " + type + " can`t be Zero!");
				return false;
			} else if (s.startsWith("-") && negative) {
				if (msg) p.sendMessage("Error: " + type + " can`t be Zero!");
				return false;
			}
		} catch(Exception ex) {
			if (msg) p.sendMessage("Error: " + type + " can`t be Zero!");
			return false;
		}
		return true;
	}

	public static boolean isInt(String s) {return isInt(null, s, null, true, true, false);}
	public static boolean isInt(String s, boolean zero, boolean negative) {return isInt(null, s, null, zero, negative, false);}
	
	public static void clear(Player p) {
		p.getInventory().clear();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
	}
	
	public static void saveLoc(String path, Location loc, FileConfiguration fc) {
		fc.set(path + ".world", loc.getWorld().getName());
		fc.set(path + ".x", loc.getX());
		fc.set(path + ".y", loc.getY());
		fc.set(path + ".z", loc.getZ());
		fc.set(path + ".yaw", loc.getYaw());
		fc.set(path + ".pitch", loc.getPitch());
	}
	
	public static Location loadLoc(String path, FileConfiguration fc) {
		if (!fc.contains(path)) return null;
		World w = Bukkit.getWorld(fc.getString(path + ".world"));
		double x = fc.getDouble(path + ".x");
		double y = fc.getDouble(path + ".y");
		double z = fc.getDouble(path + ".z");
		float yaw = (float) fc.getDouble(path + ".yaw");
		float pitch = (float) fc.getDouble(path + ".pitch");
		return new Location(w, x, y, z, yaw, pitch);
	}
	
	public static boolean canFc(Player p) {
		if (fc == null) {
			Messager.load(p);
			Messager.e1("Try Again");
			return false;
		}
		return true;
	}
	
	
	public static void saveLoc(String path, Location loc, Player p) {if (canFc(p)) saveLoc(path, loc, fc);}
	public static void saveLoc(String path, Player p) {if (canFc(p)) saveLoc(path, p.getLocation(), fc);}
	public static void saveLoc(String path, Player p, FileConfiguration fc) {saveLoc(path, p.getLocation(), fc);}
	public static Location loadLoc(String path, Player p) {if (canFc(p)) return loadLoc(path, fc); else return null;}
	
	public static boolean isOnArea(Location p1, Location p2, Location loc) {
		if (p1 == null || p2 == null) return false;
		double x = loc.getBlockX();
		double y = loc.getBlockY();
		double z = loc.getBlockZ();
		double xp1 = p1.getX();
		double xp2 = p2.getX();
		double yp1 = p1.getY();
		double yp2 = p2.getY();
		double zp1 = p1.getZ();
		double zp2 = p2.getZ();
		double xM;
		double yM;
		double zM;
		double xm;
		double ym;
		double zm;
		
		if (xp1 > xp2) xM = xp1;
		else xM = xp2;
		if (yp1 > yp2) yM = yp1;
		else yM = yp2;
		if (zp1 > zp2) zM = zp1;
		else zM = zp2;

		if (xM == xp1) xm = xp2;
		else xm = xp1;
		if (yM == yp1) ym = yp2;
		else ym = yp1;
		if (zM == zp1) zm = zp2;
		else zm = zp1;
		
		if (x > xM || y > yM || z > zM || x < xm || y < ym || z < zm) return false;
		return true;
	}
	
	public static boolean isOnArea(Location p1, Location p2, Player p) {return isOnArea(p1, p2, p.getLocation());}
}
