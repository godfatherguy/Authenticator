package org.godfather.authenticator.configs.config;

import org.godfather.authenticator.configs.ConfigurationFile;

public class LoginConfig {

    private final ConfigurationFile configurationFile;

    public LoginConfig(ConfigurationFile configurationFile) {
        this.configurationFile = configurationFile;
    }

    public boolean kickOnWrong() {
        return configurationFile.getConfig().getBoolean("login.kick-on-wrong-password");
    }

    public boolean warnTitle() {
        return configurationFile.getConfig().getBoolean("login.warnings.warn-with-title");
    }

    public int getTimeout() {
        return configurationFile.getConfig().getInt("login.timeout");
    }

    public int getWarningDelay() {
        return configurationFile.getConfig().getInt("login.warnings.delay");
    }
}
