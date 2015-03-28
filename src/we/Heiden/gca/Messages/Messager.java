package we.Heiden.gca.Messages;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import we.Heiden.gca.Functions.SettingsEnum;

public class Messager {

	private static Player p;
	
	public static void load(Player p) { Messager.p = p; }
	
	public static void msg(boolean bol, String... str) {
		boolean hb = true; if (str.length > 1) hb = false; else {SettingsEnum.setPlayer(p); if (SettingsEnum.HOTBAR.getValue().equals("Off")) hb = false;}
		
		if (hb) ActionBar.sendMessage(p, str[0]);
		else {
			if (bol) p.sendMessage("");
			for (String s : str) p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
			if (bol) p.sendMessage("");
		}
	}
	
	public static void msg(boolean bol, List<String> str) {
		boolean hb = true; if (str.isEmpty() || str.size() > 1) hb = false; else {SettingsEnum.setPlayer(p); if (SettingsEnum.HOTBAR.getValue().equals("Off")) hb = false;}
		
		if (hb) ActionBar.sendMessage(p, str.get(0));
		else {
			if (bol) p.sendMessage("");
			for (String s : str) p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
			if (bol) p.sendMessage("");
		}
	}
	
	public static void msg(String... str) {msg(false, str);}
	public static void msg(List<String> str) {msg(false, str);}
	public static List<String> msg(boolean bol, String color1, String color2, String color3, String... str) {
		boolean hb = true; if (str.length > 1) hb = false; else {SettingsEnum.setPlayer(p); if (SettingsEnum.HOTBAR.getValue().equals("Off")) hb = false;}
		
		List<String> ls = new ArrayList<String>();
		if (!hb) for (String s : str) ls.add("    " + color1 + ">> " + color2 + s + " " + color1 + "<<");
		else for (String s : str) ls.add(color3 + s);
		if (bol) msg(ls);
		return ls;
	}

	public static List<String> s(String... str) {return msg(false, "&b&l", "&6", "&6&l", str);}
	public static List<String> e(String... str) {return msg(false, "&8&l", "&c", "&c&l", str);}
	public static List<String> c(String color1, String... str) {return msg(false, color1, "&b", "&b&l", str);}
	public static List<String> s1(String... str) {return msg(true, "&b&l", "&6", "&6&l", str);}
	public static List<String> e1(String... str) {return msg(true, "&8&l", "&c", "&c&l", str);}
	public static List<String> c1(String color1, String... str) {return msg(true, color1, "&b", "&b&l", str);}
	
}
