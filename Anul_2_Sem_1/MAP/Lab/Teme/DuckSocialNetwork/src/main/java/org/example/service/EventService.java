package org.example.service;

import org.example.event.RaceEvent;
import org.example.exceptions.EventNotFoundException;
import org.example.model.Lane;
import org.example.model.SwimmingDuck;
import org.example.model.User;
import org.example.observer.Observable;
import org.example.observer.Signal;
import org.example.repository.EventRepository;
import org.example.repository.UserRepository;
import org.example.validation.EventValidationStrategy;
import org.example.validation.RaceEventValidationStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Service pentru gestionarea evenimentelor
 */
public class EventService implements Observable {
    private final UserRepository userRepo;
    private final EventRepository eventRepo;
    private final EventValidationStrategy eventValidator = new EventValidationStrategy();
    private final RaceEventValidationStrategy raceValidator = new RaceEventValidationStrategy();

    /**
     * Constructor pentru EventService
     * @param userRepo UserRepository pentru utilizatori
     * @param eventRepo EventRepository pentru evenimente
     */
    public EventService(UserRepository userRepo, EventRepository eventRepo) {
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;
    }

    /**
     * Elimina o cursa
     * @param id Long, id-ul cursei de eliminat
     * @throws EventNotFoundException daca evenimentul nu exista
     */
    public void removeRace(Long id) {
        RaceEvent event = eventRepo.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        eventValidator.validate(event);
        eventRepo.delete(id);

        notifyObservers(new Signal("EVENT_DELETED"));
    }

    /**
     * Returneaza toate evenimentele
     * @return List<RaceEvent> Lista de RaceEvent
     */
    public List<RaceEvent> getAllEvents() {
        List<RaceEvent> list = new ArrayList<>();
        for (RaceEvent event : eventRepo.getAll()) {
            list.add(event);
        }
        return list;
    }

    /**
     * Gaseste un eveniment dupa ID
     * @param id Long, id-ul evenimentului
     * @return RaceEvent, evenimentul gasit
     * @throws EventNotFoundException daca evenimentul nu exista
     */
    public RaceEvent findEventById(Long id) {
        return eventRepo.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
    }

    /**
     * Actualizeaza un eveniment
     *
     * @param event RaceEvent, evenimentul de actualizat
     * @throws EventNotFoundException daca evenimentul nu exista
     */
    public void updateEvent(RaceEvent event) {
        if (event.getId() == null || eventRepo.findById(event.getId()).isEmpty()) {
            throw new EventNotFoundException(event.getId());
        }

        raceValidator.validate(event);
        eventValidator.validate(event);

        eventRepo.update(event)
                .orElseThrow(() -> new EventNotFoundException(event.getId()));

        notifyObservers(new Signal("EVENT_UPDATED"));
    }

    /**
     * Notifica toti abonatii si administratorul despre un eveniment
     * @param event RaceEvent, evenimentul
     * @param message String, mesajul de notificare
     * @param senderId Long, id-ul expeditorului
     */
    private void notifySubscribersAndAdmin(RaceEvent event, String message, Long senderId) {
        event.getSubscribers().forEach(user -> {
            if (user != null && !user.getId().equals(senderId)) {
                user.receiveMessage(message);
            }
        });

        userRepo.getAll().forEach(user -> {
            if (user.getUsername().equals("admin") && !user.getId().equals(senderId)) {
                user.receiveMessage(message);
            }
        });
    }

    /**
     * Aboneaza un utilizator la un eveniment
     * @param eventId Long, id-ul evenimentului
     * @param userId Long, id-ul utilizatorului
     */
    public void subscribeUser(Long eventId, Long userId) {
        RaceEvent event = eventRepo.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        event.subscribe(user);
        eventRepo.update(event);

        notifySubscribersAndAdmin(
                event,
                user.getUsername() + " subscribed to Event #" + eventId,
                userId
        );

        notifyObservers(new Signal("EVENT_UPDATED"));
    }

    /**
     * Dezaboneaza un utilizator de la un eveniment
     * @param eventId Long, id-ul evenimentului
     * @param userId Long, id-ul utilizatorului
     */
    public void unsubscribeUser(Long eventId, Long userId) {
        RaceEvent event = eventRepo.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        event.unsubscribe(user);
        eventRepo.update(event);

        notifySubscribersAndAdmin(
                event,
                user.getUsername() + " unsubscribed from Event #" + eventId,
                userId
        );

        notifyObservers(new Signal("EVENT_UPDATED"));
    }

    /**
     * Inregistreaza o rata la un eveniment
     * @param eventId Long, id-ul evenimentului
     * @param duckId Long, id-ul ratei
     */
    public void registerDuckToEvent(Long eventId, Long duckId) {
        RaceEvent event = eventRepo.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        if (event.getDucks().size() >= event.getLanes().size()) {
            throw new RuntimeException("No more lanes available! This race is full ("
                    + event.getLanes().size() + "/" + event.getLanes().size() + ").");
        }

        User user = userRepo.findById(duckId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!(user instanceof SwimmingDuck)) {
            throw new RuntimeException("Only SwimmingDucks can participate in races!");
        }

        SwimmingDuck duck = (SwimmingDuck) user;

        if (event.getDucks().stream().anyMatch(d -> d.getId().equals(duckId))) {
            throw new RuntimeException("Duck already registered for this event!");
        }

        event.getDucks().add(duck);
        eventRepo.update(event);

        notifySubscribersAndAdmin(
                event,
                duck.getUsername() + " joined the race for Event #" + eventId,
                duckId
        );

        notifyObservers(new Signal("EVENT_UPDATED"));
    }

    /**
     * Deinregistreaza o rata de la un eveniment
     * @param eventId Long, id-ul evenimentului
     * @param duckId Long, id-ul ratei
     */
    public void unregisterDuckFromEvent(Long eventId, Long duckId) {
        RaceEvent event = eventRepo.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        User user = userRepo.findById(duckId).orElse(null);

        event.getDucks().removeIf(d -> d.getId().equals(duckId));
        eventRepo.update(event);

        if (user != null) {
            notifySubscribersAndAdmin(
                    event,
                    user.getUsername() + " left the race for Event #" + eventId,
                    duckId
            );
        }

        notifyObservers(new Signal("EVENT_UPDATED"));
    }

    /**
     * Creeaza o cursa goala cu pistele specificate
     * @param lanes Lista de piste pentru cursa
     */
    public void createEmptyRace(List<Lane> lanes) {
        RaceEvent event = new RaceEvent(null, new ArrayList<>(), lanes);
        raceValidator.validate(event);
        eventValidator.validate(event);
        eventRepo.save(event);

        notifyObservers(new Signal("EVENT_CREATED"));
    }
}
