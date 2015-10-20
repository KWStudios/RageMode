package org.kwstudios.play.ragemode.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KillEvent extends Event {

	public static final HandlerList HANDLERS = new HandlerList();

	private Player killer;
	private Player victim;
	private boolean firstKill;
	private boolean firstBlood;

	public KillEvent(Player killer, Player victim, boolean firstKill, boolean firstBlood) {
		this.killer = killer;
		this.victim = victim;
		this.firstKill = firstKill;
		this.firstBlood = firstBlood;
	}

	public Player getKiller() {
		return killer;
	}

	public Player getVictim() {
		return victim;
	}

	public boolean isFirstKill() {
		return firstKill;
	}

	public boolean isFirstBlood() {
		return firstBlood;
	}

	@Override
	public HandlerList getHandlers() {
		return KillEvent.HANDLERS;
	}

}
