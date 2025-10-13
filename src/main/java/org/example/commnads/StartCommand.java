package org.example.commnads;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class StartCommand implements Command {

    @Override
    public String getCommandName() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Запустить бота и получить приветствие";
    }

    @Override
    public void execute(AbsSender absSender, Message message) {
        SendMessage response = new SendMessage();


        response.setChatId(message.getChatId().toString());


        String userName = message.getFrom().getFirstName();
        String text = "👋 Привет, " + userName + "!\n\n" +
                "Я демонстрационный Telegram бот написанный на Java.\n" +
                "Используй /help чтобы увидеть список доступных команд.";

        response.setText(text);

        try {
            absSender.execute(response);
        } catch (TelegramApiException e) {

            System.err.println("Ошибка отправки сообщения: " + e.getMessage());
        }
    }
}