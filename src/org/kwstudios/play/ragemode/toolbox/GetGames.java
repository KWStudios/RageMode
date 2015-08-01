package org.kwstudios.play.ragemode.toolbox;

import org.bukkit.configuration.file.FileConfiguration;

public class GetGames {
	
	public static int getConfigGamesCount(FileConfiguration fileConfiguration) {
		int n = 0;
		if(!fileConfiguration.isSet("settings.games"))
			return 0;
		else {
			n = ConfigFactory.getKeysUnderPath("settings.games", false, fileConfiguration).size();
			return n;
		}
	}
	
	public static int GetMaxPlayers(String game, FileConfiguration fileConfiguration) {
		if(!fileConfiguration.isSet("settings.games." + game))
			return -1;
		else {
			return ConfigFactory.getInt("settings.games." + game, "maxplayers", fileConfiguration);
		}
	}

}
