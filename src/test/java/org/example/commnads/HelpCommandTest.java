package org.example.commnads;

import org.example.MessageHandler;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HelpCommandTest {
    private MessageHandler messageHandler = new MessageHandler();
    Command command = new HelpCommand(messageHandler.getCommands());


    @Test
    void getCommandName() {
        assertEquals("help",command.getCommandName());
    }

    @Test
    void getDescription() {
        assertEquals("Показать список всех команд",command.getDescription());
    }

    @Test
    void execute() {
    }
}