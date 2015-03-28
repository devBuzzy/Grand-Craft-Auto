package we.Heiden.gca.Events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Configs.Config;
import we.Heiden.gca.Functions.Bag;
import we.Heiden.gca.Functions.Garage;
import we.Heiden.gca.Functions.Tutorial;
import we.Heiden.gca.Utils.Functions;

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
public class PlayerMove implements Listener {
	
	public PlayerMove(Plugin pl) {Bukkit.getPluginManager().registerEvents(this, pl);}
	public static List<Player> left = new ArrayList<Player>();
	public static HashMap<Player, List<Location>> area = new HashMap<Player, List<Location>>();
	public static List<Player> onAirport = new ArrayList<Player>();
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (Tutorial.tuto.containsKey(p)) {
			Location to = e.getTo();
			Location from = e.getFrom();
			if (to.getX() != from.getX() || to.getZ() != from.getZ()) p.teleport(new Location(from.getWorld(), from.getX(), to.getY(), from.getZ(), to.getYaw(), to.getPitch()));
		} else {
			for (Entity ent : p.getWorld().getEntitiesByClasses(Item.class)) {
				Item enti = (Item) ent;
				if (enti.getLocation().distance(e.getTo()) < 0.5) {
					Bag.inventories.get(p).addItem(enti.getItemStack());
					if (Bag.inventories.get(p).contains(enti.getItemStack())) ent.remove();
				}
			}
			if (e.getFrom().getBlock().getLocation() != e.getTo().getBlock().getLocation()) {
				if (Functions.isOnArea(Functions.loadLoc("AirportArea.p1", p), Functions.loadLoc("AirportArea.p2", p), e.getTo()) && onAirport.contains(p))
					for (Player pl : Bukkit.getOnlinePlayers()) if (pl != p) pl.showPlayer(p);
				/*Garage*/
				if (!Garage.onGarage.containsKey(p)) {
					if (left.contains(p)) {
						Location p1 = area.get(p).get(0);
						Location p2 = area.get(p).get(1);
						if (!Functions.isOnArea(p1, p2, e.getTo())) {
							left.remove(p);
							area.remove(p);
						}
					} else if (Config.get().contains("Garage.Entries")) for (String s : Config.get().getConfigurationSection("Garage.Entries").getKeys(false)) {
						Location a1 = Functions.loadLoc("Garage.Entries." + s + ".p1", p);
						if (a1 != null && p.getWorld().equals(a1.getWorld())) {
							Location a2 = Functions.loadLoc("Garage.Entries." + s + ".p2", p);
							if (Functions.isOnArea(a1, a2, p)) {
								Garage.enter(p);
								List<Location> locs = new ArrayList<Location>();
								locs.addAll(Arrays.asList(a1, a2));
								area.put(p, locs);
								left.add(p);
								break;
							}
						}
					}
				} else {
					Location a1 = Functions.loadLoc("Garage.Exit.p1", p);
					if (a1 != null) if (p.getWorld().equals(a1.getWorld())) {
						Location a2 = Functions.loadLoc("Garage.Exit.p2", p);
						if (Functions.isOnArea(a1, a2, p)) Garage.exit(p);
					}
				}
			}
		}
	}
}
