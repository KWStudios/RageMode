package org.kwstudios.play.ragemode.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RageBow {
	
	public static ItemStack getRageBow(){
		ItemStack bow = new ItemStack(Material.BOW);
		ItemMeta meta = bow.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "RageBow");
		Enchantment infinity = Enchantment.ARROW_INFINITE;
		meta.addEnchant(infinity, 1, false);
		bow.setItemMeta(meta);
		return bow;
	}
	
}
