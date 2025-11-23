package org.example.apiMethods.YandexSchedulesApi;

import org.example.apiMethods.JsonExtractor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;

public class YandexSchedulesService {
    private final YandexSchedulesRepository repository;
    private final String suggestApiKey;

    public YandexSchedulesService(YandexSchedulesRepository repository, String suggestApiKey) {
        this.repository = repository;
        this.suggestApiKey = suggestApiKey;
    }

    public String findBusRoutes(String departureCity, String arrivalCity, LocalDate date)
            throws IOException, InterruptedException, URISyntaxException {

        // Шаг 1: Получить код города отправления
        String departureCityJson = repository.sendCityCode(departureCity);
        String departureCode = parseCityCode(departureCityJson); // Бизнес-логика парсинга

        // Шаг 2: Получить код города прибытия
        String arrivalCityJson = repository.sendCityCode(arrivalCity);
        String arrivalCode = parseCityCode(arrivalCityJson); // Бизнес-логика парсинга

        // Шаг 3: Запросить расписание
        String scheduleJson = repository.getSchedule(
                departureCode,
                arrivalCode,
                date.toString(),
                suggestApiKey
        );

        // Шаг 4: Парсинг и формирование результата
        return JsonExtractor.extractBusShedules(scheduleJson); // Бизнес-логика обработки
    }

    private String parseCityCode(String json) {
        // Логика парсинга для получения кода станции (например, "s9623439")
        return JsonExtractor.extractBusShedules(json);
    }
}
