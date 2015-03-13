package we.Heiden.gca.core;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import we.Heiden.gca.Utils.ItemUtils;

public class Timer5T extends BukkitRunnable {

	public Timer5T() {}
	
	public void run() {
		for (Player p : ItemUtils.hasItems)
			if (p.getInventory().getItem(ItemUtils.SlotMoney) != null)
				if (!p.getInventory().getItem(ItemUtils.SlotMoney).equals(ItemUtils.ItemMoney(p)))
					p.getInventory().setItem(ItemUtils.SlotMoney, ItemUtils.ItemMoney(p));
	}
}
