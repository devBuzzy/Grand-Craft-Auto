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
	
	private static void gEntry(Player p) {
		int n = 1;
		if (Config.get().contains("Garage.Entries")) n = Config.get().getConfigurationSection("Garage.Entries").getKeys(false).size()+1;
		Functions.saveLoc("Garage.Entries." + n + ".p1", p1.get(p), p);
		Functions.saveLoc("Garage.Entries." + n + ".p2", p2.get(p), p);
		Config.save();
		Messager.load(p);
		Messager.s1("Garage Entry &dNº" + n + " &6set");
	}
	
	public static void gExit(Player p) {
		Functions.saveLoc("Garage.Exit.p1", p1.get(p), p);
		Functions.saveLoc("Garage.Exit.p2", p2.get(p), p);
		Config.save();
		Messager.load(p);
		Messager.s1("Garage Exit Set");
	}
	
	public static void gSlot(Player p) {
		int n = 1;
		if (Config.get().contains("Garage.Slots")) n = Config.get().getConfigurationSection("Garage.Slots").getKeys(false).size()+1;
		Functions.saveLoc("Garage.Slots." + n, p1.get(p), p);
		Config.get().set("Garage.Slots." + n + ".Rotation", 90F);
		Config.save();
		Messager.load(p);
		Messager.s1("Garage Slot &dNº" + n + " &6set");
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
					if (type.equals("Garage Entry")) gEntry(p);
					else if (type.equals("Garage Exit")) gExit(p);
					else if (type.equals("Garage Slot")) gSlot(p);
					CMessager.msg("");
					clear(p);
				}
			}
		}
	}
}
