package org.example.repository;

import org.example.event.RaceEvent;
import org.example.model.Duck;
import org.example.model.Lane;
import org.example.model.SwimmingDuck;
import org.example.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository pentru gestionarea evenimentelor de tip RaceEvent
 */
public class EventRepository {
    private final List<RaceEvent> events = new ArrayList<>();
    private final String eventsFile;

    /**
     * Constructor pentru clasa EventRepository
     * @param eventsFile String, numele fisierului dedicat evenimentelor
     */
    public EventRepository(String eventsFile) {
        this.eventsFile = eventsFile;
    }

    /**
     * Adauga un eveniment în repository
     * @param event RaceEvent, evenimentul de adaugat
     */
    public void addEvent(RaceEvent event) {
        events.add(event);
        saveEvents(getAllEvents());
    }

    /**
     * Elimina un eveniment din repository
     * @param id Long, id-ul evenimentului de eliminat
     */
    public void removeEvent(Long id) {
        RaceEvent event = events.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
        events.remove(event);
        saveEvents(getAllEvents());
    }

    /**
     * Gaseste un eveniment dupa id
     * @param id Long, id-ul evenimentului de gasit
     * @return RaceEvent, evenimentul gasit sau null daca nu exista
     */
    public RaceEvent findById(Long id) {
        return events.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returneaza toate evenimentele existente
     * @return List<RaceEvent>, lista de evenimente
     */
    public List<RaceEvent> getAllEvents() {
        return new ArrayList<>(events);
    }

    /**
     * Incarca evenimentele din fisier
     * @param users List<User>, lista de utilizatori pentru a asocia participantii la evenimente
     * @return List<RaceEvent>, lista de evenimente incarcate
     */
    public List<RaceEvent> loadEvents(List<User> users) {
        List<RaceEvent> events = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(eventsFile))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                Long id = Long.parseLong(parts[0]);
                String[] participants = parts[1].split("\\|");
                String[] laneIds = parts[2].split("\\|");

                List<SwimmingDuck> ducks = new ArrayList<>();
                for (String username : participants) {
                    users.stream()
                            .filter(u -> u instanceof SwimmingDuck && u.getUsername().equals(username))
                            .findFirst()
                            .ifPresent(u -> ducks.add((SwimmingDuck) u));
                }

                List<Lane> lanes = new ArrayList<>();
                for (String laneId : laneIds) {
                    lanes.add(new Lane(Integer.parseInt(laneId), Double.parseDouble(laneId) * 100)); // exemplu simplu
                }

                events.add(new RaceEvent(id, ducks, lanes));
            }
        } catch (Exception e) {
            System.out.println("Error loading events: " + e.getMessage());
        }
        return events;
    }

    /**
     * Salveaza evenimentele in fisier
     * @param events List<RaceEvent>, lista de evenimente de salvat
     */
    public void saveEvents(List<RaceEvent> events) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(eventsFile))) {
            pw.println("ID,PARTICIPANTS,LANES");
            int id = 1;
            for (RaceEvent event : events) {
                String participants = String.join("|",
                        event.getDucks().stream()
                                .map(Duck::getUsername)
                                .toList());

                String lanes = String.join("|",
                        event.getLanes().stream()
                                .map(l -> String.valueOf(l.getId()))
                                .toList());

                pw.printf("%d,%s,%s%n", id++, participants, lanes);
            }
        } catch (Exception ex) {
            System.out.println("Error saving events: " + ex.getMessage());
        }
    }
}
