package org.kwstudios.play.ragemode.gameLogic;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;
import org.kwstudios.play.ragemode.toolbox.MapChecker;

public class GameSpawnGetter {

	private String gameName;
	private FileConfiguration fileConfiguration;
	private boolean areSpawnsReady = false;

	public GameSpawnGetter(String gameName, FileConfiguration fileConfiguration) {
		this.gameName = gameName;
		this.fileConfiguration = fileConfiguration;
		getSpawns();
	}

	private void getSpawns() {
		if (new MapChecker(gameName, fileConfiguration).isValid()) {
			String path = ConstantHolder.GAME_PATH + "." + gameName;
			if (fileConfiguration.isSet(path + ".spawns")) {
				Set<String> spawnNames = ConfigFactory.getKeysUnderPath(path + ".spawns", false, fileConfiguration);
				for (String spawnName : spawnNames) {
					int spawnX = ConfigFactory.getInt(path + ".spawns", spawnName, fileConfiguration);
				}
			}
		}else{
			areSpawnsReady = false;
		}
	}

}
