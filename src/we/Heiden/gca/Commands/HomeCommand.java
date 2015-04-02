package we.Heiden.gca.Commands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import we.Heiden.gca.Configs.Config;
import we.Heiden.gca.Configs.PlayerConfig;
import we.Heiden.gca.Functions.Houses;
import we.Heiden.gca.Messages.Messager;

public class HomeCommand {

	private static Houses house;
	
	public static void home(Player p, String[] args) {
		Messager.load(p);
		if (!PlayerConfig.get(p).contains("Houses")) Messager.e1("You don`t have any house");
		else if (args.length < 1) Messager.e1("Specify a Home Type");
		else {
			List<String> ls = PlayerConfig.get().getStringList("Houses");
			Houses house = null;
			for (String s : ls) {if (s.equalsIgnoreCase(args[0])) { house = Houses.matchWarp(args[0]); break; }}
			if (house == null) Messager.e1("Couldn`t find the House " + args[0]);
			else {
				HomeCommand.house = house;
				Location loc = we.Heiden.gca.Utils.Functions.loadLoc(house.path + ".Entrywarp", p);
				if (loc == null || !(c(".ExitWarp", ".EntryWarp", ".Exit", ".Entry"))) Messager.e1("This house isn`t ready yet");
				else we.Heiden.gca.Utils.Functions.hide(p).teleport(loc);
			}
		}
	}
	
	public static boolean c(String... paths) {
		boolean bol = true;
		for (String path : paths) if (!Config.get().contains(house.path + path)) { bol = false; break; }
		return bol;
	}
}
