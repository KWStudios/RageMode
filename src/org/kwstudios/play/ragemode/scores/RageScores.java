package org.kwstudios.play.ragemode.scores;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
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

			switch (killCause.toLowerCase()) {
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

				killer.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + "You killed "
						+ ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + victim.getName()
						+ ChatColor.RESET.toString() + ChatColor.DARK_AQUA + " with a direct arrow hit. "
						+ ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "+" + bowPoints);

				victim.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + "You were killed by "
						+ ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + killer.getName());

				killer.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + "You now have "
						+ ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + totalPoints
						+ ChatColor.RESET.toString() + ChatColor.DARK_AQUA.toString() + " points.");
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

				killer.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + "You killed "
						+ ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + victim.getName()
						+ ChatColor.RESET.toString() + ChatColor.DARK_AQUA + " with your CombatAxe. "
						+ ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "+" + axePoints);

				victim.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + "You were killed by "
						+ ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + killer.getName()
						+ ChatColor.BOLD.toString() + ChatColor.DARK_RED.toString() + axeMinusPoints);

				killer.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + "You now have "
						+ ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + totalPoints
						+ ChatColor.RESET.toString() + ChatColor.DARK_AQUA.toString() + " points.");
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

				killer.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + "You killed "
						+ ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + victim.getName()
						+ ChatColor.RESET.toString() + ChatColor.DARK_AQUA + " with your RageKnife. "
						+ ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "+" + knifePoints);

				victim.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + "You were killed by "
						+ ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + killer.getName());

				killer.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + "You now have "
						+ ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + totalPoints
						+ ChatColor.RESET.toString() + ChatColor.DARK_AQUA.toString() + " points.");
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

				killer.sendMessage(
						ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + "You killed " + ChatColor.GOLD.toString()
								+ ChatColor.BOLD.toString() + victim.getName() + ChatColor.RESET.toString()
								+ ChatColor.DARK_AQUA + " by causing heavy explosions with your RageBow. "
								+ ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "+" + explosionPoints);

				victim.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + "You were killed by "
						+ ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + killer.getName());

				killer.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + "You now have "
						+ ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + totalPoints
						+ ChatColor.RESET.toString() + ChatColor.DARK_AQUA.toString() + " points.");
				break;
			default:
				break;
			}
			ScoreBoard board = ScoreBoard.allScoreBoards.get(PlayerList.getPlayersGame(killer));
			updateScoreBoard(killer, board);
			updateScoreBoard(victim, board);
		} else {
			killer.sendMessage(
					ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + "You killed yourself you silly idiot.");
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
			//playerpoints.remove(playerUUID);
			int totalPoints = oldPoints + points;
			int totalKills = oldKills;
			int totalDeaths = oldDeaths;
			if (killer) {
				totalKills++;
			} else {
				totalDeaths++;
			}
			pointsHolder.setPoints(totalPoints);
			pointsHolder.setKills(totalKills);
			pointsHolder.setDeaths(totalDeaths);
			//playerpoints.put(playerUUID, pointsHolder);
			return totalPoints;
		} else {
			int totalKills = 0;
			int totalDeaths = 0;
			if (killer) {
				totalKills = 1;
			} else {
				totalDeaths = 1;
			}
			PlayerPoints pointsHolder = new PlayerPoints(playerUUID);
			pointsHolder.setPoints(points);
			pointsHolder.setKills(totalKills);
			pointsHolder.setDeaths(totalDeaths);
			playerpoints.put(playerUUID, pointsHolder);
			return points;
		}
	}

	public static void calculateWinner(String game, String[] players) {
		String highest = UUID.randomUUID().toString();
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
			}
			i++;
			
			playerpoints.get(highest).setWinner(true);
			
		}
		i = 0;
		while (i < imax) {
			if (players[i].equals(highest)) {
				Bukkit.getPlayer(UUID.fromString(highest))
						.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.LIGHT_PURPLE + "You won the game "
								+ ChatColor.GOLD + game + ChatColor.LIGHT_PURPLE + ".");
			} else {
				Bukkit.getPlayer(UUID.fromString(players[i]))
						.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_GREEN
								+ Bukkit.getPlayer(UUID.fromString(highest)).getName() + ChatColor.LIGHT_PURPLE
								+ " won the game " + ChatColor.GOLD + game + ChatColor.LIGHT_PURPLE + ".");
			}
			i++;
		}
	}

	// TODO Statistics for games and for the server globally. (Maybe also for
	// each map separately) (Total Axe kills, total bow kills,..., best axe
	// killer, best total killer,..., best victim,...

}