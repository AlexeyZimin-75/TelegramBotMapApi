package org.example.commands;

import org.example.apiMethods.YandexApi.YandexMapsRepository;
import org.example.apiMethods.YandexApi.YandexMapsService;
import org.example.keyboards.LocationKeyboard;
import org.example.service.UserDataService;
import org.example.service.UserStateService;
import org.example.states.UserState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

import org.example.apiMethods.*;

public class GetLocationCommand implements Command {

    private final UserStateService userStateService;

    private final YandexMapsRepository yandexMapsRepository;
    private final ConfigurationManager configurationManager;

    private final Map<String,String> locationTriggers;


    public GetLocationCommand(UserStateService userStateService, UserDataService userDataService) {
        this.userStateService = userStateService;

        HttpClient httpClient = HttpClientProvider.getClient();
        this.yandexMapsRepository = new YandexMapsRepository(httpClient);
        this.configurationManager = ConfigurationManager.getInstance();

        this.locationTriggers = new HashMap<>();
        locationTriggers.put("\uD83C\uDF0D Проложить маршрут","/location");
    }


    @Override
    public String getCommandName() {
        return "location";
    }

    @Override
    public String getDescription() {
        return "Определить мой город по геолокации";
    }

    @Override
    public SendMessage execute(AbsSender absSender, Message message) {
        Long userId = message.getFrom().getId();

        System.out.println("📍 Команда location от пользователя: " + userId);

        // Устанавливаем состояние ожидания геолокации
        userStateService.setUserState(userId, UserState.AWAITING_LOCATION);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("📍 Пожалуйста, разрешите доступ к вашей геолокации\n\nНажмите кнопки ниже:");
        LocationKeyboard locationKeyboard = new LocationKeyboard();
        sendMessage.setReplyMarkup(locationKeyboard.createLocationKeyboard());

        try {
            absSender.execute(sendMessage);
            System.out.println("✅ Запрос геолокации отправлен пользователю: " + userId);
        } catch (TelegramApiException e) {
            System.err.println("❌ Ошибка отправки сообщения: " + e.getMessage());
        }

        return sendMessage;
    }

    public Map<String, String> getLocationTriggers() {
        return locationTriggers;
    }


    public String getCityFromCoordinates(double latitude, double longitude) throws Exception {
        YandexMapsService yandexMapsService = new YandexMapsService(yandexMapsRepository, configurationManager.getGeocodeApiKey());
        String city = yandexMapsService.getCityName(longitude, latitude);
        System.out.println("📍 Определен город по координатам " + latitude + ", " + longitude + ": " + city);
        return city;
    }



    public String getCityLandmarks(String city) throws Exception {
        System.out.println("🏛️ Получение достопримечательностей для города: " + city);
        YandexMapsService yandexMapsService = new YandexMapsService(yandexMapsRepository, configurationManager.getSuggestApiKey());
        String landmarks = yandexMapsService.getLandmarks(city);
        System.out.println("✅ Получены достопримечательности: " + landmarks);
        return landmarks;
    }
}