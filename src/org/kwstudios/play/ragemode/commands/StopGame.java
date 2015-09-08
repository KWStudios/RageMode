package org.kwstudios.play.ragemode.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.scoreboard.ScoreBoard;
import org.kwstudios.play.ragemode.scores.PlayerPoints;
import org.kwstudios.play.ragemode.scores.RageScores;
import org.kwstudios.play.ragemode.statistics.MySQLThread;
import org.kwstudios.play.ragemode.statistics.YAMLStats;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;
import org.kwstudios.play.ragemode.toolbox.GameBroadcast;
import org.kwstudios.play.ragemode.toolbox.GetGames;

public class StopGame {
	
	public StopGame(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		if(args.length >= 2) {
			if(PlayerList.isGameRunning(args[1])) {
				String[] players = PlayerList.getPlayersInGame(args[1]);
				
				RageScores.calculateWinner(args[1], players);
				
				ScoreBoard.allScoreBoards.get(args[1]).removeScoreBoard();
				
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
				GameBroadcast.broadcastToGame(args[1], ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().GAME_STOPPED.replace("$GAME$", args[1])));

				RageScores.removePointsForPlayers(players);
				PlayerList.setGameNotRunning(args[1]);

			}
			else {
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().GAME_NOT_RUNNING));
			}
		}
		else {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().MISSING_ARGUMENTS.replace("$USAGE$", "/rm stop <GameName>")));
		}
	}
	
	public static void stopGame(String game) {
		if(PlayerList.isGameRunning(game)) {
			String[] players = PlayerList.getPlayersInGame(game);
			RageScores.calculateWinner(game, players);
			
			int f = 0;
			int fmax = players.length;
			List<PlayerPoints> lPP = new ArrayList<PlayerPoints>();			
			boolean doSQL = PluginLoader.getInstance().getConfig().getString("settings.global.statistics.type").equalsIgnoreCase("mySQL");
			while(f < fmax) {				
				if(RageScores.getPlayerPoints(players[f]) != null) {
					PlayerPoints pP = RageScores.getPlayerPoints(players[f]);
					lPP.add(pP);
					
					if(doSQL) {
						Thread sthread = new Thread(new MySQLThread(pP));
						sthread.start();						
					}

				}				
				f++;
			}
			Thread thread = new Thread(YAMLStats.createPlayersStats(lPP));
			thread.start();

			ScoreBoard.allScoreBoards.get(game).removeScoreBoard();
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
			
			GameBroadcast.broadcastToGame(game, ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().GAME_STOPPED.replace("$GAME$", game)));
			PlayerList.setGameNotRunning(game);

		}
	}
	
	public static void stopAllGames(FileConfiguration fileConfiguration, Logger logger) {
		logger.info("RageMode is searching for games to stop...");
		
		String[] games = GetGames.getGameNames(fileConfiguration);
		
		int i = 0;
		int imax = games.length;
		
		while(i < imax) {
			if(PlayerList.isGameRunning(games[i])) {
				
				logger.info("Stopping " + games[i] + " ...");
				ScoreBoard.allScoreBoards.get(games[i]).removeScoreBoard();
				
				String[] players = PlayerList.getPlayersInGame(games[i]);
				RageScores.calculateWinner(games[i], players);
				
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
				RageScores.removePointsForPlayers(players);
				PlayerList.setGameNotRunning(games[i]);
				logger.info(games[i] + " has been stopped.");
			}
		i++;
		}
		logger.info("Ragemode: All games stopped!");
	}
}
