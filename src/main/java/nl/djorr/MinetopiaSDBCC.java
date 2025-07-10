package nl.djorr;

import lombok.Getter;
import nl.djorr.command.BuildModeCommand;
import nl.djorr.listener.BuildModeListener;
import nl.djorr.manager.BuildModeManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class MinetopiaSDBCC extends JavaPlugin {
    private static MinetopiaSDBCC instance;
    private BuildModeManager buildModeManager;

    @Override
    public void onEnable() {
        instance = this;
        this.buildModeManager = new BuildModeManager(this);
        getCommand("buildmode").setExecutor(new BuildModeCommand(buildModeManager));
        getServer().getPluginManager().registerEvents(new BuildModeListener(buildModeManager), this);
        getLogger().info("BuildMode plugin enabled!");
    }

    @Override
    public void onDisable() {
        if (buildModeManager != null) {
            buildModeManager.restoreAllOnlinePlayers();
        }
        getLogger().info("BuildMode plugin disabled!");
    }

    public static MinetopiaSDBCC getInstance() {
        return instance;
    }

    public static String getPrefix() {
        return "§8[§bMinetopia§3SDB-CC§8] §7";
    }
} 