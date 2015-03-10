package we.Heiden.gca.Cars;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import we.Heiden.gca.Misc.Others;

public enum CarsEnum {

	ADVENTURE(1, 1, "Classic", 750, Others.getItem(Material.MINECART, "&b&oAdventure", "&6Type: &7Classic", "&6Speed: &71", "Right click", "to use it")),
	CRUISER(1, 2, "Sport", 1000, Others.getItem(Material.MINECART, "&b&oCruiser", "&6Type: &7Sport", "&6Speed: &71 - 2", "Right click", "to use it")),
	DERBY(1, 3, "Heavy Duty", 1500, Others.getItem(Material.MINECART, "&b&oDerby", "&6Type: &7Heavy Duty", "&6Speed: &71 - 3", "Right click", "to use it")),
	ROYCE(1, 4, "Classy", 1750, Others.getItem(Material.MINECART, "&b&oRoyce", "&6Type: &7Classy", "&6Speed: &71 - 4", "Right click", "to use it")),
	ESCALADE_SPORT(1, 7, "Sport", 2500, Others.getItem(Material.MINECART, "&b&oEscalade Sport", "&6Type: &7Sport", "&6Speed: &71 - 7", "Right click", "to use it")),
	CYPHER(1, 8, "Sport", 3000, Others.getItem(Material.MINECART, "&b&oCypher", "&6Type: &7Sport", "&6Speed: &71 - 8", "Right click", "to use it")),
	AVENGER_GT(1, 10, "Super", 5000, Others.getItem(Material.MINECART, "&b&oAvenger GT", "&6Type: &7Super", "&6Speed: &71 - 10", "Right click", "to use it"));
	
	private int min;
	private int max;
	private String type;
	private int price;
	private ItemStack item;
	private ItemStack key;
	
	private CarsEnum(int min, int max, String type, int price, ItemStack item) {
		this.min = min;
		this.max = max;
		this.type = type;
		this.price = price;
		this.item = item;
		String name = item.getItemMeta().getDisplayName();
		this.key = Others.getItem(Material.PRISMARINE_SHARD, ChatColor.BLUE + name.substring(4) + "'s Key", "Right click", "your car", "with this");
	}
	
	public String getType() {return type;}
	public Integer getMin() {return min;}
	public Integer getMax() {return max;}
	public Integer getPrice() {return price;}
	public ItemStack getItem() {return item;}
	public ItemStack getKey() {return key;}
}
