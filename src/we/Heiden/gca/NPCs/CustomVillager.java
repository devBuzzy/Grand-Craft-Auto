package we.Heiden.gca.NPCs;

import java.util.List;

import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.EntityVillager;
import net.minecraft.server.v1_8_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R1.PathfinderGoalInteractVillagers;
import net.minecraft.server.v1_8_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R1.PathfinderGoalMoveIndoors;
import net.minecraft.server.v1_8_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R1.PathfinderGoalOpenDoor;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R1.PathfinderGoalRestrictOpenDoor;
import net.minecraft.server.v1_8_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R1.PathfinderGoalTakeFlower;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftVillager;

import we.Heiden.hs2.Holograms.EntityUtils;
import we.Heiden.hs2.Holograms.HologramUtils;

public class CustomVillager extends EntityVillager implements EntityUtils {

	@SuppressWarnings("rawtypes")
	public CustomVillager(World world) {
		super(world);
		
        List goalB = (List)NMSNpc.getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
        List goalC = (List)NMSNpc.getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
        List targetB = (List)NMSNpc.getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
        List targetC = (List)NMSNpc.getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();

        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new PathfinderGoalMoveIndoors(this));
        this.goalSelector.a(3, new PathfinderGoalRestrictOpenDoor(this));
        this.goalSelector.a(4, new PathfinderGoalOpenDoor(this, true));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 0.6D));
        this.goalSelector.a(7, new PathfinderGoalTakeFlower(this));
        this.goalSelector.a(9, new PathfinderGoalInteractVillagers(this));
        this.goalSelector.a(9, new PathfinderGoalRandomStroll(this, 0.6D));
        this.goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityInsentient.class, 8.0F));
	}

	public void setLockTick(boolean arg0) { }
	@Override public void setCustomName(String customName) { }
	@Override public void setCustomNameVisible(boolean visible) { }
	@Override public void setCustomNameNMS(String name) {
		if (name != null && name.length() > 300) name = name.substring(0, 300);
		super.setCustomName(name);
		super.setCustomNameVisible(name != null && !name.isEmpty());
	}
	
	@Override public String getCustomNameNMS() { return super.getCustomName(); }
	@Override public void die() { super.die(); }
	
	@Override public CraftEntity getBukkitEntity() {
		if (super.bukkitEntity == null) this.bukkitEntity = new CraftVillager(this.world.getServer(), this);
		return this.bukkitEntity;
	}
	
	@Override public void killEntityNMS() { die(); }
	
	@Override public void setLocationNMS(double x, double y, double z) {
		super.setPosition(x, y, z);
		// Send a packet near to update the position.
		PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(this);
		for (Object obj : this.world.players) {
			if (obj instanceof EntityPlayer) {
				EntityPlayer nmsPlayer = (EntityPlayer) obj;
				double distanceSquared = HologramUtils.square(nmsPlayer.locX - this.locX) + HologramUtils.square(nmsPlayer.locZ - this.locZ);
				if (distanceSquared < 8192 && nmsPlayer.playerConnection != null) nmsPlayer.playerConnection.sendPacket(teleportPacket);
			}
		}
	}
	
	@Override public boolean isDeadNMS() { return this.dead; }
	@Override public int getIdNMS() { return this.getId(); }
	@Override public org.bukkit.entity.Entity getBukkitEntityNMS() { return getBukkitEntity(); }

	public static CustomVillager spawn(Location loc, String name) {
		name = ChatColor.translateAlternateColorCodes('&', name);
		WorldServer nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();
		CustomVillager entity = new CustomVillager(nmsWorld);
		entity.setCustomNameNMS(name);
		entity.setLocationNMS(loc.getX(), loc.getY(), loc.getZ());
		if (!HologramUtils.addEntityToWorld(nmsWorld, entity)) return null;
		return entity;
	}
}
