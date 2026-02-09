package org.example.repository;

import org.example.domain.Medic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedicRepository implements Repository<Integer, Medic> {
    private final Db db;

    public MedicRepository(Db db) {
        this.db = db;
    }

    @Override
    public Optional<Medic> findOne(Integer id) {
        String sql = "SELECT id,id_sectie,nume,vechime,rezident FROM medici WHERE id=?";
        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Medic> findAll() {
        String sql = "SELECT id,id_sectie,nume,vechime,rezident FROM medici ORDER BY nume";
        List<Medic> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Medic> findBySectie(int idSectie) {
        String sql = "SELECT id,id_sectie,nume,vechime,rezident FROM medici WHERE id_sectie=? ORDER BY nume";
        List<Medic> out = new ArrayList<>();
        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idSectie);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private Medic map(ResultSet rs) throws SQLException {
        return new Medic(
                rs.getInt("id"),
                rs.getInt("id_sectie"),
                rs.getString("nume"),
                rs.getInt("vechime"),
                rs.getBoolean("rezident")
        );
    }

    @Override public Medic save(Medic entity) { throw new UnsupportedOperationException(); }
    @Override public Medic update(Medic entity) { throw new UnsupportedOperationException(); }
    @Override public Optional<Medic> delete(Integer id) { throw new UnsupportedOperationException(); }
}
