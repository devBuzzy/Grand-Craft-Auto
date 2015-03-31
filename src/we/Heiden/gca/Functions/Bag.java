package we.Heiden.gca.Functions;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import we.Heiden.gca.Configs.PlayerConfig;
import we.Heiden.gca.Events.InventoryClick;
import we.Heiden.gca.Pets.Pets;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.gca.Utils.ItemUtils;

public class Bag {

	public static HashMap<Player, Inventory> inventories = new HashMap<Player, Inventory>();
	
	public static void open(Player p) {
		
		if (PlayerConfig.get(p).contains("Pets")) {
			List<String> ls = PlayerConfig.get().getStringList("Pets");
			if (ls.contains(Pets.Wolf.getType())) 
				if (InventoryClick.petEnum.containsKey(p) && InventoryClick.petEnum.get(p).equals(Pets.Wolf)) 
					p.getInventory().setItem(15, Functions.addGlow(ItemUtils.Wolf().clone()));
				else p.getInventory().setItem(15, ItemUtils.Wolf());
			if (ls.contains(Pets.Cat.getType())) 
				if (InventoryClick.petEnum.containsKey(p) && InventoryClick.petEnum.get(p).equals(Pets.Cat)) 
					p.getInventory().setItem(16, Functions.addGlow(ItemUtils.Cat().clone()));
				else p.getInventory().setItem(16, ItemUtils.Cat());
			p.updateInventory();
		}
		
		p.closeInventory();
		p.openInventory(inventories.get(p));
	}
}
