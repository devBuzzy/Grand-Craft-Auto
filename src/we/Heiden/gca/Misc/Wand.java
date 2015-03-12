package we.Heiden.gca.Misc;

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

public class Wand implements Listener {

	public Wand(Plugin pl) {Bukkit.getPluginManager().registerEvents(this, pl);}
	
	public static ItemStack wand = Others.getItem(Material.BLAZE_ROD, "&9&lSelector", "Use This to", "Select the areas", "of this awesome", "plugin!");
	public static HashMap<Player, String> selecting = new HashMap<Player, String>();
	public static HashMap<Player, ItemStack> item = new HashMap<Player, ItemStack>();
	public static HashMap<Player, Integer> slot = new HashMap<Player, Integer>();
	public static HashMap<Player, Location> p1 = new HashMap<Player, Location>();
	public static HashMap<Player, Location> p2 = new HashMap<Player, Location>();
	
	public static void give(Player p, String type) {
		item.put(p, p.getItemInHand());
		slot.put(p, p.getInventory().getHeldItemSlot());
		p.setItemInHand(wand);
		p.updateInventory();
		selecting.put(p, type);
		/*Mensaje Selector Activado*/
	}
	
	public static void cancel(Player p) {
		p.getInventory().setItem(slot.get(p), item.get(p));
		p.updateInventory();
		clear(p);
		/*Mensaje Selector Desactivado*/
	}
	
	private static void clear(Player p) {
		selecting.remove(p);
		item.remove(p);
		slot.remove(p);
		p1.remove(p);
		p2.remove(p);
	}
	
	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent e) {if (selecting.containsKey(e.getPlayer())) cancel(e.getPlayer());}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (selecting.containsKey(e.getPlayer()))
		if (e.getClickedBlock() != null) {
			Player p = e.getPlayer();
			CMessager.load(p);
			Location loc = e.getClickedBlock().getLocation();
			int n = 2;
			if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				n = 1;
				p1.put(p, loc);
			} else p2.put(p, loc);
			CMessager.msg("&7" + selecting.get(p) + ": &dPoint &b" + n + " &dSet! &8&l(&a" + loc.getBlockX() + "&7, &a" + loc.getBlockY() + "&7, &a" + loc.getBlockZ() + "&8&l)");
			if (p1.containsKey(p) && p2.containsKey(p)) {
				
			}
		}
	}
}
