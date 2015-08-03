package org.kwstudios.play.ragemode.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;
import org.kwstudios.play.ragemode.toolbox.GetGames;

public class ListGames {
	public ListGames(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		String[] games = GetGames.getGameNames(fileConfiguration);  //TODO Test if there are keys or not. -> NullPointerException
		int i = 0;
		int imax = games.length;
		
		player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + "Listing all available ragemode games...");
		
		while(i < imax) {
			if(PlayerList.isGameRunning(games[i])) {
				player.sendMessage(i + 1 + ".) " + games[i] + ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + " running");
			}
			else {
				player.sendMessage(i + 1 + ".) " + games[i] + ChatColor.GRAY + " idle");
			}
			i++;
		}
	}
}
