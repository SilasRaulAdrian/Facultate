package org.example.repository;

import org.example.domain.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationRepository implements Repository<Long, Location> {
    private final Db db;

    public LocationRepository(Db db) {
        this.db = db;
    }

    @Override
    public Optional<Location> findOne(Long id) {
        String sql = "SELECT location_id, location_name FROM locations WHERE location_id=?";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new Location(rs.getLong(1), rs.getString(2)));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Location> findAll() {
        String sql = "SELECT location_id, location_name FROM locations ORDER BY location_name";
        List<Location> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new Location(rs.getLong(1), rs.getString(2)));
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override public Location save(Location entity) { throw new UnsupportedOperationException(); }
    @Override public Location update(Location entity) { throw new UnsupportedOperationException(); }
    @Override public Optional<Location> delete(Long id) { throw new UnsupportedOperationException(); }
}
