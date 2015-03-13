package we.Heiden.gca.Commands;

import org.bukkit.entity.Player;

import we.Heiden.gca.Configs.Config;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.Utils.Functions;

public class GaragewarpCommand {

	public static void gw(Player p) {
		Functions.saveLoc("Garage.Warp", p);
		Config.save();
		Messager.load(p);
		Messager.s1("Garage Warp Set");
	}
}
