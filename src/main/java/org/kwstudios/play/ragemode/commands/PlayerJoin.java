package org.kwstudios.play.ragemode.commands;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.signs.SignCreator;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;
import org.kwstudios.play.ragemode.toolbox.GameBroadcast;
import org.kwstudios.play.ragemode.toolbox.MapChecker;

public class PlayerJoin {

	private Player player;
	@SuppressWarnings("unused")
	private String label;
	private String[] args;
	private FileConfiguration fileConfiguration;

	public PlayerJoin(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		this.player = player;
		this.label = label;
		this.args = args;
		this.fileConfiguration = fileConfiguration;
		doPlayerJoin();
	}

	private void doPlayerJoin() {
		if (args.length < 2) {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
					PluginLoader.getMessages().MISSING_ARGUMENTS.replace("$USAGE$", "/rm join <GameName>")));
			return;
		}
		MapChecker mapChecker = new MapChecker(args[1], fileConfiguration);
		if (mapChecker.isValid()) {
			String world = ConfigFactory.getString(ConstantHolder.GAME_PATH + "." + args[1] + ".lobby", "world", fileConfiguration);
			double lobbyX = ConfigFactory.getDouble(ConstantHolder.GAME_PATH + "." + args[1] + ".lobby", "x", fileConfiguration);
			double lobbyY = ConfigFactory.getDouble(ConstantHolder.GAME_PATH + "." + args[1] + ".lobby", "y", fileConfiguration);
			double lobbyZ = ConfigFactory.getDouble(ConstantHolder.GAME_PATH + "." + args[1] + ".lobby", "z", fileConfiguration);
			double lobbyYaw = ConfigFactory.getDouble(ConstantHolder.GAME_PATH + "." + args[1] + ".lobby", "yaw", fileConfiguration);
			double lobbyPitch = ConfigFactory.getDouble(ConstantHolder.GAME_PATH + "." + args[1] + ".lobby", "pitch", fileConfiguration);

			Location lobbyLocation = new Location(Bukkit.getWorld(world), lobbyX, lobbyY, lobbyZ);
			lobbyLocation.setYaw((float) lobbyYaw);
			lobbyLocation.setPitch((float) lobbyPitch);

			Location playerLocation = player.getLocation();
			ItemStack[] playerInventory = player.getInventory().getContents();
			ItemStack[] playerArmor = player.getInventory().getArmorContents();
			Double playerHealth = player.getHealth();
			int playerHunger = player.getFoodLevel();
			GameMode playerGameMode = player.getGameMode();

			Logger logger = Logger.getLogger("Minecraft");

			if (PlayerList.addPlayer(player, args[1], fileConfiguration)) {
				PlayerList.oldLocations.addToBoth(player, playerLocation);
				PlayerList.oldInventories.addToBoth(player, playerInventory);
				PlayerList.oldArmor.addToBoth(player, playerArmor);
				PlayerList.oldHealth.addToBoth(player, playerHealth);
				PlayerList.oldHunger.addToBoth(player, playerHunger);
				PlayerList.oldGameMode.addToBoth(player, playerGameMode);
				player.getInventory().clear();
				player.teleport(lobbyLocation);
				player.setGameMode(GameMode.SURVIVAL);
				player.setHealth(20);
				player.setFoodLevel(20);
				player.getInventory().setHelmet(null);
				player.getInventory().setChestplate(null);
				player.getInventory().setLeggings(null);
				player.getInventory().setBoots(null);
				String message = ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
						PluginLoader.getMessages().PLAYER_JOINED.replace("$PLAYER$", player.getName()));
				GameBroadcast.broadcastToGame(args[1], message);
				logger.info(message);
				SignCreator.updateAllSigns(args[1]);
			} else {
				logger.info(ConstantHolder.RAGEMODE_PREFIX
						+ ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().PLAYER_COULD_NOT_JOIN
								.replace("$PLAYER$", player.getName()).replace("$GAME$", args[1])));
			}

		} else {
			player.sendMessage(mapChecker.getMessage());
		}
	}

}
