package org.example.repository;

import org.example.domain.Flight;
import org.example.domain.FlightRow;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightRepository implements PagingRepository<Long, Flight> {
    private final Db db;

    public FlightRepository(Db db) {
        this.db = db;
    }

    @Override
    public List<Flight> findPage(int limit, int offset) {
        String sql = """
            SELECT flight_id, from_loc, to_loc, departure_time, landing_time, seats
            FROM flights
            ORDER BY departure_time
            LIMIT ? OFFSET ?
        """;
        return fetchFlights(sql, List.of(limit, offset));
    }

    @Override
    public Optional<Flight> findOne(Long id) {
        String sql = """
            SELECT flight_id, from_loc, to_loc, departure_time, landing_time, seats
            FROM flights
            WHERE flight_id=?
        """;

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new Flight(
                        rs.getLong("flight_id"),
                        rs.getString("from_loc"),
                        rs.getString("to_loc"),
                        rs.getTimestamp("departure_time").toLocalDateTime(),
                        rs.getTimestamp("landing_time").toLocalDateTime(),
                        rs.getInt("seats")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Flight> findAll() {
        String sql = """
            SELECT flight_id, from_loc, to_loc, departure_time, landing_time, seats
            FROM flights
            ORDER BY departure_time
        """;
        return fetchFlights(sql, List.of());
    }

    @Override
    public Flight save(Flight entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Flight update(Flight entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Flight> delete(Long id) {
        throw new UnsupportedOperationException();
    }

    public List<String> findDistinctFrom() {
        return fetchDistinct("SELECT DISTINCT from_loc FROM flights ORDER BY from_loc");
    }

    public List<String> findDistinctTo() {
        return fetchDistinct("SELECT DISTINCT to_loc FROM flights ORDER BY to_loc");
    }

    private List<String> fetchDistinct(String sql) {
        List<String> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(rs.getString(1));
            return out;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int countFiltered(String from, String to, LocalDate day) {
        String sql = """
            SELECT COUNT(*)
            FROM flights f
            WHERE f.from_loc=? AND f.to_loc=?
              AND f.departure_time >= ? AND f.departure_time < ?
        """;

        Timestamp start = Timestamp.valueOf(day.atStartOfDay());
        Timestamp end = Timestamp.valueOf(day.plusDays(1).atStartOfDay());

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, from);
            ps.setString(2, to);
            ps.setTimestamp(3, start);
            ps.setTimestamp(4, end);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FlightRow> findFilteredPage(String from, String to, LocalDate day, int limit, int offset) {
        String sql = """
            SELECT
              f.flight_id,
              f.from_loc,
              f.to_loc,
              f.departure_time,
              f.landing_time,
              f.seats,
              (f.seats - COUNT(t.id)) AS available
            FROM flights f
            LEFT JOIN tickets t ON t.flight_id = f.flight_id
            WHERE f.from_loc=? AND f.to_loc=?
              AND f.departure_time >= ? AND f.departure_time < ?
            GROUP BY f.flight_id, f.from_loc, f.to_loc, f.departure_time, f.landing_time, f.seats
            ORDER BY f.departure_time
            LIMIT ? OFFSET ?
        """;

        Timestamp start = Timestamp.valueOf(day.atStartOfDay());
        Timestamp end = Timestamp.valueOf(day.plusDays(1).atStartOfDay());

        List<FlightRow> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, from);
            ps.setString(2, to);
            ps.setTimestamp(3, start);
            ps.setTimestamp(4, end);
            ps.setInt(5, limit);
            ps.setInt(6, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new FlightRow(
                            rs.getLong("flight_id"),
                            rs.getString("from_loc"),
                            rs.getString("to_loc"),
                            rs.getTimestamp("departure_time").toLocalDateTime(),
                            rs.getTimestamp("landing_time").toLocalDateTime(),
                            rs.getInt("seats"),
                            rs.getInt("available")
                    ));
                }
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Flight> fetchFlights(String sql, List<Object> params) {
        List<Flight> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(new Flight(
                        rs.getLong("flight_id"),
                        rs.getString("from_loc"),
                        rs.getString("to_loc"),
                        rs.getTimestamp("departure_time").toLocalDateTime(),
                        rs.getTimestamp("landing_time").toLocalDateTime(),
                        rs.getInt("seats")
                ));
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
