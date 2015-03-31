package we.Heiden.gca.Weapons;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import we.Heiden.gca.Functions.Bag;
import we.Heiden.gca.Functions.SettingsEnum;

public class WeaponUtils {

	public static void zoom(Player p, boolean zoom) {
		if (zoom) {
			SettingsEnum.setPlayer(p);
			String zoption = SettingsEnum.GUN_ZOOM.getValue();
			int zlevel = -1;
			if (zoption.equals("30%")) zlevel = 0;
			else if (zoption.equals("60%")) zlevel = 1;
			else if (zoption.equals("90%")) zlevel = 2;
			else if (zoption.equals("120%")) zlevel = 3;
			else if (zoption.equals("150%")) zlevel = 4;
			else if (zoption.equals("180%")) zlevel = 5;
			else if (zoption.equals("210%")) zlevel = 6;
			if (zlevel != -1) p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, zlevel));
			p.getInventory().setHelmet(new ItemStack(Material.PUMPKIN));
			p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
		} else {
			p.getInventory().setHelmet(null);
			p.removePotionEffect(PotionEffectType.SLOW);
			p.removePotionEffect(PotionEffectType.NIGHT_VISION);
		}
	}
	
	public static void explode(Item item) {
		TNTPrimed tnt =  (TNTPrimed) item.getWorld().spawnEntity(item.getLocation(), EntityType.PRIMED_TNT);
		tnt.setFuseTicks(-1);
		item.remove();
		WeaponHandler.tnts.add(tnt);
	}
	
    public static Projectile shootSingle(float accuracy, Player p, Class<? extends Projectile> proj) {
    	accuracy = accuracy/6;
        Vector vec = p.getLocation().getDirection().add(
        		new Vector(Math.random() * accuracy - accuracy, Math.random() * accuracy - accuracy, Math.random() * accuracy - accuracy)).multiply(20);
        return p.launchProjectile(proj, vec);
    }
    
    public static void recharge(Player p, Weapons wep) {
    	if (wep.getCharge(p) >= wep.shootCapacity) return;
    	int bullets = wep.getCharge(p);
		for (ItemStack i : Bag.inventories.get(p)) if (i != null && i.getItemMeta().hasLore() && 
				i.getItemMeta().getLore().equals(wep.bullet.getItemMeta().getLore())) bullets += i.getAmount();
		int rech = bullets >= wep.shootCapacity ? wep.shootCapacity : bullets;
		wep.setCharge(p, rech);
		for (ItemStack i : Bag.inventories.get(p)) if (i != null && i.getItemMeta().hasLore()  && i.getItemMeta().getLore().equals(wep.bullet.getItemMeta().getLore())) {
			int amount = i.getAmount();
			if (rech >= amount) {
				rech -= amount;
				Bag.inventories.get(p).removeItem(i);
			} else if (rech < amount) i.setAmount(amount-rech);
			if (rech == 0) {
				p.updateInventory();
				break;
			}
		}
    }
}
