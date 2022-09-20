package org.godfather.authenticator.configs.config;

import org.godfather.authenticator.configs.ConfigurationFile;

public class PremiumConfig {

    private final ConfigurationFile configurationFile;

    public PremiumConfig(ConfigurationFile configurationFile) {
        this.configurationFile = configurationFile;
    }

    public boolean enabled() {
        return configurationFile.getConfig().getBoolean("premium.enabled");
    }
}
