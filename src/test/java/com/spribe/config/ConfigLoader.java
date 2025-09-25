package com.spribe.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;

public final class ConfigLoader {
    private static final ObjectMapper YAML = new ObjectMapper(new YAMLFactory());

    private ConfigLoader() {
    }

    public static AppConfig load() {
        String path = System.getProperty("config.path", "config/appConfig.yaml");

        try (var is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalStateException("Config file not found in classpath: " + path);
            }
            return YAML.readValue(is, AppConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read config: " + path + " -> " + e.getMessage(), e);
        }
    }
}
