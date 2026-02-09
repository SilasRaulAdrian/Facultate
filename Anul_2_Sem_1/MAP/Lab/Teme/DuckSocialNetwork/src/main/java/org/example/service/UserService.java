package org.example.service;

import org.example.exceptions.DuplicateUserException;
import org.example.exceptions.UserNotFoundException;
import org.example.hash.PasswordHasher;
import org.example.model.Duck;
import org.example.model.Person;
import org.example.model.TipRata;
import org.example.model.User;
import org.example.model.paging.Page;
import org.example.model.paging.Pageable;
import org.example.observer.Observable;
import org.example.observer.Signal;
import org.example.repository.FriendshipRepository;
import org.example.repository.UserRepository;
import org.example.validation.DuckValidationStrategy;
import org.example.validation.PersonValidationStrategy;
import org.example.validation.UserIdValidationStrategy;
import org.example.validation.ValidationStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Clasa pentru service-ul de utilizatori
 */
public class UserService implements Observable {
    private final UserRepository userRepo;
    private final FriendshipRepository friendshipRepo;

    /**
     * Constructor pentru clasa UserService
     *
     * @param userRepo       UserRepository, repository-ul pentru utilizatori
     * @param friendshipRepo FriendshipRepository, repository-ul pentru prietenii
     */
    public UserService(UserRepository userRepo, FriendshipRepository friendshipRepo) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
        userRepo.getAll();
    }

    /**
     * Returneaza strategia de validare corespunzatoare tipului de utilizator
     *
     * @param user User, utilizatorul pentru care se doreste validarea
     * @return ValidationStrategy, strategia de validare corespunzatoare
     * @throws IllegalArgumentException daca tipul utilizatorului este necunoscut
     */
    private ValidationStrategy getValidationStrategy(User user) {
        if (user instanceof Person) {
            return new PersonValidationStrategy();
        } else if (user instanceof Duck) {
            return new DuckValidationStrategy();
        }
        throw new IllegalArgumentException("Unknown user type");
    }

    /**
     * Adauga o persoana in sistem
     *
     * @param username     String, username-ul persoanei
     * @param email        String, email-ul persoanei
     * @param password     String, parola persoanei
     * @param nume         String, numele persoanei
     * @param prenume      String, prenumele persoanei
     * @param dataNasterii LocalDate, data nasterii persoanei
     * @param ocupatie     String, ocupatia persoanei
     * @param nivelEmpatie double, nivelul de empatie al persoanei
     * @throws DuplicateUserException daca username-ul exista deja in sistem
     */
    public void addPerson(String username, String email, String password,
                          String nume, String prenume, LocalDate dataNasterii,
                          String ocupatie, double nivelEmpatie) {
        List<User> users = (List<User>) userRepo.getAll();
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
            throw new DuplicateUserException(username);
        }

        Person person = new Person(null, username, email, password, nume, prenume, dataNasterii, ocupatie, nivelEmpatie);

        getValidationStrategy(person).validate(person);
        userRepo.save(person);
        notifyObservers(new Signal("USER_ADDED"));
    }

    /**
     * Adauga o rata in sistem
     *
     * @param username   String, username-ul ratei
     * @param email      String, email-ul ratei
     * @param password   String, parola ratei
     * @param tip        TipRata, tipul ratei
     * @param viteza     double, viteza ratei
     * @param rezistenta double, rezistenta ratei
     * @throws DuplicateUserException daca username-ul exista deja in sistem
     */
    public void addDuck(String username, String email, String password,
                        TipRata tip, double viteza, double rezistenta) {
        List<User> users = (List<User>) userRepo.getAll();
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
            throw new DuplicateUserException(username);
        }

        String tipStr = tip.toString();
        if (tipStr.equalsIgnoreCase("SWIMMING")) {
            Duck duck = new org.example.model.SwimmingDuck(null, username, email, password, viteza, rezistenta);
            getValidationStrategy(duck).validate(duck);
            userRepo.save(duck);
            notifyObservers(new Signal("USER_ADDED"));
        } else if (tipStr.equalsIgnoreCase("FLYING")) {
            Duck duck = new org.example.model.FlyingDuck(null, username, email, password, viteza, rezistenta);
            getValidationStrategy(duck).validate(duck);
            userRepo.save(duck);
            notifyObservers(new Signal("USER_ADDED"));
        } else if (tipStr.equalsIgnoreCase("FLYING_AND_SWIMMING")) {
            Duck duck = new org.example.model.FlyingAndSwimmingDuck(null, username, email, password, viteza, rezistenta);
            getValidationStrategy(duck).validate(duck);
            userRepo.save(duck);
            notifyObservers(new Signal("USER_ADDED"));
        } else {
            throw new IllegalArgumentException("Unknown duck type: " + tipStr);
        }
    }

    /**
     * Elimina un utilizator din sistem
     *
     * @param id Long, id-ul utilizatorului de eliminat
     * @throws UserNotFoundException daca utilizatorul cu id-ul dat nu exista
     */
    public void removeUser(Long id) {
        ValidationStrategy idValidator = new UserIdValidationStrategy();
        idValidator.validate(id);

        User userToRemove = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        for (User friend : new ArrayList<>(userToRemove.getFriends())) {
            friend.removeFriend(userToRemove, friendshipRepo);
        }

        userToRemove.getFriends().clear();

        userRepo.delete(id);
        notifyObservers(new Signal("USER_REMOVED"));
    }

    /**
     * Cauta un utilizator dupa id
     *
     * @param id Long, id-ul utilizatorului cautat
     * @return User, utilizatorul cautat
     * @throws UserNotFoundException daca utlizatorul nu a fost gasit
     */
    public User findById(Long id) {
        new UserIdValidationStrategy().validate(id);
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Modifica un utilizator existent
     *
     * @param id   Long, id-ul utilizatorului de modificat
     * @param user User, utilizatorul cu noile date
     * @throws UserNotFoundException daca utilizatorul cu id-ul dat nu exista
     */
    public void modifyUser(Long id, User user) {
        new UserIdValidationStrategy().validate(id);
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setId(existingUser.getId());
        getValidationStrategy(user).validate(user);
        userRepo.update(user);
    }

    /**
     * Returneaza toti utilizatorii din sistem
     *
     * @return List<User>, lista de utilizatori
     */
    public List<User> getAllUsers() {
        return (List<User>) userRepo.getAll();
    }

    /**
     * Returneaza toate ratele din sistem
     *
     * @return List<Duck>, lista de rate
     */
    public List<Duck> getDucks() {
        return userRepo.getDucksFromDB();
    }

    /**
     * Returneaza toate persoanele din sistem
     *
     * @return List<Person>, lista de persoane
     */
    public List<Person> getPersons() {
        return userRepo.getPersonsFromDB();
    }

    /**
     * Returneaza toate ratele de un anumit tip
     *
     * @param tip TipRata, tipul ratei cautate
     * @return List<Duck>, lista de rate de tipul dat
     */
    public List<Duck> getDucksByType(TipRata tip) {
        return userRepo.getDucksByTypeFromDB(tip);
    }

    /**
     * Returneaza o pagina de utilizatori
     *
     * @param pageable Pageable, obiectul care contine informatii despre pagina
     * @return Page<User>, pagina de utilizatori
     */
    public Page<User> findAllOnPage(Pageable pageable) {
        return userRepo.findAllOnPage(pageable);
    }

    /**
     * Returneaza o pagina de rate, optional filtrata dupa tip.
     *
     * @param pageable Pageable, obiectul care contine informatii despre pagina
     * @param tip TipRata, tipul ratei pentru filtrare (null pentru toate ratele)
     * @return Page<Duck>, pagina de rate.
     */
    public Page<Duck> findAllDucksOnPage(Pageable pageable, TipRata tip) {
        return userRepo.findAllDucksOnPage(pageable, tip);
    }

    /**
     * Returneaza o pagina de persoane.
     *
     * @param pageable Pageable, obiectul care contine informatii despre pagina
     * @return Page<Person>, pagina de persoane.
     */
    public Page<Person> findAllPersonsOnPage(Pageable pageable) {
        return userRepo.findAllPersonsOnPage(pageable);
    }

    /**
     * Returneaza un utilizator dupa username
     *
     * @param username String, username-ul utilizatorului cautat
     * @return User, utilizatorul cautat
     * @throws UserNotFoundException daca utilizatorul cu username-ul dat nu exista
     */
    public User findByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    /**
     * Migreaza toate parolele din baza de date la format hash-uit.
     */
    public void migratePasswordsToHash() {
        List<User> allUsers = getAllUsers();

        for (User user : allUsers) {
            String plainPassword = user.getPassword();

            if (plainPassword.length() != 64) {
                String hashedPassword = PasswordHasher.hashPassword(plainPassword);
                user.setPassword(hashedPassword);
                userRepo.update(user);
                System.out.println("Migrated password for user: " + user.getUsername());
            }
        }

        System.out.println("Password migration completed!");
    }

    /**
     * Returneaza o pagina de utilizatori care nu sunt prieteni cu utilizatorul dat
     *
     * @param userId   Long, id-ul utilizatorului
     * @param pageable Pageable, obiectul care contine informatii despre pagina
     * @return Page<User>, pagina de utilizatori care nu sunt prieteni
     */
    public Page<User> getUsersNotFriendsPage(Long userId, Pageable pageable) {
        return userRepo.findNotFriendsPage(userId, pageable);
    }

    /**
     * Returneaza un utilizator impreuna cu lista sa de prieteni
     *
     * @param userId Long, id-ul utilizatorului
     * @return User, utilizatorul cu lista sa de prieteni
     */
    public User getUserWithFriends(Long userId) {
        User user = userRepo.findById(userId).orElseThrow();

        StreamSupport.stream(friendshipRepo.getAll().spliterator(), false)
                .filter(f ->
                        f.getU1().getId().equals(userId) ||
                                f.getU2().getId().equals(userId)
                )
                .forEach(f -> {
                    User friend =
                            f.getU1().getId().equals(userId)
                                    ? f.getU2()
                                    : f.getU1();

                    user.addFriendWithoutSave(friend);
                });

        return user;
    }
}
