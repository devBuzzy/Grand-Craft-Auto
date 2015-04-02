package we.Heiden.gca.Commands;

import java.util.HashMap;

import org.bukkit.entity.Player;

import we.Heiden.gca.Configs.Config;
import we.Heiden.gca.NPCs.CustomVillager;
import we.Heiden.gca.NPCs.NPCs;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.hs2.Messages.Chat;

public class SetnpcCommand {

	public static HashMap<CustomVillager, String> villagers = new HashMap<CustomVillager, String>();
	public static HashMap<String, Integer> respawn = new HashMap<String, Integer>();

	public static void setNpc(Player p, String[] args) {
		Chat c = new Chat(p);
		if (args.length < 1) {
			c.e("Specify a Npc Type");
			c.msg("&9&oTypes: &6Civ, Car, Homes, Pet, Bank, Clerk");
		} if (args[0].equalsIgnoreCase("civ")) {
			int size = 1;
			if (Config.get().contains("Civilians")) size = Config.get().getConfigurationSection("Civilians").getKeys(false).size()+1;
			if (size >= 30) c.msg(Chat.e2("Max Civilian Amount Reached"), "&9&lTry removing them from config");
			else {
				Functions.saveLoc("Civilians." + size, p);
				Config.save();
				villagers.put(CustomVillager.spawn(p.getLocation(), "&a&lCivilian"), "Civilians." + size);
				c.s("Civilian Set");
			}
		} else if (we.Heiden.hs2.Utils.Functions.equals(args[0], "car", "homes", "pet", "bank", "clerk")) {
			NPCs npc = null;
			if (args[0].equalsIgnoreCase("car")) npc = NPCs.Cars;
			else if (args[0].equalsIgnoreCase("homes")) npc = NPCs.Homes;
			else if (args[0].equalsIgnoreCase("pet")) npc = NPCs.Pets;
			else if (args[0].equalsIgnoreCase("bank")) npc = NPCs.Bank;
			else npc = NPCs.Store;
			npc.getNPC().spawn(p);
			String path = "NPCs." + we.Heiden.hs2.Utils.Functions.normalize(args[0]);
			int size = 1;
			if (Config.get().contains(path)) size = Config.get().getConfigurationSection(path).getKeys(false).size()+1;
			Functions.saveLoc(path + "." + size, p);
			Config.save();
			c.s("NPC Set");
		} else c.e("Invalid Type");
	}
}
