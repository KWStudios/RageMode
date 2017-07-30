package org.kwstudios.play.ragemode.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class AddGame {

	private Player player;
	private String[] args;
	private FileConfiguration fileConfiguration;

	public AddGame(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		this.player = player;
		this.args = args;
		this.fileConfiguration = fileConfiguration;

		addGametoConfig();
	}

	private void addGametoConfig() {
		if (args.length < 3) {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX
					+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().MISSING_ARGUMENTS
							.replace("$USAGE$", "/rm add <GameName> <MaxPlayers>")));
			return;
		}
		int x;
		try {
			x = Integer.parseInt(args[2]);
		} catch (Exception e) {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
					PluginLoader.getMessages().NOT_A_NUMBER.replace("$WRONG_NUMBER$", args[2])));
			return;
		}

		if (fileConfiguration.isSet("settings.games." + args[1])) {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
					PluginLoader.getMessages().ALREADY_EXISTS.replace("$GAME$", args[1])));
			return;
		}
		
		if(x < 2) {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
					PluginLoader.getMessages().AT_LEAST_TWO));
			return;
		}

		PlayerList.addGameToList(args[1], x);

		ConfigFactory.setString("settings.games", args[1], "", fileConfiguration);
		ConfigFactory.setInt("settings.games." + args[1], "maxplayers", Integer.parseInt(args[2]), fileConfiguration);
		ConfigFactory.setString("settings.games." + args[1], "world", player.getWorld().getName(), fileConfiguration);

		player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
				PluginLoader.getMessages().ADDED_SUCCESSFULLY.replace("$GAME$", args[1])));
		return;
	}

}
