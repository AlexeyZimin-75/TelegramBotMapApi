package org.example.apiMethods;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestApiTest {
    YandexMapsClient yandexMapsClient = new YandexMapsClient();
    @Test
    void get_city_with_coords() throws Exception {

        String answer = yandexMapsClient.getCityName(60.761076, 56.769870);
        assertEquals("""
Россия, Свердловская область, Екатеринбург""", answer);
    }

    @Test
    void get_landmarks_with_coords() throws Exception {
        String answer = yandexMapsClient.getLandmarks("Россия, Свердловская область, Екатеринбург");
        assertEquals("""
Шарташские каменные палатки
Нулевой километр
Дом Н. И. Севастьянова
Плотинка
Культурно-просветительский Центр Эрмитаж-Урал
Рок-переход""", answer);
    }
}