package org.kwstudios.play.ragemode.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class AddLobby {

	private static final String GAMES_PATH = "settings.games";

	private Player player;
	@SuppressWarnings("unused")
	private String label;
	private String[] args;
	private FileConfiguration fileConfiguration;

	public AddLobby(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		this.player = player;
		this.label = label;
		this.args = args;
		this.fileConfiguration = fileConfiguration;
		addLobbyToConfig();
	}

	private void addLobbyToConfig() {
		if (args.length >= 2) {
			String gameName = args[1];
			if (!fileConfiguration.isSet(GAMES_PATH + "." + gameName)) {
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + "This game was not set yet! Set it with"
						+ ChatColor.DARK_RED + "/rm add [name]");
				return;
			} else {
				String path = GAMES_PATH + "." + gameName + "." + "lobby";
				ConfigFactory.setString(path, "world", player.getWorld().getName(), fileConfiguration);
				ConfigFactory.setInt(path, "x", player.getLocation().getBlockX(), fileConfiguration);
				ConfigFactory.setInt(path, "y", player.getLocation().getBlockY(), fileConfiguration);
				ConfigFactory.setInt(path, "z", player.getLocation().getBlockZ(), fileConfiguration);
				ConfigFactory.setDouble(path, "yaw", player.getLocation().getYaw(), fileConfiguration);
				ConfigFactory.setDouble(path, "pitch", player.getLocation().getPitch(), fileConfiguration);
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_GREEN + "The lobby for the game "
						+ ChatColor.DARK_AQUA + gameName + ChatColor.DARK_GREEN + " was set successfully!");
			}
		} else {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_RED
					+ "Missing arguments! Usage: /rm lobby <GameName>");
		}
	}

}
