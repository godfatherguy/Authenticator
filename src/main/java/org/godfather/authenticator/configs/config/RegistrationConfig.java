package org.godfather.authenticator.configs.config;

import org.godfather.authenticator.configs.ConfigurationFile;

import java.util.List;

public class RegistrationConfig {

    private final ConfigurationFile configurationFile;

    public RegistrationConfig(ConfigurationFile configurationFile) {
        this.configurationFile = configurationFile;
    }

    public boolean enabled() {
        return configurationFile.getConfig().getBoolean("registration.enabled");
    }

    public boolean warnTitle() {
        return configurationFile.getConfig().getBoolean("registration.warnings.warn-with-title");
    }

    public boolean passwordDoubleCheck() {
        return configurationFile.getConfig().getBoolean("registration.password.double-check");
    }

    public boolean kickAfterReg() {
        return configurationFile.getConfig().getBoolean("registration.kick-after-registration");
    }

    public boolean logAfterReg() {
        return configurationFile.getConfig().getBoolean("registration.login-after-registration");
    }

    public int getTimeout() {
        return configurationFile.getConfig().getInt("registration.timeout");
    }

    public int getNicknameMinLength() {
        return configurationFile.getConfig().getInt("registration.nickname.min-length");
    }

    public int getNicknameMaxLength() {
        return configurationFile.getConfig().getInt("registration.nickname.max-length");
    }

    public int getWarningDelay() {
        return configurationFile.getConfig().getInt("registration.warnings.delay");
    }

    public int getPasswordMinLength() {
        return configurationFile.getConfig().getInt("registration.password.min-length");
    }

    public int getPasswordMaxLength() {
        return configurationFile.getConfig().getInt("registration.password.max-length");
    }

    public List<String> getUnsafePasswords() {
        return configurationFile.getConfig().getStringList("registration.password.unsafe-passwords");
    }
}
