package org.example.handlers;

import org.example.commands.GetLocationCommand;
import org.example.keyboards.LastKeyboard;
import org.example.service.UserStateService;
import org.example.states.UserState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class RouteHandler {
    private final UserStateService userStateService;
    private final GetLocationCommand getLocationCommand;

    public RouteHandler(UserStateService userStateService) {
        this.userStateService = userStateService;
        this.getLocationCommand = new GetLocationCommand(userStateService);
    }

    public void handleDestinationCity(Message message, AbsSender absSender) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        String destinationCity = message.getText();

        // Сохраняем город назначения
        userStateService.getUserData(userId).setDestinationCity(destinationCity);
        userStateService.setUserState(userId, UserState.READY_TO_SEARCH);

        String currentCity = userStateService.getUserData(userId).getCurrentCity();
        sendRouteInfo(chatId, currentCity, destinationCity, absSender);
    }

    private void sendRouteInfo(Long chatId, String currentCity, String destinationCity, AbsSender absSender) {
        try {
            String landmarks = getLocationCommand.getCityLandmarks(destinationCity);
            String routeInfo = buildRouteMessage(currentCity, destinationCity, landmarks);
            sendMessageWithKeyboard(chatId, routeInfo, absSender);
        } catch (Exception e) {
            System.err.println("❌ Ошибка при получении достопримечательностей: " + e.getMessage());
            // Отправляем сообщение без достопримечательностей
            String fallbackMessage = buildFallbackRouteMessage(currentCity, destinationCity);
            sendMessageWithKeyboard(chatId, fallbackMessage, absSender);
        }
    }

    private String buildRouteMessage(String currentCity, String destinationCity, String landmarks) {
        return String.format(
                "🎉 Отлично! Маршрут построен:\n📍 От: %s\n🎯 До: %s\n\n🏛️ Вот некоторые достопримечательности города %s:\n%s",
                currentCity, destinationCity, destinationCity, landmarks
        );
    }

    private String buildFallbackRouteMessage(String currentCity, String destinationCity) {
        return String.format(
                "🎉 Отлично! Маршрут построен:\n📍 От: %s\n🎯 До: %s\n\nТеперь вы можете искать места для посещения в %s!",
                currentCity, destinationCity, destinationCity
        );
    }

    private void sendMessageWithKeyboard(Long chatId, String text, AbsSender absSender) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());
        response.setText(text);
        response.setReplyMarkup(new LastKeyboard().createStartKeyboard());

        try {
            absSender.execute(response);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки сообщения: " + e.getMessage());
        }
    }
}