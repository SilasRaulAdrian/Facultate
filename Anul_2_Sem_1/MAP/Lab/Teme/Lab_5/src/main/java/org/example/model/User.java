package org.example.model;

import org.example.repository.InMemoryRepository;

import java.util.HashSet;
import java.util.Set;

/**
 * Clasa abstracta User care reprezinta un utilizator in sistem
 */
public abstract class User {
    protected Long id;
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
     * Functie care adauga un prieten utilizatorului
     * @param user User, prietenul care va fi adaugat
     */
    public void addFriend(User user, InMemoryRepository repo) {
        friends.add(user);
        repo.saveFriendships(repo.getAllUsers());
    }

    /**
     * Functie care elimina un prieten al utilizatorului
     * @param user User, prietenul care va fi eliminat
     */
    public void removeFriend(User user, InMemoryRepository repo) {
        friends.remove(user);
        repo.saveFriendships(repo.getAllUsers());
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
