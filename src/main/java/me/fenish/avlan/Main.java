package me.fenish.avlan;

import me.fenish.avlan.commands.MainCommand;
import me.fenish.avlan.events.HuntEvents;
import me.fenish.avlan.events.MainGuiClick;
import me.fenish.avlan.events.SubGuiClick;
import me.fenish.avlan.utils.BossBar;
import me.fenish.avlan.utils.MainUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main instance;
    public static Main getInstance() {
        return instance;
    }
    public static MainUtils util;
    public static BossBar bossbar;
    @Override
    public void onEnable() {
        instance = this;
        util = new MainUtils();
        bossbar = new BossBar();
        this.getCommand("avlan").setExecutor(new MainCommand());
        this.getServer().getPluginManager().registerEvents(new MainGuiClick(), this);
        this.getServer().getPluginManager().registerEvents(new SubGuiClick(), this);
        this.getServer().getPluginManager().registerEvents(new HuntEvents(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
