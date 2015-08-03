package org.kwstudios.play.ragemode.gameLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class GameLoader {
	
	private String gameName;
	private FileConfiguration fileConfiguration;
	private List<Location> gameSpawns = new ArrayList<Location>();
	
	public GameLoader(String gameName, FileConfiguration fileConfiguration){
		this.gameName = gameName;
		this.fileConfiguration = fileConfiguration;
		//getGameSpawns();
		teleportPlayersToGameSpawns();
	}
	
	private void teleportPlayersToGameSpawns(){
		for(String playerUUID : PlayerList.getPlayersInGame(gameName)){
			Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
		}
	}

}
