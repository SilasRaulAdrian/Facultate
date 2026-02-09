package org.example.repository;

import org.example.domain.Bed;
import org.example.domain.BedType;
import org.example.domain.OccupiedBedRow;

import java.sql.*;
import java.util.*;

public class BedRepository implements Repository<Integer, Bed> {
    private final Db db;

    public BedRepository(Db db) {
        this.db = db;
    }

    @Override
    public Optional<Bed> findOne(Integer id) {
        String sql = "SELECT id, type, ventilation, patient_cnp FROM beds WHERE id=?";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new Bed(
                        rs.getInt("id"),
                        BedType.valueOf(rs.getString("type")),
                        rs.getBoolean("ventilation"),
                        rs.getString("patient_cnp")
                ));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Bed> findAll() {
        String sql = "SELECT id, type, ventilation, patient_cnp FROM beds ORDER BY type, id";
        List<Bed> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new Bed(
                    rs.getInt("id"),
                    BedType.valueOf(rs.getString("type")),
                    rs.getBoolean("ventilation"),
                    rs.getString("patient_cnp")
            ));
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Bed save(Bed entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bed update(Bed entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Bed> delete(Integer integer) {
        throw new UnsupportedOperationException();
    }

    public int countOccupied() {
        String sql = "SELECT COUNT(*) FROM beds WHERE patient_cnp IS NOT NULL";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public Map<BedType, Integer> countFreeByType() {
        String sql = """
            SELECT type, COUNT(*) AS free_cnt
            FROM beds
            WHERE patient_cnp IS NULL
            GROUP BY type
        """;
        Map<BedType, Integer> out = new EnumMap<>(BedType.class);
        for (BedType t : BedType.values()) out.put(t, 0);

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.put(BedType.valueOf(rs.getString("type")), rs.getInt("free_cnt"));
            }
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Bed> findFreeBeds(BedType type, Boolean ventilationOnly) {
        String sql = """
            SELECT id, type, ventilation, patient_cnp
            FROM beds
            WHERE patient_cnp IS NULL
                AND type = ?
                AND (? IS NULL OR ventilation = ?)
            ORDER BY id
        """;
        List<Bed> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, type.name());
            if (ventilationOnly == null) {
                ps.setNull(2, Types.BOOLEAN);
                ps.setNull(3, Types.BOOLEAN);
            } else {
                ps.setBoolean(2, ventilationOnly);
                ps.setBoolean(3, ventilationOnly);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(new Bed(
                        rs.getInt("id"),
                        BedType.valueOf(rs.getString("type")),
                        rs.getBoolean("ventilation"),
                        rs.getString("patient_cnp")
                ));
            }
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<OccupiedBedRow> findOccupiedRows() {
        String sql = """
            SELECT b.id AS bed_id, b.type, b.ventilation, b.patient_cnp,
                   p.severity, p.diagnosis
            FROM beds b
            JOIN patients p ON p.cnp = b.patient_cnp
            ORDER BY b.type, b.id
        """;
        List<OccupiedBedRow> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new OccupiedBedRow(
                        rs.getInt("bed_id"),
                        BedType.valueOf(rs.getString("type")),
                        rs.getBoolean("ventilation"),
                        rs.getString("patient_cnp"),
                        rs.getInt("severity"),
                        rs.getString("diagnosis")
                ));
            }
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void assignPatientToBed(String patientCnp, int bedId) {
        String lockBed = "SELECT patient_cnp FROM beds WHERE id=? FOR UPDATE";
        String update = "UPDATE beds SET patient_cnp=? WHERE id=? AND patient_cnp IS NULL";

        try (Connection c = db.getConnection()) {
            c.setAutoCommit(false);

            // verific ca pacientul nu e deja in pat
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT 1 FROM beds WHERE patient_cnp=?")) {
                ps.setString(1, patientCnp);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        c.rollback();
                        throw new IllegalStateException("Patient is already assigned to a bed");
                    }
                }
            }

            try (PreparedStatement ps = c.prepareStatement(lockBed)) {
                ps.setInt(1, bedId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        c.rollback();
                        throw new NoSuchElementException("Bed not found");
                    }
                }
            }

            int updated;
            try (PreparedStatement ps = c.prepareStatement(update)) {
                ps.setString(1, patientCnp);
                ps.setInt(2, bedId);
                updated = ps.executeUpdate();
            }

            if (updated == 0) {
                c.rollback();
                throw new IllegalStateException("Bed is already occupied");
            }

            c.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
