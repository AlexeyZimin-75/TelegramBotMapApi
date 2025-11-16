package org.example.commands;

import org.example.handlers.CommandManager;
import org.example.service.UserDataService;
import org.example.service.UserStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class HelpCommandTest {
    UserStateService userStateService = new UserStateService();
    UserDataService userDataService = new UserDataService();
    CommandManager handler = new CommandManager(userStateService,userDataService);
    HelpCommand helpCommand = new HelpCommand(handler.getCommands());


    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private AbsSender absSender;

    @Mock
    private Message message;

    @Mock
    private User user;

    @Mock
    private Chat chat;

    @Mock
    private SendMessage sendMessage;




    @Test
    void getCommandName() {
        assertEquals("help",helpCommand.getCommandName());
    }

    @Test
    void getDescription() {
        assertEquals("Показать список всех команд",helpCommand.getDescription());
    }

    @Test
    void execute() {
        when(message.getChatId()).thenReturn(12L);

        sendMessage = helpCommand.execute(absSender,message);
        String expectedText = "\nБот помогает построить маршрут до заданного города," +
                " для начала вам следует отправить геолокацию, затем выполняйте последующие команды" +
                "бота, удачного использования";

        assertEquals(expectedText,sendMessage.getText());
    }
}