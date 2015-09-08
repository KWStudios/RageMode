package org.kwstudios.play.ragemode.toolbox;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class MapChecker {

	private String gameName;
	private FileConfiguration fileConfiguration;
	private boolean isValid = false;
	private String message = "";
	private int maxPlayers;

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
		if (!fileConfiguration.isSet(ConstantHolder.GAME_PATH + "." + gameName)) {
			message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.YELLOW + gameName + ChatColor.DARK_RED
					+ " is not a valid RageMode Map.";
			isValid = false;
		} else {
			isValid = true;
		}
	}

	private void checkBasics() {
		String path = ConstantHolder.GAME_PATH + "." + gameName;
		if (!fileConfiguration.isSet(path + ".maxplayers") || !fileConfiguration.isSet(path + ".world")) {
			message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_RED
					+ "The worldname or the maxplayers are not set. Please contact an Admin for further information.";
			isValid = false;
		} else {
			if (ConfigFactory.getString(path, "world", fileConfiguration) != "") {
				maxPlayers = ConfigFactory.getInt(path, "maxplayers", fileConfiguration);
				if (maxPlayers != -32500000) {
					isValid = true;
				} else {
					message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_RED + "The maxplayers value for "
							+ gameName + " is not set properly.";
					isValid = false;
				}
			} else {
				message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_RED + "The world name can't be empty!";
				isValid = false;
			}
		}
	}

	private void checkLobby() {
		if (!fileConfiguration.isSet(ConstantHolder.GAME_PATH + "." + gameName + "." + "lobby")) {
			message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_RED + "The lobby was not set yet for "
					+ ChatColor.DARK_AQUA + gameName + ChatColor.DARK_RED + ". Set it with /rm lobby [game name]";
			isValid = false;
		} else {
			String thisPath = ConstantHolder.GAME_PATH + "." + gameName + "." + "lobby";
			if (fileConfiguration.isSet(thisPath + ".x") && fileConfiguration.isSet(thisPath + ".y")
					&& fileConfiguration.isSet(thisPath + ".z") && fileConfiguration.isSet(thisPath + ".world") && fileConfiguration.isSet(thisPath + ".yaw") && fileConfiguration.isSet(thisPath + ".pitch")) {
				if (!ConfigFactory.getString(thisPath, "world", fileConfiguration).isEmpty()) {
					if (isDouble(ConfigFactory.getString(thisPath, "x", fileConfiguration))
							&& isDouble(ConfigFactory.getString(thisPath, "y", fileConfiguration))
							&& isDouble(ConfigFactory.getString(thisPath, "z", fileConfiguration)) && isDouble(ConfigFactory.getString(thisPath, "yaw", fileConfiguration)) && isDouble(ConfigFactory.getString(thisPath, "pitch", fileConfiguration))) {
						isValid = true;
					} else {
						message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_RED
								+ "The lobby coordinates were not set properly. Ask an Admin to check the config.yml";
						isValid = false;
						return;
					}
				} else {
					message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_RED
							+ "The world key can't be empty. Ask an Admin to check the config.yml";
					isValid = false;
				}
			} else {
				message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_RED
						+ "The lobby was not set properly. Ask an Admin to check the config.yml";
				isValid = false;
			}
		}
	}

	private void checkSpawns() {
		String path = ConstantHolder.GAME_PATH + "." + gameName;
		if (fileConfiguration.isSet(path + ".spawns")) {
			Set<String> spawnNames = ConfigFactory.getKeysUnderPath(path + ".spawns", false, fileConfiguration);
			if (spawnNames.size() >= maxPlayers) {
				for (String s : spawnNames) {
					if (isDouble(ConfigFactory.getString(path + ".spawns." + s, "x", fileConfiguration))
							&& isDouble(ConfigFactory.getString(path + ".spawns." + s, "y", fileConfiguration))
							&& isDouble(ConfigFactory.getString(path + ".spawns." + s, "z", fileConfiguration)) && isDouble(ConfigFactory.getString(path + ".spawns." + s, "yaw", fileConfiguration)) && isDouble(ConfigFactory.getString(path + ".spawns." + s, "pitch", fileConfiguration))) {
						isValid = true;
					} else {
						message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_RED
								+ "One or more spawns are not set properly!";
						isValid = false;
						break;
					}
				}
			} else {
				message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_RED
						+ "The number of spawns must be greater than or equal the maxplayers value!";
				isValid = false;
			}
		} else {
			message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.DARK_RED + "In " + gameName
					+ " are no spawns configured!";
			isValid = false;
		}
	}

	private boolean isDouble(String string) {
		try {
			Double.parseDouble(string);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean isValid() {
		return isValid;
	}

	public String getMessage() {
		return message;
	}

}
