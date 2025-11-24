package org.example.handlers;

import org.example.commands.TextCommand.GetEvents;
import org.example.commands.GetLocationCommand;
import org.example.keyboards.LastKeyboard;
import org.example.service.UserData;
import org.example.service.UserStateService;
import org.example.service.UserDataService;
import org.example.states.UserState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.example.DataBaseManager;

public class RouteHandler {
    private final UserStateService userStateService;
    private final UserDataService userDataService;
    private final GetLocationCommand getLocationCommand;
    private final DataBaseManager dataBaseManager;

    public RouteHandler(UserStateService userStateService, UserDataService userDataService) {
        this.userStateService = userStateService;
        this.userDataService = userDataService;
        this.getLocationCommand = new GetLocationCommand(userStateService, userDataService);
        this.dataBaseManager = new DataBaseManager();
    }

    public void handleDestinationCity(Message message, AbsSender absSender) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        String destinationCity = message.getText().trim();

        System.out.println("🎯 Сохраняем город назначения: " + destinationCity + " для пользователя: " + userId);

        // Сохраняем город назначения
        UserData userData = userDataService.getUserData(userId);
        userData.setDestinationCity(destinationCity);

        // Логируем текущие данные
        System.out.println("📊 Данные пользователя после сохранения destination:");
        System.out.println("   - currentCity: " + userData.getCurrentCity());
        System.out.println("   - destinationCity: " + userData.getDestinationCity());

        // Переходим к запросу даты прибытия
        userStateService.setUserState(userId, UserState.AWAITING_ARRIVAL_DATE_RESPONSE);

        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());
        response.setText("✅ Город назначения сохранен: " + destinationCity +
                "\n\nТеперь укажите дату прибытия в формате дд.мм.гггг:");

        try {
            absSender.execute(response);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки сообщения: " + e.getMessage());
        }
    }

    // Метод для отправки финальной информации о маршруте (после ввода всех дат)
    public void sendFinalRouteInfo(Long userId, Long chatId, AbsSender absSender) {
        UserData userData = userDataService.getUserData(userId);
        GetEvents getEvents = new GetEvents(userStateService,userDataService);

        // Логируем все данные перед отправкой
        System.out.println("📊 ФИНАЛЬНЫЕ ДАННЫЕ ПОЛЬЗОВАТЕЛЯ:");
        System.out.println("   - currentCity: " + userData.getCurrentCity());
        System.out.println("   - destinationCity: " + userData.getDestinationCity());
        System.out.println("   - departureDate: " + userData.getDepartureDate());
        System.out.println("   - arrivalDate: " + userData.getArrivalDate());

        try {
            dataBaseManager.saveUser(userId); // Сохраняем пользователя если его нет
            dataBaseManager.saveRoute(userId, userData); // Сохраняем маршрут
            System.out.println("💾 Маршрут сохранен в базу данных для пользователя: " + userId);
        } catch (Exception e) {
            System.err.println("❌ Ошибка при сохранении маршрута в БД: " + e.getMessage());
        }

        try {
            String currentCity = userData.getCurrentCity();
            String destinationCity = userData.getDestinationCity();
            String departureDate = userData.getDepartureDate();
            String arrivalDate = userData.getArrivalDate();

            // Проверяем, что все данные есть
            if (destinationCity == null) {
                sendMessageWithKeyboard(chatId, "❌ Ошибка: город назначения не указан", absSender);
                return;
            }

            String landmarks = getLocationCommand.getCityLandmarks(destinationCity);
            String routeInfo = buildFinalRouteMessage(currentCity, destinationCity, departureDate, arrivalDate, landmarks);
            String eventsInfo = getEvents.execute(userId);

            String fullMessage = routeInfo + "\n\n" + eventsInfo;
            sendMessageWithKeyboard(chatId, fullMessage, absSender);
        } catch (Exception e) {
            System.err.println("❌ Ошибка при получении достопримечательностей: " + e.getMessage());
            // Отправляем сообщение без достопримечательностей
            UserData userDataFallback = userDataService.getUserData(userId);
            String fallbackMessage = buildFallbackRouteMessage(
                    userDataFallback.getCurrentCity(),
                    userDataFallback.getDestinationCity(),
                    userDataFallback.getDepartureDate(),
                    userDataFallback.getArrivalDate()
            );

            sendMessageWithKeyboard(chatId, fallbackMessage, absSender);
        }
    }

    private String buildFinalRouteMessage(String currentCity, String destinationCity, String departureDate, String arrivalDate, String landmarks) {
        // Защита от null значений
        currentCity = currentCity != null ? currentCity : "не указан";
        destinationCity = destinationCity != null ? destinationCity : "не указан";
        departureDate = departureDate != null ? departureDate : "не указана";
        arrivalDate = arrivalDate != null ? arrivalDate : "не указана";

        return String.format(
                "🎉 Отлично! Все данные сохранены:\n\n" +
                        "📍 От: %s\n" +
                        "🎯 До: %s\n" +
                        "📅 Дата отправления: %s\n" +
                        "📅 Дата прибытия: %s\n\n" +
                        "🏛️ Вот некоторые достопримечательности города %s:\n%s\n\n" +
                        "Теперь вы можете искать билеты и места для посещения!",
                currentCity, destinationCity, departureDate, arrivalDate, destinationCity, landmarks
        );
    }

    private String buildFallbackRouteMessage(String currentCity, String destinationCity, String departureDate, String arrivalDate) {
        // Защита от null значений
        currentCity = currentCity != null ? currentCity : "не указан";
        destinationCity = destinationCity != null ? destinationCity : "не указан";
        departureDate = departureDate != null ? departureDate : "не указана";
        arrivalDate = arrivalDate != null ? arrivalDate : "не указана";

        return String.format(
                "🎉 Отлично! Все данные сохранены:\n\n" +
                        "📍 От: %s\n" +
                        "🎯 До: %s\n" +
                        "📅 Дата отправления: %s\n" +
                        "📅 Дата прибытия: %s\n\n" +
                        "Теперь вы можете искать билеты и места для посещения в %s!",
                currentCity, destinationCity, departureDate, arrivalDate, destinationCity
        );
    }


    private void sendMessageWithKeyboard(Long chatId, String text, AbsSender absSender) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());
        response.setText(text);
        response.setReplyMarkup(new LastKeyboard().createStartKeyboard());
        response.disableWebPagePreview();

        try {
            absSender.execute(response);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки сообщения: " + e.getMessage());
        }
    }
}