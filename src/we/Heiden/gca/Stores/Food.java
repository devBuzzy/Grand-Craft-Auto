package we.Heiden.gca.Stores;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import we.Heiden.gca.Utils.ItemUtils;

public enum Food {

	Bread(ItemUtils.getItem(Material.BREAD, "&dHot Bread"), 2),
	Porkchop(ItemUtils.getItem(Material.GRILLED_PORK, "&eDelicious Meat"), 4),
	Chicken(ItemUtils.getItem(Material.COOKED_CHICKEN, "&aDelicious Meat"), 4),
	apple(ItemUtils.getItem(Material.APPLE, "&9Healty Apple"), 2),
	cookie(ItemUtils.getItem(Material.COOKIE, "&2I Like Chocolate"), 1),
	mutton(ItemUtils.getItem(Material.COOKED_MUTTON, "&bDelicious Meat"), 5),
	melon(ItemUtils.getItem(Material.MELON, "&c85% Water"), 2),
	salmon(ItemUtils.getItem(Material.COOKED_FISH, (short) 1, "&3Delicious Meat"), 3),
	gapple(ItemUtils.getItem(Material.GOLDEN_APPLE, (short) 1, "&6The Taste of Power"), 13),
	beef(ItemUtils.getItem(Material.COOKED_BEEF, "&7Delicious Meat"), 4),
	carrot(ItemUtils.getItem(Material.CARROT_ITEM, "&fGood for vision"), 2),
	fish(ItemUtils.getItem(Material.COOKED_FISH, "&5Delicious Meat"), 2),
	pie(ItemUtils.getItem(Material.PUMPKIN_PIE, "&eJust a Pie"), 5),
	stew_m(ItemUtils.getItem(Material.MUSHROOM_SOUP, "&a&lHEALER", "This will restore", "all your hp", " &cWarning: &5&oWont restore", " food level"), 30),
	rabbit(ItemUtils.getItem(Material.COOKED_RABBIT, "&3Delicious Meat"), 5),
	stew_r(ItemUtils.getItem(Material.RABBIT_STEW, "&bNormal Stew"), 6),
	potato(ItemUtils.getItem(Material.BAKED_POTATO, "&2Potato"), 2),
	cake(ItemUtils.getItem(Material.CAKE, "&fCake"), 50);
	
	public ItemStack item;
	public int price;
	
	private Food(ItemStack item, int price) {
		this.item = item;
		this.price = price;
	}
	
	public static int getPrice(ItemStack item) {
		for (Food food : Food.values()) if (food.item.equals(item)) return food.price;
		return 1;
	}
}
