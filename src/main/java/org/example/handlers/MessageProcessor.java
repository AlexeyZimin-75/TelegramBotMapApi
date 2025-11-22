package org.example.handlers;

import org.example.service.UserDataService;
import org.example.commands.Command;
import org.example.service.UserStateService;
import org.example.states.UserState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageProcessor {
    private final CommandManager commandManager;
    private final LocationHandler locationHandler;
    private final RouteHandler routeHandler;
    private final UserStateService userStateService;
    private final DateHandler dateHandler;
    private final UserDataService userDataService;// Добавляем единый экземпляр

    public MessageProcessor(UserStateService userStateService) {
        this.userStateService = userStateService;
        this.userDataService = new UserDataService();
        this.commandManager = new CommandManager(userStateService, userDataService);
        this.locationHandler = new LocationHandler(userStateService, userDataService);
        this.routeHandler = new RouteHandler(userStateService, userDataService);
        this.dateHandler = new DateHandler(userStateService, userDataService);
    }

    public void processUpdate(Update update, AbsSender absSender) {
        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasLocation()) {
                handleLocationMessage(message, absSender);
            } else if (message.hasText()) {
                handleTextMessage(message, absSender);
            }
        }
    }

    private void handleLocationMessage(Message message, AbsSender absSender) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();

        locationHandler.handleLocation(userId, chatId, message.getLocation(), absSender);
    }

    private void handleTextMessage(Message message, AbsSender absSender) {
        String text = message.getText();
        Long userId = message.getFrom().getId();

        String normalizedText = normalizeText(text);

        if (commandManager.isCommand(normalizedText)) {
            executeCommand(normalizedText, message, absSender);
        } else {
            handleUserState(message, absSender);
        }
    }

    private String normalizeText(String text) {
        String commandFromTrigger = commandManager.getCommandByTrigger(text);
        return commandFromTrigger != null ? commandFromTrigger : text;
    }

    private void executeCommand(String text, Message message, AbsSender absSender) {
        String commandName = text.startsWith("/") ? text.substring(1).toLowerCase() : text;
        Command command = commandManager.getCommand(commandName);

        if (command != null) {
            System.out.println("🔧 Выполняется команда: " + text);
            command.execute(absSender, message);
        } else {
            System.out.println("❌ Неизвестная команда: " + text);
            sendUnknownCommand(message, absSender);
        }
    }

    private void handleUserState(Message message, AbsSender absSender) {
        Long userId = message.getFrom().getId();
        UserState currentState = userStateService.getUserState(userId);
        String text = message.getText();

        System.out.println("📝 Обработка текстового сообщения в FSM, состояние: " + currentState);

        switch (currentState) {
            case AWAITING_LOCATION:
            case AWAITING_MANUAL_CITY:
                locationHandler.handleTextLocation(userId, message.getChatId(), text, currentState, absSender);
                break;
            case AWAITING_DESTINATION_CITY:
                routeHandler.handleDestinationCity(message, absSender);
                break;
            case AWAITING_ARRIVAL_DATE_RESPONSE:
                dateHandler.handleArrivalDate(message, absSender);
                break;
            case AWAITING_DEPARTURE_DATE_RESPONSE:
                dateHandler.handleDepartureDate(message, absSender);
                break;
            default:
                // Для других состояний или когда состояние не установлено
                handleDefaultMessage(message, absSender);
                break;
        }
    }

    private void handleDefaultMessage(Message message, AbsSender absSender) {
        SendMessage response = new SendMessage();
        response.setChatId(message.getChatId().toString());
        response.setText("Введите /start для начала работы с ботом");

        try {
            absSender.execute(response);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки сообщения: " + e.getMessage());
        }
    }

    private void sendUnknownCommand(Message message, AbsSender absSender) {
        SendMessage response = new SendMessage();
        response.setChatId(message.getChatId().toString());
        response.setText("❌ Неизвестная команда. Используй /help для списка команд.");

        try {
            absSender.execute(response);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка при отправке сообщения об ошибке: " + e.getMessage());
        }
    }
}