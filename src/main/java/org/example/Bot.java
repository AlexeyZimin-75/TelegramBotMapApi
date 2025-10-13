package org.example;


import org.example.commands.GetLocationCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


public class Bot extends TelegramLongPollingBot {


    private final MessageHandler messageHandler;
    private final String BOT_TOKEN = "8211999191:AAELFsGpVUN16YW70M0UDxu4MgVPSuAXSD8";
    private final String BOT_USERNAME = "TouristsBot";


    public Bot() {
        super("8211999191:AAELFsGpVUN16YW70M0UDxu4MgVPSuAXSD8");
        this.messageHandler = new MessageHandler();
    }


    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasLocation()){
            GetLocationCommand getLocationCommand = new GetLocationCommand();
            try {
                getLocationCommand.showDataOfLocation(update,this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
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