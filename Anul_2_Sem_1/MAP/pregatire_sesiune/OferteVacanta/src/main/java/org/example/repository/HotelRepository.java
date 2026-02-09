package org.example.repository;

import org.example.domain.Hotel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HotelRepository implements PagingRepository<Long, Hotel> {
    private final Db db;

    public HotelRepository(Db db) {
        this.db = db;
    }

    @Override
    public Optional<Hotel> findOne(Long id) {
        String sql = "SELECT * FROM hotels WHERE hotel_id=?";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new Hotel(
                        rs.getLong("hotel_id"),
                        rs.getLong("location_id"),
                        rs.getString("hotel_name"),
                        rs.getInt("no_rooms"),
                        rs.getDouble("price_per_night"),
                        rs.getString("type")
                ));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Hotel> findAll() {
        String sql = "SELECT * FROM hotels ORDER BY hotel_name";
        List<Hotel> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new Hotel(
                    rs.getLong("hotel_id"),
                    rs.getLong("location_id"),
                    rs.getString("hotel_name"),
                    rs.getInt("no_rooms"),
                    rs.getDouble("price_per_night"),
                    rs.getString("type")
            ));
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Hotel> findByLocation(long locationId) {
        String sql = "SELECT * FROM hotels WHERE location_id=? ORDER BY hotel_name";
        List<Hotel> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, locationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(new Hotel(
                        rs.getLong("hotel_id"),
                        rs.getLong("location_id"),
                        rs.getString("hotel_name"),
                        rs.getInt("no_rooms"),
                        rs.getDouble("price_per_night"),
                        rs.getString("type")
                ));
            }
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override public List<Hotel> findPage(int limit, int offset) {
        String sql = "SELECT * FROM hotels ORDER BY hotel_name LIMIT ? OFFSET ?";
        List<Hotel> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit); ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(new Hotel(
                        rs.getLong("hotel_id"),
                        rs.getLong("location_id"),
                        rs.getString("hotel_name"),
                        rs.getInt("no_rooms"),
                        rs.getDouble("price_per_night"),
                        rs.getString("type")
                ));
            }
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override public Hotel save(Hotel entity) { throw new UnsupportedOperationException(); }
    @Override public Hotel update(Hotel entity) { throw new UnsupportedOperationException(); }
    @Override public Optional<Hotel> delete(Long id) { throw new UnsupportedOperationException(); }
}
