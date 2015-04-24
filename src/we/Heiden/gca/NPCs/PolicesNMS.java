package we.Heiden.gca.NPCs;

import java.util.List;

import net.minecraft.server.v1_8_R1.DamageSource;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.EntityZombie;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R1.PathfinderGoalSelector;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import we.Heiden.gca.Configs.Config;
import we.Heiden.hs2.Holograms.HologramUtils;

public class PolicesNMS extends EntityZombie {

	private static ItemStack hand = null;
	private static ItemStack helmet = null;
	private static ItemStack chestplate = null;
	private static ItemStack leggings = null;
	private static ItemStack boots = null;

	@SuppressWarnings("rawtypes")
	public PolicesNMS(Location loc, Player p) {
		super(((CraftWorld) loc.getWorld()).getHandle());

		List goalB = (List) NMSNpc.getPrivateField("b",
				PathfinderGoalSelector.class, goalSelector);
		goalB.clear();
		List goalC = (List) NMSNpc.getPrivateField("c",
				PathfinderGoalSelector.class, goalSelector);
		goalC.clear();
		List targetB = (List) NMSNpc.getPrivateField("b",
				PathfinderGoalSelector.class, targetSelector);
		targetB.clear();
		List targetC = (List) NMSNpc.getPrivateField("c",
				PathfinderGoalSelector.class, targetSelector);
		targetC.clear();

		goalSelector.a(0, new PathfinderGoalFloat(this));
		goalSelector.a(2, new PathfinderGoalMeleeAttack(this,
				EntityHuman.class, 1.0D, false));
		goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
		goalSelector.a(8, new PathfinderGoalLookAtPlayer(this,
				EntityHuman.class, 8.0F));
		goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
		targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true,
				new Class[] { EntityHuman.class }));

		setGoalTarget(((CraftPlayer) p).getHandle(), TargetReason.CUSTOM, false);
		((LivingEntity) this.getBukkitEntity()).setMaxHealth(60.0D);
		setHealth(this.getMaxHealth());
		String name = ChatColor.translateAlternateColorCodes('&', Config.get()
				.getString("Polices Name"));
		setCustomName(name);
		setCustomNameVisible(name != null && !name.isEmpty());

		if (hand == null) {
			org.bukkit.inventory.ItemStack item = new org.bukkit.inventory.ItemStack(
					Material.STICK);
			item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
			hand = CraftItemStack.asNMSCopy(item);
		}

		if (helmet == null)
			helmet = getArmor(Material.LEATHER_HELMET);
		if (chestplate == null)
			chestplate = getArmor(Material.LEATHER_CHESTPLATE);
		if (leggings == null)
			leggings = getArmor(Material.LEATHER_LEGGINGS);
		if (boots == null)
			boots = getArmor(Material.LEATHER_BOOTS);

		setEquipment(0, helmet);
		setEquipment(1, chestplate);
		setEquipment(2, leggings);
		setEquipment(3, boots);
		setEquipment(4, hand);

		setLocationNMS(loc.getX(), loc.getY(), loc.getZ());
	}
	
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
	public boolean damageEntity(DamageSource damagesource, float f) {
		return super.damageEntity(damagesource, f) && f < getMaxHealth();
	}

	public void setLocation(Location loc) {
		setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(),
				loc.getPitch());
	}

	@Override
	protected void aW() {
		super.aW();
		this.getAttributeInstance(GenericAttributes.maxHealth).setValue(60D);
		this.getAttributeInstance(GenericAttributes.b).setValue(200D);
		this.getAttributeInstance(GenericAttributes.c).setValue(0.5D);
		this.getAttributeInstance(GenericAttributes.d).setValue(1.5D);
		this.getAttributeInstance(GenericAttributes.e).setValue(6D);
	}

	private static ItemStack getArmor(Material mat) {
		org.bukkit.inventory.ItemStack item = new org.bukkit.inventory.ItemStack(
				mat);
		item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(Color.BLUE);
		item.setItemMeta(meta);
		return CraftItemStack.asNMSCopy(item);
	}
}
