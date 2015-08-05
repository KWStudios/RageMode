package org.kwstudios.play.ragemode.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandParser {
	
	private Player player;
	@SuppressWarnings("unused")
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
	
	private void parseFirstArg(){
		switch (args[0].toLowerCase()){
		case "add":
			new AddGame(player, label, args, fileConfiguration);
			break;
		case "lobby":
			new AddLobby(player, label, args, fileConfiguration);
			break;
		case "addspawn":
			new AddSpawn(player, label, args, fileConfiguration);
			break;
		case "join":
			new PlayerJoin(player, label, args, fileConfiguration);
			break;
		case "leave":
			new PlayerLeave(player, label, args, fileConfiguration);
			break;
		case "remove":
			new RemoveGame(player, label, args, fileConfiguration);
			break;
		case "list":
			new ListGames(player, label, args, fileConfiguration);
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
