package org.kwstudios.play.ragemode.toolbox;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.kwstudios.play.ragemode.statistics.MySQLThread;


public class OfflineUUIDer {
	public String sUUID;
	public String name;
	
	
	public OfflineUUIDer(String name) {
		this.name = name;
		Thread thread = new Thread(new uuiderThread());
		thread.start();
		
	}
	
	private class UUIDStrings {
		public String id;
		public String name;
		public boolean legacy;
	}
	
	private class uuiderThread implements Runnable {
		@Override
		public void run() {
			http("https://api.mojang.com/users/profiles/minecraft/" + name, "");
		}
	}
	
   public HttpResponse http(String url, String body) {

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
    }

}
