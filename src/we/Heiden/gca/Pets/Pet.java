package we.Heiden.gca.Pets;


import org.bukkit.World;
import org.bukkit.entity.Player;

public interface Pet {
	
	public Pet spawn(World world, Player owner);
	public void setLockTick(boolean lock) ;
	public void killEntityNMS();
}
