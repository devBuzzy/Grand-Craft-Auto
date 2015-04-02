package we.Heiden.gca.Commands;

import org.bukkit.entity.Player;

import we.Heiden.gca.Functions.Houses;
import we.Heiden.gca.Functions.Wand;
import we.Heiden.hs2.Messages.Chat;
import we.Heiden.hs2.Utils.Functions;

public class SethomeCommand {

	public static void setHome(Player p, String[] args) {
		Chat c = new Chat(p);
		if (args.length < 2) c.e("Syntax: /shome (home) (entry/exit/exitwarp/entrywarp)");
		else {
			Houses home = Houses.matchWarp(args[0]);
			if (home == null) c.e("Could not find " + args[0]);
			else if (Functions.equals(args[1], "entry", "exit")) Wand.give(p, home.toString() + Functions.normalize(args[0]));
			else if (Functions.equals(args[1], "exitwarp", "entrywarp")) {
				String path = home.path + "." + (args[1].equalsIgnoreCase("exitwarp") ? "ExitWarp" : "EntryWarp");
				we.Heiden.gca.Utils.Functions.setWarp(p, "&a&l" + (args[1].equalsIgnoreCase("exitwarp") ? "Exit Warp" : "Entry Warp"), path);
			} else c.e("Option " + args[1] + " seems not to exist");
		} 
	}
}
