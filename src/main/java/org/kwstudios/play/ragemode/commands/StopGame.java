package org.kwstudios.play.ragemode.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.events.EventListener;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
import org.kwstudios.play.ragemode.holo.HoloHolder;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.runtimeRPP.RuntimeRPPManager;
import org.kwstudios.play.ragemode.scores.PlayerPoints;
import org.kwstudios.play.ragemode.scores.RageScores;
import org.kwstudios.play.ragemode.signs.SignCreator;
import org.kwstudios.play.ragemode.statistics.MySQLThread;
import org.kwstudios.play.ragemode.statistics.YAMLStats;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;
import org.kwstudios.play.ragemode.toolbox.GameBroadcast;
import org.kwstudios.play.ragemode.toolbox.GetGames;
import org.kwstudios.play.ragemode.toolbox.TitleAPI;

public class StopGame {

	public StopGame(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		if (args.length >= 2) {
			if (PlayerList.isGameRunning(args[1])) {
				String[] players = PlayerList.getPlayersInGame(args[1]);

				RageScores.calculateWinner(args[1], players);

				if (players != null) {
					int i = 0;
					int imax = players.length;

					while (i < imax) {
						if (players[i] != null) {
							PlayerList.removePlayer(Bukkit.getPlayer(UUID.fromString(players[i])));
							PlayerList.removePlayerSynced(Bukkit.getPlayer(UUID.fromString(players[i])));
						}
						i++;
					}
				}
				GameBroadcast.broadcastToGame(args[1],
						ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
								PluginLoader.getMessages().GAME_STOPPED.replace("$GAME$", args[1])));

				RageScores.removePointsForPlayers(players);
				PlayerList.setGameNotRunning(args[1]);
				SignCreator.updateAllSigns(args[1]);

			} else {
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX
						+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().GAME_NOT_RUNNING));
			}
		} else {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
					PluginLoader.getMessages().MISSING_ARGUMENTS.replace("$USAGE$", "/rm stop <GameName>")));
		}
	}

	public static void stopGame(String game) {
		boolean winnervalid = false;
		if (PlayerList.isGameRunning(game)) {
			String[] players = PlayerList.getPlayersInGame(game);
			String winnerUUID = RageScores.calculateWinner(game, players);
			if(winnerUUID != null) {
				if(UUID.fromString(winnerUUID) != null) {
					if (Bukkit.getPlayer(UUID.fromString(winnerUUID)) != null) {
						winnervalid = true;
						Player winner = Bukkit.getPlayer(UUID.fromString(winnerUUID));
						for (String playerUUID : players) {
							Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
							String title = ChatColor.DARK_GREEN + winner.getName() + ChatColor.GOLD + " won this round!";
							String subtitle = ChatColor.GOLD + "He has got " + ChatColor.DARK_PURPLE
									+ Integer.toString(RageScores.getPlayerPoints(winnerUUID).getPoints()) + ChatColor.GOLD
									+ " points and his K/D is " + ChatColor.DARK_PURPLE
									+ Integer.toString(RageScores.getPlayerPoints(winnerUUID).getKills()) + " / "
									+ Integer.toString(RageScores.getPlayerPoints(winnerUUID).getDeaths()) + ChatColor.GOLD
									+ ".";
							PlayerList.removePlayerSynced(player);
							TitleAPI.sendTitle(player, 20, 200, 20, title, subtitle);
		//					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 5, false, false), true);
						}
					}				
				}				
			}
			if(winnervalid == false) {
				for (String playerUUID : players) {
					Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
					String title = ChatColor.DARK_GREEN + "Herobrine" + ChatColor.GOLD + " won this round!";
					PlayerList.removePlayerSynced(player);
					TitleAPI.sendTitle(player, 20, 200, 20, title, null);
				}
			}


			if (!EventListener.waitingGames.containsKey(game)) {
				EventListener.waitingGames.put(game, true);
			} else {
				EventListener.waitingGames.remove(game);
				EventListener.waitingGames.put(game, true);
			}
		
			
			final String gameName = game;
			PluginLoader.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(PluginLoader.getInstance(),
					new Runnable() {
						@Override
						public void run() {
							finishStopping(gameName);
							if (EventListener.waitingGames.containsKey(gameName)) {
								EventListener.waitingGames.remove(gameName);
							}
						}
					}, 200);
		}
	}

	private static void finishStopping(String game) {
		if (PlayerList.isGameRunning(game)) {
			final String[] players = PlayerList.getPlayersInGame(game);
			int f = 0;
			int fmax = players.length;
			List<PlayerPoints> lPP = new ArrayList<PlayerPoints>();
			boolean doSQL = PluginLoader.getInstance().getConfig().getString("settings.global.statistics.type")
					.equalsIgnoreCase("mySQL");
			while (f < fmax) {
				if (RageScores.getPlayerPoints(players[f]) != null) {
					final PlayerPoints pP = RageScores.getPlayerPoints(players[f]);
					lPP.add(pP);

					if (doSQL) {
						Thread sthread = new Thread(new MySQLThread(pP));
						sthread.start();
					}
					Bukkit.getServer().getScheduler().runTaskAsynchronously(PluginLoader.getInstance(), new Runnable() {
						
						@Override
						public void run() {
							RuntimeRPPManager.updatePlayerEntry(pP);
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PluginLoader.getInstance(), new Runnable() {

								@Override
								public void run() {
									for (String playerUUID : players) {				
										HoloHolder.updateHolosForPlayer(Bukkit.getPlayer(UUID.fromString(playerUUID)));				
									}	
								}
								
							});
						}
					});		
				}
				f++;
			}
			Thread thread = new Thread(YAMLStats.createPlayersStats(lPP));
			thread.start();

			if (players != null) {
				int i = 0;
				int imax = players.length;

				while (i < imax) {
					if (players[i] != null) {

						PlayerList.removePlayer(Bukkit.getPlayer(UUID.fromString(players[i])));
					}
					i++;
				}
			}
			RageScores.removePointsForPlayers(players);

			GameBroadcast.broadcastToGame(game,
					ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
							PluginLoader.getMessages().GAME_STOPPED.replace("$GAME$", game)));
			PlayerList.setGameNotRunning(game);
			SignCreator.updateAllSigns(game);			
		}
	}

	public static void stopAllGames(FileConfiguration fileConfiguration, Logger logger) {
		logger.info("RageMode is searching for games to stop...");

		String[] games = GetGames.getGameNames(fileConfiguration);

		int i = 0;
		int imax = games.length;

		while (i < imax) {
			if (PlayerList.isGameRunning(games[i])) {

				logger.info("Stopping " + games[i] + " ...");

				String[] players = PlayerList.getPlayersInGame(games[i]);
				RageScores.calculateWinner(games[i], players);

				if (players != null) {
					int n = 0;
					int nmax = players.length;

					while (n < nmax) {
						if (players[n] != null) {
							PlayerList.removePlayer(Bukkit.getPlayer(UUID.fromString(players[n])));
						}
						n++;
					}
				}
				RageScores.removePointsForPlayers(players);
				PlayerList.setGameNotRunning(games[i]);
				SignCreator.updateAllSigns(games[i]);
				logger.info(games[i] + " has been stopped.");
			}
			i++;
		}
		logger.info("Ragemode: All games stopped!");
	}
}
