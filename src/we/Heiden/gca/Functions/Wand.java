package we.Heiden.gca.Functions;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Configs.Config;
import we.Heiden.gca.Messages.CMessager;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.gca.Utils.ItemUtils;

public class Wand implements Listener {

	public Wand(Plugin pl) {Bukkit.getPluginManager().registerEvents(this, pl);}
	
	public static ItemStack wand = ItemUtils.getItem(Material.BLAZE_ROD, "&9&lSelector", "Use This to", "Select the areas", "of this awesome", "plugin!");
	private static HashMap<Player, String> selecting = new HashMap<Player, String>();
	private static HashMap<Player, String> selecting2 = new HashMap<Player, String>();
	private static HashMap<Player, ItemStack> item = new HashMap<Player, ItemStack>();
	private static HashMap<Player, Integer> slot = new HashMap<Player, Integer>();
	private static HashMap<Player, Location> p1 = new HashMap<Player, Location>();
	private static HashMap<Player, Location> p2 = new HashMap<Player, Location>();
	
	public static void give(Player p, String type, boolean bol) {
		item.put(p, p.getItemInHand());
		slot.put(p, p.getInventory().getHeldItemSlot());
		p.setItemInHand(wand);
		p.updateInventory();
		if (bol) selecting.put(p, type);
		else selecting2.put(p, type);
		CMessager.load(p);
		CMessager.msg("", "&b&lSelector Mode On!");
	}
	
	public static void give(Player p, String type) {give(p, type, true);}
	
	public static void cancel(Player p) {
		clear(p);
		CMessager.load(p);
		CMessager.e1("Selector Canceled");
	}
	
	private static void clear(Player p) {
		p.getInventory().setItem(slot.get(p), item.get(p));
		p.updateInventory();
		selecting.remove(p);
		selecting2.remove(p);
		item.remove(p);
		slot.remove(p);
		p1.remove(p);
		p2.remove(p);
	}
	
	private static void withList(Player p, String path, String type) {
		int n = 1;
		if (Config.get().contains(path)) n = Config.get().getConfigurationSection(path).getKeys(false).size()+1;
		saveLoc2p(p, path + "." + n, type.replace("%n%", n + ""));
	}
	
	public static void saveLoc2p(Player p, String type, String path) {
		Functions.saveLoc(path + ".p1", p1.get(p), p);
		Functions.saveLoc(path + ".p2", p2.get(p), p);
		Config.save();
		Messager.load(p);
		Messager.s1(type + " Set");
	}
	
	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent e) {if (selecting.containsKey(e.getPlayer()) || selecting2.containsKey(e.getPlayer())) cancel(e.getPlayer());}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (selecting.containsKey(e.getPlayer()) || selecting2.containsKey(e.getPlayer())) {
			if (e.getClickedBlock() != null) {
				Player p = e.getPlayer();
				CMessager.load(p);
				Location loc = e.getClickedBlock().getLocation();
				if (selecting.containsKey(p)) {
					int n = 2;
					if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
						n = 1;
						p1.put(p, loc);
					} else p2.put(p, loc);
					CMessager.msg("&d&l>> &7" + selecting.get(p) + ": &dPoint &b" + n + " &dSet! &8&l(&a" + loc.getBlockX()+"&7, &a"+loc.getBlockY()+"&7, &a"+loc.getBlockZ()+"&8&l)");
				} else p1.put(p, loc);
				if (p1.containsKey(p) && p2.containsKey(p) || selecting2.containsKey(p)) {
					CMessager.s1("Selector Finished");
					String type;
					if (selecting.containsKey(p)) type = selecting.get(p);
					else type = selecting2.get(p);
					if (type.equals("Garage Entry")) withList(p, "Garage.Entries", "Garage Entry &d%n%&6");
					else if (type.equals("Garage Exit")) saveLoc2p(p, "Garage Exit", "Garage.Exit");
					else if (type.equals("Garage Slot")) withList(p, "Garage.Slots", "Garage Slot &d%n%&6");
					else if (type.equals("Airport")) saveLoc2p(p, "Airport Area", "AirportArea");
					else if (type.equals("Hospital")) saveLoc2p(p, "Hospital Area", "Hospital.Area");
					CMessager.msg("");
					clear(p);
				}
			}
		}
	}
}
