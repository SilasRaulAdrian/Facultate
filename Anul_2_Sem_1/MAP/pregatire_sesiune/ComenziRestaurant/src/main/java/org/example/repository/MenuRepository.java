package org.example.repository;

import org.example.domain.MenuItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuRepository implements Repository<Integer, MenuItem> {
    private final Db db;

    public MenuRepository(Db db) {
        this.db = db;
    }

    @Override
    public Optional<MenuItem> findOne(Integer id) {
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM menu WHERE id = ?")) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }

                return Optional.of(new MenuItem(
                        rs.getInt("id"),
                        rs.getString("category"),
                        rs.getString("item"),
                        rs.getDouble("price"),
                        rs.getString("currency")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MenuItem> findAll() {
        List<MenuItem> out = new ArrayList<>();

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM menu ORDER BY category, item");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new MenuItem(
                        rs.getInt("id"),
                        rs.getString("category"),
                        rs.getString("item"),
                        rs.getDouble("price"),
                        rs.getString("currency")
                ));
            }
            return out;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MenuItem save(MenuItem entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MenuItem update(MenuItem entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<MenuItem> delete(Integer id) {
        throw new UnsupportedOperationException();
    }
}
