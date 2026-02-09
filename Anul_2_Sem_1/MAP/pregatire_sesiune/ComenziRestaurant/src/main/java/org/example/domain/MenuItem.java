package org.example.domain;

public class MenuItem {
    private final int id;
    private final String category;
    private final String item;
    private final double price;
    private final String currency;

    public MenuItem(int id, String category, String item, double price, String currency) {
        this.id = id;
        this.category = category;
        this.item = item;
        this.price = price;
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getItem() {
        return item;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }
}
