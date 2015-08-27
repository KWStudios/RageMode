package org.kwstudios.play.ragemode.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreBoardHolder {

	private Player player;
	private Scoreboard scoreboard;
	private Objective objective;

	public ScoreBoardHolder(Player player, Scoreboard scoreboard, Objective objective) {
		this.player = player;
		this.scoreboard = scoreboard;
		this.objective = objective;
	}

	public Player getPlayer() {
		return player;
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	public Objective getObjective() {
		return objective;
	}

}
