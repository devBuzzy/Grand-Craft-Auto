package we.Heiden.gca.Functions;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import we.Heiden.gca.Utils.ItemUtils;

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
		key = ItemUtils.getItem(Material.PRISMARINE_SHARD, ChatColor.BLUE + "'s Key", "Right click", "your car", "with this");
	}
	
	private static HashMap<CarsEnum, ItemStack> items = new HashMap<CarsEnum, ItemStack>();
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
	public ItemStack getKey() {
		ItemStack i = key.clone();
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(ChatColor.BLUE + name + "'s Key");
		i.setItemMeta(im);
		return key;}
	public String getName() {return name;}
	public String getCN() {return color + name;}
}
