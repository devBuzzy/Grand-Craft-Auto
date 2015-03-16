package we.Heiden.gca.Holograms;

public interface EntityUtils {

	/*Names*/
	public void setCustomNameNMS(String name);
	public String getCustomNameNMS();
	
	// Sets if the entity should tick or not.
	public void setLockTick(boolean lock);
	// Sets the location through NMS.
	public void setLocationNMS(double x, double y, double z);
	// Returns if the entity is dead through NMS.
	public boolean isDeadNMS();
	// Kills the entity through NMS.
	public void killEntityNMS();
	// The entity ID.
	public int getIdNMS();
	// Returns the bukkit entity.
	public org.bukkit.entity.Entity getBukkitEntityNMS();
}
