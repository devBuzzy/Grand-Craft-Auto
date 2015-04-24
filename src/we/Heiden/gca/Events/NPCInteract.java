package we.Heiden.gca.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Commands.StoreCommand;
import we.Heiden.gca.CustomEvents.NPCInteractEvent;
import we.Heiden.gca.Functions.CellPhone;
import we.Heiden.hs2.Messages.Chat;

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
public class NPCInteract implements Listener {
	
	public NPCInteract(Plugin pl) {Bukkit.getPluginManager().registerEvents(this, pl);}
	
	@EventHandler
	public void onNPCInteract(NPCInteractEvent e) {
		Player p = e.getPlayer();
		if (CellPhone.calling.containsKey(p) || CellPhone.onCall.containsKey(p)) new Chat(p).e("You are on a call");
		else if (CellPhone.msg.containsKey(p)) new Chat(p).e("You are writting a message");
		else {
			e.getType().getStore().options(p);
			StoreCommand.npc.put(p, e.getTarget());
		}
		e.setCancelled(true);
	}
}
