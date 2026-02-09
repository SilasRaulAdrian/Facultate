package org.example.repository;

import org.example.domain.Ticket;

import java.sql.*;
import java.time.LocalDateTime;

public class TicketRepository {
    private final Db db;
    public TicketRepository(Db db) { this.db = db; }

    // cumpara 1 bilet daca mai sunt locuri
    public Ticket buyTicket(String username, long flightId) {
        String sqlAvail = """
            SELECT f.seats - (SELECT COUNT(*) FROM tickets WHERE flight_id=?) AS avail
            FROM flights f
            WHERE f.flight_id=?
            FOR UPDATE
        """;

        String sqlInsert = """
            INSERT INTO tickets(username, flight_id, purchase_time)
            VALUES (?,?,?)
        """;

        try (Connection c = db.getConnection()) {
            c.setAutoCommit(false);

            int avail;
            try (PreparedStatement ps = c.prepareStatement(sqlAvail)) {
                ps.setLong(1, flightId);
                ps.setLong(2, flightId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        c.rollback();
                        throw new IllegalArgumentException("Zbor inexistent.");
                    }
                    avail = rs.getInt("avail");
                }
            }

            if (avail <= 0) {
                c.rollback();
                throw new IllegalArgumentException("Nu mai sunt locuri disponibile pentru acest zbor.");
            }

            LocalDateTime now = LocalDateTime.now();
            try (PreparedStatement ps = c.prepareStatement(sqlInsert)) {
                ps.setString(1, username);
                ps.setLong(2, flightId);
                ps.setTimestamp(3, Timestamp.valueOf(now));
                ps.executeUpdate();
            }

            c.commit();
            return new Ticket(username, flightId, now);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
