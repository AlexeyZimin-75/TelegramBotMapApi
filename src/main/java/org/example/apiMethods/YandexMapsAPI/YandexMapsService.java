package org.example.apiMethods.YandexMapsAPI;

import org.example.apiMethods.JsonExtractor;

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
            throws IOException {
        String geocode = latitude + "," + longitude;
        String response = repository.sendGeocodeRequest(geocode, suggestApiKey);
        return JsonExtractor.extractFormattedAddress(response);
    }

    public String getCityName(String cityName)
            throws IOException {
        String response = repository.sendGeocodeRequest( cityName, suggestApiKey);
        return JsonExtractor.extractFormattedAddress(response);
    }

    public String getLandmarks(String city)
            throws IOException {
        String searchText = "Достопримечательности " + city;
        String response = repository.sendSuggestRequest(searchText, suggestApiKey);
        return JsonExtractor.extractLandmarkTexts(response);
    }
}

