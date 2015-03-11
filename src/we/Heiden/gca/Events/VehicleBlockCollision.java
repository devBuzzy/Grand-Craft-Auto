/*
 * ******************************************** <p>
 * <b>This has been made by <i>Heiden Team</b>
 * <ul>
 * <li>Don't claim this class as your own
 * <li>Don't remove this disclaimer
 * </ul>
 *         <b>All rights reserved <p>
 *           Heiden Team 2015 <p></b>
 * ********************************************
 */

package we.Heiden.gca.Events;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.plugin.Plugin;
import we.Heiden.gca.Cars.CarsMain;
import we.Heiden.gca.Misc.Messager;
import we.Heiden.gca.core.Timer20T;

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

public class VehicleBlockCollision implements Listener{

    public VehicleBlockCollision (Plugin pl) { Bukkit.getPluginManager().registerEvents(this, pl); }

    @EventHandler
    public void VehicleCollison (VehicleBlockCollisionEvent e) {
        if (e.getVehicle() instanceof Minecart) {
            if (e.getVehicle().getPassenger() instanceof Player) {
                if (!e.getBlock().getType().equals(Material.AIR)) {
                    Player p = (Player) e.getVehicle().getPassenger();
                    if (CarsMain.vehicles.containsKey(e.getVehicle()) && CarsMain.vehicles.get(e.getVehicle()).equals(p)) {
                        CarsMain.setGear(p, 1);
                        Messager.e1("Your Have Crash, Please Turn Your Car Back Again");
                        VehicleMove.CarStoped.add(p);
                        Timer20T.actionBar.remove(p);
                    }
                }
            }
        }
    }
}
