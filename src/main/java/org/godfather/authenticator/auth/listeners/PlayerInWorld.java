package org.godfather.authenticator.auth.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
}
