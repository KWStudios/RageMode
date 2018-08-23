package org.kwstudios.play.ragemode.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
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

	public static HashMap<String, ScoreBoard> allScoreBoards = new HashMap<>();
	private List<Player> player = new ArrayList<>();
	private ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
	private HashMap<Player, ScoreBoardHolder> scoreboards = new HashMap<>();

	/**
	 * Creates a new instance of ScoreBoard, which manages the ScoreBoards for
	 * the given List of Players.
	 * 
	 * @param player
	 *            The List of player, the ScoreBoard should be set to later.
	 */
	public ScoreBoard(List<Player> player) {
		this.player = player;
		for (Player loopPlayer : player) {
			Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
			scoreboard.clearSlot(DisplaySlot.SIDEBAR);
			Objective objective = scoreboard.registerNewObjective("ragescores", "dummy");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			scoreboards.put(loopPlayer, new ScoreBoardHolder(loopPlayer, scoreboard, objective));
		}
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
		for (Player loopPlayer : player) {
			Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
			scoreboard.clearSlot(DisplaySlot.SIDEBAR);
			Objective objective = scoreboard.registerNewObjective("ragescores", "dummy");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			scoreboards.put(loopPlayer, new ScoreBoardHolder(loopPlayer, scoreboard, objective));
		}
	}

	/**
	 * Sets the title for the created ScoreBoard.
	 * 
	 * @param title
	 *            The String, the title should be set to.
	 */
	public void setTitle(String title) {
		for (Player player : this.player) {
			scoreboards.get(player).getObjective().setDisplayName(title);
		}
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
		for (Player player : this.player) {
			Score score = scoreboards.get(player).getObjective().getScore(line);
			score.setScore(dummyScore);
		}
	}

	/**
	 * Updates one score-line with the given String as the name and the Integer
	 * as a score which should be displayed next to the name.
	 * 
	 * @param player
	 *            The Player for which the Line should be set.
	 * @param oldLine
	 *            The old score-name which should be updated.
	 * @param line
	 *            The name of the new score.
	 * @param dummyScore
	 *            The integer, the score should be set to.
	 */
	public void updateLine(Player player, String oldLine, String line, int dummyScore) {
		scoreboards.get(player).getScoreboard().resetScores(oldLine);
		Score score = scoreboards.get(player).getObjective().getScore(line);
		score.setScore(dummyScore);
	}

	/**
	 * Sets the ScoreBoard for all the Players given in the constructor.
	 * 
	 */
	public void setScoreBoard() {
		for (Player player : this.player) {
			player.setScoreboard(scoreboards.get(player).getScoreboard());
		}
	}

	/**
	 * Sets the ScoreBoard for the given Player.
	 * 
	 * @param player
	 *            The Player instance for which the ScoreBoard should be set.
	 */
	public void setScoreBoard(Player player) {
		player.setScoreboard(scoreboards.get(player).getScoreboard());
	}

	/**
	 * Removes the ScoreBoard for all the Players given in the constructor.
	 * 
	 */
	public void removeScoreBoard() {
		for (Player player : this.player) {
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

	/**
	 * Adds this instance to the global ScoreBoards list allScoreBoards. This
	 * can be accessed with the getScoreBoard(String gameName) method.
	 * 
	 * @param gameName
	 *            the unique game-name for which the ScoreBoards element should
	 *            be saved for.
	 * 
	 * @return Whether the ScoreBoard was stored successfully or not.
	 */
	public boolean addToScoreBoards(String gameName, boolean forceReplace) {
		if (!allScoreBoards.containsKey(gameName)) {
			allScoreBoards.put(gameName, this);
			return true;
		} else if (forceReplace) {
			allScoreBoards.remove(gameName);
			allScoreBoards.put(gameName, this);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the HashMap with all the ScoreBoardHolder elements for each Player within this ScoreBoard.
	 * @return The ScoreBoardHolder HashMap.
	 * 
	 */
	public HashMap<Player, ScoreBoardHolder> getScoreboards() {
		return scoreboards;
	}

	/**
	 * @deprecated
	 * Returns the ScoreBoard element which was saved for the unique gameName
	 * String with the addToScoreBoards method.
	 * 
	 * @param gameName
	 *            The unique String key for which the ScoreBoard was saved.
	 * @return The ScoreBoard element which was saved for the given String.
	 * 
	 */
	public ScoreBoard getScoreBoard(String gameName) {
		if (allScoreBoards.containsKey(gameName)) {
			return allScoreBoards.get(gameName);
		} else {
			return null;
		}
	}

}
