package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.config.ConnectionManager;
import org.example.event.RaceEvent;
import org.example.model.Lane;
import org.example.model.SwimmingDuck;
import org.example.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Repository pentru gestionarea evenimentelor de tip RaceEvent
 */
@RequiredArgsConstructor
public class EventRepository implements Repository<Long, RaceEvent> {
    private final UserRepository userRepo;

    /**
     * Metoda pentru obtinerea unei conexiuni la baza de date
     * @return Connection, conexiunea la baza de date
     * @throws SQLException in caz de eroare la conectare
     */
    private Connection getConnection() throws SQLException {
        return ConnectionManager.getConnection();
    }

    /**
     * Metoda pentru gasirea unui eveniment dupa ID
     * @param id Long, ID-ul evenimentului
     * @return Optional<RaceEvent>, evenimentul gasit sau gol daca nu exista
     */
    @Override
    public Optional<RaceEvent> findById(Long id) {
        String sql = "SELECT id FROM events WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    List<SwimmingDuck> ducks = loadEventParticipants(id, conn);
                    List<Lane> lanes = loadEventLanes(id, conn);
                    RaceEvent event = new RaceEvent(id, ducks, lanes);
                    loadEventSubscribers(id, conn).forEach(event::subscribe);
                    return Optional.of(event);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Metoda pentru obtinerea tuturor evenimentelor
     * @return Iterable<RaceEvent>, lista tuturor evenimentelor
     */
    @Override
    public Iterable<RaceEvent> getAll() {
        Map<Long, RaceEvent> eventsMap = new LinkedHashMap<>();

        try (Connection conn = getConnection()) {
            String sqlEvents = "SELECT id FROM events ORDER BY id";
            try (PreparedStatement ps = conn.prepareStatement(sqlEvents);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    eventsMap.put(id, new RaceEvent(id, new ArrayList<>(), new ArrayList<>()));
                }
            }

            if (eventsMap.isEmpty()) return new ArrayList<>();

            String sqlParticipants = "SELECT ep.event_id, ep.duck_id FROM event_participants ep";
            try (PreparedStatement ps = conn.prepareStatement(sqlParticipants);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Long eventId = rs.getLong("event_id");
                    Long duckId = rs.getLong("duck_id");
                    userRepo.findById(duckId).ifPresent(user -> {
                        if (user instanceof SwimmingDuck duck) {
                            eventsMap.get(eventId).getDucks().add(duck);
                        }
                    });
                }
            }

            String sqlLanes = "SELECT event_id, lane_id, distance FROM event_lanes";
            try (PreparedStatement ps = conn.prepareStatement(sqlLanes);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Long eventId = rs.getLong("event_id");
                    int laneId = rs.getInt("lane_id");
                    double dist = rs.getDouble("distance");
                    eventsMap.get(eventId).getLanes().add(new Lane(laneId, dist));
                }
            }

