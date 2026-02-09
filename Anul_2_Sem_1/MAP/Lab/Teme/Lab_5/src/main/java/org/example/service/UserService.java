package org.example.service;

import org.example.exceptions.DuplicateUserException;
import org.example.exceptions.UserNotFoundException;
import org.example.model.Duck;
import org.example.model.Person;
import org.example.model.TipRata;
import org.example.model.User;
import org.example.repository.InMemoryRepository;
import org.example.validation.DuckValidationStrategy;
import org.example.validation.PersonValidationStrategy;
import org.example.validation.UserIdValidationStrategy;
import org.example.validation.ValidationStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clasa pentru service-ul de utilizatori
 */
public class UserService {
    private final InMemoryRepository repo;

    /**
     * Constructor pentru clasa UserService
     * @param repo InMemoryRepository, repository-ul in memorie
     */
    public UserService(InMemoryRepository repo) {
        this.repo = repo;
        repo.getAllUsers();
    }

    /**
     * Returneaza strategia de validare corespunzatoare tipului de utilizator
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
     * @param username String, username-ul persoanei
     * @param email String, email-ul persoanei
     * @param password String, parola persoanei
     * @param nume String, numele persoanei
     * @param prenume String, prenumele persoanei
     * @param dataNasterii LocalDate, data nasterii persoanei
     * @param ocupatie String, ocupatia persoanei
     * @param nivelEmpatie double, nivelul de empatie al persoanei
     * @throws DuplicateUserException daca username-ul exista deja in sistem
     */
    public void addPerson(String username, String email, String password,
                          String nume, String prenume, LocalDate dataNasterii,
                          String ocupatie, double nivelEmpatie) {
        if (repo.getAllUsers().stream().anyMatch(u -> u.getUsername().equals(username))) {
            throw new DuplicateUserException(username);
        }

        Person person = new Person(repo.nextId(), username, email, password, nume, prenume, dataNasterii, ocupatie, nivelEmpatie);

        getValidationStrategy(person).validate(person);
        repo.addUser(person);
    }

    /**
     * Adauga o rata in sistem
     * @param username String, username-ul ratei
     * @param email String, email-ul ratei
     * @param password String, parola ratei
     * @param tip TipRata, tipul ratei
     * @param viteza double, viteza ratei
     * @param rezistenta double, rezistenta ratei
     * @throws DuplicateUserException daca username-ul exista deja in sistem
     */
    public void addDuck(String username, String email, String password,
                        TipRata tip, double viteza, double rezistenta) {
        if (repo.getAllUsers().stream().anyMatch(u -> u.getUsername().equals(username))) {
            throw new DuplicateUserException(username);
        }

        String tipStr = tip.toString();
        if (tipStr.equalsIgnoreCase("SWIMMING")) {
            Duck duck = new org.example.model.SwimmingDuck(repo.nextId(), username, email, password, viteza, rezistenta);
            getValidationStrategy(duck).validate(duck);
            repo.addUser(duck);
        } else if (tipStr.equalsIgnoreCase("FLYING")) {
            Duck duck = new org.example.model.FlyingDuck(repo.nextId(), username, email, password, viteza, rezistenta);
            getValidationStrategy(duck).validate(duck);
            repo.addUser(duck);
        } else {
            throw new IllegalArgumentException("Unknown duck type: " + tipStr);
        }
    }

    /**
     * Elimina un utilizator din sistem
     * @param id Long, id-ul utilizatorului de eliminat
     * @throws UserNotFoundException daca utilizatorul cu id-ul dat nu exista
     */
    public void removeUser(Long id) {
        ValidationStrategy idValidator = new UserIdValidationStrategy();
        idValidator.validate(id);

        User userToRemove = repo.findById(id);
        if (userToRemove == null) {
            throw new UserNotFoundException(id);
        }

        for (User friend : new ArrayList<>(userToRemove.getFriends())) {
            friend.removeFriend(userToRemove, repo);
        }

        userToRemove.getFriends().clear();

        repo.removeUser(id);
    }

    /**
     * Cauta un utilizator dupa id
     * @param id Long, id-ul utilizatorului cautat
     * @return User, utilizatorul cautat
     * @throws UserNotFoundException daca utlizatorul nu a fost gasit
     */
    public User findById(Long id) {
        ValidationStrategy idValidator = new UserIdValidationStrategy();
        idValidator.validate(id);

        User user = repo.findById(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
    }

    /**
     * Returneaza toti utilizatorii din sistem
     * @return List<User>, lista de utilizatori
     */
    public List<User> getAllUsers() {
        return repo.getAllUsers();
    }
}
