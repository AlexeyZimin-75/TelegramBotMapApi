package org.example.handlers;

import org.example.service.UserStateService;
import org.example.states.UserState;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageProcessorTest {

    @Mock private UserStateService userStateService;
    @Mock private AbsSender absSender;
    @Mock private Message message;
    @Mock private User user;
    @Mock private Location location;

    @Test
    void processUpdate_AllScenarios() {
        MessageProcessor processor = new MessageProcessor(userStateService);

        // Настраиваем общие моки
        when(message.getFrom()).thenReturn(user);
        when(user.getId()).thenReturn(1L);
        when(message.getChatId()).thenReturn(123L);

        // Тест с геолокацией
        Update locationUpdate = mock(Update.class);
        when(locationUpdate.hasMessage()).thenReturn(true);
        when(message.hasLocation()).thenReturn(true);
        when(message.getLocation()).thenReturn(location);
        when(locationUpdate.getMessage()).thenReturn(message);

        processor.processUpdate(locationUpdate, absSender);

        // Тест с текстовым сообщением и состоянием AWAITING_DESTINATION_CITY
        Update textUpdate = mock(Update.class);
        when(textUpdate.hasMessage()).thenReturn(true);
        when(message.hasText()).thenReturn(true);
        when(message.hasLocation()).thenReturn(false);
        when(message.getText()).thenReturn("Москва");
        when(textUpdate.getMessage()).thenReturn(message);
        when(userStateService.getUserState(1L)).thenReturn(UserState.AWAITING_DESTINATION_CITY);

        processor.processUpdate(textUpdate, absSender);

        // Тест с неизвестной командой
        Update commandUpdate = mock(Update.class);
        when(commandUpdate.hasMessage()).thenReturn(true);
        Message commandMessage = mock(Message.class); // Создаем новый мок для сообщения
        when(commandMessage.getFrom()).thenReturn(user);
        when(commandMessage.getChatId()).thenReturn(123L);
        when(commandMessage.hasText()).thenReturn(true);
        when(commandMessage.hasLocation()).thenReturn(false);
        when(commandMessage.getText()).thenReturn("/unknown");
        when(commandUpdate.getMessage()).thenReturn(commandMessage);

        processor.processUpdate(commandUpdate, absSender);
    }
}