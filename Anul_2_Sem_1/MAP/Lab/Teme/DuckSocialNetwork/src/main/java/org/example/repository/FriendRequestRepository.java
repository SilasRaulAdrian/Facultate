package org.example.repository;

import org.example.model.FriendRequest;

import java.util.List;
import java.util.Optional;

/**
 * Interfata pentru repository-ul de cereri de prietenie
 */
public interface FriendRequestRepository extends Repository<Long, FriendRequest> {
    /**
     * Metoda pentru a gasi toate cererile de prietenie in asteptare pentru un utilizator
     * @param userId Long, id-ul utilizatorului
     * @return List<FriendRequest>, lista de cereri de prietenie in asteptare
     */
    List<FriendRequest> findPendingForUser(Long userId);

    /**
     * Metoda pentru a gasi o cerere de prietenie intre doi utilizatori
     * @param fromId Long, id-ul utilizatorului care a trimis cererea
     * @param toId Long, id-ul utilizatorului care a primit cererea
     * @return Optional<FriendRequest>, cererea de prietenie gasita sau empty daca nu exista
     */
    Optional<FriendRequest> findBetween(Long fromId, Long toId);
}
