package org.kwstudios.play.ragemode.commands;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
import org.kwstudios.play.ragemode.gameLogic.RageScores;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;
import org.kwstudios.play.ragemode.toolbox.GameBroadcast;
import org.kwstudios.play.ragemode.toolbox.GetGames;


public class StopGame {
	
	public StopGame(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		if(args.length >= 2) {
			if(PlayerList.isGameRunning(args[1])) {
				String[] players = PlayerList.getPlayersInGame(args[1]);
				if(players != null) {
					int i = 0;
					int imax = players.length;
					
					while(i < imax) {
						if(players[i] != null) {
							PlayerList.removePlayer(Bukkit.getPlayer(UUID.fromString(players[i])));
						}
						i++;
					}
				}
				PlayerList.setGameNotRunning(args[1]);
				GameBroadcast.broadcastToGame(args[1], ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + args[1] + ChatColor.DARK_GREEN + " has been stopped.");
			}
			else {
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + "This game isn't running.");
			}
		}
		else {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_RED + "Missing arguments! Usage: /rm stop <GameName>");
		}
	}
	
	public static void stopGame(String game) {
		if(PlayerList.isGameRunning(game)) {
			String[] players = PlayerList.getPlayersInGame(game);
			if(players != null) {
				int i = 0;
				int imax = players.length;
				
				while(i < imax) {
					if(players[i] != null) {
						PlayerList.removePlayer(Bukkit.getPlayer(UUID.fromString(players[i])));
					}
					i++;
				}
			}
			RageScores.removePointsForPlayers(players);
			PlayerList.setGameNotRunning(game);
			GameBroadcast.broadcastToGame(game, ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_AQUA + game + ChatColor.DARK_GREEN + " has been stopped.");

		}
	}
	
	public static void stopAllGames(FileConfiguration fileConfiguration, Logger logger) {
		logger.info("RageMode is searching for games to stop ...");
		
		String[] games = GetGames.getGameNames(fileConfiguration);
		
		int i = 0;
		int imax = games.length;
		
		while(i < imax) {
			if(PlayerList.isGameRunning(games[i])) {
				
				logger.info("stopping " + games[i] + " ...");
				
				String[] players = PlayerList.getPlayersInGame(games[i]);
				if(players != null) {
					int n = 0;
					int nmax = players.length;
					
					while(n < nmax) {
						if(players[n] != null) {
							PlayerList.removePlayer(Bukkit.getPlayer(UUID.fromString(players[n])));
						}
						n++;
					}
				}
				PlayerList.setGameNotRunning(games[i]);
				logger.info(games[i] + " has been stopped.");
			}
		i++;
		}
		logger.info("Ragemode: all games stopped!");
	}
}
