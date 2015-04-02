package we.Heiden.gca.Weapons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

import we.Heiden.gca.Functions.Bag;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.gca.core.Timer20T;
import we.Heiden.gca.core.Timer2T;
import we.Heiden.hs2.Messages.ActionBar;

public class WeaponHandler implements Listener {

	public WeaponHandler(Plugin pl) { Bukkit.getPluginManager().registerEvents(this, pl); }
	
	public static HashMap<Projectile, List<Object>> bullet = new HashMap<Projectile, List<Object>>();
	public static HashMap<Player, HashMap<Weapons, Integer>> delay = new HashMap<Player, HashMap<Weapons, Integer>>();
	public static List<Projectile> toRemove = new ArrayList<Projectile>();
	public static HashMap<Item, Integer> grenades = new HashMap<Item, Integer>();
	public static List<TNTPrimed> tnts = new ArrayList<TNTPrimed>();
	
	@EventHandler
	public void onWeaponUse(PlayerInteractEvent e) {
		if (e.getItem() != null) {
			ItemStack c = e.getItem().clone();
			ItemMeta im = c.getItemMeta();
			im.setLore(null);
			c.setItemMeta(im);
			for (Weapons wep : Weapons.values()) {
				ItemStack wepi = wep.item;
				im = wepi.getItemMeta();
				im.setLore(null);
				wepi.setItemMeta(im);
				if (c.equals(wepi)) {
					e.setCancelled(true);
					Player p = e.getPlayer();
					if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
						if (wep.isFireWeapon) {
							if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION) && p.hasPotionEffect(PotionEffectType.SLOW) && 
									p.getInventory().getHelmet().getType() == Material.PUMPKIN) 
								WeaponUtils.zoom(p, false);
							else if (wep.zoom) WeaponUtils.zoom(p, true);
						} else if (wep.equals(Weapons.GRENADE)) {
							Item item = p.getWorld().dropItem(p.getEyeLocation(), wep.item);
							item.setVelocity(p.getLocation().getDirection().multiply(3));
							grenades.put(item, 3);
							int amount = p.getItemInHand().getAmount();
							amount--;
							if (amount < 1) p.setItemInHand(null);
							else p.getItemInHand().setAmount(amount);
						}
					} else {
						Messager.load(p);
						if (!delay.containsKey(p) || !delay.get(p).containsKey(wep)) {
							if (wep.isFireWeapon) {
								int charge = wep.getCharge(p);
								if (charge < wep.bulletsPerShoot) {
									int left = wep.bulletsPerShoot-charge;
									Messager.e1("You need " + left + " more bullet(s)");
								} else {
									List<Object> lo = new ArrayList<Object>();
									lo.addAll(Arrays.asList(wep, wep.bulletsPerShoot));
									wep.setCharge(p, charge-wep.bulletsPerShoot);
									Timer2T.shoots.put(p, lo);
									
									ItemStack i = p.getItemInHand();
									im = i.getItemMeta();
									im.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&aCharge: &b" + wep.getCharge(p) + "&9/&c" + wep.shootCapacity)));
									i.setItemMeta(im);
									p.setItemInHand(i);
									
									p.getWorld().playSound(p.getLocation(), wep.sound, 1, 1);
									
									if (wep.getCharge(p) >= wep.bulletsPerShoot) {
										HashMap<Weapons, Integer> hm;
										if (!delay.containsKey(p)) hm = new HashMap<Weapons, Integer>();
										else hm = delay.get(p);
										hm.put(wep, wep.shootDelay);
										delay.put(p, hm);
									}
								}
							}
						}
					}
					break;
				}
			}
		}
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {
		Projectile proj = e.getEntity();
		if (bullet.containsKey(proj)) toRemove.add(proj);
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Projectile) {
			Projectile proj = (Projectile) e.getDamager();
			if (bullet.containsKey(proj)) {
				e.setCancelled(true);
				Weapons wep = (Weapons) bullet.get(proj).get(0);
				bullet.remove(proj);
				if (e.getEntity() instanceof LivingEntity) {
					((LivingEntity)e.getEntity()).damage(wep.damage, (Player)proj.getShooter());
					Functions.displayEffect(e.getEntity().getLocation(), 9, 0.5F, 10, e.getEntity().getWorld().getPlayers());
				}
				proj.remove();
			}
		} else if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if (p.getItemInHand().equals(Weapons.KNIFE.item) && e.getEntity() instanceof LivingEntity) ((LivingEntity)e.getEntity()).damage(2.0D);
		} else if (e.getDamager() instanceof TNTPrimed) {
			if (tnts.contains(e.getDamager()) && e.getEntity() instanceof LivingEntity) {
				((LivingEntity)e.getEntity()).damage(8.0D);
				e.setCancelled(true);
				tnts.remove(e.getDamager());
			}
		}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		ItemStack c = e.getItemDrop().getItemStack();
		Player p = e.getPlayer();
		if (p.getOpenInventory() == null)
		if (!Timer20T.recharging.containsKey(p)) {
			for (Weapons wep : Weapons.values()) {
				if (c.getItemMeta().getDisplayName().equals(wep.item.getItemMeta().getDisplayName()) && wep.getCharge(p) != wep.shootCapacity) {
					e.setCancelled(true);
					int bullets = 0;
					for (ItemStack i : Bag.inventories.get(p)) if (i != null && i.hasItemMeta() && i.getItemMeta().hasLore() &&
							i.getItemMeta().getLore().equals(wep.bullet.getItemMeta().getLore())) bullets += i.getAmount();
					if (bullets < 1) {
						if (wep.getCharge(p) == 0) {
							Messager.load(p);
							Messager.e1("You don`t have any bullet");
						}
					} else {
						List<Object> lo = new ArrayList<Object>();
						lo.addAll(Arrays.asList(wep, wep.rechargeDelay));
						Timer20T.recharging.put(p, lo);
						p.getWorld().playSound(p.getLocation(), Sound.FIREWORK_TWINKLE, 1, 1);
					}
					break;
				}
			}
		} else e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent e) {
		Player p = e.getPlayer();
		if (Timer20T.recharging.containsKey(p)) {
			Timer20T.recharging.remove(p);
			new ActionBar(p).e("Recharge cancelled");
			p.setLevel(0);
		}
	}
}
