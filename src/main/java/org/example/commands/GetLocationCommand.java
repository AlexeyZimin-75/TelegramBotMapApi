package org.example.commands;

import org.example.keyboards.LocationKeyboard;
import org.example.apiMethods.YandexMapsClient;
import org.example.service.UserDataService;
import org.example.service.UserStateService;
import org.example.states.UserState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;


public class GetLocationCommand implements Command {

    private final UserStateService userStateService;
    private final YandexMapsClient yandexMapsClient;
    private Map<String,String> locationTriggers;




    public GetLocationCommand(UserStateService userStateService, UserDataService userDataService) {
        this.userStateService = userStateService;
        this.yandexMapsClient = new YandexMapsClient();
        this.locationTriggers = new HashMap<>();
        locationTriggers.put("\uD83C\uDF0D Проложить маршрут","/location");
        //locationTriggers.put("🗺️ Создать новый маршрут 🏛️","/location");
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
        org.example.apiMethods.YandexMapsClient yandexMapsClient = new org.example.apiMethods.YandexMapsClient();
        String city = yandexMapsClient.getCityName(longitude, latitude);
        System.out.println("📍 Определен город по координатам " + latitude + ", " + longitude + ": " + city);
        return city;
    }
    public String getCityLandmarks(String city) throws Exception {
        System.out.println("🏛️ Получение достопримечательностей для города: " + city);
        String landmarks = yandexMapsClient.getLandmarks(city);
        System.out.println("✅ Получены достопримечательности: " + landmarks);
        return landmarks;
    }
}