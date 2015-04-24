package we.Heiden.gca.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;

import we.Heiden.gca.Events.InventoryClick;
import we.Heiden.gca.Events.PlayerInteract;
import we.Heiden.gca.Functions.CellPhone;
import we.Heiden.gca.Pets.Pet;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.gca.Weapons.WeaponHandler;
import we.Heiden.gca.Weapons.Weapons;
import we.Heiden.hs2.Messages.ActionBar;
import we.Heiden.hs2.Messages.Chat;

public class Timer5T extends BukkitRunnable {

	public static List<Player> toUpdate = new ArrayList<Player>();
	private Integer[] rings = {38, 37, 34, 33, 30, 29, 26, 25, 22, 21, 18, 17, 14, 13, 10, 9, 6, 5, 2, 1};

	public void run() {
		if (!CellPhone.calling2.isEmpty())
			for (Player p : CellPhone.calling2.keySet()) {
				int time = CellPhone.calling2.get(p);
				time--;
				for (int ring : rings)
					if (ring == time) {
						p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
						CellPhone.calling.get(p).playSound(CellPhone.calling.get(p).getLocation(), Sound.ORB_PICKUP, 1, 1);
						break;
					}
				if (time > 0) CellPhone.calling2.put(p, time);
				else {
					CellPhone.calling2.remove(p);
					CellPhone.calling.remove(CellPhone.calling.get(p));
					CellPhone.cancelDisplay.remove(CellPhone.calling.get(p));
					CellPhone.calling.remove(p);
					if (Timer20T.actionBar.containsKey(p) && Timer20T.actionBar.get(p).equals("&b&oCalling")) Timer20T.actionBar.remove(p);
					new Chat(p).e("Not Avaible, Try Again Later");
				}
			}
		if (!toUpdate.isEmpty())
			try {
				for (Player p : toUpdate) {
					p.updateInventory();
					toUpdate.remove(p);
				}
			} catch(Exception ex) { }

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
				if (p.getInventory().getItem(ItemUtils.SlotMoney).getType().equals(Material.WHEAT) && 
						!p.getInventory().getItem(ItemUtils.SlotMoney).equals(ItemUtils.ItemMoney(p)))
					p.getInventory().setItem(ItemUtils.SlotMoney, ItemUtils.ItemMoney(p));
		if (!WeaponHandler.bullet.isEmpty()) 
			try {
				for (Projectile proj : WeaponHandler.bullet.keySet()) {
					if (((Location)WeaponHandler.bullet.get(proj).get(0)).distance(proj.getLocation()) > ((Weapons)WeaponHandler.bullet.get(proj).get(1)).maxDistance) {
						WeaponHandler.bullet.remove(proj);
						WeaponHandler.toRemove.remove(proj);
						proj.remove();
					}
				}
			} catch(Exception ex) { }
		if (!PlayerInteract.ballDelay.isEmpty())
			try {
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
			} catch(Exception ex) { }
	}
}
