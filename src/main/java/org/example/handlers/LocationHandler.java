package org.example.handlers;

import org.example.commands.GetLocationCommand;
import org.example.service.UserStateService;
import org.example.states.UserState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class LocationHandler {
    private final UserStateService userStateService;
    private final GetLocationCommand getLocationCommand;

    public LocationHandler(UserStateService userStateService) {
        this.userStateService = userStateService;
        this.getLocationCommand = new GetLocationCommand(userStateService);
    }

    public void handleLocation(Long userId, Long chatId, Location location, AbsSender absSender) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        try {
            String city = getLocationCommand.getCityFromCoordinates(latitude, longitude);
            handleSuccessfulCityDetection(userId, chatId, city, absSender);
        } catch (Exception e) {
            handleCityDetectionError(userId, chatId, e, absSender);
        }
    }

    public void handleTextLocation(Long userId, Long chatId, String text,
                                   UserState currentState, AbsSender absSender) {
        switch (currentState) {
            case AWAITING_LOCATION:
                if (text.equals("\uD83D\uDC49 ввести город вручную")) {
                    handleTextCityRequest(userId, chatId, absSender);
                } else {
                    handleTextCityInput(userId, chatId, text, absSender);
                }
                break;
            case AWAITING_MANUAL_CITY:
                handleTextCityInput(userId, chatId, text, absSender);
                break;
        }
    }

    private void handleTextCityRequest(Long userId, Long chatId, AbsSender absSender) {
        userStateService.setUserState(userId, UserState.AWAITING_MANUAL_CITY);
        sendMessage(chatId, "🏙️ Пожалуйста, введите название вашего города:", absSender);
    }

    private void handleTextCityInput(Long userId, Long chatId, String cityName, AbsSender absSender) {
        try {
            // Здесь должна быть проверка существования города через API
            handleSuccessfulCityDetection(userId, chatId, cityName, absSender);
        } catch (Exception e) {
            handleCityInputError(chatId, absSender);
        }
    }

    private void handleSuccessfulCityDetection(Long userId, Long chatId, String city, AbsSender absSender) {
        userStateService.getUserData(userId).setCurrentCity(city);
        userStateService.setUserState(userId, UserState.AWAITING_DESTINATION_CITY);

        String message = "📍 Отлично! Ваш город: " + city +
                "\n\nТеперь введите город, в который хотите поехать:";
        sendMessage(chatId, message, absSender);
    }

    private void handleCityDetectionError(Long userId, Long chatId, Exception error, AbsSender absSender) {
        System.err.println("❌ Ошибка при определении города по координатам: " + error.getMessage());
        userStateService.setUserState(userId, UserState.AWAITING_MANUAL_CITY);
        sendMessage(chatId, "❌ Не удалось определить город по вашим координатам. Пожалуйста, введите ваш город вручную:", absSender);
    }

    private void handleCityInputError(Long chatId, AbsSender absSender) {
        System.err.println("Ошибка ввода города (скорее всего такого не существует)");
        sendMessage(chatId, "❌ Произошла ошибка при ручном вводе города. " +
                "Попробуйте ввести снова или воспользуйтесь автоматическим вводом геолокации", absSender);
    }

    private void sendMessage(Long chatId, String text, AbsSender absSender) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());
        response.setText(text);

        try {
            absSender.execute(response);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки сообщения: " + e.getMessage());
        }
    }
}