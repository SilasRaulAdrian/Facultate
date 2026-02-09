package org.example.repository;

import org.example.domain.Sectie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SectieRepository implements Repository<Integer, Sectie> {
    private final Db db;

    public SectieRepository(Db db) {
        this.db = db;
    }

    @Override
    public Optional<Sectie> findOne(Integer id) {
        String sql = "SELECT id,nume,id_sef_sectie,pret_per_consultatie,durata_maxima_ore FROM sectii WHERE id=?";
        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Sectie> findAll() {
        String sql = "SELECT id,nume,id_sef_sectie,pret_per_consultatie,durata_maxima_ore FROM sectii ORDER BY nume";
        List<Sectie> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private Sectie map(ResultSet rs) throws SQLException {
        return new Sectie(
                rs.getInt("id"),
                rs.getString("nume"),
                rs.getInt("id_sef_sectie"),
                rs.getDouble("pret_per_consultatie"),
                rs.getInt("durata_maxima_ore")
        );
    }

    @Override public Sectie save(Sectie entity) { throw new UnsupportedOperationException(); }
    @Override public Sectie update(Sectie entity) { throw new UnsupportedOperationException(); }
    @Override public Optional<Sectie> delete(Integer id) { throw new UnsupportedOperationException(); }
}
