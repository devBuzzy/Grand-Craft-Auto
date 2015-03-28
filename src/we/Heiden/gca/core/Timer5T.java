package we.Heiden.gca.core;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;

import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.gca.Weapons.WeaponHandler;
import we.Heiden.gca.Weapons.Weapons;

public class Timer5T extends BukkitRunnable {

	public void run() {
		if (!ItemUtils.hasItems.isEmpty()) for (Player p : ItemUtils.hasItems)
			if (p.getInventory().getItem(ItemUtils.SlotMoney) != null)
				if (!p.getInventory().getItem(ItemUtils.SlotMoney).equals(ItemUtils.ItemMoney(p)))
					p.getInventory().setItem(ItemUtils.SlotMoney, ItemUtils.ItemMoney(p));
		if (!WeaponHandler.bullet.isEmpty()) 
			for (Projectile proj : WeaponHandler.bullet.keySet()) {
				if (((Location)WeaponHandler.bullet.get(proj).get(0)).distance(proj.getLocation()) > ((Weapons)WeaponHandler.bullet.get(proj).get(1)).maxDistance) {
					WeaponHandler.bullet.remove(proj);
					WeaponHandler.toRemove.remove(proj);
					proj.remove();
				}
			}
	}
}
