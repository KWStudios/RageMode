package org.kwstudios.play.ragemode.commands;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class SetLocale {

	private Player player;
	private String[] args;
	private FileConfiguration fileConfiguration;
	private Set<String> fileNames = new HashSet<String>();

	public SetLocale(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		this.player = player;
		this.args = args;
		this.fileConfiguration = fileConfiguration;

		loadFiles();
		setLocale();
	}

	private void loadFiles() {
		for (File file : PluginLoader
				.listFilesForFolder(new File(PluginLoader.getInstance().getDataFolder(), "locale"))) {
			if (!fileNames.contains(file.getName())) {
				fileNames.add(file.getName());
			}
		}
	}

	private void setLocale() {
		if (args.length >= 2) {
			if (fileNames.contains(args[1] + ".json")) {
				ConfigFactory.setString("settings.global", "locale", args[1], fileConfiguration);
				PluginLoader.getInstance().loadMessages();
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX
						+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().SUCCESS));
			} else {
				String message = ConstantHolder.RAGEMODE_PREFIX
						+ "This localization file is not available! There are currently " + ChatColor.DARK_PURPLE;
				for (String fileName : fileNames) {
					message = message + fileName.replace(".json", ", ");
				}
				message = message + ChatColor.RESET + "available";
				player.sendMessage(message);
			}
		} else {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
					PluginLoader.getMessages().MISSING_ARGUMENTS.replace("$USAGE$", "/rm locale <Language-code>")));
		}
	}

}
