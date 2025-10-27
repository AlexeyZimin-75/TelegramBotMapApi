package org.example;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static org.mockito.Mockito.*;

public class MessageHandlerTest {

    @Test
    void handleLocation() {
        // Создаем моки
        Update update = mock(Update.class);
        AbsSender absSender = mock(AbsSender.class);
        MessageHandler messageHandler = new MessageHandler();

        // Создаем мок сообщения
        Message message = mock(Message.class);
        User user = mock(User.class);

        // Настраиваем поведение моков:
        // Когда update.getMessage() вызывается, возвращаем наш мок сообщения
        when(update.getMessage()).thenReturn(message);

        // Когда message.getFrom() вызывается, возвращаем мок пользователя
        when(message.getFrom()).thenReturn(user);

        // Когда user.getId() вызывается, возвращаем тестовый ID (это рандомные цифры)
        when(user.getId()).thenReturn(123L);

        // Настраиваем, что сообщение содержит геолокацию
        when(message.hasLocation()).thenReturn(true);

        // Создаем тестовый мок локации
        Location location = mock(Location.class);
        when(message.getLocation()).thenReturn(location);
        when(location.getLatitude()).thenReturn(55.7558); // должно выдавать мск
        when(location.getLongitude()).thenReturn(37.6173);

        // Настраиваем chatId
        when(message.getChatId()).thenReturn(123L);

        // Просто вызываем метод - это самый примитивный тест
        messageHandler.handleLocation(update, absSender);

        // Если метод выполнился без исключений - тест пройден
        System.out.println("handleLocation test passed");
    }

    @Test
    void handleMessage() {
        // Создаем моки
        Message message = mock(Message.class);
        AbsSender absSender = mock(AbsSender.class);
        MessageHandler messageHandler = new MessageHandler();

        // Настраиваем мок
        when(message.getText()).thenReturn("/start");
        when(message.getFrom()).thenReturn(mock(User.class));

        // Вызываем метод
        messageHandler.handleMessage(message, absSender);

        // Если метод выполнился без исключений - тест пройден
        System.out.println("handleMessage test passed");
    }

    @Test
    void handleMessageWithText() {
        // Создаем моки
        Message message = mock(Message.class);
        AbsSender absSender = mock(AbsSender.class);
        MessageHandler messageHandler = new MessageHandler();

        // Настраиваем мок для текстового сообщения (не команда)
        when(message.getText()).thenReturn("Привет");
        when(message.getFrom()).thenReturn(mock(User.class));
        when(message.getChatId()).thenReturn(123L);

        // Вызываем метод
        messageHandler.handleMessage(message, absSender);

        System.out.println("handleMessageWithText test passed");
    }

    @Test
    void handleMessageWithUnknownCommand() {
        // Создаем моки
        Message message = mock(Message.class);
        AbsSender absSender = mock(AbsSender.class);
        MessageHandler messageHandler = new MessageHandler();

        // Настраиваем мок для неизвестной команды
        when(message.getText()).thenReturn("/unknowncommand");
        when(message.getFrom()).thenReturn(mock(User.class));
        when(message.getChatId()).thenReturn(123L);

        // Вызываем метод
        messageHandler.handleMessage(message, absSender);

        System.out.println("handleMessageWithUnknownCommand test passed");
    }

    @Test
    void testMessageHandlerCreation() {
        // Проверяем что можем создать объект
        MessageHandler handler = new MessageHandler();
        System.out.println("MessageHandler object created successfully");
    }

}