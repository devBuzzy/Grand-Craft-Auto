package we.Heiden.gca.core;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import we.Heiden.gca.Misc.Others;

public class Timer5T extends BukkitRunnable {

	public Timer5T() {}
	
	public void run() {
		for (Player p : Others.hasItems)
			if (p.getInventory().getItem(Others.SlotMoney) != null)
				if (!p.getInventory().getItem(Others.SlotMoney).equals(Others.ItemMoney(p)))
					p.getInventory().setItem(Others.SlotMoney, Others.ItemMoney(p));
	}
}
