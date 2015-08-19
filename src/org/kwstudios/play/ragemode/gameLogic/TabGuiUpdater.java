package org.kwstudios.play.ragemode.gameLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.gmail.zahusek.tinyprotocolapi.api.tabapi.TabAPI;

public class TabGuiUpdater {

	public static void setTabGui(List<String> playerUUIDs) {
		for (String playerUUID : playerUUIDs) {
			Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
			setTitles(player);
			TabAPI.modifyTab(player);
		}
	}

	public static void setTabGui(String playerUUID) {
		Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
		setTitles(player);
		TabAPI.modifyTab(player);
	}

	public static void updateTabGui(String gameName) {
		List<PlayerPoints> playersPoints = new ArrayList<PlayerPoints>();
		String[] playersInGame = PlayerList.getPlayersInGame(gameName);
		for (String playerInGame : playersInGame) {
			PlayerPoints playerPoints = RageScores.getPlayerPoints(playerInGame);
			if (playerPoints != null) {
				playersPoints.add(playerPoints);
			}
		}
		for (PlayerPoints playerPoints : playersPoints) {
			Player player = Bukkit.getPlayer(UUID.fromString(playerPoints.getPlayerUUID()));
			TabAPI.setTabSlot(player, 2, 0, ChatColor.BLUE + Integer.toString(playersPoints.size()), 0);
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
				TabAPI.setTabSlot(innerPlayer, i + 6, 0, ChatColor.YELLOW + Integer.toString(points), 0);
				TabAPI.setTabSlot(innerPlayer, i + 6, 1, ChatColor.YELLOW + player.getName(), 0);
				TabAPI.setTabSlot(innerPlayer, i + 6, 2,
						ChatColor.YELLOW + Integer.toString(kills) + " - " + Integer.toString(deaths), 0);
			}
		}

		for (PlayerPoints playerPoints : playersPoints) {
			TabAPI.modifyTab(Bukkit.getPlayer(UUID.fromString(playerPoints.getPlayerUUID())));
		}
	}

	private static void setTitles(Player player) {
		TabAPI.setTabHnF(player, ChatColor.DARK_RED + "RageMode",
				ChatColor.YELLOW + "KWStudios.org " + ChatColor.BLUE + "Network");

		TabAPI.setTabSlot(player, 1, 0, ChatColor.DARK_AQUA + "Player", 0);
		TabAPI.setTabSlot(player, 1, 1, ChatColor.DARK_AQUA + "Time", 0);
		TabAPI.setTabSlot(player, 1, 2, ChatColor.DARK_AQUA + "Map", 0);

		TabAPI.setTabSlot(player, 4, 0, ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + "Points", 0);
		TabAPI.setTabSlot(player, 4, 1, ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + "Player", 0);
		TabAPI.setTabSlot(player, 4, 2, ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + "Kills - Deaths", 0);
	}

}
