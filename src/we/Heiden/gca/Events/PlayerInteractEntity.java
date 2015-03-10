package we.Heiden.gca.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import we.Heiden.gca.Cars.CarsMain;
import we.Heiden.gca.Misc.Messager;

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
public class PlayerInteractEntity implements Listener {
	
	public PlayerInteractEntity(Plugin pl) {Bukkit.getPluginManager().registerEvents(this, pl);}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		Messager.load(p);
		if (CarsMain.players.containsKey(p) && CarsMain.players.get(p).equals(e.getRightClicked())) {
			if (CarsMain.enums.containsKey(p) && p.getItemInHand().equals(CarsMain.enums.get(p).getKey())) {
				e.getRightClicked().setVelocity(new Vector(0, 0, 0));
				e.getRightClicked().setPassenger(p);
				Messager.e1("Turn your car");
				e.setCancelled(true);
			} else {
				Messager.e1("Invalid Key!");
				e.setCancelled(true);
			}
		} else if (e.getRightClicked() instanceof Minecart && CarsMain.vehicles.containsKey(e.getRightClicked())) {
			Messager.e1("This car isn`t yours!");
			e.setCancelled(true);
		}
	}
}
