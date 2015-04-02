package we.Heiden.gca.core;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;

import we.Heiden.gca.Events.InventoryClick;
import we.Heiden.gca.Events.PlayerInteract;
import we.Heiden.gca.Pets.Pet;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.gca.Weapons.WeaponHandler;
import we.Heiden.gca.Weapons.Weapons;
import we.Heiden.hs2.Messages.ActionBar;

public class Timer5T extends BukkitRunnable {

	public void run() {
		for (Player p : Bukkit.getOnlinePlayers()) p.updateInventory();
		if (!InventoryClick.pet.isEmpty())
			for (Player p : InventoryClick.pet.keySet()) {
				p.updateInventory();
				Pet pet = InventoryClick.pet.get(p);
				if (p.getLocation().distance(pet.getLocation()) > 50) {
					pet.killEntityNMS();
					InventoryClick.pet.remove(p);
					InventoryClick.petEnum.remove(p);
				}
			}
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
		if (!PlayerInteract.ballDelay.isEmpty())
			for (Player p : PlayerInteract.ballDelay.keySet()) {
				int time = PlayerInteract.ballDelay.get(p).get(0);
				time--;
				if (time > 0) {
					PlayerInteract.ballDelay.get(p).set(0, time);
					double percentage = ((double) 48-time) / ((double) 48) * 100D;
					String charge = "&a&l";
					boolean bol = true;
					for (int n = 1; n <= 50; n++) {
						if (percentage < n*2 && bol) {
							charge += "&c&l";
							bol = false;
						}
						charge += "|";
					}
					if (p.getInventory().getHeldItemSlot() == PlayerInteract.ballDelay.get(p).get(1)) new ActionBar(p).msg("&6Reloading &d►►" + charge + "&d◄◄");
				} else {
					new ActionBar(p).msg("&a&lReady!");
					PlayerInteract.ballDelay.remove(p);
				}
			}
	}
}
