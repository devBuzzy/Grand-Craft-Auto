package we.Heiden.gca.Stores;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import we.Heiden.gca.Configs.PlayerConfig;
import we.Heiden.gca.Functions.CarsEnum;
import we.Heiden.gca.Messages.JsonMessage;
import we.Heiden.gca.Messages.JsonMessage.ClickAction;
import we.Heiden.gca.Messages.JsonMessage.HoverAction;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.Utils.ItemUtils;

public class BankStore implements BasicStore {

	public String welcome() { return ChatColor.translateAlternateColorCodes('&', "&aDealership Owner &d-> &eWelcome!"); }
	
	public void options(Player p) {
		JsonMessage.sendJson(p, 
				JsonMessage.newJson("&6&l============================="), 
				JsonMessage.newJson()
					.add("          ").build()
					.add("&a&lManage Owned Cars")
						.hoverEvent(HoverAction.Show_Item, JsonMessage.newItem("&a&lDifferent from garage", "&bHere you can &lAuth", "&bor sell &lYour &bcars"))
						.clickEvent(ClickAction.Run_Command, "/Store Cars Manage").build().build(), 
				JsonMessage.newJson("&e&l============================="), 
				JsonMessage.newJson()
					.add("              ").build()
					.add("&a&lPurchase Car")
						.hoverEvent(HoverAction.Show_Item, JsonMessage.newItem("&b&oVisit our car shop", "", "&a\"&e&oI swear you will love it&a\""))
							.clickEvent(ClickAction.Run_Command, "/Store Cars Purchase").build().build(), 
				JsonMessage.newJson("&6&l============================="));
	}
	
	public static void Manage(Player p) {
		PlayerConfig.load(p);
		if (!PlayerConfig.get().contains("Vehicles.Cars")) {
			Messager.load(p);
			Messager.e1("You don`t have any cars");
			return;
		}
		List<String> cars = PlayerConfig.get().getStringList("Vehicles.Cars");
		int n = cars.size();
		n = (n/9*9)+9;
		Inventory inv = Bukkit.createInventory(null, n, ManageN());
		n = 0;
		for (String s : cars) {
			ItemStack item = CarsEnum.valueOf(s.substring(4).toUpperCase().replace(" ", "_")).getItem();
			item.setAmount(n+1);
			inv.setItem(n, item);
			n++;
		}
		
		for (; n<inv.getSize(); n++) inv.setItem(n, ItemUtils.ItemDefault());
		p.openInventory(inv);
	}
	
	public static String ManageN() { return ChatColor.translateAlternateColorCodes('&', "&a&lCars Management"); }
	
	public static void Purchase(Player p) {
		int n = CarsEnum.values().length;
		n = (n/9*9)+9;
		Inventory inv = Bukkit.createInventory(null, n, PurchaseN());
		n = 0;
		for (CarsEnum car : CarsEnum.values()) {
			ItemStack item = car.getShop();
			item.setAmount(n+1);
			inv.setItem(n, item);
			n++;
		}
		
		for (; n<inv.getSize(); n++) inv.setItem(n, ItemUtils.ItemDefault());
		
		p.openInventory(inv);
	}
	
	public static String PurchaseN() { return ChatColor.translateAlternateColorCodes('&', "&a&lPurchase Cars"); }
}
