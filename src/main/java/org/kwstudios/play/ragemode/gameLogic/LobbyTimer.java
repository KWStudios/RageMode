package org.kwstudios.play.ragemode.gameLogic;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class LobbyTimer {

	private static final String LOBBY_DELAY_PATH = "settings.global.lobbydelay";

	private String gameName;
	private FileConfiguration fileConfiguration;
	private int secondsRemaining;
	private Timer t;

	public LobbyTimer(String gameName, FileConfiguration fileConfiguration) {
		this.gameName = gameName;
		this.fileConfiguration = fileConfiguration;
		getSecondsToWait();
		sendTimerMessages();
	}

	private void getSecondsToWait() {
		if (fileConfiguration.isSet(LOBBY_DELAY_PATH)
				&& isInt(ConfigFactory.getString("settings.global", "lobbydelay", fileConfiguration))) {
			secondsRemaining = ConfigFactory.getInt("settings.global", "lobbydelay", fileConfiguration);
		} else {
			secondsRemaining = 30;
			ConfigFactory.setInt("settings.global", "lobbydelay", secondsRemaining, fileConfiguration);
		}

		if (fileConfiguration.isSet(ConstantHolder.GAME_PATH + "." + gameName + ".lobbydelay") && isInt(
				ConfigFactory.getString(ConstantHolder.GAME_PATH + "." + gameName, "lobbydelay", fileConfiguration))) {
			secondsRemaining = ConfigFactory.getInt(ConstantHolder.GAME_PATH + "." + gameName, "lobbydelay",
					fileConfiguration);
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
					String[] playerUUIDs = PlayerList.getPlayersInGame(gameName);
					for (int i = 0; i < playerUUIDs.length; i++) {
						String message = ConstantHolder.RAGEMODE_PREFIX
								+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().LOBBY_MESSAGE
										.replace("$TIME$", Integer.toString(totalMessagesBeforeTen * 10)));
						Bukkit.getPlayer(UUID.fromString(playerUUIDs[i])).sendMessage(message);
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
					String[] playerUUIDs = PlayerList.getPlayersInGame(gameName);
					for (int i = 0; i < playerUUIDs.length; i++) {
						String message = ConstantHolder.RAGEMODE_PREFIX
								+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().LOBBY_MESSAGE
										.replace("$TIME$", Integer.toString(timesToSendMessage)));
						Bukkit.getPlayer(UUID.fromString(playerUUIDs[i])).sendMessage(message);
					}
					timesToSendMessage--;
				} else if (timesToSendMessage == 0 && PlayerList.getPlayersInGame(gameName).length >= 2) {
					this.cancel();
					PluginLoader.getInstance().getServer().getScheduler()
							.scheduleSyncDelayedTask(PluginLoader.getInstance(), new Runnable() {
						@Override
						public void run() {
							new GameLoader(gameName, fileConfiguration);
						}
					});
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
