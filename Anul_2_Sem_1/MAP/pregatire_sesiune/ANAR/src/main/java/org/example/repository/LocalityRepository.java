package org.example.repository;

import org.example.domain.Locality;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocalityRepository implements PagingRepository<String, Locality> {
    private final Db db;

    public LocalityRepository(Db db) {
        this.db = db;
    }

    @Override
    public List<Locality> findPage(int limit, int offset) {
        String sql = """
            SELECT name, river_name, cmdr, cma
            FROM localities
            ORDER BY name
            LIMIT ? OFFSET ?
        """;
        return fetch(sql, List.of(limit, offset));
    }

    @Override
    public Optional<Locality> findOne(String name) {
        String sql = "SELECT name, river_name, cmdr, cma FROM localities WHERE name=?";
        var list = fetch(sql, List.<Object>of(name));
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<Locality> findAll() {
        String sql = "SELECT name, river_name, cmdr, cma FROM localities ORDER BY name";
        return fetch(sql, List.of());
    }

    @Override
    public Locality save(Locality entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Locality update(Locality entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Locality> delete(String s) {
        throw new UnsupportedOperationException();
    }

    private List<Locality> fetch(String sql, List<Object> params) {
        List<Locality> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Locality(
                            rs.getString("name"),
                            rs.getString("river_name"),
                            rs.getDouble("cmdr"),
                            rs.getDouble("cma")
                    ));
                }
            }
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
