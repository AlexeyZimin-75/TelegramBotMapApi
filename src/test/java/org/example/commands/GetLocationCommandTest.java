package org.example.commands;

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
import static org.mockito.Mockito.*;

class GetLocationCommandTest {
    private GetLocationCommand command;

    @Mock
    private UserStateService userStateService;

    @Mock
    private AbsSender absSender;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    @Mock
    private SendMessage sendMessage;

    @Mock
    private UserDataService userDataService;

    @Mock
    private User user;


    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        command = new GetLocationCommand(userStateService,userDataService);
    }

    @Test
    void getCommandName() {
        assertEquals("location",command.getCommandName());
    }

    @Test
    void getDescription() {
        assertEquals("Определить мой город по геолокации",command.getDescription());
    }

    @Test
    void execute() {
        when(user.getId()).thenReturn(12345L);
        when(chat.getId()).thenReturn(67890L);
        when(message.getFrom()).thenReturn(user);
        when(message.getChatId()).thenReturn(67890L);
        when(message.getChat()).thenReturn(chat);
        String expectedMessage = "📍 Пожалуйста, разрешите доступ к вашей геолокации\n\nНажмите кнопки ниже:";

        sendMessage = command.execute(absSender,message);
        assertEquals(expectedMessage,sendMessage.getText());

    }




}