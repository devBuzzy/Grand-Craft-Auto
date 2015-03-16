package we.Heiden.gca.Holograms;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityTypes;
import net.minecraft.server.v1_8_R1.MathHelper;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class HologramUtils {
	
	private static Method validateEntityMethod;
	
	public HologramUtils() throws Exception {
		registerCustomEntity(NMSEntity.class, "ArmorStand", 30);
		validateEntityMethod = World.class.getDeclaredMethod("a", Entity.class);
		validateEntityMethod.setAccessible(true);
	}
	
	@SuppressWarnings("rawtypes")
	public void registerCustomEntity(Class entityClass, String name, int id) throws Exception {
		putInPrivateStaticMap(EntityTypes.class, "d", entityClass, name);
		putInPrivateStaticMap(EntityTypes.class, "f", entityClass, Integer.valueOf(id));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void putInPrivateStaticMap(Class<?> clazz, String fieldName, Object key, Object value) throws Exception {
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		Map map = (Map) field.get(null);
		map.put(key, value);
	}
	
	public static <T> List<T> newList() {
		return new ArrayList<T>();
	}
	
	public static void setPrivateField(Class<?> clazz, Object handle, String fieldName, Object value) throws Exception {
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(handle, value);
	}
	
	public static double square(double num) { return num * num; }
	
	public static NMSEntity spawnNMSArmorStand(org.bukkit.World world, double x, double y, double z, String name) {
		WorldServer nmsWorld = ((CraftWorld) world).getHandle();
		NMSEntity entity = new NMSEntity(nmsWorld);
		entity.setCustomNameNMS(name);
		entity.setLocationNMS(x, y, z);
		if (!addEntityToWorld(nmsWorld, entity)) return null;
		return entity;
	}
	
	public static void remove(NMSEntity entity) {entity.killEntityNMS();}
	
	
	@SuppressWarnings("unchecked")
	public static boolean addEntityToWorld(WorldServer nmsWorld, Entity nmsEntity) {
		isTrue(Bukkit.isPrimaryThread(), "Async entity add");
		if (validateEntityMethod == null) { return nmsWorld.addEntity(nmsEntity, SpawnReason.CUSTOM); }
		final int chunkX = MathHelper.floor(nmsEntity.locX / 16.0);
		final int chunkZ = MathHelper.floor(nmsEntity.locZ / 16.0);
		if (!nmsWorld.chunkProviderServer.isChunkLoaded(chunkX, chunkZ)) {
			// This should never happen
			nmsEntity.dead = true;
			return false;
		}
		nmsWorld.getChunkAt(chunkX, chunkZ).a(nmsEntity);
		nmsWorld.entityList.add(nmsEntity);
		try { validateEntityMethod.invoke(nmsWorld, nmsEntity);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void isTrue(boolean statement, String message) {
		if (!statement) {
			throw new IllegalArgumentException(message);
		}
	}
}
