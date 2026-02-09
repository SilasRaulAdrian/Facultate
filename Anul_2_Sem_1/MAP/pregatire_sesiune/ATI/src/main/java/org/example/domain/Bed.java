package org.example.domain;

public class Bed {
    private final int id;
    private final BedType type;
    private final boolean ventilation;
    private final String patientCnp; // null = liber

    public Bed(int id, BedType type, boolean ventilation, String patientCnp) {
        this.id = id;
        this.type = type;
        this.ventilation = ventilation;
        this.patientCnp = patientCnp;
    }

    public int getId() { return id; }
    public BedType getType() { return type; }
    public boolean isVentilation() { return ventilation; }
    public String getPatientCnp() { return patientCnp; }

    public boolean isFree() { return patientCnp == null; }
}
