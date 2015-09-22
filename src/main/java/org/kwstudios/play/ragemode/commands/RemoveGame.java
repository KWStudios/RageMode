package org.kwstudios.play.ragemode.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;
import org.kwstudios.play.ragemode.toolbox.GetGames;

public class RemoveGame {
	public RemoveGame(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		if (args.length >= 2) {
			String game = args[1];

			if (!GetGames.isGameExistent(game, fileConfiguration)) {
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
						PluginLoader.getMessages().REMOVED_NON_EXISTENT_GAME));
				return;
			} else {
				if (PlayerList.isGameRunning(game)) {
					player.sendMessage(ConstantHolder.RAGEMODE_PREFIX
							+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().GAME_RUNNING));
					return;
				} else {
					PlayerList.deleteGameFromList(game);

					fileConfiguration.set("settings.games." + game, null);

					player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
							PluginLoader.getMessages().REMOVED_SUCCESSFULLY.replace("$GAME$", game)));
				}
			}
		} else {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
					PluginLoader.getMessages().MISSING_ARGUMENTS.replace("$USAGE$", "/rm remove <GameName>")));
			return;
		}
	}
}
