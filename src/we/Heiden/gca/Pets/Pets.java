package we.Heiden.gca.Pets;

import org.bukkit.entity.Player;

public enum Pets {

	Wolf(new Wolf(), 500),
	Cat(new Ocelot(), 750);
	
	private Pet pet;
	public int cost;
	
	private Pets(Pet pet, int cost) { pet.killEntityNMS(); this.pet = pet; this.cost = cost; }
	
	public String getType() { return this.toString(); }
	
	public static Pets getByType(String type) {
		for (Pets pet : Pets.values()) if (pet.getType().equals(type)) return pet;
		return null;
	}
	
	public Pet spawn(Player owner) { return pet.spawn(owner); }
}
