package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class BotStarter {


    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            Bot bot = new Bot();
            botsApi.registerBot(bot);


            System.out.println("✅ Бот успешно запущен и готов к работе!");
            System.out.println("🤖 Имя бота: " + bot.getBotUsername());

        } catch (TelegramApiException e) {
            System.err.println("❌ Ошибка при запуске бота: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
