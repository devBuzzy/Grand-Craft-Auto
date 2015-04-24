package we.Heiden.gca.Functions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import we.Heiden.gca.Configs.PlayerConfig;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.Utils.Displayable;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.gca.core.Timer20T;
import we.Heiden.hs2.Core.Rank;
import we.Heiden.hs2.Messages.ActionBar;
import we.Heiden.hs2.Messages.Chat;
import we.Heiden.hs2.SQL.Operations;

public class CellPhone implements Displayable {

	public static String agendN = ChatColor.translateAlternateColorCodes('&',
			"&a&l&oAgenda");
	public static String inboxN = ChatColor.translateAlternateColorCodes('&',
			"&c&oInbox");
	public static String outboxN = ChatColor.translateAlternateColorCodes('&',
			"&6&oOutbox");
	public static String contactN = ChatColor.translateAlternateColorCodes('&',
			"&b&oContact &a");
	private static String prefix = ChatColor.translateAlternateColorCodes('&',
			"&a&o");
	public static ItemStack call = ItemUtils.getItem(Material.NETHER_STAR,
			"&9&lCall");
	public static ItemStack message = ItemUtils.getItem(Material.PAPER,
			"&e&oSend Message");
	public static ItemStack inbox = ItemUtils.getItem(Material.GLOWSTONE_DUST,
			"&bInbox");
	public static ItemStack outbox = ItemUtils.getItem(Material.REDSTONE,
			"&cOutbox");

	/** Controlar! **/
	private static HashMap<Player, HashMap<Integer, String>> players = new HashMap<Player, HashMap<Integer, String>>();
	private static HashMap<Player, Integer> InvPage = new HashMap<Player, Integer>();
	private static HashMap<Player, String> selected = new HashMap<Player, String>();

	public static HashMap<Player, Player> calling = new HashMap<Player, Player>();
	public static HashMap<Player, Integer> calling2 = new HashMap<Player, Integer>();
	public static List<Player> cancelDisplay = new ArrayList<Player>();
	public static HashMap<Player, Player> onCall = new HashMap<Player, Player>();
	public static List<Player> callOwner = new ArrayList<Player>();
	public static List<Player> openOther = new ArrayList<Player>();

	public static HashMap<Player, String> msg = new HashMap<Player, String>();
	private static HashMap<Player, Integer> SecPage = new HashMap<Player, Integer>();

	public static void agend(Player p, int page) {
		if (cancelDisplay.contains(p)) {
			cancelDisplay.remove(p);
			if (calling.containsKey(p)
					&& !cancelDisplay.contains(calling.get(p))) {
				calling2.remove(calling.get(p));
				Player target = calling.get(p);
				calling.remove(p);
				calling.remove(target);
				onCall.put(p, target);
				onCall.put(target, p);
				callOwner.add(target);
				new Chat(p).msg("&b&oOn Call with " + target.getName(),
						"  &9&oWrite on chat to speak");
				new Chat(target).msg("&b&oOn Call with " + p.getName(),
						"  &9&oWrite on chat to speak");
			}
			return;
		} else if (onCall.containsKey(p)) {
			Player target = onCall.get(p);
			onCall.remove(p);
			onCall.remove(target);
			Messager.load(p);
			Messager.s("Call Ended");
			Messager.load(target);
			Messager.s(p.getName() + " had ended the call");
		} else {
			if (page < 1) page = 1;
			List<String> friends = getFriends(p);
			Inventory inv = Bukkit.createInventory(null, 27, agendN);
			int start = 0;
			int end = friends.size() - 1;
			if (friends.size() > 27) {
				int pages = friends.size() / 27 + 1;
				if (page > pages)
					page = pages;
				start = -25 + page * 25;
				end = start + 24;
				if (pages != page)
					inv.setItem(26, ItemUtils.Next());
				if (page != 1)
					inv.setItem(18, ItemUtils.Previous());
			}
			for (int current = 0; current <= end; current++) {
				if (friends.get(start + current) == null)
					break;
				int slot = current - start;
				if (slot > 17)
					slot++;
				String player = friends.get(start + current);
				UUID uuid = null;
				try {
					uuid = Operations.getUUID(player);
				} catch (Exception ex) {
				}
				String rname = "";
				if (uuid != null) {
					Rank rank = Rank.getRank(uuid);
					rname = rank.Name() + "► " + rank.nameColor;
				}
				ItemStack i = ItemUtils.getItem(Material.SKULL_ITEM, (short) 3,
						prefix + rname + friends.get(start + current));
				SkullMeta sm = (SkullMeta) i.getItemMeta();
				int friend = start + current;
				if (friend < 2)
					sm.setLore(Arrays.asList(t("&b&oComming Soon")));
				else
					sm.setLore(Arrays.asList("Click to Select"));
				sm.setOwner(player);
				i.setItemMeta(sm);
				if (!players.containsKey(p))
					players.put(p, new HashMap<Integer, String>());
				players.get(p).put(slot, player);
				inv.setItem(slot, i);
			}

			p.openInventory(inv);
			InvPage.put(p, page);
		}
	}

