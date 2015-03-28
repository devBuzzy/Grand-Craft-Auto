package we.Heiden.gca.Events;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Commands.StoreCommand;
import we.Heiden.gca.Stores.BankStore;
import we.Heiden.gca.Stores.PetStore;
import we.Heiden.hs2.Messages.Chat;
import we.Heiden.hs2.Utils.Functions;
import we.Heiden.gca.Functions.Tutorial;

/**
 * ********************************************* <p>
 * <b>This has been made by <i>Heiden Team</b>
 * <ul>
 * <li>Don't claim this class as your own
 * <li>Don't remove this disclaimer
 * </ul>
 *         <b>All rights reserved <p>
 *           Heiden Team 2015 <p></b>
 * ********************************************* 
 **/
public class AsyncPlayerChat implements Listener {
	
	public AsyncPlayerChat(Plugin pl) {Bukkit.getPluginManager().registerEvents(this, pl);}
	
	public static HashMap<Player, String> name = new HashMap<Player, String>();
	
	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String msg = e.getMessage();
		if (PetStore.confirming.containsKey(p)) {
			e.setCancelled(true);
			if (PetStore.confirming.get(p)) {
				if (msg.equals(name.get(p))) {
					name.remove(p);
					PetStore.confirming.remove(p);
					PetStore.finish(p);
				} else new Chat(p).msg("&cInvalid Verification, type the name again");
			} else {
				name.put(p, msg);
				new Chat(p).msg("&bConfirmation &l►► &aEnter the name again");
				PetStore.confirming.put(p, true);
			}
		} else if (StoreCommand.bank.containsKey(p)) {
			e.setCancelled(true);
			if (Functions.isInt(p, msg, "Amount")) {
				if (StoreCommand.bank.get(p).equals("Deposit")) BankStore.deposit(p, Integer.parseInt(msg));
				else BankStore.withdraw(p, Integer.parseInt(msg));
				StoreCommand.bank.remove(p);
			}
		}
		if (!e.isCancelled()) for (Player pl : Tutorial.tuto.keySet()) e.getRecipients().remove(pl);
	}
}
