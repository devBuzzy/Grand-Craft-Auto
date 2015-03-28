package we.Heiden.gca.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import we.Heiden.gca.Functions.Bag;
import we.Heiden.gca.Functions.CarsEnum;
import we.Heiden.gca.Functions.Wand;
import we.Heiden.hs2.SQL.Operations;

public class ItemUtils {
	
	public static HashMap<Player, Confirmable> yes = new HashMap<Player, Confirmable>();
	
	private static HashMap<String, ItemStack> ritems = new HashMap<String, ItemStack>();
	
	public static void setup() {
		ritems.put("ItemJobs", getItem(Material.NETHER_STAR, "&b&lJobs Selector", "Here you can &9&owin", "   some &9&omoney&5&o!", " &5Hint: &9&oIt`s good to have money"));
		ritems.put("ItemBag", getItem(Material.CHEST, "&a&lBackPack", "Store your items to", "   carry them with you", " &7Nope, you can`t use", "   &7your inventory"));
		ritems.put("ItemSettings", getItem(Material.COMPASS, "&f&lSettings", "Do you have lag?", "or just you don`t like", "   how some things look?", 
				" I`ve a &fSolution &5&ofor &fYou", "", "&bJust click me"));
		ritems.put("ItemDefault", getItem(Material.STAINED_GLASS_PANE, (short) 7, "&a"));
		ritems.put("GearUp", getItem(Material.BANNER, (short) 10, "&a&lGear Up"));
		ritems.put("GearDown", getItem(Material.BANNER, (short) 1, "&c&lGear Down"));
		ritems.put("GearMax", getItem(Material.BANNER, "&9&lMax Level Reached"));
		ritems.put("Garage", getItem(Material.ACACIA_FENCE_GATE, "&7&lGarage", "Click your car", "with this to", "take it to the", "Garage!"));

		ritems.put("W_M1911", getItem(Material.INK_SACK, (short) 1, "&9&lM1911", "&aCharge: &b0&9/&c20"));
		ritems.put("W_AK47", getItem(Material.INK_SACK, (short) 10, "&9&lAK47", "&aCharge: &b0&9/&c30"));
		ritems.put("W_FAMAS", getItem(Material.INK_SACK, (short) 12, "&9&lFAMAS", "&aCharge: &b0&9/&c30"));
		ritems.put("W_AUG", getItem(Material.INK_SACK, (short) 3, "&9&lAUG", "&aCharge: &b0&9/&c30"));
		ritems.put("W_KNIFE", getItem(Material.INK_SACK, (short) 8, "&7&l&oKnife"));
		ritems.put("W_GRENADE", getItem(Material.SNOW_BALL, "&c&lGrenade"));
		ritems.put("W_SPAS_12", getItem(Material.INK_SACK, "&9&lSPAS12", "&aCharge: &b0&9/&c5"));
		ritems.put("W_BARRET_50", getItem(Material.INK_SACK, (short) 14, "&9&lBARRETT-50-CAL", "&aCharge: &b0&9/&c10"));

		ritems.put("Bullet", getItem(Material.ARROW, "&a&lBullet"));
		
		ritems.put("Food", getItem(Material.COOKED_CHICKEN, "&a&lFood n Heal"));
		ritems.put("Weapons", getItem(Material.INK_SACK, (short) 1, "&c&lWeapons"));
		
		ritems.put("Yes", getItem(Material.STAINED_CLAY, (short) 5, "&a&lConfirm", "&2&oConfirm whatever you", "&2&oare confirmating"));
		ritems.put("No", getItem(Material.STAINED_CLAY, (short) 14, "&c&lDeny", "&4&oIt`s like confirming,", "&4&obut no."));
		ritems.put("CarInfo", getItem(Material.PAPER, "&5&lCar Info"));
		ritems.put("CarAuth", getItem(Material.STAINED_CLAY, (short) 4, "&e&lAuth", "&7With Delay", "", "Your car will be added", "to our Auth List, if", "anyone buys it, you",
				"will get the money"));
		ritems.put("CarSell", getItem(Material.STAINED_CLAY, (short) 1, "&6&lSell", "&7Instant", "", "If you &lsell", "your car, you", "will obtain half", "of it`s cost back"));
		
		weapons.addAll(Arrays.asList(W_AK47(), W_AUG(), W_BARRET_50(), W_FAMAS(), W_GRENADE(), W_KNIFE(), W_M1911(), W_SPAS_12()));
	}

	public static HashMap<ItemStack, CarsEnum> cars = new HashMap<ItemStack, CarsEnum>();
	public static List<ItemStack> keys = new ArrayList<ItemStack>();
	public static List<ItemStack> items = new ArrayList<ItemStack>();
	public static List<ItemStack> weapons = new ArrayList<ItemStack>();
	
	public static final int SlotPistol = 0;
	public static final int SlotSettings = 1;
	public static final int SlotJobs = 4;
	public static final int SlotBag = 7;
	public static final int SlotMoney = 8;

