package org.example.apiMethods.YandexApi;

import java.io.IOException;
import java.net.URISyntaxException;

public class YandexMapsService {
    private final YandexMapsRepository repository;
    private final String suggestApiKey;

    public YandexMapsService(YandexMapsRepository repository, String suggestApiKey) {
        this.repository = repository;
        this.suggestApiKey = suggestApiKey;
    }

    public String getCityName(double latitude, double longitude)
            throws IOException, InterruptedException, URISyntaxException {
        String geocode = latitude + "," + longitude;
        return repository.sendGeocodeRequest(geocode, suggestApiKey);
    }

    public String getCityName(String cityName)
            throws IOException, InterruptedException, URISyntaxException {
        return repository.sendGeocodeRequest(cityName, suggestApiKey);
    }

    public String getLandmarks(String city)
            throws IOException, InterruptedException, URISyntaxException {
        String searchText = "Достопримечательности " + city; // Бизнес-логика!
        String response = repository.sendSuggestRequest(searchText, suggestApiKey);
        return JsonExtractor.extractLandmarkTexts(response);
    }
}

