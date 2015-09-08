package org.kwstudios.play.ragemode.signs;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.SignChangeEvent;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
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
		// File file = SignConfiguration.getYamlSignsFile();
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
					? ChatColor.DARK_GREEN.toString() + ChatColor.ITALIC.toString() + "Running"
					: ChatColor.DARK_AQUA.toString() + "Waiting...";
			sign.setLine(3, running);
			sign.getBlock().getState().update(true);
		}

		return false;
	}

}
