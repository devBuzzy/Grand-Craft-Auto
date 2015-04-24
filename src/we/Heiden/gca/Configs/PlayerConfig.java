package we.Heiden.gca.Configs;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import we.Heiden.gca.core.Main;

public class PlayerConfig {

	public static File pfile;
	public static File folder = new File("plugins" + File.separator
			+ Main.pl.getName() + File.separator + "user data" + File.separator);
	public static FileConfiguration player;

	public PlayerConfig(Player p) {
		new PlayerConfig(p.getUniqueId());
	}

	public PlayerConfig(UUID uuid) {
		pfile = new File(folder.getPath() + File.separator + uuid + ".yml");
		if (!folder.exists())
			folder.mkdirs();
		if (!pfile.exists())
			try {
				pfile.createNewFile();
			} catch (Exception ex) {
				Bukkit.getConsoleSender().sendMessage(
						ChatColor.RED + "Error when creating "
								+ pfile.getName() + "!");
			}
		player = YamlConfiguration.loadConfiguration(pfile);
	}

	public static File getFolder() {
		return folder;
	}

	public static File getFile() {
		return pfile;
	}

	public static FileConfiguration get() {
		return player;
	}

	public static FileConfiguration get(Player p) {
		return load(p);
	}

	public static FileConfiguration get(UUID uuid) {
		return load(uuid);
	}

	public static FileConfiguration load(Player p) {
		return load(p.getUniqueId());
	}

	public static FileConfiguration load(UUID uuid) {
		pfile = new File(folder.getPath() + File.separator + uuid + ".yml");
		player = YamlConfiguration.loadConfiguration(pfile);
		return player;
	}

	public static void save() {
		try {
			player.save(pfile);
		} catch (Exception ex) {
			Bukkit.getConsoleSender().sendMessage(
					ChatColor.RED + "Error when saving " + pfile.getName()
							+ "!");
		}
	}
}
