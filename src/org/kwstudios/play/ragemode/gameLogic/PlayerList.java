package org.kwstudios.play.ragemode.gameLogic;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.toolbox.GetGames;
import org.kwstudios.play.ragemode.toolbox.TableList;

public class PlayerList {
	private static FileConfiguration fileConfiguration;
	private static String[] list = new String[1]; // [Gamemane,Playername x
													// overallMaxPlayers,Gamename,...]
	public static TableList<Player, Location> oldLocations = new TableList<Player, Location>();
	private static String[] runningGames = new String[1];

	public PlayerList(FileConfiguration fileConfiguration) {
		int i = 0;
		int imax = GetGames.getConfigGamesCount(fileConfiguration);
		String[] games = GetGames.getGameNames(fileConfiguration);
		PlayerList.fileConfiguration = fileConfiguration;
		list = Arrays
				.copyOf(list,
						GetGames.getConfigGamesCount(fileConfiguration)
								* (GetGames
										.getOverallMaxPlayers(fileConfiguration) + 1));
		while(i < imax) {
			list[i*GetGames.getOverallMaxPlayers(fileConfiguration)] = games[i];
			i++;
		}
		runningGames = Arrays.copyOf(runningGames,
				GetGames.getConfigGamesCount(fileConfiguration));
	}

	public static String[] getPlayersInGame(String game) {
		String[] players = new String[GetGames.getMaxPlayers(game,
				fileConfiguration)];

		int i = 0;
		int n;
		int imax = GetGames.getConfigGamesCount(fileConfiguration)
				* (GetGames.getOverallMaxPlayers(fileConfiguration) + 1);
		int playersPerGame = GetGames.getOverallMaxPlayers(fileConfiguration);
		while (i <= imax) {
			if(list[i] != null) {
				if (list[i].equals(game)) {
					n = i;
					while (n < GetGames.getMaxPlayers(game, fileConfiguration)) {
						if(list[n + 1] == null)
							break;
						players[n] = list[n + 1];
						n++;
					}
					players = Arrays.copyOf(players, n);
				}
			}
		    i = i + playersPerGame;		
		}
		return players;
	}

	public static void updateListSize(FileConfiguration fileConfiguration) {
		list = Arrays
				.copyOf(list,
						GetGames.getConfigGamesCount(fileConfiguration)
								* (GetGames
										.getOverallMaxPlayers(fileConfiguration) + 1));
	}

	public static boolean addPlayer(Player player, String game,
			FileConfiguration fileConfiguration) {
		if (isGameRunning(game)) {
			player.sendMessage("This Game is already running.");
			return false;
		}

		int i, n;
		i = 0;
		n = 0;
		int kickposition;
		int imax = GetGames.getConfigGamesCount(fileConfiguration)
				* (GetGames.getOverallMaxPlayers(fileConfiguration) + 1);
		int playersPerGame = GetGames.getOverallMaxPlayers(fileConfiguration);
		while (i < imax) {
			if (player.getUniqueId().toString().equals(list[i])) {
				player.sendMessage("You are already in a game. You can leave it by typing /rm leave");
				return false;
			}
			i++;
		}
		i = 0;
		while (i < imax) {
			if (list[i] != null) {
				if (list[i].equals(game)) {
					n = i;
					while (n <= GetGames.getMaxPlayers(game, fileConfiguration)) {
						if (list[n] == null) {
							list[n] = player.getUniqueId().toString();
							player.sendMessage("You joined "
									+ ChatColor.DARK_AQUA + game
									+ ChatColor.WHITE + "." + Integer.toString(getPlayersInGame(game).length));

							if (getPlayersInGame(game).length == 2) {
								new LobbyTimer(game, getPlayersInGame(game),
										fileConfiguration);
							}
							return true;
						}
						n++;
					}
				}
				if (player.hasPermission("rm.vip")) {
					Random random = new Random();
					kickposition = random.nextInt(GetGames.getMaxPlayers(game,
							fileConfiguration) - 1);
					kickposition = kickposition + 1 + i;
					n = 0;
					Player playerToKick = Bukkit.getPlayer(UUID
							.fromString(list[kickposition]));
					while (n <= oldLocations.getFirstLength()) {
						if (oldLocations.getFromFirstObject(n) == playerToKick) {
							playerToKick.teleport(oldLocations
									.getFromSecondObject(n));
						}
						n++;
					}
					list[kickposition] = player.getUniqueId().toString();
					playerToKick
							.sendMessage("You were kicked out of the Game to make room for a VIP.");

					if (getPlayersInGame(game).length == 2) {
						new LobbyTimer(game, getPlayersInGame(game),
								fileConfiguration);
					}
					return true;
				} else {
					player.sendMessage("This Game is already full!");
					return false;
				}

			}
			i = i + playersPerGame;
		}

		player.sendMessage("The game you wish to join wasn't found.");
		return false;
	}

	public static boolean removePlayer(Player player) {
		int i = 0;
		int n = 0;
		int imax = GetGames.getConfigGamesCount(fileConfiguration)
				* (GetGames.getOverallMaxPlayers(fileConfiguration) + 1);

		while (i < imax) {
			if(list[i] != null) {
				if (player.getUniqueId().toString().equals(list[i])) {
					player.sendMessage("You left your current Game");

					while (n < oldLocations.getFirstLength()) {
						if (oldLocations.getFromFirstObject(n) == player) {
							player.teleport(oldLocations.getFromSecondObject(n));
							oldLocations.removeFromBoth(n);
						}
						n++;
					}
					list[i] = null;
					return true;
				}
			}
			i++;
		}
		return false;
	}

	public static boolean isGameRunning(String game) {
		int i = 0;
		int imax = runningGames.length;
		while (i < imax) {
			if (runningGames[i] != null) {
				if (runningGames[i].equals(game)) {
					return true;
				}
			}
			i++;
		}
		return false;
	}

	public static boolean setGameRunning(String game) {
		if (!GetGames.isGameExistent(game, fileConfiguration))
			return false;
		int i = 0;
		int imax = GetGames.getConfigGamesCount(fileConfiguration);
		while (i < imax) {
			if (runningGames[i].equals(game))
				return false;
			i++;
		}
		i = 0;
		while (i < imax) {
			if (runningGames[i] == null) {
				runningGames[i] = game;
			}
			i++;
		}
		return false;
	}
	
	public static boolean setGameNotRunning(String game) {
		if (!GetGames.isGameExistent(game, fileConfiguration))
			return false;
		
		int i = 0;
		int imax = GetGames.getConfigGamesCount(fileConfiguration);
		
		while (i < imax) {
			if (runningGames[i].equals(game)) {
				runningGames[i] = null;
				return true;			
			}
			i++;
		}
		return false;				
	}

	public static boolean isPlayerPlaying(String player) {
		int i = 0;
		int imax = list.length;
		
		while(i < imax) {
			if(list[i].equals(player)) {
				return true;
			}
			i++;			
		}
		return false;
	}
}
