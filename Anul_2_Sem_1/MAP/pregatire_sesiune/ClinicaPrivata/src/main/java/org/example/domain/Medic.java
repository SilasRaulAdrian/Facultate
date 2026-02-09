package org.example.domain;

public class Medic {
    private final int id;
    private final int idSectie;
    private final String nume;
    private final int vechime;
    private final boolean rezident;

    public Medic(int id, int idSectie, String nume, int vechime, boolean rezident) {
        this.id = id;
        this.idSectie = idSectie;
        this.nume = nume;
        this.vechime = vechime;
        this.rezident = rezident;
    }

    public int getId() { return id; }
    public int getIdSectie() { return idSectie; }
    public String getNume() { return nume; }
    public int getVechime() { return vechime; }
    public boolean isRezident() { return rezident; }

    @Override public String toString() { return nume + (rezident ? " (rezident)" : ""); }
}
