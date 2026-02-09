package org.example.observer;

/**
 * Clasa Signal care reprezinta un semnal trimis catre observatori
 */
public class Signal {
    private final String type;

    /**
     * Constructor pentru clasa Signal
     * @param type String, tipul semnalului
     */
    public Signal(String type) {
        this.type = type;
    }

    /**
     * Getter pentru tipul semnalului
     * @return String, tipul semnalului
     */
    public String getType() {
        return type;
    }
}
