package org.example.domain;

public class User {
    private final int id;
    private final String username;
    private final String password;
    private final UserRole role;
    private final String name;

    public User(int id, String username, String password, UserRole role, String name) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return username;
    }
}
