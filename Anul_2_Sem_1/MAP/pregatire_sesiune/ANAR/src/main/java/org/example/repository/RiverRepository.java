package org.example.repository;

import org.example.domain.River;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RiverRepository implements PagingRepository<String, River> {
    private final Db db;

    public RiverRepository(Db db) {
        this.db = db;
    }

    @Override
    public List<River> findPage(int limit, int offset) {
        String sql = """
            SELECT name, avg_level
            FROM rivers
            ORDER BY name
            LIMIT ? OFFSET ?
        """;
        return fetch(sql, List.of(limit, offset));
    }

    @Override
    public Optional<River> findOne(String s) {
        String sql = "SELECT name, avg_level FROM rivers WHERE name=?";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new River(
                        rs.getString("name"),
                        rs.getDouble("avg_level")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<River> findAll() {
        String sql = "SELECT name, avg_level FROM rivers ORDER BY name";
        return fetch(sql, List.of());
    }

    @Override
    public River save(River entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public River update(River entity) {
        String sql = "UPDATE rivers SET avg_level=? WHERE name=?";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, entity.getAvgLevel());
            ps.setString(2, entity.getName());
            ps.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<River> delete(String s) {
        throw new UnsupportedOperationException();
    }

    private List<River> fetch(String sql, List<Object> params) {
        List<River> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new River(
                            rs.getString("name"),
                            rs.getDouble("avg_level")
                    ));
                }
            }
            return out;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
