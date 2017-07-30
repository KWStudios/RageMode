package org.kwstudios.play.ragemode.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class SetGlobalMessages {

	private Player player;
	private String[] args;
	private FileConfiguration fileConfiguration;

	public SetGlobalMessages(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		this.player = player;
		this.args = args;
		this.fileConfiguration = fileConfiguration;

		parseArgs();
	}

	private void parseArgs() {
		switch (args[0].toLowerCase()) {
		case "global":
			setBroadcast(true);
			break;
		case "globalmessages":
			setBroadcast(false);
			break;
		default:
			break;
		}
	}

	private void setBroadcast(boolean global) {
		if (global) {
			if (args.length >= 3) {
				ConfigFactory.setBoolean("settings.global", "deathmessages", Boolean.parseBoolean(args[2]),
						fileConfiguration);
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX
						+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().SUCCESS));
			} else {
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX
						+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().MISSING_ARGUMENTS
								.replace("$USAGE$", "/rm global globalmessages <true|false>")));
			}
		} else {
			if (args.length >= 3) {
				if (!fileConfiguration.isSet(ConstantHolder.GAME_PATH + "." + args[1])) {
					player.sendMessage(ConstantHolder.RAGEMODE_PREFIX
							+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().NOT_SET_YET
									.replace("$USAGE$", "/rm add <GameName> <MaxPlayers>")));
					return;
				}
				ConfigFactory.setBoolean(ConstantHolder.GAME_PATH + "." + args[1], "deathmessages",
						Boolean.parseBoolean(args[2]), fileConfiguration);
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX
						+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().SUCCESS));
			} else {
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX
						+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().MISSING_ARGUMENTS
								.replace("$USAGE$", "/rm globalmessages <GameName> <true|false>")));
			}
		}
	}

}
