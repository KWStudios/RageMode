package org.kwstudios.play.ragemode.loader;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.kwstudios.play.ragemode.commands.CommandParser;
import org.kwstudios.play.ragemode.commands.StopGame;
import org.kwstudios.play.ragemode.events.EventListener;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
import org.kwstudios.play.ragemode.tabsGuiListOverlay.TabAPI;
import org.mcstats.Metrics;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class PluginLoader extends JavaPlugin{
	
	private static PluginLoader instance = null;
	private ProtocolManager protocolManager;
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		PluginLoader.instance = this;
		
		protocolManager = ProtocolLibrary.getProtocolManager();		
		protocolManager.addPacketListener(
				  new PacketAdapter(this, ListenerPriority.NORMAL, 
				          PacketType.Play.Server.PLAYER_INFO) {
				    @Override
				    public void onPacketSending(PacketEvent event) {
				    	if(PlayerList.isPlayerPlaying(event.getPlayer().getUniqueId().toString())) {
				    		event.setCancelled(true);
				        }
				    	Bukkit.broadcastMessage(event.getPacket().getModifier().read(0).toString());
				    	Bukkit.broadcastMessage(event.getPacket().getModifier().read(1).toString());
//				    	Bukkit.broadcastMessage(event.getPacket().getModifier().read(2).toString());
//				    	Bukkit.broadcastMessage(event.getPacket().getModifier().read(3).toString());
				    }
				  }
		);
		new TabAPI(protocolManager);
		
		PluginDescriptionFile pluginDescriptionFile = getDescription();
		Logger logger = Logger.getLogger("Minecraft");
		
		logger.info(pluginDescriptionFile.getName() + " was loaded successfully! (Version: " + pluginDescriptionFile.getVersion() + ")" );
		//getConfig().options().copyDefaults(true);
		//saveConfig();
		new PlayerList(getConfig());
		new EventListener(this, getConfig());
		
	    try {
	        Metrics metrics = new Metrics(this);
	        metrics.start();
	    } catch (IOException e) {
	        // Failed to submit the stats :-(
	    }
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
