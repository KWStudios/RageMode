package org.kwstudios.play.ragemode.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.holo.HoloHolder;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class HoloStats {

	private Player player;
	@SuppressWarnings("unused")
	private String label;
	@SuppressWarnings("unused")
	private String[] args;
	@SuppressWarnings("unused")
	private FileConfiguration fileConfiguration;

	public HoloStats(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		this.player = player;
		this.label = label;
		this.args = args;
		this.fileConfiguration = fileConfiguration;
		if(PluginLoader.getHolographicDisplaysAvailable()){
			if(args.length >= 2) {
				if(args[1].equalsIgnoreCase("add"))
					addHolo();
				if(args[1].equalsIgnoreCase("remove"))
					removeHolo();
			}
			else
				player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().MISSING_ARGUMENTS.replace("$USAGE$", "/rm holo <add/remove>")));			
		}
		else
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + ChatColor.translateAlternateColorCodes('§', PluginLoader.getMessages().MISSING_DEPENDENCIES.replace("$DEPENDENCIE$", "HolographicDisplays & ProtocolLib")));
	}


	private void addHolo() {
		if(PluginLoader.getHolographicDisplaysAvailable())
			HoloHolder.addHolo(player.getLocation());
	}
	
	private void removeHolo() {
		if(PluginLoader.getHolographicDisplaysAvailable())
			HoloHolder.deleteHologram(HoloHolder.getClosest(player));
	}	
}
