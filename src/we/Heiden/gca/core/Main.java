package we.Heiden.gca.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import we.Heiden.gca.Commands.CommandsHandler;
import we.Heiden.gca.Commands.SetnpcCommand;
import we.Heiden.gca.Configs.Config;
import we.Heiden.gca.Events.EntityExplode;
import we.Heiden.gca.Events.EventsHandler;
import we.Heiden.gca.NPCs.CustomVillager;
import we.Heiden.gca.NPCs.NMSNpc;
import we.Heiden.gca.NPCs.NPCs;
import we.Heiden.gca.NPCs.PolicesNMS;
import we.Heiden.gca.Pets.Ocelot;
import we.Heiden.gca.Pets.Wolf;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.gca.Utils.UtilsMain;
import we.Heiden.gca.Weapons.Weapons;
import we.Heiden.hs2.Utils.Functions;

/**
 * *********************************************
 * <p>
 * <b>This has been made by <i>Heiden Team</b>
 * <ul>
 * <li>Don't claim this class as your own
 * <li>Don't remove this disclaimer
 * </ul>
 * <b>All rights reserved
 * <p>
 * Heiden Team 2015
 * <p>
 * </b> *********************************************
 **/
public class Main extends JavaPlugin {

	public static Plugin pl;

	public void onEnable() {
		pl = this;
		new Config().defaults();
		UtilsMain.setup();
		ItemUtils.items();
		UtilsMain.load();
		new EventsHandler(this);
		new Timer1T().runTaskTimer(this, 20, 1);
		new Timer2T().runTaskTimer(this, 20, 2);
		new Timer5T().runTaskTimer(this, 20, 5);
		new Timer20T().runTaskTimer(this, 20, 20);
		new Timer600T().runTaskTimer(this, 20, 600);
	}

	@SuppressWarnings("deprecation")
	public void onDisable() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			for (Player pl : Bukkit.getOnlinePlayers())
				if (!p.canSee(pl))
					p.showPlayer(pl);
			if (p.getOpenInventory() != null)
				p.closeInventory();
		}
		UtilsMain.save();
		for (NPCs npcs : NPCs.npcs.keySet())
			for (NMSNpc npc : NPCs.npcs.get(npcs).keySet())
				if (npc != null)
					npc.killEntityNMS();
		for (CustomVillager e : SetnpcCommand.villagers.keySet())
			e.killEntityNMS();
		for (World w : Bukkit.getWorlds()) {
			for (Entity e : w.getEntities()) {
				if (e instanceof Wolf)
					((Wolf) e).killEntityNMS();
				else if (e instanceof Ocelot)
					((Ocelot) e).killEntityNMS();
			}
		}
		for (LivingEntity e : le) e.setHealth(0);
		
		for (Entity e : EntityExplode.restore.keySet()) {
			if (Config.get().contains("Temp." + e.getUniqueId())) for (String s : Config.get().getConfigurationSection("Temp." + e.getUniqueId()).getKeys(false)) {
				Location loc = we.Heiden.gca.Utils.Functions.loadLoc("Temp." + e.getUniqueId() + "." + s + ".Location", Config.get());
				loc.getBlock().setType(Material.matchMaterial(Config.get().getString("Temp." + e.getUniqueId() + "." + s + ".Type")));
				loc.getBlock().setData((byte) Config.get().getInt("Temp." + e.getUniqueId() + "." + s + ".Data"));
			}
			Config.get().set("Temp." + e.getUniqueId(), null);
			Config.save();
			EntityExplode.restore.remove(e);
		}
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String CommandLabel, String[] args) {
		if (args.length == 2 && args[0].equalsIgnoreCase("weapon")) {
			List<String> ls = new ArrayList<String>();
			for (Weapons wep : Weapons.values())
				ls.add(Functions.normalize(wep.toString()));
			return ls;
		}
		return null;
	}
	
	private static List<LivingEntity> le = new ArrayList<LivingEntity>();

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		new CommandsHandler(sender, cmd, args);
		if (cmd.getName().equalsIgnoreCase("test") && sender instanceof Player) {
			Player p = (Player) sender;
			le.add((LivingEntity) new PolicesNMS(p.getLocation(), p).getBukkitEntity());
		}
		return true;
	}
}
