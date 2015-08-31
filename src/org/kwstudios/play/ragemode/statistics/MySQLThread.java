package org.kwstudios.play.ragemode.statistics;

import org.kwstudios.play.ragemode.gameLogic.PlayerPoints;
import org.kwstudios.play.ragemode.loader.PluginLoader;

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
