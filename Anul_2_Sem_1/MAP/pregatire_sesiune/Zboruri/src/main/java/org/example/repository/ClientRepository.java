package org.example.repository;

import org.example.domain.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements Repository<String, Client> {
    private final Db db;

    public ClientRepository(Db db) {
        this.db = db;
    }

    @Override
    public Optional<Client> findOne(String username) {
        String sql = "SELECT username, name FROM clients WHERE username=?";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new Client(rs.getString("username"), rs.getString("name")));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Client> findAll() {
        String sql = "SELECT username, name FROM clients ORDER BY username";
        List<Client> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new Client(rs.getString("username"), rs.getString("name")));
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Client save(Client entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Client update(Client entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Client> delete(String s) {
        throw new UnsupportedOperationException();
    }
}
