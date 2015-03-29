package we.Heiden.gca.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Functions.Bag;
import we.Heiden.gca.Utils.ItemUtils;

/**
 * ********************************************* <p>
 * <b>This has been made by <i>Heiden Team</b>
 * <ul>
 * <li>Don't claim this class as your own
 * <li>Don't remove this disclaimer
 * </ul>
 *         <b>All rights reserved <p>
 *           Heiden Team 2015 <p></b>
 * ********************************************* 
 **/
public class PlayerDropItem implements Listener {
	
	public PlayerDropItem(Plugin pl) {Bukkit.getPluginManager().registerEvents(this, pl);}
	
	@EventHandler
	public void onPlayerItemDrop(PlayerDropItemEvent e) {
		ItemStack c = e.getItemDrop().getItemStack();
		if (e.getItemDrop() != null) if (ItemUtils.getItem(e.getPlayer()).contains(c) || ItemUtils.weapons.contains(c) ||
				(c.getType() == ItemUtils.Bullet().getType() && c.getItemMeta().getDisplayName().equals(ItemUtils.Bullet().getItemMeta().getDisplayName()))) {
			Player p = e.getPlayer();
			if (p.getOpenInventory() == null || !p.getOpenInventory().equals(Bag.inventories.get(p)) || !Bag.inventories.get(p).contains(c)) e.setCancelled(true);
		}
	}
}
