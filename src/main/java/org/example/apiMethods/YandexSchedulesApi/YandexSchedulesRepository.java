package org.example.apiMethods.YandexSchedulesApi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.net.URISyntaxException;

public class YandexSchedulesRepository {

    private final OkHttpClient client;

    public YandexSchedulesRepository(OkHttpClient client) {
        this.client = client;
    }

    public String sendCityCode(String cityName) throws
            IOException {

        // Построение URL с параметрами
        HttpUrl url = HttpUrl.parse("https://suggests.rasp.yandex.net/all_suggests")
                .newBuilder()
                .addQueryParameter("format", "old")
                .addQueryParameter("part", cityName)
                .build();

        // Создание запроса
        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .get()
                .build();

        // Выполнение запроса с автоматическим закрытием Response
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to fetch cityCode: " + response.code());
            }
            return response.body().string();
        }
    }

    public String getSchedule(String fromCode, String toCode, String date, String apiKey)
            throws IOException {

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.rasp.yandex.net")
                .addPathSegment("v3.0")
                .addPathSegment("search")
                .addEncodedPathSegment("") // Последний слэш в пути
                .addQueryParameter("apikey", apiKey)
                .addQueryParameter("format", "json")
                .addQueryParameter("from", fromCode)
                .addQueryParameter("to", toCode)
                .addQueryParameter("lang", "ru_RU")
                .addQueryParameter("page", "1")
                .addQueryParameter("date", date)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Schedule request failed: " + response.code());
            }
            return response.body().string();
        }
    }
}
