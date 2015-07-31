package org.kwstudios.play.ragemode.toolbox;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class MapChecker {

	private static final String GAME_PATH = "settings.games";

	private String gameName;
	private FileConfiguration fileConfiguration;
	private boolean isValid = false;
	String message;
	int maxPlayers;

	public MapChecker(String gameName, FileConfiguration fileConfiguration) {
		this.gameName = gameName;
		this.fileConfiguration = fileConfiguration;
		checkMapName();
		if (isValid) {
			checkBasics();
		}
		if (isValid) {
			checkLobby();
		}
		if (isValid) {
			checkSpawns();
		}
	}

	private void checkMapName() {
		if (!fileConfiguration.isSet(GAME_PATH + "." + gameName)) {
			message = ChatColor.YELLOW + gameName + ChatColor.DARK_RED + " is not a valid RageMode Map.";
			isValid = false;
		} else {
			isValid = true;
		}
	}

	private void checkBasics() {
		String path = GAME_PATH + "." + gameName;
		if (!fileConfiguration.isSet(path + ".maxplayers") || !fileConfiguration.isSet(path + ".world")) {
			message = ChatColor.DARK_RED
					+ "The worldname or the maxplayers are not set. Please contact an Admin for further information.";
			isValid = false;
		} else {
			if (ConfigFactory.getString(path, "world", fileConfiguration) != "") {
				maxPlayers = ConfigFactory.getInt(path, "maxplayers", fileConfiguration);
				if (maxPlayers != -32500000) {
					isValid = true;
				} else {
					message = ChatColor.DARK_RED + "The maxplayers value for " + gameName + " is not set properly.";
					isValid = false;
				}
			} else {
				message = ChatColor.DARK_RED + "The world name can't be empty!";
				isValid = false;
			}
		}
	}

	private void checkLobby() {
		if (!fileConfiguration.isSet(GAME_PATH + "." + gameName + "." + "lobby")) {
			message = ChatColor.DARK_RED + "The lobby was not set yet for " + ChatColor.DARK_AQUA + gameName
					+ ". Set it with /rm lobby [game name]";
			isValid = false;
		} else {
			String thisPath = GAME_PATH + "." + gameName + "." + "lobby";
			if (fileConfiguration.isSet(thisPath + ".x") && fileConfiguration.isSet(thisPath + ".y")
					&& fileConfiguration.isSet(thisPath + ".z") && fileConfiguration.isSet(thisPath + ".world")) {
				if (ConfigFactory.getString(thisPath, "world", fileConfiguration) != "") {
					if (ConfigFactory.getInt(thisPath, "x", fileConfiguration) != -32500000
							&& ConfigFactory.getInt(thisPath, "y", fileConfiguration) != -32500000
							&& ConfigFactory.getInt(thisPath, "z", fileConfiguration) != -32500000) {
						isValid = true;
					} else {
						message = ChatColor.DARK_RED
								+ "The lobby coordinates were not set properly. Ask an Admin to check the config.yml";
						isValid = false;
						return;
					}
				}
			}
		}
	}

	private void checkSpawns() {
		String path = GAME_PATH + "." + gameName;
		if (fileConfiguration.isSet(path + ".spawns")) {
			Set<String> spawnNames = ConfigFactory.getKeysUnderPath(path + ".spawns", false, fileConfiguration);
			if (spawnNames.size() >= maxPlayers) {
				for (String s : spawnNames) {
					if (ConfigFactory.getInt(path + ".spawns." + s, "x", fileConfiguration) != -32500000
							&& ConfigFactory.getInt(path + ".spawns." + s, "y", fileConfiguration) != -32500000
							&& ConfigFactory.getInt(path + ".spawns." + s, "z", fileConfiguration) != -32500000) {
						isValid = true;
					} else {
						message = ChatColor.DARK_RED + "One or more spawns are not set properly!";
						isValid = false;
						break;
					}
				}
			} else {
				message = ChatColor.DARK_RED
						+ "The number of spawns must be greater than or equal the maxplayers value!";
				isValid = false;
			}
		} else {
			message = ChatColor.DARK_RED + "In " + gameName + " are no spawns configured!";
			isValid = false;
		}
	}

	public boolean isValid() {
		return isValid;
	}

	public String getMessage() {
		return message;
	}

}
