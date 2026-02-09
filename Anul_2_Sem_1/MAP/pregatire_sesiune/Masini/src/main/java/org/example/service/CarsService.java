package org.example.service;

import org.example.domain.Car;
import org.example.domain.MasinaStatus;
import org.example.domain.User;
import org.example.observer.AppEvent;
import org.example.observer.AppEventType;
import org.example.observer.Observable;
import org.example.observer.Observer;
import org.example.repository.CarRepository;
import org.example.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class CarsService implements Observable<AppEvent> {
    private final UserRepository userRepo;
    private final CarRepository carRepo;

    private final List<Observer<AppEvent>> observers = new ArrayList<>();

    public CarsService(UserRepository userRepo, CarRepository carRepo) {
        this.userRepo = userRepo;
        this.carRepo = carRepo;
    }

    public User login(String username, String password) {
        User u = userRepo.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User inexistent."));
        if (!u.getPassword().equals(password)) {
            throw new IllegalArgumentException("Parola incorecta.");
        }

        return u;
    }

    public List<Car> getAllCars() {
        return carRepo.findAll();
    }

    public List<Car> getCarsNeedsApproval() {
        return carRepo.findNeedsApproval();
    }

    public int countNeedsApproval() {
        return carRepo.countNeedsApproval();
    }

    public void submitForApproval(int carId, int dealerId) {
        boolean ok = carRepo.setStatus(carId, MasinaStatus.NEW, MasinaStatus.NEEDS_APPROVAL);
        if (!ok) ok = carRepo.setStatus(carId, MasinaStatus.REJECTED, MasinaStatus.NEEDS_APPROVAL);

        if (!ok) {
            notifyObservers(new AppEvent(AppEventType.USER_MESSAGE, dealerId, "Status invalid, nu pot trimite."));
            return;
        }

        notifyObservers(new AppEvent(AppEventType.CARS_CHANGED, null, null));
        notifyObservers(new AppEvent(AppEventType.APPROVALS_CHANGED, null, "Cereri noi de aprobare!"));
    }

    public void approveCar(int carId) {
        carRepo.findOne(carId)
                .orElseThrow(() -> new IllegalArgumentException("Masina inexistenta."));
        carRepo.setStatus(carId, MasinaStatus.NEEDS_APPROVAL, MasinaStatus.APPROVED);
        //carRepo.update(car);
        notifyObservers(new AppEvent(AppEventType.CARS_CHANGED, null, null));
        notifyObservers(new AppEvent(AppEventType.APPROVALS_CHANGED, null, "Aprobare efectuata"));
    }

    public void rejectCar(int carId) {
        carRepo.findOne(carId)
                .orElseThrow(() -> new IllegalArgumentException("Masina inexistenta."));
        carRepo.setStatus(carId, MasinaStatus.NEEDS_APPROVAL, MasinaStatus.REJECTED);
        //carRepo.update(car);
        notifyObservers(new AppEvent(AppEventType.CARS_CHANGED, null, null));
        notifyObservers(new AppEvent(AppEventType.APPROVALS_CHANGED, null, "Respingere efectuata"));
    }

    @Override
    public void addObserver(Observer<AppEvent> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<AppEvent> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(AppEvent e) {
        observers.forEach(o -> o.update(e));
    }
}
