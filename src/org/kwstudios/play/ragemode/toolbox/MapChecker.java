package org.kwstudios.play.ragemode.toolbox;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class MapChecker {
	
	private static final String GAME_PATH = "settings.games";
	
	private String gameName;
	private FileConfiguration fileConfiguration;
	private boolean isValid = false;
	String message;
	
	public MapChecker(String gameName, FileConfiguration fileConfiguration){
		this.gameName = gameName;
		this.fileConfiguration = fileConfiguration;
		checkMapName();
		checkLobby();
	}
	
	private void checkMapName(){
		if(!fileConfiguration.isSet(GAME_PATH + "." + gameName)){
			message = ChatColor.YELLOW + gameName + ChatColor.DARK_RED + " is not a valid RageMode Map.";
			isValid = false;
		}else{
			isValid = true;
		}
	}
	
	private void checkLobby(){
		if(!fileConfiguration.isSet(GAME_PATH + "." + gameName + "." + "lobby")){
			message = ChatColor.DARK_RED + "The lobby was not set yet for " + ChatColor.DARK_AQUA + gameName + ". Set it with /rm lobby [game name]";
		}
	}

}
