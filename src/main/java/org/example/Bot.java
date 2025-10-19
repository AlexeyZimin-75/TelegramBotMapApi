package org.example;

import org.example.commands.GetLocationCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.InputStream;
import java.util.Properties;

public class Bot extends TelegramLongPollingBot {

    private final String BOT_TOKEN;
    private final String BOT_USERNAME;
    private final MessageHandler messageHandler;

    public Bot() {
        super(loadBotProperties().getProperty("telegram-bot.token"));
        Properties props = loadBotProperties();
        this.BOT_TOKEN = props.getProperty("telegram-bot.token");
        this.BOT_USERNAME = props.getProperty("telegram-bot.username");
        this.messageHandler = new MessageHandler();
    }

    private static Properties loadBotProperties() {
        try (InputStream input = Bot.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("application.properties not found in classpath");
            }

            Properties prop = new Properties();
            prop.load(input);

            validateProperty(prop, "telegram-bot.token");
            validateProperty(prop, "telegram-bot.username");

            return prop;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load bot properties from application.properties", e);
        }
    }

    private static void validateProperty(Properties prop, String key) {
        String value = prop.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException(key + " not found or empty in application.properties");
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasLocation()){
            GetLocationCommand getLocationCommand = new GetLocationCommand();
            try {
                getLocationCommand.showDataOfLocation(update, this);
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
