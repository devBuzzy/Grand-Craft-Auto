package we.Heiden.gca.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import we.Heiden.gca.Functions.Wand;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.hs2.Core.HS2Api.Perms;
import we.Heiden.hs2.Messages.Chat;

public class CommandsHandler {

	public CommandsHandler(CommandSender sender, Command cmd, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (!Perms.hasPermission(sender, "admin")) p.sendMessage(Messager.e("You Can`t do that").get(0));
			else if (cmd.getName().equalsIgnoreCase("setGarageEntry")) Wand.give(p, "Garage Entry");
			else if (cmd.getName().equalsIgnoreCase("GarageWarp")) GaragewarpCommand.gw(p);
			else if (cmd.getName().equalsIgnoreCase("GarageExit")) Wand.give(p, "Garage Exit");
			else if (cmd.getName().equalsIgnoreCase("GarageSlot")) Wand.give(p, "Garage Slot", false);
			else if (cmd.getName().equalsIgnoreCase("Store")) StoreCommand.store(p, args);
			else if (cmd.getName().equalsIgnoreCase("AirportSpawn")) Functions.setWarp(p, "Airport", "Airport");
			else if (cmd.getName().equalsIgnoreCase("AirportArea")) Wand.give(p, "Airport");
		} else new Chat(sender).e("Console not supported");
	}
}
