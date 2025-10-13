package org.example.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Команда /start - приветствие пользователя при запуске бота
 */
public class StartCommand implements Command {

    @Override
    public String getCommandName() {
        return "start"; // Команда вызывается через /start
    }

    @Override
    public String getDescription() {
        return "Запустить бота и получить приветствие";
    }

    @Override
    public void execute(AbsSender absSender, Message message) {
        // Создаем объект для ответного сообщения
        SendMessage response = new SendMessage();

        // Устанавливаем ID чата (куда отправить ответ)
        response.setChatId(message.getChatId().toString());

        // Формируем приветственный текст
        String userName = message.getFrom().getFirstName(); // Имя пользователя
        String text = "👋 Привет, " + userName + "!\n\n" +
                "Я демонстрационный Telegram бот написанный на Java.\n" +
                "Используй /help чтобы увидеть список доступных команд.";

        response.setText(text); // Устанавливаем текст сообщения

        try {
            absSender.execute(response);
        } catch (TelegramApiException e) {

            System.err.println("Ошибка отправки сообщения: " + e.getMessage());
        }
    }
}