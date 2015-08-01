package org.kwstudios.play.ragemode.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.MapChecker;

public class PlayerJoin {
	
	private static final String GAME_PATH = "settings.games";
	
	private Player player;
	private String label;
	private String[] args;
	private FileConfiguration fileConfiguration;
	
	public PlayerJoin(Player player, String label, String[] args, FileConfiguration fileConfiguration){
		this.player = player;
		this.label = label;
		this.args = args;
		this.fileConfiguration = fileConfiguration;
		doPlayerJoin();
	}
	
	private void doPlayerJoin(){
		MapChecker mapChecker = new MapChecker(args[1], fileConfiguration);
		if(mapChecker.isValid()){
			String world = ConfigFactory.getString(GAME_PATH + "." + args[1] + ".lobby", "world", fileConfiguration);			
			int lobbyX = ConfigFactory.getInt(GAME_PATH + "." + args[1] + ".lobby", "x", fileConfiguration);
			int lobbyY = ConfigFactory.getInt(GAME_PATH + "." + args[1] + ".lobby", "y", fileConfiguration);
			int lobbyZ = ConfigFactory.getInt(GAME_PATH + "." + args[1] + ".lobby", "y", fileConfiguration);
			
			player.sendMessage(ChatColor.DARK_GREEN + "The game was set up successfully!");
			
		}else{
			player.sendMessage(mapChecker.getMessage());
		}
	}

}
