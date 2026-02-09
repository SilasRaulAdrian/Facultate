package org.example.domain;

public class Client {
    private final String username;
    private final String name;

    public Client(String username, String name) {
        this.username = username;
        this.name = name;
    }

    public String getUsername() { return username; }
    public String getName() { return name; }
}
