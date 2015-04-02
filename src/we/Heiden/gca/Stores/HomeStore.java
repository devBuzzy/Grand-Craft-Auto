package we.Heiden.gca.Stores;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import we.Heiden.gca.Configs.PlayerConfig;
import we.Heiden.gca.Functions.Houses;
import we.Heiden.gca.Messages.JsonMessage;
import we.Heiden.gca.Messages.JsonMessage.ClickAction;
import we.Heiden.gca.Messages.JsonMessage.HoverAction;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.Messages.Titles;
import we.Heiden.gca.Utils.Confirmable;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.hs2.Messages.Chat;
import we.Heiden.hs2.SQL.Operations;

public class HomeStore implements BasicStore, Confirmable {

	public String welcome() { return ChatColor.translateAlternateColorCodes('&', "&aAgent &d-> &eHello!"); }

	public static HashMap<Player, Houses> temp1 = new HashMap<Player, Houses>();
	public static HashMap<Player, Houses> temp2 = new HashMap<Player, Houses>();
	
	public void options(Player p) {
		JsonMessage.sendJson(p, 
				JsonMessage.newJson("&6&l============================="), 
				JsonMessage.newJson()
					.add("          ").build()
					.add("&a&lSell Houses")
						.hoverEvent(HoverAction.Show_Item, JsonMessage.newItem("&a&lFnS: Fast and Secure", "&bHere you can &lSell &bYour &lHouse"))
						.clickEvent(ClickAction.Run_Command, "/Store House Manage").build().build(), 
				JsonMessage.newJson("&e&l============================="), 
				JsonMessage.newJson()
					.add("              ").build()
					.add("&a&lBuy Houses")
						.hoverEvent(HoverAction.Show_Item, JsonMessage.newItem("&7You will need a", "&aGood &7place to &dsleep", "&7at &bCold &7nights"))
							.clickEvent(ClickAction.Run_Command, "/Store House Purchase").build().build(), 
				JsonMessage.newJson("&6&l============================="));
	}
	
	public static void Manage(Player p) {
		List<String> ls = null;
		if (PlayerConfig.get(p).contains("Houses")) ls = PlayerConfig.get().getStringList("Houses");
		if (ls == null || ls.size() < 1) new Titles(p, "&c").subtitle("&c&oYou don`t have any house").send();
		else {
			Inventory inv = Bukkit.createInventory(null, 9, ManageN());
			int n = -1;
			for (String h : ls) if (!h.isEmpty()) { n++; inv.setItem(n, Houses.valueOf(h).sell); }
			for (n++; n < 9; n++) inv.setItem(n, ItemUtils.ItemDefault());
			
			p.openInventory(inv);
		}
	}
	
	public static String ManageN() { return ChatColor.translateAlternateColorCodes('&', "&b&lHouse Selling"); }
	
	public static void Purchase(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9, PurchaseN());
		int n = -1;
		List<String> ls = null;
		if (PlayerConfig.get(p).contains("Houses")) ls = PlayerConfig.get().getStringList("Houses");
		for (Houses house : Houses.values()) {
			n++;
			if (ls != null && ls.contains(house.toString())) inv.setItem(n, Functions.addGlow(house.item));
			else inv.setItem(n, house.item);
		}
		
		p.openInventory(inv);
	}
	
	public static String PurchaseN() { return ChatColor.translateAlternateColorCodes('&', "&d&l&oBuy Houses"); }
	
	public static void confirm(Player p, Houses house) {
		int money = Operations.getMoney(p);
		if (PlayerConfig.get(p).contains("Houses") && PlayerConfig.get().getStringList("Houses").contains(house.toString())) new Chat(p).e("You already have that house");
		else if (money < house.cost) {
			p.closeInventory();
			Messager.load(p);
			Messager.e1("You can`t afford that");
		} else {
			temp1.put(p, house);
			ItemUtils.yes.put(p, new HomeStore());
			Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&a&lAre you sure?"));
			for (int n = 0; n < inv.getSize(); n++) inv.setItem(n, ItemUtils.ItemDefault());
			inv.setItem(12, ItemUtils.Yes());
			inv.setItem(14, ItemUtils.No());
			List<String> lore = Functions.newList();
			lore.add(house.name);
			if (house.item.getItemMeta().hasLore()) lore.addAll(house.item.getItemMeta().getLore());
			inv.setItem(4, ItemUtils.getItem(Material.DOUBLE_PLANT, "&6&lPrice: &e" + house.cost + " coins", lore));
			
			p.openInventory(inv);
		}
	}

	public static void confirm2(Player p, Houses house) {
		temp2.put(p, house);
		ItemUtils.yes.put(p, new HomeStore());
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&6&lSell this house?"));
		for (int n = 0; n < inv.getSize(); n++) inv.setItem(n, ItemUtils.ItemDefault());
		inv.setItem(12, ItemUtils.Yes());
		inv.setItem(14, ItemUtils.No());
		List<String> lore = Functions.newList();
		lore.add(house.name);
		if (house.item.getItemMeta().hasLore()) lore.addAll(house.item.getItemMeta().getLore());
		inv.setItem(4, ItemUtils.getItem(Material.DOUBLE_PLANT, "&eYou will obtain: &9" + (int) house.cost*75/100 + " coins", lore));

		p.openInventory(inv);
	}

	public void no(Player p) { if (temp1.containsKey(p)) { temp1.remove(p); Purchase(p); } else { Manage(p); temp2.remove(p); } }

	public void yes(Player p) {
		p.closeInventory();
		if (temp1.containsKey(p)) {
			if (temp1.get(p).add(p)) new Titles(p, "&a&lHouse Bought").subtitle("&c&o/home " + temp1.get(p).toString().toLowerCase() + " &b&oto visit it").send();
			temp1.remove(p);
		} else {
			if (temp2.get(p).remove(p)) new Titles(p, "&c").subtitle("&c&oHouse Sold &b◄► &aYou had earned &6&o" + (int) temp2.get(p).cost*75/100 + " coins").send();
			temp2.remove(p);
		}
	}
}
