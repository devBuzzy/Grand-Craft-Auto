package we.Heiden.gca.Misc;

import org.bukkit.entity.Player;

public class Others {
	public static boolean isInt(Player p, String s, String type, boolean zero) {
		try {
			int i = Integer.parseInt(s);
			if (i == 0 && zero) {
				p.sendMessage("Error: " + type + " can`t be Zero!");
				return false;
			} else if (s.startsWith("-")) {
				p.sendMessage("Error: " + type + " can`t be Zero!");
				return false;
			}
		} catch(Exception ex) {
			p.sendMessage("Error: " + type + " can`t be Zero!");
			return false;
		}
		return true;
	}
	
	public static boolean isInt(String s) {
		try {Integer.parseInt(s);
		} catch(Exception ex) {return false;}
		return true;
	}
}
