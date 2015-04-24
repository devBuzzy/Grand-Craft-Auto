package we.Heiden.gca.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Commands.SetnpcCommand;
import we.Heiden.gca.Functions.Polices;
import we.Heiden.gca.Functions.RobberyMode;

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
public class EntityDeath implements Listener {
	
	public EntityDeath(Plugin pl) {Bukkit.getPluginManager().registerEvents(this, pl);}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		e.getDrops().clear(); e.setDroppedExp(0);
		if (SetnpcCommand.villagers.containsKey(e.getEntity())) {
			SetnpcCommand.respawn.put(SetnpcCommand.villagers.get(e.getEntity()), 300);
			SetnpcCommand.villagers.remove(e.getEntity());
		} else if (Polices.polices.containsKey(e.getEntity())) {
			Player p = Polices.polices.get(e.getEntity());
			if (RobberyMode.robbery2.containsKey(p)) Polices.spawn(p, 1, 15);
			Polices.polices.remove(e.getEntity());
			Polices.players.get(p).remove(e.getEntity());
			if (Polices.players.get(p).isEmpty()) Polices.players.remove(p);
		}
	}
}
