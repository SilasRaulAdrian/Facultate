package org.example.service;

import org.example.event.RaceEvent;
import org.example.exceptions.EventNotFoundException;
import org.example.model.Lane;
import org.example.model.SwimmingDuck;
import org.example.repository.EventRepository;
import org.example.repository.InMemoryRepository;
import org.example.validation.EventValidationStrategy;
import org.example.validation.RaceEventValidationStrategy;

import java.util.List;

/**
 * Service pentru gestionarea evenimentelor
 */
public class EventService {
    private final InMemoryRepository userRepo;
    private final EventRepository eventRepo;
    private final EventValidationStrategy eventValidator = new EventValidationStrategy();
    private final RaceEventValidationStrategy raceValidator = new RaceEventValidationStrategy();

    /**
     * Constructor pentru EventService
     * @param userRepo InMemoryRepository pentru utilizatori
     * @param eventRepo EventRepository pentru evenimente
     */
    public EventService(InMemoryRepository userRepo, EventRepository eventRepo) {
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;

        loadUsersFromCsv();
        loadEventsFromCsv();
    }

    /**
     * Incarca utilizatorii din fisier
     */
    private void loadUsersFromCsv() {
        if (userRepo.getAllUsers().isEmpty()) {
            userRepo.loadUsers().forEach(userRepo::addUser);
        }
    }

    /**
     * Incarca evenimentele din fisier
     */
    private void loadEventsFromCsv() {
        List<RaceEvent> events = eventRepo.loadEvents(userRepo.getAllUsers());
        for (RaceEvent event : events) {
            eventRepo.addEvent(event);
        }
    }

    /**
     * Creeaza si porneste o cursa
     *
     * @param lanes Lista de piste pentru cursa
     */
    public void createAndStartRace(List<Lane> lanes) {
        List<SwimmingDuck> ducks = userRepo.getAllUsers().stream()
                .filter(java.util.Objects::nonNull)
                .filter(user -> user instanceof SwimmingDuck)
                .map(user -> (SwimmingDuck) user)
                .toList();

        long nextId = 1L;
        List<RaceEvent> existingEvents = eventRepo.getAllEvents();
        if (!existingEvents.isEmpty()) {
            nextId = existingEvents.stream()
                    .mapToLong(RaceEvent::getId)
                    .max()
                    .orElse(0L) + 1;
        }

        RaceEvent event = new RaceEvent(nextId, ducks, lanes);

        raceValidator.validate(event);
        eventValidator.validate(event);

        eventRepo.addEvent(event);
        event.startRace();
    }

    /**
     * Elimina o cursa
     * @param id Long, id-ul cursei de eliminat
     */
    public void removeRace(Long id) {
        RaceEvent event = eventRepo.findById(id);
        if (event == null) {
            throw new EventNotFoundException(id);
        }
        eventValidator.validate(event);
        eventRepo.removeEvent(id);
    }

    /**
     * Returneaza toate evenimentele
     * @return List<RaceEvent> Lista de RaceEvent
     */
    public List<RaceEvent> getAllEvents() {
        return eventRepo.getAllEvents();
    }
}
