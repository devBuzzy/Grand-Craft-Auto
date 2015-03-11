package we.Heiden.gca.Events;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Cars.CarsEnum;
import we.Heiden.gca.Cars.CarsMain;
import we.Heiden.gca.Functions.Bag;
import we.Heiden.gca.Functions.Settings;
import we.Heiden.gca.Misc.Messager;
import we.Heiden.gca.Misc.Others;

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
	
	public static HashMap<ItemStack, CarsEnum> cars = new HashMap<ItemStack, CarsEnum>();
	
	public PlayerInteract(Plugin pl) {
		Bukkit.getPluginManager().registerEvents(this, pl);
		for (CarsEnum car : CarsEnum.values()) cars.put(car.getItem(), car);}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Messager.load(p);
		if (e.getItem() != null) {
			ItemStack c = e.getItem();
			ItemStack bag = Others.ItemBag();
			ItemStack settings = Others.ItemSettings();
			ItemStack gup = Others.GearUp();
			if (CarsMain.velocity.containsKey(p)) gup.setAmount(CarsMain.velocity.get(p)+1);
			ItemStack gdown = Others.GearDown();
			if (CarsMain.velocity.containsKey(p)) gdown.setAmount(CarsMain.velocity.get(p)-1);
			
			if (Others.ItemList(p).contains(c) || c.equals(gdown) || c.equals(gup)) e.setCancelled(true);
			
			if (c.equals(bag)) Bag.open(p);
			else if (c.equals(settings)) Settings.display(p);
			else if (c.equals(gup) || c.equals(Others.GearMax())) CarsMain.gearUp(p);
			else if (c.equals(gdown)) CarsMain.gearDown(p);
			else if (cars.containsKey(c)) {
				if (e.getClickedBlock() == null) Messager.e1("You must click a block!");
				else {
					Location loc = e.getClickedBlock().getLocation();
					loc.setY(loc.getY()+1);
					if (!VehicleMove.rideable(loc)) Messager.e1("You must use it on the street");
					else {
						CarsMain.spawnCar(p, cars.get(c), loc);
						Messager.s1("Car Spawned!");
						p.setItemInHand(cars.get(c).getKey());
					}
				}
			} else if (CarsMain.enums.containsKey(p) && c.equals(CarsMain.enums.get(p).getKey())) {
				CarsMain.players.get(p).setVelocity(p.getLocation().getDirection().multiply(0.1));
				CarsMain.vec.put(p, p.getLocation().getDirection());
				CarsMain.updateGear(p);
				p.getInventory().setItem(5, CarsMain.enums.get(p).getKey());
                VehicleMove.CarStoped.remove(p);
				if (gup.getAmount() > CarsMain.enums.get(p).getMax()) p.getInventory().setItem(6, Others.GearMax());
				else p.getInventory().setItem(6, gup);
				p.updateInventory();
			}
		}
	}
}
