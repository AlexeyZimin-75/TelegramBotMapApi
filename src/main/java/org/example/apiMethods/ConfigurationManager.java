package org.example.apiMethods;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationManager {
    private static ConfigurationManager instance;
    private final String geocodeApiKey;
    private final String suggestApiKey;
    private final String schedulesApiKey;

    private ConfigurationManager() {
        Properties properties = new Properties();
        try (
                InputStream input = getClass().getClassLoader()
                        .getResourceAsStream("application.properties")){
            if (input == null) {
                throw new RuntimeException("Не удалось найти" +
                        " application.properties");
            }

            properties.load(input);

            this.geocodeApiKey = properties.getProperty("yandex-api-geocode.token");
            this.suggestApiKey = properties.getProperty("yandex-api.geosuggest.token");
            this.schedulesApiKey = properties.getProperty("yandex-api.schedules.token");

            if (this.geocodeApiKey == null || this.suggestApiKey == null ||
                    this.schedulesApiKey == null) {
                throw new RuntimeException("API ключи не найдены в config.properties");
            }

        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    public String getSuggestApiKey() {return suggestApiKey; }
    public String getGeocodeApiKey() { return geocodeApiKey; }
    public String getSchedulesApiKey() { return schedulesApiKey; }
}
