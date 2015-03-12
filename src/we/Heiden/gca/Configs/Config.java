package we.Heiden.gca.Configs;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import we.Heiden.gca.core.Main;

public class Config {

	static File folder = new File("plugins" + File.separator + Main.pl.getName() + File.separator);
	static File cfile = new File(folder.getPath() + File.separator + "config.yml");
	static FileConfiguration fc;
	
	public Config() {
		if (!folder.exists()) folder.mkdirs();
		if (!cfile.exists()) try {cfile.createNewFile();
		} catch(Exception ex) {Bukkit.broadcastMessage(ChatColor.RED + "Error when creating " + cfile.getName() + "!");}
		fc = YamlConfiguration.loadConfiguration(cfile);
	}
	
	public static FileConfiguration get() {return fc;}
	
	public static void save() {
		try {fc.save(cfile);
		} catch(Exception ex) {Bukkit.broadcastMessage(ChatColor.RED + "Error when saving " + cfile.getName() + "!");}
	}
}