	public static List<String> getFriends(Player p) {
		List<String> ls = new ArrayList<String>();
		ls.addAll(Arrays.asList("Leaster", "Moe"));
		if (PlayerConfig.get(p).contains("Friends"))
			ls.addAll(PlayerConfig.get().getStringList("Friends"));
		return ls;
	}

	private static String t(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}

	public void display(Player p) {
		agend(p, InvPage.get(p));
	}

	public static void select(Player p, int slot) {
		if (!players.containsKey(p) || !players.get(p).containsKey(slot))
			new Chat(p).e("Unexpected Exception, Try Again");
		else if (InvPage.get(p) == 1 && slot < 2)
			new Chat(p).s("Coming Soon");
		else {
			String target = players.get(p).get(slot);
			players.remove(p);
			select(p, target);
		}
	}

	public static void select(Player p, String target) {
		selected.put(p, target);
		Inventory inv = Bukkit.createInventory(null, 9, contactN + target);

		for (int n = 0; n < inv.getSize(); n++)
			inv.setItem(n, ItemUtils.ItemDefault());
		inv.setItem(1, call);
		inv.setItem(3, message);
		inv.setItem(5, inbox);
		inv.setItem(7, outbox);

		openOther.add(p);
		p.openInventory(inv);
		openOther.remove(p);
	}

	public static void next(Player p) {
		agend(p, InvPage.get(p) + 1);
	}

	public static void previous(Player p) {
		agend(p, InvPage.get(p) - 1);
	}

	public static void select(Player p) {
		select(p, selected.get(p));
		SecPage.remove(p);
	}

	public static void call(Player p) {
		if (calling.containsKey(p) || onCall.containsKey(p))
			return;
		Chat c = new Chat(p);
		String targetName = selected.get(p);
		Player target = Bukkit.getPlayer(targetName);
		double bank = Functions.getBankMoney(p);
		if (target == null)
			c.e(targetName + "'s cellphone is off");
		else if (calling.containsKey(target) || onCall.containsKey(target))
			c.e(targetName + " is unavaible");
		else if (bank < 3)
			c.msg(Chat.e2("You don`t have enough coins on your account"),
					"  &9&oDeposit it on the bank");
		else {
			Functions.setBankMoney(p, bank - 3);
			cancelDisplay.add(p);
			cancelDisplay.add(target);
			p.closeInventory();
			clear(p);
			if (!Timer20T.actionBar.containsKey(p)) {
				Timer20T.actionBar.put(p, "&b&oCalling");
				new ActionBar(p).msg("&b&oCalling");
			} else
				c.s("Calling");
			calling.put(p, target);
			calling.put(target, p);
			calling2.put(p, 40);
		}
	}

	public static void message(Player p) {
		Chat c = new Chat(p);
		String targetName = selected.get(p);
		Player target = Bukkit.getPlayer(targetName);
		UUID uuid = null;
		double bank = Functions.getBankMoney(p);
		if (target == null) {
			try {
				uuid = Operations.getUUID(targetName);
			} catch (Exception ex) {
			}
		} else
			uuid = target.getUniqueId();
		if (uuid == null)
			c.e("Can`t find " + targetName);
		else if (bank < 5)
			c.msg(Chat.e2("You don`t have enough coins on your account"),
					"  &9&oDeposit it on the bank");
		else {
			cancelDisplay.add(p);
			p.closeInventory();
			clear(p);
			msg.put(p, "");
			new Chat(p).msg("&6&oWrite your message on chat!",
					" &c&o* &9use &2\"\\n\" &9to add a line",
					" &c&o* &2.send &9to send the message",
					" &c&o* &2.exit &9to Cancel");
		}
	}

	public static void sendMessage(Player p) {
		String message = msg.get(p);
		String targetName = selected.get(p);
		Player target = Bukkit.getPlayer(targetName);
		double bank = Functions.getBankMoney(p);
		UUID uuid = null;
		SimpleDateFormat format = new SimpleDateFormat("MM dd HH mm ss:");
		String date = format.format(new Date());

		if (target == null) {
			try { uuid = Operations.getUUID(targetName);
			} catch (Exception ex) { Bukkit.broadcastMessage("oops"); }
		} else uuid = target.getUniqueId();

		Functions.setBankMoney(p, bank - 5);
		msg.remove(p);

		List<String> messages = (PlayerConfig.get(uuid).contains(
				"Inbox." + p.getName()) ? PlayerConfig.get().getStringList(
				"Inbox." + p.getName()) : new ArrayList<String>());
		messages.add(message);
		PlayerConfig.get().set("Inbox." + p.getName(), date + messages);
		PlayerConfig.save();
		PlayerConfig.get(p).set("Outbox." + targetName, date + messages);
		Messager.load(p);
		Messager.s1("Message Successfully sent");
	}

