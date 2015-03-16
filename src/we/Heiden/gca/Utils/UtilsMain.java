package we.Heiden.gca.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import we.Heiden.gca.Configs.Config;
import we.Heiden.gca.Configs.PlayerConfig;
import we.Heiden.gca.Functions.Bag;
import we.Heiden.gca.Functions.CarsEnum;
import we.Heiden.gca.Functions.Settings;
import we.Heiden.gca.Functions.SettingsEnum;
import we.Heiden.gca.Holograms.HologramUtils;
import we.Heiden.gca.NPCs.NPCs;
import we.Heiden.gca.core.Main;

public class UtilsMain {
	
	public static void setup() {
		try { new HologramUtils(); } catch(Exception ex) { }
		NPCs.setup();
		CarsEnum.setup();
		ItemUtils.setCars();
		ItemUtils.setKeys();
		ItemUtils.setItems();
		Settings.configure();
		for (Player p : Bukkit.getOnlinePlayers()) SettingsEnum.register(p);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.pl, new Runnable() {
			public void run() {Functions.fc = Config.get();}
		}, 1);
	}

	public static void save() {for (Player p : Bukkit.getOnlinePlayers()) save(p);}
	public static void load() {for (Player p : Bukkit.getOnlinePlayers()) load(p);}
	
	public static void save(Player p) {
		PlayerConfig.load(p);
		Inventory inv = Bag.inventories.get(p);
		int n = 0;
		if (inv != null) for (ItemStack i : inv) {
			if (i != null && i.getType() != Material.AIR) PlayerConfig.get().set("Temp.Bag." + n, i);
			n++;
		}
		if (ItemUtils.hasItems.contains(p)) PlayerConfig.get().set("Temp.hasItems", "true");
		PlayerConfig.save();
	}
	
	public static void load(Player p) {
		if (PlayerConfig.getFile() == null || !PlayerConfig.getFile().exists()) new PlayerConfig(p);
		PlayerConfig.load(p);
		if (PlayerConfig.get().contains("Temp.hasItems")) ItemUtils.hasItems.add(p);
		if (PlayerConfig.get().contains("Temp.Bag")) {
			Inventory inv = Bag.inventories.get(p);
			for (String s : PlayerConfig.get().getConfigurationSection("Temp.Bag").getKeys(false)) {
				int slot = Integer.parseInt(s);
				inv.setItem(slot, PlayerConfig.get().getItemStack("Temp.Bag." + s));
			}
			Bag.inventories.put(p, inv);
		}
		SettingsEnum.register(p);
	}
}
