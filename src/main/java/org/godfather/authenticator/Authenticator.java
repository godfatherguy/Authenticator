package org.godfather.authenticator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.godfather.authenticator.auth.AuthManager;
import org.godfather.authenticator.configs.ConfigManager;
import org.godfather.authenticator.data.LanguageData;
import org.godfather.authenticator.data.PlayerData;
import org.godfather.authenticator.data.SpawnData;

public class Authenticator extends JavaPlugin {

    private ConfigManager configManager;
    private PlayerData playerData;
    private LanguageData languageData;
    private SpawnData spawnData;
    private AuthManager authManager;

    public void onEnable() {
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        if (!configManager.getConfigFile().getEnabled()) {
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        setupConfigs();
        authManager = new AuthManager(this);
        authManager.setup();

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[AUTH] Plugin successfully enabled!");
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[AUTH] Plugin disabled...");
    }

    public void setupConfigs() {
        playerData = new PlayerData(this);
        playerData.setup();
        playerData.savePlayerConfig();
        languageData = new LanguageData(this);
        saveResource("language.yml", false);
        languageData.setup();
        languageData.saveLangConfig();
        spawnData = new SpawnData(this);
        saveResource("spawn.yml", false);
        spawnData.setup();
        spawnData.saveSpawnConfig();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public LanguageData getLangData() {
        return languageData;
    }

    public SpawnData getSpawnData() {
        return spawnData;
    }

    public AuthManager getAuthManager() {
        return authManager;
    }
}
