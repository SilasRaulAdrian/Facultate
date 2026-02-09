package org.example;

import org.example.config.Config;
import org.example.model.EntityChangedEvent;
import org.example.model.User;
import org.example.observer.Observer;
import org.example.repos.Repository;
import org.example.repos.UserDbRepo;
import org.example.repos.UserRepository;
import org.example.repos.dto.Pageable;
import org.example.service.UserService;
import org.example.validator.UserValidator;
import org.example.validator.ValidationException;
import org.example.validator.Validator;

import java.time.LocalDateTime;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() throws ValidationException {
        var config = Config.getProperties();

        UserRepository userRepository = new UserDbRepo(
                config.getProperty("db.url"),
                config.getProperty("db.username"),
                config.getProperty("db.password"));
        Validator<User> userValidator = new UserValidator();

        UserService userService = new UserService(userRepository, userValidator);
        userService.add(new PrintObserver());

        User user1 = new User("ceva", LocalDateTime.now(), 5);
        //userService.save(user1);
        userService.delete(10);
        //userService.getAll().forEach(System.out::println);

        userService.findAllOnPage(new Pageable(3, 2)).getEntities().forEach(System.out::println);

   }

   static class PrintObserver implements Observer<EntityChangedEvent<User>> {

       @Override
       public void update(EntityChangedEvent<User> userEntityChangedEvent) {
           System.out.println(userEntityChangedEvent + "---------------observer");
       }
   }
}