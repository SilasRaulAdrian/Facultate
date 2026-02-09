package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.config.ConnectionManager;
import org.example.model.Friendship;
import org.example.model.User;
import org.example.model.paging.Page;
import org.example.model.paging.Pageable;

import java.lang.ScopedValue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Clasa pentru repository-ul de prietenii
 */
@RequiredArgsConstructor
public class FriendshipRepository implements Repository<Long, Friendship>, PagingRepository<Friendship> {
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
     * Metoda pentru gasirea unei prietenii dupa id
     * @param id Long, id-ul prieteniei
     * @return Optional<Friendship>, prietenia gasita sau empty daca nu exista
     */
    @Override
    public Optional<Friendship> findById(Long id) {
        String sql = "SELECT id, user1, user2 FROM friendships WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            var rs = ps.executeQuery();

            if (rs.next()) {
                long frId = rs.getLong("id");
                long u1 = rs.getLong("user1");
                long u2 = rs.getLong("user2");

                User user1 = userRepo.findById(u1).orElseThrow(() -> new IllegalArgumentException("User not found: " + u1));
                User user2 = userRepo.findById(u2).orElseThrow(() -> new IllegalArgumentException("User not found: " + u2));

                if (user1 != null && user2 != null) {
                    return Optional.of(new Friendship(frId, user1, user2));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error in findById: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Metoda pentru obtinerea tuturor prieteniilor
     * @return Iterable<Friendship>, toate prieteniile din repository
     */
    @Override
    public Iterable<Friendship> getAll() {
        List<Friendship> list = new ArrayList<>();

        String sql = "SELECT id, user1, user2 FROM friendships";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                long frId = rs.getLong("id");
                long u1 = rs.getLong("user1");
                long u2 = rs.getLong("user2");

                User user1 = userRepo.findById(u1).orElseThrow(() -> new IllegalArgumentException("User not found: " + u1));
                User user2 = userRepo.findById(u2).orElseThrow(() -> new IllegalArgumentException("User not found: " + u2));

                if (user1 != null && user2 != null) {
                    list.add(new Friendship(frId, user1, user2));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error in findAll: " + e.getMessage());
        }

        return list;
    }

    /**
     * Metoda pentru salvarea unei prietenii
     * @param friendship Friendship, prietenia care va fi salvata
     * @return Optional<Friendship>, prietenia salvata sau empty daca nu a fost salvata
     */
    @Override
    public Optional<Friendship> save(Friendship friendship) {
        String sql = "INSERT INTO friendships(user1, user2) VALUES(?, ?) RETURNING id";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, friendship.getU1().getId());
            ps.setLong(2, friendship.getU2().getId());

            var rs = ps.executeQuery();

            if (rs.next()) {
                long id = rs.getLong("id");
                friendship.setId(id);
                return Optional.of(friendship);
            }

            return Optional.empty();

        } catch (SQLException e) {
            System.out.println("Error saving friendship: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Metoda pentru actualizarea unei prietenii
     * @param f Friendship, prietenia care va fi actualizata
     * @return Optional<Friendship>, prietenia actualizata sau empty daca nu a fost actualizata
     */
    @Override
    public Optional<Friendship> update(Friendship f) {
        String sql = "UPDATE friendships SET user1 = ?, user2 = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, f.getU1().getId());
            ps.setLong(2, f.getU2().getId());
            ps.setLong(3, f.getId());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Optional.of(f);
            }

        } catch (SQLException e) {
            System.out.println("Error updating friendship: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Metoda pentru stergerea unei prietenii dupa id
     * @param id Long, id-ul prieteniei care va fi stearsa
     * @return Optional<Friendship>, prietenia stearsa sau empty daca nu a fost stearsa
     */
    @Override
    public Optional<Friendship> delete(Long id) {
        Optional<Friendship> existing = findById(id);

        if (existing.isEmpty()) {
            return Optional.empty();
        }

        String sql = "DELETE FROM friendships WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                return existing;
            }

        } catch (SQLException e) {
            System.out.println("Error deleting friendship: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Page<Friendship> findAllOnPage(Pageable pageable) {
        List<Friendship> list = new ArrayList<>();
        int totalElements = 0;

        String sqlCount = "SELECT COUNT(*) AS total FROM friendships";
        String sqlPage = "SELECT id, user1, user2 FROM friendships ORDER BY id LIMIT ? OFFSET ?";

        try (Connection conn = getConnection()) {

            var rsCount = conn.prepareStatement(sqlCount).executeQuery();
            if(rsCount.next()) totalElements = rsCount.getInt("total");

            try (PreparedStatement ps = conn.prepareStatement(sqlPage)) {
                ps.setInt(1, pageable.getPageSize());
                ps.setInt(2, pageable.getPageNumber() * pageable.getPageSize());
                var rs = ps.executeQuery();

                while (rs.next()) {
                    long frId = rs.getLong("id");
                    long u1Id = rs.getLong("user1");
                    long u2Id = rs.getLong("user2");

                    User user1 = userRepo.findById(u1Id)
                            .orElseThrow(() -> new IllegalArgumentException("User not found: " + u1Id));
                    User user2 = userRepo.findById(u2Id)
                            .orElseThrow(() -> new IllegalArgumentException("User not found: " + u2Id));

                    list.add(new Friendship(frId, user1, user2));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error in findAllOnPage: " + e.getMessage());
        }

        return new Page<>(list, totalElements);
    }

    /**
     * Metoda pentru gasirea unei prietenii intre doi utilizatori
     * @param idUser1 Long, id-ul primului utilizator
     * @param idUser2 Long, id-ul celui de-al doilea utilizator
     * @return Optional<Friendship>, prietenia gasita sau empty daca nu exista
     */
    public Optional<Friendship> findFriendshipBetween(Long idUser1, Long idUser2) {
        String sql = "SELECT id, user1, user2 FROM friendships WHERE (user1 = ? AND user2 = ?) OR (user1 = ? AND user2 = ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idUser1);
            ps.setLong(2, idUser2);
            ps.setLong(3, idUser2);
            ps.setLong(4, idUser1);

            var rs = ps.executeQuery();

            if (rs.next()) {
                long frId = rs.getLong("id");
                long u1 = rs.getLong("user1");
                long u2 = rs.getLong("user2");

                User user1 = userRepo.findById(u1).orElseThrow(() -> new IllegalArgumentException("User not found: " + u1));
                User user2 = userRepo.findById(u2).orElseThrow(() -> new IllegalArgumentException("User not found: " + u2));

                return Optional.of(new Friendship(frId, user1, user2));
            }

        } catch (SQLException e) {
            System.out.println("Error in findFriendshipBetween: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Metoda pentru gasirea prietenilor unui utilizator pe pagini
     * @param userId Long, id-ul utilizatorului
     * @param pageable Pageable, informatii despre pagina
     * @return Page<User>, pagina cu prietenii utilizatorului
     */
    public Page<User> findFriendsPage(Long userId, Pageable pageable) {
        List<User> list = new ArrayList<>();
        int totalElements = 0;

        String countSql = """
        SELECT COUNT(*)
        FROM friendships
        WHERE user1 = ? OR user2 = ?
    """;

        String selectSql = """
        SELECT CASE
            WHEN user1 = ? THEN user2
            ELSE user1
        END AS friend_id
        FROM friendships
        WHERE user1 = ? OR user2 = ?
        ORDER BY friend_id
        LIMIT ? OFFSET ?
    """;

        try (Connection conn = getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(countSql)) {
                ps.setLong(1, userId);
                ps.setLong(2, userId);
                var rs = ps.executeQuery();
                if (rs.next()) {
                    totalElements = rs.getInt(1);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setLong(1, userId);
                ps.setLong(2, userId);
                ps.setLong(3, userId);
                ps.setInt(4, pageable.getPageSize());
                ps.setInt(5, pageable.getPageNumber() * pageable.getPageSize());

                var rs = ps.executeQuery();
                while (rs.next()) {
                    long friendId = rs.getLong("friend_id");
                    userRepo.findById(friendId).ifPresent(list::add);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error findFriendsPage: " + e.getMessage());
        }

        return new Page<>(list, totalElements);
    }
}
