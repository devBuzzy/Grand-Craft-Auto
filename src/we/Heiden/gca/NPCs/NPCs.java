package we.Heiden.gca.NPCs;

import java.util.HashMap;

import org.bukkit.Location;

public enum NPCs {

	Store(0, 0, "&a&lClerk"),
	Cars(3, 2, "&a&lDealership Owner"),
	Homes(4, 0, "&a&lHome Agent"),
	Pets(2, 0, "&a&lPet Clerk");
	
	NPC object;
	public static HashMap<NPCs, HashMap<NMSNpc, Location>> npcs = new HashMap<NPCs, HashMap<NMSNpc, Location>>();
	
	private NPCs(int prof, int bv, String name) { object = new NPC(this, prof, bv, name) { }; }
	
	public NPC getNPC() { return object; }
	
	public static void setup() { for (NPCs npc : NPCs.values()) npcs.put(npc, new HashMap<NMSNpc, Location>()); }
	
	public static void clear() { for (NPCs npc : npcs.keySet()) for (NMSNpc entity : npcs.get(npc).keySet()) entity.killEntityNMS(); npcs.clear();}
}
