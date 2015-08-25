package org.kwstudios.play.ragemode.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CombatAxe {
	
	public static ItemStack getCombatAxe(){
		ItemStack axe = new ItemStack(Material.IRON_AXE);
		ItemMeta meta = axe.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "CombatAxe");
		axe.setItemMeta(meta);
		return axe;
	}

}
