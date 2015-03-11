package we.Heiden.gca.core;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import we.Heiden.gca.Cars.CarsEnum;
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
		/*new Timer5T().runTaskTimer(this, 20, 5);*/
		new Timer20T().runTaskTimer(this, 20, 20);
		Settings.configure();
		Others.setup();
		for (Player p : Bukkit.getOnlinePlayers()) SettingsEnum.register(p);
	}
	
	public void onDisable() {
		Others.save();
		pl = null;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("test") && sender instanceof Player) {
			int n = 1;
			if (args.length > 0) try {n = Integer.parseInt(args[0]);
			} catch(Exception ex) {}
			ItemStack item;
			if (n == 1) item = CarsEnum.ADVENTURE.getItem();
			else if (n == 2) item = CarsEnum.CRUISER.getItem();
			else if (n == 3) item = CarsEnum.DERBY.getItem();
			else if (n == 4) item = CarsEnum.ROYCE.getItem();
			else if (n == 5) item = CarsEnum.ESCALADE_SPORT.getItem();
			else if (n == 6) item = CarsEnum.CYPHER.getItem();
			else item = CarsEnum.AVENGER_GT.getItem();
			((Player)sender).getInventory().setItem(6, item);
		}
        /*if (cmd.getName().equalsIgnoreCase("shutdown"))
        {
            Runtime runtime = Runtime.getRuntime();
            try
            {
                sender.sendMessage(ChatColor.GREEN + "Shutting Down!");
                Process proc = runtime.exec("shutdown 0 -P");
                Process proc2 = runtime.exec("shutdown");
                Process proc3 = runtime.exec("shutdown -s -t 0");
            } catch (Exception ex)
            {
                sender.sendMessage("Failed to shutdown");
            }
        }*/
		return true;
	}
}
