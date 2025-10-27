package org.example.apiMethods;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestApiTest {
    YandexMapsClient yandexMapsClient = new YandexMapsClient();
    @Test
    void getCityWithCords() throws Exception {

        String answer = yandexMapsClient.getCityName(60.761076, 56.769870);
        assertEquals("""
Россия, Свердловская область, Екатеринбург""", answer);
    }

    @Test
    void getCityWithCityName() throws Exception {

        String answer = yandexMapsClient.getCityName("Симферополь");
        assertEquals("""
Россия, Республика Крым, Симферополь""", answer);
    }

    @Test
    void getCityWithWrongCityName() throws Exception {

        String answer = yandexMapsClient.getCityName("вылрплоыврпловы");
        assertEquals("""
                """, answer);
    }

    @Test
    void getLandmarksWithCity() throws Exception {
        String answer = yandexMapsClient.getLandmarks("Россия, Свердловская область, Екатеринбург");
        assertEquals("""
Шарташские каменные палатки
Нулевой километр
Дом Н. И. Севастьянова
Плотинка
Культурно-просветительский Центр Эрмитаж-Урал
Рок-переход""", answer);
    }

    @Test
    void getLandmarksWithWrongCity() throws Exception {
        String answer = yandexMapsClient.getLandmarks("fihj=hksfdsk");
        assertEquals("", answer);
    }

}