package org.kwstudios.play.ragemode.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RageKnife {
	
	public static ItemStack getRageKnife(){
		ItemStack knife = new ItemStack(Material.SHEARS);
		ItemMeta meta = knife.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "RageKnife");
		return knife;
		//TODO set attack level to 20 for the RageKnife
	}

}
