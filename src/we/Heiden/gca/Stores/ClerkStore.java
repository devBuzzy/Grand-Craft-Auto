package we.Heiden.gca.Stores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import we.Heiden.gca.Functions.Bag;
import we.Heiden.gca.Functions.Food;
import we.Heiden.gca.Messages.JsonMessage;
import we.Heiden.gca.Messages.JsonMessage.ClickAction;
import we.Heiden.gca.Messages.JsonMessage.HoverAction;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.Messages.Titles;
import we.Heiden.gca.Utils.Confirmable;
import we.Heiden.gca.Utils.Displayable;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.gca.Weapons.Weapons;
import we.Heiden.hs2.SQL.Operations;

public class ClerkStore implements BasicStore, Confirmable, Displayable {

	public String welcome() {
		return ChatColor.translateAlternateColorCodes('&',
				"&a&lClerk &d-> &b25% Off! &2(just kidding)");
	}

	public static HashMap<Player, List<Object>> temp01 = new HashMap<Player, List<Object>>();

	public void options(Player p) {
		JsonMessage.sendJson(
				p,
				JsonMessage.newJson("&6&l============================="),
				JsonMessage.newJson().add("          &9&l*").build()
						.add("&a&lPurchase")
						.hoverEvent(HoverAction.Show_Text, "&e&oAmazing Shop")
						.clickEvent(ClickAction.Run_Command, "/Store Clerk")
						.build().build(),
				JsonMessage.newJson("&e&l============================="),
				JsonMessage
						.newJson()
						.add("          &4&l*")
						.build()
						.add("&c&oRobbery Mode")
						.hoverEvent(HoverAction.Show_Text, "&cRob the store")
						.clickEvent(ClickAction.Run_Command,
								"/Store Robbery Clerk").build().build(),
				JsonMessage.newJson("&6&l============================="));
	}

	public void display(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9, displayN);
		for (int n = 0; n < inv.getSize(); n++)
			inv.setItem(n, ItemUtils.ItemDefault());
		inv.setItem(2, ItemUtils.Food());
		inv.setItem(3, ItemUtils.Weapons());
		inv.setItem(5, ItemUtils.JetPack());
		inv.setItem(6, ItemUtils.PaintGun());

		p.openInventory(inv);
	}

	public static String displayN = ChatColor.translateAlternateColorCodes('&',
			"&a&lClerk Shop");

	public static void weapons(Player p) {
		Inventory inv = Bukkit.createInventory(null, 18, weaponsN);
		for (int n = 0; n < inv.getSize(); n++)
			inv.setItem(n, ItemUtils.ItemDefault());
		int n = -1;
		for (Weapons wep : Weapons.values()) {
			n++;
			if (n == 4)
				n++;
			inv.setItem(n, wep.item);
			if (wep.isFireWeapon) {
				ItemStack bullet = wep.bullet.clone();
				bullet.setAmount(wep.shootCapacity);
				inv.setItem(n + 9, bullet);
			}
		}

		p.openInventory(inv);
	}

	public static String weaponsN = ChatColor.translateAlternateColorCodes('&',
			"&c&lWeapons &bStore");

	public static void food(Player p) {
		Inventory inv = Bukkit.createInventory(null, 18, foodN);
		for (int n = 0; n < inv.getSize(); n++)
			inv.setItem(n, ItemUtils.ItemDefault());
		int n = -1;
		for (Food food : Food.values()) {
			n++;
			inv.setItem(n, food.item);
		}

		p.openInventory(inv);
	}

	public static String foodN = ChatColor.translateAlternateColorCodes('&',
			"&a&lFood n Heal");

	public static void confirm(Player p, ItemStack item, int price) {
		int money = Operations.getMoney(p);
		if (money < price) {
			p.closeInventory();
			Messager.load(p);
			Messager.e1("You can`t afford that");
			return;
		}
		List<Object> lo = new ArrayList<Object>();
		lo.addAll(Arrays.asList(item, price));
		temp01.put(p, lo);
		ItemUtils.yes.put(p, new ClerkStore());
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor
				.translateAlternateColorCodes('&', "&a&lAre you sure?"));
		for (int n = 0; n < inv.getSize(); n++)
			inv.setItem(n, ItemUtils.ItemDefault());
		inv.setItem(12, ItemUtils.Yes());
		inv.setItem(14, ItemUtils.No());
		inv.setItem(
				4,
				ItemUtils.getItem(Material.DOUBLE_PLANT, "&6&lPrice: &e"
						+ price + " coins"));

		p.openInventory(inv);
	}

	public void no(Player p) {
		display(p);
	}

	public void yes(Player p) {
		Bag.inventories.get(p).addItem((ItemStack) temp01.get(p).get(0));
		Operations.setMoney(p.getUniqueId(), Operations.getMoney(p)
				- (int) temp01.get(p).get(1));
		temp01.remove(p);
		p.closeInventory();
		new Titles(p, "&a&lItem Bought").send();
	}
}
