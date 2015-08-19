package org.kwstudios.play.ragemode.gameLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.tabapi.TabAPI;

public class TabGuiUpdater {

	public static void setTabGui(List<String> playerUUIDs) {
		for (String playerUUID : playerUUIDs) {
			Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
			TabAPI.setPriority(PluginLoader.getInstance(), player, 2);
			setTitles(player);
			TabAPI.updatePlayer(player);
		}
	}

	public static void setTabGui(String playerUUID) {
		Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
		TabAPI.setPriority(PluginLoader.getInstance(), player, 2);
		setTitles(player);
		TabAPI.updatePlayer(player);
	}

	public static void updateTabGui(String gameName) {
		List<PlayerPoints> playersPoints = new ArrayList<PlayerPoints>();
		String[] playersInGame = PlayerList.getPlayersInGame(gameName);
		for(String playerInGame : playersInGame){
			PlayerPoints playerPoints = RageScores.getPlayerPoints(playerInGame);
			if(playerPoints != null){
				playersPoints.add(playerPoints);
			}
		}
		for (PlayerPoints playerPoints : playersPoints) {
			Player player = Bukkit.getPlayer(UUID.fromString(playerPoints.getPlayerUUID()));
			TabAPI.setTabString(PluginLoader.getInstance(), player, 2, 0,
					ChatColor.BLUE + Integer.toString(playersPoints.size()));
		}

		Collections.sort(playersPoints);
		for (int i = 0; i < playersPoints.size(); i++) {
			PlayerPoints playerPoints = playersPoints.get(i);
			Player player = Bukkit.getPlayer(UUID.fromString(playerPoints.getPlayerUUID()));
			int points = playerPoints.getPoints();
			int kills = playerPoints.getKills();
			int deaths = playerPoints.getDeaths();
			for (PlayerPoints innerPlayerPoints : playersPoints) {
				Player innerPlayer = Bukkit.getPlayer(UUID.fromString(innerPlayerPoints.getPlayerUUID()));
				TabAPI.setTabString(PluginLoader.getInstance(), innerPlayer, i + 6, 0,
						ChatColor.YELLOW + Integer.toString(points));
				TabAPI.setTabString(PluginLoader.getInstance(), innerPlayer, i + 6, 1,
						ChatColor.YELLOW + player.getName());
				TabAPI.setTabString(PluginLoader.getInstance(), innerPlayer, i + 6, 2,
						ChatColor.YELLOW + Integer.toString(kills) + " - " + Integer.toString(deaths));
			}
		}

		for (PlayerPoints playerPoints : playersPoints) {
			TabAPI.updatePlayer(Bukkit.getPlayer(UUID.fromString(playerPoints.getPlayerUUID())));
		}
	}

	private static void setTitles(Player player) {
		TabAPI.setTabString(PluginLoader.getInstance(), player, 0, 1, ChatColor.YELLOW + "RageMode");

		TabAPI.setTabString(PluginLoader.getInstance(), player, 1, 0, ChatColor.DARK_AQUA + "Player");
		TabAPI.setTabString(PluginLoader.getInstance(), player, 1, 1, ChatColor.DARK_AQUA + "Time");
		TabAPI.setTabString(PluginLoader.getInstance(), player, 1, 2, ChatColor.DARK_AQUA + "Map");

		TabAPI.setTabString(PluginLoader.getInstance(), player, 4, 0,
				ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + "Points");
		TabAPI.setTabString(PluginLoader.getInstance(), player, 4, 1,
				ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + "Player");
		TabAPI.setTabString(PluginLoader.getInstance(), player, 4, 2,
				ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + "Kills - Deaths");
	}

}
