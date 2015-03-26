package we.Heiden.gca.CustomEvents;

import we.Heiden.gca.NPCs.NMSNpc;
import we.Heiden.gca.NPCs.NPCs;

public interface NPCEvent {

	public NMSNpc getTarget();
	public NPCs getType();
}
