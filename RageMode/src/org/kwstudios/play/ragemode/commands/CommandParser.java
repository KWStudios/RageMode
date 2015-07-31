package org.kwstudios.play.ragemode.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandParser {
	
	private Player player;
	private Command command;
	private String label;
	private String[] args;
	private FileConfiguration fileConfiguration;
	
	private boolean isCommand = false;
	
	public CommandParser(Player player, Command command, String label, String[] args, FileConfiguration fileConfiguration){
		this.player = player;
		this.command = command;
		this.label = label;
		this.args = args;
		this.fileConfiguration = fileConfiguration;
		checkCommand();
	}
	
	private void checkCommand(){
		switch (label.toLowerCase()) {
		case "rm":
			parseFirstArg();
			break;

		default:
			isCommand = false;
			break;
		}
	}
	
	private void parseFirstArg(){
		switch (args[0].toLowerCase()){
		case "add":
			AddGame addGame = new AddGame(player, label, args, fileConfiguration);
			break;
			
		default:
			player.sendMessage(ChatColor.DARK_RED + "This is not a valid RageMode command! Type /help ragemode for more help.");
			break;
		}
	}

	public boolean isCommand() {
		return isCommand;
	}

}
