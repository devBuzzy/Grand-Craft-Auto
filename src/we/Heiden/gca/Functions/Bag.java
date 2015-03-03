package we.Heiden.gca.Functions;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Bag {

	public static HashMap<Player, Inventory> inventories = new HashMap<Player, Inventory>();
	
	public static void open(Player p) {
		p.closeInventory();
		p.openInventory(inventories.get(p));
	}
}
