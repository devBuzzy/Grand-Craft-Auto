package we.Heiden.gca.Functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import we.Heiden.gca.Configs.PlayerConfig;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.hs2.SQL.Operations;

public enum Houses {

	FairviewDeluxe(3000, "&aFairview Deluxe", "Homes.Fairview"),
	Beachfront(4000, "&bBeach Front", "Homes.Beachfront"),
	StreetCondo(4000, "&dStreet Condo", "Homes.StreetCondo"),
	HomestyleDeluxe(5000, "&e&lHomeStyle Deluxe", "Homes.HomeStyleD"),
	HomestyleSuite(6000, "&9&lHomeStyle Suite", "Homes.HomeStyleS"),
	GroveSuite(7500, "&7&l&oGrove Suite", "Homes.GroveS", "backyard"),
	Mansion(12000, "&a►► &f&oMansion &a◄◄", "Homes.Mansion", "backyard"),
	MansionPremium(15000, "&b&o►► &6&oMansion Premium &b&o◄◄", "Homes.Mansion+", "backyard", "driveway"),
	Penthouse(25000, "&b&o&l►&a&o&l► &3&oPentHouse &a&o&l◄&b&o&l◄", "Homes.Penthouse");
	
	public int cost;
	public String path;
	public ItemStack item;
	public ItemStack sell;
	public String name;
	
	private Houses(int cost, String name, String path, String... extra) {
		this.cost = cost;
		this.path = path;
		this.name = name;
		ItemStack item = ItemUtils.getItem(Material.ACACIA_DOOR_ITEM, name);
		if (extra != null && extra.length > 0) {
			ItemMeta im = item.getItemMeta();
			List<String> lore = Functions.newList();
			for (String e : extra) lore.add(ChatColor.translateAlternateColorCodes('&', "&d&l►► &6&o" + e));
			im.setLore(lore);
			item.setItemMeta(im);
		}
		this.item = item.clone();
		item.setType(Material.SPRUCE_DOOR_ITEM);
		ItemMeta im = item.getItemMeta();
		List<String> ls = im.hasLore() ? im.getLore() : new ArrayList<String>();
		ls.addAll(Arrays.asList("", t("&d&oYou will obtain"), t("  &6&o" + (int) cost*75/100 + " coins &b(75%)")));
		im.setLore(ls);
		item.setItemMeta(im);
		this.sell = item;
	}
	
	private String t(String str) { return ChatColor.translateAlternateColorCodes('&', str); }

	public static Houses matchWarp(String warp) { for (Houses house : Houses.values()) if (house.toString().equalsIgnoreCase(warp)) return house; return null;
	}
	public static Houses matchItem(ItemStack item) { for (Houses house : Houses.values()) if (house.item.equals(item)) return house; return null; }
	public static Houses matchSell(ItemStack item) { for (Houses house : Houses.values()) if (house.sell.equals(item)) return house; return null; }

	public boolean add(Player p) {
		List<String> ls = PlayerConfig.get(p).contains("Houses") ? PlayerConfig.get().getStringList("Houses") : new ArrayList<String>();
		if (ls.contains(this.toString())) return false;
		else {
			ls.add(this.toString());
			Operations.setMoney(p.getUniqueId(), Operations.getMoney(p)-this.cost);
			PlayerConfig.get().set("Houses", ls);
			PlayerConfig.save();
			return true;
		}
	}
	
	public boolean remove(Player p) {
		List<String> ls = PlayerConfig.get(p).contains("Houses") ? PlayerConfig.get().getStringList("Houses") : new ArrayList<String>();
		if (!ls.contains(this.toString())) return false;
		else {
			ls.remove(this.toString());
			Operations.setMoney(p.getUniqueId(), Operations.getMoney(p)+this.cost*75/100);
			PlayerConfig.get().set("Houses", ls);
			PlayerConfig.save();
			return true;
		}
	}
}
