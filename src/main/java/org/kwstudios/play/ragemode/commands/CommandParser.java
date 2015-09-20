package org.kwstudios.play.ragemode.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class CommandParser {

	private Player player;
	@SuppressWarnings("unused")
	private Command command;
	private String label;
	private String[] args;
	private FileConfiguration fileConfiguration;

	private boolean isCommand = false;

	public CommandParser(Player player, Command command, String label, String[] args,
			FileConfiguration fileConfiguration) {
		this.player = player;
		this.command = command;
		this.label = label;
		this.args = args;
		this.fileConfiguration = fileConfiguration;
		checkCommand();
	}

	private void checkCommand() {
		if (args == null || args.length < 1) {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX
					+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().WRONG_COMMAND));
			return;
		}
		switch (label.toLowerCase()) {
		case "rm":
			parseFirstArg();
			isCommand = true;
			break;
		case "ragemode":
			parseFirstArg();
			isCommand = true;
			break;

		default:
			isCommand = false;
			break;
		}
	}

	private void parseFirstArg() {
		switch (args[0].toLowerCase()) {
		case "add":
			if (player.hasPermission("ragemode.admin.add")) {
				new AddGame(player, label, args, fileConfiguration);
			} else {
				player.sendMessage(
						ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
			}
			break;
		case "lobby":
			if (player.hasPermission("ragemode.admin.lobby")) {
				new AddLobby(player, label, args, fileConfiguration);
			} else {
				player.sendMessage(
						ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
			}
			break;
		case "addspawn":
			if (player.hasPermission("ragemode.admin.addspawn")) {
				new AddSpawn(player, label, args, fileConfiguration);
			} else {
				player.sendMessage(
						ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
			}
			break;
		case "join":
			if (player.hasPermission("ragemode.rm.join")) {
				new PlayerJoin(player, label, args, fileConfiguration);
			} else {
				player.sendMessage(
						ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
			}
			break;
		case "leave":
			if (player.hasPermission("ragemode.rm.leave")) {
				new PlayerLeave(player, label, args, fileConfiguration);
			} else {
				player.sendMessage(
						ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
			}
			break;
		case "remove":
			if (player.hasPermission("ragemode.admin.remove")) {
				new RemoveGame(player, label, args, fileConfiguration);
			} else {
				player.sendMessage(
						ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
			}
			break;
		case "list":
			if (player.hasPermission("ragemode.rm.list")) {
				new ListGames(player, label, args, fileConfiguration);
			} else {
				player.sendMessage(
						ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
			}
			break;
		case "stop":
			if (player.hasPermission("ragemode.admin.stop")) {
				new StopGame(player, label, args, fileConfiguration);
			} else {
				player.sendMessage(
						ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
			}
			break;
		case "reload":
			if (player.hasPermission("ragemode.admin.reload")) {
				new ReloadConfig(player, label, args, fileConfiguration);
			} else {
				player.sendMessage(
						ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
			}
			break;
		case "stats":
			if (player.hasPermission("ragemode.rm.stats")) {
				new ShowStats(player, label, args, fileConfiguration);
			} else {
				player.sendMessage(
						ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
			}
			break;
		case "lobbydelay":
			if (player.hasPermission("ragemode.admin.lobbydelay")) {
				new SetLobbyDelay(player, label, args, fileConfiguration);
			} else {
				player.sendMessage(
						ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
			}
			break;
		case "gametime":
			if (player.hasPermission("ragemode.admin.gametime")) {
				new SetGameTime(player, label, args, fileConfiguration);
			} else {
				player.sendMessage(
						ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
			}
			break;
		case "globalmessages":
			if (player.hasPermission("ragemode.admin.globalmessages")) {
				new SetGlobalMessages(player, label, args, fileConfiguration);
			} else {
				player.sendMessage(
						ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
			}
			break;
		case "global":
			if (player.hasPermission("ragemode.admin.global")) {
				parseSecondArg();
			} else {
				player.sendMessage(
						ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
			}
			break;
		case "bossbar":
			if (player.hasPermission("ragemode.admin.bossbar")) {
				new SetBossBar(player, label, args, fileConfiguration);
			} else {
				player.sendMessage(
						ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
			}
			break;
		case "actionbar":
			if (player.hasPermission("ragemode.admin.actionbar")) {
				new SetActionBar(player, label, args, fileConfiguration);
			} else {
				player.sendMessage(
						ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
			}
			break;
		case "holo":
			if (player.hasPermission("ragemode.admin.holo")) {
				new HoloStats(player, label, args, fileConfiguration);
			} else {
				player.sendMessage(
						ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
			}
			break;
		case "locale":
			if (player.hasPermission("ragemode.admin.locale")) {
				new SetLocale(player, label, args, fileConfiguration);
			} else {
				player.sendMessage(
						ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
			}
			break;
		default:
			player.sendMessage(
					ChatColor.DARK_RED + "This is not a valid RageMode command! Type /help ragemode for more help.");
			break;
		}
	}

	private void parseSecondArg() {
		if (args.length >= 2) {
			switch (args[1].toLowerCase()) {
			case "lobbydelay":
				if (player.hasPermission("ragemode.admin.lobbydelay")) {
					new SetLobbyDelay(player, label, args, fileConfiguration);
				} else {
					player.sendMessage(
							ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
				}
				break;
			case "gametime":
				if (player.hasPermission("ragemode.admin.gametime")) {
					new SetGameTime(player, label, args, fileConfiguration);
				} else {
					player.sendMessage(
							ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
				}
				break;
			case "globalmessages":
				if (player.hasPermission("ragemode.admin.globalmessages")) {
					new SetGlobalMessages(player, label, args, fileConfiguration);
				} else {
					player.sendMessage(
							ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
				}
				break;
			default:
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
						PluginLoader.getMessages().MISSING_ARGUMENTS.replace("$USAGE$",
								"/rm global <lobbydelay|gametime|globalmessages> <Seconds|Minutes|true,false>")));
				break;
			}
		} else {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
					PluginLoader.getMessages().MISSING_ARGUMENTS.replace("$USAGE$",
							"/rm global <lobbydelay|gametime|globalmessages> <Seconds|Minutes|true,false>")));
		}
	}

	public boolean isCommand() {
		return isCommand;
	}

}
