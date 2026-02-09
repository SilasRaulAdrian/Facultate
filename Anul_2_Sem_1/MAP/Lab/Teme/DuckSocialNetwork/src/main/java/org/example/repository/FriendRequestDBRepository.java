package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.config.ConnectionManager;
import org.example.model.FriendRequest;
import org.example.model.FriendRequestStatus;
import org.example.model.Person;
import org.example.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Clasa FriendRequestDBRepository care implementeaza FriendRequestRepository
 * pentru gestionarea cererilor de prietenie in baza de date
 */
@RequiredArgsConstructor
public class FriendRequestDBRepository implements FriendRequestRepository {
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
     * Metoda pentru maparea unui ResultSet la un obiect User
     * @param rs ResultSet, rezultatul interogarii SQL
     * @param prefix String, prefixul pentru coloanele utilizatorului
     * @return User, obiectul User mapat
     * @throws SQLException in caz de eroare la citirea datelor
     */
    private User mapUser(ResultSet rs, String prefix) throws SQLException {
        return new Person(
                rs.getLong(prefix + "_id"),
                rs.getString(prefix + "_username"),
                rs.getString(prefix + "_email"),
                null,
                null,
                null,
                null,
                null,
                0.0
        );
    }

    /**
     * Metoda pentru maparea unui ResultSet la un obiect FriendRequest
     * care include si utilizatorii implicati in cererea de prietenie
     * @param rs ResultSet, rezultatul interogarii SQL
     * @return FriendRequest, obiectul FriendRequest mapat
     * @throws SQLException in caz de eroare la citirea datelor
     */
    private FriendRequest mapWithUsers(ResultSet rs) throws SQLException {
        Long id = rs.getLong("fr_id");
        FriendRequestStatus status =
                FriendRequestStatus.valueOf(rs.getString("fr_status"));

        User from = mapUser(rs, "from");
        User to   = mapUser(rs, "to");

        return new FriendRequest(id, from, to, status);
    }


    /**
     * Metoda pentru gasirea tuturor cererilor de prietenie in asteptare pentru un utilizator
     * @param userId Long, id-ul utilizatorului
     * @return List<FriendRequest>, lista de cereri de prietenie in asteptare
     */
    @Override
    public List<FriendRequest> findPendingForUser(Long userId) {
        List<FriendRequest> list = new ArrayList<>();

        String sql = """
        SELECT
            fr.id AS fr_id,
            fr.status AS fr_status,

            uf.id AS from_id,
            uf.username AS from_username,
            uf.email AS from_email,

            ut.id AS to_id,
            ut.username AS to_username,
            ut.email AS to_email
        FROM friend_requests fr
        JOIN users uf ON fr.from_user = uf.id
        JOIN users ut ON fr.to_user = ut.id
        WHERE fr.to_user = ? AND fr.status = 'PENDING'
    """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapWithUsers(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Metoda pentru a gasi o cerere de prietenie intre doi utilizatori
     * @param fromId Long, id-ul utilizatorului care a trimis cererea
     * @param toId Long, id-ul utilizatorului care a primit cererea
     * @return Optional<FriendRequest>, cererea de prietenie gasita sau empty daca nu exista
     */
    @Override
    public Optional<FriendRequest> findBetween(Long fromId, Long toId) {
        String sql = """
        SELECT
            fr.id AS fr_id,
            fr.status AS fr_status,

            uf.id AS from_id,
            uf.username AS from_username,
            uf.email AS from_email,

            ut.id AS to_id,
            ut.username AS to_username,
            ut.email AS to_email
        FROM friend_requests fr
        JOIN users uf ON fr.from_user = uf.id
        JOIN users ut ON fr.to_user = ut.id
        WHERE fr.from_user = ? AND fr.to_user = ?
    """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, fromId);
            ps.setLong(2, toId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapWithUsers(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * Metoda pentru gasirea unei cereri de prietenie dupa id
     * @param id Long, id-ul cererii de prietenie
     * @return Optional<FriendRequest>, cererea de prietenie gasita sau empty daca nu exista
     */
    @Override
    public Optional<FriendRequest> findById(Long id) {
        String sql = """
        SELECT
            fr.id AS fr_id,
            fr.status AS fr_status,

            uf.id AS from_id,
            uf.username AS from_username,
            uf.email AS from_email,

            ut.id AS to_id,
            ut.username AS to_username,
            ut.email AS to_email
        FROM friend_requests fr
        JOIN users uf ON fr.from_user = uf.id
        JOIN users ut ON fr.to_user = ut.id
        WHERE fr.id = ?
    """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapWithUsers(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * Metoda pentru obtinerea tuturor cererilor de prietenie
     * @return Iterable<FriendRequest>, toate cererile de prietenie din repository
     */
    @Override
    public Iterable<FriendRequest> getAll() {
        List<FriendRequest> list = new ArrayList<>();

        String sql = """
        SELECT
            fr.id AS fr_id,
            fr.status AS fr_status,

            uf.id AS from_id,
            uf.username AS from_username,
            uf.email AS from_email,

            ut.id AS to_id,
            ut.username AS to_username,
            ut.email AS to_email
        FROM friend_requests fr
        JOIN users uf ON fr.from_user = uf.id
        JOIN users ut ON fr.to_user = ut.id
    """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapWithUsers(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Metoda pentru salvarea unei cereri de prietenie
     * @param fr FriendRequest, cererea de prietenie care va fi salvata
     * @return Optional<FriendRequest>, cererea de prietenie salvata sau empty daca nu a fost salvata
     */
    @Override
    public Optional<FriendRequest> save(FriendRequest fr) {
        String sql = """
            INSERT INTO friend_requests(from_user, to_user, status)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, fr.getFrom().getId());
            ps.setLong(2, fr.getTo().getId());
            ps.setString(3, fr.getStatus().name());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                fr.setId(keys.getLong(1));
            }

            return Optional.of(fr);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * Metoda pentru actualizarea unei cereri de prietenie
     * @param fr FriendRequest, cererea de prietenie care va fi actualizata
     * @return Optional<FriendRequest>, cererea de prietenie actualizata sau empty daca nu a fost actualizata
     */
    @Override
    public Optional<FriendRequest> update(FriendRequest fr) {
        String sql = "UPDATE friend_requests SET status = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fr.getStatus().name());
            ps.setLong(2, fr.getId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                return Optional.of(fr);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * Metoda pentru stergerea unei cereri de prietenie dupa id
     * @param id Long, id-ul cererii de prietenie care va fi stearsa
     * @return Optional<FriendRequest>, cererea de prietenie stearsa sau empty daca nu a fost stearsa
     */
    @Override
    public Optional<FriendRequest> delete(Long id) {
        Optional<FriendRequest> fr = findById(id);
        if (fr.isEmpty()) return Optional.empty();

        String sql = "DELETE FROM friend_requests WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

            return fr;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
