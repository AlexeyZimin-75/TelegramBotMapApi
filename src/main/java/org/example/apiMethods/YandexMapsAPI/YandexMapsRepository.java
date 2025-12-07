package org.example.apiMethods.YandexMapsAPI;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.net.URISyntaxException;

public class YandexMapsRepository {
    private final OkHttpClient client;

    public YandexMapsRepository(OkHttpClient client) {
        this.client = client;
    }

    // Метод для поиска через Suggest API
    public String sendSuggestRequest(String searchText, String apiKey)
            throws IOException{

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("suggest-maps.yandex.ru")
                .addPathSegment("v1")
                .addPathSegment("suggest")
                .addQueryParameter("text", searchText)
                .addQueryParameter("apikey", apiKey)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("API request failed: " + response.code());
            }

            return response.body().string();
        }
    }

    // Метод для geocode API
    public String sendGeocodeRequest(String geocode, String apiKey)
            throws IOException {

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("geocode-maps.yandex.ru")
                .addPathSegment("1.x")
                .addQueryParameter("apikey", apiKey)
                .addQueryParameter("geocode", geocode)
                .addQueryParameter("kind", "locality")
                .addQueryParameter("results", "5")
                .addQueryParameter("format", "json")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Geocode request failed: " + response.code());
            }

            return response.body().string();
        }
    }
}
