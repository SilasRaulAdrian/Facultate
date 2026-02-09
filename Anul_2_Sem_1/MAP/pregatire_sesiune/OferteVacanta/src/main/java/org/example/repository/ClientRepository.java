package org.example.repository;

import org.example.domain.Client;
import org.example.domain.Hobby;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements Repository<Long, Client> {
    private final Db db;

    public ClientRepository(Db db) {
        this.db = db;
    }

    @Override
    public Optional<Client> findOne(Long id) {
        String sql = "SELECT * FROM clients WHERE client_id=?";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new Client(
                        rs.getLong("client_id"),
                        rs.getString("name"),
                        rs.getInt("fidelity_grade"),
                        rs.getInt("age"),
                        Hobby.valueOf(rs.getString("hobby"))
                ));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Client> findAll() {
        String sql = "SELECT * FROM clients ORDER BY client_id";
        List<Client> out = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new Client(
                    rs.getLong("client_id"),
                    rs.getString("name"),
                    rs.getInt("fidelity_grade"),
                    rs.getInt("age"),
                    Hobby.valueOf(rs.getString("hobby"))
            ));
            return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override public Client save(Client entity) { throw new UnsupportedOperationException(); }
    @Override public Client update(Client entity) { throw new UnsupportedOperationException(); }
    @Override public Optional<Client> delete(Long id) { throw new UnsupportedOperationException(); }
}
