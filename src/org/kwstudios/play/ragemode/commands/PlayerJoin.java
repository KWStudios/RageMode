package org.kwstudios.play.ragemode.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PlayerJoin {
	
	private static final String GAME_PATH = "settings.games";
	
	private Player player;
	private String label;
	private String[] args;
	private FileConfiguration fileConfiguration;
	private boolean isRageModeMap = false;
	
	public PlayerJoin(Player player, String label, String[] args, FileConfiguration fileConfiguration){
		this.player = player;
		this.label = label;
		this.args = args;
		this.fileConfiguration = fileConfiguration;
		doPlayerJoin();
	}
	
	private void doPlayerJoin(){
		if(!fileConfiguration.isSet(GAME_PATH + "." + args[1])){
			player.sendMessage(ChatColor.YELLOW + args[1] + ChatColor.DARK_RED + " is not a valid RageMode Map.");
			isRageModeMap = false;
		}else{
			isRageModeMap = true;
			
		}
	}

}
