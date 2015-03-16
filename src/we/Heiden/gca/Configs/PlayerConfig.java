package we.Heiden.gca.Configs;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import we.Heiden.gca.core.Main;

public class PlayerConfig {

	public static File pfile;
	public static File folder = new File("plugins" + File.separator + Main.pl.getName() + File.separator + "user data" + File.separator);
	public static FileConfiguration player;
	
	public PlayerConfig(Player p) {
		pfile = new File(folder.getPath() + File.separator + p.getName() + ".yml");
		if (!folder.exists()) folder.mkdirs();
		if (!pfile.exists()) try {pfile.createNewFile();
		} catch(Exception ex) {Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error when creating " + pfile.getName() + "!");}
		player = YamlConfiguration.loadConfiguration(pfile);
	}
	
	public static File getFolder() {return folder;}
	public static File getFile() {return pfile;}
	public static FileConfiguration get() {return player;}
	public static FileConfiguration get(Player p) {load(p); return player;}
	
	public static void load(Player p) {
		save();
		pfile = new File(folder.getPath() + File.separator + p.getName() + ".yml");
		player = YamlConfiguration.loadConfiguration(pfile);
	}
	
	public static void save() {
		try {player.save(pfile);
		} catch(Exception ex) {Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error when saving " + pfile.getName() + "!");}
	}
}
