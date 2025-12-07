package org.example.apiMethods;

import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;

public class HttpClientProvider {
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)  // Рекомендуется явно задать
            .writeTimeout(10, TimeUnit.SECONDS) // Рекомендуется явно задать
            .followRedirects(true)              // Аналог HttpClient.Redirect.NORMAL
            .followSslRedirects(true)           // Разрешает редиректы между HTTP/HTTPS
            .build();

    public static OkHttpClient getClient() {
        return CLIENT;
    }
}
