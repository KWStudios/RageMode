package org.kwstudios.play.ragemode.toolbox;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;

public class GameBroadcast {
	
	public static void broadcastToGame(String game, String message) {
			String[] playersInGame = PlayerList.getPlayersInGame(game);
			int i = 0;
			int imax = playersInGame.length;
			
			while(i < imax) {
				Bukkit.getPlayer(UUID.fromString(playersInGame[i])).sendMessage(message);
				i++;
		}
	}

}
