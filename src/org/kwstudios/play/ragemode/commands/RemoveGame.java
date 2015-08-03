package org.kwstudios.play.ragemode.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;
import org.kwstudios.play.ragemode.toolbox.GetGames;

public class RemoveGame {
	public RemoveGame(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		if(args[2] != null) {
			String game = args[2];
			
			if(!GetGames.isGameExistent(game, fileConfiguration)) {
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_RED + "Don't remove nonexistent games!");
				return;
			}
			else {
				if(PlayerList.isGameRunning(game)) {
					player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_RED + "This game is running at the moment. Please wait until it is over.");
					return;
				}
				else {
					fileConfiguration.set("settings.games." + game, null);
				}			
			}
		}
		else {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_RED + "Missing arguments. Usage: /rm remove <GameName>");
			return;
		}
	}
}