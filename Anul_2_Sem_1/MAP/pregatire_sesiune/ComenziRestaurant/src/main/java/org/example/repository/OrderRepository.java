package org.example.repository;

import org.example.domain.Order;
import org.example.domain.OrderStatus;
import org.example.domain.PlacedOrderRow;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class OrderRepository implements PagingRepository<Integer, Order> {
    private final Db db;

    public OrderRepository(Db db) {
        this.db = db;
    }

    @Override
    public Optional<Order> findOne(Integer id) {
        var list = fetchOrders(
                "SELECT o.id, o.table_id, o.date_time, o.status FROM orders o WHERE o.id=?",
                List.of(id)
        );

        if (list.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(list.get(0));
    }

    @Override
    public List<Order> findAll() {
        return fetchOrders(
                "SELECT o.id, o.table_id, o.date_time, o.status FROM orders o",
                List.of()
        );
    }

    @Override
    public Order save(Order entity) {
        String insertOrder = "INSERT INTO orders(table_id, date_time, status) VALUES (?,?,?) RETURNING id";
        String insertItem = "INSERT INTO order_items(order_id, menu_item_id) VALUES (?,?)";

        try (Connection c = db.getConnection()) {
            c.setAutoCommit(false);
            try {
                int newId;

                try (PreparedStatement ps = c.prepareStatement(insertOrder)) {
                    ps.setInt(1, entity.getTable());
                    ps.setTimestamp(2, Timestamp.valueOf(entity.getDate()));
                    ps.setString(3, entity.getStatus().name());

                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        newId = rs.getInt(1);
                    }
                }

                try (PreparedStatement ps = c.prepareStatement(insertItem)) {
                    for (Integer mid : entity.getMenuItems()) {
                        ps.setInt(1, newId);
                        ps.setInt(2, mid);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                c.commit();

                return new Order(
                        newId,
                        entity.getTable(),
                        new ArrayList<>(entity.getMenuItems()),
                        entity.getDate(),
                        entity.getStatus()
                );

            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PlacedOrderRow> findPlacedOrdersRows() {
        String sqlOrders = """
            SELECT o.id, o.table_id, o.date_time
            FROM orders o
            WHERE o.status = 'PLACED'
            ORDER BY o.date_time ASC
            """;

        String sqlItems = """
            SELECT m.item
            FROM order_items oi
            JOIN menu m ON m.id = oi.menu_item_id
            WHERE oi.order_id = ?
            ORDER BY m.item
            """;

        List<PlacedOrderRow> out = new ArrayList<>();

        try (Connection c = db.getConnection();
             PreparedStatement psOrders = c.prepareStatement(sqlOrders);
             ResultSet rsOrders = psOrders.executeQuery();
             PreparedStatement psItems = c.prepareStatement(sqlItems)) {

            while (rsOrders.next()) {
                int orderId = rsOrders.getInt("id");
                int tableId = rsOrders.getInt("table_id");
                var dateTime = rsOrders.getTimestamp("date_time").toLocalDateTime();

                psItems.setInt(1, orderId);

                List<String> productsList = new ArrayList<>();
                try (ResultSet rsItems = psItems.executeQuery()) {
                    while (rsItems.next()) {
                        productsList.add(rsItems.getString("item"));
                    }
                }

                String products = String.join(", ", productsList);

                out.add(new PlacedOrderRow(tableId, dateTime, products));
            }

            return out;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order update(Order entity) {
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE orders SET status=? WHERE id=?")) {
            ps.setString(1, entity.getStatus().name());
            ps.setInt(2, entity.getId());
            ps.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Order> delete(Integer id) {
        Optional<Order> old = findOne(id);

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM orders WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return old;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Order> fetchOrders(String sql, List<Object> params) {
        List<Order> base = new ArrayList<>();

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); ++i) {
                Object p = params.get(i);
                ps.setObject(i + 1, p);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    base.add(new Order(
                            rs.getInt("id"),
                            rs.getInt("table_id"),
                            new ArrayList<>(),
                            rs.getTimestamp("date_time").toLocalDateTime(),
                            OrderStatus.valueOf(rs.getString("status").toUpperCase())
                    ));
                }
            }

            try (PreparedStatement psItems = c.prepareStatement(
                    "SELECT menu_item_id FROM order_items WHERE order_id=?")) {
                for (Order o : base) {
                    psItems.setInt(1, o.getId());
                    try (ResultSet rsi = psItems.executeQuery()) {
                        List<Integer> items = new ArrayList<>();
                        while (rsi.next()) {
                            items.add(rsi.getInt(1));
                        }

                        o.getMenuItems().clear();
                        o.getMenuItems().addAll(items);
                    }
                }
            }

            return base;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findPage(int limit, int offset) {
        return fetchOrders(
                "SELECT o.id, o.table_id, o.date_time, o.status FROM orders o " +
                        "ORDER BY o.date_time DESC LIMIT ? OFFSET ?",
                List.of(limit, offset)
        );
    }
}
