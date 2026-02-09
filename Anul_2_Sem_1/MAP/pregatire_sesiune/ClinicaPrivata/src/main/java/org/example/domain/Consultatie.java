package org.example.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Consultatie {
    private final long id;
    private final int idMedic;
    private final String cnpPacient;
    private final String numePacient;
    private final LocalDate data;
    private final LocalTime ora;

    public Consultatie(long id, int idMedic, String cnpPacient, String numePacient, LocalDate data, LocalTime ora) {
        this.id = id;
        this.idMedic = idMedic;
        this.cnpPacient = cnpPacient;
        this.numePacient = numePacient;
        this.data = data;
        this.ora = ora;
    }

    public long getId() { return id; }
    public int getIdMedic() { return idMedic; }
    public String getCnpPacient() { return cnpPacient; }
    public String getNumePacient() { return numePacient; }
    public LocalDate getData() { return data; }
    public LocalTime getOra() { return ora; }
}
