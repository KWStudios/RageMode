package org.kwstudios.play.ragemode.signs;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.SignChangeEvent;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;
import org.kwstudios.play.ragemode.toolbox.GetGames;

public class SignCreator {

	public synchronized static boolean createNewSign(Sign sign, String game) {
		File file = SignConfiguration.getYamlSignsFile();
		FileConfiguration fileConfiguration = SignConfiguration.getSignConfiguration();

		Set<String> signs = ConfigFactory.getKeysUnderPath("signs", false, fileConfiguration);
		if (signs != null) {
			if (signs.contains(Integer.toString(sign.getX()) + Integer.toString(sign.getY())
					+ Integer.toString(sign.getZ()) + sign.getWorld().getName())) {
				return false;
			}
		}

		int x = sign.getX();
		int y = sign.getY();
		int z = sign.getZ();
		String world = sign.getWorld().getName();
		String path = "signs." + Integer.toString(x) + Integer.toString(y) + Integer.toString(z) + world;

		ConfigFactory.setString(path, "game", game, fileConfiguration);
		ConfigFactory.setString(path, "world", world, fileConfiguration);
		ConfigFactory.setInt(path, "x", x, fileConfiguration);
		ConfigFactory.setInt(path, "y", y, fileConfiguration);
		ConfigFactory.setInt(path, "z", z, fileConfiguration);

		try {
			fileConfiguration.save(file);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public synchronized static boolean removeSign(Sign sign) {
		File file = SignConfiguration.getYamlSignsFile();
		FileConfiguration fileConfiguration = SignConfiguration.getSignConfiguration();

		int x = sign.getX();
		int y = sign.getY();
		int z = sign.getZ();
		String world = sign.getWorld().getName();
		String path = "signs." + Integer.toString(x) + Integer.toString(y) + Integer.toString(z) + world;

		if (fileConfiguration.contains(path)) {
			fileConfiguration.set(path, null);
			try {
				fileConfiguration.save(file);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public static boolean updateSign(SignChangeEvent sign) {
		FileConfiguration fileConfiguration = SignConfiguration.getSignConfiguration();

		int x = sign.getBlock().getState().getX();
		int y = sign.getBlock().getState().getY();
		int z = sign.getBlock().getState().getZ();
		String world = sign.getBlock().getState().getWorld().getName();
		String path = "signs." + Integer.toString(x) + Integer.toString(y) + Integer.toString(z) + world;

		if (fileConfiguration.contains(path)) {
			String game = ConfigFactory.getString(path, "game", fileConfiguration);
			sign.setLine(0, ChatColor.DARK_AQUA + "[" + ChatColor.DARK_PURPLE + "RageMode" + ChatColor.DARK_AQUA + "]");
			sign.setLine(1, game);
			sign.setLine(2,
					"Players " + ChatColor.DARK_AQUA + "[" + Integer.toString(PlayerList.getPlayersInGame(game).length)
							+ " / " + GetGames.getMaxPlayers(game, PluginLoader.getInstance().getConfig()) + "]");
			String running = (PlayerList.isGameRunning(game))
					? ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "Running..."
					: ChatColor.DARK_GREEN.toString() + "Waiting...";
			sign.setLine(3, running);
			if (sign.getBlock().getState().update()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Updates all JoinSigns for the given game which are properly configured.
	 * 
	 * @param gameName
	 *            The name of the game for which the signs should be updated.
	 * @return True if at least one sign was updated successfully for the given
	 *         game.
	 */
	public static boolean updateAllSigns(String gameName) {
		FileConfiguration fileConfiguration = SignConfiguration.getSignConfiguration();

		Set<String> signs = ConfigFactory.getKeysUnderPath("signs", false, fileConfiguration);
		if (signs != null) {
			for (String signString : signs) {
				String path = "signs." + signString;
				String game = ConfigFactory.getString(path, "game", fileConfiguration);
				if (game != null) {
					if (game.trim().equalsIgnoreCase(gameName.trim())) {
						int x = ConfigFactory.getInt(path, "x", fileConfiguration);
						int y = ConfigFactory.getInt(path, "y", fileConfiguration);
						int z = ConfigFactory.getInt(path, "z", fileConfiguration);
						String world = ConfigFactory.getString(path, "world", fileConfiguration);
						Location signLocation = new Location(Bukkit.getWorld(world), x, y, z);
						if (signLocation.getBlock().getState() instanceof Sign) {
							Sign sign = (Sign) signLocation.getBlock().getState();
							sign.setLine(0, ChatColor.DARK_AQUA + "[" + ChatColor.DARK_PURPLE + "RageMode"
									+ ChatColor.DARK_AQUA + "]");
							sign.setLine(1, game);
							sign.setLine(2, "Players " + ChatColor.DARK_AQUA + "["
									+ Integer.toString(PlayerList.getPlayersInGame(game).length) + " / "
									+ GetGames.getMaxPlayers(game, PluginLoader.getInstance().getConfig()) + "]");
							String running = (PlayerList.isGameRunning(game))
									? ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "Running..."
									: ChatColor.DARK_GREEN.toString() + "Waiting...";
							sign.setLine(3, running);
							sign.update();
						} else {
							Bukkit.getConsoleSender().sendMessage(ConstantHolder.RAGEMODE_PREFIX
									+ "A funny jester tried to modify the signs.yml without commands.");
							Bukkit.getConsoleSender().sendMessage(ConstantHolder.RAGEMODE_PREFIX + "Skipping update of "
									+ ChatColor.DARK_PURPLE + "(1)" + ChatColor.RESET + " sign...");
						}
					}
				} else {
					return false;
				}
			}
			return true;
		}

		return false;
	}

	/**
	 * Checks whether the given Sign is properly configured to be an JoinSign or
	 * not.
	 * 
	 * @param sign
	 *            The Sign for which the check should be done.
	 * @return True if the signs.yml contains a correct value for the given Sign
	 *         instance.
	 */
	public static boolean isJoinSign(Sign sign) {
		FileConfiguration fileConfiguration = SignConfiguration.getSignConfiguration();

		Set<String> signs = ConfigFactory.getKeysUnderPath("signs", false, fileConfiguration);
		if (signs != null) {
			for (String signString : signs) {
				String path = "signs." + signString;
				String game = ConfigFactory.getString(path, "game", fileConfiguration);
				for (String gameName : GetGames.getGameNames(PluginLoader.getInstance().getConfig())) {
					if (game.trim().equalsIgnoreCase(gameName.trim())) {
						int x = ConfigFactory.getInt(path, "x", fileConfiguration);
						int y = ConfigFactory.getInt(path, "y", fileConfiguration);
						int z = ConfigFactory.getInt(path, "z", fileConfiguration);
						String world = ConfigFactory.getString(path, "world", fileConfiguration);
						Location signLocation = new Location(Bukkit.getWorld(world), x, y, z);
						if (signLocation.getBlock().getState() instanceof Sign) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public static String getGameFromSign(Sign sign) {
		FileConfiguration fileConfiguration = SignConfiguration.getSignConfiguration();

		int x = sign.getX();
		int y = sign.getY();
		int z = sign.getZ();
		String world = sign.getWorld().getName();
		String signString = Integer.toString(x) + Integer.toString(y) + Integer.toString(z) + world;
		String path = "signs." + signString;
		String game = ConfigFactory.getString(path, "game", fileConfiguration);
		for (String gameName : GetGames.getGameNames(PluginLoader.getInstance().getConfig())) {
			if (game.trim().equalsIgnoreCase(gameName.trim())) {
				return game;
			}
		}

		return "";
	}

}
