package org.example.service;

import org.example.model.Notification;
import org.example.repository.NotificationRepository;

import java.util.List;

/**
 * Clasa NotificationService pentru gestionarea notificarilor
 */
public class NotificationService {
    private final NotificationRepository repo;

    /**
     * Constructor pentru clasa NotificationService
     * @param repo NotificationRepository, repository-ul pentru notificari
     */
    public NotificationService(NotificationRepository repo) {
        this.repo = repo;
    }

    /**
     * Metoda pentru notificarea unui utilizator
     * @param userId Long, id-ul utilizatorului
     * @param message String, mesajul notificarii
     */
    public void notifyUser(Long userId, String message) {
        repo.save(new Notification(userId, message));
    }

    /**
     * Metoda pentru obtinerea notificarilor nevizualizate ale unui utilizator
     * @param userId Long, id-ul utilizatorului
     * @return List<Notification>, lista de notificari nevizualizate
     */
    public List<Notification> getUnseen(Long userId) {
        return repo.findUnseen(userId);
    }

    /**
     * Metoda pentru marcarea unei notificari ca vizualizata
     * @param notificationId Long, id-ul notificarii
     */
    public void markSeen(Long notificationId) {
        repo.markAsSeen(notificationId);
    }
}
