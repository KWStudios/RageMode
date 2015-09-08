package org.kwstudios.play.ragemode.signs;

import java.io.File;
import java.util.Set;

import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;

public class SignCreator {

	public static boolean createNewSign(Sign sign, String game) {
		File file = SignConfiguration.getYamlSignsFile();
		FileConfiguration fileConfiguration = SignConfiguration.getSignConfiguration();

		Set<String> signs = ConfigFactory.getKeysUnderPath("signs", false, fileConfiguration);
		if (signs.contains(Integer.toString(sign.getX()) + Integer.toString(sign.getY()) + Integer.toString(sign.getZ())
				+ sign.getWorld().getName())) {
			return false;
		}
		return true;
	}

}
