package nl.djorr.listener;

import lombok.RequiredArgsConstructor;
import nl.djorr.manager.BuildModeManager;
import nl.djorr.MinetopiaSDBCC;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class BuildModeListener implements Listener {
    private final BuildModeManager manager;

    private boolean isInBuildMode(Player player) {
        return manager.isInBuildMode(player) && player.getGameMode() == GameMode.CREATIVE;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (isInBuildMode(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        if (isInBuildMode(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onPickupNew(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player && isInBuildMode((Player) e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player && isInBuildMode((Player) e.getWhoClicked())) {
            // Alleen toestaan als het de eigen player-inventory is
            if (e.getClickedInventory() == null || !e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        if (isInBuildMode(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent e) {
        if (isInBuildMode(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onArmorStand(PlayerArmorStandManipulateEvent e) {
        if (isInBuildMode(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e) {
        if (e.getPlayer() != null && isInBuildMode(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (isInBuildMode(e.getPlayer())) e.setCancelled(false);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && isInBuildMode((Player) e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getTarget() instanceof Player && isInBuildMode((Player) e.getTarget())) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player && isInBuildMode((Player) e.getEntity())) e.getDrops().clear();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e) {
        if (isInBuildMode(e.getPlayer())) e.setCancelled(false);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getPlayer() instanceof Player && isInBuildMode((Player) e.getPlayer())) {
            // Alleen toestaan als het de eigen inventory is
            if (!e.getInventory().equals(((Player) e.getPlayer()).getInventory())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (isInBuildMode(player)) {
            manager.getBuildModePlayer(player).ifPresent(bmp -> bmp.restore(player));
            player.setGameMode(GameMode.SURVIVAL);
        }
        manager.removePlayerFromBuildMode(player);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (isInBuildMode(player)) {
            manager.getBuildModePlayer(player).ifPresent(bmp -> bmp.restore(player));
            player.setGameMode(GameMode.SURVIVAL);
            manager.removePlayerFromBuildMode(player);
        }
    }
} 