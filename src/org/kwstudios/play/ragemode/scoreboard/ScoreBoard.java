package org.kwstudios.play.ragemode.scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreBoard {

	private List<Player> player = new ArrayList<Player>();
	private ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
	private Scoreboard scoreboard;
	private Objective objective;

	/**
	 * Creates a new instance of ScoreBoard, which manages the ScoreBoards for
	 * the given List of Players.
	 * 
	 * @param player
	 *            The List of player, the ScoreBoard should be set to later.
	 */
	public ScoreBoard(List<Player> player) {
		this.player = player;
		scoreboard = scoreboardManager.getNewScoreboard();
		scoreboard.clearSlot(DisplaySlot.SIDEBAR);
		objective = scoreboard.registerNewObjective("ragescores", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}

	/**
	 * Creates a new instance of ScoreBoard, which manages the ScoreBoards for
	 * the given List of Player UUID Strings.
	 * 
	 * @param player
	 *            The List of player, the ScoreBoard should be set to later.
	 * @param isList
	 *            Set it to whatever you want, it won't be used. Just to be able
	 *            to create a new constructor with the List parameter.
	 */
	public ScoreBoard(List<String> playerString, boolean isList) {
		for (String player : playerString) {
			this.player.add(Bukkit.getPlayer(UUID.fromString(player)));
		}
		scoreboard = scoreboardManager.getNewScoreboard();
		scoreboard.clearSlot(DisplaySlot.SIDEBAR);
		objective = scoreboard.registerNewObjective("ragescores", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}

	/**
	 * Sets the title for the created ScoreBoard.
	 * 
	 * @param title
	 *            The String, the title should be set to.
	 */
	public void setTitle(String title) {
		objective.setDisplayName(title);
	}

	/**
	 * Sets one score-line with the given String as the name and the Integer as
	 * a score which should be displayed next to the name.
	 * 
	 * @param line
	 *            The name of the new score.
	 * @param dummyScore
	 *            The integer, the score should be set to.
	 */
	public void setLine(String line, int dummyScore) {
		Score score = objective.getScore(line);
		score.setScore(dummyScore);
	}

	/**
	 * Sets the ScoreBoard for all the Players given in the constructor.
	 * 
	 */
	public void setScoreBoard() {
		for (Player player : player) {
			player.setScoreboard(scoreboard);
		}
	}

	/**
	 * Sets the ScoreBoard for the given Player.
	 * 
	 * @param player
	 *            The Player instance for which the ScoreBoard should be set.
	 */
	public void setScoreBoard(Player player) {
		player.setScoreboard(scoreboard);
	}

	/**
	 * Removes the ScoreBoard for all the Players given in the constructor.
	 * 
	 */
	public void removeScoreBoard() {
		for (Player player : player) {
			player.setScoreboard(scoreboardManager.getNewScoreboard());
		}
	}

	/**
	 * Removes the ScoreBoard for the given Player.
	 * 
	 * @param player
	 *            The Player instance for which the ScoreBoard should be
	 *            removed.
	 */
	public void removeScoreBoard(Player player) {
		player.setScoreboard(scoreboardManager.getNewScoreboard());
	}

}
