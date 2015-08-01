package org.kwstudios.play.ragemode.gameLogic;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.configuration.file.FileConfiguration;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;

public class LobbyTimer {

	private static final String LOBBY_DELAY_PATH = "settings.lobbydelay";

	private String gameName;
	private String[] playerUUIDs;
	private FileConfiguration fileConfiguration;
	private int secondsRemaining;

	public LobbyTimer(String gameName, String[] playerUUIDs, FileConfiguration fileConfiguration) {
		this.gameName = gameName;
		this.playerUUIDs = playerUUIDs;
		this.fileConfiguration = fileConfiguration;
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
		Timer t = new Timer();

		t.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				System.out.println("10 seconds passed");
			}
		}, 0, 10000);

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
