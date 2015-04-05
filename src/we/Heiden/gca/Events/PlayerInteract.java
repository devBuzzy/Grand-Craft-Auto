package we.Heiden.gca.Events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.CustomEvents.NPCInteractEvent;
import we.Heiden.gca.Functions.Bag;
import we.Heiden.gca.Functions.Cars;
import we.Heiden.gca.Functions.CellPhone;
import we.Heiden.gca.Functions.Food;
import we.Heiden.gca.Functions.Settings;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.NPCs.NMSNpc;
import we.Heiden.gca.NPCs.NPCs;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.gca.core.Timer20T;
import we.Heiden.hs2.SQL.Operations;

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
public class PlayerInteract implements Listener {
	
	public PlayerInteract(Plugin pl) {Bukkit.getPluginManager().registerEvents(this, pl);}

	public static List<Entity> balls = new ArrayList<Entity>();
	public static HashMap<Player, List<Integer>> ballDelay = new HashMap<Player, List<Integer>>();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Messager.load(p);
		if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			List<Block> hsb = p.getLineOfSight(null, 5);
			NMSNpc target = null;
			boolean bool = false;
			for (NMSNpc ent : NPCs.entities) {
				CraftEntity cent = ent.getBukkitEntity();
				if (cent.getLocation().getWorld().equals(p.getLocation().getWorld())) {
					for (Block bl : hsb) {
						Location loc = cent.getLocation();
						loc.setY(loc.getY()+1);
						if (bl.getLocation().getBlock().getLocation().equals(loc.getBlock().getLocation())) {
							target = ent;
							bool = true;
							break;
						}
					}
				}
				if (bool) break;
			}
			if (target != null) {
				NPCs type = null;
				boolean bol = false;
				for (NPCs types : NPCs.values()) {
					for (NMSNpc entity : NPCs.npcs.get(types).keySet()) if (entity.equals(target)) {
						type = types;
						bol = true;
						break;
					}
					if (bol) break;
				}
				if (type != null) Bukkit.getPluginManager().callEvent(new NPCInteractEvent(p, we.Heiden.gca.CustomEvents.Action.LEFT_CLICK, target, type));
			}
		}
		if (e.getItem() != null) {
			ItemStack c = e.getItem();
			ItemStack bag = ItemUtils.ItemBag();
			ItemStack settings = ItemUtils.ItemSettings();
			ItemStack gup = ItemUtils.GearUp();
			if (Cars.velocity.containsKey(p)) gup.setAmount(Cars.velocity.get(p)+1);
			ItemStack gdown = ItemUtils.GearDown();
			if (Cars.velocity.containsKey(p)) gdown.setAmount(Cars.velocity.get(p)-1);
			
			if (ItemUtils.getItem(p).contains(c)) {
				e.setCancelled(true);
				p.updateInventory();
			}
			
			if (c.equals(bag)) Bag.open(p);
			else if (c.equals(settings)) new Settings().display(p);
			else if (c.equals(gup) || c.equals(ItemUtils.GearMax())) Cars.gearUp(p);
			else if (c.equals(gdown)) Cars.gearDown(p);
			else if (c.equals(ItemUtils.ItemPhone())) CellPhone.agend(p, 1);
			else if (c.equals(Food.stew_m.item)) {
				e.setCancelled(true);
				if (p.getHealth() < p.getMaxHealth()) {
					int amount = c.getAmount();
					amount --;
					if (amount > 0) {
						ItemStack item = c.clone();
						item.setAmount(amount);
						p.setItemInHand(item);
					} else p.setItemInHand(null);
					p.setHealth(p.getMaxHealth());
				}
			} else if (c.equals(ItemUtils.PaintGun())) {
				e.setCancelled(true);
				int money = Operations.getMoney(p);
				Messager.load(p);
				if (money < 5) Messager.e1("You need " + ((int)5-money) + " coins more");
				else if (ballDelay.containsKey(p)) Messager.e1("This is still recharging");
				else {
					Entity ball = p.launchProjectile(Snowball.class, p.getLocation().getDirection().multiply(3));
					balls.add(ball);
					List<Integer> li = Functions.newList();
					li.addAll(Arrays.asList(48, p.getInventory().getHeldItemSlot()));
					ballDelay.put(p, li);
					Operations.setMoney(p.getUniqueId(), money-5);
				}
			} else if (ItemUtils.cars.containsKey(c)) {
				if (e.getClickedBlock() == null) Messager.e1("You must click a block!");
				else {
					Location loc = e.getClickedBlock().getLocation();
					loc.setY(loc.getY()+1);
					if (!VehicleMove.rideable(loc)) Messager.e1("You must use it on the street");
					else {
						Cars.spawnCar(p, ItemUtils.cars.get(c), loc);
						Messager.s1("Car Spawned!");
						p.getInventory().setItem(6, ItemUtils.cars.get(c).getKey());
						p.getInventory().setItem(5, ItemUtils.Garage());
						p.updateInventory();
					}
				}
			} else if (Cars.enums.containsKey(p) && c.equals(Cars.enums.get(p).getKey()) && p.getLocation().getPitch() > 45F) {
				if (VehicleMove.CarStoped.contains(p)) {
					Cars.players.get(p).setVelocity(p.getLocation().getDirection().multiply(0.1));
					Cars.vec.put(p, p.getLocation().getDirection());
					Cars.setGear(p, 1);
					Cars.updateGear(p);
					VehicleMove.CarStoped.remove(p);
					VehicleExit.confirm.remove(p);
				} else {
					VehicleMove.CarStoped.add(p);
					Cars.setGear(p, 0);
					Messager.s1("Car Stoped");
					Timer20T.actionBar.remove(p);
				}
			}
		}
	}
}