	public static void inbox(Player p, int page) {
		List<String> messages = (PlayerConfig.get(p).contains(
				"Inbox." + selected.get(p)) ? PlayerConfig.get().getStringList(
				"Inbox." + selected.get(p)) : null);
		Chat c = new Chat(p);
		if (messages == null)
			c.e("That inbox is empty");
		else {
			if (page < 1)
				page = 1;
			int size = (messages.size() / 9 * 9) + 1;
			if (size > 5)
				size = 5;
			Inventory inv = Bukkit.createInventory(null, size * 9, inboxN);
			int min = 0, max = min + inv.getSize();
			if (messages.size() > inv.getSize()) {
				int pages = messages.size() / 27 + 1;
				if (page > pages)
					page = pages;
				if (pages != page)
					inv.setItem(inv.getSize() - 1, ItemUtils.Next());
				if (page != 1)
					inv.setItem(inv.getSize() - 9, ItemUtils.Previous());
				min = (inv.getSize() - 2) * (page - 1);
				max = min + inv.getSize() - 2;
			}
			ItemStack paper = new ItemStack(Material.PAPER);
			ItemMeta paperMeta = paper.getItemMeta();
			for (int minClone = min; minClone <= max; minClone++) {
				if (messages.size() - 1 < minClone)
					return;
				String[] original = messages.get(minClone).split(":");
				String message = original[1];
				String[] date = original[0].split(" ");
				ItemMeta im = paperMeta.clone();
				im.setDisplayName(t("&b&oDate: &6" + date[1] + "&e/&6"
						+ date[0] + " " + date[2] + "&e:&6" + date[3] + "&e:&6"
						+ date[4]));
				List<String> lore = new ArrayList<String>();
				for (String l : message.split("\n"))
					lore.add(l);
				im.setLore(lore);
				ItemStack item = paper.clone();
				item.setItemMeta(im);
				int slot = minClone - min;
				if (slot >= inv.getSize() - 9)
					slot++;
				inv.setItem(slot, item);
			}
			SecPage.put(p, page);
			openOther.add(p);
			p.openInventory(inv);
			openOther.remove(p);
		}
	}

	public static void outbox(Player p, int page) {
		List<String> messages = (PlayerConfig.get(p).contains(
				"Outbox." + selected.get(p)) ? PlayerConfig.get()
				.getStringList("Outbox." + selected.get(p)) : null);
		Chat c = new Chat(p);
		if (messages == null)
			c.e("That outbox is empty");
		else {
			if (page < 1)
				page = 1;
			int size = (messages.size() / 9 * 9) + 1;
			if (size > 5)
				size = 5;
			Inventory inv = Bukkit.createInventory(null, size * 9, outboxN);
			int min = 0, max = min + inv.getSize();
			if (messages.size() > inv.getSize()) {
				int pages = messages.size() / 27 + 1;
				if (page > pages)
					page = pages;
				if (pages != page)
					inv.setItem(inv.getSize() - 1, ItemUtils.Next());
				if (page != 1)
					inv.setItem(inv.getSize() - 9, ItemUtils.Previous());
				min = (inv.getSize() - 2) * (page - 1);
				max = min + inv.getSize() - 2;
			}
			ItemStack paper = new ItemStack(Material.PAPER);
			ItemMeta paperMeta = paper.getItemMeta();
			for (int minClone = min; minClone <= max; minClone++) {
				if (messages.size() - 1 < minClone)
					return;
				String[] original = messages.get(minClone).split(":");
				String message = original[1];
				String[] date = original[0].split(" ");
				ItemMeta im = paperMeta.clone();
				im.setDisplayName(t("&b&oDate: &6" + date[1] + "&e/&6"
						+ date[0] + " " + date[2] + "&e:&6" + date[3] + "&e:&6"
						+ date[4]));
				List<String> lore = new ArrayList<String>();
				for (String l : message.split("\n"))
					lore.add(l);
				im.setLore(lore);
				ItemStack item = paper.clone();
				item.setItemMeta(im);
				int slot = minClone - min;
				if (slot >= inv.getSize() - 9)
					slot++;
				inv.setItem(slot, item);
			}
			SecPage.put(p, page);
			openOther.add(p);
			p.openInventory(inv);
			openOther.remove(p);
		}
	}

	public static void nextInbox(Player p) {
		inbox(p, SecPage.get(p) + 1);
	}

	public static void previousInbox(Player p) {
		inbox(p, SecPage.get(p) - 1);
	}

	public static void nextOutbox(Player p) {
		outbox(p, SecPage.get(p) + 1);
	}

	public static void previousOutbox(Player p) {
		outbox(p, SecPage.get(p) - 1);
	}

	public static void clear(Player p) {
		players.remove(p);
		InvPage.remove(p);
		selected.remove(p);
		SecPage.remove(p);
	}
}
