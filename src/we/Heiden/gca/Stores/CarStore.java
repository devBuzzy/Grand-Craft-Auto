package we.Heiden.gca.Stores;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import we.Heiden.gca.Configs.PlayerConfig;
import we.Heiden.gca.Functions.CarsEnum;
import we.Heiden.gca.Messages.JsonMessage;
import we.Heiden.gca.Messages.JsonMessage.ClickAction;
import we.Heiden.gca.Messages.JsonMessage.HoverAction;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.Messages.Titles;
import we.Heiden.gca.Utils.Confirmable;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.hs2.SQL.Operations;

public class CarStore implements BasicStore, Confirmable {

	public String welcome() { return ChatColor.translateAlternateColorCodes('&', "&aDealership Owner &d-> &eWelcome!"); }
	public String welcomeS() { return new CarStore().welcome(); }
	
	private static HashMap<Player, CarsEnum> temp01 = new HashMap<Player, CarsEnum>();
	public static HashMap<Player, String> invn = new HashMap<Player, String>();
	private static HashMap<Player, Integer> carInt = new HashMap<Player, Integer>();
	private static HashMap<Player, CarsEnum> carEnum = new HashMap<Player, CarsEnum>();
	
	public static void optionsS(Player p) { new CarStore().options(p); }
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
	
	public static void confirm(Player p, CarsEnum car) {
		temp01.put(p, car);
		ItemUtils.yes.put(p, new CarStore());
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&dBuy a " + car.getCN() + "&d?"));
		for (int n = 0; n < inv.getSize(); n++) inv.setItem(n, ItemUtils.ItemDefault());
		inv.setItem(12, ItemUtils.Yes());
		inv.setItem(14, ItemUtils.No());
		
		p.openInventory(inv);
	}
	
	public static void buy(Player p, CarsEnum car) {
		int money = Operations.getMoney(p);
		int price = car.getPrice();
		Messager.load(p);
		if (price > money) {
			p.closeInventory();
			int left = price-money;
			Messager.e1("You need " + left + " coins more");
		} else confirm(p, car);
	
	}
	
	public void no(Player p) { p.closeInventory(); temp01.remove(p); carInt.remove(p); invn.remove(p); carEnum.remove(p); }
	public void yes(Player p) {
		p.closeInventory();
		if (temp01.containsKey(p)) {
			temp01.get(p).add(p);
			new Titles(p, "&aPurchased: " + temp01.get(p).getC() + temp01.get(p).getSName() + " &bcar")
				.subtitle("&d&oIt has been stored in the garage").send();
			temp01.remove(p);
		} else {
			PlayerConfig.load(p);
			List<String> cars = PlayerConfig.get().getStringList("Vehicles.Cars");
			int n = 0;
			for (String car : cars) {
				if (!car.equals(cars.get(carInt.get(p)))) {
					n++;
					String nn = n + "";
					if (n < 10) nn = "0" + nn;
					cars.set(n-1, nn + car.substring(2));
				}
			}
			carInt.remove(p);
			cars.remove(cars.size()-1);
			PlayerConfig.get().set("Vehicles.Cars", cars);
			PlayerConfig.save();
			invn.remove(p);
			Operations.setMoney(p.getUniqueId(), Operations.getMoney(p)+carEnum.get(p).getPrice()/2);
			carEnum.remove(p);
			new Titles(p, "&a&lCar Sold!").send();
		}
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
		double d = n;
		if (d/9D*9D == n/9*9) n = n/9*9;
		else n = (n/9*9)+9;
		Inventory inv = Bukkit.createInventory(null, n, ManageN());
		n = 0;
		for (String s : cars) {
			ItemStack item = CarsEnum.valueOf(s.substring(4).toUpperCase().replace(" ", "_")).getItem().clone();
			item.setAmount(n+1);
			ItemMeta im = item.getItemMeta();
			List<String> ls = im.getLore();
			im.setLore(Arrays.asList(ls.get(0), ls.get(1)));
			item.setItemMeta(im);
			inv.setItem(n, item);
			n++;
		}
		
		for (; n<inv.getSize(); n++) inv.setItem(n, ItemUtils.ItemDefault());
		p.openInventory(inv);
	}
	
	public static String ManageN() { return ChatColor.translateAlternateColorCodes('&', "&a&lCars Management"); }
	
	public static void ManageCar(Player p, int car) {
		PlayerConfig.load(p);
		List<String> cars = PlayerConfig.get().getStringList("Vehicles.Cars");
		CarsEnum cenum = CarsEnum.valueOf(cars.get(car).substring(4).toUpperCase().replace(" ", "_"));
		
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "&a&lManagement: "+ cenum.getC() + cenum.getSName()));
		for (int n = 0; n < inv.getSize(); n++) inv.setItem(n, ItemUtils.ItemDefault());
		ItemStack info = ItemUtils.CarInfo();
		ItemMeta im = info.getItemMeta();
		im.setLore(Arrays.asList("", 
				ChatColor.COLOR_CHAR + "9" + ChatColor.COLOR_CHAR + "lCar: " + cenum.getCN(),
				ChatColor.COLOR_CHAR + "aOriginal Price: " + ChatColor.COLOR_CHAR + "d" + cenum.getPrice(),
				ChatColor.COLOR_CHAR + "2Sell Price: " + ChatColor.COLOR_CHAR + "d" + cenum.getPrice()/2));
		info.setItemMeta(im);
		inv.setItem(0, info);
		inv.setItem(3, ItemUtils.CarSell());
		inv.setItem(5, ItemUtils.CarAuth());
		
		invn.put(p, inv.getName());
		carInt.put(p, car);
		carEnum.put(p, cenum);
		p.openInventory(inv);
	}
	
	public static void confirm2(Player p) {
		ItemUtils.yes.put(p, new CarStore());
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&dSell a(n) " + carEnum.get(p).getCN() + "&d?"));
		for (int n = 0; n < inv.getSize(); n++) inv.setItem(n, ItemUtils.ItemDefault());
		inv.setItem(12, ItemUtils.Yes());
		inv.setItem(14, ItemUtils.No());
		
		p.openInventory(inv);
	}
	
	public static void Purchase(Player p) {
		int n = CarsEnum.values().length;
		n = (n/9*9)+9;
		Inventory inv = Bukkit.createInventory(null, n, PurchaseN());
		n = 0;
		for (CarsEnum car : CarsEnum.values()) {
			inv.setItem(n, car.getShop());
			n++;
		}
		
		for (; n<inv.getSize(); n++) inv.setItem(n, ItemUtils.ItemDefault());
		
		p.openInventory(inv);
	}
	
	public static String PurchaseN() { return ChatColor.translateAlternateColorCodes('&', "&a&lPurchase Cars"); }
}
