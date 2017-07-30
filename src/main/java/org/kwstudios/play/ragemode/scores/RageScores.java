package org.kwstudios.play.ragemode.scores;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.scoreboard.ScoreBoard;
import org.kwstudios.play.ragemode.scoreboard.ScoreBoardHolder;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class RageScores {

	private static HashMap<String, PlayerPoints> playerpoints = new HashMap<String, PlayerPoints>();
	// private static TableList<String, String> playergame = new
	// TableList<String, String>(); ----> User PlayerList.getPlayersInGame
	// instead
	private static int totalPoints = 0;

	public static void addPointsToPlayer(Player killer, Player victim, String killCause) {
		if (!killer.getUniqueId().toString().equals(victim.getUniqueId().toString())) {

			String killerUUID = killer.getUniqueId().toString();
			PlayerPoints killerPoints;
			String victimUUID = victim.getUniqueId().toString();
			PlayerPoints victimPoints;

			String message;

			switch (killCause.toLowerCase().trim()) {
			case "ragebow":
				int bowPoints = ConstantHolder.POINTS_FOR_BOW_KILL;
				totalPoints = addPoints(killer, PlayerList.getPlayersGame(killer), bowPoints, true);
				addPoints(victim, PlayerList.getPlayersGame(victim), 0, false);

				killerPoints = playerpoints.get(killerUUID);
				int oldDirectArrowKills = killerPoints.getDirectArrowKills();
				int newDirectArrowKills = oldDirectArrowKills + 1;
				killerPoints.setDirectArrowKills(newDirectArrowKills);

				victimPoints = playerpoints.get(victimUUID);
				int oldDirectArrowDeaths = victimPoints.getDirectArrowDeaths();
				int newDirectArrowDeaths = oldDirectArrowDeaths + 1;
				victimPoints.setDirectArrowDeaths(newDirectArrowDeaths);

				message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
						PluginLoader.getMessages().MESSAGE_ARROW_KILL.replace("$VICTIM$", victim.getName())
								.replace("$POINTS$", "+" + Integer.toString(bowPoints)));
				killer.sendMessage(message);

				message = ConstantHolder.RAGEMODE_PREFIX
						+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().MESSAGE_ARROW_DEATH
								.replace("$KILLER$", killer.getName()).replace("$POINTS$", ""));
				victim.sendMessage(message);

				message = ConstantHolder.RAGEMODE_PREFIX
						+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().MESSAGE_CURRENT_POINTS
								.replace("$POINTS$", Integer.toString(totalPoints)));
				killer.sendMessage(message);
				break;
			case "combataxe":
				int axePoints = ConstantHolder.POINTS_FOR_AXE_KILL;
				int axeMinusPoints = ConstantHolder.MINUS_POINTS_FOR_AXE_DEATH;
				totalPoints = addPoints(killer, PlayerList.getPlayersGame(killer), axePoints, true);
				addPoints(victim, PlayerList.getPlayersGame(victim), axeMinusPoints, false);

				killerPoints = playerpoints.get(killerUUID);
				int oldAxeKills = killerPoints.getAxeKills();
				int newAxeKills = oldAxeKills + 1;
				killerPoints.setAxeKills(newAxeKills);

				victimPoints = playerpoints.get(victimUUID);
				int oldAxeDeaths = victimPoints.getAxeDeaths();
				int newAxeDeaths = oldAxeDeaths + 1;
				victimPoints.setAxeDeaths(newAxeDeaths);

				message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
						PluginLoader.getMessages().MESSAGE_AXE_KILL.replace("$VICTIM$", victim.getName())
								.replace("$POINTS$", "+" + Integer.toString(axePoints)));
				killer.sendMessage(message);

				message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
						PluginLoader.getMessages().MESSAGE_AXE_DEATH.replace("$KILLER$", killer.getName())
								.replace("$POINTS$", Integer.toString(axeMinusPoints)));
				victim.sendMessage(message);

				message = ConstantHolder.RAGEMODE_PREFIX
						+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().MESSAGE_CURRENT_POINTS
								.replace("$POINTS$", Integer.toString(totalPoints)));
				killer.sendMessage(message);
				break;
			case "rageknife":
				int knifePoints = ConstantHolder.POINTS_FOR_KNIFE_KILL;
				totalPoints = addPoints(killer, PlayerList.getPlayersGame(killer), knifePoints, true);
				addPoints(victim, PlayerList.getPlayersGame(victim), 0, false);

				killerPoints = playerpoints.get(killerUUID);
				int oldKnifeKills = killerPoints.getKnifeKills();
				int newKnifeKills = oldKnifeKills + 1;
				killerPoints.setKnifeKills(newKnifeKills);

				victimPoints = playerpoints.get(victimUUID);
				int oldKnifeDeaths = victimPoints.getKnifeDeaths();
				int newKnifeDeaths = oldKnifeDeaths + 1;
				victimPoints.setKnifeDeaths(newKnifeDeaths);

				message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
						PluginLoader.getMessages().MESSAGE_KNIFE_KILL.replace("$VICTIM$", victim.getName())
								.replace("$POINTS$", "+" + Integer.toString(knifePoints)));
				killer.sendMessage(message);

				message = ConstantHolder.RAGEMODE_PREFIX
						+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().MESSAGE_KNIFE_DEATH
								.replace("$KILLER$", killer.getName()).replace("$POINTS$", ""));
				victim.sendMessage(message);

				message = ConstantHolder.RAGEMODE_PREFIX
						+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().MESSAGE_CURRENT_POINTS
								.replace("$POINTS$", Integer.toString(totalPoints)));
				killer.sendMessage(message);
				break;
			case "explosion":
				int explosionPoints = ConstantHolder.POINTS_FOR_EXPLOSION_KILL;
				totalPoints = addPoints(killer, PlayerList.getPlayersGame(killer), explosionPoints, true);
				addPoints(victim, PlayerList.getPlayersGame(victim), 0, false);

				killerPoints = playerpoints.get(killerUUID);
				int oldExplosionKills = killerPoints.getExplosionKills();
				int newExplosionKills = oldExplosionKills + 1;
				killerPoints.setExplosionKills(newExplosionKills);

				victimPoints = playerpoints.get(victimUUID);
				int oldExplosionDeaths = victimPoints.getExplosionDeaths();
				int newExplosionDeaths = oldExplosionDeaths + 1;
				victimPoints.setExplosionDeaths(newExplosionDeaths);

				message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
						PluginLoader.getMessages().MESSAGE_EXPLOSION_KILL.replace("$VICTIM$", victim.getName())
								.replace("$POINTS$", "+" + Integer.toString(explosionPoints)));
				killer.sendMessage(message);

				message = ConstantHolder.RAGEMODE_PREFIX
						+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().MESSAGE_EXPLOSION_DEATH
								.replace("$KILLER$", killer.getName()).replace("$POINTS$", ""));
				victim.sendMessage(message);

				message = ConstantHolder.RAGEMODE_PREFIX
						+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().MESSAGE_CURRENT_POINTS
								.replace("$POINTS$", Integer.toString(totalPoints)));
				killer.sendMessage(message);
				break;
			default:
				break;
			}

			// -------KillStreak messages-------

			PlayerPoints currentPoints = playerpoints.get(killerUUID);
			int currentStreak = currentPoints.getCurrentStreak();
			if (currentStreak == 3 || currentStreak % 5 == 0) {
				currentPoints.setPoints(currentPoints.getPoints() + (currentStreak * 10));

				message = ConstantHolder.RAGEMODE_PREFIX
						+ ChatColor.translateAlternateColorCodes('§',
								PluginLoader.getMessages().MESSAGE_STREAK
										.replace("$NUMBER$", Integer.toString(currentStreak))
										.replace("$POINTS$", "+" + Integer.toString(currentStreak * 10)));
				killer.sendMessage(message);
			}
			// -------End of KillStreak messages-------

			ScoreBoard board = ScoreBoard.allScoreBoards.get(PlayerList.getPlayersGame(killer));
			updateScoreBoard(killer, board);
			updateScoreBoard(victim, board);
		} else {
			String message = ConstantHolder.RAGEMODE_PREFIX
					+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().MESSAGE_SUICIDE);
			killer.sendMessage(message);
		}
		// TabGuiUpdater.updateTabGui(PlayerList.getPlayersGame(killer));
		// TabAPI.updateTabGuiListOverlayForGame(PlayerList.getPlayersGame(killer));
	}

	private static void updateScoreBoard(Player player, ScoreBoard scoreBoard) {
		PlayerPoints points = playerpoints.get(player.getUniqueId().toString());
		ScoreBoardHolder holder = scoreBoard.getScoreboards().get(player);

		String oldKD = holder.getOldKdLine();
		String newKD = ChatColor.YELLOW + Integer.toString(points.getKills()) + " / "
				+ Integer.toString(points.getDeaths()) + " " + ConstantHolder.SCOREBOARD_DEFAULT_KD;
		holder.setOldKdLine(newKD);
		scoreBoard.updateLine(player, oldKD, newKD, 0);

		String oldPoints = holder.getOldPointsLine();
		String newPoints = ChatColor.YELLOW + Integer.toString(points.getPoints()) + " "
				+ ConstantHolder.SCOREBOARD_DEFAULT_POINTS;
		holder.setOldPointsLine(newPoints);
		scoreBoard.updateLine(player, oldPoints, newPoints, 1);
	}

	public static void removePointsForPlayers(String[] playerUUIDs) {
		for (String playerUUID : playerUUIDs) {
			if (playerpoints.containsKey(playerUUID)) {
				playerpoints.remove(playerUUID);
			}
		}
	}

	public static PlayerPoints getPlayerPoints(String playerUUID) {
		if (playerpoints.containsKey(playerUUID)) {
			return playerpoints.get(playerUUID);
		} else {
			return null;
		}
	}

	private static int addPoints(Player player, String gameName, int points, boolean killer) { // returns
		// total
		// points
		String playerUUID = player.getUniqueId().toString();
		if (playerpoints.containsKey(playerUUID)) {
			PlayerPoints pointsHolder = playerpoints.get(playerUUID);
			int oldPoints = pointsHolder.getPoints();
			int oldKills = pointsHolder.getKills();
			int oldDeaths = pointsHolder.getDeaths();
			// playerpoints.remove(playerUUID);
			int totalPoints = oldPoints + points;
			int totalKills = oldKills;
			int totalDeaths = oldDeaths;
			int currentStreak = 0;
			int longestStreak = 0;
			if (killer) {
				totalKills++;
				currentStreak = pointsHolder.getCurrentStreak() + 1;
			} else {
				totalDeaths++;
				currentStreak = 0;
			}
			longestStreak = (currentStreak > pointsHolder.getLongestStreak()) ? currentStreak
					: pointsHolder.getLongestStreak();

			pointsHolder.setPoints(totalPoints);
			pointsHolder.setKills(totalKills);
			pointsHolder.setDeaths(totalDeaths);
			pointsHolder.setCurrentStreak(currentStreak);
			pointsHolder.setLongestStreak(longestStreak);
			// playerpoints.put(playerUUID, pointsHolder);
			return totalPoints;
		} else {
			int totalKills = 0;
			int totalDeaths = 0;
			int currentStreak = 0;
			int longestStreak = 0;
			if (killer) {
				totalKills = 1;
				currentStreak = 1;
				longestStreak = 1;
			} else {
				totalDeaths = 1;
				currentStreak = 0;
			}
			PlayerPoints pointsHolder = new PlayerPoints(playerUUID);
			pointsHolder.setPoints(points);
			pointsHolder.setKills(totalKills);
			pointsHolder.setDeaths(totalDeaths);
			pointsHolder.setCurrentStreak(currentStreak);
			pointsHolder.setLongestStreak(longestStreak);
			playerpoints.put(playerUUID, pointsHolder);
			return points;
		}
	}

	public static String calculateWinner(String game, String[] players) {
		String highest = UUID.randomUUID().toString();
		String goy = highest;
		int highestPoints = -200000000;
		int i = 0;
		int imax = players.length;
		while (i < imax) {
			if (playerpoints.containsKey(players[i])) {
				// Bukkit.broadcastMessage(Bukkit.getPlayer(UUID.fromString(players[i])).getName()
				// + " " + Integer.toString(i) + " " +
				// playerpoints.get(players[i]).getPoints() + " " +
				// Integer.toString(highestPoints));
				if (playerpoints.get(players[i]).getPoints() > highestPoints) {
					highest = players[i];
					highestPoints = playerpoints.get(players[i]).getPoints();
				}
				// else
				// Bukkit.broadcastMessage("nothighest"+players[i]);
			}
			// else
			// Bukkit.broadcastMessage(players[i]);
			i++;

		}

		if(goy == highest) {
			i = 0;
			while (i < imax) {
					String message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
							PluginLoader.getMessages().MESSAGE_PLAYER_WON
									.replace("$PLAYER$", "Herobrine")
									.replace("$GAME$", game));
					Bukkit.getPlayer(UUID.fromString(players[i])).sendMessage(message);
				i++;
			}
			return null;
		}
		else {
			playerpoints.get(highest).setWinner(true);
	
			i = 0;
			while (i < imax) {
				if (players[i].equals(highest)) {
					String message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
							PluginLoader.getMessages().MESSAGE_YOU_WON.replace("$GAME$", game));
					Bukkit.getPlayer(UUID.fromString(highest)).sendMessage(message);
				} else {
					String message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
							PluginLoader.getMessages().MESSAGE_PLAYER_WON
									.replace("$PLAYER$", Bukkit.getPlayer(UUID.fromString(highest)).getName())
									.replace("$GAME$", game));
					Bukkit.getPlayer(UUID.fromString(players[i])).sendMessage(message);
				}
				i++;
			}			
		}



		return highest;
	}

	// TODO Statistics for games and for the server globally. (Maybe also for
	// each map separately) (Total Axe kills, total bow kills,..., best axe
	// killer, best total killer,..., best victim,...

}