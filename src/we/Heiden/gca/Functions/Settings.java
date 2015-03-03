package we.Heiden.gca.Functions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import we.Heiden.gca.Misc.Others;
import we.Heiden.gca.Misc.SettingsEnum;

public class Settings {

	public static void configure() {
		List<ItemStack> li = new ArrayList<ItemStack>();
		for (String s : SettingsEnum.GUN_RECHARGE_EFFECT.getValues()) li.add(greffect01(s));
		greffectList = li;
		
		li = new ArrayList<ItemStack>();
		for (String s : SettingsEnum.GUN_ZOOM.getValues()) {
			if (!s.equals("Off")) li.add(gzoom03(s));
			else li.add(gzoom02);
		}
		gzoomList = li;
		
		li = new ArrayList<ItemStack>();
		for (String s : SettingsEnum.QUALITY.getValues()) {
			li.add(quality01(s));
		}
		qualityList = li;
	}
	
	public static ItemStack gspartic01 = Others.getItem(Material.INK_SACK, (short) 1, "&6Gun Shot Particles &2(&aOn&2)",
			"Toggle Particles that", "follows every shot`s visibility");
	public static ItemStack gspartic02 = Others.getItem(Material.INK_SACK, 2, (short) 1, "&6Gun Shot Particles &4(&cOff&4)", 
			"Toggle Particles that", "follows every shot`s visibility");
	
	public static ItemStack greffect = Others.getItem(Material.INK_SACK, (short) 2, "&bGun Recharge Effect &5(&9%state%&5)", "Change animation when", "you recharge your guns");
	public static ItemStack greffect01(String state) {return replace(greffect, state);}
	
	public static ItemStack titles01 = Others.getItem(Material.PAPER, "&aTitles &2(&aOn&2)");
	public static ItemStack titles02 = Others.getItem(Material.PAPER, 2, (short) 0, "&aTitles &4(&cOff&4)");
	
	public static ItemStack hotbar01 = Others.getItem(Material.COMPASS, (short) 0,
			"&9Hotbar Messages &2(&aOn&2)", "Toggle Hotbar Messages", "&4Warning: &fSome messages", "&fWill still being displayed");
	public static ItemStack hotbar02 = Others.getItem(Material.COMPASS, 2, (short) 1, 
			"&9Hotbar Messages &4(&cOff&4)", "Toggle Hotbar Messages", "&4Warning: &fSome messages", "&fWill still being displayed");
	
	public static ItemStack gzoom01 = Others.getItem(Material.INK_SACK, (short) 3, 
			"&eGun Zoom &2(&a%state%&2)", "Toggles Zoom when you", "use zoom on your guns", "&fWill not affect", "&fNight Vision");
	public static ItemStack gzoom02 = Others.getItem(Material.INK_SACK, 2, (short) 3, 
			"&eGun Zoom &4(&cOff&4)", "Toggles Zoom when you", "use zoom on your guns", "&fWill not affect", "&fNight Vision");
	public static ItemStack gzoom03(String state) {return replace(gzoom01, state);}
	
	public static ItemStack pvisibility01 = Others.getItem(Material.MONSTER_EGG, (short) 95, "&dPet Visibility &2(&aOn&2)");
	public static ItemStack pvisibility02 = Others.getItem(Material.MONSTER_EGG, 2, (short) 95, "&dPet Visibility &4(&cOff&4)");
	
	public static ItemStack eparticles01 = Others.getItem(Material.FIREWORK, "&fExtra Particles &2(&aOn&2)", "Toggles All particles", "Generated by plugin");
	public static ItemStack eparticles02 = Others.getItem(Material.FIREWORK, 2, (short) 0, "&fExtra Particles &4(&cOff&4)", "Toggles All particles", "Generated by plugin");
	
	public static ItemStack quality = Others.getItem(Material.WATCH,
			"&cQuality &5(&9%state%&5)", "Changes Quality of", "Resource Pack", "&cWarning: &fThis means", "&fDownloading another", "&fResource Pack!");
	public static ItemStack quality01(String state) {return replace(quality, state);}
	
