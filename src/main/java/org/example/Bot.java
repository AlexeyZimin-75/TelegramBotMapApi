package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {


    private final MessageHandler messageHandler;
    private final String BOT_TOKEN = "TOKEN";
    private final String BOT_USERNAME = "TouristsBot";


    public Bot() {
        super("TOKEN");
        this.messageHandler = new MessageHandler();
    }


    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasLocation()){
            messageHandler.handleLocation(update,this);
        }
        else if (update.hasMessage() && update.getMessage().hasText()) {
            messageHandler.handleMessage(update.getMessage(), this);
        }
    }


    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }
}