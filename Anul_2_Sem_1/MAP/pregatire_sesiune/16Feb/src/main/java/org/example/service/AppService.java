package org.example.service;

import org.example.domain.Nevoie;
import org.example.domain.Oras;
import org.example.domain.Persoana;
import org.example.domain.StatusNevoie;
import org.example.observer.AppEvent;
import org.example.observer.AppEventType;
import org.example.observer.Observable;
import org.example.observer.Observer;
import org.example.repository.NevoieFileRepository;
import org.example.repository.PersoanaFileRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AppService implements Observable<AppEvent> {
    private final PersoanaFileRepository persoanaRepo;
    private final NevoieFileRepository nevoieRepo;

    private final List<Observer<AppEvent>> observers = new ArrayList<>();

    public AppService(PersoanaFileRepository persoanaRepo, NevoieFileRepository nevoieRepo) {
        this.persoanaRepo = persoanaRepo;
        this.nevoieRepo = nevoieRepo;
    }

    public List<String> getAllUsernames() {
        return persoanaRepo.findAllUsernames();
    }

    public Persoana loginByUsername(String username) {
        return persoanaRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User inexistent."));
    }

    public Persoana register(String nume, String prenume, String username, String parola, String parola2,
                             Oras oras, String strada, String numarStrada, String telefon) {
        if (nume.isBlank() || prenume.isBlank() || username.isBlank() || parola.isBlank()
                || strada.isBlank() || numarStrada.isBlank() || telefon.isBlank())
            throw new IllegalArgumentException("Completeaza toate campurile!");

        if (!parola.equals(parola2))
            throw new IllegalArgumentException("Parolele nu coincid!");

        if (persoanaRepo.findByUsername(username).isPresent())
            throw new IllegalArgumentException("Username deja folosit!");

        Persoana p = new Persoana(0, nume.trim(), prenume.trim(), username.trim(), parola, oras,
                strada.trim(), numarStrada.trim(), telefon.trim());

        Persoana saved = persoanaRepo.save(p);
        notifyObservers(new AppEvent(AppEventType.PERSOANA_ADDED, saved, null));
        return saved;
    }

    public List<Nevoie> getNevoiPentruOrasAjutor(Persoana user) {
        Oras orasUser = user.getOras();
        return nevoieRepo.findAll().stream()
                .filter(n -> n.getStatus() == StatusNevoie.CAUT_EROU)
                .filter(n -> n.getOmSalvator() == null)
                .filter(n -> n.getOmInNevoie() != user.getId())
                .filter(n -> persoanaRepo.findOne(n.getOmInNevoie())
                        .map(p -> p.getOras() == orasUser)
                        .orElse(false))
                .sorted(Comparator.comparing(Nevoie::getDeadline))
                .toList();
    }

    public List<Nevoie> getFapteBunePentruUser(Persoana user) {
        return nevoieRepo.findAll().stream()
                .filter(n -> n.getOmSalvator() != null && n.getOmSalvator() == user.getId())
                .sorted(Comparator.comparing(Nevoie::getDeadline))
                .toList();
    }

    public void preiaNevoie(long nevoieId, Persoana user) {
        Nevoie n = nevoieRepo.findOne(nevoieId).orElseThrow();

        if (n.getStatus() != StatusNevoie.CAUT_EROU || n.getOmSalvator() != null)
            throw new IllegalArgumentException("Nevoia nu mai este disponibila.");

        if (n.getOmInNevoie() == user.getId())
            throw new IllegalArgumentException("Nu poti prelua propria nevoie.");

        n.setOmSalvator(user.getId());
        n.setStatus(StatusNevoie.EROU_GASIT);
        nevoieRepo.update(n);

        notifyObservers(new AppEvent(AppEventType.NEVOIE_UPDATED, null, n));
    }

    public void adaugaNevoie(Persoana user, String titlu, String descriere, LocalDate deadlineDate) {
        if (titlu.isBlank() || descriere.isBlank() || deadlineDate == null)
            throw new IllegalArgumentException("Completeaza titlu, descriere si deadline!");

        LocalDateTime deadline = deadlineDate.atTime(23, 59);

        Nevoie n = new Nevoie(0, titlu.trim(), descriere.trim(), deadline,
                user.getId(), null, StatusNevoie.CAUT_EROU);

        Nevoie saved = nevoieRepo.save(n);
        notifyObservers(new AppEvent(AppEventType.NEVOIE_ADDED, null, saved));
    }

    @Override
    public void addObserver(Observer<AppEvent> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<AppEvent> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(AppEvent e) {
        observers.forEach(o -> o.update(e));
    }
}
