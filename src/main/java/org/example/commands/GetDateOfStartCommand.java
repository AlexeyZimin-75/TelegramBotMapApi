package org.example.commands;

import org.example.service.UserStateService;
import org.example.service.UserDataService;
import org.example.service.UserData;
import org.example.states.UserState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class GetDateOfStartCommand implements Command {
    private final UserStateService userStateService;
    private final UserDataService userDataService;

    public GetDateOfStartCommand(UserStateService userStateService, UserDataService userDataService) {
        this.userStateService = userStateService;
        this.userDataService = userDataService;
    }

    @Override
    public String getCommandName() {
        return "dateOfStart";
    }

    @Override
    public String getDescription() {
        return "Указать дату отправления";
    }

    @Override
    public SendMessage execute(AbsSender absSender, Message message) {
        Long userId = message.getFrom().getId();

        // Проверяем, есть ли город назначения
        UserData userData = userDataService.getUserData(userId);
        if (userData.getDestinationCity() == null || userData.getDestinationCity().isEmpty()) {
            SendMessage errorMessage = new SendMessage();
            errorMessage.setChatId(message.getChatId().toString());
            errorMessage.setText("❌ Сначала укажите город назначения с помощью команды /setdestination");

            try {
                absSender.execute(errorMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            return errorMessage;
        }

        userStateService.setUserState(userId, UserState.AWAITING_DEPARTURE_DATE_RESPONSE);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Укажите дату отправления в формате дд.мм.гггг:");

        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        return sendMessage;
    }

    public boolean checkValidDate(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate date = LocalDate.parse(dateStr, formatter);
            // Проверяем, что дата не в прошлом
            LocalDate today = LocalDate.now();
            return !date.isBefore(today.minusDays(1));
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}