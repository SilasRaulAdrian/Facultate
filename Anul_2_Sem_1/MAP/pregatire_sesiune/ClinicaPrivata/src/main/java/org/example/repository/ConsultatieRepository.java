package org.example.repository;

import org.example.domain.Consultatie;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConsultatieRepository implements PagingRepository<Long, Consultatie> {
    private final Db db;

    public ConsultatieRepository(Db db) {
        this.db = db;
    }

    @Override
    public List<Consultatie> findPage(int limit, int offset) {
        String sql = """
            SELECT id,id_medic,cnp_pacient,nume_pacient,data,ora
            FROM consultatii
            ORDER BY data,ora
            LIMIT ? OFFSET ?
        """;
        return fetch(sql, List.of(limit, offset));
    }

    @Override
    public Optional<Consultatie> findOne(Long id) {
        String sql = "SELECT id,id_medic,cnp_pacient,nume_pacient,data,ora FROM consultatii WHERE id=?";
        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new Consultatie(
                        rs.getLong("id"),
                        rs.getInt("id_medic"),
                        rs.getString("cnp_pacient"),
                        rs.getString("nume_pacient"),
                        rs.getDate("data").toLocalDate(),
                        rs.getTime("ora").toLocalTime()
                ));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Consultatie> findAll() {
        String sql = "SELECT id,id_medic,cnp_pacient,nume_pacient,data,ora FROM consultatii ORDER BY data,ora";
        return fetch(sql, List.of());
    }

    public List<Consultatie> findFutureForMedic(int idMedic) {
        String sql = """
            SELECT id,id_medic,cnp_pacient,nume_pacient,data,ora
            FROM consultatii
            WHERE id_medic=? AND (data > CURRENT_DATE OR (data = CURRENT_DATE AND ora >= CURRENT_TIME))
            ORDER BY data ASC, ora ASC
        """;
        return fetch(sql, List.of(idMedic));
    }

    public boolean existsOverlapInSectie(int sectieId, LocalDate date, LocalTime start, LocalTime endExclusive) {
        String sql = """
            SELECT 1
            FROM consultatii c
            JOIN medici m ON m.id = c.id_medic
            JOIN sectii s ON s.id = m.id_sectie
            WHERE s.id = ? AND c.data = ? AND c.ora >= ? AND c.ora < ?
            LIMIT 1
        """;
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sectieId);
            ps.setDate(2, Date.valueOf(date));
            ps.setTime(3, Time.valueOf(start));
            ps.setTime(4, Time.valueOf(endExclusive));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Consultatie save(Consultatie entity) {
        String sql = """
            INSERT INTO consultatii(id_medic, cnp_pacient, nume_pacient, data, ora)
            VALUES (?,?,?,?,?)
            RETURNING id,id_medic,cnp_pacient,nume_pacient,data,ora
        """;
        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdMedic());
            ps.setString(2, entity.getCnpPacient());
            ps.setString(3, entity.getNumePacient());
            ps.setDate(4, Date.valueOf(entity.getData()));
            ps.setTime(5, Time.valueOf(entity.getOra()));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return new Consultatie(
                        rs.getLong("id"),
                        rs.getInt("id_medic"),
                        rs.getString("cnp_pacient"),
                        rs.getString("nume_pacient"),
                        rs.getDate("data").toLocalDate(),
                        rs.getTime("ora").toLocalTime()
                );
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private List<Consultatie> fetch(String sql, List<Object> params) {
        List<Consultatie> out = new ArrayList<>();
        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(new Consultatie(
                        rs.getLong("id"),
                        rs.getInt("id_medic"),
                        rs.getString("cnp_pacient"),
                        rs.getString("nume_pacient"),
                        rs.getDate("data").toLocalDate(),
                        rs.getTime("ora").toLocalTime()
                ));
            }
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Consultatie update(Consultatie entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Consultatie> delete(Long aLong) {
        throw new UnsupportedOperationException();
    }
}
