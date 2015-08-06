package org.kwstudios.play.ragemode.events;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.kwstudios.play.ragemode.gameLogic.GameSpawnGetter;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
import org.kwstudios.play.ragemode.loader.PluginLoader;

public class EventListener implements Listener {

	public FileConfiguration fileConfiguration = null;
	
	public EventListener(PluginLoader plugin, FileConfiguration fileconfiguration) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		fileConfiguration = fileconfiguration;
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		// Bukkit.broadcastMessage("Ragemode noticed that " + player.getName() +
		// " disconnected.");

		if (PlayerList.isPlayerPlaying(event.getPlayer().getUniqueId().toString())) {
			if (PlayerList.removePlayer(player)) {
				// Bukkit.broadcastMessage(player.getName() + " was removed
				// successfully.");
			}

		}
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {    //RageArrow explosion event
		if (event.getEntity() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getEntity();
			if (arrow.getShooter() instanceof Player) {
				Player shooter = (Player) arrow.getShooter();
				if (PlayerList.isPlayerPlaying(shooter.getUniqueId().toString())) {
					Location location = arrow.getLocation();
					World world = arrow.getWorld();
					double x = location.getX();
					double y = location.getY();
					double z = location.getZ();

					world.createExplosion(x, y, z, 4f, false, false);
					//TODO check if 4f is too strong (4f is TNT strength)
				}
			}
		}
	}
	
	@EventHandler
	public void onRageKnifeHit(EntityDamageByEntityEvent event){    //RageKnife hit event
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
			Player killer = (Player) event.getDamager();
			Player victim = (Player) event.getEntity();
			Bukkit.broadcastMessage("AAA");
			if(PlayerList.isPlayerPlaying(killer.getUniqueId().toString()) && PlayerList.isPlayerPlaying(victim.getUniqueId().toString())){
				Bukkit.broadcastMessage("BBB");
				if(killer.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "RageKnife")){
					//TODO check if "killer.getItemInHand() instanceof MATERIAL.SHEARS" also works (maybe more stable)
					Bukkit.broadcastMessage("CCC");
					event.setDamage(25);
				}
			}
		}
		//TODO add Constant for "RageKnife" for unexpected error preventing
	}
	
	@EventHandler
	public void onArrowHitPlayer(EntityDamageEvent event){    //Arrow hit player event
		if(event.getEntity() instanceof Player && event.getCause().equals(DamageCause.PROJECTILE)){
			Bukkit.broadcastMessage("HIT");
			Player victim = (Player) event.getEntity();
			if(PlayerList.isPlayerPlaying(victim.getUniqueId().toString())){
				Bukkit.broadcastMessage("DDD");
					event.setDamage(25);
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if(PlayerList.isPlayerPlaying(event.getEntity().getUniqueId().toString())) {
			Player deceased = (Player) event.getEntity();
			deceased.getInventory().clear();
			
			GameSpawnGetter gameSpawnGetter = new GameSpawnGetter(PlayerList.getPlayersGame(deceased), fileConfiguration);
			
			List<Location> spawns = gameSpawnGetter.getSpawnLocations();
			//Location[] aSpawns = (Location[]) spawns.toArray();    ----> performance optimization
			
			Random rand = new Random();
			int x = rand.nextInt(spawns.size() - 1);    //----> performance optimization

			deceased.teleport(spawns.get(x));    //----> performance optimization
			deceased.setHealth(20);
		}
	}
	

	// @EventHandler
	// public void onPlayerDeath(PlayerDeathEvent event) {
	// event.getEntity().getLastDamageCause().
	//
	// }
}
