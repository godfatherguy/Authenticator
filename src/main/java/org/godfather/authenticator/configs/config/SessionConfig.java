package org.godfather.authenticator.configs.config;

import org.godfather.authenticator.configs.ConfigurationFile;

public class SessionConfig {

    private final ConfigurationFile configurationFile;

    public SessionConfig(ConfigurationFile configurationFile) {
        this.configurationFile = configurationFile;
    }

    public boolean enabled() {
        return configurationFile.getConfig().getBoolean("sessions.enabled");
    }

    public boolean expireIpChange() {
        return configurationFile.getConfig().getBoolean("sessions.expire-on-ip-change");
    }

    public int getTimeout() {
        return configurationFile.getConfig().getInt("sessions.timeout");
    }
}
