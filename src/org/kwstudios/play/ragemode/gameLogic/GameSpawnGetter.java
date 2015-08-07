package org.kwstudios.play.ragemode.gameLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;
import org.kwstudios.play.ragemode.toolbox.MapChecker;

public class GameSpawnGetter {

	private String gameName;
	private FileConfiguration fileConfiguration;
	private boolean isGameReady = false;
	private List<Location> spawnLocations = new ArrayList<Location>();
	private List<Location> removedLocations = new ArrayList<Location>();

	public GameSpawnGetter(String gameName, FileConfiguration fileConfiguration) {
		this.gameName = gameName;
		this.fileConfiguration = fileConfiguration;
		getSpawns();
	}

	private void getSpawns() {
		if (new MapChecker(gameName, fileConfiguration).isValid()) {
			String path = ConstantHolder.GAME_PATH + "." + gameName;
				Set<String> spawnNames = ConfigFactory.getKeysUnderPath(path + ".spawns", false, fileConfiguration);
				for (String spawnName : spawnNames) {
					String world = ConfigFactory.getString(path, "world", fileConfiguration);
					double spawnX = ConfigFactory.getDouble(path + ".spawns." + spawnName, "x", fileConfiguration);
					double spawnY = ConfigFactory.getDouble(path + ".spawns." + spawnName, "y", fileConfiguration);
					double spawnZ = ConfigFactory.getDouble(path + ".spawns." + spawnName, "z", fileConfiguration);
					double spawnYaw = ConfigFactory.getDouble(path + ".spawns." + spawnName, "yaw", fileConfiguration);
					double spawnPitch = ConfigFactory.getDouble(path + ".spawns." + spawnName, "pitch", fileConfiguration);
					Location location = new Location(Bukkit.getWorld(world), spawnX, spawnY, spawnZ);
					location.setYaw((float) spawnYaw);
					location.setPitch((float) spawnPitch);
					spawnLocations.add(location);
				}
				isGameReady = true;
		}else{
			isGameReady = false;
		}
	}

	public boolean isGameReady() {
		return isGameReady;
	}

	public List<Location> getSpawnLocations() {
		return spawnLocations;
	}

}
