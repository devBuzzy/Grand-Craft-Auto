package we.Heiden.gca.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import we.Heiden.gca.Commands.SetnpcCommand;
import we.Heiden.gca.Configs.Config;
import we.Heiden.gca.Configs.PlayerConfig;
import we.Heiden.gca.Events.PlayerMove;
import we.Heiden.gca.Functions.Bag;
import we.Heiden.gca.Functions.CarsEnum;
import we.Heiden.gca.Functions.Settings;
import we.Heiden.gca.Functions.SettingsEnum;
import we.Heiden.gca.Functions.Tutorial;
import we.Heiden.gca.NPCs.CustomVillager;
import we.Heiden.gca.NPCs.NMSNpc;
import we.Heiden.gca.NPCs.NPCs;
import we.Heiden.gca.Pets.Ocelot;
import we.Heiden.gca.Pets.Wolf;
import we.Heiden.gca.Weapons.Weapons;
import we.Heiden.gca.core.Main;
import we.Heiden.hs2.Holograms.HologramUtils;

public class UtilsMain {
	
	public static void bc(String message) { for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage(message); }
	
	public static void setup() {
		try { new HologramUtils().registerCustomEntity(NMSNpc.class, "Villager", 120); } catch(Exception ex) { }
		try { new HologramUtils().registerCustomEntity(CustomVillager.class, "Villager", 120); } catch(Exception ex) { }
		try { new HologramUtils().registerCustomEntity(Wolf.class, "Wolf", 95); } catch(Exception ex) { }
		try { new HologramUtils().registerCustomEntity(Ocelot.class, "Ozelot", 98); } catch(Exception ex) { }
		loadCivilians();
		ItemUtils.setup();
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

	private static void loadCivilians() {
		if (Config.get().contains("Civilians")) for (String path : Config.get().getConfigurationSection("Civilians").getKeys(false)) 
			SetnpcCommand.villagers.put(CustomVillager.spawn(Functions.loadLoc("Civilians." + path, Config.get()), "&a&lCivilian"), "Civilians." + path);
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
		if (PlayerConfig.getFile() == null || !PlayerConfig.getFile().exists()) {
			new PlayerConfig(p);
			Location loc = Functions.loadLoc("Airport", p);
			if (loc != null) {
				p.teleport(loc);
				for (Player pl : Bukkit.getOnlinePlayers()) if (pl != p) pl.hidePlayer(p);
				PlayerMove.onAirport.add(p);
				if (Config.get().contains("Tutorial.1") && Config.get().contains("Tutorial.2") && Config.get().contains("Tutorial.3")) Tutorial.tuto.put(p, 1);
			}
		}
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
		Weapons.register(p);
	}
}
