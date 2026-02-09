package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.config.ConnectionManager;
import org.example.model.Message;
import org.example.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Clasa pentru repository-ul de mesaje
 */
@RequiredArgsConstructor
public class MessageRepository implements Repository<Long, Message> {
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
     * Metoda pentru gasirea unui mesaj dupa id
     * @param id Long, id-ul mesajului
     * @return Optional<Message>, mesajul gasit sau empty daca nu exista
     */
    @Override
    public Optional<Message> findById(Long id) {
        Message msg = null;

        String sql = "SELECT * FROM messages WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            var rs = ps.executeQuery();

            if (rs.next()) {
                Long fromId = rs.getLong("from_user");
                String text = rs.getString("message");
                LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();
                Long replyTo = rs.getLong("reply_to");

                User fromUser = userRepo.findById(fromId).orElse(null);

                List<User> recipients = loadRecipients(id);

                Message replyMsg = replyTo != 0 ? findById(replyTo).orElse(null) : null;

                msg = new Message(id, fromUser, recipients, text, date, replyMsg);
                msg.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(msg);
    }

    /**
     * Metoda pentru obtinerea tuturor mesajelor
     * @return Iterable<Message>, toate mesajele din baza de date
     */
    @Override
    public Iterable<Message> getAll() {
        List<Message> messages = new ArrayList<>();

        String sql = "SELECT * FROM messages ORDER BY date";

        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             var rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Long id = rs.getLong("id");
                Long fromId = rs.getLong("from_user");
                String text = rs.getString("message");
                LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();
                Long replyTo = rs.getLong("reply_to");

                User fromUser = userRepo.findById(fromId).orElse(null);
                List<User> recipients = loadRecipients(id);
                Message replyMsg = replyTo != 0 ? findById(replyTo).orElse(null) : null;

                Message msg = new Message(id, fromUser, recipients, text, date, replyMsg);
                msg.setId(id);
                messages.add(msg);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    /**
     * Metoda pentru salvarea unui mesaj in baza de date
     * @param msg Message, mesajul de salvat
     * @return Optional<Message>, empty daca salvarea a reusit, altfel mesajul nesalvat
     */
    @Override
    public Optional<Message> save(Message msg) {
        String sql = "INSERT INTO messages (from_user, message, date, reply_to) VALUES (?, ?, ?, ?) RETURNING id";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, msg.getFrom().getId());
            ps.setString(2, msg.getMessage());
            ps.setTimestamp(3, Timestamp.valueOf(msg.getDate()));

            if (msg.getReply() != null)
                ps.setLong(4, msg.getReply().getId());
            else
                ps.setNull(4, Types.BIGINT);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Long id = rs.getLong(1);
                msg.setId(id);

                saveRecipients(id, msg.getTo());
                return Optional.empty();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.of(msg);
    }

    /**
     * Metoda pentru actualizarea unui mesaj in baza de date
     * @param msg Message, mesajul de actualizat
     * @return Optional<Message>, empty daca actualizarea a reusit, altfel mesajul neactualizat
     */
    @Override
    public Optional<Message> update(Message msg) {
        String sql = "UPDATE messages SET message=?, reply_to=? WHERE id=?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, msg.getMessage());

            if (msg.getReply() != null)
                ps.setLong(2, msg.getReply().getId());
            else
                ps.setNull(2, Types.BIGINT);

            ps.setLong(3, msg.getId());

            int rows = ps.executeUpdate();

            if (rows == 0) return Optional.of(msg);

            deleteRecipients(msg.getId());
            saveRecipients(msg.getId(), msg.getTo());

            return Optional.empty();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.of(msg);
    }

    /**
     * Metoda pentru stergerea unui mesaj din baza de date
     * @param id Long, id-ul mesajului de sters
     * @return Optional<Message>, mesajul sters sau empty daca nu a fost gasit
     */
    @Override
    public Optional<Message> delete(Long id) {
        Optional<Message> msg = findById(id);
        if (msg.isEmpty()) return Optional.empty();

        String sql = "DELETE FROM messages WHERE id=?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

            return msg;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * Incarca destinatarii unui mesaj din baza de date
     * @param messageId Long, id-ul mesajului
     * @return List<User>, lista destinatarilor mesajului
     */
    private List<User> loadRecipients(Long messageId) {
        List<User> list = new ArrayList<>();

        String sql = "SELECT user_id FROM message_recipients WHERE message_id=?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, messageId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Long uid = rs.getLong("user_id");
                userRepo.findById(uid).ifPresent(list::add);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Salveaza destinatarii unui mesaj in baza de date
     * @param msgId Long, id-ul mesajului
     * @param recipients List<User>, lista destinatarilor mesajului
     * @throws SQLException in caz de eroare la salvare
     */
    private void saveRecipients(Long msgId, List<User> recipients) throws SQLException {
        String sql = "INSERT INTO message_recipients (message_id, user_id) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (User u : recipients) {
                ps.setLong(1, msgId);
                ps.setLong(2, u.getId());
                ps.executeUpdate();
            }
        }
    }

    /**
     * Sterge destinatarii unui mesaj din baza de date
     * @param msgId Long, id-ul mesajului
     * @throws SQLException in caz de eroare la stergere
     */
    private void deleteRecipients(Long msgId) throws SQLException {
        String sql = "DELETE FROM message_recipients WHERE message_id=?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, msgId);
            ps.executeUpdate();
        }
    }

