package org.godfather.authenticator.configs;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LangFile {

    private final ConfigManager configManager;

    public LangFile(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public List<String> getRegistrationMessages() {
        List<String> messages = new ArrayList<>();
        FileConfiguration config = configManager.getInstance().getLangData().getFileConfiguration();
        for(String key : Objects.requireNonNull(config.getConfigurationSection("register")).getKeys(false)) {
            messages.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("register." + key))));
        }
        return messages;
    }

    public List<String> getLoginMessages() {
        List<String> messages = new ArrayList<>();
        FileConfiguration config = configManager.getInstance().getLangData().getFileConfiguration();
        for(String key : Objects.requireNonNull(config.getConfigurationSection("login")).getKeys(false)) {
            messages.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("login." + key))));
        }
        return messages;
    }

    public List<String> getAuthMessages() {
        List<String> messages = new ArrayList<>();
        FileConfiguration config = configManager.getInstance().getLangData().getFileConfiguration();
        for(String key : Objects.requireNonNull(config.getConfigurationSection("auth")).getKeys(false)) {
            messages.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("auth." + key))));
        }
        return messages;
    }

    public List<String> getSessionMessages() {
        List<String> messages = new ArrayList<>();
        FileConfiguration config = configManager.getInstance().getLangData().getFileConfiguration();
        for(String key : Objects.requireNonNull(config.getConfigurationSection("sessions")).getKeys(false)) {
            messages.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("sessions." + key))));
        }
        return messages;
    }

    public List<String> getRestrictionMessages() {
        List<String> messages = new ArrayList<>();
        FileConfiguration config = configManager.getInstance().getLangData().getFileConfiguration();
        for(String key : Objects.requireNonNull(config.getConfigurationSection("restrictions")).getKeys(false)) {
            messages.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("restrictions." + key))));
        }
        return messages;
    }
}
