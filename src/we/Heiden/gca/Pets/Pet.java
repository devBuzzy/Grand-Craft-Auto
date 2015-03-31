package we.Heiden.gca.Pets;


import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Pet {
	
	public Pet spawn(Player owner);
	public void killEntityNMS();
	public Location getLocation();
}
