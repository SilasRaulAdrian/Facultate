package org.example.service;

import org.example.exceptions.FriendshipAlreadyExistsException;
import org.example.exceptions.FriendshipNotFoundException;
import org.example.model.Friendship;
import org.example.model.User;
import org.example.repository.InMemoryRepository;
import org.example.validation.FriendshipValidationStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Clasa pentru service-ul de prietenii
 */
public class FriendshipService {
    private final InMemoryRepository repo;
    private final FriendshipValidationStrategy validator = new FriendshipValidationStrategy();

    /**
     * Constructor pentru clasa FriendshipService
     * @param repo InMemoryRepository, repository-ul in memorie
     */
    public FriendshipService(InMemoryRepository repo) {
        this.repo = repo;

        loadFriendshipFromCsv();
    }

    /**
     * Incarca prieteniile din fisier
     */
    public void loadFriendshipFromCsv() {
        for (var f : repo.loadFriendships()) {
            User u1 = repo.findById(f[0]);
            User u2 = repo.findById(f[1]);
            if (u1 != null && u2 != null) {
                u1.addFriend(u2, repo);
                u2.addFriend(u1, repo);
            }
        }
    }

    /**
     * Adauga o prietenie intre doi utilizatori
     * @param userId1 Long, id-ul primului utilizator
     * @param userId2 Long, id-ul celui de-al doilea utilizator
     * @throws FriendshipAlreadyExistsException daca prietenia exista deja
     */
    public void addFriendship(Long userId1, Long userId2) {
        validator.validate(userId1, userId2);

        User user1 = repo.findById(userId1);
        User user2 = repo.findById(userId2);

        if (user1.getFriends().contains(user2)) {
            throw new FriendshipAlreadyExistsException(userId1, userId2);
        }

        user1.addFriend(user2, repo);
        user2.addFriend(user1, repo);
    }

    /**
     * Returneaza toate prieteniile
     * @return List<Friendship>, lista de prietenii
     */
    public List<Friendship> getAllFriendships() {
        return repo.getAllFriendships();
    }

    /**
     * Elimina o prietenie intre doi utilizatori
     * @param userId1 Long, id-ul primului utilizator
     * @param userId2 Long, id-ul celui de-al doilea utilizator
     * @throws FriendshipNotFoundException daca prietenia nu exista
     */
    public void removeFriendship(Long userId1, Long userId2) {
        validator.validate(userId1, userId2);

        User user1 = repo.findById(userId1);
        User user2 = repo.findById(userId2);

        if (!user1.getFriends().contains(user2)) {
            throw new FriendshipNotFoundException(userId1, userId2);
        }

        user1.removeFriend(user2, repo);
        user2.removeFriend(user1, repo);
    }

    /**
     * Returneaza numarul de comunitati din reteaua de prietenii
     * @return int, numarul de comunitati
     */
    public int numberOfCommunities() {
        Set<User> visited = new HashSet<>();
        int count = 0;

        for (User u : repo.getAllUsers()) {
            if (!visited.contains(u)) {
                dfs(u, visited);
                count++;
            }
        }

        return count;
    }

    /**
     * Parcurgere in adancime pentru a marca utilizatorii vizitati
     * @param u User, utilizatorul curent
     * @param visited Set<User>, setul de utilizatori vizitati
     */
    private void dfs(User u, Set<User> visited) {
        visited.add(u);
        for (User friend : u.getFriends()) {
            if (!visited.contains(friend)) {
                dfs(friend, visited);
            }
        }
    }

    /**
     * Returneaza cea mai sociabila comunitate din reteaua de prietenii
     * @return List<User>, lista de utilizatori din cea mai sociabila comunitate
     */
    public List<User> mostSociableCommunity() {
        Set<User> visited = new HashSet<>();
        List<User> largestCommunity = new ArrayList<>();

        for (User u : repo.getAllUsers()) {
            if (!visited.contains(u)) {
                List<User> currentCommunity = new ArrayList<>();
                dfsCollect(u, visited, currentCommunity);
                if (currentCommunity.size() > largestCommunity.size()) {
                    largestCommunity = currentCommunity;
                }
            }
        }

        return largestCommunity;
    }

    /**
     * Parcurgere in adancime pentru a colecta utilizatorii dintr-o comunitate
     * @param u User, utilizatorul curent
     * @param visited Set<User>, setul de utilizatori vizitati
     * @param community List<User>, lista de utilizatori din comunitate
     */
    private void dfsCollect(User u, Set<User> visited, List<User> community) {
        visited.add(u);
        community.add(u);
        for (User friend : u.getFriends()) {
            if (!visited.contains(friend)) {
                dfsCollect(friend, visited, community);
            }
        }
    }
}
