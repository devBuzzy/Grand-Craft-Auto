package we.Heiden.gca.NPCs;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.server.v1_8_R1.DamageSource;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.EntityVillager;
import net.minecraft.server.v1_8_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftVillager;

import we.Heiden.hs2.Holograms.EntityUtils;
import we.Heiden.hs2.Holograms.HologramUtils;

@SuppressWarnings("rawtypes")
public class NMSNpc extends EntityVillager implements EntityUtils {
	private boolean lockTick;

	public NMSNpc(World world, int profession, int bv) {
		super(world);
		setProfession(profession);
		if (bv != 0)
			try {
				HologramUtils.setPrivateField(EntityVillager.class, this, "bv",
						bv);
			} catch (Exception e) {
			}
		List goalB = (List) getPrivateField("b", PathfinderGoalSelector.class,
				goalSelector);
		goalB.clear();
		List goalC = (List) getPrivateField("c", PathfinderGoalSelector.class,
				goalSelector);
		goalC.clear();
		List targetB = (List) getPrivateField("b",
				PathfinderGoalSelector.class, targetSelector);
		targetB.clear();
		List targetC = (List) getPrivateField("c",
				PathfinderGoalSelector.class, targetSelector);
		targetC.clear();

		this.goalSelector.a(1, new PathfinderGoalLookAtPlayer(this,
				EntityPlayer.class, 20.0F));
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
	}

	public static Object getPrivateField(String fieldName, Class clazz,
			Object object) {
		Field field;
		Object o = null;

		try {
			field = clazz.getDeclaredField(fieldName);

			field.setAccessible(true);

			o = field.get(object);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return o;
	}

	@Override
	public boolean isInvulnerable(DamageSource source) {
		return true;
	}

	@Override
	public void setCustomName(String customName) {
	}

	@Override
	public void setCustomNameVisible(boolean visible) {
	}

	@Override
	public void makeSound(String sound, float f1, float f2) {
	}

	@Override
	public void s_() {
		if (!lockTick)
			super.s_();
	}

	@Override
	public void setCustomNameNMS(String name) {
		if (name != null && name.length() > 300)
			name = name.substring(0, 300);
		super.setCustomName(name);
		super.setCustomNameVisible(name != null && !name.isEmpty());
	}

	@Override
	public String getCustomNameNMS() {
		return super.getCustomName();
	}

	@Override
	public void setLockTick(boolean lock) {
		lockTick = lock;
	}

	@Override
	public void die() {
		setLockTick(false);
		super.die();
	}

	@Override
	public CraftEntity getBukkitEntity() {
		if (super.bukkitEntity == null)
			this.bukkitEntity = new CraftVillager(this.world.getServer(), this);
		return this.bukkitEntity;
	}

	@Override
	public void killEntityNMS() {
		die();
	}

	@Override
	public void setLocationNMS(double x, double y, double z) {
		super.setPosition(x, y, z);
		// Send a packet near to update the position.
		PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(
				this);
		for (Object obj : this.world.players) {
			if (obj instanceof EntityPlayer) {
				EntityPlayer nmsPlayer = (EntityPlayer) obj;
				double distanceSquared = HologramUtils.square(nmsPlayer.locX
						- this.locX)
						+ HologramUtils.square(nmsPlayer.locZ - this.locZ);
				if (distanceSquared < 8192
						&& nmsPlayer.playerConnection != null)
					nmsPlayer.playerConnection.sendPacket(teleportPacket);
			}
		}
	}

	@Override
	public boolean isDeadNMS() {
		return this.dead;
	}

	@Override
	public int getIdNMS() {
		return this.getId();
	}

	@Override
	public org.bukkit.entity.Entity getBukkitEntityNMS() {
		return getBukkitEntity();
	}

	public static NMSNpc spawn(Location loc, int profession, int bv, String name) {
		if (loc == null) return null;
		name = ChatColor.translateAlternateColorCodes('&', name);
		WorldServer nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();
		NMSNpc entity = new NMSNpc(nmsWorld, profession, bv);
		entity.setCustomNameNMS(name);
		entity.setLocationNMS(loc.getX(), loc.getY(), loc.getZ());
		if (!HologramUtils.addEntityToWorld(nmsWorld, entity))
			return null;
		return entity;
	}
}
