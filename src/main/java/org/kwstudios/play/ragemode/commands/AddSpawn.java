package org.kwstudios.play.ragemode.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class AddSpawn {

	private Player player;
	private String[] args;
	private FileConfiguration fileConfiguration;

	public AddSpawn(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		this.player = player;
		this.args = args;
		this.fileConfiguration = fileConfiguration;

		addSpawnToConfig();
	}

	private void addSpawnToConfig() {
		if (args.length < 2) {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
					PluginLoader.getMessages().MISSING_ARGUMENTS.replace("$USAGE$", "/rm addspawn <GameName>")));
			return;
		}
		int i = 1;
		if (!fileConfiguration.isSet("settings.games." + args[1])) {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
					PluginLoader.getMessages().NOT_SET_YET.replace("$USAGE$", "/rm add <GameName> <MaxPlayers>")));
			return;
		}

		while (fileConfiguration.isSet("settings.games." + args[1] + ".spawns." + Integer.toString(i))) {
			i++;
		}
		ConfigFactory.setString("settings.games." + args[1] + ".spawns", Integer.toString(i), "", fileConfiguration);
		ConfigFactory.setString("settings.games." + args[1] + ".spawns." + Integer.toString(i), "world", player.getWorld().getName(), fileConfiguration);
		ConfigFactory.setDouble("settings.games." + args[1] + ".spawns." + Integer.toString(i), "x", player.getLocation().getX(), fileConfiguration);
		ConfigFactory.setDouble("settings.games." + args[1] + ".spawns." + Integer.toString(i), "y", player.getLocation().getY(), fileConfiguration);
		ConfigFactory.setDouble("settings.games." + args[1] + ".spawns." + Integer.toString(i), "z", player.getLocation().getZ(), fileConfiguration);
		ConfigFactory.setDouble("settings.games." + args[1] + ".spawns." + Integer.toString(i), "yaw", player.getLocation().getYaw(), fileConfiguration);
		ConfigFactory.setDouble("settings.games." + args[1] + ".spawns." + Integer.toString(i), "pitch", player.getLocation().getPitch(), fileConfiguration);
		player.sendMessage(ConstantHolder.RAGEMODE_PREFIX
				+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().SPAWN_SUCCESSFULLY
						.replace("$NUMBER$", Integer.toString(i)).replace("$GAME$", args[1])));
	}

}
