package org.kwstudios.play.ragemode.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.locale.Messages;
import org.kwstudios.play.ragemode.scores.RetPlayerPoints;
import org.kwstudios.play.ragemode.statistics.MySQLStats;
import org.kwstudios.play.ragemode.statistics.YAMLStats;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

import com.google.gson.Gson;

public class ShowStats {
	public ShowStats(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		if(args.length < 2) {
			constructMessage(player, player.getName());
		}
		else {
			constructMessage(player, args[1]);
		}
	}

	private void constructMessage(Player player, String playerName) {		
		Thread thread = new Thread(new uuiderThread(playerName));
		thread.start();
	}

	private class UUIDStrings {
		public String id;
		public String name;
		public boolean legacy;
	}
	
	private class uuiderThread implements Runnable {
		private String name;
		private String sUUID;
		public uuiderThread(String name) {
			this.name = name;
		}
		@Override
		public void run() {
		//	http("https://api.mojang.com/users/profiles/minecraft/" + name, "");
			https();
			
			if(sUUID == null)
				return;
			
			CharSequence sUUID_SEQ_1 = sUUID.subSequence(0, 8);
			CharSequence sUUID_SEQ_2 = sUUID.subSequence(8, 12);
			CharSequence sUUID_SEQ_3 = sUUID.subSequence(12, 16);
			CharSequence sUUID_SEQ_4 = sUUID.subSequence(16, 20);
			CharSequence sUUID_SEQ_5 = sUUID.subSequence(20, 32);
			sUUID = new String(sUUID_SEQ_1 + "-" + sUUID_SEQ_2 + "-" + sUUID_SEQ_3 + "-" + sUUID_SEQ_4 + "-" + sUUID_SEQ_5);
			
			Player player = Bukkit.getPlayer(UUID.fromString(sUUID));
			RetPlayerPoints rpp = null;
			
			if(PluginLoader.getInstance().getConfig().getString("settings.global.statistics.type").equalsIgnoreCase("yaml")) {
				rpp = YAMLStats.getPlayerStatistics(sUUID);
			}
			
			if(PluginLoader.getInstance().getConfig().getString("settings.global.statistics.type").equalsIgnoreCase("mySQL")) {
//				Bukkit.broadcastMessage(sUUID);
				rpp = MySQLStats.getPlayerStatistics(Bukkit.getPlayer(UUID.fromString(sUUID)), PluginLoader.getMySqlConnector());
			}
			
			if(rpp != null) {
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + "Showing the stats of " + name + ":");
				player.sendMessage("Deaths:  " + rpp.getDeaths());
				player.sendMessage("Kills:   " + rpp.getKills());
				player.sendMessage("KD:      " + rpp.getKD());
				player.sendMessage("---------------");
				player.sendMessage("Games:   " + rpp.getGames());
				player.sendMessage("Wins:    " + rpp.getWins());
				player.sendMessage("---------------");
				player.sendMessage("Score:   " + rpp.getPoints());				
				player.sendMessage("Rank:    " + "Ranker™ hasn't been added jet :(");					
			}
			else
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + "That player hasn't played on this server yet.");
		}
		
/*		   public HttpResponse http(String url, String body) {
		
		        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
		            HttpPost request = new HttpPost(url);
		            StringEntity params = new StringEntity(body);
		            request.addHeader("content-type", "application/json");
		            request.setEntity(params);
		            HttpResponse result = httpClient.execute(request);
		            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
		
		            com.google.gson.Gson gson = new com.google.gson.Gson();
		            UUIDStrings data = gson.fromJson(json, UUIDStrings.class);
		            Bukkit.broadcastMessage(data.id + data.name + data.legacy);
		            this.sUUID = data.id;
		
		        } catch (IOException ex) {
		        }
		        return null;
    		}*/	
		public void https() {
						
			try {
			URL	url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF8"));
			
            com.google.gson.Gson gson = new com.google.gson.Gson();
            UUIDStrings data = gson.fromJson(reader, UUIDStrings.class);
            if((data.id != null) && (data.name != null)) {
//            	Bukkit.broadcastMessage(data.id + data.name);            	
            	this.sUUID = data.id;
            }
            else
            	return;
            
			} catch (Exception i) {
				i.printStackTrace();
			}			
		}
	}
}
