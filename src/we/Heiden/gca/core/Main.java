package we.Heiden.gca.core;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import we.Heiden.gca.Commands.CommandsHandler;
import we.Heiden.gca.Configs.Config;
import we.Heiden.gca.Events.EventsHandler;
import we.Heiden.gca.Functions.CarsEnum;
import we.Heiden.gca.Holograms.HologramUtils;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.gca.Utils.UtilsMain;

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
		ItemUtils.items();
		UtilsMain.load();
		new Config();
		new EventsHandler(this);
		new Timer2T().runTaskTimer(this, 20, 2);
		/*new Timer5T().runTaskTimer(this, 20, 5);*/
		new Timer20T().runTaskTimer(this, 20, 20);
		UtilsMain.setup();
	}
	
	public void onDisable() {
		UtilsMain.save();
		pl = null;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("test") && sender instanceof Player) {
			if (args.length > 1 && args[0].equalsIgnoreCase("hm")) {
				String s = "";
				int n = 0;
				for (String arg : args) {
					n++;
					if (n > 1) s += ChatColor.translateAlternateColorCodes('&', arg) + " ";
				}
				s = s.substring(0, s.length()-1);
				Location loc = ((Player)sender).getLocation();
				HologramUtils.spawnNMSArmorStand(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), s);
				return true;
			}
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
		} else new CommandsHandler(sender, cmd, args);
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
