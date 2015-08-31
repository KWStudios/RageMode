package org.kwstudios.play.ragemode.statistics;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.kwstudios.play.ragemode.gameLogic.PlayerPoints;
import org.kwstudios.play.ragemode.gameLogic.RetPlayerPoints;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;

public class YAMLStats {
	
	private static boolean inited = false;
	private static File yamlStatsFile;
	private static FileConfiguration statsConfiguration;
	
	protected static boolean working = false;
	
	
	
	public static void initS() {
		if(inited)
			return;
		else
			inited = true;
		
		File file = new File(PluginLoader.getInstance().getDataFolder(), "stats.yml");
		YamlConfiguration config = null;
		yamlStatsFile = file;
		
        if(!file.exists()) {
			file.getParentFile().mkdirs();
			
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
            config = new YamlConfiguration();
            config.createSection("data");
            
            try {
				config.save(file);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
        } else {
            config = YamlConfiguration.loadConfiguration(file);
        }
        
        statsConfiguration = config; 
        try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void addPlayerStatistics(List<PlayerPoints> pP) {
		if(!inited)
			return;
		
		int i = 0;
		int imax = pP.size();
		while(i < imax) {
			Set<String> contents = ConfigFactory.getKeysUnderPath("data", false, statsConfiguration);
			if(contents.contains(pP.get(i).getPlayerUUID())) {
				ConfigFactory.setString("data." + pP.get(i).getPlayerUUID(), "name", Bukkit.getPlayer(UUID.fromString(pP.get(i).getPlayerUUID())).getName(), statsConfiguration);
				
				int kills = ConfigFactory.getInt("data." + pP.get(i).getPlayerUUID(), "kills", statsConfiguration);
				int axeKills = ConfigFactory.getInt("data." + pP.get(i).getPlayerUUID(), "axe_kills", statsConfiguration);
				int directArrowKills = ConfigFactory.getInt("data." + pP.get(i).getPlayerUUID(), "direct_arrow_kills", statsConfiguration);
				int explosionKills = ConfigFactory.getInt("data." + pP.get(i).getPlayerUUID(), "explosion_kills", statsConfiguration);
				int knifeKills = ConfigFactory.getInt("data." + pP.get(i).getPlayerUUID(), "knife_kills", statsConfiguration);
				
				int deaths = ConfigFactory.getInt("data." + pP.get(i).getPlayerUUID(), "kills", statsConfiguration);
				int axeDeaths = ConfigFactory.getInt("data." + pP.get(i).getPlayerUUID(), "axe_deaths", statsConfiguration);
				int directArrowDeaths = ConfigFactory.getInt("data." + pP.get(i).getPlayerUUID(), "direct_arrow_deaths", statsConfiguration);
				int explosionDeaths = ConfigFactory.getInt("data." + pP.get(i).getPlayerUUID(), "explosion_deaths", statsConfiguration);
				int knifeDeaths = ConfigFactory.getInt("data." + pP.get(i).getPlayerUUID(), "knife_deaths", statsConfiguration);
				
				int wins = ConfigFactory.getInt("data." + pP.get(i).getPlayerUUID(), "wins", statsConfiguration);
				int score = ConfigFactory.getInt("data." + pP.get(i).getPlayerUUID(), "score", statsConfiguration);
				int games = ConfigFactory.getInt("data." + pP.get(i).getPlayerUUID(), "games", statsConfiguration);
				
				
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "kills", (kills + pP.get(i).getKills()), statsConfiguration);
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "axe_kills", (axeKills + pP.get(i).getAxeKills()), statsConfiguration);
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "direct_arrow_kills", (directArrowKills + pP.get(i).getDirectArrowKills()), statsConfiguration);
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "explosion_kills", (explosionKills + pP.get(i).getExplosionKills()), statsConfiguration);
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "knife_kills", (knifeKills + pP.get(i).getKnifeKills()), statsConfiguration);
				
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "deaths", (deaths + pP.get(i).getDeaths()), statsConfiguration);
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "axe_deaths", (axeDeaths + pP.get(i).getAxeDeaths()), statsConfiguration);
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "direct_arrow_deaths",(directArrowDeaths + pP.get(i).getDirectArrowDeaths()), statsConfiguration);
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "explosion_deaths", (explosionDeaths + pP.get(i).getExplosionDeaths()), statsConfiguration);
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "knife_deaths", (knifeDeaths + pP.get(i).getKnifeDeaths()), statsConfiguration);
				
				if(pP.get(i).isWinner())
					ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "wins", (wins + 1), statsConfiguration);
				else
					ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "wins", wins, statsConfiguration);
				
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "score", (score + pP.get(i).getPoints()), statsConfiguration);
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "games", (games + 1), statsConfiguration);
				if((deaths + pP.get(i).getDeaths()) != 0)
					ConfigFactory.setDouble("data." + pP.get(i).getPlayerUUID(), "KD", ((kills + pP.get(i).getKills())/(deaths + pP.get(i).getDeaths())), statsConfiguration);	
				else
					ConfigFactory.setDouble("data." + pP.get(i).getPlayerUUID(), "KD", 1.0d, statsConfiguration);

			}
			else {
				ConfigFactory.setString("data", pP.get(i).getPlayerUUID(), "", statsConfiguration);
				
				ConfigFactory.setString("data." + pP.get(i).getPlayerUUID(), "name", Bukkit.getPlayer(UUID.fromString(pP.get(i).getPlayerUUID())).getName(), statsConfiguration);
	
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "kills", pP.get(i).getKills(), statsConfiguration);
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "axe_kills", pP.get(i).getAxeKills(), statsConfiguration);
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "direct_arrow_kills", pP.get(i).getDirectArrowKills(), statsConfiguration);
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "explosion_kills", pP.get(i).getExplosionKills(), statsConfiguration);
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "knife_kills", pP.get(i).getKnifeKills(), statsConfiguration);
				
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "deaths", pP.get(i).getDeaths(), statsConfiguration);
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "axe_deaths", pP.get(i).getAxeDeaths(), statsConfiguration);
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "direct_arrow_deaths", pP.get(i).getDirectArrowDeaths(), statsConfiguration);
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "explosion_deaths", pP.get(i).getExplosionDeaths(), statsConfiguration);
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "knife_deaths", pP.get(i).getKnifeDeaths(), statsConfiguration);
				
				if(pP.get(i).isWinner())
					ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "wins", 1, statsConfiguration);
				else
					ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "wins", 0, statsConfiguration);
				
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "score", pP.get(i).getPoints(), statsConfiguration);
				ConfigFactory.setInt("data." + pP.get(i).getPlayerUUID(), "games", 1, statsConfiguration);
				if(pP.get(i).getDeaths() != 0) 
					ConfigFactory.setDouble("data." + pP.get(i).getPlayerUUID(), "KD", pP.get(i).getKills()/pP.get(i).getDeaths(), statsConfiguration);
				else
					ConfigFactory.setDouble("data." + pP.get(i).getPlayerUUID(), "KD", 1.0d, statsConfiguration);
			}			
			
			i++;
		}
		
		try {
			statsConfiguration.save(yamlStatsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static RetPlayerPoints getPlayerStatistics(String sUUID) { //returns a RetPlayerPoints object containing the GLOBAL statistics of a player
		if(!inited)
			return null;
		
		RetPlayerPoints plPo = new RetPlayerPoints(sUUID);
		
		if(ConfigFactory.getKeysUnderPath("data", false, statsConfiguration).contains(sUUID)) {
			plPo.setKills(ConfigFactory.getInt("data." + sUUID, "kills", statsConfiguration));
			plPo.setAxeKills(ConfigFactory.getInt("data." + sUUID, "axe_kills", statsConfiguration));
			plPo.setDirectArrowKills(ConfigFactory.getInt("data." + sUUID, "direct_arrow_kills", statsConfiguration));
			plPo.setExplosionKills(ConfigFactory.getInt("data." + sUUID, "explosion_kills", statsConfiguration));
			plPo.setKnifeKills(ConfigFactory.getInt("data." + sUUID, "knife_kills", statsConfiguration));
			
			plPo.setDeaths(ConfigFactory.getInt("data." + sUUID, "kills", statsConfiguration));
			plPo.setAxeDeaths(ConfigFactory.getInt("data." + sUUID, "axe_deaths", statsConfiguration));
			plPo.setDirectArrowDeaths(ConfigFactory.getInt("data." + sUUID, "direct_arrow_deaths", statsConfiguration));
			plPo.setExplosionDeaths(ConfigFactory.getInt("data." + sUUID, "explosion_deaths", statsConfiguration));
			plPo.setKnifeDeaths(ConfigFactory.getInt("data." + sUUID, "knife_deaths", statsConfiguration));
			
			plPo.setWins(ConfigFactory.getInt("data." + sUUID, "wins", statsConfiguration));
			plPo.setPoints(ConfigFactory.getInt("data." + sUUID, "score", statsConfiguration));
			plPo.setGames(ConfigFactory.getInt("data." + sUUID, "games", statsConfiguration));
			
			ConfigFactory.getDouble("data." + sUUID, "KD", statsConfiguration);
		}
		return plPo;
	}
	
	private static class AddToPlayersStats implements Runnable{
		private List<PlayerPoints> lsUUIDs = null;
		
		public AddToPlayersStats(List<PlayerPoints> ls) {
			super();
			this.lsUUIDs = ls;
		}

		@Override
		public void run() {
			while(working) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			working = true;
			addPlayerStatistics(lsUUIDs);			
			working = false;
		}
	}
	
	public static AddToPlayersStats createPlayersStats(List<PlayerPoints> ls) {
		return new AddToPlayersStats(ls);
	}
}
