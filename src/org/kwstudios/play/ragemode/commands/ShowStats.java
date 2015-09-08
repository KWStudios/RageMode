package org.kwstudios.play.ragemode.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.scores.RetPlayerPoints;
import org.kwstudios.play.ragemode.statistics.MySQLStats;
import org.kwstudios.play.ragemode.statistics.MySQLThread;
import org.kwstudios.play.ragemode.statistics.YAMLStats;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;
import org.kwstudios.play.ragemode.toolbox.OfflineUUIDer;

public class ShowStats {
	public ShowStats(Player player, String label, String[] args, FileConfiguration fileConfiguration) {
		if(args.length < 2) {
			constructMessage(player, player.getName());
		}
		else {
			constructMessage(player, args[1]);
		}
	}

	private void constructMessage(Player player, String playerName) {
		OfflineUUIDer ou = new OfflineUUIDer(playerName);
		RetPlayerPoints rpp = null;
		
		if(PluginLoader.getInstance().getConfig().getString("settings.global.statistics.type").equals("yaml")) {
			rpp = MySQLStats.getPlayerStatistics(Bukkit.getPlayer(UUID.fromString(ou.sUUID)), PluginLoader.getMySqlConnector());
		}
		
		if(PluginLoader.getInstance().getConfig().getString("settings.global.statistics.type").equals("mySQL")) {
			rpp = YAMLStats.getPlayerStatistics(ou.sUUID);
		}
		
		if(rpp != null) {
			player.sendMessage(ConstantHolder.RAGEMODE_PREFIX + "Showing the stats of " + playerName + ":");
			player.sendMessage("Deaths: " + rpp.getDeaths());
			player.sendMessage("Kills:  " + rpp.getKills());
			player.sendMessage("KD:     " + rpp.getKD());
			player.sendMessage("---------------");
			player.sendMessage("Games:   " + rpp.getGames());
			player.sendMessage("Wins:    " + rpp.getWins());
			player.sendMessage("---------------");
			player.sendMessage("Score:    " + rpp.getPoints());				
			player.sendMessage("Rank:     " + "Ranker™ hasn't been added jet :(");					
		}

	}
}