    /**
     * Obtine toate mesajele care implica un anumit utilizator si o lista de alti utilizatori
     * @param me User, utilizatorul principal
     * @param others List<User>, lista celorlalti utilizatori
     * @return List<Message>, lista mesajelor care implica utilizatorii specificati
     */
    public List<Message> getMessagesForUsers(User me, List<User> others) {
        List<Long> participantIds = new ArrayList<>();
        participantIds.add(me.getId());
        for (User u : others) participantIds.add(u.getId());

        String inClause = participantIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(", "));

        String sql =
                "SELECT DISTINCT m.* " +
                        "FROM messages m " +
                        "LEFT JOIN message_recipients mr ON m.id = mr.message_id " +
                        "WHERE m.from_user IN (" + inClause + ") " +
                        "OR mr.user_id IN (" + inClause + ") " +
                        "ORDER BY m.date ASC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;

            for (Long id : participantIds) {
                ps.setLong(index++, id);
            }

            for (Long id : participantIds) {
                ps.setLong(index++, id);
            }

            ResultSet rs = ps.executeQuery();
            List<Message> messages = new ArrayList<>();

            while (rs.next()) {
                Message m = extractMessage(rs);
                messages.add(m);
            }

            return messages;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Extrage un mesaj dintr-un ResultSet
     * @param rs ResultSet, setul de rezultate din care se extrage mesajul
     * @return Message, mesajul extras
     * @throws Exception in caz de eroare la extragere
     */
    private Message extractMessage(ResultSet rs) throws Exception {
        Long id = rs.getLong("id");
        Long fromId = rs.getLong("from_user");
        String text = rs.getString("message");
        Timestamp ts = rs.getTimestamp("date");
        LocalDateTime date = ts.toLocalDateTime();
        Long replyTo = rs.getLong("reply_to");

        User fromUser = userRepo.findById(fromId).orElse(null);
        List<User> recipients = loadRecipients(id);

        Message replyMsg = null;
        if (replyTo != null && replyTo != 0) {
            replyMsg = findById(replyTo).orElse(null);
        }

        Message msg = new Message(id, fromUser, recipients, text, date, replyMsg);
        msg.setId(id);

        return msg;
    }

    /**
     * Obtine toate mesajele care formeaza o conversatie stricta intre un set de utilizatori
     * @param chatUsers Set<Long>, setul de id-uri ale utilizatorilor implicati in conversatie
     * @return List<Message>, lista mesajelor din conversatia stricta
     */
    public List<Message> getStrictConversation(Set<Long> chatUsers) {
        if (chatUsers == null || chatUsers.isEmpty())
            return List.of();

        try (Connection conn = getConnection()) {

            Long[] participants = chatUsers.toArray(Long[]::new);

            String sql =
                    "WITH msg AS (" +
                            "  SELECT m.id, m.from_user, " +
                            "    ARRAY(SELECT r.user_id FROM message_recipients r WHERE r.message_id = m.id ORDER BY r.user_id) AS recips " +
                            "  FROM messages m" +
                            ") " +
                            "SELECT m.* FROM messages m " +
                            "JOIN msg x ON x.id = m.id " +
                            "WHERE array_sort(array_append(x.recips, x.from_user)) = array_sort(?) " +
                            "ORDER BY m.date";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setArray(1, conn.createArrayOf("bigint", participants));

            ResultSet rs = ps.executeQuery();

            List<Message> list = new ArrayList<>();
            while (rs.next()) {
                list.add(extractMessage(rs));
            }

            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