            String sqlSubs = "SELECT event_id, user_id FROM event_subscribers";
            try (PreparedStatement ps = conn.prepareStatement(sqlSubs);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Long eventId = rs.getLong("event_id");
                    Long userId = rs.getLong("user_id");
                    userRepo.findById(userId).ifPresent(user -> {
                        eventsMap.get(eventId).subscribe(user);
                    });
                }
            }

        } catch (SQLException e) {
            System.out.println("Error optimized getAll: " + e.getMessage());
        }

        return eventsMap.values();
    }

    /**
     * Metoda pentru salvarea unui eveniment
     * @param e RaceEvent, evenimentul de salvat
     * @return Optional<RaceEvent>, evenimentul salvat sau gol in caz de eroare
     */
    @Override
    public Optional<RaceEvent> save(RaceEvent e) {
        String insertEvent = """
            INSERT INTO events DEFAULT VALUES
            RETURNING id
        """;

        try (Connection conn = getConnection()) {

            conn.setAutoCommit(false);

            PreparedStatement psEvent = conn.prepareStatement(insertEvent);
            var rs = psEvent.executeQuery();

            if (!rs.next()) {
                conn.rollback();
                return Optional.empty();
            }

            long newId = rs.getLong("id");

            String sqlParticipants = """
                INSERT INTO event_participants (event_id, duck_id)
                VALUES (?, ?)
            """;

            PreparedStatement psParticipants = conn.prepareStatement(sqlParticipants);
            for (SwimmingDuck duck : e.getDucks()) {
                psParticipants.setLong(1, newId);
                psParticipants.setLong(2, duck.getId());
                psParticipants.addBatch();
            }
            psParticipants.executeBatch();

            String sqlLanes = """
                INSERT INTO event_lanes (event_id, lane_id, distance)
                VALUES (?, ?, ?)
            """;

            PreparedStatement psLanes = conn.prepareStatement(sqlLanes);
            for (Lane lane : e.getLanes()) {
                psLanes.setLong(1, newId);
                psLanes.setInt(2, lane.getId());
                psLanes.setDouble(3, lane.getDistance());
                psLanes.addBatch();
            }
            psLanes.executeBatch();

            String sqlSubscribers = """
            INSERT INTO event_subscribers (event_id, user_id)
            VALUES (?, ?)
            """;

            PreparedStatement psSub = conn.prepareStatement(sqlSubscribers);
            for (User u : e.getSubscribers()) {
                psSub.setLong(1, newId);
                psSub.setLong(2, u.getId());
                psSub.addBatch();
            }
            psSub.executeBatch();

            conn.commit();
            conn.setAutoCommit(true);

            return Optional.of(new RaceEvent(newId, e.getDucks(), e.getLanes()));

        } catch (SQLException ex) {
            System.out.println("Error save: " + ex.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Metoda pentru actualizarea unui eveniment
     * @param e RaceEvent, evenimentul de actualizat
     * @return Optional<RaceEvent>, evenimentul actualizat sau gol in caz de eroare
     */
    @Override
    public Optional<RaceEvent> update(RaceEvent e) {
        if (e.getId() == null || findById(e.getId()).isEmpty()) {
            return Optional.empty();
        }

        try (Connection conn = getConnection()) {

            conn.setAutoCommit(false);

            String deleteParticipants = "DELETE FROM event_participants WHERE event_id = ?";
            PreparedStatement psDelParticipants = conn.prepareStatement(deleteParticipants);
            psDelParticipants.setLong(1, e.getId());
            psDelParticipants.executeUpdate();

            String deleteLanes = "DELETE FROM event_lanes WHERE event_id = ?";
            PreparedStatement psDelLanes = conn.prepareStatement(deleteLanes);
            psDelLanes.setLong(1, e.getId());
            psDelLanes.executeUpdate();

            String deleteSubscribers = "DELETE FROM event_subscribers WHERE event_id = ?";
            PreparedStatement psDelSubscribers = conn.prepareStatement(deleteSubscribers);
            psDelSubscribers.setLong(1, e.getId());
            psDelSubscribers.executeUpdate();

            String sqlParticipants = """
                INSERT INTO event_participants (event_id, duck_id)
                VALUES (?, ?)
            """;
            PreparedStatement psParticipants = conn.prepareStatement(sqlParticipants);
            for (SwimmingDuck duck : e.getDucks()) {
                psParticipants.setLong(1, e.getId());
                psParticipants.setLong(2, duck.getId());
                psParticipants.addBatch();
            }
            psParticipants.executeBatch();

            String sqlLanes = """
                INSERT INTO event_lanes (event_id, lane_id, distance)
                VALUES (?, ?, ?)
            """;
            PreparedStatement psLanes = conn.prepareStatement(sqlLanes);
            for (Lane lane : e.getLanes()) {
                psLanes.setLong(1, e.getId());
                psLanes.setInt(2, lane.getId());
                psLanes.setDouble(3, lane.getDistance());
                psLanes.addBatch();
            }
            psLanes.executeBatch();

            String sqlSubscribers = """
            INSERT INTO event_subscribers (event_id, user_id)
            VALUES (?, ?)
        """;
            PreparedStatement psSubscribers = conn.prepareStatement(sqlSubscribers);
            for (User user : e.getSubscribers()) {
                psSubscribers.setLong(1, e.getId());
                psSubscribers.setLong(2, user.getId());
                psSubscribers.addBatch();
            }
            psSubscribers.executeBatch();

            conn.commit();
            conn.setAutoCommit(true);
            return Optional.of(e);

        } catch (SQLException ex) {
            System.out.println("Error update: " + ex.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Metoda pentru stergerea unui eveniment dupa ID
     * @param id Long, ID-ul evenimentului de sters
     * @return Optional<RaceEvent>, evenimentul sters sau gol daca nu exista
     */
    @Override
    public Optional<RaceEvent> delete(Long id) {
        Optional<RaceEvent> toDelete = findById(id);
        if (toDelete.isEmpty()) return Optional.empty();

        String sql = "DELETE FROM events WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error delete: " + e.getMessage());
        }

        return toDelete;
    }

    /**
     * Metoda pentru incarcarea participantilor unui eveniment
     * @param eventId Long, ID-ul evenimentului
     * @param conn Connection, conexiunea la baza de date
     * @return List<SwimmingDuck>, lista de participanti
     * @throws SQLException in caz de eroare
     */
    private List<SwimmingDuck> loadEventParticipants(Long eventId, Connection conn) throws SQLException {
        List<SwimmingDuck> ducks = new ArrayList<>();
        String sql = "SELECT duck_id FROM event_participants WHERE event_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    userRepo.findById(rs.getLong("duck_id")).ifPresent(u -> {
                        if (u instanceof SwimmingDuck d) ducks.add(d);
                    });
                }
            }
        }
        return ducks;
    }

    /**
     * Metoda pentru incarcarea pistelor unui eveniment
     * @param eventId Long, ID-ul evenimentului
     * @param conn Connection, conexiunea la baza de date
     * @return List<Lane>, lista de piste
     * @throws SQLException in caz de eroare
     */
    private List<Lane> loadEventLanes(Long eventId, Connection conn) throws SQLException {
        List<Lane> lanes = new ArrayList<>();
        String sql = "SELECT lane_id, distance FROM event_lanes WHERE event_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lanes.add(new Lane(rs.getInt("lane_id"), rs.getDouble("distance")));
                }
            }
        }
        return lanes;
    }

    /**
     * Metoda pentru incarcarea abonatilor unui eveniment
     * @param eventId Long, ID-ul evenimentului
     * @param conn Connection, conexiunea la baza de date
     * @return List<User>, lista de abonati
     * @throws SQLException in caz de eroare
     */
    private List<User> loadEventSubscribers(Long eventId, Connection conn) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id FROM event_subscribers WHERE event_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    userRepo.findById(rs.getLong("user_id")).ifPresent(users::add);
                }
            }
        }
        return users;
    }

}
