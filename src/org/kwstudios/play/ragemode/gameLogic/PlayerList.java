package org.kwstudios.play.ragemode.gameLogic;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PlayerList {
	private static String list[][];	//[Gamemames][Playernames]

	public boolean addPlayer(Player player, String game, FileConfiguration fileconfiguration) {
		int i,n;
		i = 0;
		n = 0;
		while(list[i][n] != null) {
			while(list[i][n] != null) {
				if(player.getUniqueId().toString()==list[i][n]) {
					
				}
				n++;	
			}
			i++;
		}
		return true;
	}
}



