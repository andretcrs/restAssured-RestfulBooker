package br.com.desafio.config;

import org.aeonbits.owner.ConfigFactory;

public class ConfigManager {

    private static final Configuration CONFIG = ConfigFactory.create(Configuration.class);

    private ConfigManager() {
    }

    public static Configuration getConfiguration() {
        return CONFIG;
    }
}