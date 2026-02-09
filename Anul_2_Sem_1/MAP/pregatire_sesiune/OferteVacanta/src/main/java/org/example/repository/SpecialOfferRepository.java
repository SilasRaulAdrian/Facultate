package org.example.repository;

import org.example.domain.ClientOfferRow;
import org.example.domain.SpecialOffer;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpecialOfferRepository implements PagingRepository<Long, SpecialOffer> {
    private final Db db;

    public SpecialOfferRepository(Db db) {
        this.db = db;
    }

    @Override
    public List<SpecialOffer> findPage(int limit, int offset) {
        String sql = "SELECT * FROM special_offers ORDER BY start_date DESC LIMIT ? OFFSET ?";
        List<SpecialOffer> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit); ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(new SpecialOffer(
                        rs.getLong("special_offer_id"),
                        rs.getLong("hotel_id"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getInt("percents")
                ));
            }
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Optional<SpecialOffer> findOne(Long id) {
        String sql = "SELECT * FROM special_offers WHERE special_offer_id=?";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new SpecialOffer(
                        rs.getLong("special_offer_id"),
                        rs.getLong("hotel_id"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getInt("percents")
                ));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<SpecialOffer> findAll() {
        String sql = "SELECT * FROM special_offers ORDER BY start_date DESC";
        List<SpecialOffer> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new SpecialOffer(
                    rs.getLong("special_offer_id"),
                    rs.getLong("hotel_id"),
                    rs.getDate("start_date").toLocalDate(),
                    rs.getDate("end_date").toLocalDate(),
                    rs.getInt("percents")
            ));
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<SpecialOffer> findForHotelInInterval(long hotelId, LocalDate start, LocalDate end) {
        String sql = """
            SELECT * FROM special_offers
            WHERE hotel_id=? 
                AND NOT (end_date < ? OR start_date > ?)
            ORDER BY start_date
        """;
        List<SpecialOffer> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, hotelId);
            ps.setDate(2, Date.valueOf(start));
            ps.setDate(3, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(new SpecialOffer(
                        rs.getLong("special_offer_id"),
                        rs.getLong("hotel_id"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getInt("percents")
                ));
            }
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<ClientOfferRow> findActiveOffersForClient(long clientId, LocalDate today) {
        String sql = """
            SELECT h.hotel_name, l.location_name, so.start_date, so.end_date, so.percents
            FROM clients c
            JOIN special_offers so ON TRUE
            JOIN hotels h ON h.hotel_id = so.hotel_id
            JOIN locations l ON l.location_id = h.location_id
            WHERE c.client_id = ?
                AND so.end_date >= ?
                AND c.fidelity_grade > so.percents
        """;
        List<ClientOfferRow> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, clientId);
            ps.setDate(2, Date.valueOf(today));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new ClientOfferRow(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getDate(3).toLocalDate(),
                            rs.getDate(4).toLocalDate(),
                            rs.getInt(5)
                    ));
                }
            }
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public SpecialOffer save(SpecialOffer entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpecialOffer update(SpecialOffer entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<SpecialOffer> delete(Long aLong) {
        throw new UnsupportedOperationException();
    }
}
