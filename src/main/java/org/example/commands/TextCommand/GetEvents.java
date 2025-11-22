package org.example.commands.TextCommand;

import org.example.apiMethods.KudaGo.Event;
import org.example.apiMethods.KudaGo.KudaGoClient;
import org.example.service.UserData;
import org.example.service.UserDataService;
import org.example.service.UserStateService;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

public class GetEvents {
    private final UserStateService userStateService;
    private final UserDataService userDataService;

    public GetEvents(UserStateService userStateService, UserDataService userDataService) {
        this.userStateService = userStateService;
        this.userDataService = userDataService;
    }

    public String execute(Long userId) {
        UserData userData = userDataService.getUserData(userId);


        if (userData.getArrivalDate() == null || userData.getDestinationCity() == null) {
            return "Не хватает данных для поиска мероприятий (дата прибытия или город назначения)";
        }

        KudaGoClient client = new KudaGoClient();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            String arrivalDateStr = userData.getDepartureDate();
            String departureDateStr = userData.getArrivalDate();


            LocalDate startDate = LocalDate.parse(arrivalDateStr, formatter);
            LocalDate endDate = departureDateStr != null ?
                    LocalDate.parse(departureDateStr, formatter) :
                    startDate.plusDays(7);


            long startTimestamp = startDate.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
            long endTimestamp = endDate.atStartOfDay(ZoneOffset.UTC).toEpochSecond();


            String destinationCity = userData.getDestinationCity();
            String citySlug = getCitySlug(destinationCity);

            StringBuilder sb = new StringBuilder()
                    .append("🎭 Поиск мероприятий в KudaGo\n")
                    .append("📍 Город: ").append(destinationCity).append("\n")
                    .append("📅 Период: ").append(startDate).append(" - ").append(endDate).append("\n")
                    .append("=".repeat(60)).append("\n\n");


            List<Event> events = client.returnEvents(
                    citySlug,
                    String.valueOf(startTimestamp),
                    String.valueOf(endTimestamp)
            );

            sb.append(returnAllEvents(events, client));
            return sb.toString();

        } catch (Exception e) {
            System.err.println("Критическая ошибка в GetEvents: " + e.getMessage());
            e.printStackTrace();
            return "Ошибка при поиске мероприятий: " + e.getMessage();
        }
    }

    protected static StringBuilder returnAllEvents(List<Event> events, KudaGoClient client) {
        StringBuilder sb = new StringBuilder();

        if (events == null || events.isEmpty()) {
            sb.append("Мероприятия не найдены");
            return sb;
        }

        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            sb.append((i + 1) + ". 🎭 **").append(event.getTitle()).append("**\n");

            if (event.getDates() != null && !event.getDates().isEmpty()) {
                sb.append("   🔄 Всего сеансов: ").append(event.getDates().size()).append("\n");
            } else {
                sb.append("   📅 Даты не указаны\n");
            }


            if (event.getPrice() != null && !event.getPrice().isEmpty()) {
                sb.append("   💰 ").append(event.getPrice()).append("\n");
            }


            if (event.getCategories() != null && !event.getCategories().isEmpty()) {
                sb.append("   🏷️ ").append(String.join(", ", event.getCategories())).append("\n");
            }


            if (event.getSiteUrl() != null) {
                sb.append("   🔗 ").append(event.getSiteUrl()).append("\n");
            }

        }

        return sb;
    }

    protected String getCitySlug(String cityName) {
        if (cityName == null) return "msk";

        switch (cityName.toLowerCase()) {
            case "москва": return "msk";
            case "санкт-петербург": case "питер": case "спб": return "spb";
            case "новосибирск": return "nsk";
            case "екатеринбург": return "ekb";
            case "казань": return "kzn";
            case "нижний новгород": return "nnv";
            case "ростов-на-дону": return "rnd";
            case "сочи": return "sochi";
            case "краснодар": return "krd";
            case "самара": return "sam";
            case "омск": return "omsk";
            case "челябинск": return "che";
            case "уфа": return "ufa";
            case "пермь": return "perm";
            case "воронеж": return "vor";
            case "волгоград": return "vlg";
            default: return "msk";
        }
    }
}