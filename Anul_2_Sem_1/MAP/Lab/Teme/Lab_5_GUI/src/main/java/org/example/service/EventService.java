package org.example.service;

import org.example.event.RaceEvent;
import org.example.exceptions.EventNotFoundException;
import org.example.model.Lane;
import org.example.model.SwimmingDuck;
import org.example.model.User;
import org.example.repository.EventRepository;
import org.example.repository.UserRepository;
import org.example.validation.EventValidationStrategy;
import org.example.validation.RaceEventValidationStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Service pentru gestionarea evenimentelor
 */
public class EventService {
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
     * Creeaza si porneste o cursa
     * @param lanes Lista de piste pentru cursa
     */
    public void createAndStartRace(List<Lane> lanes) {
        List<User> users = new ArrayList<>();
        for (User u : userRepo.getAll()) {
            users.add(u);
        }

        List<SwimmingDuck> ducks = users.stream()
                .filter(java.util.Objects::nonNull)
                .filter(user -> user instanceof SwimmingDuck)
                .map(user -> (SwimmingDuck) user)
                .toList();

        RaceEvent event = new RaceEvent(null, ducks, lanes);

        raceValidator.validate(event);
        eventValidator.validate(event);

        eventRepo.save(event).ifPresent(RaceEvent::startRace);
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
    }

}
