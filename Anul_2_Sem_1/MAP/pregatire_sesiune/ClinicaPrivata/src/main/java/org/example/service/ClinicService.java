package org.example.service;

import org.example.domain.Consultatie;
import org.example.domain.Medic;
import org.example.domain.Sectie;
import org.example.observer.ClinicEvent;
import org.example.observer.ClinicEventType;
import org.example.observer.Observable;
import org.example.observer.Observer;
import org.example.repository.ConsultatieRepository;
import org.example.repository.MedicRepository;
import org.example.repository.SectieRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ClinicService implements Observable<ClinicEvent> {
    private final SectieRepository sectieRepo;
    private final MedicRepository medicRepo;
    private final ConsultatieRepository consultRepo;

    private final List<Observer<ClinicEvent>> observers = new ArrayList<>();

    public ClinicService(SectieRepository sectieRepo, MedicRepository medicRepo, ConsultatieRepository consultRepo) {
        this.sectieRepo = sectieRepo;
        this.medicRepo = medicRepo;
        this.consultRepo = consultRepo;
    }

    public List<Sectie> getSectii() {
        return sectieRepo.findAll();
    }

    public List<Medic> getMedici() {
        return medicRepo.findAll();
    }

    public List<Medic> getMediciBySectie(int idSectie) {
        return medicRepo.findBySectie(idSectie);
    }

    public List<Consultatie> getFutureConsultatiiForMedic(int medicId) {
        return consultRepo.findFutureForMedic(medicId);
    }

    public Consultatie programeazaConsultatie(
            int sectieId,
            int medicId,
            String cnpPacient,
            String numePacient,
            LocalDate data,
            LocalTime ora
    ) {
        if (cnpPacient == null || cnpPacient.trim().isEmpty())
            throw new IllegalArgumentException("CNP pacient obligatoriu.");
        if (numePacient == null || numePacient.trim().isEmpty())
            throw new IllegalArgumentException("Nume pacient obligatoriu.");
        if (data == null) throw new IllegalArgumentException("Data obligatorie.");
        if (ora == null) throw new IllegalArgumentException("Ora obligatorie.");

        Sectie sectie = sectieRepo.findOne(sectieId)
                .orElseThrow(() -> new IllegalArgumentException("Sectie inexistenta."));
        Medic medic = medicRepo.findOne(medicId)
                .orElseThrow(() -> new IllegalArgumentException("Medic inexistent."));

        if (medic.getIdSectie() != sectieId) {
            throw new IllegalArgumentException("Medicul selectat nu apartine sectiei.");
        }

        int dur = sectie.getDurataMaximaConsultatieOre();
        LocalTime start = ora;
        LocalTime endExclusive = ora.plusHours(1);
        LocalTime windowStart = start.minusHours(dur);

        if (windowStart.isAfter(start)) windowStart = LocalTime.MIDNIGHT;

        boolean overlap = consultRepo.existsOverlapInSectie(sectieId, data, windowStart, endExclusive);
        if (overlap) {
            throw new IllegalArgumentException("Exista deja o consultatie pe sectie care se suprapune.");
        }

        Consultatie saved = consultRepo.save(new Consultatie(
                0L, medicId, cnpPacient.trim(), numePacient.trim(), data, ora
        ));

        notifyObservers(new ClinicEvent(ClinicEventType.CONSULTATII_CHANGED, null));
        return saved;
    }

    @Override
    public void addObserver(Observer<ClinicEvent> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<ClinicEvent> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(ClinicEvent e) {
        observers.forEach(o -> o.update(e));
    }
}
