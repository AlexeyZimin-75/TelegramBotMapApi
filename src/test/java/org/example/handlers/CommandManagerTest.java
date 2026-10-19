package org.example.handlers;

import org.example.commands.*;
import org.example.service.UserStateService;
import org.example.service.UserDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CommandManagerTest {

    private UserStateService userStateService;
    private UserDataService userDataService;
    private CommandManager commandManager;

    // Простые реализации для тестирования
    static class TestUserStateService extends UserStateService {
        public TestUserStateService() {
            super();
        }
    }

    static class TestUserDataService extends UserDataService {
        public TestUserDataService() {
            super();
        }
    }

    @BeforeEach
    void setUp() {
        userStateService = new TestUserStateService();
        userDataService = new TestUserDataService();
        commandManager = new CommandManager(userStateService, userDataService);
    }

    @Test
    void testConstructor_RegistersCommands() {
        Map<String, Command> commands = commandManager.getCommands();

        assertNotNull(commands);
        assertFalse(commands.isEmpty());
        assertTrue(commands.containsKey("start"));
        assertTrue(commands.containsKey("help"));
    }

    @Test
    void testGetCommand_WithExistingCommand_ReturnsCommand() {
        Command command = commandManager.getCommand("start");
        assertNotNull(command);
        assertInstanceOf(StartCommand.class, command);
    }

    @Test
    void testGetCommand_WithNonExistingCommand_ReturnsNull() {
        Command command = commandManager.getCommand("non_existing_command");
        assertNull(command);
    }

    @Test
    void testIsCommand_WithSlashCommand_ReturnsTrue() {
        assertTrue(commandManager.isCommand("/start"));
    }

    @Test
    void testIsCommand_WithNonCommandText_ReturnsFalse() {
        assertFalse(commandManager.isCommand("просто текст"));
    }

    @Test
    void testGetCommands_ReturnsCopyNotOriginal() {
        Map<String, Command> commands1 = commandManager.getCommands();
        Map<String, Command> commands2 = commandManager.getCommands();

        assertNotSame(commands1, commands2);
        assertEquals(commands1.size(), commands2.size());
    }
}