package we.Heiden.gca.Commands;

import org.bukkit.entity.Player;

import we.Heiden.gca.Stores.CarStore;
import we.Heiden.gca.Stores.HomeStore;

public class StoreCommand {

	public static void store(Player p, String[] args) {
		if (args[0].equalsIgnoreCase("Cars")) {
			if (args[1].equalsIgnoreCase("Purchase")) CarStore.Purchase(p);
			else CarStore.Manage(p);
		} else if (args[0].equalsIgnoreCase("House")) {
			if (args[1].equalsIgnoreCase("Purchase")) HomeStore.Purchase(p);
			else HomeStore.Manage(p);
		}
	}
}
