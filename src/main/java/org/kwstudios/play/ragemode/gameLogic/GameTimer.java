package org.kwstudios.play.ragemode.gameLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.kwstudios.play.ragemode.bossbar.BossbarLib;
import org.kwstudios.play.ragemode.commands.StopGame;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.scoreboard.ScoreBoard;
import org.kwstudios.play.ragemode.scores.PlayerPoints;
import org.kwstudios.play.ragemode.scores.RageScores;
import org.kwstudios.play.ragemode.toolbox.ActionBarAPI;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class GameTimer {

	private static final String GAME_TIME_PATH = "settings.global.gametime";

	private String gameName;
	private FileConfiguration fileConfiguration;
	private int secondsRemaining;
	private Timer t;

	public GameTimer(String gameName, FileConfiguration fileConfiguration) {
		this.gameName = gameName;
		this.fileConfiguration = fileConfiguration;
		PlayerList.setGameRunning(gameName);
		getMinutesToGo();
		startCounting();
	}

	private void getMinutesToGo() {
		if (fileConfiguration.isSet(GAME_TIME_PATH)
				&& isInt(ConfigFactory.getString("settings.global", "gametime", fileConfiguration))) {
			secondsRemaining = ConfigFactory.getInt("settings.global", "gametime", fileConfiguration) * 60;
		} else {
			secondsRemaining = 300;
			ConfigFactory.setInt("settings.global", "gametime", secondsRemaining / 60, fileConfiguration);
		}

		if (fileConfiguration.isSet(ConstantHolder.GAME_PATH + "." + gameName + ".gametime") && isInt(
				ConfigFactory.getString(ConstantHolder.GAME_PATH + "." + gameName, "gametime", fileConfiguration))) {
			secondsRemaining = ConfigFactory.getInt(ConstantHolder.GAME_PATH + "." + gameName, "gametime",
					fileConfiguration) * 60;
		}
	}

	private void startCounting() {
		t = new Timer();

		int totalTimerMillis = ((int) (((secondsRemaining * 1000) + 5000) / 10000)) * (10000);
		if (totalTimerMillis == 0) {
			totalTimerMillis = 10000;
		}
		final int timeMillisForLoop = totalTimerMillis;

		t.scheduleAtFixedRate(new TimerTask() {
			private int totalMessages = timeMillisForLoop / 60000;
			private int secondsRemaining = timeMillisForLoop / 1000;
			private int bossBarPointer = 0;
			// private int totalTimesLooped = 0;
			// private final long startTimeMillis = System.currentTimeMillis();

			public void run() {
				if (totalMessages > 0 && PlayerList.getPlayersInGame(gameName).length >= 2) {

					// long timeMillisNow = System.currentTimeMillis();
					// long timeMillisPassed = timeMillisNow - startTimeMillis;

					// if (fullMinute % 1 == 0) {
					// String[] playerUUIDs =
					// PlayerList.getPlayersInGame(gameName);
					// for (int i = 0; i < playerUUIDs.length; i++) {
					// Bukkit.getPlayer(UUID.fromString(playerUUIDs[i]))
					// .sendMessage(ConstantHolder.RAGEMODE_PREFIX +
					// ChatColor.BLUE
					// + "This round will end in " + ChatColor.YELLOW
					// + Integer.toString(totalMessages) + ChatColor.BLUE + "
					// minutes.");
					// }
					// totalMessages--;
					// }
					double minutes = Math.floor(secondsRemaining / 60);
					double seconds = secondsRemaining % 60;
					secondsRemaining--;
					String minutesString;
					String secondsString;
					if (minutes < 10) {
						minutesString = "0" + Integer.toString((int) minutes);
					} else {
						minutesString = Integer.toString((int) minutes);
					}
					secondsString = (seconds < 10) ? "0" + Integer.toString((int) seconds)
							: Integer.toString((int) seconds);
					ScoreBoard gameBoard = ScoreBoard.allScoreBoards.get(gameName);
					gameBoard.setTitle(ConstantHolder.SCOREBOARD_DEFAULT_TITLE + " " + ChatColor.DARK_AQUA
							+ minutesString + ":" + secondsString);
					if (secondsRemaining == 0) {
						this.cancel();
						PluginLoader.getInstance().getServer().getScheduler()
								.scheduleSyncDelayedTask(PluginLoader.getInstance(), new Runnable() {
							@Override
							public void run() {
								StopGame.stopGame(gameName);
							}
						});
					}

					// -------BossBar and ActionBar update-------

					if (secondsRemaining % 2 == 0) {
						String[] playerUUIDs = PlayerList.getPlayersInGame(gameName);
						List<PlayerPoints> playerPoints = new ArrayList<>();
						for (String playerUUID : playerUUIDs) {
							PlayerPoints points = RageScores.getPlayerPoints(playerUUID);
							if (points != null) {
								playerPoints.add(points);
							}
						}
						Collections.sort(playerPoints);
						if (playerPoints.size() > bossBarPointer) {
							PlayerPoints points = playerPoints.get(bossBarPointer);
							String message = ChatColor.DARK_GREEN + Integer.toString(bossBarPointer + 1) + ": "
									+ ChatColor.DARK_AQUA
									+ Bukkit.getPlayer(UUID.fromString(points.getPlayerUUID())).getName() + " - "
									+ ChatColor.GOLD + Integer.toString(points.getPoints()) + ChatColor.DARK_AQUA
									+ " points. " + ChatColor.GOLD + Integer.toString(points.getKills()) + " / "
									+ Integer.toString(points.getDeaths()) + ChatColor.DARK_AQUA + " K/D.";
							for (String playerUUID : playerUUIDs) {
								if (ConfigFactory.getBoolean("settings.global", "statistics", fileConfiguration)) {
									BossbarLib.getHandler().getBossbar(Bukkit.getPlayer(UUID.fromString(playerUUID)))
											.setMessage(message).setPercentage(1f);
									BossbarLib.getHandler()
											.updateBossbar(Bukkit.getPlayer(UUID.fromString(playerUUID)));
								}
								if (ConfigFactory.getBoolean("settings.global", "actionbar", fileConfiguration)) {
									ActionBarAPI.sendActionBar(Bukkit.getPlayer(UUID.fromString(playerUUID)), message);
								}
							}
							bossBarPointer++;
						} else {
							if (bossBarPointer >= PlayerList.getPlayersInGame(gameName).length) {
								bossBarPointer = 0;
							}
						}
					}

					// -------End of BossBar and ActionBar update-------
				} else {
					this.cancel();
					PluginLoader.getInstance().getServer().getScheduler()
							.scheduleSyncDelayedTask(PluginLoader.getInstance(), new Runnable() {
						@Override
						public void run() {
							StopGame.stopGame(gameName);
						}
					});

				}
			}
		}, 0, 1000);
	}

	private boolean isInt(String string) {
		try {
			Integer.parseInt(string);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
