package org.example.service;

import org.example.domain.City;
import org.example.domain.RouteRow;
import org.example.domain.TrainStation;
import org.example.observer.Observable;
import org.example.observer.Observer;
import org.example.observer.TrainEvent;
import org.example.observer.TrainEventType;
import org.example.repository.CityRepository;
import org.example.repository.TrainStationRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TrainService implements Observable<TrainEvent> {
    public static final float PRICE_PER_STATION = 10.0f;

    private final CityRepository cityRepo;
    private final TrainStationRepository stationRepo;

    private final List<Observer<TrainEvent>> observers = new ArrayList<>();

    private final Map<UUID, FilterState> activeFilters = new ConcurrentHashMap<>();

    public TrainService(CityRepository cityRepo, TrainStationRepository stationRepo) {
        this.cityRepo = cityRepo;
        this.stationRepo = stationRepo;
    }

    public List<City> getCities() {
        return cityRepo.findAll();
    }

    public void updateWindowFilters(UUID windowId, String depCityId, String destCityId) {
        if (depCityId == null || destCityId == null) {
            activeFilters.remove(windowId);
        } else {
            activeFilters.put(windowId, new FilterState(depCityId, destCityId));
        }
        notifyObservers(new TrainEvent(TrainEventType.FILTERS_CHANGED));
    }

    public void closeWindow(UUID windowId) {
        activeFilters.remove(windowId);
        notifyObservers(new TrainEvent(TrainEventType.FILTERS_CHANGED));
    }

    public int countOtherWindows(UUID myId, String depCityId, String destCityId) {
        if (depCityId == null || destCityId == null) return 0;
        int cnt = 0;
        for (var e : activeFilters.entrySet()) {
            if (e.getKey().equals(myId)) continue;
            FilterState st = e.getValue();
            if (st.depCityId().equals(depCityId) && st.destCityId().equals(destCityId)) cnt++;
        }
        return cnt;
    }

    public List<RouteRow> searchRoutes(String depCityId, String destCityId, boolean directOnly) {
        if (depCityId == null || destCityId == null) return List.of();
        if (depCityId.equals(destCityId)) return List.of();

        Map<String, City> cities = new HashMap<>();
        for (City c : cityRepo.findAll()) cities.put(c.getId(), c);

        Map<String, List<TrainStation>> adj = new HashMap<>();
        for (TrainStation ts : stationRepo.findAll()) {
            adj.computeIfAbsent(ts.getDepartureCityId(), k -> new ArrayList<>()).add(ts);
        }

        List<PathEdge> path = new ArrayList<>();
        Set<String> visitedCities = new HashSet<>();
        visitedCities.add(depCityId);

        List<RouteRow> out = new ArrayList<>();
        dfs(depCityId, destCityId, directOnly, adj, visitedCities, path, out, cities);

        return out;
    }

    private void dfs(
            String currentCity,
            String destCity,
            boolean directOnly,
            Map<String, List<TrainStation>> adj,
            Set<String> visitedCities,
            List<PathEdge> path,
            List<RouteRow> out,
            Map<String, City> cities
    ) {
        if (currentCity.equals(destCity)) {
            out.add(buildRouteRow(path, cities));
            return;
        }

        for (TrainStation edge : adj.getOrDefault(currentCity, List.of())) {
            String nextCity = edge.getDestinationCityId();
            if (visitedCities.contains(nextCity)) continue;

            if (directOnly && !path.isEmpty()) {
                String firstTrain = path.get(0).trainId();
                if (!firstTrain.equals(edge.getTrainId())) continue;
            }

            visitedCities.add(nextCity);
            path.add(new PathEdge(edge.getTrainId(), currentCity, nextCity));

            dfs(nextCity, destCity, directOnly, adj, visitedCities, path, out, cities);

            path.remove(path.size() - 1);
            visitedCities.remove(nextCity);
        }
    }

    private RouteRow buildRouteRow(List<PathEdge> path, Map<String, City> cities) {
        int numberOfStations = path.size() + 1;
        double price = PRICE_PER_STATION * numberOfStations;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            PathEdge e = path.get(i);
            if (i == 0) sb.append(cities.get(e.fromCityId()).getName());
            sb.append(" —").append(e.trainId()).append("-> ");
            sb.append(cities.get(e.toCityId()).getName());
        }
        return new RouteRow(sb.toString(), price);
    }

    @Override
    public void addObserver(Observer<TrainEvent> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<TrainEvent> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(TrainEvent e) {
        observers.forEach(o -> o.update(e));
    }
}
