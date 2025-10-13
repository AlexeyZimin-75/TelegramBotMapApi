package org.example;

import org.example.commands.HelpCommand;
import org.example.commands.StartCommand;
import org.example.commands.Command;
import org.example.commands.GetLocationCommand;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.HashMap;
import java.util.Map;


public class MessageHandler {

    // Карта для хранения всех команд бота (ключ - имя команды, значение - объект команды)
    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Конструктор - регистрирует все команды при создании обработчика
     */
    public MessageHandler() {
        registerCommands();
    }

    /**
     * Регистрирует все команды бота
     */
    private void registerCommands() {

        Command startCommand = new StartCommand();
        commands.put(startCommand.getCommandName(), startCommand);

        Command helpCommand = new HelpCommand(commands);
        commands.put(helpCommand.getCommandName(), helpCommand);

        Command getLocationCommand = new GetLocationCommand();
        commands.put(getLocationCommand.getCommandName(),getLocationCommand);


        System.out.println("Зарегистрированы команды: " + commands.keySet());
    }


    public void handleMessage(Message message, AbsSender absSender) {
        String text = message.getText();


        if (text.startsWith("/")) {

            String commandName = text.substring(1).toLowerCase();


            Command command = commands.get(commandName);

            if (command != null) {

                command.execute(absSender, message);
            }
            else {

                sendUnknownCommand(message, absSender);
            }
        }


    }

    private void sendUnknownCommand(Message message, AbsSender absSender) {
        try {
            SendMessage response = new SendMessage();
            response.setChatId(message.getChatId().toString());
            response.setText("❌ Неизвестная команда. Используй /help для списка команд.");
            absSender.execute(response);
        } catch (Exception e) {
            System.err.println("Ошибка при отправке сообщения об ошибке: " + e.getMessage());
        }
    }

}