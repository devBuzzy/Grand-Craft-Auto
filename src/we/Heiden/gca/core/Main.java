package we.Heiden.gca.core;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import we.Heiden.gca.Events.EventsHandler;
import we.Heiden.gca.Misc.Others;

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

	public void onEnable() {
		new EventsHandler(this);
		
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("test")) {
			if ((args.length > 3 && sender instanceof Player) || args.length > 4) {
				if (sender instanceof BlockCommandSender || sender instanceof Player) {
					Location loc;
					if (sender instanceof BlockCommandSender) loc = ((BlockCommandSender)sender).getBlock().getLocation();
					else loc = ((Player)sender).getLocation();
					if (args[1].startsWith("~")) {
						int x = loc.getBlockX();
						if (Others.isInt(args[1].substring(1))) x+= Double.parseDouble(args[1].substring(1));
						args[1] = x + "";
					}
					if (args[2].startsWith("~")) {
						int y = loc.getBlockY();
						if (Others.isInt(args[2].substring(1))) y+= Double.parseDouble(args[2].substring(1));
						args[2] = y + "";
					}
					if (args[3].startsWith("~")) {
						int z = loc.getBlockZ();
						if (Others.isInt(args[3].substring(1))) z+= Double.parseDouble(args[3].substring(1));
						args[3] = z + "";
					}
				}
				if (Others.isInt(args[1]) && Others.isInt(args[2]) && Others.isInt(args[3]) && EntityType.fromName(args[0]) != null) {
					World w;
					if (args.length > 3) w = Bukkit.getWorld(args[4]);
					else w = ((Player)sender).getWorld();
					double x = Double.parseDouble(args[1]);
					double y = Double.parseDouble(args[2]);
					double z = Double.parseDouble(args[3]);
					w.spawnEntity(new Location(w, x, y, z), EntityType.fromName(args[0]));
				}
			}
		}
		return true;
	}
}
