package we.Heiden.gca.Functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import we.Heiden.gca.Configs.PlayerConfig;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.hs2.SQL.Operations;

public enum CarsEnum {

	ADVENTURE(1, 1, "Classic", 750, "Adventure", "&2"),
	CRUISER(1, 2, "Sport", 1000, "Cruiser", "&a"),
	DERBY(1, 3, "Heavy Duty", 1500, "Derby", "&9"),
	ROYCE(1, 4, "Classy", 1750, "Royce", "&f&o"),
	ESCALADE_SPORT(1, 7, "Sport", 2500, "Escalade Sport", "&d"),
	CYPHER(1, 8, "Sport", 3000, "Cypher", "&b&l"),
	AVENGER_GT(1, 10, "Super", 5000, "Avenger GT", "&6&l");

	public static void setup() {
		items.put(CarsEnum.ADVENTURE, ItemUtils.getItem(Material.MINECART, "&b&oAdventure", "&6Type: &7Classic", "&6Speed: &71", "Right click", "to use it"));
		items.put(CarsEnum.CRUISER, ItemUtils.getItem(Material.MINECART, "&b&oCruiser", "&6Type: &7Sport", "&6Speed: &71 - 2", "Right click", "to use it"));
		items.put(CarsEnum.DERBY, ItemUtils.getItem(Material.MINECART, "&b&oDerby", "&6Type: &7Heavy Duty", "&6Speed: &71 - 3", "Right click", "to use it"));
		items.put(CarsEnum.ROYCE, ItemUtils.getItem(Material.MINECART, "&b&oRoyce", "&6Type: &7Classy", "&6Speed: &71 - 4", "Right click", "to use it"));
		items.put(CarsEnum.ESCALADE_SPORT, ItemUtils.getItem(Material.MINECART, "&b&oEscalade Sport", "&6Type: &7Sport", "&6Speed: &71 - 7", "Right click", "to use it"));
		items.put(CarsEnum.CYPHER, ItemUtils.getItem(Material.MINECART, "&b&oCypher", "&6Type: &7Sport", "&6Speed: &71 - 8", "Right click", "to use it"));
		items.put(CarsEnum.AVENGER_GT, ItemUtils.getItem(Material.MINECART, "&b&oAvenger GT", "&6Type: &7Super", "&6Speed: &71 - 10", "Right click", "to use it"));
		
		shop.put(CarsEnum.ADVENTURE, ItemUtils.getItem(Material.MINECART, "&b&oAdventure", "&7&oCollection: &9Classic", "&6Speed: &91", "", "&a&lPrice: &5750 Coins"));
		shop.put(CarsEnum.CRUISER, ItemUtils.getItem(Material.MINECART, "&b&oCruiser", "&7Type: &9Sport", "&6Speed: &91 - 2", "", "&a&lPrice: &51000 Coins"));
		shop.put(CarsEnum.DERBY, ItemUtils.getItem(Material.MINECART, "&b&oDerby", "&7Type: &9Heavy Duty", "&6Speed: &91 - 3", "", "&a&lPrice: &51500 Coins"));
		shop.put(CarsEnum.ROYCE, ItemUtils.getItem(Material.MINECART, "&b&oRoyce", "&7Type: &9Classy", "&6Speed: &91 - 4", "", "&a&lPrice: &51750 Coins"));
		shop.put(CarsEnum.ESCALADE_SPORT, ItemUtils.getItem(Material.MINECART, "&b&oEscalade Sport", "&7Type: &9Sport", "&6Speed: &71 - 7", "", "&a&lPrice: &52500 Coins"));
		shop.put(CarsEnum.CYPHER, ItemUtils.getItem(Material.MINECART, "&b&oCypher", "&7Type: &9Sport", "&6Speed: &91 - 8", "", "&a&lPrice: &53000 Coins"));
		shop.put(CarsEnum.AVENGER_GT, ItemUtils.getItem(Material.MINECART, "&b&oAvenger GT", "&7Type: &9Super", "&6Speed: &91 - 10", "", "&a&lPrice: &55000 Coins"));

		shop2.put(shop.get(CarsEnum.ADVENTURE), CarsEnum.ADVENTURE);
		shop2.put(shop.get(CarsEnum.CRUISER), CarsEnum.CRUISER);
		shop2.put(shop.get(CarsEnum.DERBY), CarsEnum.DERBY);
		shop2.put(shop.get(CarsEnum.ROYCE), CarsEnum.ROYCE);
		shop2.put(shop.get(CarsEnum.ESCALADE_SPORT), CarsEnum.ESCALADE_SPORT);
		shop2.put(shop.get(CarsEnum.CYPHER), CarsEnum.CYPHER);
		shop2.put(shop.get(CarsEnum.AVENGER_GT), CarsEnum.AVENGER_GT);
		
		key = ItemUtils.getItem(Material.PRISMARINE_SHARD, ChatColor.BLUE + "'s Key", "Right click", "your car", "with this");
	}

	private static HashMap<CarsEnum, ItemStack> items = new HashMap<CarsEnum, ItemStack>();
	private static HashMap<CarsEnum, ItemStack> shop = new HashMap<CarsEnum, ItemStack>();
	public static HashMap<ItemStack, CarsEnum> shop2 = new HashMap<ItemStack, CarsEnum>();
	private int min;
	private int max;
	private String type;
	private int price;
	private static ItemStack key;
	private String name;
	private String color;
	
	private CarsEnum(int min, int max, String type, int price, String name, String color) {
		this.min = min;
		this.max = max;
		this.type = type;
		this.price = price;
		this.name = name;
		this.color = ChatColor.translateAlternateColorCodes('&', color);
	}
	
	public String getType() {return type;}
	public Integer getMin() {return min;}
	public Integer getMax() {return max;}
	public Integer getPrice() {return price;}
	public ItemStack getItem() {return items.get(this);}
	public ItemStack getShop() {return shop.get(this);}
	public ItemStack getKey() {
		ItemStack i = key.clone();
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(ChatColor.BLUE + name + "'s Key");
		i.setItemMeta(im);
		return key;}
	public String getName() {return name;}
	public String getSName() {
		return name.split(" ")[0];
	}
	public String getCN() { return color + name; }
	public String getC() { return color.substring(0, 2); }
	
	public void add(Player p) {
		PlayerConfig.load(p);
		FileConfiguration fc = PlayerConfig.get();
		if (!fc.contains("Vehicles.Cars")) fc.set("Vehicles.Cars", new ArrayList<String>());
		List<String> ls = fc.getStringList("Vehicles.Cars");
		String s = ls.size() + "";
		if (ls.size() < 10) s = "0" + s;
		ls.add(s + ": " + this.getName());
		fc.set("Vehicles.Cars", ls);
		PlayerConfig.save();
		Operations.setMoney(p.getUniqueId(), Operations.getMoney(p)-this.price);
	}
}
