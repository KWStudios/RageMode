package org.kwstudios.play.ragemode.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class SetActionBar {
	private Player player;
	private String[] args;
	private FileConfiguration fileConfiguration;

	public SetActionBar(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		this.player = player;
		this.args = args;
		this.fileConfiguration = fileConfiguration;

		setActionBar();
	}

	private void setActionBar() {
		if (args.length >= 2) {
			ConfigFactory.setBoolean("settings.global", "actionbar", Boolean.parseBoolean(args[1]), fileConfiguration);
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX
					+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().SUCCESS));
		} else {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
					PluginLoader.getMessages().MISSING_ARGUMENTS.replace("$USAGE$", "/rm actionbar <true|false>")));
		}
	}
}
