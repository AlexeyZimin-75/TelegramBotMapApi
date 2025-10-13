package org.example.commnads;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

public class HelpCommand implements Command {

    // Карта с зарегистрированными командами для показа в справке
    private final Map<String, Command> commands;


    public HelpCommand(Map<String, Command> commands) {
        this.commands = commands;
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
    public void execute(AbsSender absSender, Message message) {
        SendMessage response = new SendMessage();
        response.setChatId(message.getChatId().toString());


        StringBuilder helpText = new StringBuilder();
        helpText.append("📚 Доступные команды:\n\n");


        for (Command command : commands.values()) {
            helpText.append("/")
                    .append(command.getCommandName())
                    .append(" - ")
                    .append(command.getDescription())
                    .append("\n");
        }

        helpText.append("\nВыбери команду и отправь её боту!");
        response.setText(helpText.toString());

        try {
            absSender.execute(response);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки справки: " + e.getMessage());
        }
    }
}