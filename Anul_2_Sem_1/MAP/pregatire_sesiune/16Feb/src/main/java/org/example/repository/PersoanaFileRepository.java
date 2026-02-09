package org.example.repository;

import org.example.domain.Oras;
import org.example.domain.Persoana;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class PersoanaFileRepository implements Repository<Long, Persoana> {
    private final Path path;
    private final Map<Long, Persoana> data = new HashMap<>();
    private long nextId = 1;

    public PersoanaFileRepository(String file) {
        this.path = Paths.get(file);
        load();
    }

    private void load() {
        data.clear();
        if (!Files.exists(path)) return;

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                // id,nume,prenume,username,parola,oras,strada,numarStrada,telefon
                String[] p = line.split(",", -1);
                long id = Long.parseLong(p[0]);
                Persoana pers = new Persoana(
                        id, p[1], p[2], p[3], p[4],
                        Oras.valueOf(p[5]), p[6], p[7], p[8]
                );
                data.put(id, pers);
                nextId = Math.max(nextId, id + 1);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void persist() {
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (Persoana p : data.values().stream().sorted(Comparator.comparingLong(Persoana::getId)).toList()) {
                bw.write(String.join(",",
                        String.valueOf(p.getId()),
                        p.getNume(),
                        p.getPrenume(),
                        p.getUsername(),
                        p.getParola(),
                        p.getOras().name(),
                        p.getStrada(),
                        p.getNumarStrada(),
                        p.getTelefon()
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Persoana> findByUsername(String username) {
        return data.values().stream().filter(p -> p.getUsername().equals(username)).findFirst();
    }

    public List<String> findAllUsernames() {
        return data.values().stream().map(Persoana::getUsername).sorted().collect(Collectors.toList());
    }

    @Override
    public Optional<Persoana> findOne(Long id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Persoana> findAll() {
        return data.values().stream().sorted(Comparator.comparingLong(Persoana::getId)).toList();
    }

    @Override
    public Persoana save(Persoana entity) {
        entity.setId(nextId++);
        data.put(entity.getId(), entity);
        persist();
        return entity;
    }

    @Override
    public Persoana update(Persoana entity) {
        data.put(entity.getId(), entity);
        persist();
        return entity;
    }

    @Override
    public Optional<Persoana> delete(Long id) {
        Persoana removed = data.remove(id);
        persist();
        return Optional.ofNullable(removed);
    }
}
