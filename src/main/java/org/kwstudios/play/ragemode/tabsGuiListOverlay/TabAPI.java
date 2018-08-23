package org.kwstudios.play.ragemode.tabsGuiListOverlay;

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

public class TabAPI {
	
	public static HashMap<String, TabAPI> allScoreBoards = new HashMap<>();
	private List<Player> player = new ArrayList<>();
	private ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
	private HashMap<Player, TabHolder> scoreboards = new HashMap<>();

	public TabAPI(List<Player> player) {
		this.player = player;
		for (Player loopPlayer : player) {
			Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
			scoreboard.clearSlot(DisplaySlot.PLAYER_LIST);
			Objective objective = scoreboard.registerNewObjective("ragescores", "dummy");
			objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
			scoreboards.put(loopPlayer, new TabHolder(loopPlayer, scoreboard, objective));
		}
	}

	public TabAPI(List<String> playerString, boolean isList) {
		for (String player : playerString) {
			this.player.add(Bukkit.getPlayer(UUID.fromString(player)));
		}
		for (Player loopPlayer : player) {
			Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
			scoreboard.clearSlot(DisplaySlot.PLAYER_LIST);
			Objective objective = scoreboard.registerNewObjective("ragescores", "dummy");
			objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
			scoreboards.put(loopPlayer, new TabHolder(loopPlayer, scoreboard, objective));
		}
	}

	public void setTitle(String title) {
		for (Player player : this.player) {
			scoreboards.get(player).getObjective().setDisplayName(title);
		}
	}

	public void setLine(String line, int dummyScore) {
		for (Player player : this.player) {
			Score score = scoreboards.get(player).getObjective().getScore(line);
			score.setScore(dummyScore);
		}
	}

	public void updateLine(Player player, String oldLine, String line, int dummyScore) {
		scoreboards.get(player).getScoreboard().resetScores(oldLine);
		Score score = scoreboards.get(player).getObjective().getScore(line);
		score.setScore(dummyScore);
	}

	public void setScoreBoard() {
		for (Player player : this.player) {
			player.setScoreboard(scoreboards.get(player).getScoreboard());
		}
	}

	public void setScoreBoard(Player player) {
		player.setScoreboard(scoreboards.get(player).getScoreboard());
	}

	public void removeScoreBoard() {
		for (Player player : this.player) {
			player.setScoreboard(scoreboardManager.getNewScoreboard());
		}
	}

	public void removeScoreBoard(Player player) {
		player.setScoreboard(scoreboardManager.getNewScoreboard());
	}

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

	public HashMap<Player, TabHolder> getScoreboards() {
		return scoreboards;
	}
	
}
