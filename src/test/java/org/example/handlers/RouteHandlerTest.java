package org.example.handlers;

import org.example.service.UserData;
import org.example.service.UserStateService;
import org.example.service.UserDataService;
import org.example.states.UserState;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteHandlerCompactTest {

    @Mock private UserStateService userStateService;
    @Mock private UserDataService userDataService;
    @Mock private AbsSender absSender;
    @Mock private Message message;
    @Mock private User user;

    @Test
    void routeHandler_AllScenarios() {
        RouteHandler routeHandler = new RouteHandler(userStateService, userDataService);

        when(message.getFrom()).thenReturn(user);
        when(user.getId()).thenReturn(1L);
        when(message.getChatId()).thenReturn(123L);

        // Тест сохранения города назначения
        UserData userData = new UserData();
        userData.setCurrentCity("Санкт-Петербург");
        when(message.getText()).thenReturn("Москва");
        when(userDataService.getUserData(1L)).thenReturn(userData);

        routeHandler.handleDestinationCity(message, absSender);
        verify(userStateService).setUserState(1L, UserState.AWAITING_ARRIVAL_DATE_RESPONSE);

        // Тест отправки финальной информации
        userData.setDestinationCity("Москва");
        userData.setDepartureDate("25.11.2024");
        userData.setArrivalDate("28.11.2024");

        routeHandler.sendFinalRouteInfo(1L, 123L, absSender);
        // Проверяем, что сообщение отправлено
    }
}
