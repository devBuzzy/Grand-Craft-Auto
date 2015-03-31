package we.Heiden.gca.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import we.Heiden.gca.Commands.CommandsHandler;
import we.Heiden.gca.Commands.SetnpcCommand;
import we.Heiden.gca.Configs.Config;
import we.Heiden.gca.Events.EventsHandler;
import we.Heiden.gca.Events.PlayerMove;
import we.Heiden.gca.Functions.CarsEnum;
import we.Heiden.gca.Functions.Tutorial;
import we.Heiden.gca.NPCs.CustomVillager;
import we.Heiden.gca.NPCs.NMSNpc;
import we.Heiden.gca.NPCs.NPCs;
import we.Heiden.gca.Pets.Ocelot;
import we.Heiden.gca.Pets.Wolf;
import we.Heiden.gca.Stores.CarStore;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.gca.Utils.UtilsMain;
import we.Heiden.gca.Weapons.WeaponUtils;
import we.Heiden.gca.Weapons.Weapons;
import we.Heiden.hs2.Holograms.NMSEntity;
import we.Heiden.hs2.Messages.ActionBar;
import we.Heiden.hs2.Messages.Chat;
import we.Heiden.hs2.Utils.Functions;

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
		UtilsMain.setup();
		ItemUtils.items();
		UtilsMain.load();
		new Config();
		new EventsHandler(this);
		new Timer1T().runTaskTimer(this, 20, 1);
		new Timer2T().runTaskTimer(this, 20, 2);
		new Timer5T().runTaskTimer(this, 20, 5);
		new Timer20T().runTaskTimer(this, 20, 20);
		for (World w : Bukkit.getWorlds()) {
			for (Entity e: w.getEntities()) {
				if (e instanceof NMSNpc) ((NMSNpc)e).killEntityNMS();
				else if (e instanceof Wolf) ((Wolf)e).killEntityNMS();
				else if (e instanceof Ocelot) ((Ocelot)e).killEntityNMS();
				else if (e instanceof NMSEntity) ((NMSEntity)e).killEntityNMS();
				else if (e instanceof LivingEntity && !(e instanceof Player)) ((LivingEntity)e).setHealth(0.0D);
			}
		}
	}
	
	public void onDisable() {
		UtilsMain.save();
		for (NMSNpc e : NPCs.entities) e.killEntityNMS();
		for (CustomVillager e : SetnpcCommand.villagers.keySet()) e.killEntityNMS();
		pl = null;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		if (args.length == 2 && args[0].equalsIgnoreCase("weapon")) {
			List<String> ls = new ArrayList<String>();
			for (Weapons wep : Weapons.values()) ls.add(Functions.normalize(wep.toString()));
			return ls;
		}
		return null;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("test") && sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length > 0 && args[0].equalsIgnoreCase("spawn")) {
				NPCs npc = NPCs.Cars;
				if (args.length > 1 && !args[1].equalsIgnoreCase("1")) {
					if (args[1].equalsIgnoreCase("2")) npc = NPCs.Store;
					else if (args[1].equalsIgnoreCase("3")) npc = NPCs.Homes;
					else if (args[1].equalsIgnoreCase("4")) npc = NPCs.Pets;
					else npc = NPCs.Bank;
				}
				npc.getNPC().spawn(p);
			} else if (args.length > 0 && args[0].equalsIgnoreCase("despawn")) {
				NPCs npc = null;
				if (args.length > 1) {
					if (args[1].equalsIgnoreCase("1")) npc = NPCs.Cars;
					else if (args[1].equalsIgnoreCase("2")) npc = NPCs.Store;
					else if (args[1].equalsIgnoreCase("3")) npc = NPCs.Homes;
					else if (args[1].equalsIgnoreCase("4")) npc = NPCs.Pets;
					else npc = NPCs.Bank;
				}
				if (npc != null) npc.getNPC().remove();
				else NPCs.clear();
			} else if (args.length > 0 && args[0].equalsIgnoreCase("test")) CarStore.optionsS(p);
			else if (args.length > 0 && args[0].equalsIgnoreCase("test2")) CarStore.Purchase(p);
			else if (args.length > 0 && args[0].equalsIgnoreCase("zoom")) WeaponUtils.zoom(p, true);
			else if (args.length > 0 && args[0].equalsIgnoreCase("zoom2")) WeaponUtils.zoom(p, false);
			else if (args.length > 0 && args[0].equalsIgnoreCase("tutorial")) {
				Location loc = we.Heiden.gca.Utils.Functions.loadLoc("Airport", p);
				if (loc != null) {
					p.teleport(loc);
					for (Player pl : Bukkit.getOnlinePlayers()) if (pl != p) pl.hidePlayer(p);
					PlayerMove.onAirport.add(p);
					if (Config.get().contains("Tutorial.1") && Config.get().contains("Tutorial.2") && Config.get().contains("Tutorial.3")) Tutorial.tuto.put(p, 1);
				}
			}
			else if (args.length > 1 && args[0].equalsIgnoreCase("ab")) {
				if (args[1].equalsIgnoreCase("reload")) {
					Timer20T.reload = 6;
					return true;
				}
				String msg = null;
				for (String arg : args) {
					if (msg == null) msg = "";
					else msg += arg + " ";
				}
				msg = msg.substring(0, msg.length()-1);
				for (Player pl : Bukkit.getOnlinePlayers()) new ActionBar(pl).msg(msg);
			} else if (args.length > 0 && args[0].equalsIgnoreCase("weapon")) {
				Chat c = new Chat(p);
				if (args.length < 3) c.e("Syntax: /test weapon (weapon) (item/bullet)");
				else {
					Weapons wep = null;
					for (Weapons weap : Weapons.values()) {
						if (weap.toString().equalsIgnoreCase(args[1])) {
							wep = weap;
							break;
						}
					}
					if (wep == null) c.e("Could not find weapon " + args[1]);
					else if (args[2].equalsIgnoreCase("item")) p.getInventory().setItem(2, wep.item);
					else if (args[2].equalsIgnoreCase("bullet")) {
						ItemStack bullet = wep.bullet;
						bullet.setAmount(bullet.getMaxStackSize());
						p.getInventory().setItem(3, bullet);
					} else c.e("Syntax: /test weapon (weapon) (item/bullet)");
				}
			}
			else {
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
				p.getInventory().setItem(6, item);
			}
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
