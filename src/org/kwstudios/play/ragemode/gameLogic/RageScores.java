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

	public static void addPointsToPlayer(Player killer, Player victim, String killCause) {
		switch (killCause.toLowerCase()) {
		case "ragebow":
			int bowPoints = ConstantHolder.POINTS_FOR_BOW_KILL;
			addPoints(killer, PlayerList.getPlayersGame(killer), bowPoints);
			killer.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + "You killed "
					+ ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + killer.getName() + ChatColor.DARK_AQUA
					+ " with a direct arrow hit. " + ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "+"
					+ bowPoints);
			break;
		case "combataxe":
			int axePoints = ConstantHolder.POINTS_FOR_AXE_KILL;
			addPoints(killer, PlayerList.getPlayersGame(killer), axePoints);
			break;
		case "rageknife":
			int knifePoints = ConstantHolder.POINTS_FOR_KNIFE_KILL;
			addPoints(killer, PlayerList.getPlayersGame(killer), knifePoints);
			break;
		case "explosion":
			int explosionPoints = ConstantHolder.POINTS_FOR_EXPLOSION_KILL;
			addPoints(killer, PlayerList.getPlayersGame(killer), explosionPoints);
			break;
		default:
			break;
		}
	}

	private static void addPoints(Player player, String gameName, int points) {
		String playerUUID = player.getUniqueId().toString();
		if (playerpoints.containsKey(playerUUID)) {
			int oldPoints = playerpoints.get(playerUUID);
			playerpoints.remove(playerUUID);
			int totalPoints = oldPoints + points;
			playerpoints.put(playerUUID, totalPoints);
		} else {
			playerpoints.put(playerUUID, points);
		}
	}

	// TODO Statistics for games and for the server globally. (Maybe also for
	// each map separately) (Total Axe kills, total bow kills,..., best axe
	// killer, best total killer,..., best victim,...

}
