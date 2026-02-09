package org.example.domain;

public class OccupiedBedRow {
    private final int bedId;
    private final BedType type;
    private final boolean ventilation;
    private final String patientCnp;
    private final int severity;
    private final String diagnosis;

    public OccupiedBedRow(int bedId, BedType type, boolean ventilation,
                          String patientCnp, int severity, String diagnosis) {
        this.bedId = bedId;
        this.type = type;
        this.ventilation = ventilation;
        this.patientCnp = patientCnp;
        this.severity = severity;
        this.diagnosis = diagnosis;
    }

    public int getBedId() { return bedId; }
    public BedType getType() { return type; }
    public boolean isVentilation() { return ventilation; }
    public String getPatientCnp() { return patientCnp; }
    public int getSeverity() { return severity; }
    public String getDiagnosis() { return diagnosis; }
}
