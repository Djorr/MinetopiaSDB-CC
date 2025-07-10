package nl.djorr.command;

import lombok.RequiredArgsConstructor;
import nl.djorr.manager.BuildModeManager;
import nl.djorr.MinetopiaSDBCC;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class BuildModeCommand implements CommandExecutor {
    private final BuildModeManager manager;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MinetopiaSDBCC.getPrefix() + "§cAlleen spelers kunnen dit commando gebruiken.");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("buildmode.use")) {
            player.sendMessage(MinetopiaSDBCC.getPrefix() + "§cJe hebt geen permissie om buildmode te gebruiken.");
            return true;
        }
        if (manager.isInBuildMode(player)) {
            manager.getBuildModePlayer(player).ifPresent(bmp -> bmp.restore(player));
            manager.removePlayerFromBuildMode(player);
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(MinetopiaSDBCC.getPrefix() + "§eBuildmode uitgeschakeld.");
        } else {
            manager.addPlayerToBuildMode(player);
            player.setGameMode(GameMode.CREATIVE);
            player.getInventory().clear();
            player.sendMessage(MinetopiaSDBCC.getPrefix() + "§aBuildmode ingeschakeld. Je bent nu in creative.");
        }
        return true;
    }
} 