	public static void setCars() {for (CarsEnum e : CarsEnum.values()) cars.put(e.getItem(), e);}
	public static void setKeys() {for (CarsEnum e : CarsEnum.values()) keys.add(e.getKey());}
	public static void setItems() {
		items.addAll(Arrays.asList(ItemJobs(), ItemBag(), ItemSettings(), ItemDefault(), GearMax(), Garage(), Wand.wand, Yes(), No(), CarInfo()));
		items.addAll(cars.keySet());
		items.addAll(keys);
		for (int n = 1; n <= 10; n++) {
			ItemStack i = GearUp();
			ItemStack i2 = GearDown();
			i.setAmount(n);
			i2.setAmount(n);
			items.addAll(Arrays.asList(i, i2));
		}
	}
	
	public static List<ItemStack> getItem(Player p) {
		List<ItemStack> is = items;
		is.add(ItemMoney(p));
		return is;
	}
	
	public static List<Player> hasItems = new ArrayList<Player>();
	
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
	
	public static void items(Player p) {
		Functions.clear(p);
		PlayerInventory inv = p.getInventory();
		int s1 = SlotPistol;
		int s2 = SlotBag;
		int s3 = SlotJobs;
		int s4 = SlotMoney;
		int s5 = SlotSettings;

		if (!inv.contains(W_M1911())) if (inv.getItem(s1) == null) inv.setItem(s1, W_M1911()); else inv.addItem(W_M1911());
		if (!inv.contains(ItemBag())) if (inv.getItem(s2) == null) inv.setItem(s2, ItemBag()); else inv.addItem(ItemBag());
		if (!inv.contains(ItemJobs())) if (inv.getItem(s3) == null) inv.setItem(s3, ItemJobs()); else inv.addItem(ItemJobs());
		if (!inv.contains(ItemMoney(p))) if (inv.getItem(s4) == null) inv.setItem(s4, ItemMoney(p)); else inv.addItem(ItemMoney(p));
		if (!inv.contains(ItemSettings())) if (inv.getItem(s5) == null) inv.setItem(s5, ItemSettings()); else inv.addItem(ItemSettings());
		
		for (int n = 0; n < inv.getSize(); n++) if (inv.getItem(n) == null) inv.setItem(n, ItemDefault());
		hasItems.add(p);
		if (!Bag.inventories.containsKey(p)) Bag.inventories.put(p, Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', "&a&lPersonal Backpack")));
	}
	public static void items() {for (Player p : Bukkit.getOnlinePlayers()) items(p);}

	/** <li>Gun shot particles
	 * <li>Gun Recharge Effect
	 * <li>Titles
	 * <li>Hotbar
	 * <li>Gun Zoom
	 * <li>Pet visibility
	 * <li>Extra detailed things [particles etc]*/
	public static ItemStack ItemSettings() { return ritems.get("ItemSettings"); }
	public static ItemStack ItemMoney(Player p) {
		int money = Operations.getMoney(p);
		return getItem(Material.WHEAT, "&6&lMoney: &d" + money, "Use this to keep ", "track of your money");
	}
	public static ItemStack ItemJobs() { return ritems.get("ItemJobs"); }
	public static ItemStack ItemBag() { return ritems.get("ItemBag"); }
	public static ItemStack ItemDefault() { return ritems.get("ItemDefault"); }
	
	public static ItemStack GearUp() { return ritems.get("GearUp"); }
	public static ItemStack GearDown() { return ritems.get("GearDown"); }
	public static ItemStack GearMax() { return ritems.get("GearMax"); }
	public static ItemStack Garage() { return ritems.get("Garage"); }
	

	public static ItemStack W_M1911() { return ritems.get("W_M1911"); }
	public static ItemStack W_AK47() { return ritems.get("W_AK47"); }
	public static ItemStack W_FAMAS() { return ritems.get("W_FAMAS"); }
	public static ItemStack W_AUG() { return ritems.get("W_AUG"); }
	public static ItemStack W_KNIFE() { return ritems.get("W_KNIFE"); }
	public static ItemStack W_GRENADE() { return ritems.get("W_GRENADE"); }
	public static ItemStack W_SPAS_12() { return ritems.get("W_SPAS_12"); }
	public static ItemStack W_BARRET_50() { return ritems.get("W_BARRET_50"); }
	public static ItemStack Bullet() { return ritems.get("Bullet"); }
	

	public static ItemStack Yes() { return ritems.get("Yes"); }
	public static ItemStack No() { return ritems.get("No"); }

	public static ItemStack Weapons() { return ritems.get("Weapons"); }
	public static ItemStack Food() { return ritems.get("Food"); }


	public static ItemStack CarInfo() { return ritems.get("CarInfo"); }
	public static ItemStack CarAuth() { return ritems.get("CarAuth"); }
	public static ItemStack CarSell() { return ritems.get("CarSell"); }
}
