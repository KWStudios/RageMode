package org.kwstudios.play.ragemode.loader;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.kwstudios.play.ragemode.commands.CommandParser;
import org.kwstudios.play.ragemode.commands.StopGame;
import org.kwstudios.play.ragemode.events.EventListener;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;

public class PluginLoader extends JavaPlugin{
	
	private static PluginLoader instance = null;
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		PluginLoader.instance = this;
		
		PluginDescriptionFile pluginDescriptionFile = getDescription();
		Logger logger = Logger.getLogger("Minecraft");
		
		logger.info(pluginDescriptionFile.getName() + " was loaded successfully! (Version: " + pluginDescriptionFile.getVersion() + ")" );
		//getConfig().options().copyDefaults(true);
		//saveConfig();
		new PlayerList(getConfig());
		new EventListener(this, getConfig());
	}

	@Override
	public void onDisable() {
		super.onDisable();
		PluginDescriptionFile pluginDescriptionFile = getDescription();
		Logger logger = Logger.getLogger("Minecraft");
		
		StopGame.stopAllGames(getConfig(), logger);
		
		logger.info(pluginDescriptionFile.getName() + " was unloaded successfully! (Version: " + pluginDescriptionFile.getVersion() + ")" );
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("You must be a Player!");
			return false;
		}
		
		Player player = (Player) sender;
		
		CommandParser commandParser = new CommandParser(player, command, label, args, getConfig());
		
		if(!commandParser.isCommand()){
			return false;
		}
		
		saveConfig();
		
		return true;
	}
	
	public static PluginLoader getInstance(){
		return PluginLoader.instance;
	}
	
}
