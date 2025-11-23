package org.example.apiMethods.YandexSchedulesApi;

import org.example.apiMethods.JsonExtractor;

import java.io.IOException;
import java.time.LocalDate;

public class YandexSchedulesService {
    private final YandexSchedulesRepository repository;
    private final String suggestApiKey;

    public YandexSchedulesService(YandexSchedulesRepository repository, String suggestApiKey) {
        this.repository = repository;
        this.suggestApiKey = suggestApiKey;
    }

    public String findBusRoutes(String departureCity, String arrivalCity, LocalDate date)
            throws IOException{

        String departureCityJson = repository.sendCityCode(departureCity);
        String departureCode = parseCityCode(departureCityJson);

        String arrivalCityJson = repository.sendCityCode(arrivalCity);
        String arrivalCode = parseCityCode(arrivalCityJson);

        String scheduleJson = repository.getSchedule(
                departureCode,
                arrivalCode,
                date.toString(),
                suggestApiKey
        );

        return JsonExtractor.extractBusShedules(scheduleJson);
    }

    private String parseCityCode(String json) {
        return JsonExtractor.extractFirstBusStationCode(json);
    }
}
