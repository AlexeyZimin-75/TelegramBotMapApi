package org.example.apiMethods.YandexAPI;

import okhttp3.OkHttpClient;
import org.example.apiMethods.ConfigurationManager;
import org.example.apiMethods.HttpClientProvider;
import org.example.apiMethods.YandexMapsAPI.YandexMapsRepository;
import org.example.apiMethods.YandexMapsAPI.YandexMapsService;
import org.example.apiMethods.YandexSchedulesApi.YandexSchedulesRepository;
import org.example.apiMethods.YandexSchedulesApi.YandexSchedulesService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class YandexAPITest {
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

    //Тесты для Яндекс.Расписаний

    @Test
    void getBusSchedule() throws Exception {
        LocalDate today = LocalDate.now();
        String answer = yandexSchedulesService.findBusRoutes("Екатеринбург", "Тюмень", today);
        assertEquals("Рейс: Екатеринбург, Южный автовокзал -> Тюмень, автовокзал\n" +
                "Отправление: 23.11 18:45\n" +
                "Прибытие: 24.11 00:10\n" +
                "Перевозчик: ООО \"ТК Лига\"\n" +
                "Цена: 1840 руб.", answer);
    }

    @Test
    void getNullBusSchedule() throws Exception {
        LocalDate today = LocalDate.now();
        String answer = yandexSchedulesService.findBusRoutes("adasfgsdgs", "adasfgsdgs", today);
        assertEquals("Возможно вы ввели неправильное название населенного пункта или его не существует в базе", answer);
    }


    @Test
    void getCityCode() {
        String testJson = """
            [
                null,
                [
                    ["c54", "Екатеринбург", "г. Екатеринбург, Свердловская область", "yekaterinburg"],
                    ["s9600370", "Кольцово", "а/п Кольцово, Екатеринбург, Свердловская область", "yekaterinburg-koltsovo"],
                    ["s9635954", "Екатеринбург, Южный автовокзал", "авт.вкз. Екатеринбург, Южный автовокзал, Екатеринбург", "ekaterinburg-yuzhniy"],
                    ["s9635953", "Екатеринбург, Северный автовокзал", "авт.вкз. Екатеринбург, Северный автовокзал, Екатеринбург", "yekaterinburg-northern-bus-terminal"],
                    ["s9607404", "Екатеринбург-Пасс.", "вкз. Екатеринбург-Пасс., Екатеринбург", "ekaterinburg"]
                ]
            ]
            """;
        String answer = yandexSchedulesService.parseCityCode(testJson);
        assertEquals("s9635954", answer);
    }

    @Test
    void testEmptyJsonReturnsNull() {
        String emptyJson = "[null, []]";
        String result = yandexSchedulesService.parseCityCode(emptyJson);

        assertNull(result);
    }

    @Test
    void testNoBusStationsReturnsNull() {
        String noBusStationsJson = """
            [
                null,
                [
                    ["c54", "Екатеринбург", "г. Екатеринбург", "yekaterinburg"],
                    ["s9600370", "Кольцово", "а/п Кольцово", "yekaterinburg-koltsovo"]
                ]
            ]
            """;
        String result = yandexSchedulesService.parseCityCode(noBusStationsJson);

        assertNull(result);
    }
}