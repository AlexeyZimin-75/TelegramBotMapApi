package org.example.handlers;

import org.example.service.UserStateService;
import org.example.service.UserDataService;
import org.example.commands.GetLocationCommand;
import org.example.service.UserData;
import org.example.states.UserState;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationHandlerTest {

    @Mock private UserStateService userStateService;
    @Mock private UserDataService userDataService;
    @Mock private AbsSender absSender;
    @Mock private Location location;
    @Mock private GetLocationCommand getLocationCommand;

    @Captor private ArgumentCaptor<org.telegram.telegrambots.meta.api.methods.send.SendMessage> messageCaptor;

    private LocationHandler locationHandler;
    private final Long userId = 1L;
    private final Long chatId = 123L;

    @BeforeEach
    void setUp() {
        locationHandler = new LocationHandler(userStateService, userDataService);
        // Инъекция мока GetLocationCommand через рефлексию
        injectMockGetLocationCommand();
    }

    @Test
    void handleLocation_Success() throws Exception {
        // Arrange
        setupLocation(55.7558, 37.6173);
        UserData userData = setupUserData();
        when(getLocationCommand.getCityFromCoordinates(55.7558, 37.6173)).thenReturn("Москва");

        // Act
        locationHandler.handleLocation(userId, chatId, location, absSender);

        // Assert
        assertCitySaved(userData, "Москва");
        assertStateChanged(UserState.AWAITING_DESTINATION_CITY);
        assertMessageContains("📍 Отлично! Ваш город: Москва");
    }

    @Test
    void handleLocation_Failure() throws Exception {
        // Arrange
        setupLocation(0.0, 0.0);
        when(getLocationCommand.getCityFromCoordinates(0.0, 0.0))
                .thenThrow(new Exception("Город не найден"));

        // Act
        locationHandler.handleLocation(userId, chatId, location, absSender);

        // Assert
        assertStateChanged(UserState.AWAITING_MANUAL_CITY);
        assertMessageContains("❌ Не удалось определить город по вашим координатам");
    }

    @Test
    void handleTextLocation_ManualCityOption() throws TelegramApiException {
        // Act
        locationHandler.handleTextLocation(userId, chatId, "👉 ввести город вручную",
                UserState.AWAITING_LOCATION, absSender);

        // Assert
        assertStateChanged(UserState.AWAITING_MANUAL_CITY);
        assertMessageContains("🏙️ Пожалуйста, введите название вашего города:");
    }

    @Test
    void handleTextLocation_CityInput_InAwaitingLocationState() throws Exception {
        // Arrange
        UserData userData = setupUserData();

        // Act
        locationHandler.handleTextLocation(userId, chatId, "Санкт-Петербург",
                UserState.AWAITING_LOCATION, absSender);

        // Assert
        assertEquals("Санкт-Петербург", userData.getCurrentCity());
        assertStateChanged(UserState.AWAITING_DESTINATION_CITY);
        assertMessageContains("📍 Отлично! Ваш город: Санкт-Петербург");
    }

    @Test
    void handleTextLocation_CityInput_InAwaitingManualCityState() throws Exception {
        // Arrange
        UserData userData = setupUserData();

        // Act
        locationHandler.handleTextLocation(userId, chatId, "Новосибирск",
                UserState.AWAITING_MANUAL_CITY, absSender);

        // Assert
        assertEquals("Новосибирск", userData.getCurrentCity());
        assertStateChanged(UserState.AWAITING_DESTINATION_CITY);
        assertMessageContains("📍 Отлично! Ваш город: Новосибирск");
    }

    @Test
    void handleTextLocation_UnsupportedState() {
        // Act
        locationHandler.handleTextLocation(userId, chatId, "любой текст",
                UserState.READY_TO_SEARCH, absSender);

        // Assert
        verifyNoInteractions(userStateService, userDataService, absSender);
    }

    // Вспомогательные методы
    private void setupLocation(double lat, double lon) {
        when(location.getLatitude()).thenReturn(lat);
        when(location.getLongitude()).thenReturn(lon);
    }

    private UserData setupUserData() {
        UserData userData = new UserData();
        when(userDataService.getUserData(userId)).thenReturn(userData);
        return userData;
    }

    private void injectMockGetLocationCommand() {
        try {
            Field commandField = LocationHandler.class.getDeclaredField("getLocationCommand");
            commandField.setAccessible(true);
            commandField.set(locationHandler, getLocationCommand);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock GetLocationCommand", e);
        }
    }

    private void assertCitySaved(UserData userData, String expectedCity) {
        assertEquals(expectedCity, userData.getCurrentCity());
        verify(userDataService).getUserData(userId);
    }

    private void assertStateChanged(UserState expectedState) {
        verify(userStateService).setUserState(userId, expectedState);
    }

    private void assertMessageContains(String expectedText) throws TelegramApiException {
        verify(absSender).execute(messageCaptor.capture());
        String actualText = messageCaptor.getValue().getText();
        assertTrue(actualText.contains(expectedText),
                "Expected message to contain: '" + expectedText + "', but was: '" + actualText + "'");
    }
}