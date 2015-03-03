package we.Heiden.gca.Misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import we.Heiden.gca.Configs.PlayerConfig;
import we.Heiden.gca.Functions.Bag;

import com.aaomidi.hitslain.coins.engine.HSCoinsAPI;

public class Others {

	public static final int SlotPistol = 0;
	public static final int SlotSettings = 1;
	public static final int SlotJobs = 4;
	public static final int SlotBag = 7;
	public static final int SlotMoney = 8;
	
	public static List<Player> hasItems = new ArrayList<Player>();
	
	public static boolean isInt(Player p, String s, String type, boolean zero, boolean negative, boolean msg) {
		try {
			int i = Integer.parseInt(s);
			if (i == 0 && zero) {
				if (msg) p.sendMessage("Error: " + type + " can`t be Zero!");
				return false;
			} else if (s.startsWith("-") && negative) {
				if (msg) p.sendMessage("Error: " + type + " can`t be Zero!");
				return false;
			}
		} catch(Exception ex) {
			if (msg) p.sendMessage("Error: " + type + " can`t be Zero!");
			return false;
		}
		return true;
	}

	public static boolean isInt(String s) {return isInt(null, s, null, true, true, false);}
	public static boolean isInt(String s, boolean zero, boolean negative) {return isInt(null, s, null, zero, negative, false);}
	
	
	public static ItemStack getItem(Material mat, int amount, short data, String name, String... lore) {
		ItemStack i = new ItemStack(mat, amount, data);
		ItemMeta im = i.getItemMeta();
		if (name != null) im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		if (lore != null) {
			List<String> lores = new ArrayList<String>();
			for (String l : lore) lores.add(ChatColor.translateAlternateColorCodes('&', l));
			im.setLore(lores);
		}
		i.setItemMeta(im);
		return i;
	}
	public static ItemStack getItem(Material mat, String name, String... lore) {return getItem(mat, 1, (short) 0, name, lore);}
	public static ItemStack getItem(Material mat, short data, String name, String... lore) {return getItem(mat, 1, data, name, lore);}
	public static ItemStack getItem(Material mat, int amount, short data, String name, List<String> lore) {
		String s = "";
		for (String l : lore) s += l + "gcaspltter";
		return getItem(mat, amount, data, name, s.split("gcaspltter"));
	}
	public static ItemStack getItem(Material mat, short data, String name, List<String> lore) {return getItem(mat, 1, data, name ,lore);}
	public static ItemStack getItem(Material mat, String name, List<String> lore) {return getItem(mat, 1, (short) 0, name ,lore);}
	
	
	public static List<ItemStack> ItemList(Player p) {return Arrays.asList(ItemMoney(p), ItemJobs(), ItemBag(), ItemSettings(), Pistol01(), ItemDefault());}
	
	
	public static void items(Player p) {
		new PlayerConfig(p);
		clear(p);
		PlayerInventory inv = p.getInventory();
		int s1 = SlotPistol;
		int s2 = SlotBag;
		int s3 = SlotJobs;
		int s4 = SlotMoney;
		int s5 = SlotSettings;

		if (!inv.contains(Pistol01())) if (inv.getItem(s1) == null) inv.setItem(s1, Pistol01()); else inv.addItem(Pistol01());
		if (!inv.contains(ItemBag())) if (inv.getItem(s2) == null) inv.setItem(s2, ItemBag()); else inv.addItem(ItemBag());
		if (!inv.contains(ItemJobs())) if (inv.getItem(s3) == null) inv.setItem(s3, ItemJobs()); else inv.addItem(ItemJobs());
		if (!inv.contains(ItemMoney(p))) if (inv.getItem(s4) == null) inv.setItem(s4, ItemMoney(p)); else inv.addItem(ItemMoney(p));
		if (!inv.contains(ItemSettings())) if (inv.getItem(s5) == null) inv.setItem(s5, ItemSettings()); else inv.addItem(ItemSettings());
		
		for (int n = 0; n < inv.getSize(); n++) if (inv.getItem(n) == null) inv.setItem(n, ItemDefault());
		hasItems.add(p);
		if (!Bag.inventories.containsKey(p)) Bag.inventories.put(p, Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', "&a&lPersonal Backpack")));
	}
	public static void items() {for (Player p : Bukkit.getOnlinePlayers()) items(p);}
	public static void save() {for (Player p : Bukkit.getOnlinePlayers()) save(p);}
	public static void load() {for (Player p : Bukkit.getOnlinePlayers()) load(p);}
	
	public static void save(Player p) {
		PlayerConfig.load(p);
		Inventory inv = Bag.inventories.get(p);
		int n = 0;
		if (inv != null) for (ItemStack i : inv) {
			PlayerConfig.get().set("Temp.Bag." + n, i);
			n++;
		}
		if (hasItems.contains(p)) PlayerConfig.get().set("Temp.hasItems", "true");
		PlayerConfig.save();
	}
	
	public static void load(Player p) {
		PlayerConfig.load(p);
		if (PlayerConfig.get().contains("Temp.hasItems")) hasItems.add(p);
		if (PlayerConfig.get().contains("Temp.Bag")) {
			Inventory inv = Bag.inventories.get(p);
			for (String s : PlayerConfig.get().getConfigurationSection("Temp.Bag").getKeys(false)) {
				int slot = Integer.parseInt(s);
				inv.setItem(slot, PlayerConfig.get().getItemStack("Temp.Bag." + s));
			}
			Bag.inventories.put(p, inv);
		}
	}
	
	public static void clear(Player p) {
		p.getInventory().clear();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
	}
	
	public static ItemStack ItemMoney(Player p) {
		int money = HSCoinsAPI.getPlayer(p).getBalance();
		return getItem(Material.WHEAT, "&6&lMoney: &d" + money, "Use this to keep ", "track of your money");
	}
	public static ItemStack ItemJobs() {return getItem(Material.NETHER_STAR, "&b&lJobs Selector", "Here you can &9&owin", 
			"   some &9&omoney&5&o!", " &5Hint: &9&oIt`s good to have money");}
	public static ItemStack ItemBag() {return getItem(Material.CHEST, "&a&lBackPack", "Store your items to", 
			"   carry them with you", " &7Nope, you can`t use", "   &7your inventory");}
	public static ItemStack ItemSettings() {return getItem(Material.COMPASS, "&f&lSettings", "Do you have lag?", "or just you don`t like", "   how some things look?", 
			" I`ve a &fSolution &5&ofor &fYou", "", "&bJust click me");}
	/*Gun shot particles, Gun Recharge Effect, Titles, Hotbar, Gun Zoom, Pet visibility, Extra detailed things [particles etc]*/
	public static ItemStack ItemDefault() {return getItem(Material.STAINED_GLASS_PANE, (short) 7, "&a");}
	
	
	public static ItemStack Pistol01() {return getItem(Material.INK_SACK, (short) 1, "&8&lM1911 &7&o(colt)", "Hey man, you`re on Los Angeles", "I think you will need this...");}
	
	
}
