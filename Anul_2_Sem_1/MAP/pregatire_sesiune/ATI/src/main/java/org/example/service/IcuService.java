package org.example.service;

import org.example.domain.Bed;
import org.example.domain.BedType;
import org.example.domain.OccupiedBedRow;
import org.example.domain.Patient;
import org.example.observer.IcuEvent;
import org.example.observer.IcuEventType;
import org.example.observer.Observable;
import org.example.observer.Observer;
import org.example.repository.BedRepository;
import org.example.repository.PatientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IcuService implements Observable<IcuEvent> {
    private final BedRepository bedRepo;
    private final PatientRepository patientRepo;

    private final List<Observer<IcuEvent>> observers = new ArrayList<>();

    public IcuService(BedRepository bedRepo, PatientRepository patientRepo) {
        this.bedRepo = bedRepo;
        this.patientRepo = patientRepo;
    }

    public List<OccupiedBedRow> getOccupiedBeds() {
        return bedRepo.findOccupiedRows();
    }

    public int getOccupiedCount() {
        return bedRepo.countOccupied();
    }

    public Map<BedType, Integer> getFreeByType() {
        return bedRepo.countFreeByType();
    }

    public List<Patient> getWaitingPatients() {
        return patientRepo.findWaitingOrdered();
    }

    public List<Bed> getFreeBeds(BedType type, Boolean ventilationOnly) {
        return bedRepo.findFreeBeds(type, ventilationOnly);
    }

    public void assign(String patientCnp, int bedId) {
        if (patientCnp == null || patientCnp.isBlank())
            throw new IllegalArgumentException("Selecteaza un pacient.");
        bedRepo.assignPatientToBed(patientCnp, bedId);
        notifyObservers(new IcuEvent(IcuEventType.DATA_CHANGED));
    }

    @Override
    public void addObserver(Observer<IcuEvent> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<IcuEvent> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(IcuEvent e) {
        observers.forEach(o -> o.update(e));
    }
}
