package org.example;

import org.example.handlers.MessageProcessor;
import org.example.service.UserStateService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.InputStream;
import java.util.Properties;

public class Bot extends TelegramLongPollingBot {
    private final MessageProcessor messageProcessor;
    private final String BOT_TOKEN;
    private final String BOT_USERNAME;

    public Bot() {
        this.BOT_TOKEN = loadTokenFromConfig();
        this.BOT_USERNAME = loadUsernameFromConfig();
        this.messageProcessor = new MessageProcessor(new UserStateService());
    }

    private static String loadTokenFromConfig() {
        try {
            Properties prop = new Properties();
            InputStream input = Bot.class.getClassLoader().getResourceAsStream("application.properties");
            prop.load(input);
            return prop.getProperty("bot.token");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load token", e);
        }
    }

    private static String loadUsernameFromConfig() {
        try {
            Properties prop = new Properties();
            InputStream input = Bot.class.getClassLoader().getResourceAsStream("application.properties");
            prop.load(input);
            return prop.getProperty("bot.username");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load username", e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        messageProcessor.processUpdate(update, this);
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}