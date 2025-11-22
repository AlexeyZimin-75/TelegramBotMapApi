package org.example.handlers;

import org.example.commands.Command;
import org.example.commands.HelpCommand;
import org.example.commands.StartCommand;
import org.example.commands.GetLocationCommand;
import org.example.commands.GetDateOfStartCommand;
import org.example.commands.GetDateOfEndCommand;
import org.example.service.UserStateService;
import org.example.service.UserDataService;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final UserDataService userDataService;
    private final UserStateService userStateService;
    private final Map<String, Command> commands;
    private final Map<String, String> commandTriggers;

    public CommandManager(UserStateService userStateService, UserDataService userDataService) {
        this.userStateService = userStateService;
        this.userDataService = userDataService;
        this.commands = new HashMap<>();
        this.commandTriggers = new HashMap<>();
        registerCommands();
    }

    private void registerCommands() {
        StartCommand startCommand = new StartCommand(userStateService, userDataService);
        HelpCommand helpCommand = new HelpCommand(commands);
        GetLocationCommand getLocationCommand = new GetLocationCommand(userStateService, userDataService);
        GetDateOfStartCommand dateOfStartCommand = new GetDateOfStartCommand(userStateService, userDataService);
        GetDateOfEndCommand dateOfEndCommand = new GetDateOfEndCommand(userStateService, userDataService);

        // Регистрируем команды
        commands.put(startCommand.getCommandName(), startCommand);
        commands.put(helpCommand.getCommandName(), helpCommand);
        commands.put(getLocationCommand.getCommandName(), getLocationCommand);
        commands.put(dateOfStartCommand.getCommandName(), dateOfStartCommand);
        commands.put(dateOfEndCommand.getCommandName(), dateOfEndCommand);

        // Собираем триггеры для быстрого доступа
        commandTriggers.putAll(getLocationCommand.getLocationTriggers());
        commandTriggers.putAll(helpCommand.getHelpTriggers());
        commandTriggers.putAll(startCommand.getStartTriggers());

        System.out.println("✅ Зарегистрированы команды: " + commands.keySet());
        System.out.println("✅ Зарегистрированы триггеры: " + commandTriggers.keySet());
    }

    public Command getCommand(String commandName) {
        return commands.get(commandName);
    }

    public String getCommandByTrigger(String trigger) {
        return commandTriggers.get(trigger);
    }

    public boolean isCommand(String text) {
        return text.startsWith("/") || commandTriggers.containsKey(text);
    }

    public Map<String, Command> getCommands() {
        return new HashMap<>(commands);
    }
}