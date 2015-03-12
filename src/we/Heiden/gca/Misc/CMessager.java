package we.Heiden.gca.Misc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CMessager {

	private static CommandSender p;
	
	public static void load(CommandSender p) {CMessager.p = p;}
	
	public static void msg(boolean bol, String... str) {
		if (bol) p.sendMessage("");
		for (String s : str) p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
		if (bol) p.sendMessage("");
	}
	
	public static void msg(boolean bol, List<String> str) {
		if (bol) p.sendMessage("");
		for (String s : str) p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
		if (bol) p.sendMessage("");
	}
	
	public static void msg(String... str) {msg(false, str);}
	public static void msg(List<String> str) {msg(false, str);}
	public static List<String> msg(boolean bol, String color1, String color2, String... str) {
		List<String> ls = new ArrayList<String>();
		for (String s : str) ls.add("    " + color1 + ">> " + color2 + s + " " + color1 + "<<");
		if (bol) msg(ls);
		return ls;
	}

	public static List<String> s(String... str) {return msg(false, "&b&l", "&6", str);}
	public static List<String> e(String... str) {return msg(false, "&8&l", "&c", str);}
	public static List<String> c(String color1, String... str) {return msg(false, color1, "&b", str);}
	public static List<String> s1(String... str) {return msg(true, "&b&l", "&6", str);}
	public static List<String> e1(String... str) {return msg(true, "&8&l", "&c", str);}
	public static List<String> c1(String color1, String... str) {return msg(true, color1, "&b", str);}
	
}
