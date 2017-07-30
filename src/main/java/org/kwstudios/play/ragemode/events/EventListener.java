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
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.kwstudios.play.ragemode.commands.PlayerJoin;
import org.kwstudios.play.ragemode.gameLogic.GameSpawnGetter;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
import org.kwstudios.play.ragemode.holo.HoloHolder;
import org.kwstudios.play.ragemode.items.CombatAxe;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.scores.RageScores;
import org.kwstudios.play.ragemode.signs.SignCreator;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;
import org.kwstudios.play.ragemode.toolbox.GameBroadcast;
import org.kwstudios.play.ragemode.toolbox.GetGames;
import org.kwstudios.play.ragemode.toolbox.MapChecker;

public class EventListener implements Listener {

	public static HashMap<String, Boolean> waitingGames = new HashMap<String, Boolean>();
	public FileConfiguration fileConfiguration = null;
	private Map<UUID, UUID> explosionVictims = new HashMap<UUID, UUID>(); // shot,
																			// shooter

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
		HoloHolder.deleteHoloObjectsOfPlayer(player);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		HoloHolder.showAllHolosToPlayer(player);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onProjectileHit(ProjectileHitEvent event) { // RageArrow
															// explosion event
		if (event.getEntity() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getEntity();
			if (arrow.getShooter() instanceof Player) {
				Player shooter = (Player) arrow.getShooter();
				if (PlayerList.isPlayerPlaying(shooter.getUniqueId().toString())) {
					if (waitingGames.containsKey(PlayerList.getPlayersGame(shooter))) {
						if (waitingGames.get(PlayerList.getPlayersGame(shooter))) {
							return;
						}
					}

					Location location = arrow.getLocation();
					World world = arrow.getWorld();
					double x = location.getX();
					double y = location.getY();
					double z = location.getZ();

					List<Entity> nears = arrow.getNearbyEntities(10, 10, 10);

					world.createExplosion(x, y, z, 2f, false, false); // original
																		// 4f
					arrow.remove();

					int i = 0;
					int imax = nears.size();
					while (i < imax) {
						if (nears.get(
								i) instanceof Player /*
														 * && !nears.get(i).
														 * getUniqueId().
														 * toString().equals(
														 * shooter.getUniqueId()
														 * .toString())
														 */) {
							Player near = (Player) nears.get(i);
							if (explosionVictims != null) {
								if (explosionVictims.containsKey(near.getUniqueId())) {
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
	public void onRageKnifeHit(EntityDamageByEntityEvent event) { // RageKnife
																	// hit event
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			Player killer = (Player) event.getDamager();
			Player victim = (Player) event.getEntity();
			if (PlayerList.isPlayerPlaying(killer.getUniqueId().toString())
					&& PlayerList.isPlayerPlaying(victim.getUniqueId().toString())) {
				if (waitingGames.containsKey(PlayerList.getPlayersGame(killer))) {
					if (waitingGames.get(PlayerList.getPlayersGame(killer))) {
						event.setCancelled(true);
						return;
					}
				}
				if (killer.getItemInHand() != null && killer.getItemInHand().getItemMeta() != null
						&& killer.getItemInHand().getItemMeta().getDisplayName() != null) {
					if (killer.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "RageKnife")) {
						// TODO check if "killer.getItemInHand() instanceof
						// MATERIAL.SHEARS" also works (maybe more stable)
						event.setDamage(25);
					}
				}
			}
			if (PlayerList.isPlayerPlaying(victim.getUniqueId().toString())) {
				if (!PlayerList.isGameRunning(PlayerList.getPlayersGame(victim))) {
					event.setDamage(0);
				}
			}
		}
		// TODO add Constant for "RageKnife" for unexpected error preventing
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onItemSpawn(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (PlayerList.isPlayerPlaying(player.getUniqueId().toString())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onArrowHitPlayer(EntityDamageEvent event) { // Arrow hit player
															// event
		if (event.getEntity() instanceof Player) {
			if (PlayerList.isPlayerPlaying(event.getEntity().getUniqueId().toString())) {
				if (event.getCause().equals(DamageCause.PROJECTILE)) {
					Player victim = (Player) event.getEntity();
					if (PlayerList.isPlayerPlaying(victim.getUniqueId().toString())) {
						if (waitingGames.containsKey(PlayerList.getPlayersGame(victim))) {
							if (waitingGames.get(PlayerList.getPlayersGame(victim))) {
								event.setDamage(0);
								event.setCancelled(true);
								return;
							}
						}
						if (event.getDamage() == 0.0d) {
							event.setDamage(28.34d);
						} else {
							event.setDamage(27.114);
						}
					}
				}
				if (event.getCause().equals(DamageCause.FALL)) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) { // Player autorespawn
		Player deceased;
		if (event.getEntity() instanceof Player && event.getEntity() != null) {
			deceased = (Player) event.getEntity();
		} else {
			return;
		}
		if (PlayerList.isPlayerPlaying(event.getEntity().getUniqueId().toString())) {
			if ((deceased.getKiller() != null
					&& PlayerList.isPlayerPlaying(deceased.getKiller().getUniqueId().toString()))
					|| deceased.getKiller() == null) {
				String game = PlayerList.getPlayersGame(deceased);

				if (!fileConfiguration.isSet("settings.global.deathmessages")) {
					ConfigFactory.setBoolean("settings.global", "deathmessages", false, fileConfiguration);
				}
				boolean doDeathBroadcast = ConfigFactory.getBoolean("settings.global", "deathmessages",
						fileConfiguration);

				if (fileConfiguration.isSet("settings." + PlayerList.getPlayersGame(deceased) + ".deathmessages")) {
					String gameBroadcast = ConfigFactory.getString(
							ConstantHolder.GAME_PATH + "." + PlayerList.getPlayersGame(deceased), "deathmessages",
							fileConfiguration);
					if (gameBroadcast != null && gameBroadcast != "") {
						if (gameBroadcast.equalsIgnoreCase("true") || gameBroadcast.equalsIgnoreCase("false")) {
							doDeathBroadcast = Boolean.parseBoolean(gameBroadcast);
						}
					}
				}

				if (deceased.getLastDamage() == 0.0f) {
					if (deceased.getKiller() == null) {
						if (doDeathBroadcast) {
							String message = ConstantHolder.RAGEMODE_PREFIX
									+ ChatColor.translateAlternateColorCodes('§',
											PluginLoader.getMessages().BROADCAST_AXE_KILL
													.replace("$VICTIM$", deceased.getName())
													.replace("$KILLER$", deceased.getName()));
							GameBroadcast.broadcastToGame(game, message);
						}
						RageScores.addPointsToPlayer(deceased, deceased, "combataxe");
					} else {
						if (doDeathBroadcast) {
							String message = ConstantHolder.RAGEMODE_PREFIX
									+ ChatColor.translateAlternateColorCodes('§',
											PluginLoader.getMessages().BROADCAST_AXE_KILL
													.replace("$VICTIM$", deceased.getName())
													.replace("$KILLER$", deceased.getKiller().getName()));
							GameBroadcast.broadcastToGame(game, message);
						}
						RageScores.addPointsToPlayer(deceased.getKiller(), deceased, "combataxe");
					}
				} else if (deceased.getLastDamageCause().getCause().equals(DamageCause.PROJECTILE)) {
					if (deceased.getKiller() == null) {
						if (doDeathBroadcast) {
							String message = ConstantHolder.RAGEMODE_PREFIX
									+ ChatColor.translateAlternateColorCodes('§',
											PluginLoader.getMessages().BROADCAST_ARROW_KILL
													.replace("$VICTIM$", deceased.getName())
													.replace("$KILLER$", deceased.getName()));
							GameBroadcast.broadcastToGame(game, message);
						}
						RageScores.addPointsToPlayer(deceased, deceased, "ragebow");
					} else {
						if (doDeathBroadcast) {
							String message = ConstantHolder.RAGEMODE_PREFIX
									+ ChatColor.translateAlternateColorCodes('§',
											PluginLoader.getMessages().BROADCAST_ARROW_KILL
													.replace("$VICTIM$", deceased.getName())
													.replace("$KILLER$", deceased.getKiller().getName()));
							GameBroadcast.broadcastToGame(game, message);
						}
						RageScores.addPointsToPlayer(deceased.getKiller(), deceased, "ragebow");
					}

				} else if (deceased.getLastDamageCause().getCause().equals(DamageCause.ENTITY_ATTACK)) {
					if (deceased.getKiller() == null) {
						if (doDeathBroadcast) {
							String message = ConstantHolder.RAGEMODE_PREFIX
									+ ChatColor.translateAlternateColorCodes('§',
											PluginLoader.getMessages().BROADCAST_KNIFE_KILL
													.replace("$VICTIM$", deceased.getName())
													.replace("$KILLER$", deceased.getName()));
							GameBroadcast.broadcastToGame(game, message);
						}
						RageScores.addPointsToPlayer(deceased, deceased, "rageknife");
					} else {
						if (doDeathBroadcast) {
							String message = ConstantHolder.RAGEMODE_PREFIX
									+ ChatColor.translateAlternateColorCodes('§',
											PluginLoader.getMessages().BROADCAST_KNIFE_KILL
													.replace("$VICTIM$", deceased.getName())
													.replace("$KILLER$", deceased.getKiller().getName()));
							GameBroadcast.broadcastToGame(game, message);
						}
						RageScores.addPointsToPlayer(deceased.getKiller(), deceased, "rageknife");
					}

				} else if (deceased.getLastDamageCause().getCause().equals(DamageCause.BLOCK_EXPLOSION)) {
					if (explosionVictims.containsKey(deceased.getUniqueId())) {
						if (doDeathBroadcast) {
							String message = ConstantHolder.RAGEMODE_PREFIX
									+ ChatColor.translateAlternateColorCodes('§',
											PluginLoader.getMessages().BROADCAST_EXPLOSION_KILL
													.replace("$VICTIM$", deceased.getName()).replace("$KILLER$",
															Bukkit.getPlayer(
																	explosionVictims.get(deceased.getUniqueId()))
															.getName()));
							GameBroadcast.broadcastToGame(game, message);
						}
						RageScores.addPointsToPlayer(Bukkit.getPlayer(explosionVictims.get(deceased.getUniqueId())),
								deceased, "explosion");
					} else {
						if (doDeathBroadcast) {
							String message = ConstantHolder.RAGEMODE_PREFIX + ChatColor
									.translateAlternateColorCodes('§', PluginLoader.getMessages().BROADCAST_ERROR_KILL);
							GameBroadcast.broadcastToGame(game, message);
						}
						String message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
								PluginLoader.getMessages().UNKNOWN_KILLER);
						deceased.sendMessage(message);
					}

				} else {
					if (doDeathBroadcast) {
						String message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
								PluginLoader.getMessages().UNKNOWN_WEAPON.replace("$VICTIM$", deceased.getName()));
						GameBroadcast.broadcastToGame(game, message);
					}
				}

				event.setDeathMessage("");
			}

			event.setKeepInventory(true);
			GameSpawnGetter gameSpawnGetter = new GameSpawnGetter(PlayerList.getPlayersGame(deceased),
					fileConfiguration);
			List<Location> spawns = gameSpawnGetter.getSpawnLocations();
			// Location[] aSpawns = (Location[]) spawns.toArray(); ---->
			// performance optimization

			Random rand = new Random();
			int x = rand.nextInt(spawns.size() - 1); // ----> performance
														// optimization

			deceased.setHealth(20);

			deceased.teleport(spawns.get(x)); // ----> performance optimization

			// deceased.getInventory().clear();
			// deceased.getInventory().setItem(0, RageBow.getRageBow()); //
			// deceased.getInventory().setItem(1, RageKnife.getRageKnife()); //
			// give him a new set of items
			// deceased.getInventory().setItem(9, RageArrow.getRageArrow()); //
			deceased.getInventory().setItem(2, CombatAxe.getCombatAxe());

		}
	}

	@EventHandler
	public void onHungerGain(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (PlayerList.isPlayerPlaying(player.getUniqueId().toString())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (PlayerList.isPlayerPlaying(event.getPlayer().getUniqueId().toString())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onSignBreak(BlockBreakEvent event) {
		if (event.getBlock().getState() instanceof Sign && !event.isCancelled()) {
			SignCreator.removeSign((Sign) event.getBlock().getState());
		}
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if (PlayerList.isPlayerPlaying(event.getPlayer().getUniqueId().toString())
				&& !event.getPlayer().hasPermission("ragemode.admin.cmd")) {
			if (event.getMessage() != null) {
				String cmd = event.getMessage().trim().toLowerCase();
				if (cmd.equals("/rm leave") || cmd.equals("/ragemode leave") || cmd.equals("/rm list")
						|| cmd.equals("/ragemode list") || cmd.equals("/rm stop") || cmd.equals("/ragemode stop")
						|| PluginLoader.getInGameCommands().contains(cmd)) {
					if (waitingGames.containsKey(PlayerList.getPlayersGame(event.getPlayer())))
						if (waitingGames.get(PlayerList.getPlayersGame(event.getPlayer())))
							event.setCancelled(true);
				} else {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onCombatAxeThrow(PlayerInteractEvent event) {
		if (PlayerList.isPlayerPlaying(event.getPlayer().getUniqueId().toString())) {
			Player thrower = event.getPlayer();
			if (waitingGames.containsKey(PlayerList.getPlayersGame(thrower))) {
				if (waitingGames.get(PlayerList.getPlayersGame(thrower))) {
					return;
				}
			}
			if (thrower.getItemInHand() != null && thrower.getItemInHand().getItemMeta() != null
					&& thrower.getItemInHand().getItemMeta().getDisplayName() != null) {
				if (thrower.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "CombatAxe")) {
					Snowball sb = thrower.launchProjectile(Snowball.class);
					thrower.getInventory().setItemInHand(null);
				}
			}
		}

		if (event.getClickedBlock() != null && event.getClickedBlock().getState() != null) {
			if (event.getClickedBlock().getState() instanceof Sign) {
				if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					Sign sign = (Sign) event.getClickedBlock().getState();
					if (SignCreator.isJoinSign(sign)) {
						Player player = event.getPlayer();
						if (player.hasPermission("ragemode.rm.join")) {
							String[] args = new String[2];
							args[0] = "join";
							args[1] = SignCreator.getGameFromSign(sign);
							new PlayerJoin(player, args[0], args, PluginLoader.getInstance().getConfig());
						} else {
							player.sendMessage(ChatColor.translateAlternateColorCodes('§',
									PluginLoader.getMessages().PERMISSION_MESSAGE));
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onInventoryInteractEvent(InventoryClickEvent event) {
		if (PlayerList.isPlayerPlaying(event.getWhoClicked().getUniqueId().toString())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemPickedUpEvent(PlayerPickupItemEvent event) {
		if (PlayerList.isPlayerPlaying(event.getPlayer().getUniqueId().toString())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		Sign sign = (Sign) event.getBlock().getState();

		if (event.getPlayer().hasPermission("ragemode.admin.signs")) {
			if (event.getLine(1).trim().equalsIgnoreCase("[rm]")
					|| event.getLine(1).trim().equalsIgnoreCase("[ragemode]")) {
				String[] allGames = GetGames.getGameNames(PluginLoader.getInstance().getConfig());
				for (String game : allGames) {
					if (event.getLine(2).trim().equalsIgnoreCase(game.trim())) {
						SignCreator.createNewSign(sign, game);
						SignCreator.updateSign(event);
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (PlayerList.isPlayerPlaying(event.getPlayer().getUniqueId().toString())) {
			if (EventListener.waitingGames != null) {
				if (EventListener.waitingGames.containsKey(PlayerList.getPlayersGame(event.getPlayer()))) {
					if (EventListener.waitingGames.get(PlayerList.getPlayersGame(event.getPlayer()))) {
						Location from = event.getFrom();
						Location to = event.getTo();
						double x = Math.floor(from.getX());
						double z = Math.floor(from.getZ());
						if (Math.floor(to.getX()) != x || Math.floor(to.getZ()) != z) {
							x += .5;
							z += .5;
							event.getPlayer().teleport(
									new Location(from.getWorld(), x, from.getY(), z, from.getYaw(), from.getPitch()));
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onWorldChangedEvent(PlayerTeleportEvent event) {
		if (PlayerList.isPlayerPlaying(event.getPlayer().getUniqueId().toString())) {
			if (!MapChecker.isGameWorld(PlayerList.getPlayersGame(event.getPlayer()), event.getTo().getWorld())) {
				if (!event.getPlayer().hasMetadata("Leaving")) {
					event.getPlayer().performCommand("rm leave");
				} else {
					event.getPlayer().removeMetadata("Leaving", PluginLoader.getInstance());
				}
			}
		}
	}
}
