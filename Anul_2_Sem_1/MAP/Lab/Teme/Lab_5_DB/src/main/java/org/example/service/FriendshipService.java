package org.example.service;

import org.example.exceptions.FriendshipAlreadyExistsException;
import org.example.exceptions.FriendshipNotFoundException;
import org.example.model.Friendship;
import org.example.model.User;
import org.example.repository.FriendshipRepository;
import org.example.repository.UserRepository;
import org.example.validation.FriendshipValidationStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Clasa pentru service-ul de prietenii
 */
public class FriendshipService {
    private final UserRepository userRepo;
    private final FriendshipRepository friendshipRepo;
    private final FriendshipValidationStrategy validator = new FriendshipValidationStrategy();

    /**
     * Constructor pentru clasa FriendshipService
     * @param userRepo UserRepository, repository-ul pentru utilizatori
     * @param friendshipRepo FriendshipRepository, repository-ul pentru prietenii
     */
    public FriendshipService(UserRepository userRepo, FriendshipRepository friendshipRepo) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;

        loadFriendshipsFromDatabase();
    }

    /**
     * Incarca prieteniile din baza de date
     */
    public void loadFriendshipsFromDatabase() {
        for (Friendship friendship : friendshipRepo.getAll()) {
            Long id1 = friendship.getU1().getId();
            Long id2 = friendship.getU2().getId();

            User u1 = userRepo.findById(id1).orElse(null);
            User u2 = userRepo.findById(id2).orElse(null);

            if (u1 == null || u2 == null) {
                System.out.println("Warning: Friendship contains invalid user IDs: " + id1 + ", " + id2);
                continue;
            }

            u1.addFriendWithoutSave(u2);
            u2.addFriendWithoutSave(u1);
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

        User user1 = userRepo.findById(userId1)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId1));
        User user2 = userRepo.findById(userId2)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId2));

        if (user1.getFriends().contains(user2)) {
            throw new FriendshipAlreadyExistsException(userId1, userId2);
        }

        Friendship friendship = new Friendship(null, user1, user2);
        friendshipRepo.save(friendship);

        user1.addFriendWithoutSave(user2);
        user2.addFriendWithoutSave(user1);
    }

    /**
     * Modifica o prietenie intre doi utilizatori
     * @param friendshipId Long, id-ul prieteniei de modificat
     * @param newUserId1 Long, id-ul primului utilizator nou
     * @param newUserId2 Long, id-ul celui de-al doilea utilizator nou
     */
    public void updateFriendship(Long friendshipId, Long newUserId1, Long newUserId2) {
        Friendship f = friendshipRepo.findById(friendshipId)
                .orElseThrow(() -> new FriendshipNotFoundException(friendshipId, null));

        User oldU1 = f.getU1();
        User oldU2 = f.getU2();

        User newU1 = userRepo.findById(newUserId1)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + newUserId1));
        User newU2 = userRepo.findById(newUserId2)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + newUserId2));

        oldU1.removeFriendWithoutSave(oldU2);
        oldU2.removeFriendWithoutSave(oldU1);

        f.setU1(newU1);
        f.setU2(newU2);

        friendshipRepo.update(f);

        newU1.addFriendWithoutSave(newU2);
        newU2.addFriendWithoutSave(newU1);
    }

    /**
     * Cauta o prietenie dupa id
     * @param id Long, id-ul prieteniei cautate
     * @return Friendship, prietenia cautata
     * @throws FriendshipNotFoundException daca prietenia nu exista
     */
    public Friendship findFriendshipById(Long id) {
        return friendshipRepo.findById(id)
                .orElseThrow(() -> new FriendshipNotFoundException(id, null));
    }

    /**
     * Returneaza toate prieteniile
     * @return List<Friendship>, lista de prietenii
     */
    public List<Friendship> getAllFriendships() {
        List<Friendship> list = new ArrayList<>();
        for (Friendship f : friendshipRepo.getAll()) {
            list.add(f);
        }
        return list;
    }

    /**
     * Elimina o prietenie intre doi utilizatori
     * @param id Long, id-ul prieteniei de eliminat
     * @throws FriendshipNotFoundException daca prietenia nu exista
     */
    public void removeFriendship(Long id) {
        Friendship f = friendshipRepo.findById(id)
                .orElseThrow(() -> new FriendshipNotFoundException(id, null));

        User u1 = f.getU1();
        User u2 = f.getU2();

        u1.removeFriendWithoutSave(u2);
        u2.removeFriendWithoutSave(u1);

        friendshipRepo.delete(id);
    }

    /**
     * Returneaza numarul de comunitati din reteaua de prietenii
     * @return int, numarul de comunitati
     */
    public int numberOfCommunities() {
        Set<User> visited = new HashSet<>();
        int count = 0;

        for (User u : userRepo.getAll()) {
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

        for (User u : userRepo.getAll()) {
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