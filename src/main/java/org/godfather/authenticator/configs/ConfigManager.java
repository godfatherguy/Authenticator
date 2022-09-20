package org.godfather.authenticator.configs;

import org.godfather.authenticator.Authenticator;

public class ConfigManager {

    private final Authenticator plugin;
    private final ConfigurationFile configurationFile;
    private final LangFile langFile;

    public ConfigManager(Authenticator plugin) {
        this.plugin = plugin;
        configurationFile = new ConfigurationFile(this);
        langFile = new LangFile(this);
    }

    public Authenticator getInstance() {
        return plugin;
    }

    public ConfigurationFile getConfigFile() {
        return configurationFile;
    }

    public LangFile getLangFile() {
        return langFile;
    }
}
