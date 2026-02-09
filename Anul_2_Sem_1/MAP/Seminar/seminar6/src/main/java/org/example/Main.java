package org.example;

import org.example.model.User;
import org.example.repos.Repository;
import org.example.repos.UserRepository;

import java.time.LocalDateTime;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        var url = "jdbc:postgresql://localhost:5432/map_seminar";
        var username = "postgres";
        var password = "root";

        Repository<Integer, User> userRepository = new UserRepository(url, username, password);

        User user1 = new User("lab6", LocalDateTime.now().minusDays(4), 4);
        //userRepository.save(user1);
        user1.setId(1);

       //System.out.println(userRepository.update(user1));
        System.out.println(userRepository.delete(1));

        userRepository.getAll().forEach(System.out::println);
    }
}