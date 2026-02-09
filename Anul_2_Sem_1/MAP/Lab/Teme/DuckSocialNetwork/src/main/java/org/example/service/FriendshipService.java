package org.example.service;

import org.example.exceptions.FriendshipAlreadyExistsException;
import org.example.exceptions.FriendshipNotFoundException;
import org.example.model.Friendship;
import org.example.model.User;
import org.example.model.paging.Page;
import org.example.model.paging.Pageable;
import org.example.observer.Observable;
import org.example.observer.Signal;
import org.example.repository.FriendshipRepository;
import org.example.repository.UserRepository;
import org.example.validation.FriendshipValidationStrategy;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Clasa pentru service-ul de prietenii
 */
public class FriendshipService implements Observable {
    private final UserRepository userRepo;
    private final FriendshipRepository friendshipRepo;
    private final FriendshipValidationStrategy validator = new FriendshipValidationStrategy();
    private final NotificationService notificationService;

    /**
     * Constructor pentru clasa FriendshipService
     * @param userRepo UserRepository, repository-ul pentru utilizatori
     * @param friendshipRepo FriendshipRepository, repository-ul pentru prietenii
     * @param notificationService NotificationService, service-ul pentru notificari
     */
    public FriendshipService(UserRepository userRepo, FriendshipRepository friendshipRepo, NotificationService notificationService) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
        this.notificationService = notificationService;

        loadFriendshipsFromDatabase();
    }

    /**
     * Incarca prieteniile din baza de date
     */
    public void loadFriendshipsFromDatabase() {
        for (Friendship friendship : friendshipRepo.getAll()) {
            User u1 = friendship.getU1();
            User u2 = friendship.getU2();

            if (u1 == null || u2 == null) {
                System.out.println("Warning: Friendship contains invalid user IDs: " + u1 + ", " + u2);
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
        notifyObservers(new Signal("FRIENDSHIP_ADDED"));

        user1.addFriendWithoutSave(user2);
        user2.addFriendWithoutSave(user1);

        notifyObservers(new Signal(
                "FRIEND_CHANGED",
                user1,
                List.of(user2)
        ));

        notifyObservers(new Signal(
                "FRIEND_CHANGED",
                user2,
                List.of(user1)
        ));
    }

    /**
     * Modifica o prietenie intre doi utilizatori
     * @param friendshipId Long, id-ul prieteniei de modificat
     * @param newUserId1 Long, id-ul primului utilizator nou
     * @param newUserId2 Long, id-ul celui de-al doilea utilizator nou
     */
    public void updateFriendship(Long friendshipId, Long newUserId1, Long newUserId2) {
        Friendship f = friendshipRepo.findById(friendshipId)
                .orElseThrow(() -> new FriendshipNotFoundException(friendshipId));

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
                .orElseThrow(() -> new FriendshipNotFoundException(id));
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
     * Elimina o prietenie dupa id
     * @param friendshipId Long, id-ul prieteniei de eliminat
     * @param actorUserId Long, id-ul utilizatorului care a initiat stergerea
     * @throws FriendshipNotFoundException daca prietenia nu exista
     */
    public void removeFriendship(Long friendshipId, Long actorUserId) {
        Friendship friendship = friendshipRepo.findById(friendshipId)
                .orElseThrow(() -> new FriendshipNotFoundException(friendshipId));

        User u1 = friendship.getU1();
        User u2 = friendship.getU2();

        u1.removeFriendWithoutSave(u2);
        u2.removeFriendWithoutSave(u1);

        friendshipRepo.delete(friendshipId);

        User notifiedUser;
        User actorUser;

        if (u1.getId().equals(actorUserId)) {
            actorUser = u1;
            notifiedUser = u2;
        } else {
            actorUser = u2;
            notifiedUser = u1;
        }

        notificationService.notifyUser(
                notifiedUser.getId(),
                actorUser.getUsername() + " a sters prietenia cu tine."
        );

        notifyObservers(new Signal("FRIENDSHIP_REMOVED"));

        notifyObservers(new Signal(
                "FRIEND_CHANGED",
                u1,
                List.of(u2)
        ));

        notifyObservers(new Signal(
                "FRIEND_CHANGED",
                u2,
                List.of(u1)
        ));
    }

    /**
     * Elimina o prietenie dupa id (de catre admin)
     * @param friendshipId Long, id-ul prieteniei de eliminat
     * @throws FriendshipNotFoundException daca prietenia nu exista
     */
    public void removeFriendshipByAdmin(Long friendshipId) {
        Friendship friendship = friendshipRepo.findById(friendshipId)
                .orElseThrow(() -> new FriendshipNotFoundException(friendshipId));

        User u1 = friendship.getU1();
        User u2 = friendship.getU2();

        u1.removeFriendWithoutSave(u2);
        u2.removeFriendWithoutSave(u1);

        friendshipRepo.delete(friendshipId);

        notifyObservers(new Signal("FRIENDSHIP_REMOVED"));
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
                List<User> community = new ArrayList<>();
                dfsCollect(u, visited, community);
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

    /**
     * Returneaza o pagina de prietenii
     * @param pageable Pageable, obiectul care contine informatii despre pagina
     * @return Page<Friendship>, pagina de prietenii
     */
    public Page<Friendship> findAllOnPage(Pageable pageable) {
        return friendshipRepo.findAllOnPage(pageable);
    }

    /**
     * Returneaza lista de prieteni ai unui utilizator
     * @param userId Long, id-ul utilizatorului
     * @return List<User>, lista de prieteni ai utilizatorului
     */
    public List<User> getFriendsOf(Long userId) {
        List<Friendship> friendships = new ArrayList<>();
        friendshipRepo.getAll().forEach(friendships::add);

        return friendships.stream()
                .filter(f -> f.getU1().getId().equals(userId) || f.getU2().getId().equals(userId))
                .map(f -> f.getU1().getId().equals(userId) ? f.getU2() : f.getU1())
                .collect(Collectors.toList());
    }

    /**
     * Gaseste id-ul prieteniei dintre doi utilizatori
     * @param idUser1 Long, id-ul primului utilizator
     * @param idUser2 Long, id-ul celui de-al doilea utilizator
     * @return Long, id-ul prieteniei
     * @throws FriendshipNotFoundException daca prietenia nu exista
     */
    public Long findFriendshipId(Long idUser1, Long idUser2) {
        Friendship friendship = friendshipRepo.findFriendshipBetween(idUser1, idUser2)
                .orElseThrow(() -> new FriendshipNotFoundException(idUser1, idUser2));

        return friendship.getId();
    }

    /**
     * Returneaza o pagina de prieteni ai utilizatorului dat
     *
     * @param userId   Long, id-ul utilizatorului
     * @param pageable Pageable, obiectul care contine informatii despre pagina
     * @return Page<User>, pagina de prieteni ai utilizatorului
     */
    public Page<User> getFriendsPage(Long userId, Pageable pageable) {
        return friendshipRepo.findFriendsPage(userId, pageable);
    }
}