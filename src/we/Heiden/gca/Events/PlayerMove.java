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

import we.Heiden.gca.Commands.HomeCommand;
import we.Heiden.gca.Configs.Config;
import we.Heiden.gca.Functions.Bag;
import we.Heiden.gca.Functions.Cars;
import we.Heiden.gca.Functions.Garage;
import we.Heiden.gca.Functions.Houses;
import we.Heiden.gca.Functions.RobberyMode;
import we.Heiden.gca.Functions.Tutorial;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.gca.Utils.ItemUtils;

/**
 * *********************************************
 * <p>
 * <b>This has been made by <i>Heiden Team</b>
 * <ul>
 * <li>Don't claim this class as your own
 * <li>Don't remove this disclaimer
 * </ul>
 * <b>All rights reserved
 * <p>
 * Heiden Team 2015
 * <p>
 * </b> *********************************************
 **/
public class PlayerMove implements Listener {

	public PlayerMove(Plugin pl) {
		Bukkit.getPluginManager().registerEvents(this, pl);
	}

	public static List<Player> left = new ArrayList<Player>();
	public static HashMap<Player, List<Location>> area = new HashMap<Player, List<Location>>();
	public static List<Player> onAirport = new ArrayList<Player>();
	public static List<Player> onHospital = new ArrayList<Player>();
	public static HashMap<Player, Houses> onHouse = new HashMap<Player, Houses>();

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (Tutorial.tuto.containsKey(p)) {
			Location to = e.getTo();
			Location from = e.getFrom();
			if (to.getX() != from.getX() || to.getZ() != from.getZ())
				p.teleport(new Location(from.getWorld(), from.getX(),
						to.getY(), from.getZ(), to.getYaw(), to.getPitch()));
		} else {
			List<Item> li = null;
			if (!RobberyMode.Reward.isEmpty()) {
				li = new ArrayList<Item>();
				for (Player pl : RobberyMode.Reward.keySet())
					if (pl != p)
						li.add(RobberyMode.Reward.get(pl));
			}
			for (Entity ent : p.getWorld().getEntitiesByClasses(Item.class)) {
				Item enti = (Item) ent;
				if ((li == null || !li.contains(enti))
						&& enti.getLocation().distance(e.getTo()) < 0.5D) {
					if (RobberyMode.Reward.containsKey(p)
							&& enti.equals(RobberyMode.Reward.get(p)))
						RobberyMode.Reward.remove(p);
					Bag.inventories.get(p).addItem(enti.getItemStack());
					if (Bag.inventories.get(p).contains(enti.getItemStack()))
						ent.remove();
				}
			}
			if (e.getFrom().getBlock().getLocation() != e.getTo().getBlock()
					.getLocation()) {
				if (onHospital.contains(p)
						&& Functions.isOnArea(
								Functions.loadLoc("Hospital.Area.p1", p),
								Functions.loadLoc("Hospital.Area.p2", p),
								e.getTo())) {
					Location loc = Functions.loadLoc("Hospital.Warp", p);
					if (loc != null)
						p.teleport(loc);
					else
						p.teleport(p.getWorld().getSpawnLocation());
					onHospital.remove(p);
				} else if (onAirport.contains(p)
						&& Functions.isOnArea(
								Functions.loadLoc("AirportArea.p1", p),
								Functions.loadLoc("AirportArea.p2", p),
								e.getTo())) {
					for (Player pl : Bukkit.getOnlinePlayers())
						if (pl != p)
							pl.showPlayer(p);
					onAirport.remove(p);
				} else if (onHouse.containsKey(p)) {
					String path = onHouse.get(p).path;
					if (Functions.isOnArea(
							Functions.loadLoc(path + ".Exit.p1", p),
							Functions.loadLoc(path + ".Exit.p2", p), e.getTo())) {
						Location loc = Functions.loadLoc(path + ".Exitwarp", p);
						if (loc != null) {
							onHouse.remove(p);
							left.add(p);
							p.teleport(loc);
							for (Player pl : Bukkit.getOnlinePlayers())
								if (!pl.canSee(p))
									pl.showPlayer(p);
						} else
							p.teleport(p.getWorld().getSpawnLocation());
					} else if (left.contains(p)
							&& !Functions.isOnArea(
									Functions.loadLoc(path + ".Entry.p1", p),
									Functions.loadLoc(path + ".Entry.p2", p),
									e.getTo())) {
						left.remove(p);
						onHouse.remove(p);
					}
				} else {
					boolean go = true;
					if (!left.contains(p))
						for (Houses house : Houses.values()) {
							String path = house.path;
							if (Functions.isOnArea(
									Functions.loadLoc(path + ".Entry.p1", p),
									Functions.loadLoc(path + ".Entry.p2", p),
									e.getTo())) {
								Location loc = Functions.loadLoc(house.path
										+ ".Entrywarp", p);
								if (loc == null
										|| !(HomeCommand
												.c(".ExitWarp", ".EntryWarp",
														".Exit", ".Entry"))) {
									Messager.e1("This house isn`t ready yet");
									Location to = e.getTo();
									Location from = e.getFrom();
									p.teleport(new Location(from.getWorld(),
											from.getX(), to.getY(),
											from.getZ(), to.getYaw(), to
													.getPitch()));
								} else
									Functions.hide(p).teleport(loc);
								go = false;
								break;
							}
						}
					if (go)
						/* Garage */
						if (!Garage.onGarage.containsKey(p)) {
							if (left.contains(p)) {
								Location p1 = area.get(p).get(0);
								Location p2 = area.get(p).get(1);
								if (!Functions.isOnArea(p1, p2, e.getTo())) {
									left.remove(p);
									area.remove(p);
								}
							} else if (Config.get().contains("Garage.Entries"))
								for (String s : Config
										.get()
										.getConfigurationSection(
												"Garage.Entries")
										.getKeys(false)) {
									Location a1 = Functions.loadLoc(
											"Garage.Entries." + s + ".p1", p);
									if (a1 != null
											&& p.getWorld().equals(
													a1.getWorld())) {
										Location a2 = Functions.loadLoc(
												"Garage.Entries." + s + ".p2",
												p);
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
							Location a1 = Functions
									.loadLoc("Garage.Exit.p1", p);
							if (a1 != null)
								if (p.getWorld().equals(a1.getWorld())) {
									Location a2 = Functions.loadLoc(
											"Garage.Exit.p2", p);
									if (Functions.isOnArea(a1, a2, p))
										Garage.exit(p);
								}
						}
				}
				if (Cars.players.containsKey(p)) {
					boolean bol = Cars.temp.contains(p);
					if (!bol) {
						if (e.getTo().distance(
								Cars.players.get(p).getLocation()) > 20) {
							Cars.temp.add(p);
							p.getInventory()
									.setItem(5, ItemUtils.ItemDefault());
							p.getInventory()
									.setItem(6, ItemUtils.ItemDefault());
							p.updateInventory();
						}
					} else if (e.getTo().distance(
							Cars.players.get(p).getLocation()) <= 20) {
						Cars.temp.remove(p);
						p.getInventory().setItem(5, ItemUtils.Garage());
						p.getInventory().setItem(6, Cars.enums.get(p).getKey());
						p.updateInventory();
					}
				}
			}
		}
	}
}
