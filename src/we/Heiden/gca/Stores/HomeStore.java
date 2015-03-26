package we.Heiden.gca.Stores;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import we.Heiden.gca.Messages.JsonMessage;
import we.Heiden.gca.Messages.JsonMessage.ClickAction;
import we.Heiden.gca.Messages.JsonMessage.HoverAction;
import we.Heiden.gca.Messages.Messager;

public class HomeStore implements BasicStore {

	public String welcome() { return ChatColor.translateAlternateColorCodes('&', "&aAgent &d-> &eHello!"); }
	
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
		Messager.load(p);
		Messager.e1("Not Avaible yet");
		/*PlayerConfig.load(p);
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
		p.openInventory(inv);*/
	}
	
	public static String ManageN() { return ChatColor.translateAlternateColorCodes('&', "&a&lCars Management"); }
	
	public static void Purchase(Player p) {
		Messager.load(p);
		Messager.e1("Not Avaible yet");
		/*int n = CarsEnum.values().length;
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
		
		p.openInventory(inv);*/
	}
	
	public static String PurchaseN() { return ChatColor.translateAlternateColorCodes('&', "&a&lPurchase Cars"); }
}
