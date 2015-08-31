package org.kwstudios.play.ragemode.statistics;

import java.io.File;
import java.io.IOException;
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
	
	
	
	public static void initS() {
		if(inited)
			return;
		else
			inited = true;
		
		File file = new File(PluginLoader.getInstance().getDataFolder() + "stats.yml");
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
	}
	
	public static void addPlayerStatistics(PlayerPoints pP) {
		Set<String> contents = ConfigFactory.getKeysUnderPath("data", false, statsConfiguration);
		if(contents.contains(pP.getPlayerUUID())) {
			ConfigFactory.setString("data." + pP.getPlayerUUID(), "name", Bukkit.getPlayer(UUID.fromString(pP.getPlayerUUID())).getName(), statsConfiguration);
			
			int kills = ConfigFactory.getInt("data." + pP.getPlayerUUID(), "kills", statsConfiguration);
			int axeKills = ConfigFactory.getInt("data." + pP.getPlayerUUID(), "axe_kills", statsConfiguration);
			int directArrowKills = ConfigFactory.getInt("data." + pP.getPlayerUUID(), "direct_arrow_kills", statsConfiguration);
			int explosionKills = ConfigFactory.getInt("data." + pP.getPlayerUUID(), "explosion_kills", statsConfiguration);
			int knifeKills = ConfigFactory.getInt("data." + pP.getPlayerUUID(), "knife_kills", statsConfiguration);
			
			int deaths = ConfigFactory.getInt("data." + pP.getPlayerUUID(), "kills", statsConfiguration);
			int axeDeaths = ConfigFactory.getInt("data." + pP.getPlayerUUID(), "axe_deaths", statsConfiguration);
			int directArrowDeaths = ConfigFactory.getInt("data." + pP.getPlayerUUID(), "direct_arrow_deaths", statsConfiguration);
			int explosionDeaths = ConfigFactory.getInt("data." + pP.getPlayerUUID(), "explosion_deaths", statsConfiguration);
			int knifeDeaths = ConfigFactory.getInt("data." + pP.getPlayerUUID(), "knife_deaths", statsConfiguration);
			
			int wins = ConfigFactory.getInt("data." + pP.getPlayerUUID(), "wins", statsConfiguration);
			int score = ConfigFactory.getInt("data." + pP.getPlayerUUID(), "score", statsConfiguration);
			int games = ConfigFactory.getInt("data." + pP.getPlayerUUID(), "games", statsConfiguration);
			
			
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "kills", (kills + pP.getKills()), statsConfiguration);
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "axe_kills", (axeKills + pP.getAxeKills()), statsConfiguration);
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "direct_arrow_kills", (directArrowKills + pP.getDirectArrowKills()), statsConfiguration);
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "explosion_kills", (explosionKills + pP.getExplosionKills()), statsConfiguration);
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "knife_kills", (knifeKills + pP.getKnifeKills()), statsConfiguration);
			
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "deaths", (deaths + pP.getDeaths()), statsConfiguration);
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "axe_deaths", (axeDeaths + pP.getAxeDeaths()), statsConfiguration);
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "direct_arrow_deaths",(directArrowDeaths + pP.getDirectArrowDeaths()), statsConfiguration);
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "explosion_deaths", (explosionDeaths + pP.getExplosionDeaths()), statsConfiguration);
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "knife_deaths", (knifeDeaths + pP.getKnifeDeaths()), statsConfiguration);
			
			if(pP.isWinner())
				ConfigFactory.setInt("data." + pP.getPlayerUUID(), "wins", (wins + 1), statsConfiguration);
			else
				ConfigFactory.setInt("data." + pP.getPlayerUUID(), "wins", wins, statsConfiguration);
			
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "score", (score + pP.getPoints()), statsConfiguration);
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "games", (games + 1), statsConfiguration);
			ConfigFactory.setDouble("data." + pP.getPlayerUUID(), "KD", ((kills + pP.getKills())/(deaths + pP.getDeaths())), statsConfiguration);
		}
		else {
			ConfigFactory.setString("data", pP.getPlayerUUID(), "", statsConfiguration);
			
			ConfigFactory.setString("data." + pP.getPlayerUUID(), "name", Bukkit.getPlayer(UUID.fromString(pP.getPlayerUUID())).getName(), statsConfiguration);

			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "kills", pP.getKills(), statsConfiguration);
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "axe_kills", pP.getAxeKills(), statsConfiguration);
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "direct_arrow_kills", pP.getDirectArrowKills(), statsConfiguration);
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "explosion_kills", pP.getExplosionKills(), statsConfiguration);
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "knife_kills", pP.getKnifeKills(), statsConfiguration);
			
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "deaths", pP.getDeaths(), statsConfiguration);
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "axe_deaths", pP.getAxeDeaths(), statsConfiguration);
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "direct_arrow_deaths", pP.getDirectArrowDeaths(), statsConfiguration);
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "explosion_deaths", pP.getExplosionDeaths(), statsConfiguration);
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "knife_deaths", pP.getKnifeDeaths(), statsConfiguration);
			
			if(pP.isWinner())
				ConfigFactory.setInt("data." + pP.getPlayerUUID(), "wins", 1, statsConfiguration);
			else
				ConfigFactory.setInt("data." + pP.getPlayerUUID(), "wins", 0, statsConfiguration);
			
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "score", pP.getPoints(), statsConfiguration);
			ConfigFactory.setInt("data." + pP.getPlayerUUID(), "games", 1, statsConfiguration);
			ConfigFactory.setDouble("data." + pP.getPlayerUUID(), "KD", pP.getKills()/pP.getDeaths(), statsConfiguration);
		}
		
		try {
			statsConfiguration.save(yamlStatsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static PlayerPoints getPlayerStats(String sUUID) { //returns a RetPlayerPoints object containing the GLOBAL statistics of a player
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
		}
		
		return plPo;
	}	
}
