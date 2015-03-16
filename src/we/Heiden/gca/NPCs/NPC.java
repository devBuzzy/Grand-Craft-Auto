package we.Heiden.gca.NPCs;

import org.bukkit.Location;
import org.bukkit.entity.Player;


public abstract class NPC {

	private NPCs Enum;
	private String name;
	private int prof;
	private int bv;
	
	public NPC(NPCs Enum, int prof, int bv, String name) {this.Enum = Enum; this.name = name; this.prof = prof; this.bv = bv;}
	
	public NMSNpc spawn(Location loc) { 
		NMSNpc entity = NMSNpc.spawn(loc, prof, bv, name);
		NPCs.npcs.get(Enum).put(entity, loc);
		return entity;
	}
	
	public NMSNpc spawn(Player p) { return spawn(p.getLocation()); }

	public void remove(NMSNpc entity) { entity.killEntityNMS(); }
	public void remove() { for (NMSNpc e : NPCs.npcs.get(Enum).keySet()) remove(e); }
	public Location getLocation(NMSNpc entity) { return NPCs.npcs.get(Enum).get(entity); }
}
