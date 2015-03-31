package we.Heiden.gca.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Commands.SetnpcCommand;

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
		}
	}
}
