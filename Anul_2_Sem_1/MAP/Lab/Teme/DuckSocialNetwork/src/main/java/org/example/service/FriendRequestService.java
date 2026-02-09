package org.example.service;

import org.example.model.FriendRequest;
import org.example.model.FriendRequestStatus;
import org.example.model.User;
import org.example.observer.Observable;
import org.example.observer.Signal;
import org.example.repository.FriendRequestDBRepository;

import java.util.List;

/**
 * Service pentru gestionarea cererilor de prietenie.
 */
public class FriendRequestService implements Observable {
    private final FriendRequestDBRepository repo;
    private final FriendshipService friendshipService;

    /**
     * Constructor pentru FriendRequestService.
     * @param repo Repozitoriul pentru cererile de prietenie.
     * @param friendshipService Service-ul pentru prietenii.
     */
    public FriendRequestService(FriendRequestDBRepository repo, FriendshipService friendshipService) {
        this.repo = repo;
        this.friendshipService = friendshipService;
    }

    /**
     * Trimite o cerere de prietenie de la un utilizator la altul.
     * @param from Utilizatorul care trimite cererea.
     * @param to Utilizatorul care primeste cererea.
     */
    public void sendRequest(User from, User to) {
        repo.findBetween(from.getId(), to.getId())
                .ifPresent(fr -> { throw new RuntimeException("Already sent"); });

        FriendRequest fr = new FriendRequest(null, from, to, FriendRequestStatus.PENDING);
        repo.save(fr);

        notifyObservers(new Signal("NEW_FRIEND_REQUEST", from, List.of(to)));
    }

    /**
     * Accepta o cerere de prietenie.
     * @param requestId ID-ul cererii de prietenie.
     */
    public void accept(Long requestId) {
        FriendRequest fr = repo.findById(requestId).orElseThrow();
        fr.setStatus(FriendRequestStatus.APPROVED);

        friendshipService.addFriendship(
                fr.getFrom().getId(),
                fr.getTo().getId()
        );

        repo.delete(requestId);
        notifyObservers(new Signal("REQUEST_ACCEPTED"));

        notifyObservers(new Signal(
                "FRIEND_CHANGED",
                fr.getFrom(),
                List.of(fr.getTo())
        ));

        notifyObservers(new Signal(
                "FRIEND_CHANGED",
                fr.getTo(),
                List.of(fr.getFrom())
        ));
    }

    /**
     * Refuza o cerere de prietenie.
     * @param requestId ID-ul cererii de prietenie.
     */
    public void decline(Long requestId) {
        repo.delete(requestId);
        notifyObservers(new Signal("REQUEST_DECLINED"));
    }

    /**
     * Obtine lista cererilor de prietenie in asteptare pentru un utilizator.
     * @param user Utilizatorul pentru care se obtin cererile.
     * @return Lista cererilor de prietenie in asteptare.
     */
    public List<FriendRequest> getPending(User user) {
        return repo.findPendingForUser(user.getId());
    }

    /**
     * Verifica daca exista deja o cerere de prietenie intre doi utilizatori.
     * @param loggedUser Utilizatorul autentificat.
     * @param selected Utilizatorul selectat.
     * @return True daca exista o cerere, altfel false.
     */
    public boolean requestExists(User loggedUser, User selected) {
        return repo.findBetween(loggedUser.getId(), selected.getId()).isPresent();
    }
}
