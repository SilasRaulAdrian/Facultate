package org.example.service;

import org.example.domain.*;
import org.example.observer.Observable;
import org.example.observer.Observer;
import org.example.observer.RestaurantEvent;
import org.example.observer.RestaurantEventType;
import org.example.repository.MenuRepository;
import org.example.repository.OrderRepository;
import org.example.repository.TableRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RestaurantService implements Observable<RestaurantEvent> {
    private final TableRepository tableRepo;
    private final MenuRepository menuRepo;
    private final OrderRepository orderRepo;

    private final List<Observer<RestaurantEvent>> observers = new ArrayList<>();

    public RestaurantService(TableRepository tableRepo, MenuRepository menuRepo, OrderRepository orderRepo) {
        this.tableRepo = tableRepo;
        this.menuRepo = menuRepo;
        this.orderRepo = orderRepo;
    }

    public List<Table> getTables() {
        return tableRepo.findAll();
    }

    public List<MenuItem> getMenu() {
        return menuRepo.findAll();
    }

    public List<PlacedOrderRow> getPlacedOrdersRows() {
        return orderRepo.findPlacedOrdersRows();
    }

    public void placeOrder(int tableId, List<Integer> menuItemIds) {
        if (menuItemIds == null || menuItemIds.isEmpty())
            throw new IllegalArgumentException("Nu se poate plasa o comanda fara a selecta niciun produs!");

        tableRepo.findOne(tableId).orElseThrow(() -> new IllegalArgumentException("Masa inexistenta!"));

        for (Integer mid : menuItemIds) {
            menuRepo.findOne(mid).orElseThrow(() -> new IllegalArgumentException("Produs inexistent: " + mid));
        }

        Order order = new Order(
                0,
                tableId,
                new ArrayList<>(menuItemIds),
                LocalDateTime.now(),
                OrderStatus.PLACED
        );

        Order saved = orderRepo.save(order);

        notifyObservers(new RestaurantEvent(RestaurantEventType.ORDER_PLACED, saved));
    }

    @Override
    public void addObserver(Observer<RestaurantEvent> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<RestaurantEvent> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(RestaurantEvent event) {
        observers.forEach(o -> o.update(event));
    }
}
