package org.example.repository;
import org.example.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

/**
 * Clasa pentru repository-ul in memorie
 */
public class InMemoryRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long nextId = 1L;
    private final String usersFile;
    private final String friendshipsFile;

    /**
     * Constructor pentru clasa InMemoryRepository
     * @param usersFile String, numele fisierului dedicat utilizatorilor
     * @param friendshipsFile String, numele fisierului dedicat prieteniilor
     */
    public InMemoryRepository(String usersFile, String friendshipsFile) {
        this.usersFile = usersFile;
        this.friendshipsFile = friendshipsFile;
        loadUsers();
    }

    /**
     * Adauga un utilizator in repository
     * @param user User, utilizatorul de adaugat
     */
    public void addUser(User user) {
        if (user.getId() == null) {
            user.setId(nextId++);
        } else if (user.getId() >= nextId) {
            nextId = user.getId() + 1;
        }
        users.put(user.getId(), user);
        saveUsers(new ArrayList<>(users.values()));
    }

    /**
     * Elimina un utilizator din repository dupa id
     * @param id Long, id-ul utilizatorului de eliminat
     */
    public void removeUser(Long id) {
        users.remove(id);
        saveUsers(getAllUsers());
    }

    /**
     * Returneaza toti utilizatorii din repository
     * @return ArrayList<>(), lista de utilizatori
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * Returneaza toate prieteniile din repository
     * @return List<Friendship>, lista de prietenii
     */
    public List<Friendship> getAllFriendships() {
        List<Friendship> friendships = new ArrayList<>();
        for (User user : users.values()) {
            for (User friend : user.getFriends()) {
                if (user.getId() < friend.getId()) {
                    friendships.add(new Friendship(user, friend));
                }
            }
        }
        return friendships;
    }

    /**
     * Gaseste un utilizator dupa id
     * @param id Long, id-ul utilizatorului de gasit
     * @return User, utilizatorul gasit
     */
    public User findById(Long id) {
        return users.get(id);
    }

    /**
     * Genereaza urmatorul id unic pentru un utilizator
     * @return Long, urmatorul id unic
     */
    public Long nextId() {
        Long candidateId = 1L;

        while (users.containsKey(candidateId)) {
            candidateId++;
        }

        if (candidateId >= nextId) {
            nextId = candidateId + 1;
        }

        return candidateId;
    }

    /**
     * Incarca utilizatorii din fisier
     * @return List<User>, lista de utilizatori incarcati
     */
    public List<User> loadUsers() {
        List<User> loadedUsers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(usersFile))) {
            br.readLine(); // header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);

                Long id = Long.parseLong(parts[0]);
                String type = parts[1];
                String username = parts[2];
                String email = parts[3];
                String password = parts[4];

                if (type.equalsIgnoreCase("PERSON")) {
                    String nume = parts[5];
                    String prenume = parts[6];
                    String dataNasterii = parts[7];
                    String ocupatie = parts[8];
                    double nivelEmpatie = Double.parseDouble(parts[9]);

                    loadedUsers.add(new Person(id, username, email, password,
                            nume, prenume, java.time.LocalDate.parse(dataNasterii), ocupatie, nivelEmpatie));

                } else if (type.equalsIgnoreCase("DUCK")) {
                    TipRata tip = TipRata.valueOf(parts[10].trim());
                    double viteza = Double.parseDouble(parts[11]);
                    double rezistenta = Double.parseDouble(parts[12]);

                    if (tip == TipRata.SWIMMING) {
                        loadedUsers.add(new SwimmingDuck(id, username, email, password, viteza, rezistenta));
                    } else if (tip == TipRata.FLYING) {
                        loadedUsers.add(new FlyingDuck(id, username, email, password, viteza, rezistenta));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading users: " + e.getMessage());
        }

        for (User u : loadedUsers) {
            users.put(u.getId(), u);

            if (u.getId() >= nextId) {
                nextId = u.getId() + 1;
            }
        }

        return new ArrayList<>(users.values());
    }


    /**
     * Salveaza utilizatorii in fisier
     * @param users List<User>, lista de utilizatori de salvat
     */
    public void saveUsers(List<User> users) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(usersFile))) {
            pw.println("ID,TYPE,USERNAME,EMAIL,PASSWORD,NUME,PRENUME,DATA_NASTERII,OCUPATIE,NIVEL_EMPATIE,TIP_RATA,VITEZA,REZISTENTA");
            for (User u : users) {
                if (u instanceof Person p) {
                    pw.printf("%d,PERSON,%s,%s,%s,%s,%s,%s,%s,%.2f,,,,%n",
                            p.getId(), p.getUsername(), p.getEmail(), p.getPassword(),
                            p.getNume(), p.getPrenume(), p.getDataNasterii(), p.getOcupatie(), p.getNivelEmpatie());
                } else if (u instanceof Duck d) {
                    pw.printf("%d,DUCK,%s,%s,%s,,,,,,%s,%.2f,%.2f%n",
                            d.getId(), d.getUsername(), d.getEmail(), d.getPassword(),
                            d.getTip(), d.getViteza(), d.getRezistenta());
                }
            }
        } catch (Exception e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    /**
     * Incarca prieteniile din fisier
     * @return List<long[]>, lista de perechi de ID-uri de prieteni
     */
    public List<long[]> loadFriendships() {
        List<long[]> friendships = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(friendshipsFile))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                long userId1 = Long.parseLong(parts[0]);
                long userId2 = Long.parseLong(parts[1]);
                friendships.add(new long[]{userId1, userId2});
            }
        } catch (Exception e) {
            System.out.println("Error loading friendships: " + e.getMessage());
        }
        return friendships;
    }

    /**
     * Salveaza prieteniile in fisier
     * @param users Collection<User>, colectia de utilizatori cu prietenii
     */
    public void saveFriendships(Collection<User> users) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(friendshipsFile))) {
            pw.println("ID1,ID2");
            for (User u : users) {
                for (User f : u.getFriends()) {
                    if (u.getId() < f.getId()) {
                        pw.printf("%d,%d%n", u.getId(), f.getId());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error saving friendships: " + e.getMessage());
        }
    }
}
