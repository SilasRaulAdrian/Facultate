package org.example.domain;

import java.time.LocalDateTime;

public class Nevoie extends Entity {
    private String titlu;
    private String descriere;
    private LocalDateTime deadline;

    private long omInNevoie;          // id Persoana care a postat
    private Long omSalvator;          // null initial
    private StatusNevoie status;      // CAUT_EROU initial

    public Nevoie(long id, String titlu, String descriere, LocalDateTime deadline,
                  long omInNevoie, Long omSalvator, StatusNevoie status) {
        super(id);
        this.titlu = titlu;
        this.descriere = descriere;
        this.deadline = deadline;
        this.omInNevoie = omInNevoie;
        this.omSalvator = omSalvator;
        this.status = status;
    }

    public String getTitlu() { return titlu; }
    public String getDescriere() { return descriere; }
    public LocalDateTime getDeadline() { return deadline; }
    public long getOmInNevoie() { return omInNevoie; }
    public Long getOmSalvator() { return omSalvator; }
    public void setOmSalvator(Long omSalvator) { this.omSalvator = omSalvator; }
    public StatusNevoie getStatus() { return status; }
    public void setStatus(StatusNevoie status) { this.status = status; }
}
