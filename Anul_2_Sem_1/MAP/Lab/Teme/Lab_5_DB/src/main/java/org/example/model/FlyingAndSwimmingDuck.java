package org.example.model;

public class FlyingAndSwimmingDuck extends Duck implements Zburator, Inotator {
    public FlyingAndSwimmingDuck(Long id, String username, String email, String password,
                                 double viteza, double rezistenta) {
        super(id, username, email, password, TipRata.FLYING_AND_SWIMMING, viteza, rezistenta);
    }

    @Override
    public void zboara() {
        System.out.println("Rata zboara cu viteza: " + viteza);
    }

    @Override
    public void inoata() {
        System.out.println("Rata inoata cu viteza: " + viteza);
    }
}
