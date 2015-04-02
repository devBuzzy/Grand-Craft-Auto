package we.Heiden.gca.Events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Functions.CarsEnum;
import we.Heiden.gca.Functions.Food;
import we.Heiden.gca.Functions.Houses;
import we.Heiden.gca.Functions.Settings;
import we.Heiden.gca.Functions.SettingsEnum;
import we.Heiden.gca.Functions.Trade;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.Pets.Pet;
import we.Heiden.gca.Pets.Pets;
import we.Heiden.gca.Stores.CarStore;
import we.Heiden.gca.Stores.ClerkStore;
import we.Heiden.gca.Stores.HomeStore;
import we.Heiden.gca.Stores.PetStore;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.gca.Weapons.Weapons;

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
public class InventoryClick implements Listener {
	
	public InventoryClick(Plugin pl) {Bukkit.getPluginManager().registerEvents(this, pl);}

	public static HashMap<String, SettingsEnum> TitlesMap = new HashMap<String, SettingsEnum>();
	public static HashMap<Player, Pet> pet = new HashMap<Player, Pet>();
	public static HashMap<Player, Pets> petEnum = new HashMap<Player, Pets>();
	
	public static boolean contains(ItemStack[] list, ItemStack i) {
		for (ItemStack is : list) if (is.equals(i)) return true;
		return false;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (e.getCurrentItem() != null) {
			ItemStack c = e.getCurrentItem();
			if (ItemUtils.getItem(p).contains(c)) {
				e.setCancelled(true);
				p.updateInventory();
				if (ItemUtils.yes.containsKey(p) && c.equals(ItemUtils.Yes()) || c.equals(ItemUtils.No())) {
					boolean bol = true;
					if (c.equals(ItemUtils.Yes())) ItemUtils.yes.get(p).yes(p);
					else if (c.equals(ItemUtils.No())) ItemUtils.yes.get(p).no(p);
					else bol = false;
					if (bol) ItemUtils.yes.remove(p);
				} else if (c.equals(ItemUtils.Wolf()) || c.equals(ItemUtils.Cat())) {
					p.closeInventory();
					Pets pets;
					if (c.equals(ItemUtils.Wolf())) pets = Pets.Wolf;
					else pets = Pets.Cat;
					if (!pet.containsKey(p) || !petEnum.get(p).equals(pets)) {
						if (pet.containsKey(p)) pet.get(p).killEntityNMS();
						Pet petEntity = pets.spawn(p);
						pet.put(p, petEntity);
						petEnum.put(p, pets);
					} else {
						pet.get(p).killEntityNMS();
						pet.remove(p);
						petEnum.remove(p);
					}
				}
			} else {
				String invn = e.getInventory().getName();
				if (invn.equals(Settings.name())) {
					if (e.getInventory().contains(c)) {
						e.setCancelled(true);
						if (!c.equals(ItemUtils.ItemDefault())) {
							if (contains(Settings.gsparticles, c)) Settings.menu01(p);
							else if (Settings.greffectList.contains(c)) Settings.menu02(p);
							else if (contains(Settings.titles, c)) Settings.menu03(p);
							else if (contains(Settings.hotbars, c)) Settings.menu04(p);
							else if (Settings.gzoomList.contains(c)) Settings.menu05(p);	
							else if (contains(Settings.pvisibilities, c)) Settings.menu06(p);
							else if (contains(Settings.eparticles, c)) Settings.menu07(p);
							else if (Settings.qualityList.contains(c)) Settings.menu08(p);
						}
					}
				} else if (Functions.contains(Settings.menuNames, invn) && e.getInventory().contains(c)) {
					e.setCancelled(true);
					if (c.hasItemMeta()) for (String s : Settings.menuNames) {
						if (c.getItemMeta().getDisplayName().startsWith(s)) {
							for (String value : TitlesMap.get(invn).getValues())
							if (c.getItemMeta().getDisplayName().equals(s + Settings.state().replace("%state%", value))) {
								SettingsEnum.setPlayer(p);
								TitlesMap.get(invn).setValue(value);
								p.closeInventory();
								break;
							}
							break;
						}
					}
				} else if (invn.equals(CarStore.PurchaseN())) {
					if (e.getInventory().contains(c)) {
						e.setCancelled(true);
						ItemStack item2 = c.clone();
						item2.setAmount(1);
						CarStore.buy(p, CarsEnum.shop2.get(item2));
					}
				} else if (invn.equals(CarStore.ManageN())) {
					if (e.getInventory().contains(c) && !c.equals(ItemUtils.ItemDefault())) {
						e.setCancelled(true);
						CarStore.ManageCar(p, c.getAmount()-1);
					}
				} else if (CarStore.invn.containsKey(p) && CarStore.invn.get(p).equals(invn)) {
					if (e.getInventory().contains(c)) {
						e.setCancelled(true);
						if (c.equals(ItemUtils.CarAuth())) {
							Messager.load(p);
							Messager.e1("This isn`t avaible yet");
							p.closeInventory();
						} else CarStore.confirm2(p);
					}
				} else if (invn.equalsIgnoreCase(PetStore.ShopN)) {
					if (e.getInventory().contains(c)) {
						e.setCancelled(true);
						if (c.equals(PetStore.wolf)) PetStore.Buy(p, Pets.Wolf);
						else if (c.equals(PetStore.cat)) PetStore.Buy(p, Pets.Cat);
					}
				} else if (invn.equalsIgnoreCase(ClerkStore.displayN)) {
					if (e.getInventory().contains(c)) {
						e.setCancelled(true);
						if (ItemUtils.Weapons().equals(c)) ClerkStore.weapons(p);
						else if (ItemUtils.Food().equals(c)) ClerkStore.food(p);
						else if (c.equals(ItemUtils.JetPack())) ClerkStore.confirm(p, c, 1000);
						else if (c.equals(ItemUtils.PaintGun())) ClerkStore.confirm(p, c, 250);
					}
				} else if (invn.equals(ClerkStore.weaponsN)) {
					if (e.getInventory().contains(c)) {
						e.setCancelled(true);
						ClerkStore.confirm(p, c, Weapons.getPrice(c));
					}
				} else if (invn.equals(ClerkStore.foodN)) {
					if (e.getInventory().contains(c)) {
						e.setCancelled(true);
						ClerkStore.confirm(p, c, Food.getPrice(c));
					}
				} else if (invn.equals(HomeStore.PurchaseN())) {
					if (e.getInventory().contains(c)) {
						e.setCancelled(true);
						HomeStore.confirm(p, Houses.matchItem(c));
					}
				} else if (invn.equals(HomeStore.ManageN())) {
					if (e.getInventory().contains(c)) {
						e.setCancelled(true);
						HomeStore.confirm2(p, Houses.matchSell(c));
					}
				} else if (invn.equals(Trade.displayN())) {
					if (e.getInventory().contains(c)) {
						List<Integer> forbidSlots = new ArrayList<Integer>();
						forbidSlots.addAll(Arrays.asList(4, 5, 6, 7, 8, 13, 14, 15, 16, 17, 22, 23, 24, 25, 26, 31, 32, 33, 34, 35, 40, 41, 42, 43, 44, 49, 50, 51, 52, 53));
						if (forbidSlots.contains(e.getSlot())) e.setCancelled(true);
						else if (c.equals(Trade.submited) || c.equals(Trade.unsubmited)) e.setCancelled(true);
						else if (e.getSlot() == 3) { Trade.submit(p); e.setCancelled(true); }
						else Trade.update.put(p, e.getSlot());
					} else if (e.getClickedInventory().equals(e.getInventory())) Trade.update.put(p, e.getSlot());
						/*else if (e.getAction().equals(InventoryAction.PICKUP_ALL) || e.getAction().equals(InventoryAction.PICKUP_HALF) || 
								e.getAction().equals(InventoryAction.PICKUP_ONE)) {
							if (c.equals(Trade.submited) || c.equals(Trade.unsubmited)) e.setCancelled(true);
							else if (e.getSlot() == 3) { Trade.submit(p); e.setCancelled(true); }
							else Trade.update.put(p, e.getSlot());
						} else if (e.getAction().equals(InventoryAction.PLACE_ALL) || e.getAction().equals(InventoryAction.PLACE_ONE) ||
								e.getAction().equals(InventoryAction.PLACE_SOME)) {
							if (c.equals(Trade.submited) || c.equals(Trade.unsubmited) || e.getSlot() == 3) e.setCancelled(true);
							else Trade.update.put(p, e.getSlot());
						} else e.setCancelled(true);
					}*/
				}
			}
		}
		if (e.getCursor() != null) {
			ItemStack c = e.getCursor();
			if (ItemUtils.getItem(p).contains(c)) {
				e.setCancelled(true);
				p.updateInventory();
			}
		}
	}
}
