package org.example.repository;

import org.example.domain.Driver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DriverRepository implements Repository<Integer, Driver> {
    private final Db db;

    public DriverRepository(Db db) {
        this.db = db;
    }

    @Override
    public Optional<Driver> findOne(Integer id) {
        String sql = "SELECT id,name FROM drivers WHERE id=?";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next())
                    return Optional.empty();
                return Optional.of(new Driver(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Driver> findAll() {
        String sql = "SELECT id,name FROM drivers ORDER BY id";
        List<Driver> out = new ArrayList<>();

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Driver(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
            return out;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Driver save(Driver entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Driver update(Driver entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Driver> delete(Integer id) {
        throw new UnsupportedOperationException();
    }
}
