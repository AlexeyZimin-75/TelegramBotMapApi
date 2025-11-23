package org.example.apiMethods;

import okhttp3.OkHttpClient;
import org.example.apiMethods.YandexMapsAPI.YandexMapsRepository;
import org.example.apiMethods.YandexMapsAPI.YandexMapsService;
import org.example.apiMethods.YandexSchedulesApi.YandexSchedulesRepository;
import org.example.apiMethods.YandexSchedulesApi.YandexSchedulesService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class YandexMapsAPITest {
    OkHttpClient client = HttpClientProvider.getClient();

    YandexMapsRepository yandexMapsRepository = new YandexMapsRepository(client);
    YandexMapsService yandexMapsServiceSuggest = new YandexMapsService(yandexMapsRepository, ConfigurationManager.getInstance().getSuggestApiKey());
    YandexMapsService yandexMapsServiceGeocode = new YandexMapsService(yandexMapsRepository, ConfigurationManager.getInstance().getGeocodeApiKey());

    YandexSchedulesRepository yandexSchedulesRepository = new YandexSchedulesRepository(client);
    YandexSchedulesService yandexSchedulesService = new YandexSchedulesService(yandexSchedulesRepository, ConfigurationManager.getInstance().getSchedulesApiKey());

    @Test
    void getCityWithCords() throws Exception {

        String answer = yandexMapsServiceGeocode.getCityName(60.761076, 56.769870);
        assertEquals("""
Россия, Свердловская область, Екатеринбург""", answer);
    }

    @Test
    void getCityWithCityName() throws Exception {

        String answer = yandexMapsServiceGeocode.getCityName("Симферополь");
        assertEquals("""
Россия, Республика Крым, Симферополь""", answer);
    }

    @Test
    void getCityWithWrongCityName() throws Exception {

        String answer = yandexMapsServiceGeocode.getCityName("вылрплоыврпловы");
        assertEquals("""
                """, answer);
    }

    @Test
    void getLandmarksWithCity() throws Exception {
        String answer = yandexMapsServiceSuggest.getLandmarks("Россия, Свердловская область, Екатеринбург");
        assertEquals("""
Шарташские каменные палатки
Дом Н. И. Севастьянова
Нулевой километр
Плотинка
Рок-переход
Культурно-просветительский Центр Эрмитаж-Урал""", answer);
    }

    @Test
    void getLandmarksWithWrongCity() throws Exception {
        String answer = yandexMapsServiceSuggest.getLandmarks("fihj=hksfdsk");
        assertEquals("", answer);
    }

    @Test
    void getBusSchedule() throws Exception {
        LocalDate today = LocalDate.now();
        String answer = yandexSchedulesService.findBusRoutes("Екатеринбург", "Тюмень", today);
        assertEquals("", answer);
    }

}