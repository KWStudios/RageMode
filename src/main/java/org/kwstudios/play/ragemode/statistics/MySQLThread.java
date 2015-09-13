package org.kwstudios.play.ragemode.statistics;

import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.scores.PlayerPoints;

public class MySQLThread implements Runnable {

	PlayerPoints pP = null;
	
	public MySQLThread(PlayerPoints playerPoints) {
		this.pP = playerPoints;
	}
	
	@Override
	public void run() {
		MySQLStats.addPlayerStatistics(this.pP, PluginLoader.getMySqlConnector());	
	}
}
