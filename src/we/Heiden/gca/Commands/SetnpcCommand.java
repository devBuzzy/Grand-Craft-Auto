package we.Heiden.gca.Commands;

import java.util.HashMap;

import org.bukkit.entity.Player;

import we.Heiden.gca.Configs.Config;
import we.Heiden.gca.NPCs.CustomVillager;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.hs2.Messages.Chat;

public class SetnpcCommand {

	public static HashMap<CustomVillager, String> villagers = new HashMap<CustomVillager, String>();
	public static HashMap<String, Integer> respawn = new HashMap<String, Integer>();

	public static void setNpc(Player p, String[] args) {
		Chat c = new Chat(p);
		if (args.length < 1) c.e("Specify a Npc Type");
		if (args[0].equalsIgnoreCase("civ")) {
			int size = 1;
			if (Config.get().contains("Civilians")) size = Config.get().getConfigurationSection("Civilians").getKeys(false).size()+1;
			if (size >= 30) c.msg(Chat.e2("Max Civilian Amount Reached"), "&9&lTry removing them from config");
			else {
				Functions.saveLoc("Civilians." + size, p);
				Config.save();
				villagers.put(CustomVillager.spawn(p.getLocation(), "&a&lCivilian"), "Civilians." + size);
				c.s("Civilian Set");
			}
		} else c.e("I`m not done yet");
	}
}
