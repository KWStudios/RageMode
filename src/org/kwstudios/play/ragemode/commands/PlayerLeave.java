package org.kwstudios.play.ragemode.commands;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;

public class PlayerLeave {
	
	private Player player;
	@SuppressWarnings("unused")
	private String label;
	@SuppressWarnings("unused")
	private String[] args;
	@SuppressWarnings("unused")
	private FileConfiguration fileConfiguration;
	
	public PlayerLeave(Player player, String label, String[] args, FileConfiguration fileConfiguration){
		this.player = player;
		this.label = label;
		this.args = args;
		this.fileConfiguration = fileConfiguration;
		doPlayerLeave();
	}
	
	private void doPlayerLeave(){
		PlayerList.removePlayer(player);
	}

}
