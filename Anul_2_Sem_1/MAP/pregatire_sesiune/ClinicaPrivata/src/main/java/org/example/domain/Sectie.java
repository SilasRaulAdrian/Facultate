package org.example.domain;

public class Sectie {
    private final int id;
    private final String nume;
    private final int idSefDeSectie; // refera spre Medic.id
    private final double pretPerConsultatie;
    private final int durataMaximaConsultatieOre;

    public Sectie(int id, String nume, int idSefDeSectie, double pretPerConsultatie, int durataMaximaConsultatieOre) {
        this.id = id;
        this.nume = nume;
        this.idSefDeSectie = idSefDeSectie;
        this.pretPerConsultatie = pretPerConsultatie;
        this.durataMaximaConsultatieOre = durataMaximaConsultatieOre;
    }

    public int getId() { return id; }
    public String getNume() { return nume; }
    public int getIdSefDeSectie() { return idSefDeSectie; }
    public double getPretPerConsultatie() { return pretPerConsultatie; }
    public int getDurataMaximaConsultatieOre() { return durataMaximaConsultatieOre; }

    @Override public String toString() { return nume; }
}
