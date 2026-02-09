package org.example.repository;

import org.example.domain.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientRepository implements Repository<String, Patient> {
    private final Db db;

    public PatientRepository(Db db) {
        this.db = db;
    }

    @Override
    public Optional<Patient> findOne(String cnp) {
        String sql = "SELECT cnp, age_months, premature, diagnosis, severity FROM patients WHERE cnp=?";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cnp);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new Patient(
                        rs.getString("cnp"),
                        rs.getInt("age_months"),
                        rs.getBoolean("premature"),
                        rs.getString("diagnosis"),
                        rs.getInt("severity")
                ));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Patient> findAll() {
        String sql = "SELECT cnp, age_months, premature, diagnosis, severity FROM patients ORDER BY cnp";
        List<Patient> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new Patient(
                    rs.getString("cnp"),
                    rs.getInt("age_months"),
                    rs.getBoolean("premature"),
                    rs.getString("diagnosis"),
                    rs.getInt("severity")
            ));
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Patient save(Patient entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Patient update(Patient entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Patient> delete(String s) {
        throw new UnsupportedOperationException();
    }

    public List<Patient> findWaitingOrdered() {
        String sql = """
            SELECT p.cnp, p.age_months, p.premature, p.diagnosis, p.severity
            FROM patients p
            WHERE NOT EXISTS (
                SELECT 1
                FROM beds b
                WHERE b.patient_cnp = p.cnp
            )
            ORDER BY p.severity DESC
        """;
        List<Patient> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new Patient(
                    rs.getString("cnp"),
                    rs.getInt("age_months"),
                    rs.getBoolean("premature"),
                    rs.getString("diagnosis"),
                    rs.getInt("severity")
            ));
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
