package org.godfather.authenticator.configs.config;

import org.godfather.authenticator.configs.ConfigurationFile;

public class AuthConfig {

    private final ConfigurationFile configurationFile;

    public AuthConfig(ConfigurationFile configurationFile) {
        this.configurationFile = configurationFile;
    }

    public boolean spawnOnJoin() {
        return configurationFile.getConfig().getBoolean("auth.spawn-on-join");
    }

    public boolean hideInventory() {
        return configurationFile.getConfig().getBoolean("auth.restrictions.hide-inventory");
    }

    public boolean blockMovements() {
        return configurationFile.getConfig().getBoolean("auth.restrictions.block-movements");
    }

    public boolean blockCommands() {
        return configurationFile.getConfig().getBoolean("auth.restrictions.block-commands");
    }

    public boolean receiveChat() {
        return configurationFile.getConfig().getBoolean("auth.restrictions.receive-chat");
    }

    public boolean sendChat() {
        return configurationFile.getConfig().getBoolean("auth.restrictions.send-chat");
    }

    public boolean forceSurvival() {
        return configurationFile.getConfig().getBoolean("auth.gamemode.force-survival");
    }

    public boolean forceSurvivalAfter() {
        return configurationFile.getConfig().getBoolean("auth.gamemode.force-survival-after-login");
    }

    public boolean blindnessOnJoin() {
        return configurationFile.getConfig().getBoolean("auth.blindness-on-join");
    }

    public boolean showAccounts() {
        return configurationFile.getConfig().getBoolean("auth.show-accounts");
    }

    public boolean invisible() {
        return configurationFile.getConfig().getBoolean("auth.invisible-when-auth");
    }

    public boolean invincible() {
        return configurationFile.getConfig().getBoolean("auth.invincible-when-auth");
    }
}
