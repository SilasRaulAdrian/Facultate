package org.example.model;

import java.time.LocalDateTime;

/**
 * Clasa Notification care reprezinta o notificare in sistem
 */
public class Notification extends Entity<Long> {
    private Long userId;
    private String message;
    private boolean seen;
    private LocalDateTime createdAt;

    /**
     * Constructor pentru clasa Notification
     * @param id Long, id-ul notificarii
     * @param userId Long, id-ul utilizatorului caruia ii este destinata notificarea
     * @param message String, mesajul notificarii
     * @param seen boolean, statusul de vizualizare al notificarii
     * @param createdAt LocalDateTime, data si ora crearii notificarii
     */
    public Notification(Long id, Long userId, String message, boolean seen, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.seen = seen;
        this.createdAt = createdAt;
    }

    /**
     * Constructor pentru clasa Notification fara id (pentru crearea unei noi notificari)
     * @param userId Long, id-ul utilizatorului caruia ii este destinata notificarea
     * @param message String, mesajul notificarii
     */
    public Notification(Long userId, String message) {
        this(null, userId, message, false, LocalDateTime.now());
    }

    /**
     * Getter pentru userId
     * @return Long, id-ul utilizatorului
     */
    public Long getUserId() { return userId; }

    /**
     * Getter pentru message
     * @return String, mesajul notificarii
     */
    public String getMessage() { return message; }

    /**
     * Getter pentru seen
     * @return boolean, statusul de vizualizare al notificarii
     */
    public boolean isSeen() { return seen; }

    /**
     * Getter pentru createdAt
     * @return LocalDateTime, data si ora crearii notificarii
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    /**
     * Setter pentru seen
     * @param seen boolean, statusul de vizualizare al notificarii
     */
    public void setSeen(boolean seen) { this.seen = seen; }
}
