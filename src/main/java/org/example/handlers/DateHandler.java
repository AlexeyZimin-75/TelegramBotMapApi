package org.example.handlers;

import org.example.service.UserStateService;
import org.example.service.UserDataService;
import org.example.states.UserState;
import org.example.service.UserData;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateHandler {
    private final UserStateService userStateService;
    private final UserDataService userDataService;
    private final RouteHandler routeHandler;
    private final DateTimeFormatter dateFormatter;

    public DateHandler(UserStateService userStateService, UserDataService userDataService) {
        this.userStateService = userStateService;
        this.userDataService = userDataService;
        this.routeHandler = new RouteHandler(userStateService, userDataService);
        this.dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    }

    public void handleArrivalDate(Message message, AbsSender absSender) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        String dateText = message.getText().trim();

        if (isValidDate(dateText)) {
            // Сохраняем дату прибытия
            userDataService.getUserData(userId).setArrivalDate(dateText);

            // Переходим к запросу даты отправления
            userStateService.setUserState(userId, UserState.AWAITING_DEPARTURE_DATE_RESPONSE);

            sendMessage(absSender, chatId, "✅ Дата прибытия сохранена. Теперь укажите дату отправления в формате дд.мм.гггг:");
        } else {
            sendMessage(absSender, chatId, "❌ Неверный формат даты. Пожалуйста, укажите дату в формате дд.мм.гггг:");
        }
    }

    public void handleDepartureDate(Message message, AbsSender absSender) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        String dateText = message.getText().trim();

        if (isValidDate(dateText)) {
            // Сохраняем дату отправления
            userDataService.getUserData(userId).setDepartureDate(dateText);

            // Проверяем логику дат (дата отправления должна быть раньше даты прибытия)
            if (areDatesValid(userId)) {
                userStateService.setUserState(userId, UserState.READY_TO_SEARCH);

                // Отправляем финальное сообщение с информацией о маршруте
                routeHandler.sendFinalRouteInfo(userId, chatId, absSender);
            } else {
                sendMessage(absSender, chatId, "❌ Дата отправления должна быть раньше даты прибытия. Пожалуйста, укажите корректные даты.");
                sendMessage(absSender, chatId, "Укажите дату отправления в формате дд.мм.гггг:");
            }
        } else {
            sendMessage(absSender, chatId, "❌ Неверный формат даты. Пожалуйста, укажите дату в формате дд.мм.гггг:");
        }
    }

    private boolean isValidDate(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr, dateFormatter);
            // Проверяем, что дата не в прошлом
            LocalDate today = LocalDate.now();
            return !date.isBefore(today.minusDays(1)); // Разрешаем сегодняшнюю дату
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean areDatesValid(Long userId) {
        UserData userData = userDataService.getUserData(userId);
        try {
            LocalDate departure = LocalDate.parse(userData.getDepartureDate(), dateFormatter);
            LocalDate arrival = LocalDate.parse(userData.getArrivalDate(), dateFormatter);
            return departure.isBefore(arrival);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void sendMessage(AbsSender absSender, Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки сообщения: " + e.getMessage());
        }
    }
}