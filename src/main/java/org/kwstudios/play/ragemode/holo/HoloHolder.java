package org.kwstudios.play.ragemode.holo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.loader.PluginLoader;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

public class HoloHolder {
	private static List<Location> existingHolos = new ArrayList<Location>();

	public static List<Location> getExistingHolos() {
		return existingHolos;
	}

	public static void addHolo(Location loc) {
		//TODO add th holo to the file
		HoloHolder.existingHolos.add(loc);
		Collection<? extends Player> onlines = Bukkit.getOnlinePlayers();
		for(Player player : onlines){
		    displayHoloToPlayer(player, loc);
		}
	}
	
	public static void loadHolos() {
		//TODO get all the holos from the holofile and put them into the existingHolos List
		//     (reset existing holos first to maker sure no manually removed holos stay in the list when reloading)
		//     this is supposed to be called in onEnable and reload
		existingHolos.clear();
	}
	
	public static void displayHoloToPlayer(Player player, Location loc) {
		//TODO create a hologram and show is to the specified player only
		//     this is supposed to be called when a player joins the server
		Hologram hologram  = HologramsAPI.createHologram(PluginLoader.getInstance(), loc);

		
		VisibilityManager visibilityManager = hologram.getVisibilityManager();
		visibilityManager.showTo(player);
		visibilityManager.setVisibleByDefault(false);
		
		TextLine textLine1 = hologram.appendTextLine("Das ist ein test!!!");
		TextLine textLine2 = hologram.appendTextLine("https://www.youtube.com/watch?v=MQNfied4s44");		
	}
	
	public static boolean deleteHologram(Hologram holo) {
		//TODO delete the hologram and remove it from the List
		//     remove holo from the file
		return false;
	}

	public static Hologram getClosest(Player player) {
		int i = 0;
		int imax = existingHolos.size();
		
		while(i < imax) {
			player.getLocation().distance(existingHolos.get(i));
			
			i++;
		}
		
		return null;
	}
}
