package org.example.model;

/**
 * Clasa pentru prietenie intre doi utilizatori
 */
public class Friendship {
    private User u1;
    private User u2;

    /**
     * Constructor pentru clasa Friendship
     * @param u1 User, primul utilizator
     * @param u2 User, al doilea utilizator
     */
    public Friendship(User u1, User u2) {
        this.u1 = u1;
        this.u2 = u2;
    }

    /**
     * Getter pentru primul utilizator
     * @return User, primul utilizator
     */
    public User getU1() {
        return u1;
    }

    /**
     * Getter pentru al doilea utilizator
     * @return User, al doilea utilizator
     */
    public User getU2() {
        return u2;
    }

    /**
     * Suprascrierea metodei toString pentru afisarea prieteniei
     * @return String, reprezentarea prieteniei ca String
     */
    @Override
    public String toString() {
        return u1.getUsername() + " <-> " + u2.getUsername();
    }
}
