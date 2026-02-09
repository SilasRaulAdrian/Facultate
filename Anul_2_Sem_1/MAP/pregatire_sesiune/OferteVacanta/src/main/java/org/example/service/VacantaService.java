package org.example.service;

import org.example.domain.*;
import org.example.observer.Observable;
import org.example.observer.Observer;
import org.example.observer.VacantaEvent;
import org.example.observer.VacantaEventType;
import org.example.repository.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VacantaService implements Observable<VacantaEvent> {
    private final LocationRepository locRepo;
    private final HotelRepository hotelRepo;
    private final SpecialOfferRepository offerRepo;
    private final ClientRepository clientRepo;
    private final ReservationRepository resRepo;

    private final List<Observer<VacantaEvent>> observers = new ArrayList<>();

    public VacantaService(LocationRepository locRepo,
                          HotelRepository hotelRepo,
                          SpecialOfferRepository offerRepo,
                          ClientRepository clientRepo,
                          ReservationRepository resRepo) {
        this.locRepo = locRepo;
        this.hotelRepo = hotelRepo;
        this.offerRepo = offerRepo;
        this.clientRepo = clientRepo;
        this.resRepo = resRepo;
    }

    public List<Location> getLocations() { return locRepo.findAll(); }

    public List<Hotel> getAllHotels() {
        return hotelRepo.findAll();
    }

    public List<Hotel> getHotelsForLocation(long locationId) {
        return hotelRepo.findByLocation(locationId);
    }

    public List<SpecialOffer> getOffersForHotelInterval(long hotelId, LocalDate start, LocalDate end) {
        return offerRepo.findForHotelInInterval(hotelId, start, end);
    }

    public Client getClient(long clientId) {
        return clientRepo.findOne(clientId).orElseThrow(() -> new IllegalArgumentException("Client inexistent"));
    }

    public List<ClientOfferRow> getActiveOffersForClient(long clientId) {
        return offerRepo.findActiveOffersForClient(clientId, LocalDate.now());
    }

    public void makeReservation(long clientId, long hotelId, LocalDate startDate, int noNights) {
        if (noNights <= 0) throw new IllegalArgumentException("Numar nopti invalid.");

        resRepo.addReservation(clientId, hotelId, startDate, noNights);

        List<Long> others = resRepo.otherClientsWithSameHobby(clientId);
        for (Long otherId : others) {
            notifyObservers(new VacantaEvent(
                    VacantaEventType.RESERVATIONS_CHANGED,
                    otherId,
                    "Inca un utilizator cu hobby-ul tau a facut o rezervare la hotelul " + hotelId + "."
            ));
        }
    }

    @Override
    public void addObserver(Observer<VacantaEvent> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<VacantaEvent> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(VacantaEvent e) {
        observers.forEach(o -> o.update(e));
    }
}
