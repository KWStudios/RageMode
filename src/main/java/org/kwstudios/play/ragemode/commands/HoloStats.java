package org.kwstudios.play.ragemode.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class HoloStats {

	private Player player;
	@SuppressWarnings("unused")
	private String label;
	private String[] args;
	private FileConfiguration fileConfiguration;

	public HoloStats(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		this.player = player;
		this.label = label;
		this.args = args;
		this.fileConfiguration = fileConfiguration;
		if(args.length >= 2) {
			if(args[1].equals("add"))
				addHolo();
			if(args[1].equals("remove"))
				removeHolo();
		}
	}


	private void addHolo() {
		//TODO add a hologram to the file and add it to the server
	}
	
	private void removeHolo() {
		//TODO get the right hologram and remove it frome the file and from the server
	}	

	/*private void addLobbyToConfig() { //nur zum abschauen ;)
		if (args.length >= 2) {
			String gameName = args[1];
			if (!fileConfiguration.isSet(GAMES_PATH + "." + gameName)) {
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
						PluginLoader.getMessages().NOT_SET_YET.replace("$USAGE$", "/rm add <GameName> <MaxPlayers>")));
				return;
			} else {
				String path = GAMES_PATH + "." + gameName + "." + "lobby";
				ConfigFactory.setString(path, "world", player.getWorld().getName(), fileConfiguration);
				ConfigFactory.setDouble(path, "x", player.getLocation().getX(), fileConfiguration);
				ConfigFactory.setDouble(path, "y", player.getLocation().getY(), fileConfiguration);
				ConfigFactory.setDouble(path, "z", player.getLocation().getZ(), fileConfiguration);
				ConfigFactory.setDouble(path, "yaw", player.getLocation().getYaw(), fileConfiguration);
				ConfigFactory.setDouble(path, "pitch", player.getLocation().getPitch(), fileConfiguration);
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
						PluginLoader.getMessages().LOBBY_SUCCESSFULLY.replace("$GAME$", gameName)));

			}
		} else {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§',
					PluginLoader.getMessages().MISSING_ARGUMENTS.replace("$USAGE$", "/rm lobby <GameName>")));
		}
	}*/

}
