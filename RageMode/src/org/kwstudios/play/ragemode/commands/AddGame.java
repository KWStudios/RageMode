package org.kwstudios.play.ragemode.commands;

import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

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
	}

}
