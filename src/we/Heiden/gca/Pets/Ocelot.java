package we.Heiden.gca.Pets;

import java.util.List;

import net.minecraft.server.v1_8_R1.DamageSource;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityOcelot;
import net.minecraft.server.v1_8_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R1.PathfinderGoalFollowOwner;
import net.minecraft.server.v1_8_R1.PathfinderGoalJumpOnBlock;
import net.minecraft.server.v1_8_R1.PathfinderGoalLeapAtTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.Player;

import we.Heiden.gca.NPCs.NMSNpc;

@SuppressWarnings("rawtypes")
public class Ocelot extends EntityOcelot implements Pet {

	private boolean lockTick;
	
	public Ocelot() {super(((CraftWorld)Bukkit.getWorlds().get(0)).getHandle());}

	public Ocelot(World world, Player owner) {
		super(world);

		List goalB = (List)NMSNpc.getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
		List goalC = (List)NMSNpc.getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
		List targetB = (List)NMSNpc.getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
		List targetC = (List)NMSNpc.getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();

		this.goalSelector.a(1, new PathfinderGoalFloat(this));
		this.goalSelector.a(3, new PathfinderGoalLeapAtTarget(this, 0.4F));
		this.goalSelector.a(5, new PathfinderGoalFollowOwner(this, 1.0D, 7.0F, 2.0F));
		this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(6, new PathfinderGoalJumpOnBlock(this, 0.8D));
		this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(9, new PathfinderGoalRandomLookaround(this));

		setTamed(true);
		setOwnerUUID(owner.getUniqueId().toString());
		setCustomName(ChatColor.translateAlternateColorCodes('&', "&a&l" + owner.getDisplayName() + "`s &b&Pet"));
		setCustomNameVisible(true);
		teleportTo(owner.getLocation(), false);
	}
	
	@Override public boolean isInvulnerable(DamageSource source) { return true; }
	@Override public void s_() { if (!lockTick) super.s_(); }
	@Override public void setLockTick(boolean lock) { lockTick = lock; }
	@Override public void die() { setLockTick(false); super.die(); }
	@Override public void killEntityNMS() { die(); }

	public Pet spawn(org.bukkit.World world, Player owner) { return new Ocelot(((CraftWorld)world).getHandle(), owner); }
}
