package org.example.domain;

public class Car {
    private final int id;
    private final String name;
    private final int base_price;
    private final String descriere;
    private MasinaStatus status;
    private String rejected_reason;

    public Car(int id, String name, int base_price, String descriere, MasinaStatus status, String rejected_reason) {
        this.id = id;
        this.name = name;
        this.base_price = base_price;
        this.descriere = descriere;
        this.status = status;
        this.rejected_reason = rejected_reason;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBase_price() {
        return base_price;
    }

    public String getDescriere() {
        return descriere;
    }

    public MasinaStatus getStatus() {
        return status;
    }

    public String getRejected_reason() {
        return rejected_reason;
    }

//    public void setStatus(MasinaStatus status) {
//        this.status = status;
//    }

    public void setRejected_reason(String rejected_reason) {
        this.rejected_reason = rejected_reason;
    }
}
