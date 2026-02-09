package org.example.repository;
import lombok.RequiredArgsConstructor;
import org.example.config.ConnectionManager;
import org.example.model.*;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Clasa pentru gestionarea depozitului de utilizatori
 */
@RequiredArgsConstructor
public class UserRepository implements Repository<Long, User> {
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

            if (rs.getString("nume") != null) {
                return Optional.of(new Person(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("nume"),
                        rs.getString("prenume"),
                        rs.getDate("data_nasterii").toLocalDate(),
                        rs.getString("ocupatie"),
                        rs.getDouble("nivel_empatie")
                ));
            }

            if (rs.getString("tip") != null) {
                TipRata tip = TipRata.valueOf(rs.getString("tip"));
                Duck d;

                if (tip == TipRata.SWIMMING) {
                    d = new SwimmingDuck(
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
                return Optional.of(d);
            }
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

                if (rs.getString("nume") != null) {
                    list.add(new Person(
                            rs.getLong("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("nume"),
                            rs.getString("prenume"),
                            rs.getDate("data_nasterii").toLocalDate(),
                            rs.getString("ocupatie"),
                            rs.getDouble("nivel_empatie")
                    ));
                    continue;
                }

                if (rs.getString("tip") != null) {
                    TipRata tip = TipRata.valueOf(rs.getString("tip"));
                    Duck d;
                    if (tip == TipRata.SWIMMING) {
                        d = new SwimmingDuck(
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
                    list.add(d);
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
                ps.setObject(5, d.getCardId());
                ps.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
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
                ps.setObject(4, d.getCardId());
                ps.setLong(5, d.getId());
                ps.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
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
        } catch (SQLException e) {
            System.out.println("Error delete: " + e.getMessage());
        }

        return toDelete;
    }
}
