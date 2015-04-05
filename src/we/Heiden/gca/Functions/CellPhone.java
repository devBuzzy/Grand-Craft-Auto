package we.Heiden.gca.Functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import we.Heiden.gca.Configs.PlayerConfig;
import we.Heiden.gca.Utils.Displayable;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.gca.core.Timer20T;
import we.Heiden.hs2.Core.Rank;
import we.Heiden.hs2.Messages.ActionBar;
import we.Heiden.hs2.Messages.Chat;
import we.Heiden.hs2.SQL.Operations;

public class CellPhone implements Displayable {

	public static String agendN = ChatColor.translateAlternateColorCodes('&', "&a&l&oAgend");
	public static String contactN = ChatColor.translateAlternateColorCodes('&', "&b&oContact &a");
	private static String prefix = ChatColor.translateAlternateColorCodes('&', "&a&o");
	public static ItemStack call = ItemUtils.getItem(Material.NETHER_STAR, "&9&lCall");
	public static ItemStack message = ItemUtils.getItem(Material.PAPER, "&e&oSend Message");
	public static ItemStack inbox = ItemUtils.getItem(Material.GLOWSTONE_DUST, "&bInbox");
	public static ItemStack outbox = ItemUtils.getItem(Material.REDSTONE, "&cOutbox");
	private static HashMap<Player, HashMap<Integer, String>> players = new HashMap<Player, HashMap<Integer, String>>();
	private static HashMap<Player, Integer> InvPage = new HashMap<Player, Integer>();
	private static HashMap<Player, String> selected = new HashMap<Player, String>();
	public static HashMap<Player, Player> calling = new HashMap<Player, Player>();
	public static HashMap<Player, Integer> calling2 = new HashMap<Player, Integer>();
	
	public static void agend(Player p, int page) {
		if (page < 1) page = 1;
		List<String> friends = getFriends(p);
		Inventory inv = Bukkit.createInventory(null, 27, agendN);
		int start = 0;
		int end = friends.size()-1;
		if (friends.size() > 27) {
			int pages = friends.size()/27+1;
			if (page > pages) page = pages;
			start = -25+page*25;
			end = start+24;
			if (pages != page) inv.setItem(26, ItemUtils.Next());
			if (page != 1) inv.setItem(18, ItemUtils.Previous());
		}
		for (int current = 0; current <= end; current++) {
			if (friends.get(start+current) == null) break;
			int slot = current-start;
			if (slot > 17) slot++;
			String player = friends.get(start+current);
			UUID uuid = null;
			try { uuid = Operations.getUUID(player); } catch(Exception ex) { }
			String rname = "";
			if (uuid != null) { Rank rank = Rank.getRank(uuid); rname = rank.Name() + "► " + rank.nameColor; }
			ItemStack i = ItemUtils.getItem(Material.SKULL_ITEM, (short) 3, prefix + rname + friends.get(start+current));
			SkullMeta sm = (SkullMeta) i.getItemMeta();
			int friend = start+current;
			if (friend < 2) sm.setLore(Arrays.asList(t("&b&oComming Soon")));
			else sm.setLore(Arrays.asList("Click to Select"));
			sm.setOwner(player);
			i.setItemMeta(sm);
			if (!players.containsKey(p)) players.put(p, new HashMap<Integer, String>());
			players.get(p).put(slot, player);
			inv.setItem(slot, i);
		}
		
		p.openInventory(inv);
		InvPage.put(p, page);
	}
	
	public static List<String> getFriends(Player p) {
		List<String> ls = new ArrayList<String>();
		ls.addAll(Arrays.asList("Leaster", "Moe"));
		if (PlayerConfig.get(p).contains("Friends")) ls.addAll(PlayerConfig.get().getStringList("Friends"));
		return ls;
	}
	
	private static String t(String str) { return ChatColor.translateAlternateColorCodes('&', str); }
	public void display(Player p) { agend(p, InvPage.get(p)); }
	
	public static void select(Player p, int slot) {
		if (!players.containsKey(p) || !players.get(p).containsKey(slot)) new Chat(p).e("Unexpected Exception, Try Again");
		else if (InvPage.get(p) == 1 && slot < 2) new Chat(p).s("Coming Soon");
		else {
			String target = players.get(p).get(slot);
			players.remove(p);
			selected.put(p, target);
			
			Inventory inv = Bukkit.createInventory(null, 9, contactN + target);
			
			for (int n = 0; n < inv.getSize(); n++) inv.setItem(n, ItemUtils.ItemDefault());
			inv.setItem(1, call);
			inv.setItem(3, message);
			inv.setItem(5, inbox);
			inv.setItem(7, outbox);
			
			p.openInventory(inv);
		}
	}

	public static void next(Player p) { agend(p, InvPage.get(p)+1); }
	public static void previous(Player p) { agend(p, InvPage.get(p)-1); }
	
	public static void call(Player p) {
		Chat c = new Chat(p);
		String targetName = selected.get(p);
		Player target = Bukkit.getPlayer(targetName);
		if (target == null) c.e(targetName + "'s cellphone is off");
		else if (calling.containsKey(target)) c.e(targetName + " is unavaible");
		else {
			p.closeInventory();
			if (!Timer20T.actionBar.containsKey(p)) {
				Timer20T.actionBar.put(p, "&b&oCalling");
				new ActionBar(p).msg("&b&oCalling");
			} else {
				calling.put(p, target);
				calling.put(target, p);
				calling2.put(p, 20);
				c.s("Calling");
			}
		}
	}
}
