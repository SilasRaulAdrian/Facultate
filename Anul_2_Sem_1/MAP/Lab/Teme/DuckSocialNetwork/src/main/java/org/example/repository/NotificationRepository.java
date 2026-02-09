package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.config.ConnectionManager;
import org.example.model.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clasa NotificationRepository pentru gestionarea notificarilor in baza de date
 */
@RequiredArgsConstructor
public class NotificationRepository {
    /**
     * Metoda pentru obtinerea unei conexiuni la baza de date
     * @return Connection, conexiunea la baza de date
     * @throws SQLException in caz de eroare la conectare
     */
    private Connection getConnection() throws SQLException {
        return ConnectionManager.getConnection();
    }

    /**
     * Metoda pentru maparea unui ResultSet la un obiect Notification
     * @param rs ResultSet, rezultatul interogarii SQL
     * @return Notification, obiectul Notification mapat
     * @throws SQLException in caz de eroare la citirea datelor
     */
    private Notification map(ResultSet rs) throws SQLException {
        return new Notification(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getString("message"),
                rs.getBoolean("seen"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    /**
     * Metoda pentru salvarea unei notificari in baza de date
     * @param n Notification, notificarea de salvat
     */
    public void save(Notification n) {
        String sql = """
            INSERT INTO notifications(user_id, message, seen)
            VALUES (?, ?, false)
        """;

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, n.getUserId());
            ps.setString(2, n.getMessage());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metoda pentru gasirea notificarilor nevazute ale unui utilizator
     * @param userId Long, id-ul utilizatorului
     * @return List<Notification>, lista de notificari nevazute
     */
    public List<Notification> findUnseen(Long userId) {
        String sql = """
            SELECT * FROM notifications
            WHERE user_id = ? AND seen = false
            ORDER BY created_at ASC
        """;

        List<Notification> list = new ArrayList<>();

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    /**
     * Metoda pentru marcarea unei notificari ca vazuta
     * @param notificationId Long, id-ul notificarii
     */
    public void markAsSeen(Long notificationId) {
        String sql = """
            UPDATE notifications
            SET seen = true
            WHERE id = ?
        """;

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, notificationId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
