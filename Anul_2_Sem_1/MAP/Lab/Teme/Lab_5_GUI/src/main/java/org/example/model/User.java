package org.example.model;

import org.example.repository.FriendshipRepository;

import java.util.HashSet;
import java.util.Set;

/**
 * Clasa abstracta User care reprezinta un utilizator in sistem
 */
public abstract class User extends Entity<Long> {
    protected String username;
    protected String email;
    protected String password;
    protected Set<User> friends = new HashSet<>();

    /**
     * Constructor pentru clasa User
     * @param id Long, id-ul utilizatorului
     * @param username String, username-ul utilizatorului
     * @param email String, email-ul utilizatorului
     * @param password String, parola utilizatorului
     */
    public User(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Getter pentru id-ul utilizatorului
     * @return Long, id-ul utilizatorului
     */
    public Long getId() {
        return id;
    }

    /**
     * Getter pentru username-ul utilizatorului
     * @return String, username-ul utilizatorului
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter pentru email-ul utilizatorului
     * @return String, email-ul utilizatorului
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter pentru parola utilizatorului
     * @return String, parola utilizatorului
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter pentru lista de prieteni ai utilizatorului
     * @return Set<User>, lista de prieteni ai utilizatorului
     */
    public Set<User> getFriends() {
        return friends;
    }

    /**
     * Setter pentru id-ul utilizatorului
     * @param id Long, id-ul utilizatorului
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Setter pentru username-ul utilizatorului
     * @param username String, username-ul utilizatorului
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Setter pentru email-ul utilizatorului
     * @param email String, email-ul utilizatorului
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Setter pentru parola utilizatorului
     * @param password String, parola utilizatorului
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Functie care adauga un prieten utilizatorului fara a salva in repository
     * @param user User, prietenul care va fi adaugat
     */
    public void addFriendWithoutSave(User user) {
        friends.add(user);
    }

    /**
     * Functie care elimina un prieten al utilizatorului si sterge din baza de date
     * @param user User, prietenul care va fi eliminat
     * @param friendshipRepo FriendshipRepository, repository-ul de prietenii
     */
    public void removeFriend(User user, FriendshipRepository friendshipRepo) {
        friends.remove(user);

        for (Friendship friendship : friendshipRepo.getAll()) {
            if ((friendship.getU1().getId().equals(this.id) && friendship.getU2().getId().equals(user.getId())) ||
                    (friendship.getU1().getId().equals(user.getId()) && friendship.getU2().getId().equals(this.id))) {
                friendshipRepo.delete(friendship.getId());
                break;
            }
        }
    }

    /**
     * Functie care elimina un prieten al utilizatorului fara a salva in repository
     * @param user User, prietenul care va fi eliminat
     */
    public void removeFriendWithoutSave(User user) {
        friends.remove(user);
    }

    /**
     * Suprascrierea metodei toString pentru clasa User
     * @return String, reprezentarea utilizatorului ca String
     */
    @Override
    public String toString() {
        return username + " (id=" + id + ")";
    }

    /**
     * Functie care primeste un mesaj si afiseaza o notificare
     * @param message String, mesajul primit
     */
    public void receiveMessage(String message) {
        System.out.println("[NOTIFICATION] " + username + " a primit mesaj: " + message);
    }
}
