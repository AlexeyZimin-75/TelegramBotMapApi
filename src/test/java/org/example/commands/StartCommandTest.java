package org.example.commands;

import org.example.service.UserStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartCommandTest {

    private Command command;

    @Mock
    private UserStateService userStateService;

    @Mock
    private AbsSender absSender;

    @Mock
    private Message message;

    @Mock
    private User user;

    @Mock
    private Chat chat;

    @BeforeEach
    void setUp(){
        command = new StartCommand(userStateService);
    }


    @Test
    void getCommandName() {
        assertEquals("start",command.getCommandName());
    }

    @Test
    void getDescription() {
        assertEquals("Запустить бот и получить приветствие",command.getDescription());
    }

    @Test
    void executeCheckReturnableClass() {
        when(message.getFrom()).thenReturn(user);
        when(user.getId()).thenReturn(123L);

        Object result = command.execute(absSender, message);
        assertEquals(SendPhoto.class, result.getClass());
    }

    @Test
    void executeCheckPhotoLink(){
        when(message.getFrom()).thenReturn(user);
        when(user.getId()).thenReturn(1L);

        SendPhoto sendPhoto = (SendPhoto) command.execute(absSender,message);
        assertEquals("https://media.easemytrip.com/media/Blog/International/637007769287754861/637007769287754861GltpKG.jpg",
                sendPhoto.getPhoto().getAttachName());
    }

    @Test
    void executeCheckText(){
        when(message.getFrom()).thenReturn(user);
        when(user.getId()).thenReturn(1L);

        SendPhoto sendPhoto = (SendPhoto) command.execute(absSender,message);
        String expectesCaption = "️Добро пожаловать в туристический бот! 🗺️\n" +
                "Я помогу тебе построить путь в лучшие места для путешествий!\n\n" +
                "Доступные команды:\n" +
                "/start - начать работу\n" +
                "/help - информация о боте\n" +
                "/location - узнать информацию о твоем текущем местоположении";
        assertEquals(expectesCaption,sendPhoto.getCaption());
    }


}