package org.example;

import org.example.service.UserData;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DataBaseManager {

    private static final String DB_URL = "jdbc:sqlite:telegram_bot.db";

    public DataBaseManager() {
        initializeDataBase();
    }

    private void initializeDataBase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement statement = conn.createStatement()) {
            // тут таблица пользователей
            String createUsersTable = """
                    CREATE TABLE IF NOT EXISTS users (
                        user_id INTEGER PRIMARY KEY,
                        username TEXT,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP)""";
            // тут таблица маршрутов
            String createRoutesTable = """
                        CREATE TABLE IF NOT EXISTS routes (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            user_id INTEGER,
                            current_city TEXT NOT NULL,
                            destination_city TEXT NOT NULL,
                            departure_date TEXT,
                            arrival_date TEXT,
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (user_id) REFERENCES users (user_id)
                        )
                    """;
            statement.execute(createUsersTable);
            statement.execute(createRoutesTable);

        } catch (SQLException e) {
            System.err.println("бд не создалась" + e.getMessage());
        }
    }

    public void saveUser(Long userId) {
        String sql = """
                INSERT OR REPLACE INTO users (user_id)
                VALUES (?)
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstatement = conn.prepareStatement(sql)) {

            pstatement.setLong(1, userId);
            pstatement.executeUpdate();


        } catch (SQLException e) {
            System.err.println("пользователь не сохранился");
        }

    }

    public void saveRoute(Long userId, UserData userData) {
        String sql = """
                INSERT INTO routes (user_id, current_city, destination_city, departure_date, arrival_date)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstatement = conn.prepareStatement(sql)) {
            pstatement.setLong(1, userId);
            pstatement.setString(2, userData.getCurrentCity());
            pstatement.setString(3, userData.getDestinationCity());
            pstatement.setString(4, userData.getDepartureDate());
            pstatement.setString(5, userData.getArrivalDate());
            pstatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("ошибка, маршрут не сохранился" + e.getMessage());
        }
    }

    // здесь мы получаем последние маршруты юзера
    public List<UserData> getLastRoutes(Long userId, int limit) {
        List<UserData> routes = new ArrayList<>();
        String sql = """
                SELECT current_city, destination_city, departure_date, arrival_date, created_at
                FROM routes 
                WHERE user_id = ?
                ORDER BY created_at DESC
                LIMIT ?
                """;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            pstmt.setInt(2, limit);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                UserData route = new UserData();
                route.setCurrentCity(rs.getString("current_city"));
                route.setDestinationCity(rs.getString("destination_city"));
                route.setDepartureDate(rs.getString("departure_date"));
                route.setArrivalDate(rs.getString("arrival_date"));
                routes.add(route);

            }
        } catch (SQLException e) {
            System.err.println("маршруты не выводятся:/" + e.getMessage());
        }
        return routes;

    }
    public int getUserRouteCount(Long userId) {
        String sql = "SELECT COUNT(*) as count FROM routes WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении количества маршрутов: " + e.getMessage());
        }

        return 0; // возвращаем 0 в случае ошибки
    }
}
