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
	private FileConfiguration fileConfiguration;
	private static String[] list = new String[1]; //[Gamemane,Playername x overallMaxPlayers,Gamename,...]
	public static TableList<Player, Location> oldLocation = new TableList();
	
	public PlayerList(FileConfiguration fileConfiguration) {
		this.fileConfiguration = fileConfiguration;
		list = Arrays.copyOf(list, GetGames.getConfigGamesCount(fileConfiguration)*(GetGames.getOverallMaxPlayers(fileConfiguration)+1));
	}
	
	

	public boolean addPlayer(Player player, String game, FileConfiguration fileconfiguration) {
		int i,n;
		i = 0;
		n = 0;
		int kickposition;
		int imax = GetGames.getConfigGamesCount(fileConfiguration)*(GetGames.getOverallMaxPlayers(fileConfiguration)+1);
		int playersPerGame = GetGames.getOverallMaxPlayers(fileconfiguration);
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
				while(n <= GetGames.getMaxPlayers(game, fileconfiguration)) {
					if(list[n] == null) {
						list[n] = player.getUniqueId().toString();
						player.sendMessage("You joined " + ChatColor.DARK_AQUA + game + ChatColor.WHITE + ".");	
					}
					n++;
				}
				if(player.hasPermission("rm.vip")) {
					Random random = new Random();
					kickposition = random.nextInt(GetGames.getMaxPlayers(game, fileconfiguration)-1);
					kickposition = kickposition + 1 + i;
//					Bukkit.getPlayer(UUID.fromString(list[kickposition])).teleport();
				}
				
			}
			i = i + playersPerGame;
		}
		
		player.sendMessage("The game you wish to jon wasn't found.");
		return false;
	}
	public static void updateListSize(FileConfiguration fileConfiguration){
		list = Arrays.copyOf(list, GetGames.getConfigGamesCount(fileConfiguration)*(GetGames.getOverallMaxPlayers(fileConfiguration)+1));
	}
}



