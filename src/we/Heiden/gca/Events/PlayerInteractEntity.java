package we.Heiden.gca.Events;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftVillager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import we.Heiden.gca.CustomEvents.Action;
import we.Heiden.gca.CustomEvents.NPCInteractEvent;
import we.Heiden.gca.Functions.Cars;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.NPCs.NMSNpc;
import we.Heiden.gca.NPCs.NPCs;

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
public class PlayerInteractEntity implements Listener {
	
	public PlayerInteractEntity(Plugin pl) {Bukkit.getPluginManager().registerEvents(this, pl);}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		Messager.load(p);
		if (e.getRightClicked() instanceof Minecart) {
			if (Cars.players.containsKey(p) && Cars.players.get(p).equals(e.getRightClicked())) {
				if (Cars.enums.containsKey(p) && p.getItemInHand().equals(Cars.enums.get(p).getKey())) {
					e.getRightClicked().setVelocity(new Vector(0, 0, 0));
					e.getRightClicked().setPassenger(p);
					Messager.e1("Turn your car");
					e.setCancelled(true);
				} else {
					Messager.e1("Invalid Key!");
					e.setCancelled(true);
				}
			} else if (Cars.vehicles.containsKey(e.getRightClicked())) {
				Messager.e1("This car isn`t yours!");
				e.setCancelled(true);
			}
		} else if (((CraftVillager)e.getRightClicked()).getHandle() instanceof NMSNpc && e.getRightClicked().getType().equals(EntityType.VILLAGER)) {
			NMSNpc target = (NMSNpc) ((CraftVillager)e.getRightClicked()).getHandle();
			NPCs type = null;
			boolean bol = false;
			for (NPCs types : NPCs.values()) {
				for (NMSNpc entity : NPCs.npcs.get(types).keySet()) if (entity.equals(target)) {
					type = types;
					bol = true;
					break;
				}
				if (bol) break;
			}
			if (type != null) {
				NPCInteractEvent event = new NPCInteractEvent(p, Action.RIGHT_CLICK, target, type);
				Bukkit.getPluginManager().callEvent(event);
				if (event.isCancelled()) e.setCancelled(true);
			}
		}
	}
}
