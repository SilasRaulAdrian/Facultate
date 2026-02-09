package org.example.repository;

import org.example.domain.Order;
import org.example.domain.OrderStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepository implements PagingRepository<Integer, Order> {
    private final Db db;

    public OrderRepository(Db db) {
        this.db = db;
    }

    @Override
    public List<Order> findPage(int limit, int offset) {
        String sql = """
            SELECT id, driver_id, status, start_date, end_date, pickup_address, destination_address, client_name
            FROM orders
            ORDER BY start_date DESC
            LIMIT ? OFFSET ?
        """;

        return fetch(sql, List.of(limit, offset));
    }

    @Override
    public Optional<Order> findOne(Integer id) {
        String sql = """
            SELECT id, driver_id, status, start_date, end_date, pickup_address, destination_address, client_name
            FROM orders WHERE id=?
        """;

        var list = fetch(sql, List.<Object>of(id));
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<Order> findAll() {
        String sql = """
            SELECT id, driver_id, status, start_date, end_date, pickup_address, destination_address, client_name
            FROM orders
            ORDER BY start_date DESC
        """;

        return fetch(sql, List.of());
    }

    private List<Order> fetch(String sql, List<Object> params) {
        List<Order> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(map(rs));
                }
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Order map(ResultSet rs) throws SQLException {
        Integer driverId = rs.getObject("driver_id") == null ? null : rs.getInt("driver_id");
        Timestamp end = rs.getTimestamp("end_date");
        return new Order(
                rs.getInt("id"),
                driverId,
                OrderStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("start_date").toLocalDateTime(),
                end == null ? null : end.toLocalDateTime(),
                rs.getString("pickup_address"),
                rs.getString("destination_address"),
                rs.getString("client_name")
        );
    }

    @Override
    public Order save(Order entity) {
        String sql = """
            INSERT INTO orders(driver_id,status,start_date,end_date,pickup_address,destination_address,client_name)
            VALUES (NULL,'PENDING',?,NULL,?,?,?)
            RETURNING id, driver_id, status, start_date, end_date, pickup_address, destination_address, client_name
        """;

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(entity.getStartDate()));
            ps.setString(2, entity.getPickupAddress());
            ps.setString(3, entity.getDestinationAddress());
            ps.setString(4, entity.getClientName());

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return map(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order update(Order entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Order> delete(Integer id) {
        throw new UnsupportedOperationException();
    }

    public List<Order> findActiveByDriver(int driverId) {
        String sql = """
            SELECT id, driver_id, status, start_date, end_date, pickup_address, destination_address, client_name
            FROM orders
            WHERE driver_id=? AND status='IN_PROGRESS'
            ORDER BY start_date ASC
        """;
        return fetch(sql, List.of(driverId));
    }

    public void setInProgress(int orderId, int driverId) {
        String sql = "UPDATE orders SET status='IN_PROGRESS', driver_id=? WHERE id=? AND status='PENDING'";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, driverId);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void finishOrder(int orderId) {
        String sql = "UPDATE orders SET status='FINISHED', end_date=? WHERE id=? AND status='IN_PROGRESS'";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Integer> eligibleDriversOrdered() {
        String sql = """
            SELECT d.id, MAX(o.end_date) AS last_end
            FROM drivers d
            LEFT JOIN orders o ON o.driver_id=d.id AND o.status='FINISHED'
            WHERE NOT EXISTS (
                SELECT 1 FROM orders oi
                WHERE oi.driver_id=d.id AND oi.status='IN_PROGRESS'
            )
            GROUP BY d.id
            ORDER BY last_end NULLS FIRST
        """;

        List<Integer> ids = new ArrayList<>();

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }

            return ids;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
