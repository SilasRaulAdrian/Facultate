package org.example.service;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import org.example.domain.Driver;
import org.example.domain.Order;
import org.example.domain.OrderStatus;
import org.example.observer.Observable;
import org.example.observer.Observer;
import org.example.observer.TaxiEvent;
import org.example.observer.TaxiEventType;
import org.example.repository.DriverRepository;
import org.example.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaxiService implements Observable<TaxiEvent> {
    private final DriverRepository driverRepo;
    private final OrderRepository orderRepo;

    private final List<Observer<TaxiEvent>> observers = new ArrayList<>();

    public TaxiService(DriverRepository driverRepo, OrderRepository orderRepo) {
        this.driverRepo = driverRepo;
        this.orderRepo = orderRepo;
    }

    public List<Driver> getDrivers() {
        return driverRepo.findAll();
    }

    public List<Order> getActiveOrdersForDriver(int driverId) {
        return orderRepo.findActiveByDriver(driverId);
    }

    public void addOrder(String pickup, String dest, String client) {
        if (pickup == null || pickup.isBlank() ||
                dest == null || dest.isBlank() ||
                client == null || client.isBlank()) {
            throw new IllegalArgumentException("Completeaza pickup, destination si client!");
        }

        Order pending = new Order(
                0, null, OrderStatus.PENDING,
                LocalDateTime.now(), null,
                pickup.trim(), dest.trim(), client.trim()
        );

        Order created = orderRepo.save(pending);

        notifyObservers(new TaxiEvent(TaxiEventType.ORDERS_CHANGED, null, null));

        List<Integer> eligible = orderRepo.eligibleDriversOrdered();
        if (!eligible.isEmpty()) {
            int driverId = eligible.get(0);
            notifyObservers(new TaxiEvent(TaxiEventType.ORDER_OFFERED, driverId, created));
        }
    }

    public synchronized void acceptOrder(int orderId, int driverId) {
        orderRepo.setInProgress(orderId, driverId);
        notifyObservers(new TaxiEvent(TaxiEventType.ORDERS_CHANGED, null, null));
    }

    public void finishOrder(int orderId) {
        orderRepo.finishOrder(orderId);
        notifyObservers(new TaxiEvent(TaxiEventType.ORDERS_CHANGED, null, null));
    }

    @Override
    public void addObserver(Observer<TaxiEvent> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<TaxiEvent> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(TaxiEvent e) {
        for (Observer<TaxiEvent> o : observers) {
            o.update(e);
        }
    }
}
