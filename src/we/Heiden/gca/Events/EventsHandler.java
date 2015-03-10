package we.Heiden.gca.Events;

import org.bukkit.plugin.Plugin;

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
public class EventsHandler {

	public EventsHandler(Plugin pl) {
		new PlayerJoin(pl);
		new InventoryClick(pl);
		new PlayerDropItem(pl);
		new PlayerInteract(pl);
		new PlayerQuit(pl);
		new InventoryClose(pl);
		new PlayerCommandPreproccess(pl);
		new VehicleMove(pl);
		new PlayerInteractEntity(pl);
	}
}
