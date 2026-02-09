package org.example.model;

import java.time.LocalDate;

/**
 * Clasa Person care extinde clasa User
 */
public class Person extends User {
    private String nume;
    private String prenume;
    private LocalDate dataNasterii;
    private String ocupatie;
    private double nivelEmpatie;

    /**
     * Constructor pentru clasa Person
     * @param id Long, id-ul persoanei
     * @param username String, username-ul persoanei
     * @param email String, email-ul persoanei
     * @param password String, parola persoanei
     * @param nume String, numele persoanei
     * @param prenume String, prenumele persoanei
     * @param dataNasterii LocalDate, data nasterii persoanei
     * @param ocupatie String, ocupatia persoanei
     * @param nivelEmpatie double, nivelul de empatie al persoanei
     */
    public Person(Long id, String username, String email, String password,
                  String nume, String prenume, LocalDate dataNasterii, String ocupatie, double nivelEmpatie) {
        super(id, username, email, password);
        this.nume = nume;
        this.prenume = prenume;
        this.dataNasterii = dataNasterii;
        this.ocupatie = ocupatie;
        this.nivelEmpatie = nivelEmpatie;
    }

    /**
     * Getter pentru numele persoanei
     * @return String, numele persoanei
     */
    public String getNume() {
        return nume;
    }

    /**
     * Getter pentru prenumele persoanei
     * @return String, prenumele persoanei
     */
    public String getPrenume() {
        return prenume;
    }

    /**
     * Getter pentru data nasterii persoanei
     * @return LocalDate, data nasterii persoanei
     */
    public LocalDate getDataNasterii() {
        return dataNasterii;
    }

    /**
     * Getter pentru ocupatia persoanei
     * @return String, ocupatia persoanei
     */
    public String getOcupatie() {
        return ocupatie;
    }

    /**
     * Getter pentru nivelul de empatie al persoanei
     * @return double, nivelul de empatie al persoanei
     */
    public double getNivelEmpatie() {
        return nivelEmpatie;
    }

    /**
     * Suprascrierea metodei toString pentru clasa Person
     * @return String, reprezentarea persoanei ca String
     */
    @Override
    public String toString() {
        return "Person: " + nume + " " + prenume + " (" + username + ")";
    }
}
