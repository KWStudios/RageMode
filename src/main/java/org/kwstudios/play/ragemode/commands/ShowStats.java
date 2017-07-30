package org.kwstudios.play.ragemode.commands;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

/*import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;*/
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.runtimeRPP.RuntimeRPPManager;
import org.kwstudios.play.ragemode.scores.RetPlayerPoints;
import org.kwstudios.play.ragemode.statistics.MySQLStats;
import org.kwstudios.play.ragemode.statistics.YAMLStats;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class ShowStats {
	public ShowStats(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		if (args.length < 2) {
			constructMessage(player, player.getName());
		} else {
			constructMessage(player, args[1]);
		}
	}

	private void constructMessage(Player player, String playerName) {
		Thread thread = new Thread(new uuiderThread(player, playerName));
		thread.start();
	}

	private class UUIDStrings {
		public String id;
		public String name;
		@SuppressWarnings("unused")
		public boolean legacy;
	}

	private class uuiderThread implements Runnable {
		private String name;
		private Player player;
		private String sUUID;

		public uuiderThread(Player player, String name) {
			this.player = player;
			this.name = name;
		}

		@Override
		public void run() {
			// http("https://api.mojang.com/users/profiles/minecraft/" + name,
			// "");
			https();

			if (sUUID == null) {
				String message = ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PLAYER_NONEXISTENT);
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + message);
				return;				
			}


			CharSequence sUUID_SEQ_1 = sUUID.subSequence(0, 8);
			CharSequence sUUID_SEQ_2 = sUUID.subSequence(8, 12);
			CharSequence sUUID_SEQ_3 = sUUID.subSequence(12, 16);
			CharSequence sUUID_SEQ_4 = sUUID.subSequence(16, 20);
			CharSequence sUUID_SEQ_5 = sUUID.subSequence(20, 32);
			sUUID = new String(
					sUUID_SEQ_1 + "-" + sUUID_SEQ_2 + "-" + sUUID_SEQ_3 + "-" + sUUID_SEQ_4 + "-" + sUUID_SEQ_5);

			@SuppressWarnings("unused")
			Player statsPlayer = Bukkit.getPlayer(UUID.fromString(sUUID));
			RetPlayerPoints rpp = null;

			if (PluginLoader.getInstance().getConfig().getString("settings.global.statistics.type")
					.equalsIgnoreCase("yaml")) {
//				rpp = YAMLStats.getPlayerStatistics(sUUID);
				rpp = RuntimeRPPManager.getRPPForPlayer(sUUID);
			}

			if (PluginLoader.getInstance().getConfig().getString("settings.global.statistics.type")
					.equalsIgnoreCase("mySQL")) {
				// Bukkit.broadcastMessage(sUUID);
//				rpp = MySQLStats.getPlayerStatistics(sUUID,
//						PluginLoader.getMySqlConnector());
				rpp = RuntimeRPPManager.getRPPForPlayer(sUUID);
			}


			if (rpp != null) {
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + "Showing the stats of " + name + ":");
				PluginLoader.getInstance();

				player.sendMessage(PluginLoader.getMessages().KNIFE_KILLS + rpp.getKnifeKills());
				player.sendMessage(PluginLoader.getMessages().EXPLOSION_KILLS + rpp.getExplosionKills());
				player.sendMessage(PluginLoader.getMessages().ARROW_KILLS + rpp.getDirectArrowKills());
				player.sendMessage(PluginLoader.getMessages().AXE_KILLS + rpp.getAxeKills());
				player.sendMessage("---------------");
				player.sendMessage(PluginLoader.getMessages().KNIFE_DEATHS + rpp.getKnifeDeaths());
				player.sendMessage(PluginLoader.getMessages().EXPLOSION_DEATHS + rpp.getExplosionDeaths());
				player.sendMessage(PluginLoader.getMessages().ARROW_DEATHS + rpp.getDirectArrowDeaths());
				player.sendMessage(PluginLoader.getMessages().AXE_DEATHS + rpp.getAxeDeaths());
				player.sendMessage("---------------");
				player.sendMessage(PluginLoader.getMessages().KILLS + rpp.getKills());
				player.sendMessage(PluginLoader.getMessages().DEATHS + rpp.getDeaths());
				player.sendMessage(PluginLoader.getMessages().KD + rpp.getKD());
				player.sendMessage("---------------");
				player.sendMessage(PluginLoader.getMessages().GAMES + rpp.getGames());
				player.sendMessage(PluginLoader.getMessages().WINS + rpp.getWins());
				player.sendMessage("---------------");
				player.sendMessage(PluginLoader.getMessages().SCORE + rpp.getPoints());
				player.sendMessage(PluginLoader.getMessages().RANK + Integer.toString(rpp.getRank()));
			} else {
				String message = ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().NOT_PLAYED_YET);
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + message);
			}
		}

		/*
		 * public HttpResponse http(String url, String body) {
		 * 
		 * try (CloseableHttpClient httpClient =
		 * HttpClientBuilder.create().build()) { HttpPost request = new
		 * HttpPost(url); StringEntity params = new StringEntity(body);
		 * request.addHeader("content-type", "application/json");
		 * request.setEntity(params); HttpResponse result =
		 * httpClient.execute(request); String json =
		 * EntityUtils.toString(result.getEntity(), "UTF-8");
		 * 
		 * com.google.gson.Gson gson = new com.google.gson.Gson(); UUIDStrings
		 * data = gson.fromJson(json, UUIDStrings.class);
		 * Bukkit.broadcastMessage(data.id + data.name + data.legacy);
		 * this.sUUID = data.id;
		 * 
		 * } catch (IOException ex) { } return null; }
		 */
		public void https() {

			try {
				URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
				URLConnection con = url.openConnection();
				InputStream in = con.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF8"));
				try {
					com.google.gson.Gson gson = new com.google.gson.Gson();
					UUIDStrings data = gson.fromJson(reader, UUIDStrings.class);
					if ((data.id != null) && (data.name != null)) {
						// Bukkit.broadcastMessage(data.id + data.name);
						this.sUUID = data.id;
					} else
						return;					
				} catch (NullPointerException i) {
					this.sUUID = null;
				}


			} catch (Exception i) {
				i.printStackTrace();
			}
		}
	}
}
