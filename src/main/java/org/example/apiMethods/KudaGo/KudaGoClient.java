package org.example.apiMethods.KudaGo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


public class KudaGoClient {
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final DateTimeFormatter dateFormatter;


    private static String BASE_URL;

    static {
        try (InputStream input = KudaGoClient.class.getClassLoader()
                .getResourceAsStream("application.properties")) {

            Properties prop = new Properties();
            prop.load(input);
            BASE_URL = prop.getProperty("api.base.url");

        } catch (Exception e) {
            throw new RuntimeException("Failed to load properties", e);
        }
    }


    public KudaGoClient() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    }



    public EventsResponse findEvents(String citySlug, String fromDate,
                                     String toDate, Integer maxResults) throws IOException {


        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(BASE_URL + "/events/")).newBuilder();


        urlBuilder.addQueryParameter("location", citySlug);

        if (maxResults != null) {
            urlBuilder.addQueryParameter("page_size", String.valueOf(maxResults));
        }


        urlBuilder.addQueryParameter("actual_since", fromDate);
        urlBuilder.addQueryParameter("actual_until", toDate);


        urlBuilder.addQueryParameter("fields", "id,title,place,description,dates,price,images,site_url,categories");
        urlBuilder.addQueryParameter("text_format", "text");


        urlBuilder.addQueryParameter("order_by", "-publication_date");

        String url = urlBuilder.build().toString();
        System.out.println("🔗 URL запроса: " + url);

        // Создаем запрос
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .build();

        // Выполняем запрос
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("HTTP ошибка: " + response.code() + " - " + response.message());
            }

            if (response.body() == null) {
                throw new IOException("Пустой ответ от сервера");
            }

            String responseBody = response.body().string();
            System.out.println("📄 Получен ответ, длина: " + responseBody.length() + " символов");


            return objectMapper.readValue(responseBody, EventsResponse.class);
        }
    }

    /**
     * Возвращает все ивенты
     */
    public List<Event> returnEvents(String citySlug, String fromDate, String toDate) {
        try {
            EventsResponse response = findEvents(citySlug, fromDate, toDate, 10);
            return response.getResults();
        } catch (Exception e) {
            System.err.println("❌ Ошибка при поиске мероприятий: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

}
