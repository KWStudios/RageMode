package org.kwstudios.play.ragemode.gameLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

//import com.gmail.zahusek.tinyprotocolapi.api.tabapi.TabAPI;

public class TabGuiUpdater {/*

	public static void setTabGui(List<String> playerUUIDs) {
		for (String playerUUID : playerUUIDs) {
			Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
			TabAPI.defaultTab(player);
			setTitles(player);
			TabAPI.modifyTab(player);
		}
	}

	public static void setTabGui(String playerUUID) {
		Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
		TabAPI.defaultTab(player);
		setTitles(player);
		TabAPI.modifyTab(player);
	}

	public static void updateTabGui(String gameName) {
		if (gameName != null) {
			List<PlayerPoints> playersPoints = new ArrayList<PlayerPoints>();
			String[] playersInGame = PlayerList.getPlayersInGame(gameName);
			if (playersInGame != null) {
				for (String playerInGame : playersInGame) {
					if (playerInGame != null) {
						PlayerPoints playerPoints = RageScores.getPlayerPoints(playerInGame);
						if (playerPoints != null) {
							playersPoints.add(playerPoints);
						}
					}
				}
			}
			for (PlayerPoints playerPoints : playersPoints) {
				Player player = Bukkit.getPlayer(UUID.fromString(playerPoints.getPlayerUUID()));
				TabAPI.setTabSlot(player, 0, 1, ChatColor.BLUE + Integer.toString(playersPoints.size()), 0);
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
					TabAPI.setTabSlot(innerPlayer, 0, i + 5, ChatColor.YELLOW + Integer.toString(points), 0);
					TabAPI.setTabSlot(innerPlayer, 1, i + 5, ChatColor.YELLOW + player.getName(), 0);
					TabAPI.setTabSlot(innerPlayer, 2, i + 5,
							ChatColor.YELLOW + Integer.toString(kills) + " - " + Integer.toString(deaths), 0);
				}
			}

			for (PlayerPoints playerPoints : playersPoints) {
				TabAPI.modifyTab(Bukkit.getPlayer(UUID.fromString(playerPoints.getPlayerUUID())));
			}
		}
	}
	
	public static void removeTabForPlayer(Player player){
		TabAPI.removeTab(player);
	}

	private static void setTitles(Player player) {
		TabAPI.setTabHnF(player, ChatColor.DARK_RED + "RageMode",
				ChatColor.translateAlternateColorCodes('&', "&eKWStudios.org ")
						+ ChatColor.translateAlternateColorCodes('&', "&3Network"));

		TabAPI.setTabSlot(player, 0, 0, ChatColor.translateAlternateColorCodes('&', "&9Player"), 100);
		TabAPI.setTabSlot(player, 1, 0, ChatColor.translateAlternateColorCodes('&', "&9Time"), 100);
		TabAPI.setTabSlot(player, 2, 0, ChatColor.translateAlternateColorCodes('&', "&9Map"), 100);

		TabAPI.setTabSlot(player, 0, 4, ChatColor.translateAlternateColorCodes('&', "&6&lPoints"), 100);
		TabAPI.setTabSlot(player, 1, 4, ChatColor.translateAlternateColorCodes('&', "&6&lPlayer"), 100);
		TabAPI.setTabSlot(player, 2, 4, ChatColor.translateAlternateColorCodes('&', "&6&lKills - Deaths"), 100);
	}*/

}
