package org.example.model;

/**
 * Clasa FriendRequest care reprezinta o cerere de prietenie intre doi utilizatori
 */
public class FriendRequest extends Entity<Long> {
    private User from;
    private User to;
    private FriendRequestStatus status;

    /**
     * Constructor pentru clasa FriendRequest
     * @param id Long, id-ul cererii de prietenie
     * @param from User, utilizatorul care trimite cererea
     * @param to User, utilizatorul care primeste cererea
     * @param status FriendRequestStatus, statusul cererii de prietenie
     */
    public FriendRequest(Long id, User from, User to, FriendRequestStatus status) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.status = status;
    }

    /**
     * Getter pentru utilizatorul care trimite cererea
     * @return User, utilizatorul care trimite cererea
     */
    public User getFrom() { return from; }

    /**
     * Getter pentru utilizatorul care primeste cererea
     * @return User, utilizatorul care primeste cererea
     */
    public User getTo() { return to; }

    /**
     * Getter pentru statusul cererii de prietenie
     * @return FriendRequestStatus, statusul cererii de prietenie
     */
    public FriendRequestStatus getStatus() { return status; }

    /**
     * Setter pentru statusul cererii de prietenie
     * @param status FriendRequestStatus, noul status al cererii de prietenie
     */
    public void setStatus(FriendRequestStatus status) {
        this.status = status;
    }
}
