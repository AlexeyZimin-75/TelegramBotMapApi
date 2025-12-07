package org.example.handlers;

import org.example.service.UserStateService;
import org.example.service.UserDataService;
import org.example.states.UserState;
import org.example.service.UserData;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DateHandlerTest {

    @Mock
    private UserStateService userStateService;

    @Mock
    private UserDataService userDataService;

    @Mock
    private AbsSender absSender;

    @Mock
    private Message message;

    @Mock
    private User user;

    @Captor
    private ArgumentCaptor<org.telegram.telegrambots.meta.api.methods.send.SendMessage> messageCaptor;

    private DateHandler dateHandler;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @BeforeEach
    void setUp() {
        dateHandler = new DateHandler(userStateService, userDataService);
    }

    @Test
    void handleArrivalDate_WithValidDate_ShouldSaveDateAndChangeState() throws TelegramApiException {
        // Arrange
        Long userId = 1L;
        String validDate = LocalDate.now().plusDays(1).format(dateFormatter);
        UserData userData = new UserData();

        when(user.getId()).thenReturn(userId);
        when(message.getFrom()).thenReturn(user);
        when(message.getChatId()).thenReturn(123L);
        when(message.getText()).thenReturn(validDate);
        when(userDataService.getUserData(userId)).thenReturn(userData);

        // Act
        dateHandler.handleArrivalDate(message, absSender);

        // Assert
        verify(userDataService).getUserData(userId);
        assertEquals(validDate, userData.getArrivalDate());
        verify(userStateService).setUserState(userId, UserState.AWAITING_DEPARTURE_DATE_RESPONSE);
        verify(absSender).execute(messageCaptor.capture());

        org.telegram.telegrambots.meta.api.methods.send.SendMessage sentMessage = messageCaptor.getValue();
        assertEquals("123", sentMessage.getChatId());
        assertTrue(sentMessage.getText().contains("✅ Дата прибытия сохранена"));
    }

    @Test
    void handleArrivalDate_WithInvalidDate_ShouldSendErrorMessage() throws TelegramApiException {
        // Arrange
        Long userId = 1L;
        String invalidDate = "invalid-date";

        when(user.getId()).thenReturn(userId);
        when(message.getFrom()).thenReturn(user);
        when(message.getChatId()).thenReturn(123L);
        when(message.getText()).thenReturn(invalidDate);

        // Act
        dateHandler.handleArrivalDate(message, absSender);

        // Assert
        verify(userDataService, never()).getUserData(userId);
        verify(userStateService, never()).setUserState(anyLong(), any());
        verify(absSender).execute(messageCaptor.capture());

        org.telegram.telegrambots.meta.api.methods.send.SendMessage sentMessage = messageCaptor.getValue();
        assertEquals("123", sentMessage.getChatId());
        assertTrue(sentMessage.getText().contains("❌ Неверный формат даты"));
    }

    @Test
    void handleArrivalDate_WithPastDate_ShouldSendErrorMessage() throws TelegramApiException {
        // Arrange
        Long userId = 1L;
        String pastDate = LocalDate.now().minusDays(1).format(dateFormatter);

        when(user.getId()).thenReturn(userId);
        when(message.getFrom()).thenReturn(user);
        when(message.getChatId()).thenReturn(123L);
        when(message.getText()).thenReturn(pastDate);

        // Act
        dateHandler.handleArrivalDate(message, absSender);

        // Assert
        verify(userDataService, never()).getUserData(userId);
        verify(userStateService, never()).setUserState(anyLong(), any());
        verify(absSender).execute(messageCaptor.capture());

        org.telegram.telegrambots.meta.api.methods.send.SendMessage sentMessage = messageCaptor.getValue();
        assertTrue(sentMessage.getText().contains("❌ Неверный формат даты"));
    }

    @Test
    void handleDepartureDate_WithValidDateAndValidSequence_ShouldSaveDateAndProceed() throws TelegramApiException {
        // Arrange
        Long userId = 1L;
        String arrivalDate = LocalDate.now().plusDays(5).format(dateFormatter);
        String departureDate = LocalDate.now().plusDays(3).format(dateFormatter);

        UserData userData = new UserData();
        userData.setArrivalDate(arrivalDate);

        when(user.getId()).thenReturn(userId);
        when(message.getFrom()).thenReturn(user);
        when(message.getChatId()).thenReturn(123L);
        when(message.getText()).thenReturn(departureDate);
        when(userDataService.getUserData(userId)).thenReturn(userData);

        // Act
        dateHandler.handleDepartureDate(message, absSender);

        // Assert
        // Используем atLeast(2) так как метод вызывается в handleDepartureDate и areDatesValid
        // RouteHandler.sendFinalRouteInfo может вызывать дополнительные разы
        verify(userDataService, atLeast(2)).getUserData(userId);
        assertEquals(departureDate, userData.getDepartureDate());
        verify(userStateService).setUserState(userId, UserState.READY_TO_SEARCH);
    }

    @Test
    void handleDepartureDate_WithValidDateButInvalidSequence_ShouldSendErrorMessage() throws TelegramApiException {
        // Arrange
        Long userId = 1L;
        String arrivalDate = LocalDate.now().plusDays(3).format(dateFormatter);
        String departureDate = LocalDate.now().plusDays(5).format(dateFormatter);

        UserData userData = new UserData();
        userData.setArrivalDate(arrivalDate);

        when(user.getId()).thenReturn(userId);
        when(message.getFrom()).thenReturn(user);
        when(message.getChatId()).thenReturn(123L);
        when(message.getText()).thenReturn(departureDate);
        when(userDataService.getUserData(userId)).thenReturn(userData);

        // Act
        dateHandler.handleDepartureDate(message, absSender);

        // Assert
        verify(userDataService, atLeast(2)).getUserData(userId);
        assertEquals(departureDate, userData.getDepartureDate());
        verify(userStateService, never()).setUserState(userId, UserState.READY_TO_SEARCH);

        verify(absSender, times(2)).execute(messageCaptor.capture());

        var allMessages = messageCaptor.getAllValues();
        assertTrue(allMessages.get(0).getText().contains("❌ Дата отправления должна быть раньше даты прибытия"));
        assertTrue(allMessages.get(1).getText().contains("Укажите дату отправления"));
    }

    @Test
    void handleDepartureDate_WithInvalidDate_ShouldSendErrorMessage() throws TelegramApiException {
        // Arrange
        Long userId = 1L;
        String invalidDate = "invalid-date";

        when(user.getId()).thenReturn(userId);
        when(message.getFrom()).thenReturn(user);
        when(message.getChatId()).thenReturn(123L);
        when(message.getText()).thenReturn(invalidDate);

        // Act
        dateHandler.handleDepartureDate(message, absSender);

        // Assert
        verify(userDataService, never()).getUserData(userId);
        verify(userStateService, never()).setUserState(anyLong(), any());
        verify(absSender).execute(messageCaptor.capture());

        org.telegram.telegrambots.meta.api.methods.send.SendMessage sentMessage = messageCaptor.getValue();
        assertTrue(sentMessage.getText().contains("❌ Неверный формат даты"));
    }

    // Тесты для приватных методов через рефлексию
    @Test
    void isValidDate_WithValidFutureDate_ShouldReturnTrue() throws Exception {
        // Arrange
        String futureDate = LocalDate.now().plusDays(10).format(dateFormatter);

        // Act
        boolean result = invokePrivateMethod("isValidDate", futureDate);

        // Assert
        assertTrue(result);
    }

    @Test
    void isValidDate_WithValidTodayDate_ShouldReturnTrue() throws Exception {
        // Arrange
        String todayDate = LocalDate.now().format(dateFormatter);

        // Act
        boolean result = invokePrivateMethod("isValidDate", todayDate);

        // Assert
        assertTrue(result);
    }

    @Test
    void isValidDate_WithPastDate_ShouldReturnFalse() throws Exception {
        // Arrange
        String pastDate = LocalDate.now().minusDays(1).format(dateFormatter);

        // Act
        boolean result = invokePrivateMethod("isValidDate", pastDate);

        // Assert
        assertFalse(result);
    }

    @Test
    void isValidDate_WithInvalidFormat_ShouldReturnFalse() throws Exception {
        // Arrange
        String invalidDate = "2023-12-01"; // неправильный формат

        // Act
        boolean result = invokePrivateMethod("isValidDate", invalidDate);

        // Assert
        assertFalse(result);
    }

    @Test
    void isValidDate_WithNonExistentDate_ShouldReturnFalse() throws Exception {
        // Arrange
        String nonExistentDate = "32.13.2023";

        // Act
        boolean result = invokePrivateMethod("isValidDate", nonExistentDate);

        // Assert
        assertFalse(result);
    }

    @Test
    void areDatesValid_WithValidSequence_ShouldReturnTrue() throws Exception {
        // Arrange
        Long userId = 1L;
        UserData testUserData = new UserData();
        testUserData.setArrivalDate(LocalDate.now().plusDays(5).format(dateFormatter));
        testUserData.setDepartureDate(LocalDate.now().plusDays(3).format(dateFormatter));

        when(userDataService.getUserData(userId)).thenReturn(testUserData);

        // Act
        boolean result = invokePrivateMethod("areDatesValid", userId);

        // Assert
        assertTrue(result);
    }

    @Test
    void areDatesValid_WithInvalidSequence_ShouldReturnFalse() throws Exception {
        // Arrange
        Long userId = 1L;
        UserData testUserData = new UserData();
        testUserData.setArrivalDate(LocalDate.now().plusDays(3).format(dateFormatter));
        testUserData.setDepartureDate(LocalDate.now().plusDays(5).format(dateFormatter));

        when(userDataService.getUserData(userId)).thenReturn(testUserData);

        // Act
        boolean result = invokePrivateMethod("areDatesValid", userId);

        // Assert
        assertFalse(result);
    }

    @Test
    void areDatesValid_WithInvalidDateFormat_ShouldReturnFalse() throws Exception {
        // Arrange
        Long userId = 1L;
        UserData testUserData = new UserData();
        testUserData.setArrivalDate("invalid-date");
        testUserData.setDepartureDate(LocalDate.now().plusDays(3).format(dateFormatter));

        when(userDataService.getUserData(userId)).thenReturn(testUserData);

        // Act
        boolean result = invokePrivateMethod("areDatesValid", userId);

        // Assert
        assertFalse(result);
    }

    // Вспомогательный метод для вызова приватных методов через рефлексию
    private boolean invokePrivateMethod(String methodName, Object parameter) throws Exception {
        var method = DateHandler.class.getDeclaredMethod(methodName, parameter.getClass());
        method.setAccessible(true);
        return (Boolean) method.invoke(dateHandler, parameter);
    }
}