package org.godfather.authenticator.auth.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.godfather.authenticator.auth.AuthManager;

import java.util.Objects;

public class PlayerInWorld implements Listener {

    private final AuthManager authManager;

    public PlayerInWorld(AuthManager authManager) {
        this.authManager = authManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!authManager.isAuth(player))
            return;
        if (!authManager.getInstance().getConfigManager().getConfigFile().getAuth().blockMovements())
            return;

        if (player.getLocation().add(0, -1, 0).getBlock().getType() == Material.AIR) {
            Block latestBlock = null;
            for (int i = 0; i < 384; i++) {
                if (player.getLocation().add(0, -i, 0).getBlock().getType() == Material.AIR) continue;
                latestBlock = player.getLocation().add(0, -i, 0).getBlock();
                break;
            }
            if (latestBlock != null)
                player.teleport(latestBlock.getLocation().add(0, 1, 0));
        }

        event.setTo(new Location(player.getWorld(), event.getFrom().getX(), Objects.requireNonNull(event.getFrom()).getY(), event.getFrom().getZ(), Objects.requireNonNull(event.getTo()).getYaw(), event.getTo().getPitch()));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!authManager.isAuth(player))
            return;

        if (!authManager.getInstance().getConfigManager().getConfigFile().getAuth().receiveChat())
            event.getRecipients().remove(player);
        if (!authManager.getInstance().getConfigManager().getConfigFile().getAuth().sendChat()) {
            event.setCancelled(true);
            player.sendMessage(authManager.getInstance().getConfigManager().getLangFile().getAuthMessages().get(1));
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (!authManager.isAuth(player))
            return;

        if (!authManager.getInstance().getConfigManager().getConfigFile().getAuth().blockCommands())
            return;

        if (event.getMessage().startsWith("/register") || event.getMessage().startsWith("/reg")
                || event.getMessage().startsWith("/r") || event.getMessage().startsWith("/login")
                || event.getMessage().startsWith("/log") || event.getMessage().startsWith("/l"))
            return;

        event.setCancelled(true);
        player.sendMessage(authManager.getInstance().getConfigManager().getLangFile().getAuthMessages().get(0));
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if(!authManager.getInstance().getConfigManager().getConfigFile().getAuth().blockBuilding())
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBuild(BlockBreakEvent event) {
        if(!authManager.getInstance().getConfigManager().getConfigFile().getAuth().blockBuilding())
            return;

        event.setCancelled(true);
    }
}
