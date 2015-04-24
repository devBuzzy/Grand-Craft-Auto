package we.Heiden.gca.core;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import we.Heiden.gca.Commands.TradeCommand;
import we.Heiden.gca.Functions.Trade;
import we.Heiden.gca.Weapons.WeaponHandler;
import we.Heiden.gca.Weapons.Weapons;
import we.Heiden.hs2.Messages.ActionBar;

public class Timer1T extends BukkitRunnable {

	public void run() {
		if (!WeaponHandler.delay.isEmpty())
			try {
				for (Player p : WeaponHandler.delay.keySet()) {
					for (Weapons wep : WeaponHandler.delay.get(p).keySet()) {
						int time = WeaponHandler.delay.get(p).get(wep);
						time--;
						if (time > 0) {
							WeaponHandler.delay.get(p).put(wep, time);
							double percentage = ((double) wep.shootDelay - time)
									/ ((double) wep.shootDelay) * 100D;
							String charge = "&a&l";
							boolean bol = true;
							for (int n = 1; n <= 20; n++) {
								if (percentage < n * 5 && bol) {
									charge += "&c&l";
									bol = false;
								}
								charge += "•";
							}
							new ActionBar(p).msg("&bDelay &d►►" + charge
									+ "&d◄◄");
						} else
							WeaponHandler.delay.get(p).remove(wep);
					}
					if (WeaponHandler.delay.get(p).isEmpty()) {
						WeaponHandler.delay.remove(p);
						new ActionBar(p).msg("&6&lCharged");
					}
				}
			} catch (Exception ex) {
			}

		if (!WeaponHandler.toRemove.isEmpty()) {
			try {
				for (Projectile proj : WeaponHandler.toRemove) {
					WeaponHandler.bullet.remove(proj);
					WeaponHandler.toRemove.remove(proj);
					proj.remove();
				}
			} catch(Exception ex) { }
		}

		if (!Trade.update.isEmpty())
			for (Player p : Trade.update.keySet()) {
				int slot = Trade.update.get(p);
				Player other = TradeCommand.pending.get(p);
				ItemStack item = p.getOpenInventory().getItem(slot);
				if (slot == 0)
					slot = 3;
				other.getOpenInventory().setItem(slot + 5, item);
				other.updateInventory();
				Trade.update.remove(p);
			}
	}
}
