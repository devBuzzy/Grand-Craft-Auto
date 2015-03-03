package we.Heiden.gca.core;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import we.Heiden.gca.Events.EventsHandler;
import we.Heiden.gca.Functions.Settings;
import we.Heiden.gca.Misc.Others;
import we.Heiden.gca.Misc.SettingsEnum;

/**
 * ********************************************* <p>
 * <b>This has been made by <i>Heiden Team</b>
 * <ul>
 * <li>Don't claim this class as your own
 * <li>Don't remove this disclaimer
 * </ul>
 *         <b>All rights reserved <p>
 *           Heiden Team 2015 <p></b>
 * ********************************************* 
 **/
public class Main extends JavaPlugin {

	public static Plugin pl;
	
	public void onEnable() {
		pl = this;
		Others.items();
		Others.load();
		new EventsHandler(this);
		new Timer5T().runTaskTimer(this, 20, 5);
		Settings.configure();
		for (Player p : Bukkit.getOnlinePlayers()) SettingsEnum.register(p);
	}
	
	public void onDisable() {
		Others.save();
		pl = null;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("test")) {
			
		}
		return true;
	}
}
