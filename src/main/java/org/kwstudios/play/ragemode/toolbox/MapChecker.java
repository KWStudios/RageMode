package org.kwstudios.play.ragemode.toolbox;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.kwstudios.play.ragemode.loader.PluginLoader;

public class MapChecker {

	private String gameName;
	private FileConfiguration fileConfiguration;
	private boolean isValid = false;
	private String message = "";
	private int maxPlayers;

	public MapChecker(String gameName, FileConfiguration fileConfiguration) {
		if (gameName == null) {
			throw new NullPointerException("The variable (String) gameName cannot be null!");
		}
		if (fileConfiguration == null) {
			throw new NullPointerException("The variable (FileConfiguration) fileConfiguration cannot be null!");
		}
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
			message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
					PluginLoader.getMessages().INVALID_GAME.replace("$GAME$", gameName));
			isValid = false;
		} else {
			isValid = true;
		}
	}

	private void checkBasics() {
		String path = ConstantHolder.GAME_PATH + "." + gameName;
		if (!fileConfiguration.isSet(path + ".maxplayers") || !fileConfiguration.isSet(path + ".world")) {
			message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
					PluginLoader.getMessages().NAME_OR_MAXPLAYERS_NOT_SET);
			isValid = false;
		} else {
			if (ConfigFactory.getString(path, "world", fileConfiguration) != "") {
				maxPlayers = ConfigFactory.getInt(path, "maxplayers", fileConfiguration);
				if (maxPlayers != -32500000) {
					isValid = true;
				} else {
					message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
							PluginLoader.getMessages().MAXPLAYERS_NOT_SET.replace("$GAME$", gameName));
					isValid = false;
				}
			} else {
				message = ConstantHolder.RAGEMODE_PREFIX
						+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().WORLDNAME_NOT_SET);
				isValid = false;
			}
		}
	}

	private void checkLobby() {
		if (!fileConfiguration.isSet(ConstantHolder.GAME_PATH + "." + gameName + "." + "lobby")) {
			message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
					PluginLoader.getMessages().LOBBY_NOT_SET.replace("$GAME$", gameName));
			isValid = false;
		} else {
			String thisPath = ConstantHolder.GAME_PATH + "." + gameName + "." + "lobby";
			if (fileConfiguration.isSet(thisPath + ".x") && fileConfiguration.isSet(thisPath + ".y")
					&& fileConfiguration.isSet(thisPath + ".z") && fileConfiguration.isSet(thisPath + ".world")
					&& fileConfiguration.isSet(thisPath + ".yaw") && fileConfiguration.isSet(thisPath + ".pitch")) {
				if (!ConfigFactory.getString(thisPath, "world", fileConfiguration).isEmpty()) {
					if (isDouble(ConfigFactory.getString(thisPath, "x", fileConfiguration))
							&& isDouble(ConfigFactory.getString(thisPath, "y", fileConfiguration))
							&& isDouble(ConfigFactory.getString(thisPath, "z", fileConfiguration))
							&& isDouble(ConfigFactory.getString(thisPath, "yaw", fileConfiguration))
							&& isDouble(ConfigFactory.getString(thisPath, "pitch", fileConfiguration))) {
						isValid = true;
					} else {
						message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
								PluginLoader.getMessages().LOBBY_COORDINATES_NOT_SET);
						isValid = false;
						return;
					}
				} else {
					message = ConstantHolder.RAGEMODE_PREFIX
							+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().WORLDNAME_NOT_SET);
					isValid = false;
				}
			} else {
				message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
						PluginLoader.getMessages().LOBBY_NOT_SET_PROPERLY);
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
					World world = null;
					try {
						world = Bukkit
								.getWorld(ConfigFactory.getString(path + ".spawns." + s, "world", fileConfiguration));
					} catch (Exception e) {
						world = null;
					}
					if (world != null
							&& isDouble(ConfigFactory.getString(path + ".spawns." + s, "x", fileConfiguration))
							&& isDouble(ConfigFactory.getString(path + ".spawns." + s, "y", fileConfiguration))
							&& isDouble(ConfigFactory.getString(path + ".spawns." + s, "z", fileConfiguration))
							&& isDouble(ConfigFactory.getString(path + ".spawns." + s, "yaw", fileConfiguration))
							&& isDouble(ConfigFactory.getString(path + ".spawns." + s, "pitch", fileConfiguration))) {
						isValid = true;
					} else {
						message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
								PluginLoader.getMessages().SPAWNS_NOT_SET_PROPERLY);
						isValid = false;
						break;
					}
				}
			} else {
				message = ConstantHolder.RAGEMODE_PREFIX
						+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().TOO_FEW_SPAWNS);
				isValid = false;
			}
		} else {
			message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
					PluginLoader.getMessages().NO_SPAWNS_CONFIGURED.replace("$GAME$", gameName));
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

	public static boolean isGameWorld(String gameName, World world) {
		if (gameName == null) {
			throw new NullPointerException("The variable (String) gameName cannot be null!");
		}
		if (world == null) {
			throw new NullPointerException("The variable (World) world cannot be null!");
		}
		String spawnsPath = ConstantHolder.GAME_PATH + "." + gameName + "." + "spawns";
		Set<String> allSpawns = ConfigFactory.getKeysUnderPath(spawnsPath, false,
				PluginLoader.getInstance().getConfig());
		for (String spawn : allSpawns) {
			String worldName = ConfigFactory.getString(spawnsPath + "." + spawn, "world",
					PluginLoader.getInstance().getConfig());
			if (worldName.trim().equals(world.getName().trim())) {
				return true;
			}
		}

		String lobbyPath = ConstantHolder.GAME_PATH + "." + gameName + "." + "lobby";
		String worldName = ConfigFactory.getString(lobbyPath, "world", PluginLoader.getInstance().getConfig());
		if (worldName.trim().equals(world.getName().trim())) {
			return true;
		}

		return false;
	}

}
