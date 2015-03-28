package we.Heiden.gca.Pets;

import org.bukkit.World;
import org.bukkit.entity.Player;

public enum Pets {

	Wolf("Wolf", new Wolf(), 500),
	Cat("Cat", new Ocelot(), 750);
	
	private String type;
	private Pet pet;
	public int cost;
	
	private Pets(String type, Pet pet, int cost) { this.type = type; pet.killEntityNMS(); this.pet = pet; this.cost = cost; }
	
	public String getType() { return type; }
	
	public static Pets getByType(String type) {
		for (Pets pet : Pets.values()) if (pet.getType().equals(type)) return pet;
		return null;
	}
	
	public Pet spawn(World world, Player owner) { return pet.spawn(world, owner); }
}
