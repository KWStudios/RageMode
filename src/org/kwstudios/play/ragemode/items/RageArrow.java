package org.kwstudios.play.ragemode.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RageArrow {
	
	public static ItemStack getRageArrow(){
		ItemStack arrow = new ItemStack(Material.ARROW);
		ItemMeta meta = arrow.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "RageArrow");
		arrow.setItemMeta(meta);
		return arrow;
	}

}
