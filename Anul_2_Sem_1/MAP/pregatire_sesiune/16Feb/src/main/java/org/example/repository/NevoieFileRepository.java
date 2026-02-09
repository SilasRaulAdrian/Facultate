package org.example.repository;

import org.example.domain.Nevoie;
import org.example.domain.StatusNevoie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

public class NevoieFileRepository implements Repository<Long, Nevoie> {
    private final Path path;
    private final Map<Long, Nevoie> data = new HashMap<>();
    private long nextId = 1;

    public NevoieFileRepository(String file) {
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
                // id,titlu,descriere,deadline,omInNevoie,omSalvator,status
                String[] p = line.split(",", -1);
                long id = Long.parseLong(p[0]);
                String titlu = p[1];
                String descriere = p[2];
                LocalDateTime deadline = LocalDateTime.parse(p[3]);
                long omInNevoie = Long.parseLong(p[4]);
                Long omSalvator = p[5].isBlank() ? null : Long.parseLong(p[5]);
                StatusNevoie st = StatusNevoie.valueOf(p[6]);

                Nevoie n = new Nevoie(id, titlu, descriere, deadline, omInNevoie, omSalvator, st);
                data.put(id, n);
                nextId = Math.max(nextId, id + 1);
            }
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    private void persist() {
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (Nevoie n : data.values().stream().sorted(Comparator.comparingLong(Nevoie::getId)).toList()) {
                bw.write(String.join(",",
                        String.valueOf(n.getId()),
                        n.getTitlu(),
                        n.getDescriere().replace(",", " "),
                        n.getDeadline().toString(),
                        String.valueOf(n.getOmInNevoie()),
                        n.getOmSalvator() == null ? "" : String.valueOf(n.getOmSalvator()),
                        n.getStatus().name()
                ));
                bw.newLine();
            }
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    @Override public Optional<Nevoie> findOne(Long id) { return Optional.ofNullable(data.get(id)); }
    @Override public List<Nevoie> findAll() { return data.values().stream().sorted(Comparator.comparingLong(Nevoie::getId)).toList(); }

    @Override
    public Nevoie save(Nevoie entity) {
        entity.setId(nextId++);
        data.put(entity.getId(), entity);
        persist();
        return entity;
    }

    @Override
    public Nevoie update(Nevoie entity) {
        data.put(entity.getId(), entity);
        persist();
        return entity;
    }

    @Override
    public Optional<Nevoie> delete(Long id) {
        Nevoie removed = data.remove(id);
        persist();
        return Optional.ofNullable(removed);
    }
}
