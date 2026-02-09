package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.config.ConnectionManager;
import org.example.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Clasa pentru gestionarea depozitului de carduri
 */
@RequiredArgsConstructor
public class CardRepository implements Repository<Long, Card<? extends Duck>> {
    private final UserRepository userRepo;

    /**
     * Metoda pentru obtinerea conexiunii la baza de date
     * @return Connection, conexiunea la baza de date
     */
    private Connection getConnection() {
        return ConnectionManager.getConnection();
    }

    /**
     * Metoda pentru gasirea unui card dupa id
     * @param id Long, id-ul cardului
     * @return Optional<Card<? extends Duck>>, cardul gasit sau gol daca nu exista
     */
    @Override
    public Optional<Card<? extends Duck>> findById(Long id) {
        String sql = "SELECT id, name, type FROM cards WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            var rs = ps.executeQuery();

            if (!rs.next()) return Optional.empty();

            long cardId = rs.getLong("id");
            String name = rs.getString("name");
            String type = rs.getString("type");

            Card<? extends Duck> card = createCardByType(cardId, name, type);

            loadMembers(conn, card);

            return Optional.of(card);

        } catch (Exception e) {
            System.out.println("Error findById: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Metoda pentru obtinerea tuturor cardurilor
     * @return Iterable<Card<? extends Duck>>, lista cu toate cardurile
     */
    @Override
    public Iterable<Card<? extends Duck>> getAll() {
        List<Card<? extends Duck>> list = new ArrayList<>();

        String sql = "SELECT id, name, type FROM cards";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String type = rs.getString("type");

                Card<? extends Duck> card = createCardByType(id, name, type);
                loadMembers(conn, card);

                list.add(card);
            }

        } catch (Exception e) {
            System.out.println("Error getAll: " + e.getMessage());
        }

        return list;
    }

    /**
     * Metoda pentru salvarea unui card
     * @param card Card<? extends Duck>, cardul care va fi salvat
     * @return Optional<Card<? extends Duck>>, cardul salvat sau gol daca nu s-a putut salva
     */
    @Override
    public Optional<Card<? extends Duck>> save(Card<? extends Duck> card) {
        String insertCard = "INSERT INTO cards(name, type) VALUES(?, ?) RETURNING id";
        String insertMember = "INSERT INTO card_members(card_id, duck_id) VALUES(?, ?)";

        try (Connection conn = getConnection()) {

            conn.setAutoCommit(false);

            PreparedStatement psCard =
                    conn.prepareStatement(insertCard);

            psCard.setString(1, card.getNumeCard());
            psCard.setString(2, card.getTip());
            var rs = psCard.executeQuery();

            if (!rs.next()) {
                conn.rollback();
                return Optional.empty();
            }

            long id = rs.getLong("id");
            card.setId(id);

            PreparedStatement psMember =
                    conn.prepareStatement(insertMember);

            for (Duck d : card.getMembri()) {
                psMember.setLong(1, id);
                psMember.setLong(2, d.getId());
                psMember.addBatch();
            }

            psMember.executeBatch();

            conn.commit();
            return Optional.of(card);

        } catch (Exception e) {
            System.out.println("Error save card: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Metoda pentru actualizarea unui card
     * @param card Card<? extends Duck>, cardul care va fi actualizat
     * @return Optional<Card<? extends Duck>>, cardul actualizat sau gol daca nu s-a putut actualiza
     */
    @Override
    public Optional<Card<? extends Duck>> update(Card<? extends Duck> card) {
        String updateCard = "UPDATE cards SET name = ?, type = ? WHERE id = ?";
        String deleteMembers = "DELETE FROM card_members WHERE card_id = ?";
        String insertMember = "INSERT INTO card_members(card_id, duck_id) VALUES(?, ?)";

        try (Connection conn = getConnection()) {

            conn.setAutoCommit(false);

            PreparedStatement psCard =
                    conn.prepareStatement(updateCard);

            psCard.setString(1, card.getNumeCard());
            psCard.setString(2, card.getTip());
            psCard.setLong(3, card.getId());

            if (psCard.executeUpdate() == 0) {
                conn.rollback();
                return Optional.empty();
            }

            PreparedStatement psDel =
                    conn.prepareStatement(deleteMembers);
            psDel.setLong(1, card.getId());
            psDel.executeUpdate();

            PreparedStatement psMember =
                    conn.prepareStatement(insertMember);

            for (Duck d : card.getMembri()) {
                psMember.setLong(1, card.getId());
                psMember.setLong(2, d.getId());
                psMember.addBatch();
            }

            psMember.executeBatch();

            conn.commit();
            return Optional.of(card);

        } catch (Exception e) {
            System.out.println("Error update card: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Metoda pentru stergerea unui card dupa id
     * @param id Long, id-ul cardului care va fi sters
     * @return Optional<Card<? extends Duck>>, cardul sters sau gol daca nu s-a putut sterge
     */
    @Override
    public Optional<Card<? extends Duck>> delete(Long id) {
        Optional<Card<? extends Duck>> existing = findById(id);
        if (existing.isEmpty()) return Optional.empty();

        String deleteMembers = "DELETE FROM card_members WHERE card_id = ?";
        String deleteCard = "DELETE FROM cards WHERE id = ?";

        try (Connection conn = getConnection()) {

            conn.setAutoCommit(false);

            PreparedStatement ps1 =
                    conn.prepareStatement(deleteMembers);
            ps1.setLong(1, id);
            ps1.executeUpdate();

            PreparedStatement ps2 =
                    conn.prepareStatement(deleteCard);
            ps2.setLong(1, id);

            if (ps2.executeUpdate() == 0) {
                conn.rollback();
                return Optional.empty();
            }

            conn.commit();
            return existing;

        } catch (Exception e) {
            System.out.println("Error delete card: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Metoda pentru crearea unui card in functie de tip
     * @param id Long, id-ul cardului
     * @param name String, numele cardului
     * @param type String, tipul cardului
     * @return Card<? extends Duck>, cardul creat
     */
    private Card<? extends Duck> createCardByType(Long id, String name, String type) {
        if ("SWIMMING".equalsIgnoreCase(type)) {
            return new SwimmingCard(id, name, type);
        }
        if ("FLYING".equalsIgnoreCase(type)) {
            return new FlyingCard(id, name, type);
        }
        throw new IllegalArgumentException("Unknown card type: " + type);
    }

    /**
     * Metoda pentru incarcarea membrilor unui card din baza de date
     * @param conn Connection, conexiunea la baza de date
     * @param card Card<? extends Duck>, cardul caruia i se incarca membrii
     * @throws SQLException daca apare o eroare la interogarea bazei de date
     */
    private void loadMembers(Connection conn, Card<? extends Duck> card) throws SQLException {
        String sql = "SELECT duck_id FROM card_members WHERE card_id = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, card.getId());
        var rs = ps.executeQuery();

        while (rs.next()) {
            long duckId = rs.getLong("duck_id");

            Optional<User> opt = userRepo.findById(duckId);

            if (opt.isPresent() && opt.get() instanceof Duck d) {
                ((Card<Duck>) card).getMembri().add(d);
                d.setCardId(card.getId());
            }
        }
    }
}
