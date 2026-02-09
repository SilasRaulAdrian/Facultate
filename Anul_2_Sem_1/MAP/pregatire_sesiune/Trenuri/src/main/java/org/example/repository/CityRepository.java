package org.example.repository;

import org.example.domain.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CityRepository implements Repository<String, City> {
    private final Db db;

    public CityRepository(Db db) {
        this.db = db;
    }

    @Override
    public Optional<City> findOne(String id) {
        String sql = "SELECT id,name FROM cities WHERE id=?";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new City(rs.getString("id"), rs.getString("name")));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<City> findAll() {
        String sql = "SELECT id,name FROM cities ORDER BY name";
        List<City> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                out.add(new City(rs.getString("id"), rs.getString("name")));
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override public City save(City entity) { throw new UnsupportedOperationException(); }
    @Override public City update(City entity) { throw new UnsupportedOperationException(); }
    @Override public Optional<City> delete(String id) { throw new UnsupportedOperationException(); }
}
