package org.example.commands;


import org.example.apiMethods.YandexMapsClient;
import org.example.service.UserStateService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class GetDateOfEndCommand implements Command{

    private final UserStateService userStateService;
    private final YandexMapsClient yandexMapsClient;
    private Map<String,String> dateTriggers;




    public GetDateOfEndCommand(UserStateService userStateService) {
        this.userStateService = userStateService;
        this.yandexMapsClient = new YandexMapsClient();
        this.dateTriggers = new HashMap<>();
        //dateTriggers.put("\uD83C\uDF0D Указать даты путешествия","/location");

    }


    @Override
    public String getCommandName() {
        return "dateOfEnd";
    }

    @Override
    public String getDescription() {
        return "Узнать дату поездки";
    }

    @Override
    public SendMessage execute(AbsSender absSender, Message message) {
        SendMessage sendMessage =  new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Укажите дату депортации в формате дд.мм.гггг: ");

        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        return sendMessage;
    }
}
