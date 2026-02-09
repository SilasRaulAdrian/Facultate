package org.example.repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository {
    private final Db db;

    public ReservationRepository(Db db) {
        this.db = db;
    }

    public void addReservation(long clientId, long hotelId, LocalDate startDate, int noNights) {
        String sql = "INSERT INTO reservations(client_id, hotel_id, start_date, no_nights) VALUES (?,?,?,?)";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, clientId);
            ps.setLong(2, hotelId);
            ps.setDate(3, Date.valueOf(startDate));
            ps.setInt(4, noNights);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Long> otherClientsWithSameHobby(long clientId) {
        String sql = """
            SELECT c2.client_id
            FROM clients c1
            JOIN clients c2 ON c2.hobby = c1.hobby
            WHERE c1.client_id = ? AND c2.client_id <> ?
        """;
        List<Long> ids = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, clientId);
            ps.setLong(2, clientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ids.add(rs.getLong(1));
            }
            return ids;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
