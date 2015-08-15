package org.kwstudios.play.ragemode.gameLogic;

import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class RageScores {

	private static HashMap<String, Integer> playerpoints = new HashMap<String, Integer>();
	// private static TableList<String, String> playergame = new
	// TableList<String, String>(); ----> User PlayerList.getPlayersInGame
	// instead
	private static int totalPoints = 0;

	public static void addPointsToPlayer(Player killer, Player victim, String killCause) {
		if (!killer.getUniqueId().toString().equals(victim.getUniqueId().toString())) {
			switch (killCause.toLowerCase()) {
			case "ragebow":
				int bowPoints = ConstantHolder.POINTS_FOR_BOW_KILL;
				totalPoints = addPoints(killer, PlayerList.getPlayersGame(killer), bowPoints);
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
				totalPoints = addPoints(killer, PlayerList.getPlayersGame(killer), axePoints);
				totalPoints = addPoints(victim, PlayerList.getPlayersGame(victim), axeMinusPoints);
				killer.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + "You killed "
						+ ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + victim.getName()
						+ ChatColor.RESET.toString() + ChatColor.DARK_AQUA + " with your CombatAxe. "
						+ ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "+" + axePoints);

				victim.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + "You were killed by "
						+ ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + killer.getName()
						+ ChatColor.BOLD.toString() + ChatColor.DARK_RED.toString() + " -" + axeMinusPoints);

				killer.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + "You now have "
						+ ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + totalPoints
						+ ChatColor.RESET.toString() + ChatColor.DARK_AQUA.toString() + " points.");
				break;
			case "rageknife":
				int knifePoints = ConstantHolder.POINTS_FOR_KNIFE_KILL;
				totalPoints = addPoints(killer, PlayerList.getPlayersGame(killer), knifePoints);
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
				totalPoints = addPoints(killer, PlayerList.getPlayersGame(killer), explosionPoints);
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
		} else {
			killer.sendMessage(
					ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + "You killed yourself you silly idiot.");
		}
	}

	public static void removePointsForPlayers(String[] playerUUIDs) {
		for (String playerUUID : playerUUIDs) {
			if (playerpoints.containsKey(playerUUID)) {
				playerpoints.remove(playerUUID);
			}
		}
	}

	private static int addPoints(Player player, String gameName, int points) { // returns
																				// total
																				// points
		String playerUUID = player.getUniqueId().toString();
		if (playerpoints.containsKey(playerUUID)) {
			int oldPoints = playerpoints.get(playerUUID);
			playerpoints.remove(playerUUID);
			int totalPoints = oldPoints + points;
			playerpoints.put(playerUUID, totalPoints);
			return totalPoints;
		} else {
			playerpoints.put(playerUUID, points);
			return points;
		}
	}

	// TODO Statistics for games and for the server globally. (Maybe also for
	// each map separately) (Total Axe kills, total bow kills,..., best axe
	// killer, best total killer,..., best victim,...

}
