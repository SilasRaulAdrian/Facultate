package org.example.repository;
import lombok.RequiredArgsConstructor;
import org.example.config.ConnectionManager;
import org.example.model.*;
import org.example.model.paging.Page;
import org.example.model.paging.Pageable;

import java.io.*;
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
            SELECT u.id, u.username, u.email, u.password,
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

            if (!rs.next()) {
                return Optional.empty();
            }

            User user = null;

            if (rs.getString("nume") != null) {
                user = new Person(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("nume"),
                        rs.getString("prenume"),
                        rs.getDate("data_nasterii").toLocalDate(),
                        rs.getString("ocupatie"),
                        rs.getDouble("nivel_empatie")
                );
            } else if (rs.getString("tip") != null) {
                TipRata tip = TipRata.valueOf(rs.getString("tip"));
                Duck d;

                if (tip == TipRata.SWIMMING) {
                    d = new SwimmingDuck(
                            rs.getLong("id"), rs.getString("username"),
                            rs.getString("email"), rs.getString("password"),
                            rs.getDouble("viteza"), rs.getDouble("rezistenta")
                    );
                } else if (tip == TipRata.FLYING_AND_SWIMMING) {
                    d = new FlyingAndSwimmingDuck(
                            rs.getLong("id"), rs.getString("username"),
                            rs.getString("email"), rs.getString("password"),
                            rs.getDouble("viteza"), rs.getDouble("rezistenta")
                    );
                } else {
                    d = new FlyingDuck(
                            rs.getLong("id"), rs.getString("username"),
                            rs.getString("email"), rs.getString("password"),
                            rs.getDouble("viteza"), rs.getDouble("rezistenta")
                    );
                }

                d.setCardId(rs.getLong("card_id"));
                user = d;
            }

            if (user != null) {
                cache.put(id, user);
            }

            return Optional.ofNullable(user);

        } catch (SQLException e) {
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
        SELECT u.id, u.username, u.email, u.password,
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

                User user = null;

                if (rs.getString("nume") != null) {
                    user = new Person(
                            id,
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("nume"),
                            rs.getString("prenume"),
                            rs.getDate("data_nasterii").toLocalDate(),
                            rs.getString("ocupatie"),
                            rs.getDouble("nivel_empatie")
                    );
                } else if (rs.getString("tip") != null) {
                    TipRata tip = TipRata.valueOf(rs.getString("tip"));
                    Duck d;
                    if (tip == TipRata.SWIMMING) {
                        d = new SwimmingDuck(
                                id, rs.getString("username"),
                                rs.getString("email"), rs.getString("password"),
                                rs.getDouble("viteza"), rs.getDouble("rezistenta")
                        );
                    } else if (tip == TipRata.FLYING_AND_SWIMMING) {
                        d = new FlyingAndSwimmingDuck(
                                id, rs.getString("username"),
                                rs.getString("email"), rs.getString("password"),
                                rs.getDouble("viteza"), rs.getDouble("rezistenta")
                        );
                    } else {
                        d = new FlyingDuck(
                                id, rs.getString("username"),
                                rs.getString("email"), rs.getString("password"),
                                rs.getDouble("viteza"), rs.getDouble("rezistenta")
                        );
                    }
                    d.setCardId(rs.getLong("card_id"));
                    user = d;
                }

                if (user != null) {
                    list.add(user);
                    cache.put(id, user);
                }
            }

        } catch (SQLException e) {
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
            INSERT INTO users (username, email, password)
            VALUES (?, ?, ?) RETURNING id
        """;

        try (Connection conn = getConnection()) {

            conn.setAutoCommit(false);

            PreparedStatement psUser = conn.prepareStatement(insertUser);
            psUser.setString(1, u.getUsername());
            psUser.setString(2, u.getEmail());
            psUser.setString(3, u.getPassword());
            var rs = psUser.executeQuery();

            if (!rs.next()) {
                conn.rollback();
                return Optional.empty();
            }

            long newId = rs.getLong("id");
            u.setId(newId);

            if (u instanceof Person p) {
                String sql = """
                    INSERT INTO person (id, nume, prenume, data_nasterii, ocupatie, nivel_empatie)
                    VALUES (?, ?, ?, ?, ?, ?)
                """;

                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setLong(1, newId);
                ps.setString(2, p.getNume());
                ps.setString(3, p.getPrenume());
                ps.setDate(4, java.sql.Date.valueOf(p.getDataNasterii()));
                ps.setString(5, p.getOcupatie());
                ps.setDouble(6, p.getNivelEmpatie());
                ps.executeUpdate();
            }

            if (u instanceof Duck d) {
                String sql = """
                    INSERT INTO duck (id, tip, viteza, rezistenta, card_id)
                    VALUES (?, ?, ?, ?, ?)
                """;

                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setLong(1, newId);
                ps.setString(2, d.getTip().toString());
                ps.setDouble(3, d.getViteza());
                ps.setDouble(4, d.getRezistenta());
                if (d.getCardId() == null || d.getCardId() == 0) {
                    ps.setNull(5, java.sql.Types.BIGINT);
                } else {
                    ps.setLong(5, d.getCardId());
                }
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
        String updateUserSql = """
            UPDATE users SET username=?, email=?, password=? WHERE id=?
        """;

        try (Connection conn = getConnection()) {

            conn.setAutoCommit(false);

            PreparedStatement psUser = conn.prepareStatement(updateUserSql);
            psUser.setString(1, u.getUsername());
            psUser.setString(2, u.getEmail());
            psUser.setString(3, u.getPassword());
            psUser.setLong(4, u.getId());
            psUser.executeUpdate();

            if (u instanceof Person p) {
                String sql = """
                    UPDATE person SET nume=?, prenume=?, data_nasterii=?, ocupatie=?, nivel_empatie=?
                    WHERE id=?
                """;
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, p.getNume());
                ps.setString(2, p.getPrenume());
                ps.setDate(3, java.sql.Date.valueOf(p.getDataNasterii()));
                ps.setString(4, p.getOcupatie());
                ps.setDouble(5, p.getNivelEmpatie());
                ps.setLong(6, p.getId());
                ps.executeUpdate();
            }

            if (u instanceof Duck d) {
                String sql = """
                    UPDATE duck SET tip=?, viteza=?, rezistenta=?, card_id=? WHERE id=?
                """;
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, d.getTip().toString());
                ps.setDouble(2, d.getViteza());
                ps.setDouble(3, d.getRezistenta());
                if (d.getCardId() == null || d.getCardId() == 0) {
                    ps.setNull(4, java.sql.Types.BIGINT);
                } else {
                    ps.setLong(4, d.getCardId());
                }
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
        SELECT d.id, u.username, u.email, u.password,
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
                list.add(createDuckFromResultSet(rs, tip));
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
        SELECT p.id, u.username, u.email, u.password,
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
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("nume"),
                        rs.getString("prenume"),
                        rs.getDate("data_nasterii").toLocalDate(),
                        rs.getString("ocupatie"),
                        rs.getDouble("nivel_empatie")
                );
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
        SELECT d.id, u.username, u.email, u.password,
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
                TipRata tip = TipRata.valueOf(rs.getString("tip"));
                list.add(createDuckFromResultSet(rs, tip));
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
        Duck d;
        if (tip == TipRata.SWIMMING) {
            d = new SwimmingDuck(
                    rs.getLong("id"), rs.getString("username"),
                    rs.getString("email"), rs.getString("password"),
                    rs.getDouble("viteza"), rs.getDouble("rezistenta")
            );
        } else if (tip == TipRata.FLYING_AND_SWIMMING) {
            d = new FlyingAndSwimmingDuck(
                    rs.getLong("id"), rs.getString("username"),
                    rs.getString("email"), rs.getString("password"),
                    rs.getDouble("viteza"), rs.getDouble("rezistenta")
            );
        } else {
            d = new FlyingDuck(
                    rs.getLong("id"), rs.getString("username"),
                    rs.getString("email"), rs.getString("password"),
                    rs.getDouble("viteza"), rs.getDouble("rezistenta")
            );
        }

        d.setCardId(rs.getLong("card_id"));
        return d;
    }

    public Page<User> findAllOnPage(Pageable pageable) {
        List<User> list = new ArrayList<>();

        String sql = """
        SELECT u.id, u.username, u.email, u.password,
               p.nume, p.prenume, p.data_nasterii, p.ocupatie, p.nivel_empatie,
               d.tip, d.viteza, d.rezistenta, d.card_id
        FROM users u
        LEFT JOIN person p ON p.id = u.id
        LEFT JOIN duck d ON d.id = u.id
        ORDER BY u.id
        LIMIT ? OFFSET ?
    """;

        int totalElements = 0;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            var rsCount = conn.prepareStatement("SELECT COUNT(*) AS total FROM users").executeQuery();
            if(rsCount.next()) totalElements = rsCount.getInt("total");

            ps.setInt(1, pageable.getPageSize());
            ps.setInt(2, pageable.getPageNumber() * pageable.getPageSize());
            var rs = ps.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("id");

                if (cache.containsKey(id)) {
                    list.add(cache.get(id));
                    continue;
                }

                User user = null;

                if (rs.getString("nume") != null) {
                    user = new Person(
                            id,
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("nume"),
                            rs.getString("prenume"),
                            rs.getDate("data_nasterii").toLocalDate(),
                            rs.getString("ocupatie"),
                            rs.getDouble("nivel_empatie")
                    );
                } else if (rs.getString("tip") != null) {
                    TipRata tip = TipRata.valueOf(rs.getString("tip"));
                    Duck d;
                    if (tip == TipRata.SWIMMING) {
                        d = new SwimmingDuck(
                                id, rs.getString("username"),
                                rs.getString("email"), rs.getString("password"),
                                rs.getDouble("viteza"), rs.getDouble("rezistenta")
                        );
                    } else if (tip == TipRata.FLYING_AND_SWIMMING) {
                        d = new FlyingAndSwimmingDuck(
                                id, rs.getString("username"),
                                rs.getString("email"), rs.getString("password"),
                                rs.getDouble("viteza"), rs.getDouble("rezistenta")
                        );
                    } else {
                        d = new FlyingDuck(
                                id, rs.getString("username"),
                                rs.getString("email"), rs.getString("password"),
                                rs.getDouble("viteza"), rs.getDouble("rezistenta")
                        );
                    }
                    d.setCardId(rs.getLong("card_id"));
                    user = d;
                }

                if (user != null) {
                    list.add(user);
                    cache.put(id, user);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error getPage: " + e.getMessage());
        }

        return new Page<>(list, totalElements);
    }

    /**
     * Returneaza o pagina de rate, optional filtrata dupa tip.
     *
     * @param pageable Pageable, obiectul care contine informatii despre pagina
     * @param tipRata TipRata, tipul ratei pentru filtrare (null pentru toate ratele)
     * @return Page<Duck>, pagina de rate.
     */
    public Page<Duck> findAllDucksOnPage(Pageable pageable, TipRata tipRata) {
        List<Duck> list = new ArrayList<>();

        String baseSql = """
        FROM duck d
        JOIN users u ON u.id = d.id
        WHERE 1 = 1
    """;

        String whereClause = (tipRata != null) ? " AND d.tip = ? " : "";

        String countSql = "SELECT COUNT(*) AS total " + baseSql + whereClause;

        String selectSql = """
        SELECT d.id, u.username, u.email, u.password,
               d.tip, d.viteza, d.rezistenta, d.card_id
    """ + baseSql + whereClause + " ORDER BY d.id LIMIT ? OFFSET ?";

        int totalElements = 0;

        try (Connection conn = getConnection()) {
            try (PreparedStatement psCount = conn.prepareStatement(countSql)) {
                if (tipRata != null) {
                    psCount.setString(1, tipRata.toString());
                }
                var rsCount = psCount.executeQuery();
                if (rsCount.next()) {
                    totalElements = rsCount.getInt("total");
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                int paramIndex = 1;
                if (tipRata != null) {
                    ps.setString(paramIndex++, tipRata.toString());
                }
                ps.setInt(paramIndex++, pageable.getPageSize());
                ps.setInt(paramIndex, pageable.getPageNumber() * pageable.getPageSize());

                var rs = ps.executeQuery();

                while (rs.next()) {
                    TipRata tip = TipRata.valueOf(rs.getString("tip"));
                    list.add(createDuckFromResultSet(rs, tip));
                }
            }

        } catch (Exception e) {
            System.out.println("Error findAllDucksOnPage: " + e.getMessage());
        }

        return new Page<>(list, totalElements);
    }

    /**
     * Returneaza o pagina de persoane.
     *
     * @param pageable Pageable, obiectul care contine informatii despre pagina
     * @return Page<Person>, pagina de persoane.
     */
    public Page<Person> findAllPersonsOnPage(Pageable pageable) {
        List<Person> list = new ArrayList<>();

        String selectSql = """
        SELECT p.id, u.username, u.email, u.password,
               p.nume, p.prenume, p.data_nasterii, p.ocupatie, p.nivel_empatie
        FROM person p
        JOIN users u ON u.id = p.id
        ORDER BY p.id
        LIMIT ? OFFSET ?
    """;

        int totalElements = 0;

        try (Connection conn = getConnection()) {
            try (PreparedStatement psCount = conn.prepareStatement("SELECT COUNT(*) AS total FROM person")) {
                var rsCount = psCount.executeQuery();
                if (rsCount.next()) {
                    totalElements = rsCount.getInt("total");
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setInt(1, pageable.getPageSize());
                ps.setInt(2, pageable.getPageNumber() * pageable.getPageSize());

                var rs = ps.executeQuery();

                while (rs.next()) {
                    Person p = new Person(
                            rs.getLong("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("nume"),
                            rs.getString("prenume"),
                            rs.getDate("data_nasterii").toLocalDate(),
                            rs.getString("ocupatie"),
                            rs.getDouble("nivel_empatie")
                    );
                    list.add(p);
                }
            }

        } catch (Exception e) {
            System.out.println("Error findAllPersonsOnPage: " + e.getMessage());
        }

        return new Page<>(list, totalElements);
    }

    /**
     * Returneaza un user dupa username in baza de date.
     * @param username String, username-ul cautat
     * @return Optional<User>, user-ul gasit sau gol daca nu exista
     */
    public Optional<User> findByUsername(String username) {
        String sql = """
            SELECT u.id, u.username, u.email, u.password,
                   p.nume, p.prenume, p.data_nasterii, p.ocupatie, p.nivel_empatie,
                   d.tip, d.viteza, d.rezistenta, d.card_id
            FROM users u
            LEFT JOIN person p ON p.id = u.id
            LEFT JOIN duck d ON d.id = u.id
            WHERE u.username = ?
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            var rs = ps.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            User user = null;

            if (rs.getString("nume") != null) {
                user = new Person(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("nume"),
                        rs.getString("prenume"),
                        rs.getDate("data_nasterii").toLocalDate(),
                        rs.getString("ocupatie"),
                        rs.getDouble("nivel_empatie")
                );
            } else if (rs.getString("tip") != null) {
                TipRata tip = TipRata.valueOf(rs.getString("tip"));
                Duck d;
                if (tip == TipRata.SWIMMING) {
                    d = new SwimmingDuck(
                            rs.getLong("id"), rs.getString("username"),
                            rs.getString("email"), rs.getString("password"),
                            rs.getDouble("viteza"), rs.getDouble("rezistenta")
                    );
                } else if (tip == TipRata.FLYING_AND_SWIMMING) {
                    d = new FlyingAndSwimmingDuck(
                            rs.getLong("id"), rs.getString("username"),
                            rs.getString("email"), rs.getString("password"),
                            rs.getDouble("viteza"), rs.getDouble("rezistenta")
                    );
                } else {
                    d = new FlyingDuck(
                            rs.getLong("id"), rs.getString("username"),
                            rs.getString("email"), rs.getString("password"),
                            rs.getDouble("viteza"), rs.getDouble("rezistenta")
                    );
                }
                d.setCardId(rs.getLong("card_id"));
                user = d;
            }
            if (user != null) {
                return Optional.of(user);
            }
        }
        catch (SQLException e) {
            System.out.println("Error finding user by username: " + e.getMessage());
        }
        return Optional.empty();
    }
}