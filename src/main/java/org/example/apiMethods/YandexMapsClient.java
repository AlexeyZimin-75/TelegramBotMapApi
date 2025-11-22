package org.example.apiMethods;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class YandexMapsClient {

    private final String geocodeApiKey;
    private final String suggestApiKey;
    private final HttpClient client;

    //Клиент для работы с API Яндекса
    public YandexMapsClient() {
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")){
            if (input == null) {
                throw new RuntimeException("Не удалось найти application.properties");
            }

            properties.load(input);

            this.geocodeApiKey = properties.getProperty("yandex-api-geocode.token");
            this.suggestApiKey = properties.getProperty("yandex-api.geosuggest.token");

            if (this.geocodeApiKey == null || this.suggestApiKey == null) {
                throw new RuntimeException("API ключи не найдены в config.properties");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.client = HttpClient.newBuilder().build();
    }

    //Метод для поиска достопримечательностей по городу
    public String getLandmarks(String city) throws IOException, InterruptedException, URISyntaxException {
        String text = "Достопримечательности " + city;

        URI uri = new URI(
                "https",
                "suggest-maps.yandex.ru",
                "/v1/suggest",
                "text=" + text + "&apikey=" + this.suggestApiKey,
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

        return JsonExtractor.extractLandmarkTexts(resp.body());
    }

    //Метод для поиска города по координатам
    public String getCityName(double latitude,double longitude) throws IOException, InterruptedException, URISyntaxException {

        URI uri = new URI(
                "https",
                "geocode-maps.yandex.ru",
                "/v1/",
                "apikey=" + this.geocodeApiKey+ "&geocode=" + latitude + "," + longitude +"&kind=locality" + "&results=5&format=json",
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

        return JsonExtractor.extractFormattedAddress(resp.body());
    }

    //Метод для поиска города по его названия
    public String getCityName(String cityName) throws IOException, InterruptedException, URISyntaxException {

        URI uri = new URI(
                "https",
                "geocode-maps.yandex.ru",
                "/v1/",
                "apikey=" + this.geocodeApiKey+ "&geocode=" + cityName +"&kind=locality" + "&results=5&format=json",
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

        return JsonExtractor.extractFormattedAddress(resp.body());
    }

}
