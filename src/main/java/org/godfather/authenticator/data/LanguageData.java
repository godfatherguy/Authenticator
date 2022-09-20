package org.godfather.authenticator.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.godfather.authenticator.Authenticator;

import java.io.File;
import java.io.IOException;

public class LanguageData {

    private final Authenticator plugin;
    public FileConfiguration fileConfiguration;
    private File file;

    public LanguageData(Authenticator plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        file = new File(plugin.getDataFolder(), "language.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public void saveLangConfig() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadLangConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }
}
