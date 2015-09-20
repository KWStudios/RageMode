package org.kwstudios.play.ragemode.holo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;

public class HoloHolder {
	private static File yamlHolosFile;
	private static FileConfiguration holosConfiguration;

	public static void addHolo(Location loc) {
		List<Location> savedHolos;
		if(holosConfiguration.isSet("data.holos")) {
			if(holosConfiguration.getList("data.holos") != null) {
				savedHolos = (List<Location>) holosConfiguration.getList("data.holos");				
			}
			else {
				savedHolos = new ArrayList<Location>();
			}
		}
		else {
			savedHolos = new ArrayList<Location>();			
		}

//		Bukkit.getConsoleSender().sendMessage(ConstantHolder.RAGEMODE_PREFIX + "Some idiot tried to change the holos.yml");
		
		savedHolos.add(loc);
		holosConfiguration.set("data.holos", savedHolos);

		try {
			holosConfiguration.save(yamlHolosFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Collection<? extends Player> onlines = Bukkit.getOnlinePlayers();
		for(Player player : onlines){
		    displayHoloToPlayer(player, loc);
		}
	}
	
	public static void loadHolos() {
		Collection<Hologram> holos = HologramsAPI.getHolograms(PluginLoader.getInstance());
		for(Hologram holo : holos){
		    holo.delete();
		}
		Collection<? extends Player> onlines = Bukkit.getOnlinePlayers();
		for(Player player : onlines){
			showAllHolosToPlayer(player);
		}
	}
	
	public static void displayHoloToPlayer(Player player, Location loc) {
		loc.add(0, 2, 0);
		
		Hologram hologram  = HologramsAPI.createHologram(PluginLoader.getInstance(), loc);

		VisibilityManager visibilityManager = hologram.getVisibilityManager();
		visibilityManager.showTo(player);
		visibilityManager.setVisibleByDefault(false);
		
		hologram.appendTextLine("Das ist ein test!!!");
		hologram.appendTextLine("https://www.youtube.com/watch?v=MQNfied4s44");		
	}
	
	public static void deleteHoloObjectsOfPlayer(Player player) {
		Collection<Hologram> holos = HologramsAPI.getHolograms(PluginLoader.getInstance());
		for(Hologram holo : holos){
		    if(holo.getVisibilityManager().isVisibleTo(player))
		    	holo.delete();
		}
	}
	
	public static void deleteHologram(Hologram holo) {
		List<Location> locList = (List<Location>) holosConfiguration.getList("data.holos");
		locList.remove(holo.getLocation());
		holosConfiguration.set("data.holos", locList);
		holo.delete();
	}

	public static Hologram getClosest(Player player) {
		Collection<Hologram> holos = HologramsAPI.getHolograms(PluginLoader.getInstance());
		Hologram closest = null;
		double lowestDist = Double.MAX_VALUE;		
		
		for(Hologram holo : holos){
			double dist = holo.getLocation().distance(player.getLocation());
			if(dist < lowestDist) {
				lowestDist = dist;
				closest = holo;
			}
		}
		return closest;
	}
	
	public static void initHoloHolder() {
		File file = new File(PluginLoader.getInstance().getDataFolder(), "holos.yml");
		YamlConfiguration config = null;
		yamlHolosFile = file;
		
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
        
        holosConfiguration = config; 
        try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadHolos();
	}

	public static void showAllHolosToPlayer(Player player) {
		List<Location> holoList = (List<Location>) holosConfiguration.getList("data.holos");
		int i = 0;
		int imax = holoList.size();
		while(i < imax) {
			Hologram hologram  = HologramsAPI.createHologram(PluginLoader.getInstance(), holoList.get(i));

			VisibilityManager visibilityManager = hologram.getVisibilityManager();
			visibilityManager.showTo(player);
			visibilityManager.setVisibleByDefault(false);
			
			hologram.appendTextLine("Das ist ein test!!!");
			hologram.appendTextLine("https://www.youtube.com/watch?v=MQNfied4s44");
		}
		
	}
}
