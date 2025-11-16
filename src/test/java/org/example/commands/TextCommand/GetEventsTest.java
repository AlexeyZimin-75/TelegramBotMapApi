package org.example.commands.TextCommand;

import org.example.apiMethods.KudaGo.Event;
import org.example.apiMethods.KudaGo.KudaGoClient;
import org.example.service.UserData;
import org.example.service.UserDataService;
import org.example.service.UserStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GetEventsTest {

    @Mock
    private UserStateService userStateService;

    @Mock
    private UserDataService userDataService;

    @Mock
    private KudaGoClient kudaGoClient;

    private GetEvents getEvents;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        getEvents = new GetEvents(userStateService,userDataService);
    }



    private UserData createTestUserData(String currentCity,String destinationCity,String arrivalDate,String departureDate){
        UserData userData = new UserData();
        userData.setDestinationCity(destinationCity);
        userData.setCurrentCity(currentCity);
        userData.setArrivalDate(arrivalDate);
        userData.setDepartureDate(departureDate);

        return userData;
    }

    private Event createTestEvent(String title, String price, List<String> categories, String siteUrl) {
        Event event = new Event();
        event.setTitle(title);
        event.setPrice(price);
        event.setCategories(categories);
        event.setSiteUrl(siteUrl);
        return event;
    }

    @Test
    void whenArrivalDateEqualNullWeMustGetErrorMessage(){
        UserData userData = createTestUserData("Москва","Волгоград",null,null);
        Long userId = 123L;

        when(userDataService.getUserData(userId)).thenReturn(userData);

        String expectedStr = getEvents.execute(userId);
        assertEquals(expectedStr,"Не хватает данных для поиска мероприятий (дата прибытия или город назначения)");
    }

    @Test
    void checkFunctionGetCitySlug(){
        String city = "Москва";
        String citySlug = getEvents.getCitySlug(city);
        String expextedCitySlug = "msk";

        assertEquals(expextedCitySlug,citySlug);
    }

    @Test
    void WhenArrivalDateOrDepartureDateIsInvalidWeMustGetErrorMessage(){
        Long userId = 123L;
        UserData userData = createTestUserData("Москва","Волгоград","invalid date","16.12.2025");
        when(userDataService.getUserData(userId)).thenReturn(userData);

        String result = getEvents.execute(userId);

        assertTrue(result.contains("Ошибка при поиске мероприятий"));
    }

    @Test
    void WhenDestinationalCityIsInvalidWeMustGetErrorMessage(){
        Long userId = 123L;
        UserData userData = createTestUserData("invalid","invalid","12.12.2025","16.12.2025");
        when(userDataService.getUserData(userId)).thenReturn(userData);

        String result = getEvents.execute(userId);


        assertTrue(result.contains("Мероприятия не найдены"));
    }

    @Test
    void WhenDepartureAllDataIsValid() {
        UserData userData = createTestUserData("Волгоград","Москва", "01.12.2024", "16.12.2024");
        Long userId = 123L;
        when(userDataService.getUserData(userId)).thenReturn(userData);

        String result = getEvents.execute(userId);

        assertTrue(result.contains("🎭 Поиск мероприятий в KudaGo"));
        assertTrue(result.contains("📍 Город: Москва"));
    }

    @Test
    void WhenEventsListIsNullWeMustGetErrorMessage() {
        StringBuilder result = GetEvents.returnAllEvents(null, kudaGoClient);

        assertEquals("Мероприятия не найдены", result.toString());
    }

    @Test
    void WhenSingleEventWeMustGetFormattedString() {

        Event event = createTestEvent(
                "Тестовый концерт",
                "500 руб",
                List.of("concert", "music"),
                "https://example.com"
        );
        List<Event> events = List.of(event);


        StringBuilder result = GetEvents.returnAllEvents(events, kudaGoClient);


        String resultStr = result.toString();
        assertTrue(resultStr.contains("1. 🎭 **Тестовый концерт**"));
        assertTrue(resultStr.contains("💰 500 руб"));
        assertTrue(resultStr.contains("🏷️ concert, music"));
        assertTrue(resultStr.contains("🔗 https://example.com"));
    }

}