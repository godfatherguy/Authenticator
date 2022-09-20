package org.godfather.authenticator.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.godfather.authenticator.Authenticator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerData {

    private final Authenticator plugin;
    public FileConfiguration fileConfiguration;
    private File file;

    public PlayerData(Authenticator plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        file = new File(plugin.getDataFolder(), "playerdata.yml");

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

    public void savePlayerConfig() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadPlayerConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void savePlayer(Player player) {
        if(fileConfiguration.get(player.getName()) == null)
            fileConfiguration.createSection(player.getName());
        fileConfiguration.set(player.getName() + ".ip", Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress());
        savePlayerConfig();
    }

    public void setIp(Player player) {
        fileConfiguration.set(player.getName() + ".ip", Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress());
        savePlayerConfig();
    }

    public void setPassword(Player player, String password) {
        fileConfiguration.set(player.getName() + ".password", password);
        savePlayerConfig();
    }

    public void refreshIP(Player player) {
        fileConfiguration.set(player.getName() + ".ip", Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress());
        savePlayerConfig();
    }

    public boolean isSaved(Player player) {
        boolean exist = false;
        for (String key : fileConfiguration.getKeys(false)) {
            if (key.equals(player.getName())) exist = true;
        }
        return exist;
    }

    public boolean isRegistered(Player player) {
        return fileConfiguration.getString(player.getName() + ".password") != null;
    }

    public String getPassword(Player player) {
        return fileConfiguration.getString(player.getName() + ".password");
    }

    public List<String> getIPs() {
        List<String> ips = new ArrayList<>();
        for(String key : fileConfiguration.getKeys(false)) {
            ips.add(fileConfiguration.getString(key + ".ip"));
        }
        return ips;
    }

    public List<String> getAccounts(Player player) {
        List<String> ips = new ArrayList<>();
        for(String key : fileConfiguration.getKeys(false)) {
            if(!Objects.equals(fileConfiguration.getString(key + ".ip"), Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress())) continue;
            ips.add(key);
        }
        return ips;
    }

    public int getIPs(Player player) {
        int count = 0;
        for(String key : fileConfiguration.getKeys(false)) {
            if(!Objects.equals(fileConfiguration.getString(key + ".ip"), Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress())) continue;
            count++;
        }
        return count;
    }
}
