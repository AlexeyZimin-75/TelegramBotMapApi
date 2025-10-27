package org.example.handlers;

import org.example.commands.Command;
import org.example.commands.HelpCommand;
import org.example.commands.StartCommand;
import org.example.commands.GetLocationCommand;
import org.example.service.UserStateService;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();
    private final Map<String, String> commandTriggers = new HashMap<>();

    public CommandManager(UserStateService userStateService) {
        registerCommands(userStateService);
    }

    private void registerCommands(UserStateService userStateService) {
        StartCommand startCommand = new StartCommand(userStateService);
        HelpCommand helpCommand = new HelpCommand(commands);
        GetLocationCommand getLocationCommand = new GetLocationCommand(userStateService);

        commands.put(startCommand.getCommandName(), startCommand);
        commands.put(helpCommand.getCommandName(), helpCommand);
        commands.put(getLocationCommand.getCommandName(), getLocationCommand);

        commandTriggers.putAll(getLocationCommand.getLocationTriggers());
        commandTriggers.putAll(helpCommand.getHelpTriggers());
        commandTriggers.putAll(startCommand.getStartTriggers());

        System.out.println("Зарегистрированы команды: " + commands.keySet());
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