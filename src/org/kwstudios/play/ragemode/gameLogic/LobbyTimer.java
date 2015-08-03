package org.kwstudios.play.ragemode.gameLogic;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;

import com.google.common.util.concurrent.ExecutionError;

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
		
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(10);
		ses.scheduleAtFixedRate(new Runnable() {
		    @Override
		    public void run() {
		        // do some work
		    }
		}, 0, 10000, TimeUnit.MILLISECONDS);  // execute every x seconds
		

		int totalTimerMillis = ((int) (((secondsRemaining * 1000) + 5000) / 10000)) * (10000);
		if (totalTimerMillis == 0) {
			totalTimerMillis = 10000;
		}
		final int timeMillisForLoop = totalTimerMillis;

		t.scheduleAtFixedRate(new TimerTask() {
			private int totalMessagesBeforeTen = timeMillisForLoop / 10000;
			public void run() {
				if (totalMessagesBeforeTen > 0 && PlayerList.getPlayersInGame(gameName).length >= 2) {
					System.out.println("10 seconds passed");
					for (int i = 0; i < playerUUIDs.length; i++) {
						Bukkit.getPlayer(UUID.fromString(playerUUIDs[i])).sendMessage("10 seconds passed");
					}
					totalMessagesBeforeTen--;
				}else{
					this.cancel();
					//startTimerFromTen();
				}
			}
		}, 0, 10000);
		
		t.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				
			}}, 0, 1000);

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
