package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.EntityChangedEvent;
import org.example.model.User;
import org.example.observer.Observable;
import org.example.observer.Observer;
import org.example.repos.UserRepository;
import org.example.repos.dto.Page;
import org.example.repos.dto.Pageable;
import org.example.validator.UserValidator;
import org.example.validator.ValidationException;
import org.example.validator.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserService implements Observable<EntityChangedEvent<User>> {
    private final UserRepository userRepository;
    private final Validator<User> userValidator;
    private final List<Observer<EntityChangedEvent<User>>> observers = new ArrayList<>();

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public Iterable<User> getAll() {
        return userRepository.getAll();
    }

    public Optional<User> save(User e) throws ValidationException {
        userValidator.validate(e);
        var user = userRepository.save(e);
        user.ifPresent(u -> notify(new EntityChangedEvent<>(EntityChangedEvent.EventType.ADD, u)));

        return user;
    }

    public Optional<User> update(User e) throws ValidationException {
        userValidator.validate(e);
        var user = userRepository.update(e);
        user.ifPresent(u -> notify(new EntityChangedEvent<>(EntityChangedEvent.EventType.UPDATE, u)));
        return user;
    }

    public Optional<User> delete(Integer id) {
        var user = userRepository.delete(id);
        user.ifPresent(u -> notify(new EntityChangedEvent<>(EntityChangedEvent.EventType.DELETE, u)));
        return user;
    }

    public Page<User> findAllOnPage(Pageable pageable) {
        return userRepository.findAllOnPage(pageable);
    }

    @Override
    public void add(Observer<EntityChangedEvent<User>> e) {
        observers.add(e);
    }

    @Override
    public void remove(Observer<EntityChangedEvent<User>> e) {
        observers.remove(e);
    }

    @Override
    public void notify(EntityChangedEvent<User> userEntityChangedEvent) {
        observers.forEach(o -> o.update(userEntityChangedEvent));
    }
}
