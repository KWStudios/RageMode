package org.kwstudios.play.ragemode.signs;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.kwstudios.play.ragemode.loader.PluginLoader;

public class SignConfiguration {

	private static boolean inited = false;
	private static File yamlSignsFile;
	private static FileConfiguration signConfiguration;

	public static void initSignConfiguration() {
		if (inited)
			return;
		else
			inited = true;

		File file = new File(PluginLoader.getInstance().getDataFolder(), "stats.yml");
		YamlConfiguration config = null;
		yamlSignsFile = file;

		if (!file.exists()) {
			file.getParentFile().mkdirs();

			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			config = new YamlConfiguration();
			config.createSection("data");

			try {
				config.save(file);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		} else {
			config = YamlConfiguration.loadConfiguration(file);
		}

		signConfiguration = config;
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File getYamlSignsFile() {
		return yamlSignsFile;
	}

	public static FileConfiguration getSignConfiguration() {
		return signConfiguration;
	}

}
