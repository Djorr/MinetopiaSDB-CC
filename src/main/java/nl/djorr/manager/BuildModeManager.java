package nl.djorr.manager;

import lombok.RequiredArgsConstructor;
import nl.djorr.model.BuildModePlayer;
import nl.djorr.MinetopiaSDBCC;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import java.util.*;

@RequiredArgsConstructor
public class BuildModeManager {
    private final MinetopiaSDBCC plugin;
    private final Map<UUID, BuildModePlayer> buildModePlayers = new HashMap<>();

    public boolean isInBuildMode(Player player) {
        return buildModePlayers.containsKey(player.getUniqueId());
    }

    public Optional<BuildModePlayer> getBuildModePlayer(Player player) {
        return Optional.ofNullable(buildModePlayers.get(player.getUniqueId()));
    }

    public void addPlayerToBuildMode(Player player) {
        buildModePlayers.put(player.getUniqueId(), new BuildModePlayer(player));
    }

    public void removePlayerFromBuildMode(Player player) {
        buildModePlayers.remove(player.getUniqueId());
    }

    public Set<UUID> getBuildModeUUIDs() {
        return buildModePlayers.keySet();
    }

    public void restoreAllOnlinePlayers() {
        for (UUID uuid : new HashSet<>(buildModePlayers.keySet())) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline() && buildModePlayers.get(uuid) != null) {
                buildModePlayers.get(uuid).restore(player);
                player.sendMessage(MinetopiaSDBCC.getPrefix() + "§eBuildmode is automatisch uitgeschakeld vanwege een server restart of reload.");
            }
            buildModePlayers.remove(uuid);
        }
        buildModePlayers.clear();
    }
} 