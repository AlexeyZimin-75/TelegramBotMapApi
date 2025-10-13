package org.example;

import org.example.commnads.HelpCommand;
import org.example.commnads.StartCommand;
import org.example.commnads.Command;
import org.example.commnads.GetLocationCommand;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.HashMap;
import java.util.Map;


public class MessageHandler {

    // Карта для хранения всех команд бота (ключ - имя команды, значение - объект команды)
    private final Map<String, Command> commands = new HashMap<>();


    public MessageHandler() {
        registerCommands();
    }


    private void registerCommands() {

        Command startCommand = new StartCommand();
        commands.put(startCommand.getCommandName(), startCommand);

        Command helpCommand = new HelpCommand(commands);
        commands.put(helpCommand.getCommandName(), helpCommand);

        Command getLocationCommand = new GetLocationCommand();
        commands.put(getLocationCommand.getCommandName(),getLocationCommand);


        System.out.println("Зарегистрированы команды: " + commands.keySet());
    }

    public void handleLocation(Update update, AbsSender absSender){
        // из update можем получить координаты и уже работать с ним и если нужно отправить какое-то сообщение
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

    public Map<String, Command> getCommands() {
        return commands;
    }
}