package org.example.apiMethods.YandexApi;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class YandexMapsRepository {
    private final HttpClient client;

    public YandexMapsRepository(HttpClient client) {
        this.client = client;
    }

    public String sendSuggestRequest(String text, String apiKey)
            throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(
                "https",
                "suggest-maps.yandex.ru",
                "/v1/suggest",
                "text=" + text + "&apikey=" + apiKey,
                null
        );

        HttpRequest req = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

        if (resp.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch landmarks: " + resp.statusCode());
            }
        return resp.body();
    }

    public String sendGeocodeRequest(String text, String apiKey)
            throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(
                "https",
                "geocode-maps.yandex.ru",
                "/v1/",
                "apikey=" + apiKey + "&geocode=" + text +"&kind=locality" + "&results=5&format=json",
                null
        );

        HttpRequest req = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

        if (resp.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch city: " + resp.statusCode());
        }
        return resp.body();
    }

}
