package we.Heiden.gca.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import we.Heiden.gca.Functions.Garage;
import we.Heiden.gca.Weapons.WeaponUtils;
import we.Heiden.gca.Weapons.Weapons;
import we.Heiden.hs2.Messages.ActionBar;

public class Timer20T extends BukkitRunnable {

	public static HashMap<Player, String> actionBar = new HashMap<Player, String>();
	public static HashMap<Player, List<Object>> recharging = new HashMap<Player, List<Object>>();
	public static HashMap<Player, List<Object>> toRemove = new HashMap<Player, List<Object>>();
	public static int reload = 0;
	
	public void run() {
		if (!actionBar.isEmpty()) for (Player p : actionBar.keySet()) new ActionBar(p).msg(actionBar.get(p));
		if (!recharging.isEmpty()) try {
			for (Player p : recharging.keySet()) {
				int time = (int) recharging.get(p).get(1);
				time--;
				if (time > 0) {
					recharging.get(p).set(1, time);
					Weapons wep = (Weapons) recharging.get(p).get(0);
					String msg = "&bRecharging &d►► &a&l";
					boolean bol = true;
					double percentage = ((double) wep.rechargeDelay-time) / ((double) wep.rechargeDelay) * 100D;
					for (int n = 1; n <= 10; n++) {
						if (percentage < n*10 && bol) {
							msg += "&c&l";
							bol = false;
						}
						msg += "•";
					}
					msg += " &d◄◄";
					new ActionBar(p).msg(msg);
					p.setLevel(time);
				} else {
					Weapons wep = (Weapons) recharging.get(p).get(0);
					recharging.remove(p);
					WeaponUtils.recharge(p, wep);
					new ActionBar(p).msg("&a&lRecharged!");
					p.setLevel(0);
					ItemStack i = p.getItemInHand();
					ItemMeta im = i.getItemMeta();
					im.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&aCharge: &b" + wep.getCharge(p) + "&9/&c" + wep.shootCapacity)));
					i.setItemMeta(im);
					p.setItemInHand(i);
				}
			}
		} catch(Exception ex) { }
		
		if (reload != 0) {
			reload--;
			if (reload > 0) for (Player p : Bukkit.getOnlinePlayers()) new ActionBar(p).msg("&f&l•• &b&lReload &dincoming&9: &2" + reload + " &f&l••");
			else {
				Bukkit.reload();
				for (Player p : Bukkit.getOnlinePlayers()) new ActionBar(p).msg("&f&l•• &a&lReload Complete &f&l••");
			}
		}
		
		if (!toRemove.isEmpty())
		for (Player p : toRemove.keySet()) {
			int time = (int) toRemove.get(p).get(1);
			time--;
			if (time > 0) toRemove.get(p).set(1, time);
			else {
				Entity ent = (Entity) toRemove.get(p).get(0);
				toRemove.remove(p);
				Garage.remove(p, ent);
			}
		}
	}
}
