package org.example.repos;

import lombok.RequiredArgsConstructor;
import org.example.model.User;
import org.example.repos.dto.Page;
import org.example.repos.dto.Pageable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
public class UserDbRepo implements UserRepository {

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

          return result != 0 ? Optional.of(user) : Optional.empty();

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

            return result != 0 ? Optional.of(user) : Optional.empty();

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

    @Override
    public Page<User> findAllOnPage(Pageable pageable) {
        try (var conn = DriverManager.getConnection(url, username, password)) {

            var totalNumberOfUsers = count(conn);

            List<User> users = totalNumberOfUsers > 0 ? findAllOnPage(conn, pageable) : List.of();
            return new Page<>(users, totalNumberOfUsers);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<User> findAllOnPage(Connection connection, Pageable pageable) throws SQLException {
        var statement = connection.prepareStatement("SELECT * FROM users LIMIT ? OFFSET ?");
        statement.setInt(1, pageable.getPageSize());
        statement.setInt(2, pageable.getPageSize() * pageable.getPageNumber());

        var result = statement.executeQuery();

        List<User> users = new LinkedList<>();
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
    }

    private int count(Connection conn) throws SQLException {
        try (var statement = conn.prepareStatement("SELECT COUNT(*) AS count FROM users")) {

            var result = statement.executeQuery();

            return result.next() ? result.getInt("count") : 0;
        }
    }
}
