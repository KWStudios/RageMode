package org.kwstudios.play.ragemode.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class SetGameTime {
	
	private Player player;
	private String[] args;
	private FileConfiguration fileConfiguration;

	public SetGameTime(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		this.player = player;
		this.args = args;
		this.fileConfiguration = fileConfiguration;

		parseArgs();
	}

	private void parseArgs() {
		switch (args[0].toLowerCase()) {
		case "global":
			setGameTime(true);
			break;
		case "gametime":
			setGameTime(false);
			break;
		default:
			break;
		}
	}

	private void setGameTime(boolean global) {
		if (global) {
			if (args.length >= 3) {
				if (isInt(args[2])) {
					ConfigFactory.setInt("settings.global", "gametime", Integer.parseInt(args[2]), fileConfiguration);
					player.sendMessage(ConstantHolder.RAGEMODE_PREFIX
							+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().SUCCESS));
				} else {
					player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
							PluginLoader.getMessages().NOT_A_NUMBER.replace("$WRONG_NUMBER$", args[2])));
				}
			} else {
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX
						+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().MISSING_ARGUMENTS
								.replace("$USAGE$", "/rm global gametime <Minutes>")));
			}
		} else {
			if (args.length >= 3) {
				if (!fileConfiguration.isSet(ConstantHolder.GAME_PATH + "." + args[1])) {
					player.sendMessage(ConstantHolder.RAGEMODE_PREFIX
							+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().NOT_SET_YET
									.replace("$USAGE$", "/rm add <GameName> <MaxPlayers>")));
					return;
				}
				if (isInt(args[2])) {
					ConfigFactory.setInt(ConstantHolder.GAME_PATH + "." + args[1], "gametime",
							Integer.parseInt(args[2]), fileConfiguration);
					player.sendMessage(ConstantHolder.RAGEMODE_PREFIX
							+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().SUCCESS));
				} else {
					player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
							PluginLoader.getMessages().NOT_A_NUMBER.replace("$WRONG_NUMBER$", args[2])));
				}
			} else {
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX
						+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().MISSING_ARGUMENTS
								.replace("$USAGE$", "/rm gametime <GameName> <Minutes>")));
			}
		}
	}

	private boolean isInt(String string) {
		try {
			Integer.parseInt(string);
		} catch (Exception e) {
			return false;
		}
		return true;
	}


}
