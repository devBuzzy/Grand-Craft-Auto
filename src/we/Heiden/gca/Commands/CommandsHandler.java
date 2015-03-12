package we.Heiden.gca.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import we.Heiden.gca.Misc.CMessager;
import we.Heiden.gca.Misc.Wand;

public class CommandsHandler {

	public CommandsHandler(CommandSender sender, Command cmd, String[] args) {
		
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("setgarage")) Wand.give(p, "Garage");
		} else {
			CMessager.load(sender);
			CMessager.e1("This Command is for players");
		}
	}
}
