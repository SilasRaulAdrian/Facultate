package org.example.service;

import org.example.domain.Client;
import org.example.domain.FlightRow;
import org.example.domain.Ticket;
import org.example.observer.FlightEvent;
import org.example.observer.FlightEventType;
import org.example.observer.Observable;
import org.example.observer.Observer;
import org.example.repository.ClientRepository;
import org.example.repository.FlightRepository;
import org.example.repository.TicketRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FlightService implements Observable<FlightEvent> {
    private final ClientRepository clientRepo;
    private final FlightRepository flightRepo;
    private final TicketRepository ticketRepo;

    private final List<Observer<FlightEvent>> observers = new ArrayList<>();

    public FlightService(ClientRepository clientRepo, FlightRepository flightRepo, TicketRepository ticketRepo) {
        this.clientRepo = clientRepo;
        this.flightRepo = flightRepo;
        this.ticketRepo = ticketRepo;
    }

    public Client login(String username) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Introdu username.");
        return clientRepo.findOne(username.trim())
                .orElseThrow(() -> new IllegalArgumentException("User inexistent."));
    }

    public List<String> getFromLocations() {
        return flightRepo.findDistinctFrom();
    }

    public List<String> getToLocations() {
        return flightRepo.findDistinctTo();
    }

    public int countFlights(String from, String to, LocalDate day) {
        return flightRepo.countFiltered(from, to, day);
    }

    public List<FlightRow> getFlightsPage(String from, String to, LocalDate day, int limit, int offset) {
        return flightRepo.findFilteredPage(from, to, day, limit, offset);
    }

    public Ticket buyTicket(String username, long flightId) {
        Ticket t = ticketRepo.buyTicket(username, flightId);
        notifyObservers(new FlightEvent(FlightEventType.TICKET_BOUGHT, flightId));
        return t;
    }

    @Override
    public void addObserver(Observer<FlightEvent> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<FlightEvent> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(FlightEvent e) {
        observers.forEach(o -> o.update(e));
    }
}
