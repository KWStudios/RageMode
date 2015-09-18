package org.kwstudios.play.ragemode.holo;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

public class HoloHolder {
	private static List<Location> existingHolos = new ArrayList<Location>();

	public static List<Location> getExistingHolos() {
		return existingHolos;
	}

	public static void addHolo(Location loc) {
		HoloHolder.existingHolos.add(loc);
		//TODO loop through all online players and do displayHoloForPlayer(player) for them
	}
	
	public static void loadHolos() {
		//TODO get all the holos from the holofile and put them into the existingHolos List
		//     (reset existing holos first to maker sure no manually remover holos stay in the list when reloading)
		//     this is supposed to be called in onEnable and reload
	}
	
	public static boolean displayHoloToPlayer(Player player) {
		//TODO create a hologram and show is to the specified player only
		//     this is supposed to be called when a player joins the server		
		return false;
	}
	
	public static boolean deleteHologram(Hologram holo) {
		//TODO delete the hologram and remove it from the List
		//     removing it from the file will be done in the command
		return false;
	}
}
