package fr.vi5team.vi5.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.vi5team.vi5.enums.DieCancelType;

public class PlayerKillEvent extends Event {
	
	private final Player killer;
	private final Player victim;
	private boolean canceled=false;
	private DieCancelType cancelType=DieCancelType.FULL_CANCEL;

	public PlayerKillEvent(Player _killer, Player _victim) {
		killer=_killer;
		victim=_victim;
	}
	
	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}

	public Player getKiller() {
		return killer;
	}

	public Player getVictim() {
		return victim;
	}

	public boolean is_Canceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public DieCancelType getCancelType() {
		return cancelType;
	}

	public void setCancelType(DieCancelType cancelType) {
		this.cancelType = cancelType;
	}
}
