package we.Heiden.gca.Commands;

import org.bukkit.entity.Player;

import we.Heiden.gca.Configs.Config;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.hs2.Messages.Chat;

public class TutospawnCommand {

	public static void ts(Player p, String[] args) {
		int warp = 1;
		if (args.length > 0 && Functions.isInt(args[0]))
			warp = Integer.parseInt(args[0]);
		if (warp > 5)
			warp = 5;
		Functions.saveLoc("Tutorial." + warp, p);
		Config.save();
		new Chat(p).msg("&a&lTutorial Spawn " + warp + " set");
	}
}
