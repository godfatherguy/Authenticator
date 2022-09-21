package org.godfather.authenticator.auth.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.godfather.authenticator.auth.AuthManager;

public class PlayersEvent implements Listener {

    private final AuthManager authManager;

    public PlayersEvent(AuthManager authManager) {
        this.authManager = authManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (authManager.getInstance().getConfigManager().getConfigFile().getRestrictions().blockFakeNames()) {
            for (String key : authManager.getInstance().getPlayerData().fileConfiguration.getKeys(false)) {
                if (player.getName().equals(key)) continue;
                if (!player.getName().equalsIgnoreCase(key)) continue;
                player.kickPlayer(authManager.getInstance().getConfigManager().getLangFile().getRestrictionMessages().get(0).replaceAll("%NAME%", key));
                return;
            }
        }

        if (authManager.getInstance().getConfigManager().getConfigFile().getRestrictions().getMaxJoinIP() != 0) {
            int ipCount = authManager.getInstance().getPlayerData().getIPs(player);
            if (ipCount > authManager.getInstance().getConfigManager().getConfigFile().getRestrictions().getMaxJoinIP()) {
                player.kickPlayer(authManager.getInstance().getConfigManager().getLangFile().getRestrictionMessages().get(3));
                return;
            }
        }

        if (authManager.getInstance().getConfigManager().getConfigFile().getAuth().spawnOnJoin()) {
            if (authManager.getInstance().getSpawnData().getSpawn().getWorld() != null)
                player.teleport(authManager.getInstance().getSpawnData().getSpawn());
        }

        if (authManager.isInSession(player)) {
            authManager.removeSession(player);
            player.sendMessage(authManager.getInstance().getConfigManager().getLangFile().getSessionMessages().get(0));
            return;
        }
        authManager.createAuthPlayer(player);
        if (authManager.getInstance().getConfigManager().getConfigFile().getAuth().invisible())
            player.setInvisible(true);
        if (authManager.getInstance().getConfigManager().getConfigFile().getAuth().invincible())
            player.setInvulnerable(true);
        if (authManager.getInstance().getConfigManager().getConfigFile().getAuth().blindnessOnJoin())
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100000000, 3));
        if (authManager.getInstance().getConfigManager().getConfigFile().getAuth().forceSurvival())
            player.setGameMode(GameMode.SURVIVAL);
        if (authManager.getInstance().getConfigManager().getConfigFile().getAuth().hideInventory())
            authManager.getAuthPlayer(player).hideInventory();

        if (authManager.getInstance().getPlayerData().isRegistered(player)) {
            authManager.addLogin(player);
            return;
        }

        if (!authManager.getInstance().getConfigManager().getConfigFile().getRegistration().enabled()) {
            player.kickPlayer(authManager.getInstance().getConfigManager().getLangFile().getRegistrationMessages().get(6));
            return;
        }
        authManager.addRegister(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (authManager.isLogged(player)) {
            if (authManager.getInstance().getConfigManager().getConfigFile().getSessions().enabled()) {
                authManager.addSession(player);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        authManager.removeSession(player);
                    }
                }.runTaskLater(authManager.getInstance(), authManager.getInstance().getConfigManager().getConfigFile().getSessions().getTimeout() * 20L);
            }
            return;
        }

        player.setInvulnerable(false);
        player.setInvisible(false);
        player.removePotionEffect(PotionEffectType.BLINDNESS);

        if (authManager.getInstance().getConfigManager().getConfigFile().getAuth().hideInventory())
            authManager.getAuthPlayer(player).showInventory();

        if (authManager.isLogin(player))
            authManager.removeLogin(player);
        else authManager.removeRegister(player);

        authManager.removeAuthPlayer(player);
    }
}
