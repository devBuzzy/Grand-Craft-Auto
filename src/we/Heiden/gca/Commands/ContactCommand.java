package we.Heiden.gca.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import we.Heiden.gca.Configs.PlayerConfig;
import we.Heiden.hs2.Messages.Chat;
import we.Heiden.hs2.SQL.Operations;
import we.Heiden.hs2.Utils.Functions;

public class ContactCommand {

	public static void contact(Player p, String[] args) {
		Chat c = new Chat(p);
		if (args.length < 2) c.e("Syntax: /contact &2(&4add&2/&4remove&2) &c<Player>");
		else if (Functions.equals(args[0], "add", "remove")) {
			UUID uuid = null;
			Player target = Bukkit.getPlayer(args[1]);
			if (target == null) { try { uuid = Operations.getUUID(args[1]); } catch(Exception ex) { } }
			else uuid = target.getUniqueId();
			if (uuid == null) c.e("Could not find " + args[1]);
			else if (uuid.equals(p.getUniqueId()) && args[0].equals("add")) c.e("You can`t add yourself");
			else {
				List<String> friends = (PlayerConfig.get(p).contains("Friends") ? PlayerConfig.get().getStringList("Friends") : new ArrayList<String>());
				if (args[0].equalsIgnoreCase("add")) {
					boolean bol = true;
					for (String friend : friends) if (friend.toLowerCase().equals(args[1].toLowerCase())) { bol = false; break; }
					if (bol) friends.add(args[1]);
					else { c.e(args[1] + " already is on your Agenda"); return; }
				} else if (args[0].equalsIgnoreCase("remove")) {
					if (friends.contains(args[1])) friends.remove(args[1]);
					else { c.e(args[1] + " is not on your Agenda"); return; }
				}
				PlayerConfig.get().set("Friends", friends);
				PlayerConfig.save();
				c.s("Agenda Updated");
			}
		} else c.e("Syntax: /contact &2(&4add&2/&4remove&2) &c<Player>");
	}
}
