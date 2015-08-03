package org.kwstudios.play.ragemode.gameLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;
import org.kwstudios.play.ragemode.toolbox.GameBroadcast;
import org.kwstudios.play.ragemode.toolbox.TableList;

public class GameLoader {

	private String gameName;
	private FileConfiguration fileConfiguration;
	private List<Location> gameSpawns = new ArrayList<Location>();

	public GameLoader(String gameName, FileConfiguration fileConfiguration) {
		this.gameName = gameName;
		this.fileConfiguration = fileConfiguration;
		GameSpawnGetter gameSpawnGetter = new GameSpawnGetter(gameName, fileConfiguration);
		if (gameSpawnGetter.isGameReady()) {
			gameSpawns = gameSpawnGetter.getSpawnLocations();
			teleportPlayersToGameSpawns();
		} else {
			String message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_RED
					+ "The game is not set up correctly. Please contact an Admin.";
			GameBroadcast.broadcastToGame(gameName, message);
			String[] players = PlayerList.getPlayersInGame(gameName);
			for(String player : players){
				Player thisPlayer = Bukkit.getPlayer(UUID.fromString(player));
				PlayerList.removePlayer(thisPlayer);
			}
		}
	}

	private void teleportPlayersToGameSpawns() {
		String[] players = PlayerList.getPlayersInGame(gameName);
		for (int i = 0; i < players.length; i++) {
			Player player = Bukkit.getPlayer(UUID.fromString(players[i]));
			Location location = gameSpawns.get(i);
			player.teleport(location);
		}
	}

}
