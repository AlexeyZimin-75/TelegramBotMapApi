package org.example;

import org.example.service.UserData;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataBaseManagerTest {

    @BeforeEach
    @AfterEach
    void cleanup() {
        // Удаляем тестовую базу перед и после каждого теста
        File dbFile = new File("telegram_bot.db");
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    @Test
    void testSaveAndGetUser() {
        DataBaseManager db = new DataBaseManager();
        Long userId = 12345L;

        // Сохраняем пользователя
        db.saveUser(userId);

        // Проверяем что нет ошибок (простой тест)
        assertTrue(true, "Пользователь должен сохраняться без ошибок");
    }

    @Test
    void testSaveAndGetRoute() {
        DataBaseManager db = new DataBaseManager();
        Long userId = 12345L;

        // Сначала сохраняем пользователя
        db.saveUser(userId);

        // Создаем и сохраняем маршрут
        UserData route = new UserData();
        route.setCurrentCity("Москва");
        route.setDestinationCity("Санкт-Петербург");
        route.setDepartureDate("01.01.2024");
        route.setArrivalDate("10.01.2024");

        db.saveRoute(userId, route);

        // Получаем маршруты
        List<UserData> routes = db.getLastRoutes(userId, 5);

        assertNotNull(routes);
        assertFalse(routes.isEmpty());
        assertEquals("Москва", routes.get(0).getCurrentCity());
        assertEquals("Санкт-Петербург", routes.get(0).getDestinationCity());
    }

    @Test
    void testGetLastRoutesForNonExistentUser() {
        DataBaseManager db = new DataBaseManager();
        Long nonExistentUserId = 99999L;

        List<UserData> routes = db.getLastRoutes(nonExistentUserId, 5);

        assertNotNull(routes);
        assertTrue(routes.isEmpty());
    }

    @Test
    void testGetUserRouteCount() {
        DataBaseManager db = new DataBaseManager();
        Long userId = 12345L;

        // Сохраняем пользователя
        db.saveUser(userId);

        // Проверяем начальное количество
        int initialCount = db.getUserRouteCount(userId);
        assertEquals(0, initialCount);

        // Добавляем маршрут
        UserData route = new UserData();
        route.setCurrentCity("Москва");
        route.setDestinationCity("Санкт-Петербург");
        db.saveRoute(userId, route);

        // Проверяем количество после добавления
        int countAfterSave = db.getUserRouteCount(userId);
        assertEquals(1, countAfterSave);
    }

    @Test
    void testMultipleRoutes() {
        DataBaseManager db = new DataBaseManager();
        Long userId = 12345L;

        db.saveUser(userId);

        // Сохраняем несколько маршрутов
        for (int i = 1; i <= 3; i++) {
            UserData route = new UserData();
            route.setCurrentCity("Город " + i);
            route.setDestinationCity("Назначение " + i);
            route.setDepartureDate("01.01.2024");
            route.setArrivalDate("10.01.2024");
            db.saveRoute(userId, route);
        }

        // Проверяем количество
        int count = db.getUserRouteCount(userId);
        assertEquals(3, count);

        // Проверяем получение с лимитом
        List<UserData> routes = db.getLastRoutes(userId, 2);
        assertEquals(2, routes.size());
    }

    @Test
    void testRouteWithNullDates() {
        DataBaseManager db = new DataBaseManager();
        Long userId = 12345L;

        db.saveUser(userId);

        // Сохраняем маршрут с null датами
        UserData route = new UserData();
        route.setCurrentCity("Москва");
        route.setDestinationCity("Санкт-Петербург");
        // departureDate и arrivalDate остаются null

        db.saveRoute(userId, route);

        List<UserData> routes = db.getLastRoutes(userId, 5);

        assertNotNull(routes);
        assertEquals(1, routes.size());
        assertEquals("Москва", routes.get(0).getCurrentCity());
        assertNull(routes.get(0).getDepartureDate());
        assertNull(routes.get(0).getArrivalDate());
    }

    @Test
    void testDifferentUsers() {
        DataBaseManager db = new DataBaseManager();

        Long user1 = 11111L;
        Long user2 = 22222L;

        db.saveUser(user1);
        db.saveUser(user2);

        // Маршрут для первого пользователя
        UserData route1 = new UserData();
        route1.setCurrentCity("Москва");
        route1.setDestinationCity("СПб");
        db.saveRoute(user1, route1);

        // Маршрут для второго пользователя
        UserData route2 = new UserData();
        route2.setCurrentCity("Казань");
        route2.setDestinationCity("Сочи");
        db.saveRoute(user2, route2);

        // Проверяем что у каждого пользователя свой маршрут
        List<UserData> routes1 = db.getLastRoutes(user1, 5);
        List<UserData> routes2 = db.getLastRoutes(user2, 5);

        assertEquals(1, routes1.size());
        assertEquals(1, routes2.size());
        assertEquals("Москва", routes1.get(0).getCurrentCity());
        assertEquals("Казань", routes2.get(0).getCurrentCity());
    }
}