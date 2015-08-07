package org.kwstudios.play.ragemode.toolbox;

import org.bukkit.configuration.file.FileConfiguration;

public class GetGames {
	
	public static int getConfigGamesCount(FileConfiguration fileConfiguration) {
		int n = 0;
		if(ConfigFactory.getKeysUnderPath("settings.games", false, fileConfiguration) == null) {
			return 0;
		}
			n = ConfigFactory.getKeysUnderPath("settings.games", false, fileConfiguration).size();
			return n;
	}
	
	public static int getMaxPlayers(String game, FileConfiguration fileConfiguration) {
		if(!fileConfiguration.isSet("settings.games." + game + ".maxplayers"))
			return -1;
		else {
			return ConfigFactory.getInt("settings.games." + game, "maxplayers", fileConfiguration);
		}
	}
	
	public static String[] getGameNames(FileConfiguration fileConfiguration) {
		String[] names = new String[getConfigGamesCount(fileConfiguration)];
		if(ConfigFactory.getKeysUnderPath("settings.games", false, fileConfiguration) != null) {
			ConfigFactory.getKeysUnderPath("settings.games", false, fileConfiguration).toArray(names);			
		}
		return names;
	}
	
	public static int getOverallMaxPlayers(FileConfiguration fileConfiguration) {
		int i = 0;
		int n = 0;
		int x;
		String[] names = getGameNames(fileConfiguration);
		while(i < names.length) {
			x = ConfigFactory.getInt("settings.games." + names[i], "maxplayers", fileConfiguration);
			if(n < x) 
				n = x;
			i++;
		}
		return n;
	}
	
	public static boolean isGameExistent(String game, FileConfiguration fileConfiguration) {
		int i = 0;
		int imax = getConfigGamesCount(fileConfiguration);
		String[] games = getGameNames(fileConfiguration);
		while(i < imax) {
			if(games[i].equals(game)) {
				return true;
			}
			i++;
		}
		return false;
			
	}

}
