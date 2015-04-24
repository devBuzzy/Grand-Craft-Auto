package we.Heiden.gca.CustomEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import we.Heiden.gca.NPCs.NMSNpc;
import we.Heiden.gca.NPCs.NPCs;

public class NPCInteractEvent extends PlayerEvent implements Cancellable,
		NPCEvent {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancel = false;
	private NMSNpc target;
	private Action action;
	private NPCs type;

	public NPCInteractEvent(Player player, Action action, NMSNpc target,
			NPCs type) {
		super(player);
		this.target = target;
		this.type = type;
		this.action = action;
	}

	public boolean isCancelled() {
		return this.cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public NMSNpc getTarget() {
		return target;
	}

	@Override
	public NPCs getType() {
		return type;
	}

	public Action getAction() {
		return action;
	}
}
