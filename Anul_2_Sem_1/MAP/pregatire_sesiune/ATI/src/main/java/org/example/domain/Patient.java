package org.example.domain;

public class Patient {
    private final String cnp;
    private final int ageMonths;
    private final boolean premature;
    private final String diagnosis;
    private final int severity;

    public Patient(String cnp, int ageMonths, boolean premature, String diagnosis, int severity) {
        this.cnp = cnp;
        this.ageMonths = ageMonths;
        this.premature = premature;
        this.diagnosis = diagnosis;
        this.severity = severity;
    }

    public String getCnp() { return cnp; }
    public int getAgeMonths() { return ageMonths; }
    public boolean isPremature() { return premature; }
    public String getDiagnosis() { return diagnosis; }
    public int getSeverity() { return severity; }
}
