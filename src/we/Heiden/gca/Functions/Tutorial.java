package we.Heiden.gca.Functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import we.Heiden.gca.Utils.Functions;
import we.Heiden.hs2.Messages.Chat;

public class Tutorial {

	public static HashMap<Player, Integer> tuto = new HashMap<Player, Integer>();
	private static HashMap<Player, Location> back = new HashMap<Player, Location>();
	
	public static void tutorial(Player p, int elapsed) {
		if (elapsed == 1) {
			back.put(p, p.getLocation());
			p.teleport(Functions.loadLoc("Tutorial.1", p));
		} else if (elapsed == 10) p.teleport(Functions.loadLoc("Tutorial.2", p));
		else if (elapsed == 20) p.teleport(Functions.loadLoc("Tutorial.3", p));
		if (elapsed < 5 || (elapsed > 9 && elapsed < 15) || (elapsed > 19 && elapsed < 25 || elapsed == 33)) {
			List<String> msg;
			if (elapsed < 5) msg = tuto01(elapsed);
			else if (elapsed < 15) msg = tuto02(elapsed-9);
			else if (elapsed < 25) msg = tuto03(elapsed-19);
			else msg = end();
			
			for (int n = 1; n <= 10-msg.size(); n++) p.sendMessage("");
			new Chat(p).msg(msg);
			if (elapsed == 33) {
				tuto.remove(p);
				p.teleport(back.get(p));
				back.remove(p);
			}
		}
	}
	
	public static List<String> tuto01(int elapsed) {
		List<String> msg = new ArrayList<String>();
		msg.addAll(Arrays.asList("&6&l===========================================", "&aWelcome to GrandCraftAuto &7&o(GTA in Minecraft)"));
		if (elapsed > 1) msg.add("&aYou are eligible to commit robberies, murder, stealing and more!");
		if (elapsed > 2) msg.add("&aYou can use &b&oHitslain Coins &ato buy guns, cars, and more!");
		if (elapsed > 3) msg.add("&6&l===========================================");
		return msg;
	}
	
	public static List<String> tuto02(int elapsed) {
		List<String> msg = new ArrayList<String>();
		msg.add("&6&l===========================================");
		if (elapsed > 1) msg.add("&aYou can buy tons of different homes to live in!");
		if (elapsed > 2) msg.add("&aWant friends to come over? No problem!");
		if (elapsed > 3) msg.add("&aWant to sell your home? No problem, but you’ll lose some money!");
		if (elapsed > 4) msg.add("&6&l===========================================");
		return msg;
	}
	
	public static List<String> tuto03(int elapsed) {
		List<String> msg = new ArrayList<String>();
		msg.add("&6&l===========================================");
		if (elapsed > 1) msg.add("&aYou can open a business and earn money");
		if (elapsed > 2) msg.add("&aJust find an empty lot, and buy it!");
		if (elapsed > 3) msg.add("&aYou can store it with desired goods!");
		if (elapsed > 4) msg.add("&6&l===========================================");
		return msg;
	}
	
	public static List<String> end() {
		List<String> msg = new ArrayList<String>();
		msg.addAll(Arrays.asList(
				"&6&l===========================================", 
				" &aYou can even buy cars!", 
				" &aWhen you buy a car, itll be stored in your garage!", 
				"&e&l===========================================", 
				"&c&lWant to know more?", 
				"&9Watch this full tutorial:", 
				"http://www.VIDEOCOMINGSOON.com", 
				"&6&l==========================================="));
		return msg;
	}
}
