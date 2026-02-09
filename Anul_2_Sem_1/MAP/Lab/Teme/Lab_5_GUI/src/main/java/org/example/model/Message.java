package org.example.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Clasa Message care reprezinta un mesaj in sistem
 */
public class Message extends Entity<Long> {
    private User from;
    private List<User> to;
    private String message;
    private LocalDateTime date;
    private Message reply;

    /**
     * Constructor pentru clasa Message
     * @param id Long, id-ul mesajului
     * @param from User, utilizatorul care trimite mesajul
     * @param to List<User>, lista de utilizatori care primesc mesajul
     * @param message String, continutul mesajului
     * @param date LocalDateTime, data si ora trimiterii mesajului
     * @param reply Message, mesajul la care se raspunde (daca exista)
     */
    public Message(Long id, User from, List<User> to, String message, LocalDateTime date, Message reply) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.reply = reply;
    }

    public Message() {}

    /**
     * Constructor pentru clasa Message fara mesajul la care se raspunde
     * @param id Long, id-ul mesajului
     * @param from User, utilizatorul care trimite mesajul
     * @param to List<User>, lista de utilizatori care primesc mesajul
     * @param message String, continutul mesajului
     * @param date LocalDateTime, data si ora trimiterii mesajului
     */
    public Message(Long id, User from, List<User> to, String message, LocalDateTime date) {
        this(id, from, to, message, date, null);
    }

    /**
     * Getter pentru utilizatorul care trimite mesajul
     * @return User, utilizatorul care trimite mesajul
     */
    public User getFrom() {
        return from;
    }

    /**
     * Getter pentru lista de utilizatori care primesc mesajul
     * @return List<User>, lista de utilizatori care primesc mesajul
     */
    public List<User> getTo() {
        return to;
    }

    /**
     * Getter pentru continutul mesajului
     * @return String, continutul mesajului
     */
    public String getMessage() {
        return message;
    }

    /**
     * Getter pentru data si ora trimiterii mesajului
     * @return LocalDateTime, data si ora trimiterii mesajului
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Getter pentru mesajul la care se raspunde
     * @return Message, mesajul la care se raspunde
     */
    public Message getReply() {
        return reply;
    }

    /**
     * Setter pentru utilizatorul care trimite mesajul
     * @param from User, utilizatorul care trimite mesajul
     */
    public void setFrom(User from) {
        this.from = from;
    }

    /**
     * Setter pentru lista de utilizatori care primesc mesajul
     * @param to List<User>, lista de utilizatori care primesc mesajul
     */
    public void setTo(List<User> to) {
        this.to = to;
    }

    /**
     * Setter pentru continutul mesajului
     * @param message String, continutul mesajului
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Setter pentru data si ora trimiterii mesajului
     * @param date LocalDateTime, data si ora trimiterii mesajului
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Setter pentru mesajul la care se raspunde
     * @param reply Message, mesajul la care se raspunde
     */
    public void setReply(Message reply) {
        this.reply = reply;
    }
}
