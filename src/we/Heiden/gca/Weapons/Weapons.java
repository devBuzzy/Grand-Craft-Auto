package we.Heiden.gca.Weapons;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import we.Heiden.gca.Utils.ItemUtils;

public enum Weapons {

	M1911(ItemUtils.W_M1911(), 5, 20, 0.2F, false, 4.0D, 20, 64, 20, 1, "M1911", 250, 60),
	AK47(ItemUtils.W_AK47(), 10, 2, 0.5F, true, 0.5D, 40, 320, 30, 1, "AK47", 750, 60),
	FAMAS(ItemUtils.W_FAMAS(), 5, 20, 0.3F, true, 6.0D, 40, 320, 30, 3, "Famas", 500, 50),
	AUG(ItemUtils.W_AUG(), 5, 20, 0.15F, true, 6.0D, 40, 320, 30, 1, "A.U.G", 500, 55),
	KNIFE(ItemUtils.W_KNIFE(), 2.0D, 500),
	GRENADE(ItemUtils.W_GRENADE(), 8.0D, 75),
	SPAS_12(ItemUtils.W_SPAS_12(), 15, 40, 0.6F, false, 10.0D, 10, 84, 5, 1, "Spas 12", 1000, 45),
	BARRET_50(ItemUtils.W_BARRET_50(), 15, 40, 0F, true, 16.0D, 150, 100, 10, 1, "Barret 50", 2000, 100);
	
	public ItemStack item;
	public boolean isFireWeapon;
	public int rechargeDelay;
	public int shootDelay;
	public float Accuarcy;
	public boolean zoom;
	public double damage;
	public int maxDistance;
	public int maxCapacity;
	public int shootCapacity;
	public int bulletsPerShoot;
	public ItemStack bullet;
	public int price;
	public int ammoPrice;
	
	private static HashMap<Player, HashMap<Weapons, Integer>> charge = new HashMap<Player, HashMap<Weapons, Integer>>();

	public static void register(Player p) {
		HashMap<Weapons, Integer> weps = new HashMap<Weapons, Integer>();
		for (Weapons wep : Weapons.values())  weps.put(wep, 0);
		charge.put(p, weps);
	}
	
	private Weapons(ItemStack item, double damage, int price) {
		this.item = item;
		isFireWeapon = false;
		zoom = false;
		this.damage = damage;
		this.price = price;
	}
	
	private Weapons(ItemStack item, int rechargeDelay, int shootDelay, float Accuarcy, boolean zoom, double damage, int maxDistance, 
			int maxCapacity, int shootCapacity, int bulletsPerShoot, String name, int price, int ammoPrice) {
		this.item = item;
		isFireWeapon = true;
		this.rechargeDelay = rechargeDelay;
		this.shootDelay = shootDelay;
		this.Accuarcy = Accuarcy;
		this.zoom = zoom;
		this.damage = damage;
		this.maxDistance = maxDistance;
		this.maxCapacity = maxCapacity;
		this.shootCapacity = shootCapacity;
		this.bulletsPerShoot = bulletsPerShoot;
		ItemStack bullet = ItemUtils.Bullet().clone();
		ItemMeta im = bullet.getItemMeta();
		im.setLore(Arrays.asList(name + "'s Bullet"));
		bullet.setItemMeta(im);
		this.bullet = bullet;
		this.price = price;
		this.ammoPrice = ammoPrice;
	}
	
	public int getCharge(Player p) { return charge.get(p).get(this); }
	public void setCharge(Player p, int newCharge) { charge.get(p).put(this, newCharge); }
	
	public static int getPrice(ItemStack item) {
		for (Weapons wep : Weapons.values()) {
			if (item.equals(wep.item)) return wep.price;
			if (wep.isFireWeapon && wep.bullet.equals(item)) return wep.ammoPrice;
		}
		return 1;
	}
}
