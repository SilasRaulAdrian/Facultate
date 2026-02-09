package org.example.observer;

import org.example.model.User;

import java.util.List;

/**
 * Clasa Signal care reprezinta un semnal trimis catre observatori
 */
public class Signal {
    private final String type;
    private final User from;
    private final List<User> to;

    /**
     * Constructor pentru clasa Signal
     * @param type String, tipul semnalului
     */
    public Signal(String type) {
        this(type, null, null);
    }

    /**
     * Constructor pentru clasa Signal cu detalii despre expeditor si destinatari
     * @param type String, tipul semnalului
     * @param from User, utilizatorul care trimite semnalul
     * @param to List<User>, lista de utilizatori care primesc semnalul
     */
    public Signal(String type, User from, List<User> to) {
        this.type = type;
        this.from = from;
        this.to = to;
    }

    /**
     * Getter pentru tipul semnalului
     * @return String, tipul semnalului
     */
    public String getType() {
        return type;
    }

    /**
     * Getter pentru utilizatorul care trimite semnalul
     * @return User, utilizatorul care trimite semnalul
     */
    public User getFrom() { return from; }

    /**
     * Getter pentru lista de utilizatori care primesc semnalul
     * @return List<User>, lista de utilizatori care primesc semnalul
     */
    public List<User> getTo() { return to; }
}
