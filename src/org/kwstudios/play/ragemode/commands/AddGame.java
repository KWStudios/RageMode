package org.kwstudios.play.ragemode.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class AddGame {

	private Player player;
	private String label;
	private String[] args;
	private FileConfiguration fileConfiguration;

	public AddGame(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		this.player = player;
		this.label = label;
		this.args = args;
		this.fileConfiguration = fileConfiguration;

		addGametoConfig();
	}

	private void addGametoConfig() {
		if(args.length < 3) {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_RED + "Missing arguments! Usage: /rm add <GameName> <MaxPlayers>");
			return;
		}
		int x;
		try {
			x = Integer.parseInt(args[2]);
		} catch (Exception e) {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_RED + args[2] + " is not a number.");
			return;
		}

		if (fileConfiguration.isSet("settings.games." + args[1])) {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_RED + args[1] + " already exists.");
			return;
		}
		
		PlayerList.addGameToList(args[1], Integer.parseInt(args[2]));
		
		ConfigFactory.setString("settings.games", args[1], "", fileConfiguration);
		ConfigFactory.setInt("settings.games." + args[1], "maxplayers", Integer.parseInt(args[2]), fileConfiguration);
		ConfigFactory.setString("settings.games." + args[1], "world", player.getWorld().getName(), fileConfiguration);
		
		
		player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_GREEN + "The game " + ChatColor.DARK_AQUA + args[1]
				+ ChatColor.DARK_GREEN + " was added successfully!");
		return;
	}

}
