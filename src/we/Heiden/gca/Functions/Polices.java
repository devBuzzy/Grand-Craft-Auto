package we.Heiden.gca.Functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import we.Heiden.gca.NPCs.PolicesNMS;

public class Polices {

	public static HashMap<Player, Double> wanted = new HashMap<Player, Double>();
	public static HashMap<PolicesNMS, Player> polices = new HashMap<PolicesNMS, Player>();
	public static HashMap<Player, List<PolicesNMS>> players = new HashMap<Player, List<PolicesNMS>>();
	
	public static void spawn(Player p, int amount, int range) {
		for (int n = 1; n <= amount; n++) {
			int randX = new Random().nextInt(range * 2) - range;
			int randZ = new Random().nextInt(range * 2) - range;
			Location loc = p.getLocation().clone();
			loc.setX(loc.getX() + randX);
			loc.setZ(loc.getZ() + randZ);
			PolicesNMS police = new PolicesNMS(loc, p);
			polices.put(police, p);
			if (!players.containsKey(p)) players.put(p, new ArrayList<PolicesNMS>());
			players.get(p).add(police);
			if (RobberyMode.robbery2.containsKey(p)) {
				int left = RobberyMode.robbery2.get(p);
				left--;
				if (left > 0) RobberyMode.robbery2.put(p, left);
				else RobberyMode.success(p);
			}
		}
	}

	public static void teleportBack(PolicesNMS police, int range) {
		int randX = new Random().nextInt(range * 2) - range;
		int randZ = new Random().nextInt(range * 2) - range;
		Location loc = polices.get(police).getLocation().clone();
		loc.setX(loc.getX() + randX);
		loc.setZ(loc.getZ() + randZ);
		police.setLocation(loc);
	}
}
