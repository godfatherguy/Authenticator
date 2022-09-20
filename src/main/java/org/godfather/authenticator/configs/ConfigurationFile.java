package org.godfather.authenticator.configs;

import org.bukkit.configuration.file.FileConfiguration;
import org.godfather.authenticator.configs.config.*;

public class ConfigurationFile {

    private final FileConfiguration config;
    private final SessionConfig sessions;
    private final RegistrationConfig registration;
    private final LoginConfig login;
    private final AuthConfig auth;
    private final RestrictionConfig restrictions;
    private final PremiumConfig premium;

    public ConfigurationFile(ConfigManager configManager) {
        config = configManager.getInstance().getConfig();
        sessions = new SessionConfig(this);
        registration = new RegistrationConfig(this);
        login = new LoginConfig(this);
        auth = new AuthConfig(this);
        restrictions = new RestrictionConfig(this);
        premium = new PremiumConfig(this);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public SessionConfig getSessions() {
        return sessions;
    }

    public RegistrationConfig getRegistration() {
        return registration;
    }

    public LoginConfig getLogin() {
        return login;
    }

    public AuthConfig getAuth() {
        return auth;
    }

    public RestrictionConfig getRestrictions() {
        return restrictions;
    }

    public PremiumConfig getPremium() {
        return premium;
    }

    public boolean getEnabled() {
        return config.getBoolean("auth-on-join");
    }
}
