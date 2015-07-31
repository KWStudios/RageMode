package org.kwstudios.play.ragemode.commands;

import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;

public class AddSpawn {
	
	private Player player;
	private String label;
	private String[] args;
	private FileConfiguration fileConfiguration;
	
	public AddSpawn(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		this.player = player;
		this.label = label;
		this.args = args;
		this.fileConfiguration = fileConfiguration;
		
		addSpawnToConfig();
	}
	
	private void addSpawnToConfig() {
		int i = 1;
		if(!fileConfiguration.isSet("settings.games." + args[1])) {
			player.sendMessage(ChatColor.DARK_RED + args[1] + " doesn't exist.");
			return;
		}
				
		while(fileConfiguration.isSet("settings.games." + args[1] + ".spawns." + Integer.toString(i))) {
			i++;
		}
		ConfigFactory.setString("settings.games." + args[1] + ".spawns", Integer.toString(i), "", fileConfiguration);
		ConfigFactory.setInt("settings.games." + args[1] + ".spawns." + Integer.toString(i), "x", player.getLocation().getBlockX(), fileConfiguration);
		ConfigFactory.setInt("settings.games." + args[1] + ".spawns." + Integer.toString(i), "y", player.getLocation().getBlockY(), fileConfiguration);
		ConfigFactory.setInt("settings.games." + args[1] + ".spawns." + Integer.toString(i), "z", player.getLocation().getBlockZ(), fileConfiguration);
		ConfigFactory.setDouble("settings.games." + args[1] + ".spawns." + Integer.toString(i), "yaw", player.getLocation().getYaw(), fileConfiguration);
		ConfigFactory.setDouble("settings.games." + args[1] + ".spawns." + Integer.toString(i), "pitch", player.getLocation().getPitch(), fileConfiguration);
		player.sendMessage(ChatColor.DARK_GREEN + "Spawn " + Integer.toString(i) + "for the game " + ChatColor.DARK_AQUA + args[1]
				+ ChatColor.DARK_GREEN + " was set successfully!");
		
	}

}
