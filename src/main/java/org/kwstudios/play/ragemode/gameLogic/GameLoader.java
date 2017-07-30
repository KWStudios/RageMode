package org.kwstudios.play.ragemode.gameLogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.bossbar.BossbarLib;
import org.kwstudios.play.ragemode.items.CombatAxe;
import org.kwstudios.play.ragemode.items.RageArrow;
import org.kwstudios.play.ragemode.items.RageBow;
import org.kwstudios.play.ragemode.items.RageKnife;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.scoreboard.ScoreBoard;
import org.kwstudios.play.ragemode.scoreboard.ScoreBoardHolder;
import org.kwstudios.play.ragemode.signs.SignCreator;
import org.kwstudios.play.ragemode.toolbox.ActionBarAPI;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;
import org.kwstudios.play.ragemode.toolbox.GameBroadcast;

public class GameLoader {

	private String gameName;
	private FileConfiguration fileConfiguration;
	private List<Location> gameSpawns = new ArrayList<Location>();

	public GameLoader(String gameName, FileConfiguration fileConfiguration) {
		this.gameName = gameName;
		this.fileConfiguration = fileConfiguration;
		PlayerList.setGameRunning(gameName);
		checkTeleport();
		setInventories();
		List<String> players = Arrays.asList(PlayerList.getPlayersInGame(gameName));
		// TabGuiUpdater.setTabGui(players);
		// TabAPI.setTabGuiListOverLayForPlayers(players);

		// TabAPI.setTab(players, "Test", 0, 0);

		ScoreBoard gameBoard = new ScoreBoard(players, true);
		gameBoard.setTitle(ConstantHolder.SCOREBOARD_DEFAULT_TITLE);
		String kdLine = ChatColor.YELLOW + "0 / 0 " + ConstantHolder.SCOREBOARD_DEFAULT_KD;
		gameBoard.setLine(kdLine, 0);
		String pointsLine = ChatColor.YELLOW + "0 " + ConstantHolder.SCOREBOARD_DEFAULT_POINTS;
		gameBoard.setLine(pointsLine, 1);
		gameBoard.setScoreBoard();
		gameBoard.addToScoreBoards(gameName, true);
		for (String player : players) {
			ScoreBoardHolder holder = gameBoard.getScoreboards().get(Bukkit.getPlayer(UUID.fromString(player)));
			holder.setOldKdLine(kdLine);
			holder.setOldPointsLine(pointsLine);
		}
		new GameTimer(this.gameName, this.fileConfiguration);
		SignCreator.updateAllSigns(gameName);

		String initialMessage = ChatColor.DARK_AQUA + "Welcome to " + ChatColor.DARK_PURPLE + gameName
				+ ChatColor.DARK_AQUA + "!";
		if (ConfigFactory.getBoolean("settings.global", "bossbar", fileConfiguration)) {
			for (String player : players) {
				BossbarLib.getHandler().getBossbar(Bukkit.getPlayer(UUID.fromString(player))).setMessage(initialMessage)
						.setPercentage(1f);
				BossbarLib.getHandler().updateBossbar(Bukkit.getPlayer(UUID.fromString(player)));
			}
		}

		if (ConfigFactory.getBoolean("settings.global", "actionbar", fileConfiguration)) {
			for (String player : players) {
				ActionBarAPI.sendActionBar(Bukkit.getPlayer(UUID.fromString(player)), initialMessage);
			}
		}
	}

	private void checkTeleport() {
		GameSpawnGetter gameSpawnGetter = new GameSpawnGetter(gameName, fileConfiguration);
		if (gameSpawnGetter.isGameReady()) {
			gameSpawns = gameSpawnGetter.getSpawnLocations();
			teleportPlayersToGameSpawns();
		} else {
			String message = ConstantHolder.RAGEMODE_PREFIX
					+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().GAME_NOT_SET_UP);
			GameBroadcast.broadcastToGame(gameName, message);
			String[] players = PlayerList.getPlayersInGame(gameName);
			for (String player : players) {
				Player thisPlayer = Bukkit.getPlayer(UUID.fromString(player));
				PlayerList.removePlayer(thisPlayer);
			}
		}
	}

	private void teleportPlayersToGameSpawns() {
		String[] players = PlayerList.getPlayersInGame(gameName);
		for (int i = 0; i < players.length; i++) {
			Player player = Bukkit.getPlayer(UUID.fromString(players[i]));
			Location location = gameSpawns.get(i);
			player.teleport(location);
		}
	}

	private void setInventories() {
		String[] players = PlayerList.getPlayersInGame(gameName);
		for (String playerUUID : players) {
			Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
			player.getInventory().setItem(0, RageBow.getRageBow());
			player.getInventory().setItem(1, RageKnife.getRageKnife());
			player.getInventory().setItem(2, CombatAxe.getCombatAxe());
			player.getInventory().setItem(9, RageArrow.getRageArrow());
			// see positions here:
			// http://redditpublic.com/images/b/b2/Items_slot_number.png
		}
	}

}
