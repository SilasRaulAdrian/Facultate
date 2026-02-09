package org.example.repository;

import org.example.domain.TrainStation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrainStationRepository implements PagingRepository<Integer, TrainStation> {
    private final Db db;

    public  TrainStationRepository(Db db) {
        this.db = db;
    }

    @Override
    public Optional<TrainStation> findOne(Integer id) {
        String sql = "SELECT id,train_id,departure_city_id,destination_city_id FROM train_stations WHERE id=?";
        var list = fetch(sql, List.of(id));
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<TrainStation> findAll() {
        String sql = "SELECT id,train_id,departure_city_id,destination_city_id FROM train_stations ORDER BY id";
        return fetch(sql, List.of());
    }

    @Override
    public List<TrainStation> findPage(int limit, int offset) {
        String sql = """
            SELECT id,train_id,departure_city_id,destination_city_id
            FROM train_stations
            ORDER BY id
            LIMIT ? OFFSET ?
        """;
        return fetch(sql, List.of(limit, offset));
    }

    private List<TrainStation> fetch(String sql, List<Object> params) {
        List<TrainStation> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new TrainStation(
                            rs.getInt("id"),
                            rs.getString("train_id"),
                            rs.getString("departure_city_id"),
                            rs.getString("destination_city_id")
                    ));
                }
            }
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override public TrainStation save(TrainStation entity) { throw new UnsupportedOperationException(); }
    @Override public TrainStation update(TrainStation entity) { throw new UnsupportedOperationException(); }
    @Override public Optional<TrainStation> delete(Integer id) { throw new UnsupportedOperationException(); }

    // ajuta service-ul: muchii care pleaca dintr-un oras
    public List<TrainStation> findByDepartureCity(String departureCityId) {
        String sql = """
            SELECT id,train_id,departure_city_id,destination_city_id
            FROM train_stations
            WHERE departure_city_id=?
        """;
        return fetch(sql, List.of(departureCityId));
    }
}
