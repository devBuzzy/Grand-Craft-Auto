package we.Heiden.gca.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import we.Heiden.gca.Functions.Wand;
import we.Heiden.gca.Messages.CMessager;
import we.Heiden.gca.Messages.Messager;

public class CommandsHandler {

	public CommandsHandler(CommandSender sender, Command cmd, String[] args) {
		
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (!p.hasPermission("GCA.Admin")) p.sendMessage(Messager.e("You Can`t do that").get(0));
			else if (cmd.getName().equalsIgnoreCase("GarageArea")) Wand.give(p, "Garage Area");
			else if (cmd.getName().equalsIgnoreCase("setGarageEntry")) Wand.give(p, "Garage Entry");
			else if (cmd.getName().equalsIgnoreCase("GarageWarp")) GaragewarpCommand.gw(p);
		} else {
			CMessager.load(sender);
			CMessager.e1("This Command is for players");
		}
	}
}
