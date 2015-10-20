package org.kwstudios.play.ragemode.achievements;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kwstudios.play.ragemode.achievements.Achievement.AchievementReason;
import org.kwstudios.play.ragemode.events.KillEvent;
import org.kwstudios.play.ragemode.loader.PluginLoader;

public class AchievementHandler implements Listener {

	private Set<Achievement> achievements;

	public AchievementHandler(Set<Achievement> achievements) {
		this.achievements = achievements;
		Bukkit.getServer().getPluginManager().registerEvents(this, PluginLoader.getInstance());
	}

	@EventHandler
	public void onKill(KillEvent event) {
		for (Achievement achievement : achievements) {
			if (achievement.getReason() == AchievementReason.FIRST_KILL) {
				// TODO Add Code responsible for the first kill achievement
			}

			if (achievement.getReason() == AchievementReason.FIRST_BLOOD) {
				// TODO Add Code responsible for the first blood achievement
			}
		}
	}

}
