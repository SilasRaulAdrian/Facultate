package org.example.domain;

public class AlertRow {
    private final String locality;
    private final String river;
    private final double riverLevel;
    private final double cmdr;
    private final double cma;
    private final RiskLevel risk;

    public AlertRow(String locality, String river, double riverLevel, double cmdr, double cma, RiskLevel risk) {
        this.locality = locality;
        this.river = river;
        this.riverLevel = riverLevel;
        this.cmdr = cmdr;
        this.cma = cma;
        this.risk = risk;
    }

    public String getLocality() {
        return locality;
    }

    public String getRiver() {
        return river;
    }

    public double getRiverLevel() {
        return riverLevel;
    }

    public double getCmdr() {
        return cmdr;
    }

    public double getCma() {
        return cma;
    }

    public RiskLevel getRisk() {
        return risk;
    }
}
