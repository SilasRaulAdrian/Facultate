package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clasa pentru gestionarea conexiunilor la baza de date
 */
public class ConnectionManager {
    private static String url;
    private static String user;
    private static String password;

    /**
     * Metoda pentru initializarea configuratiei bazei de date
     * @param url String, URL-ul bazei de date
     * @param user String, utilizatorul bazei de date
     * @param password String, parola bazei de date
     */
    public static void init(String url, String user, String password) {
        ConnectionManager.url = url;
        ConnectionManager.user = user;
        ConnectionManager.password = password;
        System.out.println("DB config initialized.");
    }

    /**
     * Metoda pentru obtinerea unei conexiuni la baza de date
     * @return Connection, conexiunea la baza de date
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get DB connection", e);
        }
    }
}
