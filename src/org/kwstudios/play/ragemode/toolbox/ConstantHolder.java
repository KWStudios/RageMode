package org.kwstudios.play.ragemode.toolbox;

import org.bukkit.ChatColor;

public class ConstantHolder {

	public static final String RAGEMODE_PREFIX = ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "["
			+ ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "RageMode" + ChatColor.AQUA.toString()
			+ ChatColor.BOLD.toString() + "] " + ChatColor.RESET;
	
	public static final String GAME_PATH = "settings.games";
	
	public static final int MINUS_POINTS_FOR_AXE_DEATH = 50;
	public static final int POINTS_FOR_AXE_KILL = 30;
	public static final int POINTS_FOR_BOW_KILL = 25;
	public static final int POINTS_FOR_EXPLOSION_KILL = 25;
	public static final int POINTS_FOR_KNIFE_KILL = 15;
	
	//TODO dynamic points getter from config file (with default values)

}