	public static ItemStack[] gsparticles = {gspartic01, gspartic02};
	public static List<ItemStack> greffectList;
	public static ItemStack[] titles = {titles01, titles02};
	public static ItemStack[] hotbars = {hotbar01, hotbar02};
	public static List<ItemStack> gzoomList;
	public static ItemStack[] pvisibilities = {pvisibility01, pvisibility02};
	public static ItemStack[] eparticles = {eparticles01, eparticles02};
	public static List<ItemStack> qualityList;
	
	public static ItemStack replace(ItemStack i, String state) {
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(im.getDisplayName().replace("%state%", state));
		i.setItemMeta(im);
		return i;
	}
	
	public static void display(Player p) {
		Inventory inv = Bukkit.createInventory(null, 18, name());
		
		SettingsEnum.setPlayer(p);
		String set01 = SettingsEnum.GUN_SHOT_PARTICLES.getValue();
		String set02 = SettingsEnum.GUN_RECHARGE_EFFECT.getValue();
		String set03 = SettingsEnum.TITLES.getValue();
		String set04 = SettingsEnum.HOTBAR.getValue();
		String set05 = SettingsEnum.GUN_ZOOM.getValue();
		String set06 = SettingsEnum.PET_VISIBILITY.getValue();
		String set07 = SettingsEnum.EXTRA_PARTICLES.getValue();
		String set08 = SettingsEnum.QUALITY.getValue();

		ItemStack i01;
		ItemStack i02 = greffect01(set02);
		ItemStack i03;
		ItemStack i04;
		ItemStack i05;
		ItemStack i06;
		ItemStack i07;
		ItemStack i08 = quality01(set08);
		
		if (set01.equals("On")) i01 = gspartic01; else i01 = gspartic02;
		if (set03.equals("On")) i03 = titles01; else i03 = titles02;
		if (set04.equals("On")) i04 = hotbar01; else i04 = hotbar02;
		if (set05.equals("Off")) i05 = gzoom02; else i05 = gzoom03(set05);
		if (set06.equals("On")) i06 = pvisibility01; else i06 = pvisibility02;
		if (set07.equals("On")) i07 = eparticles01; else i07 = eparticles02;
		
		for (int n = 1; n <= inv.getSize(); n++) inv.setItem(n-1, Others.ItemDefault());
		
		inv.setItem(0, i01);
		inv.setItem(10, i02);
		inv.setItem(2, i03);
		inv.setItem(12, i04);
		inv.setItem(14, i05);
		inv.setItem(6, i06);
		inv.setItem(16, i07);
		inv.setItem(8, i08);
		
		p.closeInventory();
		p.openInventory(inv);
	}

	public static String name() {return ChatColor.translateAlternateColorCodes('&', "&f&lSettings");}
	public static String greffectsName() {return ChatColor.translateAlternateColorCodes('&', "&bGun Recharge Effects");}
	
	public static void greffects(Player p) {extraMenu(p, null, greffectsName(),
			Others.getItem(Material.INK_SACK, (short) 2, "&bGun Recharge Effects &d(&9%state%&d)", "Change animation when", "you recharge your guns"), "High", "Med", "Off");}
	
	public static void extraMenu(Player p, List<Integer> slot, String title, ItemStack i, String... values) {
		Inventory inv = Bukkit.createInventory(null, 9, title);
		int length = values.length;
		List<Integer> slots = new ArrayList<Integer>();
		if (slot == null) {
			if (length/2*2 != length) {
				if (length > 4) {slots.add(0); slots.add(2);slots.add(4); slots.add(6); slots.add(8);}
				else if (length > 2) {slots.add(2); slots.add(4); slots.add(6);}
				else slots.add(4);
			} else {
				if (length > 3) {slots.add(1); slots.add(3); slots.add(5); slots.add(7);}
				else slots.add(3); slots.add(5);
			}
		} else slots = slot;
		int n = 1;
		for (int s : slots) {
			ItemStack is = i.clone();
			is.setAmount(n);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(im.getDisplayName().replace("%state%", values[n-1]));
			is.setItemMeta(im);
			inv.setItem(s, is);
			n++;
		}
		p.closeInventory();
		p.openInventory(inv);
	}
}
