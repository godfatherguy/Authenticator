package org.godfather.authenticator.auth;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.godfather.authenticator.Authenticator;
import org.godfather.authenticator.auth.commands.ChangepasswordCommand;
import org.godfather.authenticator.auth.commands.LoginCommand;
import org.godfather.authenticator.auth.commands.RegisterCommand;
import org.godfather.authenticator.auth.commands.UnregisterCommand;
import org.godfather.authenticator.auth.listeners.PlayerInWorld;
import org.godfather.authenticator.auth.listeners.PlayersEvent;
import org.godfather.authenticator.utils.AuthPlayer;

import java.util.*;

public class AuthManager {

    private final Authenticator plugin;
    private final Set<UUID> register = new HashSet<>();
    private final Set<UUID> login = new HashSet<>();
    private final Map<UUID, String> session = new HashMap<>();
    private final Map<Player, AuthPlayer> authPlayers = new HashMap<>();

    public AuthManager(Authenticator plugin) {
        this.plugin = plugin;
    }

    public Authenticator getInstance() {
        return plugin;
    }

    public boolean isAuth(Player player) {
        return register.contains(player.getUniqueId()) || login.contains(player.getUniqueId());
    }

    public boolean isLogged(Player player) {
        return !login.contains(player.getUniqueId()) && !register.contains(player.getUniqueId());
    }

    public boolean isLogin(Player player) {
        return login.contains(player.getUniqueId());
    }

    public boolean isRegister(Player player) {
        return register.contains(player.getUniqueId());
    }

    public boolean isInSession(Player player) {
        if (!session.containsKey(player.getUniqueId())) return false;
        if (plugin.getConfigManager().getConfigFile().getSessions().expireIpChange())
            return session.get(player.getUniqueId()).equals(Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress());
        else return true;
    }

    public AuthPlayer getAuthPlayer(Player player) {
        return authPlayers.get(player);
    }

    public void addRegister(Player player) {
        register.add(player.getUniqueId());
        int timeout = plugin.getConfigManager().getConfigFile().getRegistration().getTimeout();
        if (plugin.getConfigManager().getConfigFile().getRegistration().warnTitle()) {
            if (plugin.getConfigManager().getConfigFile().getRegistration().passwordDoubleCheck())
                player.sendTitle(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(2), plugin.getConfigManager().getLangFile().getRegistrationMessages().get(3), 0, timeout * 20, 0);
            else
                player.sendTitle(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(2), plugin.getConfigManager().getLangFile().getRegistrationMessages().get(4), 0, timeout * 20, 0);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isRegister(player)) {
                    cancel();
                    return;
                }
                if (plugin.getConfigManager().getConfigFile().getRegistration().passwordDoubleCheck())
                    player.sendMessage(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(0));
                else player.sendMessage(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(1));
            }
        }.runTaskTimer(plugin, 10L, plugin.getConfigManager().getConfigFile().getRegistration().getWarningDelay() * 20L);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle(" ", " ", 0, 1, 0);
                if (!isRegister(player)) return;
                if (timeout == 0) return;
                player.kickPlayer(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(5));
            }
        }.runTaskLater(plugin, timeout * 20L);
    }

    public void addLogin(Player player) {
        login.add(player.getUniqueId());
        int timeout = plugin.getConfigManager().getConfigFile().getLogin().getTimeout();
        if (plugin.getConfigManager().getConfigFile().getLogin().warnTitle())
            player.sendTitle(plugin.getConfigManager().getLangFile().getLoginMessages().get(1), plugin.getConfigManager().getLangFile().getLoginMessages().get(2), 0, timeout * 20, 0);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isLogin(player)) {
                    cancel();
                    return;
                }
                player.sendMessage(plugin.getConfigManager().getLangFile().getLoginMessages().get(0));
            }
        }.runTaskTimer(plugin, 10L, plugin.getConfigManager().getConfigFile().getLogin().getWarningDelay() * 20L);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle(" ", " ", 0, 1, 0);
                if (!isLogin(player)) return;
                if (timeout == 0) return;
                player.kickPlayer(plugin.getConfigManager().getLangFile().getLoginMessages().get(3));
            }
        }.runTaskLater(plugin, timeout * 20L);
    }

    public void addSession(Player player) {
        session.put(player.getUniqueId(), Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress());
    }

    public void removeRegister(Player player) {
        register.remove(player.getUniqueId());
        player.sendTitle(" ", " ", 0, 1, 0);
    }

    public void removeLogin(Player player) {
        login.remove(player.getUniqueId());
        player.sendTitle(" ", " ", 0, 1, 0);
    }

    public void removeSession(Player player) {
        session.remove(player.getUniqueId());
    }

    public void createAuthPlayer(Player player) {
        authPlayers.put(player, new AuthPlayer(player));
    }

    public void removeAuthPlayer(Player player) {
        authPlayers.remove(player);
    }

    public void setup() {
        Objects.requireNonNull(plugin.getCommand("register")).setExecutor(new RegisterCommand(plugin));
        Objects.requireNonNull(plugin.getCommand("login")).setExecutor(new LoginCommand(plugin));
        Objects.requireNonNull(plugin.getCommand("unregister")).setExecutor(new UnregisterCommand(plugin));
        Objects.requireNonNull(plugin.getCommand("changepassword")).setExecutor(new ChangepasswordCommand(plugin));
        plugin.getServer().getPluginManager().registerEvents(new PlayerInWorld(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayersEvent(this), plugin);
    }
}
