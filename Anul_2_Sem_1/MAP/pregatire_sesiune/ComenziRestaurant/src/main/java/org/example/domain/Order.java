package org.example.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private final int id;
    private final int table;
    private final List<Integer> menuItems;
    private final LocalDateTime date;
    private final OrderStatus status;

    public Order(int id, int table, List<Integer> menuItems, LocalDateTime date, OrderStatus status) {
        this.id = id;
        this.table = table;
        this.menuItems = menuItems;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getTable() {
        return table;
    }

    public List<Integer> getMenuItems() {
        return menuItems;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
