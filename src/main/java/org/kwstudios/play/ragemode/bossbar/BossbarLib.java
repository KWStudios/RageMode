package org.kwstudios.play.ragemode.bossbar;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.kwstudios.play.ragemode.bossbar.handler.BossbarHandler;
import org.kwstudios.play.ragemode.bossbar.handler.WitherBossbarHandler;

public final class BossbarLib extends JavaPlugin {

    private static Plugin instance;
    private static BossbarHandler handler;

    public static Plugin getPluginInstance() {
        return instance;
    }

    public static void setPluginInstance(Plugin instance) {
        if (BossbarLib.instance != null) return;
        BossbarLib.instance = instance;
        setHandler(new WitherBossbarHandler());
    }

    public static BossbarHandler getHandler() {
        return handler;
    }

    public static void setHandler(BossbarHandler handler) {
        BossbarLib.handler = handler;
    }

    @Override
    public void onEnable() {
        setPluginInstance(this);
    }

}
