package org.example.repository;

import org.example.domain.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TableRepository implements Repository<Integer, Table> {
    private final Db db;

    public TableRepository(Db db) {
        this.db = db;
    }

    @Override
    public Optional<Table> findOne(Integer id) {
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT id FROM tables_rest WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Table(rs.getInt("id")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Table> findAll() {
        List<Table> tables = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT id FROM tables_rest ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tables.add(new Table(rs.getInt("id")));
            }
            return tables;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Table save(Table entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Table update(Table entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Table> delete(Integer integer) {
        throw new UnsupportedOperationException();
    }
}
