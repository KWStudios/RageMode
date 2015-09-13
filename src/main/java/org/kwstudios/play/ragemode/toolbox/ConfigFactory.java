package org.kwstudios.play.ragemode.toolbox;

import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigFactory {

	// private static final File CONFIG_DIRECTORY = new File("KWPlugin");
	// private static final File CONFIG_FILE = new File("KWPlugin/config.yml");
	// private static YamlConfiguration config = null;

	// public static YamlConfiguration getConfig() {
	// createConfigFile();
	// if (config == null) {
	// config = YamlConfiguration.loadConfiguration(configFile);
	// }
	// return config;
	// }

	public static Boolean getBoolean(String path, String key, FileConfiguration fileConfiguration) {
		return fileConfiguration.getBoolean(path + "." + key);
	}

	public static void setBoolean(String path, String key, Boolean value, FileConfiguration fileConfiguration) {
		fileConfiguration.set(path + "." + key, value);
		// save();
	}

	public static String getString(String path, String key, FileConfiguration fileConfiguration) {
		return fileConfiguration.getString(path + "." + key);
	}

	public static void setString(String path, String key, String value, FileConfiguration fileConfiguration) {
		fileConfiguration.set(path + "." + key, value);
		// save();
	}

	public static int getInt(String path, String key, FileConfiguration fileConfiguration) {
		try {
			return fileConfiguration.getInt(path + "." + key);
		} catch (Exception e) {
			return -32500000;
		}
	}

	public static void setInt(String path, String key, int value, FileConfiguration fileConfiguration) {
		fileConfiguration.set(path + "." + key, value);
		// save();
	}

	public static double getDouble(String path, String key, FileConfiguration fileConfiguration) {
		try{
		return fileConfiguration.getDouble(path + "." + key);
		}catch(Exception e){
			return -32500000;
		}
	}

	public static void setDouble(String path, String key, double value, FileConfiguration fileConfiguration) {
		fileConfiguration.set(path + "." + key, value);
		// save();
	}

	public static Set<String> getKeysUnderPath(String path, boolean deep, FileConfiguration fileConfiguration) {
		if(fileConfiguration != null) {
			if(fileConfiguration.getConfigurationSection(path) != null) {
				Set<String> keys = fileConfiguration.getConfigurationSection(path).getKeys(deep); //TODO remove nullPointers
				return keys;			
			}			
		}

		return null;
	}

	// private static void save(FileConfiguration fileConfiguration) {
	// createConfigFile();
	// try {
	// fileConfiguration.sa;
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	// private static void createConfigFile() {
	// if (!configDirectory.exists()) {
	// configDirectory.mkdir();
	// }
	//
	// if (!configFile.exists()) {
	// try {
	// configFile.createNewFile();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }

}
