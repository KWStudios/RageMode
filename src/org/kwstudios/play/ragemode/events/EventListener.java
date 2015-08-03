package org.kwstudios.play.ragemode.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
import org.kwstudios.play.ragemode.loader.PluginLoader;

public class EventListener implements Listener{
	
    public EventListener(PluginLoader plugin) {
    	plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		Bukkit.broadcastMessage("Ragemode noticed that " + player.getName() + " disconnected.");
		
		if(PlayerList.isPlayerPlaying(event.getPlayer().getUniqueId().toString())) {
			if(PlayerList.removePlayer(player)) {
				Bukkit.broadcastMessage(player.getName() + " was removed successfully.");
			}
			
		}
	}
	
//	@EventHandler
//	public void onPlayerDeath(PlayerDeathEvent event) {
//		event.getEntity().getLastDamageCause().
//		
//	}
}
