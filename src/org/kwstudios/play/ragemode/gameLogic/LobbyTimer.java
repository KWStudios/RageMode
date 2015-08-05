package org.kwstudios.play.ragemode.gameLogic;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class LobbyTimer {

	private static final String LOBBY_DELAY_PATH = "settings.lobbydelay";

	private String gameName;
	private String[] playerUUIDs;
	private FileConfiguration fileConfiguration;
	private int secondsRemaining;
	private Timer t;

	public LobbyTimer(String gameName, String[] playerUUIDs, FileConfiguration fileConfiguration) {
		this.gameName = gameName;
		this.playerUUIDs = playerUUIDs;
		this.fileConfiguration = fileConfiguration;
		PlayerList.setGameRunning(gameName);
		getSecondsToWait();
		sendTimerMessages();
	}

	private void getSecondsToWait() {
		if (fileConfiguration.isSet(LOBBY_DELAY_PATH)
				&& isInt(ConfigFactory.getString("settings", "lobbydelay", fileConfiguration))) {
			secondsRemaining = ConfigFactory.getInt("settings", "lobbydelay", fileConfiguration);
		} else {
			secondsRemaining = 30;
			ConfigFactory.setInt("settings", "lobbydelay", secondsRemaining, fileConfiguration);
		}
	}

	private void sendTimerMessages() {
		t = new Timer();

		int totalTimerMillis = ((int) (((secondsRemaining * 1000) + 5000) / 10000)) * (10000);
		if (totalTimerMillis == 0) {
			totalTimerMillis = 10000;
		}
		final int timeMillisForLoop = totalTimerMillis;

		t.scheduleAtFixedRate(new TimerTask() {
			private int totalMessagesBeforeTen = timeMillisForLoop / 10000;

			public void run() {
				if (totalMessagesBeforeTen > 0 && PlayerList.getPlayersInGame(gameName).length >= 2) {
					for (int i = 0; i < playerUUIDs.length; i++) {
						Bukkit.getPlayer(UUID.fromString(playerUUIDs[i]))
								.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.BLUE
										+ "This round will start in " + ChatColor.YELLOW
										+ Integer.toString(totalMessagesBeforeTen * 10) + ChatColor.BLUE + " seconds.");
					}
					totalMessagesBeforeTen--;
					if (totalMessagesBeforeTen == 0) {
						this.cancel();
						startTimerFromTen();
					}
				} else if (PlayerList.getPlayersInGame(gameName).length < 2) {
					this.cancel();
				}
			}
		}, 0, 10000);
	}

	private void startTimerFromTen() {
		t.scheduleAtFixedRate(new TimerTask() {
			private int timesToSendMessage = 9;

			@Override
			public void run() {
				if (timesToSendMessage > 0 && PlayerList.getPlayersInGame(gameName).length >= 2) {
					for (int i = 0; i < playerUUIDs.length; i++) {
						Bukkit.getPlayer(UUID.fromString(playerUUIDs[i]))
								.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.BLUE
										+ "This round will start in " + ChatColor.YELLOW
										+ Integer.toString(timesToSendMessage) + ChatColor.BLUE + " seconds.");
					}
					timesToSendMessage--;
				} else if (timesToSendMessage == 0 && PlayerList.getPlayersInGame(gameName).length >= 2) {
					this.cancel();
					new GameLoader(gameName, fileConfiguration);
				} else {
					this.cancel();
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
