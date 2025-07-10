package nl.djorr.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class BuildModePlayer {
    private final String name;
    private final GameMode previousGameMode;
    private final ItemStack[] previousInventory;
    private final ItemStack[] previousArmor;

    public BuildModePlayer(Player player) {
        this.name = player.getName();
        this.previousGameMode = player.getGameMode();
        this.previousInventory = player.getInventory().getContents();
        this.previousArmor = player.getInventory().getArmorContents();
    }

    public void restore(Player player) {
        player.getInventory().setContents(previousInventory);
        player.getInventory().setArmorContents(previousArmor);
        player.setGameMode(previousGameMode);
    }
} 