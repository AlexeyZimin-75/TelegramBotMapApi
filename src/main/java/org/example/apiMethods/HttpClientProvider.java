package org.example.apiMethods;

import java.net.http.HttpClient;
import java.time.Duration;

public class HttpClientProvider {
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    public static HttpClient getClient() {
        return CLIENT;
    }
}
