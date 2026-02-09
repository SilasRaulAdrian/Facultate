package org.example.service;

import org.example.domain.AlertRow;
import org.example.domain.Locality;
import org.example.domain.RiskLevel;
import org.example.domain.River;
import org.example.observer.AnarEvent;
import org.example.observer.AnarEventType;
import org.example.observer.Observable;
import org.example.observer.Observer;
import org.example.repository.LocalityRepository;
import org.example.repository.RiverRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnarService implements Observable<AnarEvent> {
    private final RiverRepository riverRepo;
    private final LocalityRepository locRepo;
    private final List<Observer<AnarEvent>> observers = new ArrayList<>();

    public AnarService(RiverRepository riverRepo, LocalityRepository locRepo) {
        this.riverRepo = riverRepo;
        this.locRepo = locRepo;
    }

    public List<River> getAllRivers() {
        return riverRepo.findAll();
    }

    public List<Locality> getAllLocalities() {
        return locRepo.findAll();
    }

    public void updateRiverLevel(String riverName, double newLevel) {
        if (newLevel < 0)
            throw new IllegalArgumentException("Nivelul nu poate fi negativ!");

        River r = riverRepo.findOne(riverName).orElseThrow(() -> new IllegalArgumentException("Rau inexistent"));
        r.setAvgLevel(newLevel);
        riverRepo.update(r);

        notifyObservers(new AnarEvent(AnarEventType.RIVER_LEVEL_CHANGED, r));
    }

    public RiskLevel riskFor(Locality l, Map<String, Double> riverLevels) {
        double level = riverLevels.getOrDefault(l.getRiverName(), 0.0);

        if (level > l.getCma()) return RiskLevel.MAJOR;
        if (level > l.getCmdr()) return RiskLevel.MEDIUM;
        return RiskLevel.REDUCED;
    }

    public List<AlertRow> computeAlerts() {
        Map<String, Double> levels = new HashMap<>();
        for (River r : getAllRivers()) {
            levels.put(r.getName(), r.getAvgLevel());
        }

        List<AlertRow> rows = new ArrayList<>();
        for (Locality l : getAllLocalities()) {
            RiskLevel risk = riskFor(l, levels);
            rows.add(new AlertRow(
                    l.getName(),
                    l.getRiverName(),
                    levels.getOrDefault(l.getRiverName(), 0.0),
                    l.getCmdr(),
                    l.getCma(),
                    risk
            ));
        }

        return rows;
    }

    public List<River> getRiversPage(int limit, int offset) {
        return riverRepo.findPage(limit, offset);
    }

    @Override
    public void addObserver(Observer<AnarEvent> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<AnarEvent> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(AnarEvent e) {
        observers.forEach(o -> o.update(e));
    }
}
