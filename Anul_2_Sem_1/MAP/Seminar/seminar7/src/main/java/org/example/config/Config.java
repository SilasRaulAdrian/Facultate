package org.example.config;

import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = initProperties();

    private static Properties initProperties() {
        var properties = new Properties();
        try (var input = Config.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {

            if(input == null) {
                throw new RuntimeException("Cannot load config file :(");
            }

            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Properties getProperties() {
        return PROPERTIES;
    }
}
