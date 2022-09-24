package org.godfather.authenticator.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.godfather.authenticator.Authenticator;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class SpawnData {

    private final Authenticator plugin;
    public FileConfiguration fileConfiguration;
    private File file;

    public SpawnData(Authenticator plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        file = new File(plugin.getDataFolder(), "spawn.yml");

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

    public void saveSpawnConfig() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadSpawnConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void setSpawn(Location location) {
        fileConfiguration.set("spawn.x", location.getX());
        fileConfiguration.set("spawn.y", location.getY());
        fileConfiguration.set("spawn.z", location.getZ());
        fileConfiguration.set("spawn.yaw", location.getYaw());
        fileConfiguration.set("spawn.pitch", location.getPitch());
        fileConfiguration.set("spawn.world", Objects.requireNonNull(location.getWorld()).getName());
        saveSpawnConfig();
    }

    public void removeSpawn() {
        fileConfiguration.set("spawn", null);
        saveSpawnConfig();
    }

    public Location getSpawn() {
        if(fileConfiguration.getConfigurationSection("spawn") == null) return null;
        double x = fileConfiguration.getDouble("spawn.x");
        double y = fileConfiguration.getDouble("spawn.y");
        double z = fileConfiguration.getDouble("spawn.z");
        float yaw = (float) fileConfiguration.getDouble("spawn.yaw");
        float pitch = (float) fileConfiguration.getDouble("spawn.pitch");
        World world = Bukkit.getWorld(Objects.requireNonNull(fileConfiguration.getString("spawn.world")));
        return new Location(world, x, y, z, yaw, pitch);
    }
}
