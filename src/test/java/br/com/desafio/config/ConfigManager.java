package br.com.desafio.config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException("Arquivo application.properties não encontrado");
            }

            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar application.properties", e);
        }
    }

    private ConfigManager() {
    }

    public static String get(String key) {
        return System.getProperty(key, properties.getProperty(key));
    }


    public static String getBaseUrl() {
        return get("base.url");
    }

    public static String getUsername() {
        return get("auth.username");
    }

    public static String getPassword() {
        return get("auth.password");
    }
}