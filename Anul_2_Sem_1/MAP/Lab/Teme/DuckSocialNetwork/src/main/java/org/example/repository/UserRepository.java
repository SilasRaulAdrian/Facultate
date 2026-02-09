package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.config.ConnectionManager;
import org.example.model.*;
import org.example.model.paging.Page;
import org.example.model.paging.Pageable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Clasa pentru gestionarea depozitului de utilizatori
 */
@RequiredArgsConstructor
public class UserRepository implements Repository<Long, User>, PagingRepository<User> {
    private final Map<Long, User> cache = new HashMap<>();

    /**
     * Metoda pentru obtinerea unei conexiuni la baza de date
     * @return Connection, conexiunea la baza de date
     * @throws SQLException in caz de eroare la conectare
     */
    private Connection getConnection() throws SQLException {
        return ConnectionManager.getConnection();
    }

    /**
     * Metoda pentru gasirea unui utilizator dupa ID
     * @param id Long, ID-ul utilizatorului
     * @return Optional<User>, utilizatorul gasit sau gol daca nu exista
     */
    @Override
    public Optional<User> findById(Long id) {
        if (cache.containsKey(id)) {
            return Optional.of(cache.get(id));
        }

        String sql = """
            SELECT u.id, u.username, u.email, u.password, u.profile_image_path,
                   p.nume, p.prenume, p.data_nasterii, p.ocupatie, p.nivel_empatie,
                   d.tip, d.viteza, d.rezistenta, d.card_id
            FROM users u
            LEFT JOIN person p ON p.id = u.id
            LEFT JOIN duck d ON d.id = u.id
            WHERE u.id = ?
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            var rs = ps.executeQuery();

            if (rs.next()) {
                User user = mapResultSetToUser(rs);
                if (user != null) {
                    cache.put(id, user);
                    return Optional.of(user);
                }
            }
        } catch (Exception e) {
            System.out.println("Error finding user by ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Metoda pentru obtinerea tuturor utilizatorilor
     * @return Iterable<User>, lista tuturor utilizatorilor
     */
    @Override
    public Iterable<User> getAll() {
        List<User> list = new ArrayList<>();
        String sql = """
            SELECT u.id, u.username, u.email, u.password, u.profile_image_path,
                   p.nume, p.prenume, p.data_nasterii, p.ocupatie, p.nivel_empatie,
                   d.tip, d.viteza, d.rezistenta, d.card_id
            FROM users u
            LEFT JOIN person p ON p.id = u.id
            LEFT JOIN duck d ON d.id = u.id
            ORDER BY u.id
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                if (cache.containsKey(id)) {
                    list.add(cache.get(id));
                    continue;
                }

                User user = mapResultSetToUser(rs);
                if (user != null) {
                    list.add(user);
                    cache.put(id, user);
                }
            }
        } catch (Exception e) {
            System.out.println("Error getAll: " + e.getMessage());
        }
        return list;
    }

    /**
     * Metoda pentru salvarea unui utilizator
     * @param u User, utilizatorul de salvat
     * @return Optional<User>, utilizatorul salvat sau gol in caz de eroare
     */
    @Override
    public Optional<User> save(User u) {
        String insertUser = """
            INSERT INTO users (username, email, password, profile_image_path)
            VALUES (?, ?, ?, ?) RETURNING id
        """;

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement psUser = conn.prepareStatement(insertUser);
            psUser.setString(1, u.getUsername());
            psUser.setString(2, u.getEmail());
            psUser.setString(3, u.getPassword());
            psUser.setString(4, u.getProfileImagePath());

            var rs = psUser.executeQuery();
            if (!rs.next()) {
                conn.rollback();
                return Optional.empty();
            }

            long newId = rs.getLong("id");
            u.setId(newId);

            if (u instanceof Person p) {
                String sql = "INSERT INTO person (id, nume, prenume, data_nasterii, ocupatie, nivel_empatie) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setLong(1, newId);
                ps.setString(2, p.getNume());
                ps.setString(3, p.getPrenume());
                ps.setDate(4, java.sql.Date.valueOf(p.getDataNasterii()));
                ps.setString(5, p.getOcupatie());
                ps.setDouble(6, p.getNivelEmpatie());
                ps.executeUpdate();
            } else if (u instanceof Duck d) {
                String sql = "INSERT INTO duck (id, tip, viteza, rezistenta, card_id) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setLong(1, newId);
                ps.setString(2, d.getTip().toString());
                ps.setDouble(3, d.getViteza());
                ps.setDouble(4, d.getRezistenta());
                if (d.getCardId() == null || d.getCardId() == 0) ps.setNull(5, java.sql.Types.BIGINT);
                else ps.setLong(5, d.getCardId());
                ps.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
            cache.put(newId, u);
            return Optional.of(u);
        } catch (SQLException e) {
            System.out.println("Error save: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Metoda pentru actualizarea unui utilizator
     * @param u User, utilizatorul de actualizat
     * @return Optional<User>, utilizatorul actualizat sau gol in caz de eroare
     */
    @Override
    public Optional<User> update(User u) {
        String updateUserSql = "UPDATE users SET username=?, email=?, password=?, profile_image_path=? WHERE id=?";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement psUser = conn.prepareStatement(updateUserSql);
            psUser.setString(1, u.getUsername());
            psUser.setString(2, u.getEmail());
            psUser.setString(3, u.getPassword());
            psUser.setString(4, u.getProfileImagePath());
            psUser.setLong(5, u.getId());
            psUser.executeUpdate();

            if (u instanceof Person p) {
                String sql = "UPDATE person SET nume=?, prenume=?, data_nasterii=?, ocupatie=?, nivel_empatie=? WHERE id=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, p.getNume());
                ps.setString(2, p.getPrenume());
                ps.setDate(3, java.sql.Date.valueOf(p.getDataNasterii()));
                ps.setString(4, p.getOcupatie());
                ps.setDouble(5, p.getNivelEmpatie());
                ps.setLong(6, p.getId());
                ps.executeUpdate();
            } else if (u instanceof Duck d) {
                String sql = "UPDATE duck SET tip=?, viteza=?, rezistenta=?, card_id=? WHERE id=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, d.getTip().toString());
                ps.setDouble(2, d.getViteza());
                ps.setDouble(3, d.getRezistenta());
                if (d.getCardId() == null || d.getCardId() == 0) ps.setNull(4, java.sql.Types.BIGINT);
                else ps.setLong(4, d.getCardId());
                ps.setLong(5, d.getId());
                ps.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
            cache.put(u.getId(), u);
            return Optional.of(u);
        } catch (SQLException e) {
            System.out.println("Error update: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Metoda pentru stergerea unui utilizator dupa ID
     * @param id Long, ID-ul utilizatorului de sters
     * @return Optional<User>, utilizatorul sters sau gol daca nu exista
     */
    @Override
    public Optional<User> delete(Long id) {
        Optional<User> toDelete = findById(id);
        if (toDelete.isEmpty()) return Optional.empty();

        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
            cache.remove(id);
        } catch (SQLException e) {
            System.out.println("Error delete: " + e.getMessage());
        }
        return toDelete;
    }

    /**
     * Metoda pentru obtinerea tuturor ratelor din baza de date
     * @return List<Duck>, lista de rate
     */
    public List<Duck> getDucksFromDB() {
        List<Duck> list = new ArrayList<>();
        String sql = """
            SELECT d.id, u.username, u.email, u.password, u.profile_image_path,
                   d.tip, d.viteza, d.rezistenta, d.card_id
            FROM duck d
            JOIN users u ON u.id = d.id
            ORDER BY d.id
        """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                TipRata tip = TipRata.valueOf(rs.getString("tip"));
                Duck duck = createDuckFromResultSet(rs, tip);
                duck.setProfileImagePath(rs.getString("profile_image_path"));
                list.add(duck);
            }
        } catch (Exception e) {
            System.out.println("Error getDucksFromDB: " + e.getMessage());
        }
        return list;
    }

    /**
     * Metoda pentru obtinerea tuturor persoanelor din baza de date
     * @return List<Person>, lista de persoane
     */
    public List<Person> getPersonsFromDB() {
        List<Person> list = new ArrayList<>();
        String sql = """
            SELECT p.id, u.username, u.email, u.password, u.profile_image_path,
                   p.nume, p.prenume, p.data_nasterii, p.ocupatie, p.nivel_empatie
            FROM person p
            JOIN users u ON u.id = p.id
            ORDER BY p.id
        """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                Person p = new Person(
                        rs.getLong("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"),
                        rs.getString("nume"), rs.getString("prenume"),
                        rs.getDate("data_nasterii").toLocalDate(), rs.getString("ocupatie"), rs.getDouble("nivel_empatie")
                );
                p.setProfileImagePath(rs.getString("profile_image_path"));
                list.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error getPersonsFromDB: " + e.getMessage());
        }
        return list;
    }

    /**
     * Metoda pentru obtinerea unei rate dupa tip din baza de date
     * @param tipRata TipRata, tipul ratei cautate
     * @return List<Duck>, lista de rate gasite
     */
    public List<Duck> getDucksByTypeFromDB(TipRata tipRata) {
        List<Duck> list = new ArrayList<>();
        String sql = """
            SELECT d.id, u.username, u.email, u.password, u.profile_image_path,
                   d.tip, d.viteza, d.rezistenta, d.card_id
            FROM duck d
            JOIN users u ON u.id = d.id
            WHERE d.tip = ?
            ORDER BY d.id
        """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipRata.toString());
            var rs = ps.executeQuery();
            while (rs.next()) {
                Duck duck = createDuckFromResultSet(rs, tipRata);
                duck.setProfileImagePath(rs.getString("profile_image_path"));
                list.add(duck);
            }
        } catch (Exception e) {
            System.out.println("Error getDucksByTypeFromDB: " + e.getMessage());
        }
        return list;
    }

    /**
     * Metoda pentru crearea unei rate dintr-un ResultSet
     * @param rs ResultSet, rezultatul interogarii SQL
     * @param tip TipRata, tipul ratei
     * @return Duck, rata creata
     * @throws Exception in caz de eroare la citirea datelor
     */
    private Duck createDuckFromResultSet(ResultSet rs, TipRata tip) throws Exception {
        Duck d = switch (tip) {
            case SWIMMING -> new SwimmingDuck(rs.getLong("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"), rs.getDouble("viteza"), rs.getDouble("rezistenta"));
            case FLYING_AND_SWIMMING -> new FlyingAndSwimmingDuck(rs.getLong("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"), rs.getDouble("viteza"), rs.getDouble("rezistenta"));
            default -> new FlyingDuck(rs.getLong("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"), rs.getDouble("viteza"), rs.getDouble("rezistenta"));
        };
        d.setCardId(rs.getLong("card_id"));
        return d;
    }

    /**
     * Metoda helper pentru a mapa un ResultSet la un obiect User generic
     */
    private User mapResultSetToUser(ResultSet rs) throws Exception {
        User user = null;
        if (rs.getString("nume") != null) {
            user = new Person(
                    rs.getLong("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"),
                    rs.getString("nume"), rs.getString("prenume"),
                    rs.getDate("data_nasterii").toLocalDate(), rs.getString("ocupatie"), rs.getDouble("nivel_empatie")
            );
        } else if (rs.getString("tip") != null) {
            user = createDuckFromResultSet(rs, TipRata.valueOf(rs.getString("tip")));
        }
        if (user != null) {
            user.setProfileImagePath(rs.getString("profile_image_path"));
        }
        return user;
    }

    /**
     * Metoda pentru obtinerea unei pagini de utilizatori
     * @param pageable Pageable, obiectul de paginare
     * @return Page<User>, pagina de utilizatori
     */
    public Page<User> findAllOnPage(Pageable pageable) {
        List<User> list = new ArrayList<>();
        int totalElements = 0;
        try (Connection conn = getConnection()) {
            var rsCount = conn.prepareStatement("SELECT COUNT(*) AS total FROM users").executeQuery();
            if(rsCount.next()) totalElements = rsCount.getInt("total");

            String sql = """
                SELECT u.id, u.username, u.email, u.password, u.profile_image_path,
                       p.nume, p.prenume, p.data_nasterii, p.ocupatie, p.nivel_empatie,
                       d.tip, d.viteza, d.rezistenta, d.card_id
                FROM users u
                LEFT JOIN person p ON p.id = u.id
                LEFT JOIN duck d ON d.id = u.id
                ORDER BY u.id LIMIT ? OFFSET ?
            """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pageable.getPageSize());
            ps.setInt(2, pageable.getPageNumber() * pageable.getPageSize());
            var rs = ps.executeQuery();
            while (rs.next()) {
                User u = mapResultSetToUser(rs);
                if (u != null) {
                    list.add(u);
                    cache.put(u.getId(), u);
                }
            }
        } catch (Exception e) {
            System.out.println("Error getPage: " + e.getMessage());
        }
        return new Page<>(list, totalElements);
    }

    /**
     * Metoda pentru obtinerea unei pagini de rate, optional filtrate dupa tip
     * @param pageable Pageable, obiectul de paginare
     * @param tipRata TipRata, tipul ratei pentru filtrare (poate fi null)
     * @return Page<Duck>, pagina de rate
     */
    public Page<Duck> findAllDucksOnPage(Pageable pageable, TipRata tipRata) {
        List<Duck> list = new ArrayList<>();
        String baseSql = "FROM duck d JOIN users u ON u.id = d.id WHERE 1 = 1 " + (tipRata != null ? "AND d.tip = ? " : "");
        int totalElements = 0;
        try (Connection conn = getConnection()) {
            PreparedStatement psCount = conn.prepareStatement("SELECT COUNT(*) " + baseSql);
            if (tipRata != null) psCount.setString(1, tipRata.toString());
            var rsCount = psCount.executeQuery();
            if (rsCount.next()) totalElements = rsCount.getInt(1);

            PreparedStatement ps = conn.prepareStatement("SELECT d.*, u.username, u.email, u.password, u.profile_image_path " + baseSql + " ORDER BY d.id LIMIT ? OFFSET ?");
            int idx = 1;
            if (tipRata != null) ps.setString(idx++, tipRata.toString());
            ps.setInt(idx++, pageable.getPageSize());
            ps.setInt(idx, pageable.getPageNumber() * pageable.getPageSize());
            var rs = ps.executeQuery();
            while (rs.next()) {
                Duck d = createDuckFromResultSet(rs, TipRata.valueOf(rs.getString("tip")));
                d.setProfileImagePath(rs.getString("profile_image_path"));
                list.add(d);
            }
        } catch (Exception e) {
            System.out.println("Error findAllDucksOnPage: " + e.getMessage());
        }
        return new Page<>(list, totalElements);
    }

    /**
     * Metoda pentru obtinerea unei pagini de persoane
     * @param pageable Pageable, obiectul de paginare
     * @return Page<Person>, pagina de persoane
     */
    public Page<Person> findAllPersonsOnPage(Pageable pageable) {
        List<Person> list = new ArrayList<>();
        int totalElements = 0;
        try (Connection conn = getConnection()) {
            var rsCount = conn.prepareStatement("SELECT COUNT(*) FROM person").executeQuery();
            if (rsCount.next()) totalElements = rsCount.getInt(1);

            String sql = "SELECT p.*, u.username, u.email, u.password, u.profile_image_path FROM person p JOIN users u ON u.id = p.id ORDER BY p.id LIMIT ? OFFSET ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pageable.getPageSize());
            ps.setInt(2, pageable.getPageNumber() * pageable.getPageSize());
            var rs = ps.executeQuery();
            while (rs.next()) {
                Person p = new Person(rs.getLong("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"), rs.getString("nume"), rs.getString("prenume"), rs.getDate("data_nasterii").toLocalDate(), rs.getString("ocupatie"), rs.getDouble("nivel_empatie"));
                p.setProfileImagePath(rs.getString("profile_image_path"));
                list.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error findAllPersonsOnPage: " + e.getMessage());
        }
        return new Page<>(list, totalElements);
    }

    /**
     * Metoda pentru gasirea unui utilizator dupa username
     * @param username String, username-ul utilizatorului
     * @return Optional<User>, utilizatorul gasit sau gol daca nu exista
     */
    public Optional<User> findByUsername(String username) {
        String sql = """
            SELECT u.*, p.nume, p.prenume, p.data_nasterii, p.ocupatie, p.nivel_empatie, d.tip, d.viteza, d.rezistenta, d.card_id
            FROM users u LEFT JOIN person p ON p.id = u.id LEFT JOIN duck d ON d.id = u.id WHERE u.username = ?
        """;
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            var rs = ps.executeQuery();
            if (rs.next()) return Optional.ofNullable(mapResultSetToUser(rs));
        } catch (Exception e) {
            System.out.println("Error findByUsername: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Metoda pentru obtinerea unei pagini de utilizatori care nu sunt prieteni cu un anumit utilizator
     * @param userId Long, ID-ul utilizatorului
     * @param pageable Pageable, obiectul de paginare
     * @return Page<User>, pagina de utilizatori care nu sunt prieteni
     */
    public Page<User> findNotFriendsPage(Long userId, Pageable pageable) {
        List<User> list = new ArrayList<>();
        int totalElements = 0;
        String filter = "WHERE u.id <> ? AND LOWER(u.username) <> 'admin' AND u.id NOT IN (SELECT CASE WHEN user1 = ? THEN user2 ELSE user1 END FROM friendships WHERE user1 = ? OR user2 = ?)";
        try (Connection conn = getConnection()) {
            PreparedStatement psCount = conn.prepareStatement("SELECT COUNT(*) FROM users u " + filter);
            for(int i=1; i<=4; i++) psCount.setLong(i, userId);
            var rsC = psCount.executeQuery();
            if(rsC.next()) totalElements = rsC.getInt(1);

            PreparedStatement ps = conn.prepareStatement("SELECT u.*, p.nume, p.prenume, p.data_nasterii, p.ocupatie, p.nivel_empatie, d.tip, d.viteza, d.rezistenta, d.card_id FROM users u LEFT JOIN person p ON p.id = u.id LEFT JOIN duck d ON d.id = u.id " + filter + " ORDER BY u.username LIMIT ? OFFSET ?");
            for(int i=1; i<=4; i++) ps.setLong(i, userId);
            ps.setInt(5, pageable.getPageSize());
            ps.setInt(6, pageable.getPageNumber() * pageable.getPageSize());
            var rs = ps.executeQuery();
            while (rs.next()) {
                User u = mapResultSetToUser(rs);
                if (u != null) list.add(u);
            }
        } catch (Exception e) {
            System.out.println("Error findNotFriendsPage: " + e.getMessage());
        }
        return new Page<>(list, totalElements);
    }
}