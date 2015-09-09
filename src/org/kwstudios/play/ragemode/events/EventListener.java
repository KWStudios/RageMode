package org.kwstudios.play.ragemode.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.kwstudios.play.ragemode.commands.PlayerJoin;
import org.kwstudios.play.ragemode.gameLogic.GameSpawnGetter;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
import org.kwstudios.play.ragemode.items.CombatAxe;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.scores.RageScores;
import org.kwstudios.play.ragemode.signs.SignCreator;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;
import org.kwstudios.play.ragemode.toolbox.GameBroadcast;
import org.kwstudios.play.ragemode.toolbox.GetGames;



public class EventListener implements Listener {

	public FileConfiguration fileConfiguration = null;
	private Map<UUID, UUID> explosionVictims = new HashMap<UUID, UUID>();  //shot, shooter
	
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

	@EventHandler(priority = EventPriority.LOWEST)
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
					
					List<Entity> nears = arrow.getNearbyEntities(10, 10, 10);
					
					world.createExplosion(x, y, z, 2f, false, false); //original 4f
					arrow.remove();	
					
					
					int i = 0;
					int imax = nears.size();
					while(i < imax) {
						if(nears.get(i) instanceof Player /*&& !nears.get(i).getUniqueId().toString().equals(shooter.getUniqueId().toString())*/) {
							Player near = (Player) nears.get(i);
							if(explosionVictims != null) {
								if(explosionVictims.containsKey(near.getUniqueId())) {
									explosionVictims.remove(near.getUniqueId());
									explosionVictims.put(near.getUniqueId(), shooter.getUniqueId());
								}								
							}
							explosionVictims.put(near.getUniqueId(), shooter.getUniqueId());						
						}
						i++;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onRageKnifeHit(EntityDamageByEntityEvent event){    //RageKnife hit event
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
			Player killer = (Player) event.getDamager();
			Player victim = (Player) event.getEntity();
			if(PlayerList.isPlayerPlaying(killer.getUniqueId().toString()) && PlayerList.isPlayerPlaying(victim.getUniqueId().toString())){
				if(killer.getItemInHand() != null && killer.getItemInHand().getItemMeta() != null && killer.getItemInHand().getItemMeta().getDisplayName() != null) {
					if(killer.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "RageKnife")){
						//TODO check if "killer.getItemInHand() instanceof MATERIAL.SHEARS" also works (maybe more stable)
						event.setDamage(25);
					}					
				}
			}
			if(PlayerList.isPlayerPlaying(victim.getUniqueId().toString())) {
				if(!PlayerList.isGameRunning(PlayerList.getPlayersGame(victim))) {
					event.setDamage(0);
				}
			}
		}
		//TODO add Constant for "RageKnife" for unexpected error preventing
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onItemSpawn(PlayerDropItemEvent event){
		Player player = event.getPlayer();
		if(PlayerList.isPlayerPlaying(player.getUniqueId().toString())){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onArrowHitPlayer(EntityDamageEvent event){    //Arrow hit player event
		if(event.getEntity() instanceof Player) {
			if(PlayerList.isPlayerPlaying(event.getEntity().getUniqueId().toString())) {
				if(event.getCause().equals(DamageCause.PROJECTILE)) {
					Player victim = (Player) event.getEntity();
					if(PlayerList.isPlayerPlaying(victim.getUniqueId().toString())){
						if(event.getDamage() == 0.0d) {
							event.setDamage(28.34d);
						}
						else {
							event.setDamage(27.114);
						}
					}
				}
				if(event.getCause().equals(DamageCause.FALL)) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {		//Player autorespawn
		Player deceased;
		if(event.getEntity() instanceof Player && event.getEntity() != null){
			 deceased = (Player) event.getEntity();
		}
		else{
			return;
		}
		if(PlayerList.isPlayerPlaying(event.getEntity().getUniqueId().toString())) {
			if((deceased.getKiller() != null && PlayerList.isPlayerPlaying(deceased.getKiller().getUniqueId().toString())) || deceased.getKiller() == null) {
				String game = PlayerList.getPlayersGame(deceased);
				
				if(!fileConfiguration.isSet("setting.global.deathmessages")) {
					ConfigFactory.setBoolean("settings.global", "deathmessages", false, fileConfiguration);			
				}
				boolean doDeathBroadcast = ConfigFactory.getBoolean("settings.global", "deathmessages", fileConfiguration);
				if(deceased.getLastDamage() == 0.0f) {
					if(deceased.getKiller() == null) {
						if(doDeathBroadcast)
							GameBroadcast.broadcastToGame(game, ConstantHolder.RAGEMODE_PREFIX + ChatColor.GREEN + deceased.getName() + ChatColor.DARK_AQUA + " was killed by " + ChatColor.GREEN + deceased.getName() + ChatColor.DARK_AQUA + " with a" + ChatColor.GOLD + "CombatAxe" + ChatColor.DARK_AQUA + ".");
						RageScores.addPointsToPlayer(deceased, deceased, "combataxe");					
					}
					else {
						if(doDeathBroadcast)
							GameBroadcast.broadcastToGame(game, ConstantHolder.RAGEMODE_PREFIX + ChatColor.GREEN + deceased.getName() + ChatColor.DARK_AQUA + " was killed by " + ChatColor.GREEN + deceased.getKiller().getName() + ChatColor.DARK_AQUA + " with a " + ChatColor.GOLD + "CombatAxe" + ChatColor.DARK_AQUA + ".");
						RageScores.addPointsToPlayer(deceased.getKiller(), deceased, "combataxe");					
					}
				}
				else if(deceased.getLastDamageCause().getCause().equals(DamageCause.PROJECTILE)) {
					if(deceased.getKiller() == null) {
						if(doDeathBroadcast)
							GameBroadcast.broadcastToGame(game, ConstantHolder.RAGEMODE_PREFIX + ChatColor.GREEN  + deceased.getName() + ChatColor.DARK_AQUA + " was killed by a direct " + ChatColor.GOLD + "arrow" + ChatColor.DARK_AQUA + " hit from " + ChatColor.GREEN + deceased.getName() + ChatColor.DARK_AQUA + ".");	
						RageScores.addPointsToPlayer(deceased, deceased, "ragebow");				
					}
					else {
						if(doDeathBroadcast)
							GameBroadcast.broadcastToGame(game, ConstantHolder.RAGEMODE_PREFIX + ChatColor.GREEN  + deceased.getName() + ChatColor.DARK_AQUA + " was killed by a direct " + ChatColor.GOLD + "arrow" + ChatColor.DARK_AQUA + " hit from " + ChatColor.GREEN + deceased.getKiller().getName() + ChatColor.DARK_AQUA + ".");	
						RageScores.addPointsToPlayer(deceased.getKiller(), deceased, "ragebow");					
					}
	
				}
				else if(deceased.getLastDamageCause().getCause().equals(DamageCause.ENTITY_ATTACK)) {
					if(deceased.getKiller() == null) {
						if(doDeathBroadcast)
							GameBroadcast.broadcastToGame(game, ConstantHolder.RAGEMODE_PREFIX + ChatColor.GREEN  + deceased.getName() + ChatColor.DARK_AQUA + " was killed by " + ChatColor.GOLD + deceased.getName() + ChatColor.DARK_AQUA + " with a " + ChatColor.GOLD + "RageKnife" + ChatColor.DARK_AQUA + ".");	
						RageScores.addPointsToPlayer(deceased, deceased, "rageknife");					
					}
					else {
						if(doDeathBroadcast)
							GameBroadcast.broadcastToGame(game, ConstantHolder.RAGEMODE_PREFIX + ChatColor.GREEN  + deceased.getName() + ChatColor.DARK_AQUA + " was killed by " + ChatColor.GOLD + deceased.getKiller().getName() + ChatColor.DARK_AQUA + " with a " + ChatColor.GOLD + "RageKnife" + ChatColor.DARK_AQUA + ".");	
						RageScores.addPointsToPlayer(deceased.getKiller(), deceased, "rageknife");					
					}
	
				}
				else if(deceased.getLastDamageCause().getCause().equals(DamageCause.BLOCK_EXPLOSION)) {
					if(explosionVictims.containsKey(deceased.getUniqueId())) {
						if(doDeathBroadcast)
							GameBroadcast.broadcastToGame(game, ConstantHolder.RAGEMODE_PREFIX + ChatColor.GREEN  + deceased.getName() + ChatColor.DARK_AQUA + " was " + ChatColor.GOLD + "blown up" + ChatColor.DARK_AQUA + " by " + ChatColor.GREEN + Bukkit.getPlayer(explosionVictims.get(deceased.getUniqueId())).getName() + ChatColor.DARK_AQUA + ".");	
						RageScores.addPointsToPlayer(Bukkit.getPlayer(explosionVictims.get(deceased.getUniqueId())), deceased, "explosion");	
					}
					else {
						if(doDeathBroadcast)
							GameBroadcast.broadcastToGame(game, ConstantHolder.RAGEMODE_PREFIX + "Whoops, that shouldn't happen normally...");	
						deceased.sendMessage(ConstantHolder.RAGEMODE_PREFIX + "Do you know who killed you? Because we don't know it...");
					}
	
				}
				else {
					if(doDeathBroadcast)
						GameBroadcast.broadcastToGame(game, ConstantHolder.RAGEMODE_PREFIX + ChatColor.GREEN  + deceased.getName() + ChatColor.DARK_AQUA + " was killed by something unexpected.");	
				}
				
				event.setDeathMessage("");
			}
				
			event.setKeepInventory(true);
			GameSpawnGetter gameSpawnGetter = new GameSpawnGetter(PlayerList.getPlayersGame(deceased), fileConfiguration);				
			List<Location> spawns = gameSpawnGetter.getSpawnLocations();
			//Location[] aSpawns = (Location[]) spawns.toArray();    ----> performance optimization
				
			Random rand = new Random();
			int x = rand.nextInt(spawns.size() - 1);    //----> performance optimization
	
			deceased.setHealth(20);
				
			deceased.teleport(spawns.get(x));    //----> performance optimization
				
			//deceased.getInventory().clear();
			//deceased.getInventory().setItem(0, RageBow.getRageBow());		//
			//deceased.getInventory().setItem(1, RageKnife.getRageKnife());	//	give him a new set of items
			//deceased.getInventory().setItem(9, RageArrow.getRageArrow());	//
			deceased.getInventory().setItem(2, CombatAxe.getCombatAxe());

		}
	}
	
	@EventHandler
	public void onHungerGain(FoodLevelChangeEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(PlayerList.isPlayerPlaying(player.getUniqueId().toString())) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(PlayerList.isPlayerPlaying(event.getPlayer().getUniqueId().toString())) {
			event.setCancelled(true);
		}
		
		if(event.getBlock().getState() instanceof Sign){
			SignCreator.removeSign((Sign)event.getBlock().getState());
		}
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if(PlayerList.isPlayerPlaying(event.getPlayer().getUniqueId().toString()) && !event.getPlayer().hasPermission("ragemode.admin.cmd")) {
			if(event.getMessage() != null) {
				String cmd = event.getMessage().toLowerCase();
				if(cmd.equals("/rm leave") || cmd.equals("/ragemode leave") || cmd.equals("/rm list") || cmd.equals("/ragemode list")|| cmd.equals("/rm stop") || cmd.equals("/ragemode stop") || cmd.equals("/l") || cmd.equals("/lobby") || cmd.equals("/spawn")) {
				}
				else {
					event.setCancelled(true);				
				}				
			}
		}
	}
	
	@EventHandler
	public void onCombatAxeThrow(PlayerInteractEvent event) {
		if(PlayerList.isPlayerPlaying(event.getPlayer().getUniqueId().toString())) {
			Player thrower = event.getPlayer();
			if(thrower.getItemInHand() != null && thrower.getItemInHand().getItemMeta() != null && thrower.getItemInHand().getItemMeta().getDisplayName() != null) {
				if(thrower.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "CombatAxe")) {
					thrower.launchProjectile(Snowball.class);
					thrower.getInventory().setItemInHand(null);
				}
			}
		}
		
		if(event.getClickedBlock().getState() instanceof Sign){
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
				Sign sign = (Sign) event.getClickedBlock().getState();
				if(SignCreator.isJoinSign(sign)){
					Player player = event.getPlayer();
					if (player.hasPermission("ragemode.rm.join")) {
						String[] args = new String[2];
						args[0] = "join";
						args[1] = SignCreator.getGameFromSign(sign);
						new PlayerJoin(player, args[0], args, PluginLoader.getInstance().getConfig());
					} else{
						player.sendMessage(ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PERMISSION_MESSAGE));
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryInteractEvent(InventoryClickEvent event){
		if(PlayerList.isPlayerPlaying(event.getWhoClicked().getUniqueId().toString())){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onItemPickedUpEvent(PlayerPickupItemEvent event){
		if(PlayerList.isPlayerPlaying(event.getPlayer().getUniqueId().toString())){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event){
		Sign sign = (Sign)event.getBlock().getState();
		
		if(event.getLine(1).trim().equalsIgnoreCase("[rm]") || event.getLine(1).trim().equalsIgnoreCase("[ragemode]")){
			String[] allGames = GetGames.getGameNames(PluginLoader.getInstance().getConfig());
			for(String game : allGames){
				if(event.getLine(2).trim().equalsIgnoreCase(game.trim())){
					SignCreator.createNewSign(sign, game);
					SignCreator.updateSign(event);
				}
			}
		}
	}
}
