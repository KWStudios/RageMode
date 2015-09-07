package org.kwstudios.play.ragemode.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class ReloadConfig {

	public ReloadConfig(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		PluginLoader pluginLoader = PluginLoader.getInstance();
		pluginLoader.reloadConfig();
		pluginLoader.initStatistics();
		player.sendMessage(ConstantHolder.RAGEMODE_PREFIX
				+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().RELOADED_SUCCESSFULLY));
		// TODO To be continued...
	}

}
