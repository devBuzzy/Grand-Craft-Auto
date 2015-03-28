package we.Heiden.gca.Commands;

import java.util.HashMap;

import org.bukkit.entity.Player;

import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.Stores.CarStore;
import we.Heiden.gca.Stores.ClerkStore;
import we.Heiden.gca.Stores.HomeStore;
import we.Heiden.gca.Stores.PetStore;
import we.Heiden.hs2.Messages.Chat;

public class StoreCommand {

	public static HashMap<Player, String> bank = new HashMap<Player, String>();
	
	public static void store(Player p, String[] args) {
		if (args[0].equalsIgnoreCase("Cars")) {
			if (args[1].equalsIgnoreCase("Purchase")) CarStore.Purchase(p);
			else CarStore.Manage(p);
		} else if (args[0].equalsIgnoreCase("House")) {
			if (args[1].equalsIgnoreCase("Purchase")) HomeStore.Purchase(p);
			else HomeStore.Manage(p);
		} else if (args[0].equalsIgnoreCase("Pet")) PetStore.Shop(p);
		else if (args[0].equalsIgnoreCase("Clerk")) new ClerkStore().display(p);
		else if (args[0].equalsIgnoreCase("Bank")) {
			if (!bank.containsKey(p)) {
				new Chat(p).msg("&6&lBank&d&l►► &eType an amount");
				bank.put(p, args[1]);
			}
		} else if (args[0].equals("Robbery")) {
			Messager.load(p);
			Messager.e1("This is not ready yet");
		}
	}
}
