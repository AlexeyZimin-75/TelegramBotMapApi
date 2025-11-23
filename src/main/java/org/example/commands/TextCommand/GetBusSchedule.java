package org.example.commands.TextCommand;

import okhttp3.OkHttpClient;
import org.example.apiMethods.ConfigurationManager;
import org.example.apiMethods.HttpClientProvider;
import org.example.apiMethods.YandexSchedulesApi.YandexSchedulesRepository;
import org.example.apiMethods.YandexSchedulesApi.YandexSchedulesService;
import org.example.service.UserData;
import org.example.service.UserDataService;
import org.example.service.UserStateService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GetBusSchedule {
    private final UserStateService userStateService;
    private final UserDataService userDataService;
    private final YandexSchedulesRepository yandexSchedulesRepository;
    private final ConfigurationManager configurationManager;
    private final YandexSchedulesService yandexSchedulesService;

    public GetBusSchedule(UserStateService userStateService, UserDataService userDataService) {
        this.userStateService = userStateService;
        this.userDataService = userDataService;
        this.configurationManager = ConfigurationManager.getInstance();

        OkHttpClient httpClient = HttpClientProvider.getClient();
        yandexSchedulesRepository = new YandexSchedulesRepository(httpClient);
        yandexSchedulesService = new YandexSchedulesService(yandexSchedulesRepository, configurationManager.getSchedulesApiKey());

    }

    public String execute(Long userId) {
        UserData userData = userDataService.getUserData(userId);


        if (userData.getArrivalDate() == null || userData.getDestinationCity() == null) {
            return "Не хватает данных для маршрута (дата прибытия или город назначения)";
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            String arrivalDateStr = userData.getDepartureDate();
            String departureDateStr = userData.getArrivalDate();


            LocalDate startDate = LocalDate.parse(arrivalDateStr, formatter);
            LocalDate endDate = departureDateStr != null ?
                    LocalDate.parse(departureDateStr, formatter) :
                    startDate.plusDays(7);

            String currentCity = userData.getCurrentCity();
            String destinationCity = userData.getDestinationCity();

            return String.format("Маршрут до пункта назначения: %s \n\n Маршрут обратно: %s",
                    yandexSchedulesService.findBusRoutes(currentCity, destinationCity, startDate),
                    yandexSchedulesService.findBusRoutes(destinationCity, currentCity, endDate));

        } catch (Exception e) {
            System.err.println("Критическая ошибка в GetBusSchedule: " + e.getMessage());
            e.printStackTrace();
            return "Ошибка при поиске маршрутов: " + e.getMessage();
        }
    }
}
