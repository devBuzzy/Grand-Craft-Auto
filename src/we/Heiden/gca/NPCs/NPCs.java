package we.Heiden.gca.NPCs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;

import we.Heiden.gca.Stores.BankStore;
import we.Heiden.gca.Stores.BasicStore;
import we.Heiden.gca.Stores.CarStore;
import we.Heiden.gca.Stores.ClerkStore;
import we.Heiden.gca.Stores.HomeStore;
import we.Heiden.gca.Stores.PetStore;

public enum NPCs {

	Store(1, 0, "&a&lClerk", new ClerkStore()),
	Cars(3, 2, "&a&lDealership Owner", new CarStore()),
	Homes(4, 0, "&a&lHome Agent", new HomeStore()),
	Pets(2, 0, "&a&lPet Clerk", new PetStore()),
	Bank(1, 0, "&a&lBank", new BankStore());
	
	private NPC object;
	public static HashMap<NPCs, HashMap<NMSNpc, Location>> npcs = new HashMap<NPCs, HashMap<NMSNpc, Location>>();
	public static List<NMSNpc> entities = new ArrayList<NMSNpc>();
	private BasicStore store;
	public String name;
	
	private NPCs(int prof, int bv, String name, BasicStore store) { object = new NPC(this, prof, bv, name) { }; this.store = store; this.name = name; }
	
	public NPC getNPC() { return object; }
	
	public static void setup() { for (NPCs npc : NPCs.values()) npcs.put(npc, new HashMap<NMSNpc, Location>()); }
	
	public static void clear() { for (NPCs npc : npcs.keySet()) for (NMSNpc entity : npcs.get(npc).keySet()) {
		entity.killEntityNMS();}
		npcs.clear();
		entities.clear();
	}
	
	public BasicStore getStore() { return store; }
	
	public static NPCs match(String name) {
		if (name.equalsIgnoreCase("car")) return NPCs.Cars;
		else if (name.equalsIgnoreCase("homes")) return NPCs.Homes;
		else if (name.equalsIgnoreCase("pet")) return NPCs.Pets;
		else if (name.equalsIgnoreCase("bank")) return NPCs.Bank;
		else return NPCs.Store;
	}
}
