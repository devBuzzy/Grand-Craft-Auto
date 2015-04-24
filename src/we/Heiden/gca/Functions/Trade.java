package we.Heiden.gca.Functions;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import we.Heiden.gca.Commands.TradeCommand;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.hs2.Messages.Chat;

public class Trade {

	public static ItemStack submit = ItemUtils.getItem(Material.WOOL,
			(short) 5, "&a&lSubmit");
	public static ItemStack submited = ItemUtils.getItem(Material.WOOL,
			(short) 5, "&a&lSubmited", "this player has", "accepted the trade");
	public static ItemStack unsubmited = ItemUtils.getItem(Material.WOOL,
			(short) 14, "&c&oUnsubmited", "waiting confirmation");
	public static HashMap<Player, Integer> update = new HashMap<Player, Integer>();

	public static void display(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, displayN());
		for (int n = 0; n < 6; n++)
			inv.setItem(n * 9 + 4, ItemUtils.ItemDefault());
		inv.setItem(3, submit);
		inv.setItem(5, unsubmited);

		Inventory bag = Bag.inventories.get(p);
		for (int n = 0; n < 27; n++)
			p.getInventory().setItem(n + 9, bag.getItem(n));

		p.updateInventory();
		p.openInventory(inv);
	}

	public static String displayN() {
		return ChatColor.translateAlternateColorCodes('&',
				"&aTrad &2o &amatic &62000");
	}

	public static void submit(Player p) {
		if (!p.getOpenInventory().getItem(5).equals(submited)) {
			Player other = TradeCommand.pending.get(p);
			p.getOpenInventory().setItem(3,
					Functions.addGlow(p.getOpenInventory().getItem(3).clone()));
			p.updateInventory();
			other.getOpenInventory().setItem(5, submited);
			other.updateInventory();
		} else
			finish(p);
	}

	public static void cancel(Player p) {
		new Chat(remove(p, false)).e(p.getName() + " has canceled the trade");
		new Chat(p).msg("&a&lTrade Canceled");
	}

	public static void finish(Player p) {
		new Chat(remove(p, true)).msg("&a&lTrade Finished");
		new Chat(p).msg("&a&lTrade Finished");
	}

	private static Player remove(Player p, boolean trade) {
		Player other = TradeCommand.pending.get(p);
		TradeCommand.denied.remove(p);
		TradeCommand.denied.remove(other);
		TradeCommand.pending.remove(p);
		TradeCommand.pending.remove(other);
		TradeCommand.cooldown.put(p, 15);
		TradeCommand.cooldown.put(other, 15);
		Integer[] slots = { 0, 1, 2, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29,
				30, 36, 37, 38, 39, 45, 46, 47, 48 };
		for (int slot : slots) {
			ItemStack i1 = p.getOpenInventory().getItem(slot);
			ItemStack i2 = other.getOpenInventory().getItem(slot);
			if (i1 != null)
				if (trade)
					other.getInventory().addItem(i1);
				else
					p.getInventory().addItem(i1);
			if (i2 != null)
				if (trade)
					p.getInventory().addItem(i2);
				else
					other.getInventory().addItem(i2);
		}
		for (int n = 0; n < 27; n++) {
			Bag.inventories.get(p).setItem(n, p.getInventory().getItem(n + 9));
			Bag.inventories.get(other).setItem(n,
					other.getInventory().getItem(n + 9));
			p.getInventory().setItem(n + 9, ItemUtils.ItemDefault());
			other.getInventory().setItem(n + 9, ItemUtils.ItemDefault());
		}
		if (trade) {
			p.updateInventory();
			p.closeInventory();
		}
		other.updateInventory();
		other.closeInventory();
		return other;
	}
}
