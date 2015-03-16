package we.Heiden.gca.Holograms;

import net.minecraft.server.v1_8_R1.AxisAlignedBB;
import net.minecraft.server.v1_8_R1.DamageSource;
import net.minecraft.server.v1_8_R1.EntityArmorStand;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R1.Vec3D;
import net.minecraft.server.v1_8_R1.World;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;

public class NMSEntity extends EntityArmorStand implements EntityUtils {
	private boolean lockTick;
	
	public NMSEntity(World world) {
		super(world);
		setInvisible(true);
		setSmall(true);
		setArms(false);
		setGravity(true);
		setBasePlate(true);
		try { HologramUtils.setPrivateField(EntityArmorStand.class, this, "bg", Integer.MAX_VALUE); } catch (Exception e) {	}
		forceSetBoundingBox(new AxisAlignedBB(0D, 0D, 0D, 0D, 0D, 0D));
	}
	
	@Override public void b(NBTTagCompound nbttagcompound) { }
	@Override public boolean c(NBTTagCompound nbttagcompound) { return false; }
	@Override public boolean d(NBTTagCompound nbttagcompound) { return false; }
	@Override public void e(NBTTagCompound nbttagcompound) { }
	@Override public boolean isInvulnerable(DamageSource source) { return true; }
	@Override public void setCustomName(String customName) { }
	@Override public void setCustomNameVisible(boolean visible) { }
	@Override public boolean a(EntityHuman human, Vec3D vec3d) { return true; }
	@Override public boolean d(int i, ItemStack item) { return false; }
	@Override public void setEquipment(int i, ItemStack item) { }
	@Override public void a(AxisAlignedBB boundingBox) { }
	public void forceSetBoundingBox(AxisAlignedBB boundingBox) { super.a(boundingBox); }
	
	@Override public int getId() {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if (elements.length > 2 && elements[2] != null && elements[2].getFileName().equals("EntityTrackerEntry.java") && elements[2].getLineNumber() > 137 && 
				elements[2].getLineNumber() < 147) return -1;
		return super.getId();
	}
	
	@Override public void s_() { if (!lockTick) super.s_(); }
	@Override public void makeSound(String sound, float f1, float f2) { }
	
	@Override public void setCustomNameNMS(String name) {
		if (name != null && name.length() > 300) name = name.substring(0, 300);
		super.setCustomName(name);
		super.setCustomNameVisible(name != null && !name.isEmpty());
	}
	
	@Override public String getCustomNameNMS() { return super.getCustomName(); }
	@Override public void setLockTick(boolean lock) { lockTick = lock; }
	@Override public void die() { setLockTick(false); super.die(); }
	
	@Override public CraftEntity getBukkitEntity() {
		if (super.bukkitEntity == null) this.bukkitEntity = new CraftArmorStand(this.world.getServer(), this);
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
}
