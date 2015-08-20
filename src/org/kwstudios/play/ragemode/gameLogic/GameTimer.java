package org.kwstudios.play.ragemode.gameLogic;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.kwstudios.play.ragemode.commands.StopGame;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class GameTimer {
	
	private static final String GAME_TIME_PATH = "settings.gametime";

	private String gameName;
	private FileConfiguration fileConfiguration;
	private int secondsRemaining;
	private Timer t;
	
	public GameTimer(String gameName, FileConfiguration fileConfiguration) {
		this.gameName = gameName;
		this.fileConfiguration = fileConfiguration;
		PlayerList.setGameRunning(gameName);
		getMinutesToGo();
		sendTimerMessages();
	}
	
	private void getMinutesToGo() {
		if (fileConfiguration.isSet(GAME_TIME_PATH)
				&& isInt(ConfigFactory.getString("settings", "gametime", fileConfiguration))) {
			secondsRemaining = ConfigFactory.getInt("settings", "gametime", fileConfiguration) * 60;
		} else {
			secondsRemaining = 300;
			ConfigFactory.setInt("settings", "gametime", secondsRemaining/60, fileConfiguration);
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
			private int totalMessagesBeforeTen = timeMillisForLoop / 60000;

			public void run() {
				if (totalMessagesBeforeTen > 0 && PlayerList.getPlayersInGame(gameName).length >= 2) {
					String[] playerUUIDs = PlayerList.getPlayersInGame(gameName);
					for (int i = 0; i < playerUUIDs.length; i++) {
						Bukkit.getPlayer(UUID.fromString(playerUUIDs[i]))
								.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.BLUE
										+ "This round will end in " + ChatColor.YELLOW
										+ Integer.toString(totalMessagesBeforeTen) + ChatColor.BLUE + " minutes.");
					}
					totalMessagesBeforeTen--;
				}else{
					this.cancel();
					StopGame.stopGame(gameName);
				}
			}
		}, 0, 60000);
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
