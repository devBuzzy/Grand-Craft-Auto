package we.Heiden.gca.Entities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityTypes;
import net.minecraft.server.v1_8_R1.EntityZombie;

import org.bukkit.entity.EntityType;

public enum CustomEntityType {

    ZOMBIE("Zombie", 54, EntityType.ZOMBIE, EntityZombie.class, null);
 
    private String name;
    private int id;
    private EntityType entityType;
    private Class<? extends EntityInsentient> nmsClass;
    private Class<? extends EntityInsentient> customClass;
 
    private CustomEntityType(String name, int id, EntityType entityType, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass){
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
    }
 
    public String getName(){
        return this.name;
    }
 
    public int getID(){
        return this.id;
    }
 
    public EntityType getEntityType(){
        return this.entityType;
    }
 
    public Class<? extends EntityInsentient> getNMSClass(){
        return this.nmsClass;
    }
 
    public Class<? extends EntityInsentient> getCustomClass(){
        return this.customClass;
    }
    
    @Deprecated
    public static void registerEntities2(){
        for (CustomEntityType entity : values()){
            try{
                Method a = EntityTypes.class.getDeclaredMethod("a", new Class<?>[]{Class.class, String.class, int.class});
                a.setAccessible(true);
                a.invoke(null, entity.getCustomClass(), entity.getName(), entity.getID());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public static void registerEntities() {
    	try {
    		/*
             * First, we make a list of all HashMap's in the EntityTypes class
             * by looping through all fields. I am using reflection here so we
             * have no problems later when minecraft changes the field's name.
             * By creating a list of these maps we can easily modify them later
             * on.
             */
    		List<Map<?, ?>> dataMaps = new ArrayList<Map<?, ?>>();
            for (Field f : EntityTypes.class.getDeclaredFields()) {
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    f.setAccessible(true);
                    dataMaps.add((Map<?, ?>) f.get(null));
                }
            }
    		for (CustomEntityType entity : values()) {
    			/*
                 * since minecraft checks if an id has already been registered, we
                 * have to remove the old entity class before we can register our
                 * custom one
                 *
                 * map 0 is the map with names and map 2 is the map with ids
                 */
    			if (dataMaps.get(2).containsKey(entity.getID())) {
                    dataMaps.get(0).remove(entity.getName());
                    dataMaps.get(2).remove(entity.getID());
                }
    			 /*
                 * now we call the method which adds the entity to the lists in the
                 * EntityTypes class, now we are actually 'registering' our entity
                 */
                 Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
                 method.setAccessible(true);
                 method.invoke(null, entity.getCustomClass(), entity.getName(), entity.getID());
    		}
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
}