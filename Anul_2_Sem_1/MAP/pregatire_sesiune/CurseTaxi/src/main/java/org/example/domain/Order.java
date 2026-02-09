package org.example.domain;

import java.time.LocalDateTime;

public class Order {
    private final int id;
    private Integer driverId;
    private OrderStatus status;
    private final LocalDateTime startDate;
    private LocalDateTime endDate;
    private final String pickupAddress;
    private final String destinationAddress;
    private final String clientName;

    public Order(int id, Integer driverId, OrderStatus status,
                 LocalDateTime startDate, LocalDateTime endDate,
                 String pickupAddress, String destinationAddress, String clientName) {
        this.id = id;
        this.driverId = driverId;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pickupAddress = pickupAddress;
        this.destinationAddress = destinationAddress;
        this.clientName = clientName;
    }

    public int getId() {
        return id;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public String getClientName() {
        return clientName;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
