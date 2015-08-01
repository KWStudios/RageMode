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
	private static String[] list = new String[1]; //[Gamemane,Playername x overallMaxPlayers,Gamename,...]
	public static TableList<Player, Location> oldLocations = new TableList<Player, Location>();
	
	public PlayerList(FileConfiguration fileConfiguration) {
		PlayerList.fileConfiguration = fileConfiguration;
		list = Arrays.copyOf(list, GetGames.getConfigGamesCount(fileConfiguration)*(GetGames.getOverallMaxPlayers(fileConfiguration)+1));
	}
	
	public static void updateListSize(FileConfiguration fileConfiguration){
		list = Arrays.copyOf(list, GetGames.getConfigGamesCount(fileConfiguration)*(GetGames.getOverallMaxPlayers(fileConfiguration)+1));
	}

	public static boolean addPlayer(Player player, String game, FileConfiguration fileConfiguration) {
		int i,n;
		i = 0;
		n = 0;
		int kickposition;
		int imax = GetGames.getConfigGamesCount(fileConfiguration)*(GetGames.getOverallMaxPlayers(fileConfiguration)+1);
		int playersPerGame = GetGames.getOverallMaxPlayers(fileConfiguration);
		while(i <= imax) {
			if(player.getUniqueId().toString().equals(list[i])) {
				player.sendMessage("You are already in a game. You can leave it by typing /rm leave .");
				return false;
			}
			i++;
		}
		i = 0;
		while(i <= imax) {
			if(list[i].equals(game)) {
				n = i;
				while(n <= GetGames.getMaxPlayers(game, fileConfiguration)) {
					if(list[n] == null) {
						list[n] = player.getUniqueId().toString();
						player.sendMessage("You joined " + ChatColor.DARK_AQUA + game + ChatColor.WHITE + ".");	
						return true;
					}
					n++;
				}
				if(player.hasPermission("rm.vip")) {
					Random random = new Random();
					kickposition = random.nextInt(GetGames.getMaxPlayers(game, fileConfiguration)-1);
					kickposition = kickposition + 1 + i;
					n = 0;
					Player playerToKick = Bukkit.getPlayer(UUID.fromString(list[kickposition]));
					while(n <= oldLocations.getFirstLength()) {
						if(oldLocations.getFromFirstObject(n) == playerToKick) {
							playerToKick.teleport(oldLocations.getFromSecondObject(n));
						}
						n++;
					}
					list[kickposition] = player.getUniqueId().toString();
					playerToKick.sendMessage("You were kicked out of the Game to make room for a VIP.");
				}
				
			}
			i = i + playersPerGame;
		}
		
		player.sendMessage("The game you wish to join wasn't found.");
		return false;
	}
	
	public static boolean removePlayer(Player player) {
		int i = 0;
		int imax = GetGames.getConfigGamesCount(fileConfiguration)*(GetGames.getOverallMaxPlayers(fileConfiguration)+1);
		
		while(i < imax) {
			if(player.getUniqueId().toString().equals(list[i])) {
				player.sendMessage("You left your current Game");
				return true;
			}
		}
		return false;	
	}

}







