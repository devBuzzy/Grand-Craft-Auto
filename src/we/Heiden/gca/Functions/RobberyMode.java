package we.Heiden.gca.Functions;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.NPCs.NMSNpc;
import we.Heiden.gca.NPCs.PolicesNMS;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.hs2.Messages.Chat;

public class RobberyMode {

	public static HashMap<Player, Integer> PoliceDelay = new HashMap<Player, Integer>();
	public static HashMap<Player, Item> Reward = new HashMap<Player, Item>();
	public static HashMap<Player, Location> robbery = new HashMap<Player, Location>();
	public static HashMap<Player, Integer> robbery2 = new HashMap<Player, Integer>();

	public static void start(Player p, NMSNpc npc) {
		new Chat(p).msg(npc.getName()
				+ "&6►► &cDon`t hurt me! &2I`ll give you all I have!");
		if (!Polices.wanted.containsKey(p))
			Polices.wanted.put(p, 0D);
		double wanted = Polices.wanted.get(p) + 2;
		if (wanted > 5)
			wanted = 5;
		Polices.wanted.put(p, wanted);
		PoliceDelay.put(p, 7);
		Location npcLoc = new Location((World) npc.getWorld().getWorld(),
				npc.locX, npc.locY, npc.locZ);
		Vector vec = p.getLocation().toVector().subtract(npcLoc.toVector());
		int money = new Random().nextInt(799) + 201;
		ItemStack reward = ItemUtils.getItem(Material.CHEST, "&c&oStore Money",
				"&6&oChest Money: &9" + money + " coins");
		Item item = p.getWorld().dropItem(npcLoc, reward);
		item.setVelocity(vec);
		Reward.put(p, item);
		robbery.put(p, npcLoc);
		robbery2.put(p, 20);
	}

	public static void success(Player p) {
		clear(p);
		Messager.load(p);
		Messager.msg("&aRobbery Status: &b&oSuccessed",
				"&c&oStill under Development");
	}

	public static void busted(Player p) {
		clear(p);
		Messager.load(p);
		Messager.msg("&aRobbery Status: &c&lBusted",
				"&d&oStill under Development");
	}

	public static void logOut(Player p) {
		clear(p);
		Bukkit.broadcastMessage(p.getName() + " Runned out of a Robbery!");
	}

	private static void clear(Player p) {
		PoliceDelay.remove(p);
		robbery.remove(p);
		robbery2.remove(p);
		Polices.wanted.remove(p);
		if (Polices.players.containsKey(p))
			for (PolicesNMS police : Polices.players.get(p)) {
				Polices.polices.remove(police);
				police.die();
			}
		Polices.players.remove(p);
		if (Reward.containsKey(p)) {
			if (!Reward.get(p).isDead())
				Reward.get(p).remove();
			Reward.remove(p);
		}
	}
}
