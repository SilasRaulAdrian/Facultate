package org.example.repository;

import org.example.domain.Car;
import org.example.domain.MasinaStatus;
import org.example.domain.User;
import org.example.domain.UserRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarRepository implements Repository<Integer, Car> {
    private final Db db;

    public CarRepository(Db db) {
        this.db = db;
    }

    @Override
    public Optional<Car> findOne(Integer id) {
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM cars WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Car(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("base_price"),
                            rs.getString("description"),
                            MasinaStatus.valueOf(rs.getString("status")),
                            rs.getString("rejected_reason")
                    ));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Car> findAll() {
        String sql = "SELECT * FROM cars ORDER BY id";
        List<Car> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new Car(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("base_price"),
                    rs.getString("description"),
                    MasinaStatus.valueOf(rs.getString("status")),
                    rs.getString("rejected_reason")));
            return out;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Car save(Car entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Car update(Car entity) {
        try (Connection c = db.getConnection();
         PreparedStatement ps = c.prepareStatement("UPDATE cars SET status=? WHERE id=?")) {
            ps.setString(1, entity.getStatus().name());
            ps.setInt(2, entity.getId());
            ps.executeUpdate();
            return entity;
    } catch (SQLException e) {
            throw new RuntimeException(e);
    }
}

    @Override
    public Optional<Car> delete(Integer integer) {
        throw new UnsupportedOperationException();
    }

    public List<Car> findNeedsApproval() {
        String sql = "SELECT * FROM cars WHERE status = 'NEEDS_APPROVAL' ORDER BY id";
        List<Car> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new Car(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("base_price"),
                    rs.getString("description"),
                    MasinaStatus.valueOf(rs.getString("status")),
                    rs.getString("rejected_reason")));
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public int countNeedsApproval() {
        String sql = "SELECT COUNT(*) FROM cars WHERE status = 'NEEDS_APPROVAL'";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean setStatus(int carId,
                             MasinaStatus expectedStatus,
                             MasinaStatus newStatus) {
        String sql = """
        UPDATE cars
        SET status = ?
        WHERE id = ? AND status = ?
    """;

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, newStatus.name());
            ps.setInt(2, carId);
            ps.setString(3, expectedStatus.name());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
