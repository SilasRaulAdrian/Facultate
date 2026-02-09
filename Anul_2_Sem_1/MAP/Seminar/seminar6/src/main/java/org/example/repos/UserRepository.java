package org.example.repos;

import lombok.RequiredArgsConstructor;
import org.example.model.Entity;
import org.example.model.User;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class UserRepository implements Repository<Integer, User> {

    private final String url;
    private final String username;
    private final String password;

    @Override
    public Optional<User> findById(Integer id) {
        try (var conn = DriverManager.getConnection(url, username, password)) {

            var statement = conn.prepareStatement("SELECT * FROM users WHERE id=?");

            statement.setInt(1, id);
            var result = statement.executeQuery();

            if (result.next()) {
                String username = result.getString("username");
                LocalDateTime createdAt = result.getTimestamp("created_at").toLocalDateTime();
                int credits = result.getInt("credits");

                var user = new User(username, createdAt, credits);
                user.setId(id);

                return Optional.of(user);
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        @Override
    public Iterable<User> getAll() {
        try (var conn = DriverManager.getConnection(url, username, password)) {

            var statement = conn.prepareStatement("SELECT * FROM users");
            var result = statement.executeQuery();

            Set<User> users = new LinkedHashSet<>();
            while(result.next()) {
                int id = result.getInt("id");
                String username = result.getString("username");
                LocalDateTime createdAt = result.getTimestamp("created_at").toLocalDateTime();
                int credits = result.getInt("credits");

                var user = new User(username, createdAt, credits);
                user.setId(id);

                users.add(user);
            }

            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> save(User user) {
        try (var conn = DriverManager.getConnection(url, username, password)) {

          var statement = conn.prepareStatement("INSERT INTO users (username, created_at, credits) VALUES (?, ?, ?)");
          statement.setString(1,user.getUsername());
          statement.setTimestamp(2, Timestamp.valueOf(user.getCreatedAt()));
          statement.setInt(3, user.getCredits());

          var result = statement.executeUpdate();

          return result == 0 ? Optional.of(user) : Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> update(User user) {
        try (var conn = DriverManager.getConnection(url, username, password)) {

            var statement = conn.prepareStatement("UPDATE users SET username=?, created_at=?, credits=? WHERE id=?");

            statement.setString(1,user.getUsername());
            statement.setTimestamp(2, Timestamp.valueOf(user.getCreatedAt()));
            statement.setInt(3, user.getCredits());
            statement.setInt(4, user.getId());

            var result = statement.executeUpdate();

            return result == 0 ? Optional.of(user) : Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> delete(Integer id) {
        try (var conn = DriverManager.getConnection(url, username, password)) {

            var user = findById(id);
            if(user.isEmpty()) {
                return Optional.empty();
            }

            var statement = conn.prepareStatement("DELETE FROM users WHERE id=?");

            statement.setInt(1, id);

            var result = statement.executeUpdate();

            return result != 0 ? user : Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
