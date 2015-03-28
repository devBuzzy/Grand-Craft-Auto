package we.Heiden.gca.Stores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import we.Heiden.gca.Configs.PlayerConfig;
import we.Heiden.gca.Messages.JsonMessage;
import we.Heiden.gca.Messages.JsonMessage.ClickAction;
import we.Heiden.gca.Messages.JsonMessage.HoverAction;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.Messages.Titles;
import we.Heiden.gca.Pets.Pets;
import we.Heiden.gca.Utils.Confirmable;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.hs2.Messages.Chat;
import we.Heiden.hs2.SQL.Operations;

public class PetStore implements BasicStore, Confirmable {
	
	public static HashMap<Player, Pets> pets = new HashMap<Player, Pets>();
	public static ItemStack wolf = ItemUtils.getItem(Material.MONSTER_EGG, (short) 95, "&a&lDoggy Friend", "&bJust a dog", "&6&lCost: &d500 Coins");
	public static ItemStack cat = ItemUtils.getItem(Material.MONSTER_EGG, (short) 98, "&6&lNian Style", "&d&oMeeew", "&6&lCost: &d750 Coins");
	public static String ShopN = ChatColor.translateAlternateColorCodes('&', "&a&lPet Store");
	public static HashMap<Player, Boolean> confirming = new HashMap<Player, Boolean>();

	public String welcome() { return ChatColor.translateAlternateColorCodes('&', "&aPet Clerk &d-> &eWelcome!"); }
	
	public void options(Player p) {
		JsonMessage.sendJson(p, 
				JsonMessage.newJson("&6&l============================="), 
				JsonMessage.newJson()
					.add("          &9&l*").build()
					.add("&a&lBuy Pets")
						.hoverEvent(HoverAction.Show_Text, "&bYou will love this")
						.clickEvent(ClickAction.Run_Command, "/Store Pet").build().build(), 
				JsonMessage.newJson()
					.add("          &4&l*").build()
					.add("&c&oRobbery Mode")
						.hoverEvent(HoverAction.Show_Text, "&cNot such a nice guy")
						.clickEvent(ClickAction.Run_Command, "/Store Robbery Pet").build().build(), 
				JsonMessage.newJson("&6&l============================="));
	}
	
	public static void Shop(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9, ShopN);
		for (int n = 0; n < inv.getSize(); n++) inv.setItem(n, ItemUtils.ItemDefault());
		inv.setItem(3, wolf);
		inv.setItem(5, cat);
		
		p.openInventory(inv);
	}
	
	public static void Buy(Player p, Pets pet) {
		FileConfiguration fc = PlayerConfig.load(p);
		Messager.load(p);
		if (!fc.contains("Pets")) fc.set("Pets", new ArrayList<String>());
		List<String> ls = fc.getStringList("Pets");
		if (ls.contains(pet.getType())) { Messager.e1("You already have that pet"); p.closeInventory(); }
		else {
			int money = Operations.getMoney(p);
			if (money < pet.cost) { Messager.e1("You can`t afford that"); p.closeInventory(); }
			else {
				pets.put(p, pet);
				confirmation(p, pet);
			}
		}
	}
	
	public static void confirmation(Player p, Pets pet) {
		ItemUtils.yes.put(p, new PetStore());
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&dBuy a " + pet.getType() + "?"));
		for (int n = 0; n < inv.getSize(); n++) inv.setItem(n, ItemUtils.ItemDefault());
		inv.setItem(12, ItemUtils.Yes());
		inv.setItem(14, ItemUtils.No());
		
		p.openInventory(inv);
	}
	
	public void no(Player p) { p.closeInventory(); }

	public void yes(Player p) {
		confirming.put(p, false);
		new Chat(p).msg("&a&lName your pet", "  &2&oType a name in chat!", "    &c&oWarning: You can`t change it", "&dColors not supported!");
	}
	
	public static void finish(Player p) {
		Messager.load(p);
		Pets pet = pets.get(p);
		pets.remove(p);
		FileConfiguration fc = PlayerConfig.load(p);
		List<String> ls = fc.getStringList("Pets");
		ls.add(pet.getType());
		fc.set("Pets", ls);
		PlayerConfig.save();
		Operations.setMoney(p.getUniqueId(), Operations.getMoney(p) - pet.cost);
		new Titles(p).title("Pet Bought").send();
	}
}
