package org.kwstudios.play.ragemode.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;

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
			player.sendMessage(ChatColor.DARK_RED + "Missing arguments!");
			return;
		}
		int x;
		try {
			x = Integer.parseInt(args[2]);
		} catch (Exception e) {
			player.sendMessage(ChatColor.DARK_RED + args[2] + " is not a number.");
			return;
		}

		if (fileConfiguration.isSet("settings.games." + args[1])) {
			player.sendMessage(ChatColor.DARK_RED + args[1] + " already exists.");
			return;
		}

		ConfigFactory.setString("settings.games", args[1], "", fileConfiguration);
		ConfigFactory.setInt("settings.games." + args[1], "maxplayers", Integer.parseInt(args[2]), fileConfiguration);
		ConfigFactory.setString("settings.games." + args[1], "world", player.getWorld().getName(), fileConfiguration);
		
		player.sendMessage(ChatColor.DARK_GREEN + "The game " + ChatColor.DARK_AQUA + args[1]
				+ ChatColor.DARK_GREEN + " was added successfully!");
		return;
	}

}
