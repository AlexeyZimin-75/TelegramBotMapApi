package org.example.commands;

import org.example.keyboards.StartKeyboard;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class HelpCommand implements Command {

    // Карта с зарегистрированными командами для показа в справке
    private final Map<String, Command> commands;
    private Map<String,String> helpTriggers;


    public HelpCommand(Map<String, Command> commands) {
        this.commands = commands;
        helpTriggers = new HashMap<>();
        helpTriggers.put("🤖 Что умеет наш бот","/help");
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Показать список всех команд";
    }

    @Override
    public SendMessage execute(AbsSender absSender, Message message) {
        SendMessage response = new SendMessage();
        response.setChatId(message.getChatId().toString());

        // Создаем текст справки
        StringBuilder helpText = new StringBuilder();


        helpText.append("\nБот помогает построить маршрут до заданного города," +
                " для начала вам следует отправить геолокацию, затем выполняйте последующие команды" +
                "бота, удачного использования");
        response.setText(helpText.toString());
        ReplyKeyboardMarkup replyKeyboardMarkup = new StartKeyboard().createStartKeyboard();
        response.setReplyMarkup(replyKeyboardMarkup);

        try {
            absSender.execute(response);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки справки: " + e.getMessage());
        }
        return response;
    }

    public Map<String, String> getHelpTriggers() {
        return helpTriggers;
    }
}