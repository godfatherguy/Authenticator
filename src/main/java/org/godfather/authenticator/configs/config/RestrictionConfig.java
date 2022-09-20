package org.godfather.authenticator.configs.config;

import org.godfather.authenticator.configs.ConfigurationFile;

public class RestrictionConfig {

    private final ConfigurationFile configurationFile;

    public RestrictionConfig(ConfigurationFile configurationFile) {
        this.configurationFile = configurationFile;
    }

    public boolean blockFakeNames() {
        return configurationFile.getConfig().getBoolean("restrictions.block-fake-usernames");
    }

    public int getMaxRegIP() {
        return configurationFile.getConfig().getInt("restrictions.maxReg-per-ip");
    }

    public int getMaxLogIP() {
        return configurationFile.getConfig().getInt("restrictions.maxLog-per-ip");
    }

    public int getMaxJoinIP() {
        return configurationFile.getConfig().getInt("restrictions.maxJoin-per-ip");
    }
}
